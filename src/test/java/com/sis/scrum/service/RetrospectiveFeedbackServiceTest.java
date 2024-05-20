package com.sis.scrum.service;

import com.sis.scrum.enums.FeedbackType;
import com.sis.scrum.exception.NotAuthorisedUserException;
import com.sis.scrum.exception.RetrospectiveAlreadyExistException;
import com.sis.scrum.exception.RetrospectiveNotFoundException;
import com.sis.scrum.model.Feedback;
import com.sis.scrum.model.Retrospective;
import com.sis.scrum.service.Impl.RetrospectiveFeedbackServiceImpl;
import com.sis.scrum.util.PagenationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class RetrospectiveFeedbackServiceTest {
    private Map<String, Retrospective> retrospectiveMap = new HashMap<>();
    @InjectMocks
    private RetrospectiveFeedbackServiceImpl retrospectiveFeedbackService;

    @Mock
    private PagenationUtil pagenationUtil;

    @BeforeEach
    public void setUp() {
        this.retrospectiveMap = new HashMap<>();
        Map<String, Feedback> feedbackMap1 = new HashMap<>();
        feedbackMap1.put("item 1", new Feedback("Vel", "report", FeedbackType.POSITIVE, "Retrospective 1"));
        Map<String, Feedback> feedbackMap2 = new HashMap<>();
        feedbackMap2.put("item 1", new Feedback("Shan", "report", FeedbackType.POSITIVE, "Retrospective 2"));
        feedbackMap2.put("item 2", new Feedback("Vel", "report", FeedbackType.POSITIVE, "Retrospective 2"));
        retrospectiveMap.put("Retrospective 0", new Retrospective("Retrospective 0", "Post release retrospective", LocalDate.parse("2024-05-20"), Arrays.asList("Shan", "Vel"), null));
        retrospectiveMap.put("Retrospective 1", new Retrospective("Retrospective 1", "Post release retrospective", LocalDate.parse("2024-05-21"), Arrays.asList("Shan", "Vel"), feedbackMap1));
        retrospectiveMap.put("Retrospective 2", new Retrospective("Retrospective 2", "Post release retrospective", LocalDate.parse("2024-05-22"), Arrays.asList("Shan", "Murugan","Vel"), feedbackMap2));
        ReflectionTestUtils.setField(retrospectiveFeedbackService,
                "retrospectiveMap",
                retrospectiveMap);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createRetrospective_whenValidRetrospectiveGiven_thenReturnCreatedRetrospective()
            throws Exception {
        //given
        Retrospective retrospective = new Retrospective("Retrospective 3", "Post release retrospective", LocalDate.parse("2024-05-20"), Arrays.asList("Shan", "Vel"), null);
        //when
        Retrospective result = retrospectiveFeedbackService.createRetrospective(retrospective);

        Retrospective actualRetrospective = this.retrospectiveMap.get("Retrospective 3");
        assertEquals(actualRetrospective.getName(), retrospective.getName());
        assertEquals(actualRetrospective.getSummary(), retrospective.getSummary());
        assertNotNull(result);
        assertEquals(result.getName(), retrospective.getName());
    }

    @Test
    public void createRetrospective_whenDuplicateRetrospectiveGiven_thenThrowAlreadyRetrospectiveExistException()
            throws Exception {
        //given
        Retrospective retrospective = new Retrospective("Retrospective 2", "Post release retrospective", LocalDate.parse("2024-05-20"), Arrays.asList("shan", "vel"), null);

        //when
        assertThrows(RetrospectiveAlreadyExistException.class, () -> {
            retrospectiveFeedbackService.createRetrospective(retrospective);
        });
    }

    @Test
    public void fetchAllRetrospective_whenRetrospectiveAvailable_thenReturnRetrospectiveList()
            throws Exception {
        when(pagenationUtil.getPage(anyList(), anyInt(), anyInt()))
                .thenReturn(new ArrayList<>(this.retrospectiveMap.values()));
        //when
        List<Retrospective> result = retrospectiveFeedbackService.fetchAllRetrospective(1, 5);
        //then
        assertNotNull(result);
        assertEquals(this.retrospectiveMap.size(), result.size());
    }

    @Test
    public void searchRetrospective_whenValidRetrospectiveGiven_thenReturnRetrospective()
            throws Exception {
        //given

        //when
        List<Retrospective> result = retrospectiveFeedbackService.searchRetrospectiveByDate("2024-05-20");
        Retrospective actual = result.get(0);
        assertNotNull(result);
        assertEquals(result.size(), 1);
        assertEquals(actual.getName(), "Retrospective 0");

    }
    @Test
    public void createFeedback_whenValidFeedbackGiven_thenReturnCreatedFeedback() {
        Feedback feedback = new Feedback("Vel", "report-summary", FeedbackType.IDEA, "Retrospective 0");
        //when
        Feedback result = retrospectiveFeedbackService.createFeedback(feedback);
        //then
        Retrospective actualRretrospective = this.retrospectiveMap.get("Retrospective 0");
        Map<String, Feedback> resultFeedback = actualRretrospective.getFeedback();
        Feedback actual = resultFeedback.get("item 1");

        assertNotNull(result);
        assertEquals(feedback.getName(), actual.getName());
        assertEquals(feedback.getBody(), actual.getBody());
        assertEquals(feedback.getFeedbackType(), actual.getFeedbackType());
        assertNotNull(actualRretrospective.getFeedback());

    }
    @Test
    public void createFeedback_whenInValiduserFeedbackGiven_thenThrowNotAuthorisedException()
            throws Exception {
        //given
        Feedback feedback = new Feedback("muru", "report-summary", FeedbackType.PRAISE, "Retrospective 0");
        //when
        assertThrows(NotAuthorisedUserException.class,()-> {
             retrospectiveFeedbackService.createFeedback(feedback);
        });


    }

    @Test
    public void createFeedback_whenValidNewUserFeedbackAddExistingRetro_thenReturnCreatedFeedback()
            throws Exception {
        //given

        Feedback feedback = new Feedback("Murugan", "report-summary", FeedbackType.IDEA, "Retrospective 2");
        //when
        Feedback result = retrospectiveFeedbackService.createFeedback(feedback);

        Retrospective actualRretrospective = this.retrospectiveMap.get("Retrospective 2");
        Map<String, Feedback> resultFeedback = actualRretrospective.getFeedback();
        Feedback actual = resultFeedback.get("item 3");
        assertNotNull(result);
        assertEquals(feedback.getName(), actual.getName());
        assertEquals(feedback.getBody(), actual.getBody());
        assertEquals(feedback.getFeedbackType(), actual.getFeedbackType());
        assertNotNull(actualRretrospective.getFeedback());
    }


    @Test
    public void updateFeedback_whenValidFeedbackUpdateGiven_thenReturnUpdatedFeedback()
            throws Exception {
        //given

        Feedback feedback = new Feedback("Vel", "report", FeedbackType.IDEA, "Retrospective 2");
        //when
        Feedback result = retrospectiveFeedbackService.updateFeedback(feedback);

        Retrospective actualRretrospective = this.retrospectiveMap.get("Retrospective 2");
        Map<String, Feedback> actualFeedbackMap = actualRretrospective.getFeedback();

        Feedback actual = actualFeedbackMap.get("item 2");
        assertNotNull(result);
        assertNotNull(actualRretrospective.getFeedback());
        assertEquals(result.getName(), actual.getName());
        assertEquals(result.getBody(), actual.getBody());

    }


    @Test
    public void createFeedback_whenInvalidRetospectiveFeedbackGiven_thenThrowRetrospectiveNotFoundException()
            throws Exception {
        //when
        assertThrows(RetrospectiveNotFoundException.class, () -> {
            retrospectiveFeedbackService.createFeedback(new Feedback("Vel", "report", FeedbackType.POSITIVE, "Retrospective 3"));
        });
    }

    @Test
    public void updateFeedback_whenInvalidRetospectiveFeedbackGiven_thenThrowRetrospectiveNotFoundException()
            throws Exception {
        //given
        Feedback feedback = new Feedback("Vel", "report", FeedbackType.POSITIVE, "Retrospective");
        //when
        assertThrows(RetrospectiveNotFoundException.class, () -> {
            retrospectiveFeedbackService.updateFeedback(feedback);
        });

    }

}