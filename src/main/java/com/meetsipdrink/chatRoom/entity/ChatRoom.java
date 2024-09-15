package com.meetsipdrink.chatRoom.entity;
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Long memberId;

    @ElementCollection
    private Set<String> participants = new HashSet<>();

    public boolean addParticipant(String username) {
        if (participants.size() >= 6) {
            return false;
        }
        return participants.add(username);
    }

    public void removeParticipant(String username) {
        participants.remove(username);
    }


//    public ChatRoom(String name, Member member) {
//        this.name = name;
//        this.member = member;
//    }
//

}


