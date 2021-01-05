package com.senate.stock.discord.bot.data

data class Transactions(
        val transaction_date: String?,
        val owner: String?,
        val ticker: String?,
        val asset_description: String?,
        val asset_type: String?,
        val type: String?,
        val amount: String?,
        val rawTicker: String?
)