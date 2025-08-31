package org.task.feedbackbot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.entity.Feedback;
import org.task.feedbackbot.specification.request.FeedbackCriteriaRequest;

public interface FeedBackService {
    void save(Feedback feedback);

    Page<FeedbackAnalysisDto> findByCriteria(FeedbackCriteriaRequest feedbackCriteriaRequest,
                                             Pageable pageable);

    void processIntegrations(Feedback feedback);

    FeedbackAnalysisDto analyzeFeedbackWithFallback(String text);
}
