package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.PostComments;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.audition.common.TestData.getMockPosts;
import static com.audition.common.TestData.getMockPostComments;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditionServiceTest {

    @InjectMocks
    private AuditionService auditionService;

    @Mock
    private AuditionIntegrationClient client;

    @Test
    void testGetPosts() {
        final List<AuditionPost> expected = getMockPosts();
        when(client.getPosts()).thenReturn(expected);

        final List<AuditionPost> result =  auditionService.getPosts();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .containsExactlyElementsOf(expected);
    }

    @Test
    void testGetPostById() {
        final List<AuditionPost> mockPosts = getMockPosts();
        final AuditionPost expected = mockPosts.get(0);
        when(client.getPostById(1)).thenReturn(expected);

        final AuditionPost result =  auditionService.getPostById(1);

        assertThat(result).isEqualTo(expected);

    }

    @Test
    void testGetCommentsByPostId() {
        final List<PostComments> expected = getMockPostComments();
        when(client.getCommentsByPostId(1)).thenReturn(expected);
        final List<PostComments> result =  auditionService.getCommentsByPostId(1);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .containsExactlyElementsOf(expected);
    }
}