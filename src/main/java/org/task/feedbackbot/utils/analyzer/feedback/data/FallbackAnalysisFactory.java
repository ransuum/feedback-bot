package org.task.feedbackbot.utils.analyzer.feedback.data;

import org.springframework.stereotype.Component;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.enums.CriticalityLevel;
import org.task.feedbackbot.models.enums.FeedbackPattern;
import org.task.feedbackbot.models.enums.SentimentType;

import static org.task.feedbackbot.models.enums.FeedbackPattern.calculateConfidence;

@Component
public class FallbackAnalysisFactory {

    public FeedbackAnalysisDto createAnalysis(String feedbackText) {
        return FeedbackPattern.findBestMatch(feedbackText)
                .map(p -> FeedbackAnalysisBuilder.builder()
                        .withSentiment(determineSentiment(p.getCriticalityLevel()))
                        .withCriticality(p.getCriticalityLevel())
                        .withSolution(p.getDefaultSolution())
                        .withCategory(p.getCategory().getCode())
                        .withConfidence(calculateConfidence(p))
                        .build())
                .orElseGet(() -> FeedbackAnalysisBuilder.builder()
                        .withSolution("Потребує додаткового аналізу")
                        .build());
    }

    private SentimentType determineSentiment(CriticalityLevel level) {
        return switch (level) {
            case CRITICAL, HIGH -> SentimentType.NEGATIVE;
            case LOW -> SentimentType.POSITIVE;
            case MODERATE, MINOR -> SentimentType.NEUTRAL;
        };
    }
}
