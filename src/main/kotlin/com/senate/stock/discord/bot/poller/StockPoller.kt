package com.senate.stock.discord.bot.poller

import com.senate.stock.discord.bot.channels.DailyUpdateChannel
import com.senate.stock.discord.bot.config.BotConfig
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.concurrent.atomic.AtomicReference


@Component
class StockPoller(
        val stockDownloader: StockDownloader,
        val dailyUpdateChannel: DailyUpdateChannel) {

    val lastUpdateReference: AtomicReference<OffsetDateTime?> = AtomicReference(null)


    fun shouldCheckForUpdate(): Boolean = lastUpdateReference.get().let { lastUpdate ->
        lastUpdate == null || needsMorningUpdate()
    }

    fun wasUpdatedToday(): Boolean = lastUpdateReference.get()
            ?.let { lastUpdated -> lastUpdated.dayOfYear == nowNewYorkZone().dayOfYear } ?: false

    /**
     * Preform morning update once a day after the market opens.
     */
    fun needsMorningUpdate(): Boolean = !wasUpdatedToday() && isAfterStockOpen()

    /**
     * Checks if after stock market opens.
     */
    fun isAfterStockOpen(): Boolean = nowNewYorkZone().hour >= 9


    fun nowNewYorkZone(): OffsetDateTime = LocalDateTime.now().atOffset(ZoneOffset.of("America/New_York"))
}