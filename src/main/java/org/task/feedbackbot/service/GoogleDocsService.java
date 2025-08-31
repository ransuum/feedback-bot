package org.task.feedbackbot.service;

import org.task.feedbackbot.models.entity.Feedback;

public interface GoogleDocsService {
    void addFeedbackToDoc(Feedback feedback);
}
