package org.task.feedbackbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.task.feedbackbot.exception.FeedbackAnalyzerException;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.service.ChatGptService;
import org.task.feedbackbot.openai.analyzer.feedback.FeedbackAnalyzer;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGptServiceImpl implements ChatGptService {

    private final List<FeedbackAnalyzer> analyzers;

    @Override
    public FeedbackAnalysisDto analyzeFeedback(String feedbackText) {
        log.info("Starting feedback analysis: {}", feedbackText);

        final FeedbackAnalyzer selectedAnalyzer = analyzers.stream()
                .filter(analyzer -> analyzer.canHandle(feedbackText))
                .min(Comparator.comparing(FeedbackAnalyzer::getPriority))
                .orElseThrow(() -> new FeedbackAnalyzerException("No suitable analyzer found"));

        log.info("Selected analyzer: {}", selectedAnalyzer.getClass().getSimpleName());

        return selectedAnalyzer.analyze(feedbackText);
    }
}
