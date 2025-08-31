package org.task.feedbackbot.models.dto;

import lombok.Builder;
import org.task.feedbackbot.models.enums.CriticalityLevel;
import org.task.feedbackbot.models.enums.SentimentType;

@Builder
public record FeedbackAnalysisDto(
        Long id,
        String message,
        SentimentType sentiment,
        CriticalityLevel criticalityLevel,
        String solution,
        String branch,
        String category,
        double confidence,
        String trelloCardId,
        boolean syncedToGoogleDocs) {
}
