package com.sis.scrum.service;

import com.sis.scrum.model.Feedback;
import com.sis.scrum.model.Retrospective;

import java.util.List;

public interface RetrospectiveFeedbackService {
    Retrospective createRetrospective(Retrospective retrospective);

    List<Retrospective> fetchAllRetrospective(Integer page,Integer pageSize);

    List<Retrospective> searchRetrospectiveByDate(String date);
    Feedback createFeedback(Feedback feedback);

    Feedback updateFeedback(Feedback feedback);
}
