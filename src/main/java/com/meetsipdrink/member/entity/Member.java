package com.meetsipdrink.member.entity;

import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostComment;
import com.meetsipdrink.board.entity.PostLike;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity (name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(name = "member_email" , nullable = false, unique = true , updatable = false )
    private String email;

    @Column(name = "member_password", nullable = false)
    private String password;

    @Column (name = "member_profile_image" )
    private String profileImage;

    @Column(name = "member_nickname", nullable = false , unique = true )
    private String  nickname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "member_gender", nullable = false)
    private memberGender gender;

    @Column(name = "member_name" , nullable = false)
    private String name;

    @Column (name = "member_age" , nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column (name = "member_status" , nullable = false)
    private memberStatus status = memberStatus.isActive;

    @Column (name = "alcohol_type1" , nullable = false)
    private String alcoholType1;

    @Column (name = "alcohol_type2" , nullable = true)
    private String alcoholType2;

    @Column (name = "alcohol_type3" , nullable = true)
    private String alcoholType3;

    @Column (name = "member_chat_room_status" )
    private Boolean chatRoomStatus = false;

    @Column (name = "member_ban_count")
    private Integer banCount = 0 ;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE)
    private List<Post> posts = new ArrayList<>();

    public void setPosts(Post post) {
        posts.add(post);
        if(post.getMember() != this) {
            post.setMember(this);
        }
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE)
    private List<PostLike> postLikes = new ArrayList<>();

    public void setPostLikes(PostLike postLike) {
        postLikes.add(postLike);
        if(postLike.getMember() != this) {
            postLike.setMember(this);
        }
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE)
    private List<PostComment> postComments = new ArrayList<>();

    public void setPostComments(PostComment postComment) {
        postComments.add(postComment);
        if(postComment.getMember() != this) {
            postComment.setMember(this);
        }
    }

    public enum memberGender{
        M("남성"),
        F("여성");


        @Getter
        private String gender;
        memberGender(String gender) {
            this.gender = gender;
        }
    }

    public enum memberStatus{
        isActive("활동중 회원"),
        isInactive("탈퇴한 회원");

        @Getter
        private String status;
        memberStatus(String status) {
            this.status = status;
        }


    }

}
