package com.senate.stock.discord.bot.channels

import net.dv8tion.jda.api.entities.TextChannel

enum class UpdateChannel {
    daily_update,
    hourly_update,
}

fun TextChannel.isUpdateChannel(updateChannel: UpdateChannel): Boolean = this.name == updateChannel.name