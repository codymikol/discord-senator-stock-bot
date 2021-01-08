package com.senate.stock.discord.bot

import com.senate.stock.discord.bot.channels.IUpdateChannelProvider
import com.senate.stock.discord.bot.poller.ReportFormatter
import com.senate.stock.discord.bot.poller.StockDownloader
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

const val CHECK_EVERY: Long = 1000 * 60 * 60 * 12

@Component
class ReportTimer(
        val channelProvider: IUpdateChannelProvider,
        val stockDownloader: StockDownloader,
        val reportFormatter: ReportFormatter,
) {

    val timer: TimerTask = Timer("REPORT_TIMER").scheduleAtFixedRate(0L, 10 * 1000L) {

//        stockDownloader.getUpdate(LocalDate.parse("2020-12-31"))
//                .map { senatorsUpdate ->
//                    val tableString = reportFormatter.getTransactionsTableString(senatorsUpdate)
//                    println("Running timed task...")
//                    channelProvider.getUpdateChannel().sendAll(tableString)
//                }
        runBlocking { delay(60 * 1000) }
    }

}