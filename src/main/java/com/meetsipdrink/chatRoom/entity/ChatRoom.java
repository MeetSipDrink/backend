package com.meetsipdrink.chatRoom.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.chatRoomParticipant.entity.ChatRoomParticipant;
import com.meetsipdrink.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "chat_room")
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom  extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Column(name = "chat_room_name", nullable = false)
    private String chatRoomName;



    // 방장 변경 메서드
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")  // 방장 정보를 저장할 외래키
    private Member host;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Member> participant = new HashSet<>();

    public ChatRoom(String chatRoomName, Member host ) {
        this.chatRoomName = chatRoomName;
        this.host = host;
    }

    public void addMember(Member member) {
        participant.add(member);
        member.setChatRoom(this);
    }

    public void removeMember(Member member) {
        participant.remove(member);
        member.removeChatRoom();
    }

}


