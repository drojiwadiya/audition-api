package com.audition.web.advice;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionControllerAdviceTest {

    @InjectMocks
    private ExceptionControllerAdvice exceptionControllerAdvice;

    @Mock
    AuditionLogger logger;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleHttpClientException() {
        final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request");
        final ProblemDetail problemDetail = exceptionControllerAdvice.handleHttpClientException(exception);

        assertThat(problemDetail).isNotNull();
        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problemDetail.getDetail()).isEqualTo("400 Bad Request");
        assertThat(problemDetail.getTitle()).isEqualTo("API Error Occurred");
    }

    @Test
    void handleMainException() {
        final Exception exception = new Exception("Unexpected error");
        final ProblemDetail problemDetail = exceptionControllerAdvice.handleMainException(exception);

        assertThat(problemDetail).isNotNull();
        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(problemDetail.getDetail()).isEqualTo("Unexpected error");
        assertThat(problemDetail.getTitle()).isEqualTo("API Error Occurred");
    }

    @Test
    void handleSystemException() {
        final SystemException systemException = new SystemException("Custom error", 500);
        final ProblemDetail problemDetail = exceptionControllerAdvice.handleSystemException(systemException);

        assertThat(problemDetail).isNotNull();
        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(problemDetail.getDetail()).isEqualTo("Custom error");
        assertThat(problemDetail.getTitle()).isEqualTo("API Error Occurred");
    }
}