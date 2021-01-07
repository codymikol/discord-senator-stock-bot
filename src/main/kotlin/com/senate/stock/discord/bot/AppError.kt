package com.senate.stock.discord.bot

sealed class AppError(val message: String) {

    class NoReportForDate(date: String): AppError("No Report found for date $date")

}
