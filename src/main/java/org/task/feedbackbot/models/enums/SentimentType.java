package org.task.feedbackbot.models.enums;

import lombok.Getter;

@Getter
public enum SentimentType {
    POSITIVE("Позитивний"),
    NEUTRAL("Нейтральний"),
    NEGATIVE("Негативний");

    private final String displayName;

    SentimentType(String displayName) {
        this.displayName = displayName;
    }
}