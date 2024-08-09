package com.audition.web;

import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.PostComments;
import com.audition.service.AuditionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.audition.common.TestData.getMockPosts;
import static com.audition.common.TestData.getPost;
import static com.audition.common.TestData.getMockPostComments;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditionControllerTest {

    @InjectMocks
    private AuditionController controller;

    @Mock
    AuditionLogger logger;

    @Mock
    private AuditionService auditionService;


    @Test
    void testGetPostsWithoutUserId() throws Exception {

        final List<AuditionPost> expected = getMockPosts();
        when(auditionService.getPosts()).thenReturn(expected);

        final List<AuditionPost> result =  controller.getPosts(null);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .containsExactlyElementsOf(expected);
    }

    @Test
    void testGetPostsWithUserId() throws Exception {
        final List<AuditionPost> mockPosts = getMockPosts();
        when(auditionService.getPosts()).thenReturn(mockPosts);
        final List<AuditionPost> result =  controller.getPosts(3);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result).contains(getPost(1, 3, "body", "title"));
    }

    @Test
    void testGetPostById() throws Exception {
        final AuditionPost ap1 =  getPost(1, 3, "body1", "title");
        when(auditionService.getPostById(1)).thenReturn(ap1);
        final AuditionPost result =  controller.getPostById(1);

        assertThat(result).isEqualTo(ap1);
    }

    @Test
    void testGetCommentsByPostId() throws Exception {
        final List<PostComments> expected = getMockPostComments();
        when(auditionService.getCommentsByPostId(anyInt())).thenReturn(getMockPostComments());
        final List<PostComments> result =  controller.getCommentsByPostId(1);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .containsExactlyElementsOf(expected);
    }


}

