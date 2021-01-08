package com.senate.stock.discord.bot.config

import com.senate.stock.discord.bot.listener.DateRequestListener
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
    fun jda(dateRequestListener: DateRequestListener): JDA = JDABuilder.createDefault(botConfig.botToken)
            .setActivity(Activity.watching("\$\$\$ Corruption"))
            .disableCache(CacheFlag.ACTIVITY)
            .enableIntents(GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
            .setLargeThreshold(50)
            .addEventListeners(dateRequestListener)
            .build().apply {
                awaitReady()
            }

}