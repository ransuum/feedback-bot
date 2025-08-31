package org.task.feedbackbot.utils.promptbuilder;

public sealed interface PromptBuilder permits OpenAiPromptBuilder {
    String buildPrompt(String feedbackText);
}
