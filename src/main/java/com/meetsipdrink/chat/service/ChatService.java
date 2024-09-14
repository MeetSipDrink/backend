package com.meetsipdrink.chat.service;

import com.meetsipdrink.chatRoom.entity.ChatRoom;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatService {
    private Map<String, ChatRoom> chatRooms = new HashMap<>();

//    public ChatRoom createChatRoom(String name) {
//        String chatRoomId = UUID.randomUUID().toString();
//        ChatRoom chatRoom = new ChatRoom(chatRoomId, name);
//        chatRooms.put(chatRoomId, chatRoom);
//        return chatRoom;
//    }

    public ChatRoom findChatRoomById(String chatRoomId) {
        return chatRooms.get(chatRoomId);
    }

    public List<ChatRoom> findAllChatRooms() {
        return new ArrayList<>(chatRooms.values());
    }

}
