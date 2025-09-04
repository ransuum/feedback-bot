package org.task.feedbackbot.utils.converter.airesponse;

public sealed interface ResponseParser permits OpenAiResponseConverter {
    ParserBuilder parse();
}