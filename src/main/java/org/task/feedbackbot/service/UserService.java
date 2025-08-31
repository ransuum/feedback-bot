package org.task.feedbackbot.service;

import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.UserState;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long telegramId);

    User createNewUser(Long telegramId);

    void save(User user);

    void updateUserState(Long telegramId, UserState state);

    void updateLastActive(User user);
}
