package com.audition.common.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

class AuditionLoggerTest {

    @Mock
    private Logger logger;
    private AuditionLogger auditionLogger;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        auditionLogger = new AuditionLogger();
    }

    @Test
    void testInfo() {
        when(logger.isInfoEnabled()).thenReturn(true);

        final String message = "Info message";
        auditionLogger.info(logger, message);

        verify(logger).info(message);
    }

    @Test
    void testInfoNotEnabled() {
        when(logger.isInfoEnabled()).thenReturn(false);

        final String message = "Info message";
        auditionLogger.info(logger, message);

        verify(logger, never()).info(message);
    }

    @Test
    void testDebug() {
        when(logger.isDebugEnabled()).thenReturn(true);

        final String message = "Debug message";
        auditionLogger.debug(logger, message);

        verify(logger).debug(message);
    }

    @Test
    void testDebugNotEnabled() {
        when(logger.isDebugEnabled()).thenReturn(false);

        final String message = "Debug message";
        auditionLogger.debug(logger, message);

        verify(logger, never()).debug(message);
    }

    @Test
    void testError() {
        when(logger.isErrorEnabled()).thenReturn(true);

        final String message = "Error message";
        auditionLogger.error(logger, message);

        verify(logger).error(message);
    }

    @Test
    void testErrorNotEnabled() {
        when(logger.isErrorEnabled()).thenReturn(false);

        final String message = "Error message";
        auditionLogger.error(logger, message);

        verify(logger, never()).error(message);
    }

}