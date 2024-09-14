package com.meetsipdrink.chatRoom.service;

import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.chatRoom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        findAllChatRooms(chatRoom.getChatRoomId());
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElse(null);
    }

    public List<ChatRoom> findAllChatRooms(Long chatRoomId) {
        return chatRoomRepository.findAll();
    }

    public boolean addParticipantToChatRoom(Long chatRoomId, String username) {
        ChatRoom chatRoom = findChatRoomById(chatRoomId);
        if (chatRoom == null) {
            return false;
        }
        boolean added = chatRoom.addParticipant(username);
        chatRoomRepository.save(chatRoom);
        return added;
    }

    public void removeParticipantFromChatRoom(Long chatRoomId, String username) {
        ChatRoom chatRoom = findChatRoomById(chatRoomId);
        if (chatRoom != null) {
            chatRoom.removeParticipant(username);
            chatRoomRepository.save(chatRoom);
        }
    }
}
