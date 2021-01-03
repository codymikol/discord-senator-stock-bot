package com.senate.stock.discord.bot

import com.senate.stock.discord.bot.users.ListeningUsers
import net.dv8tion.jda.api.entities.User
import org.springframework.stereotype.Component

/**
 * Broadcasts a text message to all configured users.
 */
@Component
class Broadcaster(val listeningUsers: ListeningUsers) {

    /**
     * Sends a message via private channel for each configured user.
     */
    fun broadcast(message: String) = listeningUsers.users.forEach { user -> user.queuePrivateMessage(message) }
}

fun User.queuePrivateMessage(message: String) = this.openPrivateChannel().complete().sendMessage(message).queue()