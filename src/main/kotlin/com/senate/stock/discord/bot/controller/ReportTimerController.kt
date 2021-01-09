package com.senate.stock.discord.bot.controller

import com.senate.stock.discord.bot.ReportTimer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ReportTimerController(val reportTimer: ReportTimer) {

    @PostMapping("update")
    fun runUpdate(): ResponseEntity<String> = ResponseEntity.ok(reportTimer.doNextUpdate()
            .fold({ "Failed to run update, please check logs" }, { "OK" }))
}