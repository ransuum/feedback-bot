package org.task.feedbackbot.specification.request;

import org.springframework.data.jpa.domain.Specification;
import org.task.feedbackbot.models.entity.Feedback;
import org.task.feedbackbot.models.enums.Position;
import org.task.feedbackbot.models.enums.SentimentType;
import org.task.feedbackbot.specification.FeedbackSpecification;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;

public record FeedbackCriteriaRequest(
        @Min(value = 1) Integer criticalityLevel,

        LocalDateTime start,
        LocalDateTime end,

        Position position,
        String branch,
        SentimentType sentiment
) {

    public Specification<Feedback> createSpecification() {
        return Specification.allOf(
                FeedbackSpecification.hasUserBranch(branch),
                FeedbackSpecification.hasSentiment(sentiment),
                FeedbackSpecification.hasUserPosition(position),
                FeedbackSpecification.createdBetween(start, end),
                FeedbackSpecification.hasCriticality(criticalityLevel)
        );
    }
}
