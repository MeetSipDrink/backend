package com.meetsipdrink.chatRoom.mapper;


import com.meetsipdrink.chatRoom.dto.ChatRoomDto;
import com.meetsipdrink.chatRoom.entity.ChatRoom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface   ChatRoomMapper {
    ChatRoomDto chatRoomToChatRoomDto (ChatRoom chatRoom);
    ChatRoom chatRoomDtoToChatRoom (ChatRoomDto chatRoomDto);
}
