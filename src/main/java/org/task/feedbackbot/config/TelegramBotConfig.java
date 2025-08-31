package org.task.feedbackbot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.task.feedbackbot.service.impl.TelegramBotService;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TelegramBotConfig {

    private final TelegramBotService telegramBotService;

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        try {
            api.registerBot(telegramBotService);
            log.info("Telegram bot successfully registered");
        } catch (TelegramApiException e) {
            log.error("Failed to register bot", e);
            throw e;
        }
        return api;
    }
}
