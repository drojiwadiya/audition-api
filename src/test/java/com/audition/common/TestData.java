package com.audition.common;

import com.audition.model.AuditionPost;
import com.audition.model.PostComments;

import java.util.Arrays;
import java.util.List;

public class TestData {

    public static List<AuditionPost> getMockPosts() {
        return Arrays.asList(getPost(1, 3, "body", "title"),
                getPost(2, 5, "body2", "title2"));
    }

    public static AuditionPost getPost(final int id, final int userId,
                                        final String body, final String title) {
        final AuditionPost ap1 = new AuditionPost();
        ap1.setId(id);
        ap1.setUserId(userId);
        ap1.setBody(body);
        ap1.setTitle(title);
        return ap1;
    }

    public static List<PostComments> getMockPostComments() {
        final PostComments pc1 = new PostComments();
        pc1.setPostId(1);
        pc1.setId(1);
        pc1.setName("name");
        pc1.setBody("body3");
        pc1.setEmail("test@mail.com");

        final PostComments pc2 = new PostComments();
        pc2.setPostId(1);
        pc2.setId(2);
        pc2.setName("name2");
        pc2.setBody("body2");
        pc2.setEmail("test2@mail.com");
        return Arrays.asList(pc1, pc2);
    }
}
