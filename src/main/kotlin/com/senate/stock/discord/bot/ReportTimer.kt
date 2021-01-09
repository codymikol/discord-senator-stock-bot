package com.senate.stock.discord.bot

import arrow.core.Either
import com.senate.stock.discord.bot.channels.IUpdateChannelProvider
import com.senate.stock.discord.bot.date.AppDateProvider
import com.senate.stock.discord.bot.date.isTradingDay
import com.senate.stock.discord.bot.poller.ReportFormatter
import com.senate.stock.discord.bot.poller.StockDownloader
import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.entities.MessageEmbed
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

const val CHECK_EVERY: Long = 1000 * 60 * 60 * 6 // 6 Hours.

@Component
class ReportTimer(
        val channelProvider: IUpdateChannelProvider,
        val stockDownloader: StockDownloader,
        val reportFormatter: ReportFormatter,
        val appDateProvider: AppDateProvider,
) {

    val timer: TimerTask = Timer("REPORT_TIMER").scheduleAtFixedRate(0L, CHECK_EVERY) {
        if (appDateProvider.getNowDate().isTradingDay() && !appDateProvider.isLastReportedDateToday()) {
            doNextUpdate()
        }
    }

    fun doNextUpdate(): Either<Unit, Unit> = stockDownloader.getNextUpdate()
            .map { senatorData -> reportFormatter.getTransactionsTableFields(senatorData) }
            .map {
                channelProvider.getUpdateChannel()
                        .sendAll(MessageEmbed(null, "Daily Report", "", EmbedType.RICH, OffsetDateTime.now(),
                                1, null, null, null, null, null, null, it))
            }
            .mapLeft {
                println("Error downloading report: ${it.message}")
                it.message
            }
}