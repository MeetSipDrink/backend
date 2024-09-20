package com.meetsipdrink.chat.controller;

import com.meetsipdrink.chat.entity.Chat;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Chat chat) {
        log.info("Sending message: {}", chat);
        messagingTemplate.convertAndSend("/topic/chatrooms/" + chat.getChatRoomId(), chat);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload Chat chat, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chat.getSender());
        log.info("User joined: {}", chat.getSender());
        messagingTemplate.convertAndSend("/topic/chatrooms/" + chat.getChatRoomId(), chat);
    }
}