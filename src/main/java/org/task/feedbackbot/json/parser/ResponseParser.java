package org.task.feedbackbot.json.parser;

public sealed interface ResponseParser permits OpenAiResponseParser {
    ParserBuilder parse();
}