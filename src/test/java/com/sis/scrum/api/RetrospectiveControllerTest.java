package com.sis.scrum.api;

import com.google.gson.Gson;
import com.sis.scrum.enums.FeedbackType;
import com.sis.scrum.exception.RetrospectiveAlreadyExistException;
import com.sis.scrum.exception.RetrospectiveExceptionHandler;
import com.sis.scrum.model.Feedback;
import com.sis.scrum.model.Retrospective;
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

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@AutoConfigureMockMvc
public class RetrospectiveControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private RetrospectiveController retrospectiveController;
    Gson gson = new Gson();

    @Mock
    private RetrospectiveFeedbackService retrospectiveFeedbackService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = standaloneSetup(retrospectiveController)
                .setControllerAdvice(new RetrospectiveExceptionHandler()).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createRetrospective_whenValidRetrospectiveGiven_thenReturnStatus201()
            throws Exception {

        //given
        when(retrospectiveFeedbackService.createRetrospective(any()))
                .thenReturn(new Retrospective("Retrospective 3", "Post release retrospective", LocalDate.parse("2024-05-17"), Arrays.asList("shan", "vel"), null));
        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/retrospective")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Retrospective 3\",\"summary\": \"Post release retrospective\",\"date\": \"2024-05-17\",\"participants\": [\"vel\", \"shan\" ]}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"name\": \"Retrospective 3\",\"summary\": \"Post release retrospective\",\"date\": [2024,5,17],\"participants\": [\"vel\", \"shan\" ]}"));
        //then
        verify(retrospectiveFeedbackService, times(1)).createRetrospective(any());
    }

    @Test
    public void createRetrospective_whenDuplicteRetrospectiveGiven_thenReturnStatus409Conflict()
            throws Exception {

        //given

        when(retrospectiveFeedbackService.createRetrospective(any()))
                .thenThrow(new RetrospectiveAlreadyExistException("Retrospective Already Exist."));

        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.post("/retrospective")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Retrospective 3\",\"summary\": \"Post release retrospective\",\"date\": \"2024-05-17\",\"participants\": [\"vel\", \"victory\",\"shan\" ]}"))
                .andExpect(status().isConflict());
        //then
        verify(retrospectiveFeedbackService, times(1)).createRetrospective(any());
    }

    @Test
    public void fetchAllRetrospective_whenValidDateGiven_thenReturnDataWithStatus200()
            throws Exception {

        //given
        Feedback feedback = new Feedback("vel", "report", FeedbackType.POSITIVE, "Retrospective 3");
        Map<String, Feedback> feedbackMap = new HashMap<>();
        feedbackMap.put("item1", feedback);
        Retrospective retrospective = new Retrospective("Retrospective 3", "Post release retrospective", LocalDate.parse("2024-05-17"), Arrays.asList("shan", "vel"), feedbackMap);
        List<Retrospective> retrospectiveList = new ArrayList<>();
        retrospectiveList.add(retrospective);
        when(retrospectiveFeedbackService.fetchAllRetrospective(any(), any()))
                .thenReturn(retrospectiveList);
        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/retrospective?page=1&pageSize=2")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());

        //then
        verify(retrospectiveFeedbackService, times(1)).fetchAllRetrospective(any(), any());
    }

    @Test
    public void searchRetrospective_whenValidDateGiven_thenReturnDataWithStatus200()
            throws Exception {

        //given
        Feedback feedback = new Feedback("vel", "report", FeedbackType.POSITIVE, "Retrospective 3");
        Map<String, Feedback> feedbackMap = new HashMap<>();
        feedbackMap.put("item1", feedback);
        Retrospective retrospective = new Retrospective("Retrospective 3", "Post release retrospective", LocalDate.parse("2024-05-17"), Arrays.asList("shan", "vel"), feedbackMap);
        List<Retrospective> retrospectiveList = new ArrayList<>();
        retrospectiveList.add(retrospective);
        when(retrospectiveFeedbackService.searchRetrospectiveByDate(any()))
                .thenReturn(retrospectiveList);
        //when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/retrospective/2024-05-17")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());

        //then
        verify(retrospectiveFeedbackService, times(1)).searchRetrospectiveByDate(any());
    }

}
