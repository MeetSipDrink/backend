package com.meetsipdrink.board.entity;

import com.meetsipdrink.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postLikeId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if(!member.getPostLikes().contains(this)) {
            member.setPostLikes(this);
        }
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    public void setPost(Post post) {
        this.post = post;
        if(!post.getPostLikes().contains(this)) {
            post.setPostLikes(this);
        }
    }
}
