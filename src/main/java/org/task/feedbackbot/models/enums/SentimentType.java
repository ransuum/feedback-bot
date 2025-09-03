package org.task.feedbackbot.models.enums;

import lombok.Getter;

@Getter
public enum SentimentType {
    POSITIVE("Позитивний"),
    NEUTRAL("Нейтральний"),
    NEGATIVE("Негативний"),
    OTHER("Інше");

    private final String displayName;

    SentimentType(String displayName) {
        this.displayName = displayName;
    }
}