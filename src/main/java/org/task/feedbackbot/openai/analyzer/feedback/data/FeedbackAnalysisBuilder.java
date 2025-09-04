package org.task.feedbackbot.openai.analyzer.feedback.data;

import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.enums.CriticalityLevel;
import org.task.feedbackbot.models.enums.FeedbackCategory;
import org.task.feedbackbot.models.enums.SentimentType;

public class FeedbackAnalysisBuilder {

    private SentimentType sentimentType = SentimentType.NEUTRAL;
    private CriticalityLevel criticalityLevel = CriticalityLevel.MODERATE;
    private String solution = "Потребує розгляду";
    private String category = FeedbackCategory.OTHER.getCode();
    private double confidence = 0.5;

    public static FeedbackAnalysisBuilder builder() {
        return new FeedbackAnalysisBuilder();
    }

    public FeedbackAnalysisBuilder withSentiment(SentimentType sentimentType) {
        this.sentimentType = sentimentType;
        return this;
    }

    public FeedbackAnalysisBuilder withCriticality(CriticalityLevel criticalityLevel) {
        this.criticalityLevel = criticalityLevel;
        return this;
    }

    public FeedbackAnalysisBuilder withSolution(String solution) {
        this.solution = solution;
        return this;
    }

    public FeedbackAnalysisBuilder withCategory(String category) {
        this.category = category;
        return this;
    }

    public FeedbackAnalysisBuilder withConfidence(double confidence) {
        this.confidence = Math.clamp(confidence, 0.0, 1.0);
        return this;
    }

    public FeedbackAnalysisDto build() {
        return FeedbackAnalysisDto.builder()
                .sentiment(sentimentType)
                .criticalityLevel(criticalityLevel)
                .solution(solution)
                .branch(category)
                .confidence(confidence)
                .build();
    }
}
