package com.senate.stock.discord.bot.channels

import net.dv8tion.jda.api.entities.TextChannel

interface IUpdateTextChannel {

    fun getUpdateChannels(): List<TextChannel>

    fun sendAll(msg: String) = getUpdateChannels()
            .forEach { channel -> channel.sendMessage(msg).queue() }
}