package org.task.feedbackbot.models.dto;

import org.task.feedbackbot.models.entity.User;

public record UserContext(Long chatId, User user) {}
