package org.task.feedbackbot.json.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;

@Component("openAiResponseParser")
@Slf4j
public final class OpenAiResponseParser implements ResponseParser, JsonFieldParser {

    private final ObjectMapper objectMapper;

    public OpenAiResponseParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ParserBuilder parse() {
        return response1 -> {
            final String cleanJson = cleanJsonResponse(response1);
            log.debug("Cleaned JSON: {}", cleanJson);

            final var jsonNode = objectMapper.readTree(cleanJson);

            return FeedbackAnalysisDto.builder()
                    .sentiment(parseSentiment(jsonNode))
                    .criticalityLevel(parseCriticality(jsonNode))
                    .solution(parseSolution(jsonNode))
                    .branch(parseCategory(jsonNode))
                    .confidence(parseConfidence(jsonNode))
                    .build();
        };
    }

    private String cleanJsonResponse(String response) {
        return response.trim()
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}
