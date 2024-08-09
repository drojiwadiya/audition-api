package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.PostComments;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import java.util.List;

import static com.audition.common.TestData.getMockPostComments;
import static com.audition.common.TestData.getMockPosts;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withTooManyRequests;

@RestClientTest(AuditionIntegrationClient.class)
@AutoConfigureWebClient(registerRestTemplate = true)
class AuditionIntegrationClientTest {

    @Autowired
    private AuditionIntegrationClient integrationClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AuditionLogger auditionLogger;

    @Autowired
    private MockRestServiceServer server;

    private static final String CLIENT_URL = "https://jsonplaceholder.typicode.com/posts/";
    private static final String POST_URL_VARIABLE = "postUrl";

    @Test
    void testGetPosts() throws JsonProcessingException {
        final List<AuditionPost> posts = getMockPosts();
        final String postsJson = objectMapper.writeValueAsString(posts);

        ReflectionTestUtils.setField(integrationClient, POST_URL_VARIABLE, CLIENT_URL);

        this.server.expect(requestTo(CLIENT_URL))
                .andRespond(withSuccess(postsJson, MediaType.APPLICATION_JSON));

        final List<AuditionPost> result = integrationClient.getPosts();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .containsExactlyElementsOf(posts);

    }

    @Test
    void testGetPostById() throws JsonProcessingException {
        final List<AuditionPost> mockPosts = getMockPosts();
        final AuditionPost expected = mockPosts.get(0);

        final String postsJson = objectMapper.writeValueAsString(expected);

        ReflectionTestUtils.setField(integrationClient, POST_URL_VARIABLE, CLIENT_URL);

        this.server.expect(requestTo(CLIENT_URL + "1"))
                .andRespond(withSuccess(postsJson, MediaType.APPLICATION_JSON));

        final AuditionPost result = integrationClient.getPostById(1);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void testGetCommentsByPostId() throws JsonProcessingException {
        final List<PostComments> expected = getMockPostComments();
        final String postsJson = objectMapper.writeValueAsString(expected);

        ReflectionTestUtils.setField(integrationClient, POST_URL_VARIABLE, CLIENT_URL);

        this.server.expect(requestTo(CLIENT_URL + "1/comments"))
                .andRespond(withSuccess(postsJson, MediaType.APPLICATION_JSON));

        final List<PostComments> result = integrationClient.getCommentsByPostId(1);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .containsExactlyElementsOf(expected);
    }

    @Test
    void testGetCommentsByPostIdWhen404ExceptionThrown() throws JsonProcessingException {

        ReflectionTestUtils.setField(integrationClient, POST_URL_VARIABLE, CLIENT_URL);

        this.server.expect(requestTo(CLIENT_URL + "40/comments"))
                .andRespond(withResourceNotFound());

        final SystemException thrownException = assertThrows(SystemException.class, () -> {
            integrationClient.getCommentsByPostId(40);
        });
        assertEquals("Cannot find a Post with id 40", thrownException.getMessage());
        assertThat(thrownException.getStatusCode()).isEqualTo(404);
    }

    @Test
    void testGetCommentsByPostIdWhenOtherExceptionThrown() throws JsonProcessingException {

        ReflectionTestUtils.setField(integrationClient, POST_URL_VARIABLE, CLIENT_URL);

        this.server.expect(requestTo(CLIENT_URL + "429/comments"))
                .andRespond(withTooManyRequests());

        final SystemException thrownException = assertThrows(SystemException.class, () -> {
            integrationClient.getCommentsByPostId(429);
        });
        assertThat(thrownException.getTitle()).contains("Client call failed with an unknown error for the request URL:");
    }

    @Test
    void testGetPostByIdWhen404ExceptionThrown() throws JsonProcessingException {
        ReflectionTestUtils.setField(integrationClient, POST_URL_VARIABLE, CLIENT_URL);

        this.server.expect(requestTo(CLIENT_URL + "40"))
                .andRespond(withResourceNotFound());

        final SystemException thrownException = assertThrows(SystemException.class, () -> {
            integrationClient.getPostById(40);
        });
        assertEquals("Cannot find a Post with id 40", thrownException.getMessage());
        assertThat(thrownException.getStatusCode()).isEqualTo(404);
    }

    @Test
    void testGetPostByIdWhenOtherExceptionThrown() throws JsonProcessingException {
        ReflectionTestUtils.setField(integrationClient, POST_URL_VARIABLE, CLIENT_URL);

        this.server.expect(requestTo(CLIENT_URL + "429"))
                .andRespond(withTooManyRequests());

        final SystemException thrownException = assertThrows(SystemException.class, () -> {
            integrationClient.getPostById(429);
        });
        assertThat(thrownException.getTitle()).contains("Client call failed with an unknown error for the request URL:");
    }
}