package com.meetsipdrink.board.entity;

import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.member.entity.Member;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Post extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @NotNull
    @Column(name = "post_title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "post_content", nullable = false)
    private String content;

    @Column(name = "post_views", nullable = false)
    private int views = 0;

    @Column(name = "post_like_count", nullable = false)
    private int likeCount = 0;

    @Column(name = "post_comment_count", nullable = false)
    private int commentCount = 0;


    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;
    private String imageUrl4;
    private String imageUrl5;
    private String imageUrl6;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if (!member.getPosts().contains(this)) {
            member.setPosts(this);
        }
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostLike> postLikes = new ArrayList<>();

    public void setPostLikes(PostLike postLike) {
        postLikes.add(postLike);
        if (postLike.getPost() != this) {
            postLike.setPost(this);
        }
    }


    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostComment> postCommentList = new ArrayList<>();

    public void setPostComments(PostComment postComment) {
        postCommentList.add(postComment);
        if(postComment.getPost() != this) {
            postComment.setPost(this);
        }
    }
}
