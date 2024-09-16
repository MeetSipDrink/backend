package com.meetsipdrink.chat.dto;

import lombok.Getter;
import lombok.Setter;

public class ChatDto {

    @Getter
    @Setter
    public static class PostChat{

        private String chatRoomId;
        private String sender;
        private String content;
        private MessageType type;  // 메시지 타입 (예: CHAT, JOIN, LEAVE)
        public enum MessageType {
            CHAT, JOIN, LEAVE
        }

    }



}
