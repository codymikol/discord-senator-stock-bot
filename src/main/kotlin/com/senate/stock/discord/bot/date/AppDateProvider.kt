package com.senate.stock.discord.bot.date

import com.senate.stock.discord.bot.config.BotConfig
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.time.*
import java.time.format.DateTimeFormatter

// default all timezones to the NYSE exchange zone.
val NYSE_ZONE: ZoneId = ZoneId.of("America/New_York")

val FILE_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

@Component
class AppDateProvider(
        val botConfig: BotConfig) {

    fun getLastReportedDate(): LocalDate = LocalDate.parse(botConfig.dateStore.file.readText())

    fun setLastReportedDate(localDate: LocalDate) = botConfig.dateStore.file.writeText(localDate.format(FILE_DATE_FORMAT))

    fun getNowDate(): LocalDate = getNow().toLocalDate()

    private fun getNow(): ZonedDateTime = LocalDateTime.now().atZone(NYSE_ZONE.normalized())

    fun isLastReportedDateToday(): Boolean = getNowDate() == getLastReportedDate()
}

/**
 * Extension method for determining if the DayOfWeek between Mon-Fri
 */
fun LocalDate.isTradingDay(): Boolean = this.dayOfWeek
        .let { dow -> dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY }