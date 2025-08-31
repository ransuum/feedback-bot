package org.task.feedbackbot.utils.parser;

public sealed interface ResponseParser permits OpenAiResponseParser {
    ParserBuilder parse();
}