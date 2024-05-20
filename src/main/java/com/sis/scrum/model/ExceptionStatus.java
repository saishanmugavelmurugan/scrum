package com.sis.scrum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@Setter
public class ExceptionStatus {
    private HttpStatus httpStatus;
    private String message;

}
