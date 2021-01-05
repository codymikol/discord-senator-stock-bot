package com.senate.stock.discord.bot

import com.senate.stock.discord.bot.channels.IUpdateChannelProvider
import com.senate.stock.discord.bot.channels.UpdateChannel
import com.senate.stock.discord.bot.poller.ReportFormatter
import com.senate.stock.discord.bot.poller.StockDownloader
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

const val CHECK_EVERY: Long = 1000 * 60 * 60 * 12

@Component
class ReportTimer(
        val channelProvider: IUpdateChannelProvider,
        val stockDownloader: StockDownloader,
        val reportFormatter: ReportFormatter,
) {

//    val tableRenderer = TableBuilder()

    val timer: TimerTask = Timer("REPORT_TIMER").scheduleAtFixedRate(0L, 10 * 1000L) {

        val tableString = reportFormatter.sendTransactionsTable(stockDownloader.getUpdate(""))
        println("Running timed task...")
        channelProvider.getUpdateChannel(UpdateChannel.daily_update)
                .sendAll(tableString)
        runBlocking { delay(60 * 1000) }
    }

}