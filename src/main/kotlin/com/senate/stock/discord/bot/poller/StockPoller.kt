package com.senate.stock.discord.bot.poller

import com.senate.stock.discord.bot.users.ListeningUsers
import net.dv8tion.jda.api.JDA
import org.springframework.stereotype.Component

@Component
class StockPoller(
        val listeningUsers: ListeningUsers,
        val jda: JDA) {

    fun sendUpdate() {

    }
}