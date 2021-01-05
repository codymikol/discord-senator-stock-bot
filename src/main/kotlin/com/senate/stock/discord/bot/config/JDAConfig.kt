package com.senate.stock.discord.bot.config

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JDAConfig(val botConfig: BotConfig) {

    /**
     * Returns JDA with a Connected Status.
     */
    @Bean
    fun jda(): JDA = JDABuilder.createDefault(botConfig.botToken)
            .setActivity(Activity.watching("\$\$\$ Corruption"))
            .disableCache(CacheFlag.ACTIVITY)
            .enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
            .setLargeThreshold(50)
            .build().apply {
                awaitReady()
            }

}