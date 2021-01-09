package com.senate.stock.discord.bot.channels

import com.senate.stock.discord.bot.config.BotConfig
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import org.springframework.stereotype.Component

@Component
class ChannelManager(
        val jda: JDA,
        val botConfig: BotConfig) : IUpdateChannelProvider {

    init {
        createGuildChannelsIfNotExist()
    }

    /**
     * Automatically creates text channels if not existing in each server.
     */
    private final fun createGuildChannelsIfNotExist() {
        // Create channels in each guild if they do not exist.
        jda.guilds.forEach { guild ->
            guild.textChannels.filter { textChannel -> textChannel.name == botConfig.postInChannel }
                    .apply {
                        if (this.isEmpty()) {
                            guild.createTextChannel(botConfig.postInChannel).queue()
                        }
                    }
        }
    }

    override fun getUpdateChannel(): IUpdateTextChannel = object : IUpdateTextChannel {
        override fun getUpdateChannels(): List<TextChannel> = jda.guilds.flatMap { guild ->
            guild.textChannels.filter { textChannel -> textChannel.name == botConfig.postInChannel }
        }

        override fun sendAll(msg: MessageEmbed) = getUpdateChannels()
                .forEach { channel -> channel.sendMessage(msg).queue() }
    }
}