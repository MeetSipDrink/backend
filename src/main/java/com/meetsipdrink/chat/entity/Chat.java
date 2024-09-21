package com.meetsipdrink.chat.entity;

import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.chatRoomParticipant.entity.ChatRoomParticipant;
import com.meetsipdrink.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "chat")
@Getter
@Setter
@NoArgsConstructor
public class Chat extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @Column(nullable = false)
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private ChatRoomParticipant participant;


    @Enumerated(EnumType.STRING)
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }



}
