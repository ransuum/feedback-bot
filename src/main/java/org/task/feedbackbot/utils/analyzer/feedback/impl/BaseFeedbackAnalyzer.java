package org.task.feedbackbot.utils.analyzer.feedback.impl;

import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.utils.analyzer.feedback.FeedbackAnalyzer;
import org.task.feedbackbot.utils.parser.ResponseParser;
import org.task.feedbackbot.utils.promptbuilder.PromptBuilder;

@Slf4j
public abstract class BaseFeedbackAnalyzer implements FeedbackAnalyzer {

    protected final ResponseParser responseParser;
    protected final PromptBuilder promptBuilder;

    protected BaseFeedbackAnalyzer(ResponseParser responseParser,
                                   PromptBuilder promptBuilder) {
        this.responseParser = responseParser;
        this.promptBuilder = promptBuilder;
    }

    @Override
    public FeedbackAnalysisDto analyze(@NotBlank(message = "Feedback text cannot be null or empty") String feedbackText) {
        log.info("Analyzing feedback with {}: {}", this.getClass().getSimpleName(), feedbackText);

        try {
            return performAnalysis(feedbackText);
        } catch (Exception e) {
            log.error("Analysis failed: {}", e.getMessage(), e);
            return createFallbackAnalysis(feedbackText);
        }
    }

    protected abstract FeedbackAnalysisDto performAnalysis(String feedbackText);

    protected abstract FeedbackAnalysisDto createFallbackAnalysis(String feedbackText);
}
