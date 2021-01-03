package com.senate.stock.discord.bot.config

import com.senate.stock.discord.bot.users.ListeningUsers
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
    fun jda(): JDA = JDABuilder.createDefault("token...")
            .setActivity(Activity.watching("\$\$\$ Corruption"))
            .disableCache(CacheFlag.ACTIVITY)
            .disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING)
            .setLargeThreshold(50)
            .build().apply {
                awaitReady()
            }

    /**
     * Ussers listening to poll watched events.
     */
    @Bean
    fun listeningUsers(jda: JDA): ListeningUsers = ListeningUsers(botConfig.userTags.mapNotNull {
        jda.getUserByTag(it)
    })
}