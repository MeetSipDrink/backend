package com.meetsipdrink.board.service;

import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostImage;
import com.meetsipdrink.board.repository.PostImageRepository;
import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class PostImageService {
    private final PostService postService;
    private final PostImageRepository postImageRepository;

    public PostImageService(PostService postService,
                            PostImageRepository postImageRepository) {
        this.postService = postService;
        this.postImageRepository = postImageRepository;
    }

    public PostImage createPostImage(PostImage postImage) throws IllegalArgumentException {
        Post post = postService.findVerifiedPost(postImage.getPost().getPostId());

        postImage.setPost(post);

        return postImageRepository.save(postImage);
    }

    public void deletePostImage(long postImageId) {
        PostImage findPostImage = findVerifiedPostImage(postImageId);
        postImageRepository.delete(findPostImage);
    }

    public PostImage findVerifiedPostImage(long postImageId) {
        Optional<PostImage> optionalPostImage = postImageRepository.findById(postImageId);
        PostImage findPostImage = optionalPostImage.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.BOARD_IMAGE_NOT_FOUND));
        return findPostImage;
    }
}
