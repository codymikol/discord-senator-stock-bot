package com.senate.stock.discord.bot.channels

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.TextChannel
import org.springframework.stereotype.Component

@Component
class ChannelManager(val jda: JDA) : IUpdateChannelProvider {

    init {
       createGuildChannelsIfNotExist()
    }

    /**
     * Automatically creates text channels if not existing in each server.
     */
    private final fun createGuildChannelsIfNotExist() {
        // Create channels in each guild if they do not exist.
        UpdateChannel.values().forEach { updateChannel ->
            jda.guilds.forEach { guild ->
                guild.textChannels.filter { textChannel -> textChannel.isUpdateChannel(updateChannel) }
                        .apply {
                            if (this.isEmpty()) {
                                guild.createTextChannel(updateChannel.name).queue()
                            }
                        }
            }
        }
    }

    override fun getUpdateChannel(channel: UpdateChannel): IUpdateTextChannel = object : IUpdateTextChannel {
        override fun getUpdateChannels(): List<TextChannel> = jda.guilds.flatMap { guild ->
            guild.textChannels.filter { textChannel -> textChannel.isUpdateChannel(channel) }
        }
    }
}