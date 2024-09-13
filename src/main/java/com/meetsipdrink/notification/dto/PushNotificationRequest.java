package com.meetsipdrink.notification.dto;

import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    public class PushNotificationRequest {
        private String title;
        private String message;
        private String token;  // 클라이언트에서 전송된 FCM 토큰
    }

