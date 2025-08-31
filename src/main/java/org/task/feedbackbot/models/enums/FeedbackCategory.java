package org.task.feedbackbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackCategory {
    SALARY("Зарплата", "salary"),
    EQUIPMENT("Обладнання", "equipment"),
    MANAGEMENT("Керівництво", "management"),
    WORKPLACE("Робоче місце", "workplace"),
    SUGGESTION("Пропозиція", "suggestion"),
    APPRECIATION("Подяка", "appreciation"),
    OTHER("Інше", "other");

    private final String displayName;
    private final String code;
}
