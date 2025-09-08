package org.task.feedbackbot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.UserState;
import org.task.feedbackbot.service.PositionService;
import org.task.feedbackbot.service.UserService;
import org.task.feedbackbot.utils.messages.MessageSender;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
@Slf4j
public class UserRegistrationHandler {

    private final MessageSender messageSender;
    private final UserService userService;
    private final PositionService positionService;

    public UserRegistrationHandler(MessageSender messageSender,
                                   UserService userService,
                                   PositionService positionService) {
        this.messageSender = messageSender;
        this.userService = userService;
        this.positionService = positionService;
    }

    public void handleStartCommand(Long chatId, User user) {
        if (user.getState() == UserState.REGISTERED) {
            messageSender.sendMessage(chatId,
                    "Вітаю знову! Ви вже зареєстровані та можете залишати відгуки.", null);
        } else {
            handleNewUser(chatId);
        }
    }

    private void handleNewUser(Long chatId) {
        messageSender.sendMessage(chatId,
                "Вітаю! Для початку роботи потрібно пройти реєстрацію.", null);

        userService.updateUserState(chatId, UserState.AWAITING_POSITION);

        sendPositionSelection(chatId);
    }

    private void sendPositionSelection(Long chatId) {
        messageSender.sendMessage(chatId, "Оберіть вашу посаду:", positionService.createPositionKeyboard());
        log.debug("Sent position selection keyboard to user: {}", chatId);
    }

    public void sendErrorMessage(Long chatId) {
        messageSender.sendMessage(chatId,
                "Вибачте, сталася помилка. Спробуйте ще раз або зверніться до підтримки.", null);
    }

    public void resendPositionSelection(Long chatId) {
        sendPositionSelection(chatId);
    }
}