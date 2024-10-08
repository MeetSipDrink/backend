package com.meetsipdrink.board.controller;

import com.meetsipdrink.board.dto.PostCommentDto;
import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostComment;
import com.meetsipdrink.board.mapper.PostCommentMapper;
import com.meetsipdrink.board.service.PostCommentService;
import com.meetsipdrink.board.service.PostService;
import com.meetsipdrink.dto.SingleResponseDto;
import com.meetsipdrink.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/posts/{post-id}/comments")
public class PostCommentController {
    private final static String POST_COMMENT_DEFAULT_URL = "/posts/{post-id}/comments";
    private final PostCommentService postCommentService;
    private final PostService postService;
    private final PostCommentMapper mapper;

    public PostCommentController(PostCommentService postCommentService,
                                 PostService postService,
                                 PostCommentMapper mapper) {
        this.postCommentService = postCommentService;
        this.postService = postService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postPostComment(@PathVariable("post-id") @Positive long postId,
                                          @Valid @RequestBody PostCommentDto.Post requestBody,
                                          @AuthenticationPrincipal Object principal) {
        requestBody.setEmail(principal.toString());
        PostComment postComment = mapper.postCommentPostDtoToPostComment(requestBody);

        Post post = postService.findPostById(postId);
        postComment.setPost(post);

        PostComment createPostComment = postCommentService.createPostComment(postComment);

        URI location = UriCreator.createUri(POST_COMMENT_DEFAULT_URL.replace("{post-id}", String.valueOf(postId)), createPostComment.getPostCommentId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{comment-id}")
    public ResponseEntity patchPostComment(@PathVariable("comment-id") @Positive long postCommentId,
                                           @Valid @RequestBody PostCommentDto.Patch requestBody,
                                           @AuthenticationPrincipal Object principal) {
        requestBody.setEmail(principal.toString());
        PostComment updatePostComment = mapper.postCommentPatchDtoToPostComment(requestBody);
        PostComment postComment = postCommentService.updatePostComment(postCommentId, updatePostComment, requestBody.getEmail());
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.postCommentToPostCommentResponseDto(postComment)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PostCommentDto.Response>> getPostComments(@PathVariable("post-id") @Positive long postId) {
        List<PostComment> postComments = postCommentService.findPostCommentsByPostId(postId);
        List<PostCommentDto.Response> responseDtos = mapper.postCommentsToPostCommentResponseDtos(postComments);

        // 빈 리스트일 경우에도 200 OK 응답을 반환
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }


    @DeleteMapping("/{comment-id}")
    public ResponseEntity deletePostComment(@PathVariable("comment-id") @Positive long postCommentId,
                                            @AuthenticationPrincipal Object principal) {
        postCommentService.deletePostComment(postCommentId, principal.toString());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
