package com.meetsipdrink.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.meetsipdrink.notification.dto.PushNotificationRequest;
import com.meetsipdrink.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FCMService {

    private final MemberRepository memberRepository;

    // 생성자에 MemberRepository 주입
    public FCMService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 특정 사용자에게 메시지 전송
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

    // 모든 사용자에게 공지사항 알림 전송
    public void sendNoticeNotificationToAllUsers(String title, String content) {
        try {
            // 모든 사용자에 대한 FCM 토큰을 조회
            List<String> fcmTokens = memberRepository.findAllFcmTokens();

            for (String token : fcmTokens) {
                // FCM 메시지 빌드
                Message message = Message.builder()
                        .setToken(token)  // 각 사용자 FCM 토큰
                        .putData("title", "공지사항: " + title)  // 공지사항 제목
                        .putData("message", content)  // 공지사항 내용
                        .build();

                // 메시지 전송
                String response = FirebaseMessaging.getInstance().send(message);
                System.out.println("Successfully sent notification to token: " + token + ", response: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCommentNotification(String fcmToken, String title, String message) {
        try {
            Message fcmMessage = Message.builder()
                    .setToken(fcmToken)
                    .putData("title", "닉네임: " + title)
                    .putData("message", message)
                    .build();

            String response = FirebaseMessaging.getInstance().send(fcmMessage);
            System.out.println("Successfully sent notification: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
