package com.meetsipdrink.board.controller;

import com.meetsipdrink.board.dto.PostImageDto;
import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostImage;
import com.meetsipdrink.board.mapper.PostImageMapper;
import com.meetsipdrink.board.service.PostImageService;
import com.meetsipdrink.board.service.PostService;
import com.meetsipdrink.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/posts/{post-id}/image")
public class PostImageController {
    private final static String POST_IMAGE_DEFAULT_URL = "/posts/{post-id}/image";
    private final PostImageService postImageService;
    private final PostService postService;
    private final PostImageMapper mapper;

    public PostImageController(PostImageService postImageService, PostService postService, PostImageMapper mapper) {
        this.postImageService = postImageService;
        this.postService = postService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postPostImage(@PathVariable("post-id") @Positive long postId,
                                        @Valid @RequestBody PostImageDto.Post requestBody) {
        PostImage postImage = mapper.postImagePostDtoToPostImage(requestBody);

        Post post = postService.findPostById(postId);
        postImage.setPost(post);

        PostImage createPostImage = postImageService.createPostImage(postImage);

        URI location = UriCreator.createUri(POST_IMAGE_DEFAULT_URL.replace("{post-id}", String.valueOf(postId)), createPostImage.getPostImageId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{image-id}")
    public ResponseEntity deletePostImage(@PathVariable("image-id") @Positive long postImageId) {
        postImageService.deletePostImage(postImageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
