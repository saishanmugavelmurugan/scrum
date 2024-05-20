package com.sis.scrum.api;

import com.sis.scrum.model.Retrospective;
import com.sis.scrum.service.RetrospectiveFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/retrospective")
public class RetrospectiveController {

    Logger logger = LoggerFactory.getLogger(RetrospectiveController.class);
    private RetrospectiveFeedbackService retrospectiveFeedbackService;

    RetrospectiveController(RetrospectiveFeedbackService retrospectiveFeedbackService) {
        this.retrospectiveFeedbackService = retrospectiveFeedbackService;
    }

    /**
     * Api to create new Retrospective
     *
     * @param retrospective
     * @return Retrospective
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Retrospective createRetrospective(@RequestBody Retrospective retrospective) {
        return retrospectiveFeedbackService.createRetrospective(retrospective);
    }

    /**
     * API to fetch all Retrospective with pagination
     *
     * @param page     - page number
     * @param pageSize - number of record per page
     * @return List<Retrospective>
     */
    @GetMapping
    public List<Retrospective> fetchAllRetrospective(@RequestParam(name = "page", defaultValue = "1") Integer page, @RequestParam(name = "pageSize", defaultValue = "1") Integer pageSize) {
        return retrospectiveFeedbackService.fetchAllRetrospective(page, pageSize);
    }

    /**
     * Search retrospective by given date.
     *
     * @param date - date format as "yyyy-MM-dd"
     * @return List<Retrospective>
     */
    @GetMapping(value = "/{date}")
    public List<Retrospective> searchRetrospective(@PathVariable String date) {
        return retrospectiveFeedbackService.searchRetrospectiveByDate(date);
    }
}
