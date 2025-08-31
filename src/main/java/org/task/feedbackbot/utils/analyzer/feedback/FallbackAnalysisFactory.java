package org.task.feedbackbot.utils.analyzer.feedback;

import org.springframework.stereotype.Component;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.enums.CriticalityLevel;
import org.task.feedbackbot.models.enums.FeedbackPattern;
import org.task.feedbackbot.models.enums.SentimentType;

import java.util.Optional;

import static org.task.feedbackbot.models.enums.FeedbackPattern.calculateConfidence;

@Component
public class FallbackAnalysisFactory {

    public FeedbackAnalysisDto createAnalysis(String feedbackText) {
        Optional<FeedbackPattern> pattern = FeedbackPattern.findBestMatch(feedbackText);

        if (pattern.isEmpty())
            return FeedbackAnalysisBuilder.create()
                    .withSolution("Потребує додаткового аналізу")
                    .build();

        FeedbackPattern p = pattern.get();

        return FeedbackAnalysisBuilder.create()
                .withSentiment(determineSentiment(p.getCriticalityLevel()))
                .withCriticality(p.getCriticalityLevel())
                .withSolution(p.getDefaultSolution())
                .withCategory(p.getCategory().getCode())
                .withConfidence(calculateConfidence(p))
                .build();
    }

    private SentimentType determineSentiment(CriticalityLevel level) {
        return switch (level) {
            case CRITICAL, HIGH -> SentimentType.NEGATIVE;
            case LOW -> SentimentType.POSITIVE;
            case MODERATE, MINOR -> SentimentType.NEUTRAL;
        };
    }
}
