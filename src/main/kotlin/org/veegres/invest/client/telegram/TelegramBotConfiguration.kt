package org.veegres.invest.client.telegram

import io.micronaut.context.annotation.ConfigurationProperties


@ConfigurationProperties("client.telegram")
data class TelegramBotConfiguration(
    val bot: Bot
) {

    @ConfigurationProperties("bot")
    data class Bot(
        val token: String,
        val name: String
    )
}