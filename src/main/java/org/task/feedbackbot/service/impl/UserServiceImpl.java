package org.task.feedbackbot.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.UserState;
import org.task.feedbackbot.repository.UserRepository;
import org.task.feedbackbot.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long telegramId) {
        return userRepository.findById(telegramId);
    }

    @Override
    public User createNewUser(@NotNull(message = "telegramId is null") Long telegramId) {
        return userRepository.save(User.builder()
                        .telegramId(telegramId)
                        .state(UserState.NEW)
                .build());
    }

    @Override
    public void save(@NotNull(message = "User is null") User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUserState(Long telegramId, UserState state) {
        userRepository.findById(telegramId).ifPresent(user -> {
            user.setState(state);
            userRepository.save(user);
        });
    }

    @Override
    public void updateLastActive(@NotNull(message = "User is null") User user) {
        user.setLastActiveAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
