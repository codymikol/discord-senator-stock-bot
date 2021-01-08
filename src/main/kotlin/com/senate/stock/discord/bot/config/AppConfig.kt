package com.senate.stock.discord.bot.config

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun okHttpClient(): OkHttpClient = OkHttpClient()
}