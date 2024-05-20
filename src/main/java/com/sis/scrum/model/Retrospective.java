package com.sis.scrum.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class Retrospective {

    @NotNull(message = "Retrospective name can not be null")
    private String name;
    private String summary;
    @NotNull(message = "Date can not be null")
    private LocalDate date;
    @NotNull(message = "Participants can not be null")
    private List<String> participants;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, Feedback> feedback;

}

