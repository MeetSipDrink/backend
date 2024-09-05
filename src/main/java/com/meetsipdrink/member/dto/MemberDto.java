package com.meetsipdrink.member.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

public class MemberDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post{

        private String email;

        private String password;

        private String profileImage;

        private String  nickname;






    }

}
