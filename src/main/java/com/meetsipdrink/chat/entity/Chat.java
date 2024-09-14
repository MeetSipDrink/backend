package com.meetsipdrink.chat.entity;

import com.meetsipdrink.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Chat extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId; // 기본 키로 사용할 필드
    private String sender;
    private String content;
    // private MessageType type; // 필요에 따라 주석을 해제

    // @Getter
    // public enum MessageType {
    //     CHAT, JOIN, LEAVE
    // }
}
