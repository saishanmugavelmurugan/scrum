package com.sis.scrum.service.Impl;

import com.sis.scrum.exception.FeedbackAlreadyExistException;
import com.sis.scrum.exception.NotAuthorisedUserException;
import com.sis.scrum.exception.RetrospectiveAlreadyExistException;
import com.sis.scrum.exception.RetrospectiveNotFoundException;
import com.sis.scrum.model.Feedback;
import com.sis.scrum.model.Retrospective;
import com.sis.scrum.service.RetrospectiveFeedbackService;
import com.sis.scrum.util.PagenationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RetrospectiveFeedbackServiceImpl implements RetrospectiveFeedbackService {
    Logger logger = LoggerFactory.getLogger(RetrospectiveFeedbackServiceImpl.class);
    private PagenationUtil pagenationUtil;
    private Map<String, Retrospective> retrospectiveMap = new HashMap<>();

    RetrospectiveFeedbackServiceImpl(PagenationUtil pagenationUtil) {
        this.pagenationUtil = pagenationUtil;
    }

    /**
     * Create new Retrospective with name as Unique key.
     *
     * @param retrospective - user input
     * @return Retrospective
     */
    @Override
    public Retrospective createRetrospective(Retrospective retrospective) {
        if (retrospectiveMap.containsKey(retrospective.getName())) {
            throw new RetrospectiveAlreadyExistException("Retrospective Already Exist:" + retrospective.getName());
        }
        retrospectiveMap.put(retrospective.getName(), retrospective);
        return retrospective;
    }

    /**
     * Fetch all Retrospective from the list.
     *
     * @param page     - Page number
     * @param pageSize - PageSize number record per page.
     * @return List<Retrospective>
     */
    @Override
    public List<Retrospective> fetchAllRetrospective(Integer page, Integer pageSize) {
        return pagenationUtil.getPage(new ArrayList<>(retrospectiveMap.values()), page, pageSize);
    }

    /**
     * Search Retrospective by Given Date.
     *
     * @param date
     * @return List<Retrospective>
     */
    @Override
    public List<Retrospective> searchRetrospectiveByDate(String date) {
        return retrospectiveMap.values().stream().filter(retro -> retro.getDate().isEqual(LocalDate.parse(date))).collect(Collectors.toList());
    }

    /**
     * Create new Feedback for the existing Retrospective.
     *
     * @param feedback - Feedback given by User.
     * @return Feedback
     */
    @Override
    public Feedback createFeedback(Feedback feedback) {
        Retrospective retrospective = retrospectiveMap.get(feedback.getRetrospective());
        if (retrospective != null) {
            if(!retrospective.getParticipants().contains(feedback.getName())){
                throw new NotAuthorisedUserException(feedback.getName()+" Not Authorised to add Feedback.");
            }
            Map<String, Feedback> feedbackMap = retrospective.getFeedback();
            if (feedbackMap != null) {
                List<Feedback> FeedbackList = new ArrayList(feedbackMap.values());
                Optional<Feedback> feedbackExist = FeedbackList.stream().filter(f -> f.getName().equals(feedback.getName())).findFirst();
                if (feedbackExist.isPresent()) {
                    throw new FeedbackAlreadyExistException("Feedback Already Exist for the user - " + feedback.getName());
                }
            } else {
                feedbackMap = new HashMap<>();
            }
            feedbackMap.put("item " + (feedbackMap.size() + 1), feedback);
            retrospective.setFeedback(feedbackMap);
            retrospectiveMap.put(feedback.getRetrospective(), retrospective);
        } else {
            throw new RetrospectiveNotFoundException("Retrospective not found.Pls.provide valid retrospective.");
        }
        return feedback;
    }

    /**
     * Update existing feedback given by user.
     *
     * @param feedback
     * @return Feedback
     */
    @Override
    public Feedback updateFeedback(Feedback feedback) {
        Retrospective retrospective = retrospectiveMap.get(feedback.getRetrospective());
        if (retrospective != null) {
            if(!retrospective.getParticipants().contains(feedback.getName())){
                throw new NotAuthorisedUserException(feedback.getName()+" Not Authorised to add Feedback.");
            }
            Map<String, Feedback> feedbackMap = retrospective.getFeedback();
            if (feedbackMap != null) {
                Optional<String> item = feedbackMap.entrySet().stream().filter(entry -> entry.getValue().getName().equals(feedback.getName())).map(entry -> entry.getKey()).findFirst();
                if(item.isPresent()){
                    feedbackMap.put(item.get(),feedback);
                }else{
                    feedbackMap.put("item " + (feedbackMap.size() + 1), feedback);
                }
            } else {
                feedbackMap = new HashMap<>();
                feedbackMap.put("item " + (feedbackMap.size() + 1), feedback);
            }

            retrospective.setFeedback(feedbackMap);
            retrospectiveMap.put(feedback.getRetrospective(), retrospective);
        } else {
            throw new RetrospectiveNotFoundException("Retrospective not found.Pls.provide valid retrospective.");
        }
        return feedback;
    }
}
