package org.task.feedbackbot.models.events;

import jakarta.annotation.Nullable;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public record SendMessageEvent(Long chatId, String text, @Nullable ReplyKeyboard replyKeyboard) {
}
