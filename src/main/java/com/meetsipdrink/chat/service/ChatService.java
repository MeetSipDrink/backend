package com.meetsipdrink.chat.service;

import com.meetsipdrink.chat.dto.ChatDto;
import com.meetsipdrink.chat.entity.Chat;
import com.meetsipdrink.chat.mapper.ChatMapper;
import com.meetsipdrink.chat.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {

        private final ChatRepository chatRepository;
        private final ChatMapper chatMapper;

    public ChatService(ChatRepository chatRepository, ChatMapper chatMapper) {
        this.chatRepository = chatRepository;
        this.chatMapper = chatMapper;
    }

    @Transactional
        public ChatDto.Response sendMessage(ChatDto.Post postDto) {
            // 메시지 저장 로직
            Chat chat = chatMapper.postDtoToChat(postDto);
            chatRepository.save(chat);
            return chatMapper.chatToResponseDto(chat);
        }
    }



