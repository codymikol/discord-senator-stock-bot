package com.senate.stock.discord.bot.poller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.senate.stock.discord.bot.config.BotConfig
import com.senate.stock.discord.bot.data.Contents
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component

@Component
class DirectoryContentDownloader(
        val botConfig: BotConfig,
        val xmlMapper: XmlMapper = XmlMapper().apply { this.registerModule(KotlinModule()) },
        val okHttpClient: OkHttpClient = OkHttpClient()) {

    fun getFileContents(): List<Contents> = xmlMapper.readValue(okHttpClient.newCall(Request.Builder()
            .url("${botConfig.fileDirectoryHost}/filemap.xml")
            .build()).execute().body()?.let { String(it.bytes()) }, object: TypeReference<List<Contents>> () {})
}