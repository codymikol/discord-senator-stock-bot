package com.senate.stock.discord.bot.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.properties")
class BotConfig {

    val startTime: Long = System.currentTimeMillis()

    @Value( "\${botToken}" )
    lateinit var botToken: String

    @Value("\${dataDownloadHost}")
    lateinit var dataDownloadEndpoint: String

    @Value("\${postInChannel}")
    lateinit var postInChannel: String

    @Value("\${fileDirectoryHost}")
    lateinit var fileDirectoryHost: String

    @Value("\${lastRecievedOn}")
    lateinit var lastRecievedOn: String
}