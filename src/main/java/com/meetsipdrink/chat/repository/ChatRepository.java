package com.meetsipdrink.chat.repository;

import com.meetsipdrink.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository <Chat,Long> {

}
