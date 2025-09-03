package org.task.feedbackbot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.UserState;
import org.task.feedbackbot.service.UserService;
import org.task.feedbackbot.utils.messages.MessageSender;

@Component
@Slf4j
public class UserRegistrationHandler {

    private final MessageSender messageSender;
    private final UserService userService;

    public UserRegistrationHandler(MessageSender messageSender, UserService userService) {
        this.messageSender = messageSender;
        this.userService = userService;
    }

    public void handleStartCommand(Long chatId, User user) {
        if (user.getState() == UserState.REGISTERED)
            messageSender.sendMessage(chatId,
                    "Вітаю знову! Ви вже зареєстровані та можете залишати відгуки.", null);
        else handleNewUser(chatId);

    }

    private void handleNewUser(Long chatId) {
        messageSender.sendMessage(chatId,
                "Вітаю! Для початку роботи потрібно пройти реєстрацію.", null);
        userService.updateUserState(chatId, UserState.AWAITING_POSITION);
    }

    public void sendErrorMessage(Long chatId) {
        messageSender.sendMessage(chatId,
                "Вибачте, сталася помилка. Спробуйте ще раз або зверніться до підтримки.", null);
    }
}
