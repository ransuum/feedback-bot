package org.task.feedbackbot.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.enums.CriticalityLevel;
import org.task.feedbackbot.models.enums.Position;
import org.task.feedbackbot.models.enums.SentimentType;
import org.task.feedbackbot.service.FeedBackService;
import org.task.feedbackbot.specification.request.FeedbackCriteriaRequest;

import java.util.Collections;

@Controller
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedBackService feedBackService;

    public FeedbackController(FeedBackService feedBackService) {
        this.feedBackService = feedBackService;
    }

    @GetMapping
    public String listFeedbacks(
            @ModelAttribute("criteria") FeedbackCriteriaRequest criteria,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        final Page<FeedbackAnalysisDto> page = feedBackService.findByCriteria(criteria, pageable);
        model.addAttribute("criteria", criteria);
        model.addAttribute("page", page);
        model.addAttribute("feedbacks", page.hasContent() ? page : Collections.emptyList());
        model.addAttribute("positions", Position.values());
        model.addAttribute("sentiments", SentimentType.values());
        model.addAttribute("criticalityLevels", CriticalityLevel.values());
        return "feedback/list";
    }
}
