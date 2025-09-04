package org.task.feedbackbot.openai.promptbuilder;

import org.springframework.stereotype.Component;

@Component("openAiPromptBuilder")
public final class OpenAiPromptBuilder implements PromptBuilder {

    @Override
    public String buildPrompt(String feedbackText) {
        return String.format("""
                Ти - експерт з аналізу відгуків співробітників автосервісу.
                
                Проаналізуй цей відгук: "%s"
                
                Визнач категорію відгуку:
                - salary: проблеми з зарплатою, затримки виплат
                - equipment: поломки, потреба в інструментах/обладнанні
                - management: проблеми з керівництвом, адміністрацією
                - workplace: умови праці, чистота, комфорт
                - suggestion: пропозиції покращень
                - appreciation: подяки, позитивні відгуки
                - other: інше
                
                Поверни результат ТІЛЬКИ у форматі JSON:
                {
                  "sentiment": "POSITIVE|NEGATIVE|NEUTRAL",
                  "criticalityLevel": 1-5,
                  "solution": "коротке рішення українською",
                  "category": "одна з категорій вище",
                  "confidence": 0.0-1.0
                }
                
                Правила критичності:
                - Зарплата/затримки = 5 (критично)
                - Поломки обладнання = 4 (високо)
                - Проблеми з керівництвом = 3-4
                - Умови праці = 2-3
                - Пропозиції = 1-2
                - Подяки = 1
                """, feedbackText);
    }
}
