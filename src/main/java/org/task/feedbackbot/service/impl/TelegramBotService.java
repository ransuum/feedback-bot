package org.task.feedbackbot.service.impl;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.task.feedbackbot.models.entity.Feedback;
import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.Position;
import org.task.feedbackbot.models.enums.UserState;
import org.task.feedbackbot.service.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.username}")
    private String username;

    private final UserService userService;
    private final FeedBackService feedBackService;
    private final PostitionService postitionService;

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

    private void processUserMessage(Long chatId, String text) {
        try {
            userService.findById(chatId)
                    .or(() -> {
                        User newUser = userService.createNewUser(chatId);
                        log.info("Created new user: {}", chatId);
                        return Optional.of(newUser);
                    })
                    .ifPresent(user -> {
                        userService.updateLastActive(user);

                        if (text.equals("/start")) {
                            if (user.getState() == UserState.REGISTERED)
                                sendMessage(chatId, "Вітаю знову! Ви вже зареєстровані та можете залишати відгуки.", null);
                            else handleNewUser(chatId);

                            return;
                        }

                        switch (user.getState()) {
                            case NEW -> handleNewUser(chatId);
                            case AWAITING_POSITION -> handlePositionSelection(chatId, text, user);
                            case AWAITING_BRANCH -> handleBranchSelection(chatId, text, user);
                            case REGISTERED -> handleFeedback(chatId, text, user);
                        }
                    });

        } catch (Exception e) {
            log.error("Error processing message for user {}: {}", chatId, e.getMessage());
            sendMessage(chatId, "Вибачте, сталася помилка. Спробуйте ще раз або зверніться до підтримки.", null);
        }
    }

    private void handleNewUser(Long chatId) {
        try {
            sendMessage(chatId, "Вітаю! Оберіть вашу посаду:", postitionService.createPositionKeyboard());
            userService.updateUserState(chatId, UserState.AWAITING_POSITION);
            log.info("Sent position selection keyboard to user: {}", chatId);
        } catch (Exception e) {
            log.error("Error sending message to user: {}", chatId, e);
        }
    }

    private void handlePositionSelection(Long chatId, String text, User user) {
        try {
            final Position position = postitionService.findPositionByText(text);

            if (Objects.isNull(position)) {
                sendMessage(chatId, "Будь ласка, оберіть посаду з запропонованих варіантів.", null);
                return;
            }

            user.setPosition(position);
            userService.save(user);

            sendMessage(chatId, "Вкажіть назву вашої філії:", null);
            userService.updateUserState(chatId, UserState.AWAITING_BRANCH);
            log.info("User {} selected position: {}", chatId, position);
        } catch (Exception e) {
            log.error("Error handling position selection for user: {}", chatId, e);
        }
    }

    private void handleBranchSelection(Long chatId, String text, User user) {
        try {
            user.setBranch(text);
            user.setState(UserState.REGISTERED);
            user.setRegisteredAt(LocalDateTime.now());
            userService.save(user);

            sendMessage(chatId, "Дякуємо за реєстрацію! Тепер ви можете надсилати свої відгуки, скарги або пропозиції.", null);
            log.info("User {} registered with branch: {}", chatId, text);
        } catch (Exception e) {
            log.error("Error handling branch selection for user: {}", chatId, e);
        }
    }

    private void handleFeedback(Long chatId, String text, User user) {
        try {
            log.info("Processing feedback from user {}: {}", chatId, text);

            final var analysis = feedBackService.analyzeFeedbackWithFallback(text);

            feedBackService.processIntegrations(Feedback.builder()
                    .user(user)
                    .message(text)
                    .sentiment(analysis.sentiment())
                    .criticalityLevel(analysis.criticalityLevel().getValue())
                    .solution(analysis.solution())
                    .build());

            sendMessage(chatId, "Дякуємо за ваш відгук! Він був проаналізований та переданий керівництву.", null);

        } catch (Exception e) {
            log.error("Critical error handling feedback from user: {}", chatId, e);
            sendMessage(chatId, "Вибачте, сталася помилка при обробці вашого відгуку. Спробуйте ще раз.", null);
        }
    }

    private void sendMessage(Long chatId, String text, @Nullable ReplyKeyboard replyKeyboard) {
        try {
            final SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build();
            if (replyKeyboard != null) message.setReplyMarkup(replyKeyboard);

            execute(message);
            log.info("Sent message to user {}: {}", chatId, text);
        } catch (Exception e) {
            log.error("Error sending message to user: {}", chatId, e);
        }
    }
}
