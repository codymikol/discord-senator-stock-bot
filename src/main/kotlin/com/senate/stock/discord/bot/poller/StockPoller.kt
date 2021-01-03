package com.senate.stock.discord.bot.poller

import com.senate.stock.discord.bot.config.BotConfig
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import org.springframework.stereotype.Component

@Component
class StockPoller(
        val botConfig: BotConfig,
        val jda: JDA) {

    fun sendUpdate() {
//        User.fromId(botConfig.sendToUserId).openPrivateChannel().queue { channel ->
//            channel.sendMessage("Hello John!").queue()
//        }
        jda.getUsersByName("", true)
    }
}