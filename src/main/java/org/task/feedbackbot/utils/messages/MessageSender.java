package org.task.feedbackbot.utils.messages;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public sealed interface MessageSender permits TelegramMessageSender {
    void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard);

    default void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
    }
}
