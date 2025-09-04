package org.task.feedbackbot.openai.promptbuilder;

public sealed interface PromptBuilder permits OpenAiPromptBuilder {
    String buildPrompt(String feedbackText);
}
