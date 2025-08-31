package org.task.feedbackbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum FeedbackPattern {
    SALARY_ISSUES(
            List.of("зарплата", "затримка", "виплата", "гроші", "зп"),
            "Терміново вирішити питання оплати праці",
            FeedbackCategory.SALARY,
            CriticalityLevel.CRITICAL
    ),

    EQUIPMENT_BROKEN(
            List.of("не працює", "поломка", "зламано", "не функціонує"),
            "Відремонтувати обладнання",
            FeedbackCategory.EQUIPMENT,
            CriticalityLevel.HIGH
    ),

    EQUIPMENT_NEEDED(
            List.of("потрібен інструмент", "треба обладнання", "нема інструменту"),
            "Розглянути придбання нового обладнання",
            FeedbackCategory.EQUIPMENT,
            CriticalityLevel.MODERATE
    ),

    MANAGEMENT_ISSUES(
            List.of("поганий керівник", "погане керівництво", "адміністратор"),
            "Провести розмову з керівництвом",
            FeedbackCategory.MANAGEMENT,
            CriticalityLevel.HIGH
    ),

    POSITIVE_FEEDBACK(
            List.of("дякую", "відмінно", "чудово", "супер", "класно"),
            "Продовжувати в тому ж дусі",
            FeedbackCategory.APPRECIATION,
            CriticalityLevel.LOW
    );

    private final List<String> keywords;
    private final String defaultSolution;
    private final FeedbackCategory category;
    private final CriticalityLevel criticalityLevel;

    public boolean matches(String text) {
        String lowerText = text.toLowerCase();
        return keywords.stream().anyMatch(lowerText::contains);
    }

    public static Optional<FeedbackPattern> findBestMatch(String text) {
        return Arrays.stream(values())
                .filter(pattern -> pattern.matches(text))
                .findFirst();
    }

    public static double calculateConfidence(FeedbackPattern pattern) {
        return switch (pattern.getCriticalityLevel()) {
            case CRITICAL -> 0.9;
            case HIGH -> 0.8;
            case MODERATE -> 0.6;
            case LOW, MINOR -> 0.7;
        };
    }
}
