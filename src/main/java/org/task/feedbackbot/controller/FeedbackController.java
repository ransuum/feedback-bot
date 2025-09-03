package org.task.feedbackbot.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.task.feedbackbot.exception.ExporterException;
import org.task.feedbackbot.models.dto.FeedbackAnalysisDto;
import org.task.feedbackbot.models.enums.CriticalityLevel;
import org.task.feedbackbot.models.enums.ExportFormat;
import org.task.feedbackbot.models.enums.Position;
import org.task.feedbackbot.models.enums.SentimentType;
import org.task.feedbackbot.service.FeedBackService;
import org.task.feedbackbot.specification.request.FeedbackCriteriaRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Objects;

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

    @GetMapping("/export")
    public void exportFeedbacks(
            @ModelAttribute("criteria") FeedbackCriteriaRequest criteria,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(defaultValue = "false") boolean all,
            @RequestParam ExportFormat format,
            HttpServletResponse response
    ) {
        try {
            feedBackService.exportFeedbacks(criteria, pageable, all, format, response.getOutputStream(), response);
        } catch (IOException e) {
            throw new ExporterException(e.getMessage());
        }
    }
}
