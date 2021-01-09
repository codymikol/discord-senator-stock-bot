package com.senate.stock.discord.bot.unit

import com.senate.stock.discord.bot.channels.ChannelManager
import com.senate.stock.discord.bot.config.BotConfig
import io.mockk.*
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.TextChannel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ChannelManagerTest {

    lateinit var channelManager: ChannelManager

    val mockJda: JDA = mockk()

    val mockBotConfig: BotConfig = mockk()

    val mockGuild: Guild = mockk()

    val mockTextChannel: TextChannel = mockk()

    @BeforeEach
    fun setUp() {
        clearMocks(mockJda, mockGuild, mockBotConfig, mockTextChannel)
        every { mockBotConfig.postInChannel } returns "test-channel"
        every { mockGuild.createTextChannel(any()) } returns mockk(relaxed = true)
        every { mockJda.guilds } returns listOf(mockGuild)
        every { mockGuild.textChannels } returns listOf()
    }

    @Test
    fun `test channel manager expect creates new text channel in guild on init`() {
        channelManager = ChannelManager(mockJda, mockBotConfig)
        verify(exactly = 1) { mockGuild.createTextChannel(eq("test-channel")) }
    }

    @Test
    fun `test channel manager part of a guild with a channel that is not test-channel expecting channel created`() {
        every { mockTextChannel.name } returns "foobar"
        every { mockGuild.textChannels } returns listOf(mockTextChannel)
        channelManager = ChannelManager(mockJda, mockBotConfig)
        verify(exactly = 1) { mockGuild.createTextChannel(eq("test-channel")) }
    }

    @Test
    fun `test channel manager not part of any guild expecting text channel not created`() {
        every { mockJda.guilds } returns listOf()
        channelManager = ChannelManager(mockJda, mockBotConfig)
        verify(exactly = 0) { mockGuild.createTextChannel(any()) }
    }

    @Test
    fun `test channel manager part of a guild with existing channel expecting channel not created`() {
        every { mockTextChannel.name } returns "test-channel"
        every { mockGuild.textChannels } returns listOf(mockTextChannel)
        channelManager = ChannelManager(mockJda, mockBotConfig)
        verify(exactly = 0) { mockGuild.createTextChannel(eq("test-channel")) }
    }

}