package com.senate.stock.discord.bot.channels

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel
import org.springframework.stereotype.Component

@Component
class DailyUpdateChannel(val jda: JDA) {

    fun getUpdateChannels(): List<TextChannel> = jda.guilds.flatMap { guild ->
        guild.textChannels.filter { channel -> channel.name == "daily-update" }.apply {
            // attempts to create an update text channel if one does not exist.
            if (this.isEmpty()) {
                guild.createTextChannel("daily-update").queue()
            }
        }
    }
}