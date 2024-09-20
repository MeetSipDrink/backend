package com.meetsipdrink.chatRoom.dto;

import com.meetsipdrink.ban.dto.BanDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Set;


public class ChatRoomDto {

    @Getter
    @Setter
    public static class Post{
        private String roomName;
        private Set<String> participant;

    }

    @Getter
    @Setter
    public static class Request {
        private String roomName;
        private Long memberId;
    }

}
