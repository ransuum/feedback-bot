package org.task.feedbackbot.utils.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.task.feedbackbot.models.enums.CriticalityLevel;
import org.task.feedbackbot.models.enums.FeedbackCategory;
import org.task.feedbackbot.models.enums.SentimentType;

public interface JsonFieldParser {
    default CriticalityLevel parseCriticality(JsonNode jsonNode) {
        final int level = jsonNode.path("criticalityLevel").asInt(2);
        return CriticalityLevel.fromValue(level);
    }

    default String parseSolution(JsonNode jsonNode) {
        return jsonNode.path("solution").asText("Потребує розгляду");
    }

    default String parseCategory(JsonNode jsonNode) {
        return jsonNode.path("category").asText(FeedbackCategory.OTHER.getCode());
    }

    default double parseConfidence(JsonNode jsonNode) {
        return jsonNode.path("confidence").asDouble(0.8);
    }

    default SentimentType parseSentiment(JsonNode jsonNode) {
        String sentiment = jsonNode.path("sentiment").asText().toUpperCase();
        try {
            return SentimentType.valueOf(sentiment);
        } catch (IllegalArgumentException e) {
            return SentimentType.NEUTRAL;
        }
    }
}
