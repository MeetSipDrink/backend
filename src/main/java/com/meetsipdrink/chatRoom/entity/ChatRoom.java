package com.meetsipdrink.chatRoom.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.meetsipdrink.audit.Auditable;
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
    private Long ChatRoomId;

    @Column(name = "chat_room_name", nullable = false)
    private String ChatRoomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Long memberId;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<Member> participants = new HashSet<>();

    public void addMember(Member member) {
        participants.add(member);
        member.setChatRoom(this);
    }

    public void removeMember(Member member) {
        participants.remove(member);
        member.removeChatRoom();
    }

}


