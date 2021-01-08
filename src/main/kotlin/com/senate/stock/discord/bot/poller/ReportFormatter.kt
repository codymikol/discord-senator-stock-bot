package com.senate.stock.discord.bot.poller

import com.senate.stock.discord.bot.data.Senators
import org.springframework.stereotype.Component

@Component
class ReportFormatter {

    fun getTransactionsTableString(senators: List<Senators>): String = StringBuilder("").apply {
        senators.forEach { senator ->
            this.append("| Senator | ${senator.office ?: (senator.first_name + " " + senator.last_name)} |\n")
            this.append("| Ticker  | Action | Price Range | Date |\n")
            senator.transactions.forEach { transaction ->
                this.append("| ${getRawTicker(transaction.ticker ?: "--")} | ${getBuyOrSell(transaction.type ?: "")} | ${transaction.amount} | ${transaction.transaction_date} |\n")
            }
        }
    }.toString()

    fun getRawTicker(tickerLink: String): String = (tickerLink.indexOf(">") to tickerLink.indexOf("</a>"))
            .let { anchorStartEnd ->
                when (anchorStartEnd.first != -1 && anchorStartEnd.second != -1) {
                    true -> tickerLink.substring(anchorStartEnd.first + 1, anchorStartEnd.second)
                    else -> tickerLink
                }
            }

    fun isPurchase(type: String): Boolean = type.contains("Purchase")

    fun getBuyOrSell(type: String): String = when (isPurchase(type)) {
        true -> "Buy"
        else -> "Sell"
    }
}