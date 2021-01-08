package com.senate.stock.discord.bot.unit

import com.senate.stock.discord.bot.poller.ReportFormatter
import org.junit.jupiter.api.Test

class ReportFormatterTest {

    val reportFormatter = ReportFormatter()

    @Test
    fun `test ReportFormatter`() {
        val result = reportFormatter.getAnchoredHyperLinkOrNull("<a href=\"https://finance.yahoo.com/q?s=QCOM\" target=\"_blank\">QCOM</a>")
        assert(result
         == "https://finance.yahoo.com/q?s=QCOM") { "Incorrect result $result" }
    }
}