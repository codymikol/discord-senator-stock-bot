package com.senate.stock.discord.bot.listener

import arrow.core.Either
import arrow.core.flatMap
import com.senate.stock.discord.bot.AppError
import com.senate.stock.discord.bot.poller.ReportFormatter
import com.senate.stock.discord.bot.poller.StockDownloader
import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.OffsetDateTime

@Component
class DateRequestListener(
        val stockDownloader: StockDownloader,
        val reportFormatter: ReportFormatter,
) : ListenerAdapter() {

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if (!event.author.isBot && event.message.mentionedMembers.any { event.jda.selfUser.asTag == it.user.asTag }) {
            parseDateFromMessageOrError(event.message)
                    .flatMap { parsedDate -> stockDownloader.getUpdate(parsedDate) }
                    .map { senatorData -> reportFormatter.getTransactionsTableString(senatorData) }
                    .fold({ it.message }, { it })
                    .chunked(2000)
                    .forEach { messageChunk ->
                        // Send message in requires 2000 size chunks.
//                        event.channel.sendMessage(messageChunk).queue()
                        event.channel.sendMessage(MessageEmbed(null, "Daily Report", messageChunk, EmbedType.RICH, OffsetDateTime.now(),
                                1, null, null, null, null, null, null, listOf())).queue()
                    }

//            event.channel.sendMessage("Here is a link! [country codes](https://countrycode.org/)").queue()
        }
        super.onGuildMessageReceived(event)
    }

    fun parseDateFromMessageOrError(message: Message): Either<AppError, LocalDate> = message.contentRaw.let { messageStr ->
        messageStr.substring(messageStr.indexOf(">") + 1, messageStr.length).trim()
                .let { dateString ->
                    try {
                        Either.Right(LocalDate.parse(dateString))
                    } catch (e: Exception) {
                        Either.Left(AppError.InvalidDateFormat(dateString, message.author.idLong))
                    }
                }
    }
}