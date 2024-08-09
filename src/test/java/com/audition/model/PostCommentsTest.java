package com.audition.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.audition.common.TestData.getMockPostComments;
import static org.assertj.core.api.Assertions.assertThat;

class PostCommentsTest {

    @Test
    void testEquals() {
        final List<PostComments> postComments = getMockPostComments();

        assertThat(postComments.get(0)).isNotEqualTo(postComments.get(1));

        final PostComments pc1 = new PostComments();
        pc1.setPostId(1);
        pc1.setId(1);
        pc1.setName("name");
        pc1.setBody("body3");
        pc1.setEmail("test@mail.com");

        assertThat(pc1).isEqualTo(postComments.get(0));
    }

    @Test
    void testHashCode() {
        final List<PostComments> postComments = getMockPostComments();

        assertThat(postComments.get(0).hashCode()).isNotEqualTo(postComments.get(1).hashCode());

        final PostComments pc1 = new PostComments();
        pc1.setPostId(1);
        pc1.setId(1);
        pc1.setName("name");
        pc1.setBody("body3");
        pc1.setEmail("test@mail.com");

        assertThat(pc1.hashCode()).isEqualTo(postComments.get(0).hashCode());
    }
}