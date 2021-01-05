package com.senate.stock.discord.bot.data

import com.fasterxml.jackson.annotation.JsonProperty

data class Contents(
        @JsonProperty("Key")
        var Key: String?
)