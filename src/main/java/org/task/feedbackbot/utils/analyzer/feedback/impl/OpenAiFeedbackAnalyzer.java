package org.task.feedbackbot.utils.analyzer.feedback.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.exception.FeedbackJsonProcessingException;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.utils.analyzer.feedback.data.FallbackAnalysisFactory;
import org.task.feedbackbot.utils.parser.ResponseParser;
import org.task.feedbackbot.utils.promptbuilder.PromptBuilder;

import java.util.List;

@Component
@Slf4j
public class OpenAiFeedbackAnalyzer extends BaseFeedbackAnalyzer {

    private final OpenAiChatModel openAiChatModel;
    private final FallbackAnalysisFactory factory;

    public OpenAiFeedbackAnalyzer(OpenAiChatModel openAiChatModel,
                                  @Qualifier("openAiResponseParser") ResponseParser responseParser,
                                  @Qualifier("openAiPromptBuilder") PromptBuilder promptBuilder, FallbackAnalysisFactory factory) {
        super(responseParser, promptBuilder);
        this.openAiChatModel = openAiChatModel;
        this.factory = factory;
    }

    @Override
    protected FeedbackAnalysisDto performAnalysis(String feedbackText) {
        try {
            final String prompt = promptBuilder.buildPrompt(feedbackText);
            log.debug("Generated prompt: {}", prompt);

            final var response = openAiChatModel.call(
                    new Prompt(List.of(new UserMessage(prompt)))
            );

            final var aiResponse = response.getResult().getOutput().getText();
            log.info("OpenAI response: {}", aiResponse);

            return responseParser.parse().build(aiResponse);
        } catch (Exception e) {
            throw new FeedbackJsonProcessingException("Cannot process json response from ai due to: " + e.getMessage());
        }
    }

    @Override
    protected FeedbackAnalysisDto createFallbackAnalysis(String feedbackText) {
        return factory.createAnalysis(feedbackText);
    }

    @Override
    public boolean canHandle(String feedbackText) {
        return true;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
