package com.meetsipdrink.board.repository;

import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostLike;
import com.meetsipdrink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findAllByMemberAndPost(Member member, Post post);
    Optional<PostLike> findPostLikeByMemberIdAndPostPostId(Long memberId, Long postId);
}
