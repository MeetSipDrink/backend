package com.meetsipdrink.board.service;

import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostLike;
import com.meetsipdrink.board.repository.PostLikeRepository;
import com.meetsipdrink.board.repository.PostRepository;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.service.MemberService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostLikeService {
    private final PostService postService;
    private final MemberService memberService;
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    public PostLikeService(PostService postService,
                           MemberService memberService,
                           PostLikeRepository postLikeRepository,
                           PostRepository postRepository) {
        this.postService = postService;
        this.memberService = memberService;
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
    }

    public synchronized void checkLike(long memberId, long postId) {
        Post post = postService.findVerifiedPost(postId);
        Member member = memberService.findVerifiedMember(memberId);
        Optional<PostLike> optionalPostLike = postLikeRepository.findAllByMemberAndPost(member, post);

        if(optionalPostLike.isPresent()) {
            PostLike existLike = optionalPostLike.get();
            postLikeRepository.deleteById(existLike.getPostLikeId());
            post.setLikeCount(post.getLikeCount() - 1);
        } else {
            PostLike postLike = new PostLike();
            postLike.setPost(post);
            postLike.setMember(member);
            postLikeRepository.save(postLike);
            post.setLikeCount(post.getLikeCount() + 1);
        }
        postRepository.save(post);
    }

    public boolean findLike(long memberId, long postId) {
        Optional<PostLike> optionalPostLike = postLikeRepository.findPostLikeByMemberMemberIdAndPostPostId(memberId, postId);
        return optionalPostLike.isPresent();
    }
}
