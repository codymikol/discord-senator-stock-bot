package com.senate.stock.discord.bot.date

import com.senate.stock.discord.bot.config.BotConfig
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicReference

val nyse_zone: ZoneId = ZoneId.of("America/New_York")

val FILE_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

@Component
class AppDateProvider(
        val botConfig: BotConfig,
        val dateStoreResource: ClassPathResource = ClassPathResource("datestore.txt")) {

    fun getLastReportedDate(): LocalDate = LocalDate.parse(dateStoreResource.file.readText())

    fun setLastReportedDate(localDate: LocalDate) = dateStoreResource.file.writeText(localDate.format(FILE_DATE_FORMAT))

    fun getNowDate(): LocalDate = getNow().toLocalDate()

    fun getNow(): ZonedDateTime = LocalDateTime.now().atZone(nyse_zone.normalized())

}