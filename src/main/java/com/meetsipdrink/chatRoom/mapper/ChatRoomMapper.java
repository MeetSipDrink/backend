package com.meetsipdrink.chatRoom.mapper;


import com.meetsipdrink.chatRoom.dto.ChatRoomDto;
import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.member.dto.MemberDto;
import com.meetsipdrink.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface   ChatRoomMapper {
    ChatRoomDto chatRoomToChatRoomDto (ChatRoom chatRoom);
    ChatRoom chatRoomDtoToChatRoom (ChatRoomDto chatRoomDto);



    }





