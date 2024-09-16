package com.meetsipdrink.video.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.meetsipdrink.video.Entity.ChatUser;
import com.meetsipdrink.video.dto.ChatUserDto;

@Mapper(componentModel = "spring")
public interface ChatUserMapper {
    ChatUserMapper INSTANCE = Mappers.getMapper(ChatUserMapper.class);

    ChatUserDto toDto(ChatUser e);
    ChatUser toEntity(ChatUserDto d);
}
