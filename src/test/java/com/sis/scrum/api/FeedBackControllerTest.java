package com.sis.scrum.api;

import com.google.gson.Gson;
import com.sis.scrum.exception.FeedbackAlreadyExistException;
import com.sis.scrum.exception.RetrospectiveExceptionHandler;
import com.sis.scrum.exception.RetrospectiveNotFoundException;
import com.sis.scrum.model.Feedback;
import com.sis.scrum.service.RetrospectiveFeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
public class FeedBackControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private FeedBackController feedBackController;

    Gson gson = new Gson();

    @Mock
    private RetrospectiveFeedbackService retrospectiveFeedbackService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = standaloneSetup(feedBackController)
                .setControllerAdvice(new RetrospectiveExceptionHandler()).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createFeedBack_whenValidFeedbackGiven_thenReturnStatus201()
            throws Exception {

        //given
        String feedback = "{\"name\":\"vel\",\"body\":\"summary report\",\"feedbackType\":\"Positive\",\"retrospective\":\"Retrospective 3\"}";
        when(retrospectiveFeedbackService.createFeedback(any()))
                .thenReturn(new Feedback());

        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(feedback))
                .andExpect(status().isCreated());

        //then
        verify(retrospectiveFeedbackService, times(1)).createFeedback(any());
    }

    @Test
    public void createFeedBack_whenDuplicteRetrospectiveGiven_thenReturnStatus409Conflict()
            throws Exception {

        //given
        String feedback = "{\"name\":\"vel\",\"body\":\"summary report\",\"feedbackType\":\"Positive\",\"retrospective\":\"Retrospective 3\"}";
        when(retrospectiveFeedbackService.createFeedback(any()))
                .thenThrow(new FeedbackAlreadyExistException("Feedback Already Exist."));

        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(feedback))
                .andExpect(status().isConflict());
        //then
        verify(retrospectiveFeedbackService, times(1)).createFeedback(any());
    }

    @Test
    public void updateFeedBack_whenValidFeedbackGiven_thenReturnStatus200()
            throws Exception {

        //given
        String feedback = "{\"name\":\"vel\",\"body\":\"summary report\",\"feedbackType\":\"Positive\",\"retrospective\":\"Retrospective 3\"}";
        when(retrospectiveFeedbackService.updateFeedback(any()))
                .thenReturn(new Feedback());

        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.put("/feedback/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(feedback))
                .andExpect(status().isOk());

        //then
        verify(retrospectiveFeedbackService, times(1)).updateFeedback(any());
    }

    @Test
    public void updateFeedBack_whenInvalidRetrospectiveGiven_thenReturnStatus404Notfound()
            throws Exception {

        //given
        String feedback = "{\"name\":\"vel\",\"body\":\"summary report\",\"feedbackType\":\"Positive\",\"retrospective\":\"dummy\"}";
        when(retrospectiveFeedbackService.updateFeedback(any()))
                .thenThrow(new RetrospectiveNotFoundException("Retrospective not found to update feedback"));

        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.put("/feedback/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(feedback))
                .andExpect(status().isNotFound());
        //then
        verify(retrospectiveFeedbackService, times(1)).updateFeedback(any());
    }
}
