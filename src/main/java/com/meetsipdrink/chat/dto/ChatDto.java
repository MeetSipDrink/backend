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

    }



}
