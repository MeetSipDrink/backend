package com.meetsipdrink.chatRoom.repository;


import com.meetsipdrink.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom , Long> {
    Optional<ChatRoom> findByName(String name);
}
