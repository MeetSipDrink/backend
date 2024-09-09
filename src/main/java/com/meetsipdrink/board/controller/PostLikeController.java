package com.meetsipdrink.board.controller;

import com.meetsipdrink.board.dto.PostLikeDto;
import com.meetsipdrink.board.mapper.PostLikeMapper;
import com.meetsipdrink.board.service.PostLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/posts/{post-id}/like")
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
                                   @RequestParam("memberId") @Positive long memberId) {
        postLikeService.checkLike(memberId, postId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getPostLike(@PathVariable("post-id") @Positive long postId,
                                      @RequestParam("memberId") @Positive long memberId) {
        boolean isLike = postLikeService.findLike(memberId, postId);

        return new ResponseEntity<>(
                new PostLikeDto.Response(isLike),
                HttpStatus.OK
        );
    }
}
