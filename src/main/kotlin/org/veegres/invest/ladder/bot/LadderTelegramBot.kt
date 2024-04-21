package org.veegres.invest.ladder.bot

import io.micronaut.context.annotation.Context
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.message.Message
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.generics.TelegramClient
import org.veegres.invest.client.telegram.TelegramBotConfiguration
import org.veegres.invest.ladder.dto.AccountDto
import org.veegres.invest.ladder.job.LadderStepJob
import org.veegres.invest.ladder.service.LadderService


@Context
class LadderTelegramBot(
    private val config: TelegramBotConfiguration,
    private val telegramClient: TelegramClient,
    private val telegramApplication: TelegramBotsLongPollingApplication,
    private val ladderService: LadderService
) : LongPollingSingleThreadUpdateConsumer {

    @PostConstruct
    fun postConstruct() {
        telegramApplication.registerBot(config.bot.token, this)
    }

    override fun consume(update: Update) {
        if (update.hasMessage() && update.message.hasText()) {
            val send = sendMessageToChat(update.message.chatId)
            val response = findResponse(update.message)
            response?.let(send)
        }
    }

    private fun findResponse(message: Message): String? {
        val type = message.chat.type
        val user = message.chat.id
        val text = message.text
        LOG.info("User ($user) in $type: \"$text\"")
        val response =
            if (message.isCommand) {
                handleCommands(text)
            } else if (type == "group" && text.contains(config.bot.name)) {
                val trimmedName = text.removePrefix(config.bot.name)
                handleMessages(trimmedName)
            } else if (type != "group") {
                handleMessages(text)
            } else
                null
        LOG.info("Bot: $response")
        return response
    }

    private fun handleCommands(origin: String): String {
        val command = origin.drop(1)
        return when (command) {
            "start" -> "Hello! Thanks for chatting with me!"
            "help" -> "I am a bot to help you invest with laddering strategy."
            "custom" -> "This is a custom command"
            "tinkoffaccounts" -> getAccounts()
            else -> "I do not know to handle that kind of command"
        }
    }

    private fun getAccounts(): String {
        val transformer: (AccountDto) -> String = { account -> "${account.id}: ${account.status}" }
        val accounts = ladderService.getAccounts()
        return accounts.joinToString(separator = "\n", transform = transformer)
    }

    private fun handleMessages(origin: String): String {
        val text = origin.trim().lowercase()
        return if (text.contains("hello")) "Wazzup!"
        else if (text.contains("how are you")) "I am good!"
        else if (text.contains("i love you")) "I don't care!"
        else if (text.contains("/tinkoffaccounts")) getAccounts()
        else "I do not understand what your wrote..."
    }

    private fun sendMessageToChat(chatId: Long): (String) -> Unit {
        return { massage ->
            val message = SendMessage.builder().chatId(chatId).text(massage).build()
            try {
                telegramClient.execute(message)
            } catch (e: TelegramApiException) {
                LOG.error(e.stackTraceToString())
            }
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LadderStepJob::class.java)
    }
}