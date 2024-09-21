package com.meetsipdrink.chat.dto;

import lombok.Getter;
import lombok.Setter;

public class ChatDto {
        @Getter
        @Setter
        public static class Post {
            private Long chatRoomId;
            private String message;
            private String senderName;
        }

        @Getter
        @Setter
        public static class Response {
            private Long chatId;
            private Long chatRoomId;
            private String message;
            private String senderName;
        }



    @Getter
    @Setter
    public static class Patch {
        private Long chatId;
        private String message;
    }

    @Getter
    @Setter
    public static class Delete {
        private Long chatId;
    }

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}



