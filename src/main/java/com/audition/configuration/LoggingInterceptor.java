package com.audition.configuration;

import com.audition.common.logging.AuditionLogger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Component
@SuppressWarnings({"PMD.GuardLogStatement"})
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Autowired
    AuditionLogger auditionLogger;

    @Override
    public ClientHttpResponse intercept(
        final HttpRequest req, final byte[] reqBody, final ClientHttpRequestExecution ex) throws IOException {

        auditionLogger.debug(LOGGER, "Client Request URI:" + req.getURI());
        auditionLogger.debug(LOGGER, "Client Request body:" + new String(reqBody, StandardCharsets.UTF_8));
        final ClientHttpResponse response = ex.execute(req, reqBody);
        try (InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)) {
            final String body = new BufferedReader(isr).lines()
                .collect(Collectors.joining("\n"));
            auditionLogger.debug(LOGGER, "Client Response body:" + body);
        }
        return response;
    }
}

