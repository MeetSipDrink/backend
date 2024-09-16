package com.meetsipdrink.chat.controller;

import com.meetsipdrink.chat.entity.Chat;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class ChatController {
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chatRoom/{chatRoomId}")
    public Chat sendMessage(@Payload Chat chat) {
        return chat;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/chatRoom/{chatRoomId}")
    public Chat addUser(@Payload Chat chat,
                               SimpMessageHeaderAccessor headerAccessor) {
        // 사용자 이름을 웹소켓 세션에 저장
        //SimpMessageHeaderAccessor  STOMP 메시지의 헤더에 접근하거나 헤더 정보를 수정할 수 있는 유틸리티 클래스
        headerAccessor.getSessionAttributes().put("username", chat.getSender());
        return chat;
    }

}

