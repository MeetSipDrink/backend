package com.meetsipdrink.chatRoom.repository;


import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom , Long> {
    Optional<ChatRoom> findByHost(Member host);
    
}
