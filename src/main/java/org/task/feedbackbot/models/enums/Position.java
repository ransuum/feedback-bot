package org.task.feedbackbot.models.enums;

import lombok.Getter;

@Getter
public enum Position {
    MECHANIC("Механік"),
    ELECTRICIAN("Електрик"),
    MANAGER("Менеджер");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }
}
