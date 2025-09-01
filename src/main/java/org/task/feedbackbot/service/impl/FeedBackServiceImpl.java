package org.task.feedbackbot.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.task.feedbackbot.mapper.FeedbackMapper;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.entity.Feedback;
import org.task.feedbackbot.repository.FeedbackRepository;
import org.task.feedbackbot.service.FeedBackService;
import org.task.feedbackbot.service.GoogleDocsService;
import org.task.feedbackbot.service.TrelloService;
import org.task.feedbackbot.specification.request.FeedbackCriteriaRequest;
import org.task.feedbackbot.utils.analyzer.feedback.data.FallbackAnalysisFactory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedBackServiceImpl implements FeedBackService {

    private final FeedbackRepository feedbackRepository;
    private final GoogleDocsService googleDocsService;
    private final TrelloService trelloService;
    private final ChatGptServiceImpl openAiService;
    private final FallbackAnalysisFactory fallbackAnalysisFactory;
    private final FeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public void save(@NotNull(message = "Feedback is null") Feedback feedback) {
        feedbackRepository.save(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackAnalysisDto> findByCriteria(FeedbackCriteriaRequest feedbackCriteriaRequest,
                                                    Pageable pageable) {
        return feedbackRepository.findAll(feedbackCriteriaRequest.createSpecification(), pageable)
                .map(feedbackMapper::toDto);
    }

    @Override
    @Async
    @Transactional
    public void processIntegrations(Feedback feedback) {
        CompletableFuture.runAsync(() -> {
            try {
                googleDocsService.addFeedbackToDoc(feedback);

                feedback.setSyncedToGoogleDocs(true);

                log.debug("Successfully synced feedback {} to Google Docs", feedback.getId());
            } catch (Exception e) {
                log.error("Error syncing feedback {} to Google Docs", feedback.getId(), e);
            }
        });

        if (feedback.isCritical())
            CompletableFuture.runAsync(() -> {
                try {
                    Optional.ofNullable(trelloService.createCard(feedback))
                            .ifPresent(feedback::setTrelloCardId);
                } catch (Exception e) {
                    log.error("Error creating Trello card for feedback", e);
                }
            });

        feedbackRepository.save(feedback);
    }

    @Override
    public FeedbackAnalysisDto analyzeFeedbackWithFallback(String text) {
        try {
            return openAiService.analyzeFeedback(text);
        } catch (Exception e) {
            log.warn("GPT analysis failed, using fallback: {}", e.getMessage());
            return fallbackAnalysisFactory.createAnalysis(text);
        }
    }
}
