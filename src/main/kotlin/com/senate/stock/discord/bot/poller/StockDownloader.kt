package com.senate.stock.discord.bot.poller

import arrow.core.Either
import arrow.core.flatMap
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.senate.stock.discord.bot.AppError
import com.senate.stock.discord.bot.config.BotConfig
import com.senate.stock.discord.bot.data.Contents
import com.senate.stock.discord.bot.data.Senators
import com.senate.stock.discord.bot.date.AppDateProvider
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MM_dd_yyyy")

@Component
class StockDownloader(
        val botConfig: BotConfig,
        val objectMapper: ObjectMapper,
        val appDateProvider: AppDateProvider,
        val okHttpClient: OkHttpClient,
        val xmlMapper: XmlMapper = XmlMapper().apply {
            registerModule(KotlinModule())
        }
) {


    fun getFileDirectoryContents(): List<Contents> = xmlMapper.readValue(okHttpClient.newCall(Request.Builder()
            .url("${botConfig.fileDirectoryHost}/filemap.xml")
            .build()).execute().body()?.let { String(it.bytes()) }, object : TypeReference<List<Contents>>() {})

    fun normalizeContents(contents: List<Contents>): Set<LocalDate> = contents.mapNotNull { content ->
        content.Key?.let { parseLocalDateFromString(it) }
    }.toSet()


    // TODO safer regex based way of parsing local date from file string.
    fun parseLocalDateFromString(fileName: String): LocalDate? = fileName.substring(fileName.length - 15, fileName.length - 5)
            .let { substr -> LocalDate.parse(substr, DATE_FORMAT) }


    /**
     * Downloads list of Senators transactions from configured endpoint.
     * todo maybe if the site owner refactors the date format we make a more robust way of parsing
     *
     */
    fun getUpdate(forDate: LocalDate): Either<AppError, List<Senators>> =
            okHttpClient.newCall(Request.Builder()
                    .url("${botConfig.dataDownloadEndpoint}/transaction_report_for_${forDate.format(DATE_FORMAT)}.json")
                    .build())
                    .executeOrNetworkError()
                    .flatMap { response ->
                        when (response.code() == 200) {
                            true -> Either.Right(deserializeResponseBody(response))
                            else -> Either.Left(AppError.NoReportForDate(forDate.format(DATE_FORMAT)))
                        }
                    }


    private fun deserializeResponseBody(response: Response): List<Senators> = objectMapper.readValue(
            response.body()?.let { String(it.bytes()) },
            object : TypeReference<List<Senators>>() {})
}

fun Call.executeOrNetworkError(): Either<AppError, Response> = try {
    Either.Right(this.execute())
} catch (e: Exception) {
    println("Network Exception downloading report..")
    e.printStackTrace()
    Either.Left(AppError.NetworkFailure())
}