package com.meetsipdrink.board.repository;

import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPost(Post post);
}
