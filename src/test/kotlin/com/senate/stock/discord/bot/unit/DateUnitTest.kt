package com.senate.stock.discord.bot.unit

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneId

class DateUnitTest {

    @Test
    fun `test get date parts `() {
        val nowDateTime = LocalDateTime.now(ZoneId.of("America/New_York"))
        println(nowDateTime.dayOfWeek)
        println(nowDateTime.month)
        println(nowDateTime.year)
        println(nowDateTime.dayOfMonth)
    }
}