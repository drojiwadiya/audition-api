package com.audition.web;

import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.PostComments;
import com.audition.service.AuditionService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@SuppressWarnings({"PMD.GuardLogStatement"})
public class AuditionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditionController.class);

    @Autowired
    AuditionLogger logger;

    @Autowired
    AuditionService auditionService;

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(
        @RequestParam(value = "userId", required = false) final Integer userId) {
        logger.info(LOGGER, "received get posts api request with param userId:" + userId);

        List<AuditionPost> posts = auditionService.getPosts();
        if (userId != null) {
            posts =  posts.stream().filter(p -> p.getUserId() == userId).collect(Collectors.toList());
        }
        return posts;
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostById(@PathVariable("id") final int postId) {
        logger.info(LOGGER, "received get posts api request with postId:" + postId);
        return  auditionService.getPostById(postId);
    }

    @RequestMapping(value = "/posts/{id}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<PostComments> getCommentsByPostId(@PathVariable("id") final int postId) {
        logger.info(LOGGER, "received get posts comments api request with postId:" + postId);
        return auditionService.getCommentsByPostId(postId);
    }
}
