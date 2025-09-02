package org.task.feedbackbot.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.task.feedbackbot.handler.UserRegistrationHandler;
import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.UserState;
import org.task.feedbackbot.models.events.SendMessageEvent;
import org.task.feedbackbot.service.*;
import org.task.feedbackbot.utils.stateselection.StateHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.username}")
    private String username;

    private final UserService userService;
    private final Map<UserState, StateHandler> stateHandlers;
    private final UserRegistrationHandler registrationHandler;

    public TelegramBotService(
            UserService userService,
            List<StateHandler> stateHandlers,
            UserRegistrationHandler registrationHandler) {
        this.userService = userService;
        this.registrationHandler = registrationHandler;
        this.stateHandlers = stateHandlers.stream()
                .collect(Collectors.toMap(StateHandler::getHandledState, handler -> handler));
    }

    @PostConstruct
    public void init() {
        log.info("Telegram bot initialized with username: {}", username);
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                final var message = update.getMessage();
                processUserMessage(message.getChatId(), message.getText());
            }
        } catch (Exception e) {
            log.error("Critical error processing update: {}", e.getMessage(), e);
        }
    }

    @EventListener
    @Async
    public void handleSendMessageEvent(SendMessageEvent event) {
        try {
            final var message = SendMessage.builder()
                    .chatId(event.chatId())
                    .text(event.text())
                    .replyMarkup(event.replyKeyboard())
                    .build();

            execute(message);
            log.debug("Sent message to user {}: {}", event.chatId(), event.text());
        } catch (Exception e) {
            log.error("Error sending message to user {}: {}", event.chatId(), e.getMessage(), e);
        }
    }

    private void processUserMessage(Long chatId, String text) {
        try {
            final User user = userService.findById(chatId)
                    .orElseGet(() -> {
                        User newUser = userService.createNewUser(chatId);
                        log.info("Created new user: {}", chatId);
                        return newUser;
                    });

            userService.updateLastActive(user);

            if ("/start".equals(text)) {
                registrationHandler.handleStartCommand(chatId, user);
                return;
            }

            Optional.ofNullable(stateHandlers.get(user.getState()))
                    .ifPresentOrElse(handler -> handler.handleUserInput(chatId, text, user),
                            () -> {
                                log.warn("No handler found for state: {}", user.getState());
                                registrationHandler.sendErrorMessage(chatId);
                            });

        } catch (Exception e) {
            log.error("Error processing message for user {}: {}", chatId, e.getMessage(), e);
            registrationHandler.sendErrorMessage(chatId);
        }
    }
}
