package com.audition;

import com.audition.model.AuditionPost;
import com.audition.model.PostComments;
import com.audition.web.AuditionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuditionApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AuditionController controller;

    @MockBean
    private RestTemplate mockClient;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${client.typicode.url}")
    private String clientUrl;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void apiGetPostsByUserIdShouldReturnPosts() {

        final AuditionPost ap1 = new AuditionPost();
        ap1.setId(1);
        ap1.setUserId(3);
        ap1.setBody("body");
        ap1.setTitle("title");

        final AuditionPost ap2 = new AuditionPost();
        ap2.setId(2);
        ap2.setUserId(5);
        final List<AuditionPost> auditionPosts = Arrays.asList(ap1, ap2);

        final String postUrl = getAuditionAPIBaseURL() + "/posts?userId=3";
        when(mockClient.exchange(clientUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AuditionPost>>(){}))
                .thenReturn(ResponseEntity.ok(auditionPosts));


        final ResponseEntity<List<AuditionPost>> response = restTemplate.exchange(
                postUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AuditionPost>>() {
                });

        assertThat(response.getBody().size()).isEqualTo(1);
        assertThat(response.getBody()).contains(ap1);
    }

    @Test
    void apiGetPostsWithNoUserIdShouldReturnAllPosts() {

        final AuditionPost ap1 = new AuditionPost();
        ap1.setId(1);
        ap1.setUserId(3);

        final AuditionPost ap2 = new AuditionPost();
        ap2.setId(2);
        ap2.setUserId(5);
        final List<AuditionPost> auditionPosts = Arrays.asList(ap1, ap2);

        final String postUrl = getAuditionAPIBaseURL() + "/posts";
        when(mockClient.exchange(clientUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AuditionPost>>(){}))
                .thenReturn(ResponseEntity.ok(auditionPosts));


        final ResponseEntity<List<AuditionPost>> response = restTemplate.exchange(
                postUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AuditionPost>>() {
                });

        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody()).contains(ap1);
        assertThat(response.getBody()).contains(ap2);
    }

    private String getAuditionAPIBaseURL() {
        return "http://localhost:" + port;
    }

    @Test
    void apiGetPostByIdShouldReturnAPost() {

        final AuditionPost ap1 = new AuditionPost();
        ap1.setId(1);
        ap1.setUserId(5);

        final String postUrl = getAuditionAPIBaseURL() + "/posts/1";
        when(mockClient.getForEntity(clientUrl + "1", AuditionPost.class))
                .thenReturn(ResponseEntity.ok(ap1));

        final ResponseEntity<AuditionPost> response = restTemplate.getForEntity(
                postUrl, AuditionPost.class);

        assertThat(response.getBody()).isEqualTo(ap1);
    }

    @Test
    void apiGetCommentsByPostIdShouldReturnPostComments() {
        final PostComments pc1 = new PostComments();
        pc1.setPostId(1);
        pc1.setId(1);
        pc1.setName("name");
        pc1.setBody("body");
        pc1.setEmail("test@mail.com");

        final PostComments pc2 = new PostComments();
        pc2.setPostId(1);
        pc2.setId(2);
        pc2.setName("name2");
        pc2.setBody("body2");
        pc2.setEmail("test2@mail.com");


        final List<PostComments> postComments = Arrays.asList(pc1, pc2);

        final String postUrl = getAuditionAPIBaseURL() + "/posts/1/comments";
        when(mockClient.exchange(clientUrl + "1/comments",
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PostComments>>(){}))
                .thenReturn(ResponseEntity.ok(postComments));


        final ResponseEntity<List<PostComments>> response = restTemplate.exchange(
                postUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<PostComments>>() {
                });

        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody()).contains(pc1);
        assertThat(response.getBody()).contains(pc2);
    }


}
