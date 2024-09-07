package com.meetsipdrink.board.repository;

import com.meetsipdrink.board.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    Page<Post> searchByTitleOrContent(Pageable pageable, @Param("keyword") String keyword);
}
