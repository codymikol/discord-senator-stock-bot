package com.senate.stock.discord.bot.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@PropertySource("classpath:application.properties")
class BotConfig {

    val startTime: Long = System.currentTimeMillis()

    /**
     * Comma separated array of user tags.
     */
    lateinit var userTags: Array<String>

}