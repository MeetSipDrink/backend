package com.meetsipdrink.member.dto;


import com.meetsipdrink.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.text.Style;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Post{

        @NotBlank
        private String email;

        @NotNull(message = "비밀번호는 필수 항목입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자에서 20자 사이여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~]{8,15}$",
                message = "비밀번호는 8자이상 15자 이하의 알파벳, 숫자, 특수문자만 포함할 수 있습니다.")
        private String password;

        @NotBlank
        private String profileImage;

        @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,8}$",
                message = "특수문자 제외 2자이상 8자 이하로 입력해주세요.")
        private String  nickname;

        @NotBlank
        private Member.memberGender gender;

        @NotBlank(message = "이름은 공백이 아니어야 합니다.")
        private String name;

        @NotNull(message = "나이는 필수 항목입니다.")
        private Integer age;

        @NotNull(message =  "선호하는 주종 1가지는 넣어주세요")
        private String alcoholType1;

        @NotBlank
        private String alcoholType2;

        @NotBlank
        private String alcoholType3;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Patch {

//        //핸드폰 번호 따로 받아야 하는 지
//        @NotNull(message = "비밀번호는 필수 항목입니다.")
//        @Size(min = 8, max = 20, message = "비밀번호는 8자에서 20자 사이여야 합니다.")
//        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?`~]+$",
//                message = "비밀번호는 알파벳, 숫자, 특수문자만 포함할 수 있습니다.")
//        private String password;

        @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,8}$",
                message = "특수문자 제외 2자이상 8자 이하로 입력해주세요.")
        private String  nickname;

        @NotBlank
        private String profileImage;

        @NotBlank(message = "술 타입인 들어갈 수 있습니다")
        private String alcoholType1;

        @NotBlank
        private String alcoholType2;

        @NotBlank
        private String alcoholType3;


    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Response{
        private String email;

        private String password;

        private String profileImage;

        private String  nickname;

        private String name;

        private Member.memberStatus status = Member.memberStatus.isActive;

        private Member.memberGender gender;

        private Integer age;

        private String alcoholType1;

        private String alcoholType2;

        private String alcoholType3;

        private Boolean chatRoomStatus = false;

        private Integer banCount = 0;



    }

}
