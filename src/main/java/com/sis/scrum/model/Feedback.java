package com.sis.scrum.model;

import com.sis.scrum.enums.FeedbackType;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {


    @NotNull(message = "Name may not be null")
    private String name;
    @NotNull(message = "Body may not be null")
    private String body;
    @NotNull(message = "FeedbackTYpe may not be null")
    private FeedbackType feedbackType;
    @NotNull(message = "retrospective may not be null")
    private String retrospective;

}

