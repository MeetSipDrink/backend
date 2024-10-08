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

import static javax.persistence.FetchType.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PostComment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postCommentId;

    @Column(name = "post_comment_content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if(!member.getPostComments().contains(this)) {
            member.setPostComments(this);
        }
    }

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    public void setPost(Post post) {
        this.post = post;
        if(!post.getPostCommentList().contains(this)) {
            post.setPostComments(this);
        }
    }




    public PostComment(Long postCommentId) {
        this.postCommentId = postCommentId;
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id")
    private PostComment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostComment> replies = new ArrayList<>();

    public void addReply(PostComment reply) {
        replies.add(reply);
        reply.setParentComment(this);
    }
}
