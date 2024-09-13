package com.meetsipdrink.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.meetsipdrink.notification.dto.PushNotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    public void sendMessage(PushNotificationRequest request) {
        try {
            // FCM 메시지 빌드
            Message message = Message.builder()
                    .setToken(request.getToken())  // 클라이언트의 FCM 토큰
                    .putData("title", request.getTitle())  // 제목
                    .putData("message", request.getMessage())  // 메시지 내용
                    .build();

            // 메시지 전송
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

