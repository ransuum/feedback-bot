package org.task.feedbackbot.openai.analyzer.feedback;

import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;

public interface FeedbackAnalyzer {
    FeedbackAnalysisDto analyze(String feedbackText);

    boolean canHandle(String feedbackText);

    int getPriority();
}
