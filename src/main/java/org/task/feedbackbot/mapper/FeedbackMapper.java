package org.task.feedbackbot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.entity.Feedback;
import org.task.feedbackbot.models.enums.FeedbackCategory;
import org.task.feedbackbot.models.enums.FeedbackPattern;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(source = "feedback.user.position", target = "branch")
    @Mapping(target = "criticalityLevel",
            expression = "java(org.task.feedbackbot.models.enums.CriticalityLevel.fromValue(feedback.getCriticalityLevel()))")
    @Mapping(source = "message", target = "confidence", qualifiedByName = "confidence")
    @Mapping(source = "message", target = "category", qualifiedByName = "category")
    FeedbackAnalysisDto toDto(Feedback feedback);

    @Named("confidence")
    default double confidence(String message) {
        return FeedbackPattern.findBestMatch(message)
                .map(FeedbackPattern::calculateConfidence)
                .orElseGet(() -> FeedbackPattern.calculateConfidence(FeedbackPattern.MANAGEMENT_ISSUES));
    }

    @Named("category")
    default String getCategory(String message) {
        return FeedbackPattern.findBestMatch(message)
                .map(feedbackPattern -> feedbackPattern.getCategory().getDisplayName())
                .orElseGet(FeedbackCategory.OTHER::getDisplayName);
    }
}
