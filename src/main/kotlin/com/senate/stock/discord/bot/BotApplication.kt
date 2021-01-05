package com.senate.stock.discord.bot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.DayOfWeek
import java.time.LocalDateTime

@SpringBootApplication
class BotApplication

fun main(args: Array<String>) {
    runApplication<BotApplication>(*args)
}

fun LocalDateTime.isTradingDay(): Boolean = this.dayOfWeek
        .let { dow -> dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY }