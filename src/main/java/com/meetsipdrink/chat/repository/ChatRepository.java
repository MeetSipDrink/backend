package com.meetsipdrink.chat.repository;

import com.meetsipdrink.chat.entity.Chat;
import com.meetsipdrink.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository <Chat,Long> {
    List<Chat> findByChatRoom(ChatRoom chatRoom);
}
