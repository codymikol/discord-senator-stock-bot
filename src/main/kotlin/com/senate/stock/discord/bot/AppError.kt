package com.senate.stock.discord.bot

sealed class AppError(val message: String) {

    class NoReportForDate(date: String): AppError("No Report found for date $date")

    class InvalidDateFormat(date: String, userId: Long): AppError("Sorry <@!$userId> I couldn't parse the date you provided $date make sure it is in the format YYYY-MM-DD")

}
