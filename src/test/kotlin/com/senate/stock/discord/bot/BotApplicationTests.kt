package com.senate.stock.discord.bot

import com.senate.stock.discord.bot.poller.DirectoryContentDownloader
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class BotApplicationTests {

	@Autowired
	lateinit var context: WebApplicationContext

	@Test
	fun contextLoads() {
		context.getBean(DirectoryContentDownloader::class.java).getFileContents()
		println("")
	}

}
