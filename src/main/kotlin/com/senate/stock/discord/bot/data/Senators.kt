package com.senate.stock.discord.bot.data

data class Senators(
        val first_name: String?,
        val last_name: String?,
        val office: String?,
        val date_recieved: String?,
        val transactions: List<Transactions>,
)