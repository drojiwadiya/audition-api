package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.PostComments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@SuppressWarnings({"PMD.PreserveStackTrace"})
public class AuditionIntegrationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditionIntegrationClient.class);

    @Autowired
    AuditionLogger logger;

    @Value("${client.typicode.url}")
    private String postUrl;


    @Autowired
    private RestTemplate restTemplate;

    public List<AuditionPost> getPosts() {
        //make call to get Posts from https://jsonplaceholder.typicode.com/posts
        final ResponseEntity<List<AuditionPost>> response = restTemplate.exchange(
            postUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AuditionPost>>() {
            });
        return response.getBody();
    }

    public AuditionPost getPostById(final int id) {
        final String requestUrl = postUrl + id;
        try {
            final ResponseEntity<AuditionPost> response
                = restTemplate.getForEntity(requestUrl, AuditionPost.class);
            return response.getBody();
        } catch (final HttpClientErrorException e) {
            logger.logErrorWithException(LOGGER, "Client call failed", e);
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found",
                    404);
            } else {
                throw new SystemException(e.getMessage(), "Client call failed with an unknown error for the request URL:" + requestUrl, e);
            }
        }
    }

    public List<PostComments> getCommentsByPostId(final int postId) {
       final String requestUrl = postUrl + postId + "/comments";
        try {
            final ResponseEntity<List<PostComments>> response = restTemplate.exchange(
                    requestUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<PostComments>>() {
                    });
            return response.getBody();
        } catch (final HttpClientErrorException e) {
            logger.logErrorWithException(LOGGER, "Client call failed", e);
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + postId, "Resource Not Found",
                    404);
            } else {
                throw new SystemException(e.getMessage(), "Client call failed with an unknown error for the request URL:" + requestUrl, e);
            }
        }
    }
}
