package org.task.feedbackbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum KeywordType {
    SALARY(List.of("зарплата", "зп", "оплата", "виплата", "гроші"), "salary"),
    DELAY(List.of("затримка", "запізнення", "не вчасно"), "delay"),
    NOT_WORKING(List.of("не працює", "поламано", "зламано", "не функціонує"), "not_working"),
    NEED_TOOL(List.of("потрібен новий інструмент", "треба інструмент", "нема інструменту"), "need_tool"),
    BAD_ADMIN(List.of("поганий адміністратор", "поганий керівник", "погане керівництво"), "bad_admin"),
    GRATITUDE(List.of("дякую", "спасибі", "вдячний", "чудово", "відмінно"), "gratitude");

    private final List<String> keywords;
    private final String code;

    public boolean matches(String text) {
        String lowerText = text.toLowerCase();
        return keywords.stream().anyMatch(lowerText::contains);
    }
}
