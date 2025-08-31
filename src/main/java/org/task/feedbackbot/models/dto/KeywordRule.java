package org.task.feedbackbot.models.dto;

import lombok.Builder;
import org.task.feedbackbot.models.enums.CriticalityLevel;
import org.task.feedbackbot.models.enums.SentimentType;

@Builder
public record KeywordRule(
        SentimentType sentiment,
        CriticalityLevel criticalityLevel,
        String solution,
        String category,
        int priority,
        double confidence
) {
}
