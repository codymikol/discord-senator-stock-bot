package com.senate.stock.discord.bot.poller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.senate.stock.discord.bot.config.BotConfig
import com.senate.stock.discord.bot.data.Senators
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component

@Component
class StockDownloader(
        val botConfig: BotConfig,
        val objectMapper: ObjectMapper,
        val okHttpClient: OkHttpClient = OkHttpClient()) {

    /**
     * Downloads list of Senators transactions from configured endpoint.
     */
    fun getUpdate(forDate: String): List<Senators> = objectMapper.readValue(okHttpClient.newCall(Request.Builder()
            .url(botConfig.dataDownloadEndpoint)
            .build()).execute().body()?.let { String(it.bytes()) }, object : TypeReference<List<Senators>>() {})
}