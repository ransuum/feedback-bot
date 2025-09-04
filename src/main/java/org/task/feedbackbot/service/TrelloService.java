package org.task.feedbackbot.service;

import org.task.feedbackbot.models.entity.Feedback;

public interface TrelloService {
    String createCard(Feedback feedback);

    void deleteCard(String cardId);
}
