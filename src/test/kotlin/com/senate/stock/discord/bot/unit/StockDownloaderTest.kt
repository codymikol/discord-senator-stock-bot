package com.senate.stock.discord.bot.unit

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.senate.stock.discord.bot.config.BotConfig
import com.senate.stock.discord.bot.data.Contents
import com.senate.stock.discord.bot.date.AppDateProvider
import com.senate.stock.discord.bot.poller.StockDownloader
import io.mockk.clearMocks
import io.mockk.mockk
import okhttp3.OkHttpClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource

class StockDownloaderTest {

    val mockBotConfig: BotConfig = mockk()

    val mockAppDateProvider: AppDateProvider = mockk()

    val mockOkHttpClient: OkHttpClient = mockk()

    lateinit var testStockDownloader: StockDownloader

    @BeforeEach
    fun setup() {
        clearMocks(mockBotConfig, mockAppDateProvider, mockOkHttpClient)
        testStockDownloader = StockDownloader(mockBotConfig,
                ObjectMapper().apply { registerModule(KotlinModule()) },
                mockAppDateProvider,
                mockOkHttpClient,
                XmlMapper().apply { registerModule(KotlinModule()) }
        )
    }

    @Test
    fun `test parseLocalDateFromString passing file name expecting LocalDate parsed from file`() {
        val fileDate = testStockDownloader.parseLocalDateFromString("data/transaction_report_for_09_12_2017.json")
        assert(fileDate != null) { "File date was not null" }
        assert(fileDate?.year == 2017) { "Year was not 2017" }
        assert(fileDate?.monthValue == 9) { "Month was not 09" }
        assert(fileDate?.dayOfMonth == 12) { "Day of month was not 11" }
    }
}