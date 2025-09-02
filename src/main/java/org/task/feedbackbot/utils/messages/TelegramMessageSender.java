package org.task.feedbackbot.utils.messages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.models.events.SendMessageEvent;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Component
@Slf4j
public final class TelegramMessageSender implements MessageSender {

    private final ApplicationEventPublisher eventPublisher;

    public TelegramMessageSender(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        log.debug("Publishing message event for user {}", chatId);
        eventPublisher.publishEvent(new SendMessageEvent(chatId, text, replyKeyboard));
    }
}
