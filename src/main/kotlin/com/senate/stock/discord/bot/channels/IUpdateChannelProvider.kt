package com.senate.stock.discord.bot.channels

interface IUpdateChannelProvider {
    fun getUpdateChannel(channel: UpdateChannel): IUpdateTextChannel
}