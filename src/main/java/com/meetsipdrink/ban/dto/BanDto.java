package com.meetsipdrink.ban.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BanDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post {
        private long blockerId;
        private long blockedMemberId;
    }

//    @Getter
//    @Setter
//    @NoArgsConstructor
//    public static class Request {
//        private long blockerId;
//        private long blockedMemberId;
//    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private long banId;
        private long memberId;
        private long banMemberId;
        private String memberNickname;
        private String bannedNickname;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class banListResponse {
        private long banId;
        private long memberId;
        private long banMemberId;
        private String memberNickname;
        private String bannedNickname;
    }


}

