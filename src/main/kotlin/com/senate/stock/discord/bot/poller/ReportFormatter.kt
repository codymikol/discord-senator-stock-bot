package com.senate.stock.discord.bot.poller

import com.senate.stock.discord.bot.data.Senators
import net.dv8tion.jda.api.entities.MessageEmbed
import org.springframework.stereotype.Component

val URL_PATTERN: Regex = "[(http(s)?):\\/\\/(www\\.)?a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)".toRegex()

@Component
class ReportFormatter {

    fun getTransactionsTableString(senators: List<Senators>): String = StringBuilder("").apply {
        senators.filter { it.transactions.isNotEmpty() }
                .forEach { senator ->
                    this.append("| Senator | ${(senator.first_name + " " + senator.last_name)} |\n")
                    this.append("|  Ticker   | Action | Price Range | Date |\n")
                    senator.transactions.forEach { transaction ->
                        this.append("|   [${getRawTicker(transaction.ticker ?: "--")}]${transaction.ticker?.let { "(${getAnchoredHyperLinkOrNull(it)})" }}   | ${getBuyOrSell(transaction.type ?: "")} | ${transaction.amount} | ${transaction.transaction_date} |\n")
                    }
                }
    }.toString()

    fun getTransactionsTableFields(senators: List<Senators>): List<MessageEmbed.Field> = ArrayList<MessageEmbed.Field>().apply {
        senators.filter { it.transactions.isNotEmpty() }
                .forEach { senator ->
                    this.add(MessageEmbed.Field("", "| Senator | ${(senator.first_name + " " + senator.last_name)} |\n", false))
                    this.add(MessageEmbed.Field("", "|  Ticker   | Action | Price Range | Date |\n", false))
                    senator.transactions.forEach { transaction ->
                        this.add(MessageEmbed.Field("", "|   [${getRawTicker(transaction.ticker ?: "--")}]${transaction.ticker?.let { "(${getAnchoredHyperLinkOrNull(it)})" }}   | ${getBuyOrSell(transaction.type ?: "")} | ${transaction.amount} | ${transaction.transaction_date} |\n" , false))
                    }
                }
    }


    fun getRawTicker(tickerLink: String): String = (tickerLink.indexOf(">") to tickerLink.indexOf("</a>"))
            .let { anchorStartEnd ->
                when (anchorStartEnd.first != -1 && anchorStartEnd.second != -1) {
                    true -> tickerLink.substring(anchorStartEnd.first + 1, anchorStartEnd.second)
                    else -> tickerLink
                }
            }

    fun isPurchase(type: String): Boolean = type.contains("Purchase")

    fun getBuyOrSell(type: String): String = when (isPurchase(type)) {
        true -> "\uD83D\uDE80 Buy"
        else -> "\uD83D\uDCB0 Sell"
    }

    fun getAnchoredHyperLinkOrNull(text: String) = URL_PATTERN.findAll(text).map { it.value }.firstOrNull()
}