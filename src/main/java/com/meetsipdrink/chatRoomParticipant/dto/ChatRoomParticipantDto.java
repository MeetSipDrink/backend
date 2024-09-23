package com.meetsipdrink.chatRoomParticipant.dto;

import lombok.Getter;
import lombok.Setter;

public class ChatRoomParticipantDto {

    @Getter
    @Setter
    public static class Request {
        private Long memberId;
    }

    @Getter
    @Setter
    public static class Response {
        private Long id;
        private Long chatRoomId;
        private Long memberId;
    }


}
