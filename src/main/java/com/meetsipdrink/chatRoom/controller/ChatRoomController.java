package com.meetsipdrink.chatRoom.controller;

import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.chatRoom.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-rooms")
public class ChatRoomController {
    private ChatRoomService service;

//    @PostMapping
//    public ResponseEntity<ChatRoom> createChatRoom (@RequestParam String name) {
//        ChatRoom chatRoom = service.createChatRoom(name);
//        return ResponseEntity.ok(chatRoom);


    }



