package com.meetsipdrink.chatRoom.entity;
import com.meetsipdrink.audit.Auditable;
import lombok.AllArgsConstructor;
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
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ChatRoomId;

    @Column(name = "chat_room_name")
    private String ChatRoomName;

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
}


