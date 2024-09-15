package com.meetsipdrink.chat.repository;

import com.meetsipdrink.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository <ChatRoom ,Long> {

}
