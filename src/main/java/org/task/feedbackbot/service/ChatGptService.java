package org.task.feedbackbot.service;

import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.enums.CriticalityLevel;
import org.task.feedbackbot.models.enums.SentimentType;

public interface ChatGptService {
    FeedbackAnalysisDto analyzeFeedback(String feedbackText);
}
