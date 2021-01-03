package com.senate.stock.discord.bot.controller

import com.senate.stock.discord.bot.config.BotConfig
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class StatsController(val botConfig: BotConfig) {


    @GetMapping("/")
    fun get() = ResponseEntity.ok(
            "Hello! Running since ${Instant.ofEpochMilli(botConfig.startTime)}"
    )
}