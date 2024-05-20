package com.sis.scrum.api;

import com.sis.scrum.model.Feedback;
import com.sis.scrum.service.RetrospectiveFeedbackService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
public class FeedBackController {
    Logger logger = LoggerFactory.getLogger(FeedBackController.class);
    private RetrospectiveFeedbackService retrospectiveFeedbackService;

    FeedBackController(RetrospectiveFeedbackService retrospectiveFeedbackService) {
        this.retrospectiveFeedbackService = retrospectiveFeedbackService;
    }

    /**
     *  Api to create new Feedback
     * @param feedback
     * @return Feedback
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Feedback createFeedBack(@Valid @RequestBody Feedback feedback) {
        return retrospectiveFeedbackService.createFeedback(feedback);
    }

    /**
     * Api to update Existing Feedback
     * @param feedback
     * @return Feedback
     */
    @PutMapping(value = "/update")
    public Feedback updateFeedBack(@Valid @RequestBody Feedback feedback) {
        return retrospectiveFeedbackService.updateFeedback(feedback);
    }
}
