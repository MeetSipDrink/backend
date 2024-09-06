package com.meetsipdrink.board.repository;

import com.meetsipdrink.board.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
