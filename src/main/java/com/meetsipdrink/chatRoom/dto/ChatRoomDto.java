package com.meetsipdrink.chatRoom.dto;

import com.meetsipdrink.ban.dto.BanDto;
import com.meetsipdrink.member.dto.MemberDto;
import com.meetsipdrink.member.entity.Member;
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

    @Getter
    @Setter
    public static class Response {
        private Long chatRoomId;
        private String roomName;
        private MemberDto host;
        private Set<MemberDto> participant;
    }



    @Getter
    @Setter
    public static class ChatRoomResponse {
        private Long chatRoomId;
        private Long memberId;
        private String chatRoomName;
        private Set<Member> participant;

    }


}
