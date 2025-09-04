package org.task.feedbackbot.json.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;

@FunctionalInterface
public interface ParserBuilder {
    FeedbackAnalysisDto build(String response) throws JsonProcessingException;
}
