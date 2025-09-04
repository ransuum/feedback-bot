package org.task.feedbackbot.utils.stateselection;

import jakarta.annotation.Nullable;
import org.task.feedbackbot.service.UserService;
import org.task.feedbackbot.utils.messages.MessageSender;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public abstract class BaseStateSelection implements StateSelection {

    protected final UserService userService;
    protected final MessageSender messageSender;

    protected BaseStateSelection(UserService userService, MessageSender messageSender) {
        this.userService = userService;
        this.messageSender = messageSender;
    }

    protected void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    protected void sendMessage(Long chatId, String text, @Nullable ReplyKeyboard replyKeyboard) {
        messageSender.sendMessage(chatId, text, replyKeyboard);
    }
}
