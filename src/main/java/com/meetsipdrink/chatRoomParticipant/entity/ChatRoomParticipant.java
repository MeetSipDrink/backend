package com.meetsipdrink.chatRoomParticipant.entity;


import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "chat_room_participant")
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member participant;

    public ChatRoomParticipant(ChatRoom chatRoom, Member participant) {
        this.chatRoom = chatRoom;
        this.participant = participant;
    }
}