package org.task.feedbackbot.openai.analyzer.feedback.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.task.feedbackbot.exception.FeedbackJsonProcessingException;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.openai.analyzer.feedback.data.FallbackAnalysisFactory;
import org.task.feedbackbot.utils.converter.airesponse.ResponseParser;
import org.task.feedbackbot.openai.promptbuilder.PromptBuilder;

@Component
@Slf4j
public class OpenAiFeedbackAnalyzer extends BaseFeedbackAnalyzer {

    private final FallbackAnalysisFactory factory;
    private final ChatClient chatClient;

    public OpenAiFeedbackAnalyzer(@Qualifier("openAiResponseParser") ResponseParser responseParser,
                                  @Qualifier("openAiPromptBuilder") PromptBuilder promptBuilder, FallbackAnalysisFactory factory,
                                  ChatClient.Builder chatClient) {
        super(responseParser, promptBuilder);
        this.factory = factory;
        this.chatClient = chatClient.build();
    }

    @Override
    protected FeedbackAnalysisDto performAnalysis(String feedbackText) {
        try {
            final String prompt = promptBuilder.buildPrompt(feedbackText);
            log.debug("Generated prompt: {}", prompt);

            final var response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            log.info("OpenAI response: {}", response);

            return responseParser.parse().build(response);
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
