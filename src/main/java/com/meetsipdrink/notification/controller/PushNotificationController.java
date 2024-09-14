package com.meetsipdrink.notification.controller;

import com.meetsipdrink.notification.dto.PushNotificationRequest;
import com.meetsipdrink.notification.service.FCMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class PushNotificationController {

    private final FCMService fcmService;

    public PushNotificationController(FCMService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody PushNotificationRequest request) {
        fcmService.sendMessage(request);
        return ResponseEntity.ok("Notification sent successfully");
    }
}
