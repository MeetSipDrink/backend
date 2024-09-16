package com.meetsipdrink.video.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.meetsipdrink.video.Entity.ChatUser;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    ChatUser findByEmail(String email);
}
