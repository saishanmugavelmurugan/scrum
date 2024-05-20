package com.sis.scrum.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(SpringExtension.class)
public class RetrospectiveExceptionHandlerTest {
    @InjectMocks
    private RetrospectiveExceptionHandler retrospectiveExceptionHandler;


    @Test
    public void whenRetrospectiveNotFoundExceptionIsThrown_thenShouldReturnResponseStatus_NotFoundError() {
        //Given
        RetrospectiveNotFoundException retrospectiveNotFoundException = new RetrospectiveNotFoundException("Unexpected error");

        //When
        ResponseEntity<Object> responseError = retrospectiveExceptionHandler.handleNotFound(retrospectiveNotFoundException);

        //Then
        assertThat(responseError.getStatusCode(), is(equalTo(NOT_FOUND)));

    }

    @Test
    public void whenRetrospectiveAlreadyExistExceptionIsThrown_thenShouldReturnResponseStatus_AlreadyExistError() {
        //Given
        RetrospectiveAlreadyExistException retrospectiveAlreadyExistException = new RetrospectiveAlreadyExistException("Already exist");

        //When
        ResponseEntity<Object> responseError = retrospectiveExceptionHandler.handleAlreadyExist(retrospectiveAlreadyExistException);

        //Then
        assertThat(responseError.getStatusCode(), is(equalTo(CONFLICT)));

    }
    @Test
    public void whenFeedbackAlreadyExistExceptionIsThrown_thenShouldReturnResponseStatus_AlreadyExistError() {
        //Given
        FeedbackAlreadyExistException feedbackAlreadyExistException = new FeedbackAlreadyExistException("Already exist");

        //When
        ResponseEntity<Object> responseError = retrospectiveExceptionHandler.handleAlreadyExist(feedbackAlreadyExistException);

        //Then
        assertThat(responseError.getStatusCode(), is(equalTo(CONFLICT)));

    }

}
