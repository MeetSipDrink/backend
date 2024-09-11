package com.meetsipdrink.friend.dto;

import com.meetsipdrink.friend.entitiy.Friend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class FriendDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ResponseDto {
        private String friendNickName;
        private String friendGender;
        private String FriendProfileImage;
        private String friendAlcoholType1;
        private String friendAlcoholType2;
        private String FriendAlcoholType3;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class RequestDto {
        private long requesterId;
        private long recipientId;
    }

    @Getter
    @Setter
    public static class AcceptFriendDto {
        private long friendId;
        private long recipientId;
    }

    @Getter
    @AllArgsConstructor
    public enum Status {
        PENDING("대기중"),
        ACCEPTED("요청 수락"),
        REJECTED("요청 거절");

        private String status;
    }

}
