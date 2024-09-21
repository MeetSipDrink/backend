package com.meetsipdrink.chat.mapper;

import com.meetsipdrink.chat.dto.ChatDto;
import com.meetsipdrink.chat.entity.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(source = "chatRoomId", target = "chatRoom.chatRoomId")
    Chat postDtoToChat(ChatDto.Post postDto);

    List<ChatDto.Response> chatsToResponseDto(List<Chat> chats);

    @Mapping(source = "chatRoom.chatRoomId", target = "chatRoomId")
    @Mapping(source = "participant.participant.nickname", target = "senderName")  // 올바른 경로로 수정
    ChatDto.Response chatToResponseDto(Chat chat);

}
