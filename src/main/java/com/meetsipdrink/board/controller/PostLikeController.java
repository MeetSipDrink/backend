package com.meetsipdrink.board.controller;

import com.meetsipdrink.board.dto.PostLikeDto;
import com.meetsipdrink.board.mapper.PostLikeMapper;
import com.meetsipdrink.board.service.PostLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/posts/{post-id}/likes")
@Validated
public class PostLikeController {
    private final PostLikeService postLikeService;
    private final PostLikeMapper postLikeMapper;

    public PostLikeController(PostLikeService postLikeService,
                              PostLikeMapper postLikeMapper) {
        this.postLikeService = postLikeService;
        this.postLikeMapper = postLikeMapper;
    }

    @PostMapping
    public ResponseEntity PostLike(@PathVariable("post-id") @Positive long postId,
                                   @AuthenticationPrincipal Object principal) {
        postLikeService.checkLike(principal.toString(), postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getPostLike(@PathVariable("post-id") @Positive long postId,
                                      @AuthenticationPrincipal Object principal) {
        boolean isLike = postLikeService.findLike(principal.toString(), postId);

        return new ResponseEntity<>(
                new PostLikeDto.Response(isLike),
                HttpStatus.OK
        );
    }
}
