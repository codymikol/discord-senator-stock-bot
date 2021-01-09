package com.senate.stock.discord.bot.poller

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.rightIfNotNull
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
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
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MM_dd_yyyy")

val DATE_PATTERN: Regex = """\d{2}_\d{2}_\d{4}""".toRegex()

@Component
class StockDownloader(
        val botConfig: BotConfig,
        val objectMapper: ObjectMapper,
        val appDateProvider: AppDateProvider,
        val okHttpClient: OkHttpClient,
        val xmlMapper: XmlMapper = XmlMapper().apply {
            registerModule(KotlinModule())
            configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
        }
) {

    fun getFileDirectoryContents(): Either<AppError, List<Contents>> = okHttpClient.newCall(Request.Builder()
            .url(botConfig.fileDirectoryHost)
            .build())
            .executeOrNetworkError()
            .map { response ->
                xmlMapper.readValue(response.body()?.let { String(it.bytes()) },
                        object : TypeReference<List<Contents>>() {})
            }

    fun parseDates(contents: List<Contents>): Set<LocalDate> = contents
            .mapNotNull { content -> content.Key?.let { parseLocalDateFromString(it) } }.toSet()


    /**
     * Gets next report available for download.
     */
    fun getNextDate(): Either<AppError, LocalDate> = getFileDirectoryContents()
            .map { contents -> parseDates(contents).filter { it.isAfter(appDateProvider.getLastReportedDate()) } }
            .map { dates -> dates.sortedByDescending { it } }
            .flatMap { dates -> dates.firstOrNull().rightIfNotNull { AppError.NoReportForDate("") } }

    fun parseLocalDateFromString(fileName: String): LocalDate? = DATE_PATTERN.findAll(fileName)
            .map { LocalDate.parse(it.value, DATE_FORMAT) }
            .firstOrNull()


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

    /**
     * Get's next date provided from File Directory XML File. If available, downloads report for that date.
     */
    fun getNextUpdate(): Either<AppError, List<Senators>> = getNextDate()
            .flatMap { nextDate ->
                getUpdate(nextDate).map { senatorData ->
                    senatorData.apply { appDateProvider.setLastReportedDate(nextDate) }
                }
            }

    private fun deserializeResponseBody(response: Response): List<Senators> = objectMapper.readValue(
            response.body()?.let { String(it.bytes()) },
            object : TypeReference<List<Senators>>() {})

    companion object {

        fun parseLocalDateOrNull(str: String, formatter: DateTimeFormatter): LocalDate? = try {
            LocalDate.parse(str, formatter)
        } catch (e: Exception) {
            null
        }
    }
}

fun Call.executeOrNetworkError(): Either<AppError, Response> = try {
    Either.Right(this.execute())
} catch (e: Exception) {
    println("Network Exception downloading report..")
    e.printStackTrace()
    Either.Left(AppError.NetworkFailure())
}
