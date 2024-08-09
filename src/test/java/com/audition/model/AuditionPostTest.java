package com.audition.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.audition.common.TestData.getMockPosts;
import static com.audition.common.TestData.getPost;
import static org.assertj.core.api.Assertions.assertThat;

class AuditionPostTest {

    @Test
    void testEquals() {
        final List<AuditionPost> posts = getMockPosts();

        assertThat(posts.get(0)).isNotEqualTo(posts.get(1));

        final AuditionPost ap1 = getPost(1, 3, "body", "title");
        assertThat(ap1).isEqualTo(posts.get(0));
    }

    @Test
    void testHashCode() {
        final List<AuditionPost> posts = getMockPosts();

        assertThat(posts.get(0).hashCode()).isNotEqualTo(posts.get(1).hashCode());

        final AuditionPost ap1 = getPost(1, 3, "body", "title");
        assertThat(ap1.hashCode()).isEqualTo(posts.get(0).hashCode());
    }
}