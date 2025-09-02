package org.task.feedbackbot.utils.stateselection;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.Position;
import org.task.feedbackbot.models.enums.UserState;
import org.task.feedbackbot.service.PositionService;
import org.task.feedbackbot.service.UserService;
import org.task.feedbackbot.utils.messages.MessageSender;

@Component
@Slf4j
public class PositionSelectionHandler extends BaseStateHandler {

    private final PositionService positionService;

    public PositionSelectionHandler(
            UserService userService,
            MessageSender messageSender,
            PositionService positionService) {
        super(userService, messageSender);
        this.positionService = positionService;
    }

    @Override
    public void handleUserInput(Long chatId, String text, User user) {
        try {
            if (StringUtils.isBlank(text)) {
                sendMessage(chatId, "Будь ласка, надішліть ваш відгук.");
                return;
            }

            final Position position = positionService.findPositionByText(text);
            if (position == null) {
                sendMessage(chatId, "Будь ласка, оберіть посаду з запропонованих варіантів.");
                return;
            }

            user.setPosition(position);
            userService.save(user);
            userService.updateUserState(chatId, UserState.AWAITING_BRANCH);

            sendMessage(chatId, "Вкажіть назву вашої філії:");
            log.info("User {} selected position: {}", chatId, position.getDisplayName());

        } catch (Exception e) {
            log.error("Error handling position selection for user {}: {}", chatId, e.getMessage(), e);
            sendMessage(chatId, "Сталася помилка. Спробуйте ще раз.");
        }
    }

    @Override
    public UserState getHandledState() {
        return UserState.AWAITING_POSITION;
    }
}