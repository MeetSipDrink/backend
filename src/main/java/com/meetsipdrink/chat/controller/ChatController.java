package com.meetsipdrink.chat.controller;

import com.meetsipdrink.chat.entity.Chat;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Chat chat) {
        log.info("Sending message: {}", chat);
        messagingTemplate.convertAndSend("/topic/chatrooms/" + chat.getChatRoomId(), chat); // 특정 채팅방으로 메시지 전송
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload Chat chat, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chat.getSender());
        log.info("User joined: {}", chat.getSender());
        messagingTemplate.convertAndSend("/topic/chatrooms/" + chat.getChatRoomId(), chat); // 특정 채팅방으로 사용자 추가 메시지 전송
    }
}