package org.task.feedbackbot.utils.stateselection;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.models.entity.Feedback;
import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.UserState;
import org.task.feedbackbot.service.FeedBackService;
import org.task.feedbackbot.service.UserService;
import org.task.feedbackbot.utils.messages.MessageSender;

@Component
@Slf4j
public class FeedbackHandler extends BaseStateHandler {

    private final FeedBackService feedBackService;

    public FeedbackHandler(
            UserService userService,
            MessageSender messageSender,
            FeedBackService feedBackService) {
        super(userService, messageSender);
        this.feedBackService = feedBackService;
    }

    @Override
    public void handleUserInput(Long chatId, String text, User user) {
        try {
            if (StringUtils.isBlank(text)) {
                sendMessage(chatId, "Будь ласка, надішліть ваш відгук.");
                return;
            }

            log.info("Processing feedback from user {}", chatId);

            final var analysis = feedBackService.analyzeFeedbackWithFallback(text);

            feedBackService.processIntegrations(Feedback.builder()
                    .user(user)
                    .message(text)
                    .sentiment(analysis.sentiment())
                    .criticalityLevel(analysis.criticalityLevel().getValue())
                    .solution(analysis.solution())
                    .build());

            sendMessage(chatId, "Дякуємо за ваш відгук! Він був проаналізований та переданий керівництву.");

        } catch (Exception e) {
            log.error("Critical error handling feedback from user {}: {}", chatId, e.getMessage(), e);
            sendMessage(chatId, "Вибачте, сталася помилка при обробці вашого відгуку. Спробуйте ще раз.");
        }
    }

    @Override
    public UserState getHandledState() {
        return UserState.REGISTERED;
    }
}
