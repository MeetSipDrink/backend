package com.meetsipdrink.member.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.ban.entity.Ban;
import com.meetsipdrink.board.entity.Post;
import com.meetsipdrink.board.entity.PostComment;
import com.meetsipdrink.board.entity.PostLike;
import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.notice.entity.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "member")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Member extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(name = "member_email", nullable = false, unique = true, updatable = false)
    private String email;

    @Column(name = "member_password", nullable = false)
    private String password;

    @Column(name = "member_profile_image")
    private String profileImage;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "member_gender", nullable = false)
    private memberGender gender;

    @Column(name = "member_name", nullable = false)
    private String name;

    @Column(name = "member_age", nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status", nullable = false)
    private memberStatus status = memberStatus.isActive;

    @Column(name = "alcohol_type1", nullable = false)
    private String alcoholType1;

    @Column(name = "alcohol_type2")
    private String alcoholType2;

    @Column(name = "alcohol_type3")
    private String alcoholType3;

    @Column(name = "member_chat_room_status")
    private Boolean chatRoomStatus = false;

//    @Column(name = "member_chat_status")
//    private String chatRoomStatus = false;

    @Column(name = "member_ban_count")
    private Integer banCount = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    @Column(name = "member_fcm_token")
    private String fcmToken;

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> sentFriendRequests = new ArrayList<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> receivedFriendRequests = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL})
    private List<PostComment> postComments = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Notice notice;

    @OneToMany(mappedBy = "blockerMember", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ban> bans = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    @JsonBackReference  // 역참조: Member에서 ChatRoom을 직렬화하지 않음
    private ChatRoom chatRoom;


//    @Enumerated(EnumType.STRING)
//    @Column(name = "member_chat_room_status", nullable = false)
//    private memberChatRoomStatus chatRoomStatus = memberChatRoomStatus.AVAILABLE;

    // ChatRoom을 설정할 때 양방향 관계를 관리하는 메서드
    public void setChatRoom(ChatRoom chatRoom) {
        if (this.chatRoom != chatRoom) {
            if (this.chatRoom != null) {
                this.chatRoom.removeMember(this);
            }
            this.chatRoom = chatRoom;
            if (chatRoom != null && !chatRoom.getParticipant().contains(this)) {
                chatRoom.addMember(this);
            }
        }
    }


    public void setNotice(Notice notice) {
        this.notice = notice;
        if (notice.getMember() != this) {
            notice.setMember(this);
        }
    }

    public void addSentFriendRequest(Friend friend) {
        if (!sentFriendRequests.contains(friend)) {
            sentFriendRequests.add(friend);
            if (friend.getRequester() != this) {
                friend.setRequester(this);
            }
        }
    }

    public void addReceivedFriendRequest(Friend friend) {
        if (!receivedFriendRequests.contains(friend)) {
            receivedFriendRequests.add(friend);
            if (friend.getRecipient() != this) {
                friend.setRecipient(this);
            }
        }
    }

    public void setPosts(Post post) {
        posts.add(post);
        if (post.getMember() != this) {
            post.setMember(this);
        }
    }

    public void setPostLikes(PostLike postLike) {
        postLikes.add(postLike);
        if (postLike.getMember() != this) {
            postLike.setMember(this);
        }
    }

    public void setPostComments(PostComment postComment) {
        postComments.add(postComment);
        if (postComment.getMember() != this) {
            postComment.setMember(this);
        }
    }

    public void addBan(Ban ban) {
        if (!bans.contains(ban)) {
            bans.add(ban);
            ban.setBlockerMember(this);
        }
    }

    public enum memberGender {
        M("남성"),
        F("여성");

        @Getter
        private final String gender;
        memberGender(String gender) {
            this.gender = gender;
        }
    }

    public enum memberStatus {
        isActive("활동중 회원"),
        isInactive("탈퇴한 회원");
        @Getter
        private final String status;
        memberStatus(String status) {
            this.status = status;
        }
    }

//    public enum memberChatRoomStatus {
//        AVAILABLE("채팅방 입장 전"),
//        IN_CHATROOM("채팅방 입장 후");
//
//        @Getter
//        private final String chatStatus;
//
//        memberChatRoomStatus(String chatStatus) {
//            this.chatStatus = chatStatus;
//        }
//    }

}
