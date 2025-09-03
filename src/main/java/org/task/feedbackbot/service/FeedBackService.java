package org.task.feedbackbot.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.entity.Feedback;
import org.task.feedbackbot.models.enums.ExportFormat;
import org.task.feedbackbot.specification.request.FeedbackCriteriaRequest;

import java.io.OutputStream;

public interface FeedBackService {
    void save(Feedback feedback);

    Page<FeedbackAnalysisDto> findByCriteria(FeedbackCriteriaRequest feedbackCriteriaRequest,
                                             Pageable pageable);

    void processIntegrations(Feedback feedback);

    FeedbackAnalysisDto analyzeFeedbackWithFallback(String text);

    void exportFeedbacks(FeedbackCriteriaRequest criteria,
                         Pageable pageable,
                         boolean all,
                         ExportFormat format,
                         OutputStream os,
                         HttpServletResponse response);
}
