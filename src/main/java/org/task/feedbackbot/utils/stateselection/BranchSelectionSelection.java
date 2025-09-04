package org.task.feedbackbot.utils.stateselection;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.UserState;
import org.task.feedbackbot.service.UserService;
import org.task.feedbackbot.utils.messages.MessageSender;

@Component
@Slf4j
public class BranchSelectionSelection extends BaseStateSelection {

    public BranchSelectionSelection(UserService userService, MessageSender messageSender) {
        super(userService, messageSender);
    }

    @Override
    public void handleUserInput(Long chatId, String text, User user) {
        try {
            if (StringUtils.isBlank(text)) {
                sendMessage(chatId, "Будь ласка, вкажіть назву філії.");
                return;
            }

            user.setBranch(text.trim());
            user.setState(UserState.REGISTERED);
            userService.save(user);

            sendMessage(chatId, "Дякуємо за реєстрацію! Тепер ви можете надсилати свої відгуки, скарги або пропозиції.");
            log.info("User {} registered with branch: {}", chatId, text);

        } catch (Exception e) {
            log.error("Error handling branch selection for user {}: {}", chatId, e.getMessage(), e);
            sendMessage(chatId, "Сталася помилка під час реєстрації. Спробуйте ще раз.");
        }
    }

    @Override
    public UserState getHandledState() {
        return UserState.AWAITING_BRANCH;
    }
}
