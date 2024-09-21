package com.meetsipdrink.chat.controller;

import com.meetsipdrink.chat.dto.ChatDto;
import com.meetsipdrink.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    // 클라이언트가 "/app/chat.sendMessage"로 메시지를 전송하면 이 메서드가 호출됩니다.
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatDto.Post postDto) {
        // 메시지 저장
        ChatDto.Response response = chatService.sendMessage(postDto);

        // 해당 채팅방을 구독 중인 사용자에게 메시지 전송
        messagingTemplate.convertAndSend("/topic/chatrooms/" + postDto.getChatRoomId(), response);

    }
}
