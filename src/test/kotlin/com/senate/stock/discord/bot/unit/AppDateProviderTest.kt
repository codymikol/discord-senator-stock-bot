package com.senate.stock.discord.bot.unit

import com.senate.stock.discord.bot.config.BotConfig
import com.senate.stock.discord.bot.date.AppDateProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class AppDateProviderTest {

    lateinit var appDateProvider: AppDateProvider

    val mockBotConfig: BotConfig = mockk()

    @BeforeEach
    fun setUp() {
        every { mockBotConfig.dateStore } returns ClassPathResource("datestore.txt")
        appDateProvider = AppDateProvider(mockBotConfig)
    }

    @Test
    fun `test get date parts `() {
        val nowDateTime = LocalDateTime.now(ZoneId.of("America/New_York"))
        println(nowDateTime.dayOfWeek)
        println(nowDateTime.month)
        println(nowDateTime.year)
        println(nowDateTime.dayOfMonth)
    }

    @Test
    fun `test write date to file`() {
        val file = ClassPathResource("datestore.txt").file
        file.writeText("2020-10-01")
    }

    @Test
    fun `test get and set last reported date`() {
        val date1 = LocalDate.parse("2020-01-03")
        val date2 = LocalDate.parse("2021-10-03")
        appDateProvider.setLastReportedDate(date1)
        assert(appDateProvider.getLastReportedDate() == date1) { "Local Date was not updated to 2020-01-03" }
        appDateProvider.setLastReportedDate(date2)
        assert(appDateProvider.getLastReportedDate() == date2) { "Local Date was not updated to 2021-10-03" }
    }

}