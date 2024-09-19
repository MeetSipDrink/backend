package com.meetsipdrink.auth.dto;

import lombok.Getter;

@Getter
public class LoginDto {
    private String username;
    private String password;
    private String fcmtoken;
}
