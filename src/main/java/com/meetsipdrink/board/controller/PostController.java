package com.meetsipdrink.board.controller;

import com.meetsipdrink.board.dto.PostDto;
import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.mapper.PostMapper;
import com.meetsipdrink.board.service.PostService;
import com.meetsipdrink.dto.MultiResponseDto;
import com.meetsipdrink.dto.SingleResponseDto;
import com.meetsipdrink.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final static String POST_DEFAULT_URL = "/posts";
    private final PostService postService;
    private final PostMapper mapper;

    public PostController(PostService postService, PostMapper mapper) {
        this.postService = postService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postPost(@Valid @RequestBody PostDto.Post requestBody) throws IllegalArgumentException {
        Post post = mapper.postPostDtoToPost(requestBody);
        Post createPost = postService.createPost(post);
        URI location = UriCreator.createUri(POST_DEFAULT_URL, createPost.getPostId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity patchPost(@PathVariable("post-id") @Positive long postId,
                                    @Valid @RequestBody PostDto.Patch requestBody) {
        Post post = postService.updatePost(postId, mapper.postPatchDtoToPost(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.postToPostResponseDto(post)), HttpStatus.OK);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity getPost(@PathVariable("post-id") @Positive long postId) {
        Post post = postService.findPost(postId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.postToPostResponseDto(post)), HttpStatus.OK);
    }

    @GetMapping("/search/title")
    public ResponseEntity searchTitle( @RequestParam("keyword") String keyword,
                                       @RequestParam(required = false) String sort,
                                       @PageableDefault(sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {

        if (sort != null) {
            Sort sortOrder = Sort.by(sort.split("_")[0]).ascending();
            if (sort.split("_")[1].equalsIgnoreCase("desc")) {
                sortOrder = sortOrder.descending();
            }
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);
        }

        Page<Post> searchList = postService.searchPostTitle(pageable, keyword);
        List<PostDto.Response> responseList = searchList.stream()
                .map(mapper::postToPostResponseDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                new MultiResponseDto<>(responseList, searchList), HttpStatus.OK);
    }
    @GetMapping("/search/content")
    public ResponseEntity searchContent(@RequestParam("keyword") String keyword,
                                        @RequestParam(required = false) String sort,
                                        @PageableDefault(sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {

        if (sort != null) {
            Sort sortOrder = Sort.by(sort.split("_")[0]).ascending();
            if (sort.split("_")[1].equalsIgnoreCase("desc")) {
                sortOrder = sortOrder.descending();
            }
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);
        }

        Page<Post> searchList = postService.searchPostContent(pageable, keyword);
        List<PostDto.Response> responseList = searchList.stream()
                .map(mapper::postToPostResponseDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                new MultiResponseDto<>(responseList, searchList), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity searchTitleOrContent(@RequestParam("keyword") String keyword,
                                               @RequestParam(required = false) String sort,
                                               @PageableDefault(sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {

        if (sort != null) {
            Sort sortOrder = Sort.by(sort.split("_")[0]).ascending();
            if (sort.split("_")[1].equalsIgnoreCase("desc")) {
                sortOrder = sortOrder.descending();
            }
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortOrder);
        }

        Page<Post> searchList = postService.searchPostTitleOrContent(pageable, keyword);
        List<PostDto.Response> responseList = searchList.stream()
                .map(mapper::postToPostResponseDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                new MultiResponseDto<>(responseList, searchList), HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity deletePost(@PathVariable("post-id") @Positive long postId,
                                     @RequestParam("memberId") @Positive long memberId) {
        postService.deletePost(memberId, postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
