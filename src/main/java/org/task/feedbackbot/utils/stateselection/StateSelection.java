package org.task.feedbackbot.utils.stateselection;

import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.UserState;

public interface StateSelection {
    void handleUserInput(Long chatId, String text, User user);

    UserState getHandledState();
}
