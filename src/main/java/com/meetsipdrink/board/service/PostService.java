package com.meetsipdrink.board.service;

import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.repository.PostRepository;
import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;

    public PostService(PostRepository postRepository,
                       MemberService memberService) {
        this.postRepository = postRepository;
        this.memberService = memberService;
    }

    public Post createPost(Post post) throws IllegalArgumentException {
        Member member = memberService.findMemberByEmail(post.getMember().getEmail());
        post.setMember(member);

        return postRepository.save(post);
    }

    public Post updatePost(long postId, Post post) {
        Post findPost = findVerifiedPost(postId);

        if (!Objects.equals(findPost.getMember().getEmail(), post.getMember().getEmail())) {
            throw new BusinessLogicException(ExceptionCode.BOARD_UNAUTHORIZED_ACTION);
        }

        Optional.ofNullable(post.getTitle())
                .ifPresent(title -> findPost.setTitle(title));
        Optional.ofNullable(post.getContent())
                .ifPresent(content -> findPost.setContent(content));
        Optional.ofNullable(post.getImageUrl1())
                .ifPresent(imageUrl1 -> findPost.setImageUrl1(imageUrl1));
        Optional.ofNullable(post.getImageUrl1())
                .ifPresent(imageUrl2 -> findPost.setImageUrl1(imageUrl2));
        Optional.ofNullable(post.getImageUrl1())
                .ifPresent(imageUrl3 -> findPost.setImageUrl1(imageUrl3));
        Optional.ofNullable(post.getImageUrl1())
                .ifPresent(imageUrl4 -> findPost.setImageUrl1(imageUrl4));
        Optional.ofNullable(post.getImageUrl1())
                .ifPresent(imageUrl5 -> findPost.setImageUrl1(imageUrl5));
        Optional.ofNullable(post.getImageUrl1())
                .ifPresent(imageUrl6 -> findPost.setImageUrl1(imageUrl6));

        return postRepository.save(findPost);
    }

    public synchronized Post findPost(long postId) {
        Post findPost = findVerifiedPost(postId);
        findPost.setViews(findPost.getViews() + 1);
        return findPost;
    }

    @Transactional(readOnly = true)
    public Page<Post> findPostsSort(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        return postRepository.findAll(pageable);
    }

    public Page<Post> searchPostTitle(Pageable pageable, String keyword) {
        return postRepository.searchByTitle(pageable, keyword);
    }

    public Page<Post> searchPostContent(Pageable pageable, String keyword) {
        return postRepository.searchByContent(pageable, keyword);
    }

    public Page<Post> searchPostTitleOrContent(Pageable pageable, String keyword) {
        return postRepository.searchByTitleOrContent(pageable, keyword);
    }

    public void deletePost(String email, long postId) {
        Post findPost = findVerifiedPost(postId);

        if(Objects.equals(findPost.getMember().getEmail(), email)) {
            postRepository.delete(findPost);
        }else {
            throw new BusinessLogicException(ExceptionCode.BOARD_UNAUTHORIZED_ACTION);
        }
    }

    public Post findVerifiedPost(long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        return optionalPost.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
    }

    public Post findPostById(long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found for id : " + postId));
    }
}
