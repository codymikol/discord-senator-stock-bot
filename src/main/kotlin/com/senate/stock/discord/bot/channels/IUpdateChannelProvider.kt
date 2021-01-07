package com.senate.stock.discord.bot.channels

interface IUpdateChannelProvider {
    fun getUpdateChannel(): IUpdateTextChannel
}