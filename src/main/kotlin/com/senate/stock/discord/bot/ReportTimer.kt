package com.senate.stock.discord.bot

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

@Component
class ReportTimer(val broadcaster: Broadcaster) {

    val timer: TimerTask = Timer("REPORT_TIMER").scheduleAtFixedRate(0L, 10 * 1000L) {
        println("Running timed task...")
        broadcaster.broadcast("Hello there!! ")
        runBlocking { delay(60 * 1000) }
    }

}