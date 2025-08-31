package org.task.feedbackbot.specification;


import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.task.feedbackbot.models.entity.Feedback;
import org.task.feedbackbot.models.entity.User;
import org.task.feedbackbot.models.enums.Position;
import org.task.feedbackbot.models.enums.SentimentType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedbackSpecification {
    private static final String USER_FIELD = "user";
    private static final String CRITICALITY_LEVEL_FIELD = "criticalityLevel";
    private static final String SENTIMENT_FIELD = "sentiment";
    private static final String POSITION_FIELD = "position";
    private static final String BRANCH_FIELD = "branch";
    private static final String CREATED_AT_FIELD = "createdAt";

    public static Specification<Feedback> hasCriticality(Integer level) {
        return (root, cq, cb) ->
                level == null ? cb.conjunction() : cb.equal(root.get(CRITICALITY_LEVEL_FIELD), level);
    }

    public static Specification<Feedback> hasSentiment(SentimentType sentiment) {
        return (root, cq, cb) ->
                sentiment == null ? cb.conjunction() : cb.equal(root.get(SENTIMENT_FIELD), sentiment);
    }

    public static Specification<Feedback> hasUserPosition(Position position) {
        return (root, cq, cb) -> {
            if (position == null) return cb.conjunction();
            Join<Feedback, User> user = root.join(USER_FIELD, JoinType.INNER);
            return cb.equal(user.get(POSITION_FIELD), position);
        };
    }

    public static Specification<Feedback> hasUserBranch(String branch) {
        return (root, cq, cb) -> {
            if (StringUtils.isBlank(branch)) return cb.conjunction();
            Join<Feedback, User> user = root.join(USER_FIELD, JoinType.INNER);
            return cb.equal(user.get(BRANCH_FIELD), branch);
        };
    }

    public static Specification<Feedback> createdBetween(LocalDateTime from, LocalDateTime to) {
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (from != null) predicates.add(cb.greaterThanOrEqualTo(root.get(CREATED_AT_FIELD), from));
            if (to != null) predicates.add(cb.lessThanOrEqualTo(root.get(CREATED_AT_FIELD), to));
            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
