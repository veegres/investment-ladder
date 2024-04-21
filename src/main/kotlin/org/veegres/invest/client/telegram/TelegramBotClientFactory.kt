package org.veegres.invest.client.telegram

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication
import org.telegram.telegrambots.meta.generics.TelegramClient


@Factory
class TelegramBotClientFactory(val config: TelegramBotConfiguration) {

    @Singleton
    fun telegramClient(): TelegramClient {
        return OkHttpTelegramClient(config.bot.token);
    }

    @Singleton
    fun telegramApplication(): TelegramBotsLongPollingApplication {
        return TelegramBotsLongPollingApplication()
    }
}