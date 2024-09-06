package com.meetsipdrink.friend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class FriendDto {



    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response {
        private String friendNickName;
        private String friendGender;
        private String getFriendProfileImage;
        private String friendAlcoholType1;
        private String friendAlcoholType2;
        private String FriendAlcoholType3;
    }
}
