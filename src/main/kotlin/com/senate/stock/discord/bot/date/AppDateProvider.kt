package com.senate.stock.discord.bot.date

import com.senate.stock.discord.bot.config.BotConfig
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.concurrent.atomic.AtomicReference

val nyse_zone: ZoneOffset = ZoneOffset.of("America/New_York")

@Component
class AppDateProvider(val botConfig: BotConfig) {

    private val lastReportedDate: AtomicReference<LocalDate> = AtomicReference(LocalDate.parse(botConfig.lastRecievedOn))

    fun getLastReportedDate(): LocalDate = lastReportedDate.get()

    fun getNow(): ZonedDateTime = LocalDateTime.now().atZone(nyse_zone.normalized())


}