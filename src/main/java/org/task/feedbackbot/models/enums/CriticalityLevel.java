package org.task.feedbackbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CriticalityLevel {
    LOW(1, "Низька"),
    MINOR(2, "Незначна"),
    MODERATE(3, "Помірна"),
    HIGH(4, "Висока"),
    CRITICAL(5, "Критична");

    private final int value;
    private final String displayName;

    public static CriticalityLevel fromValue(int value) {
        return Arrays.stream(values())
                .filter(level -> level.value == value)
                .findFirst()
                .orElse(MODERATE);
    }
}
