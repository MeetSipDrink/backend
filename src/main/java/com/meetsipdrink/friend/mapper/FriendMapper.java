package com.meetsipdrink.friend.mapper;

import com.meetsipdrink.friend.dto.FriendDto;
import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendMapper {
FriendDto.Response friendToResponse (Friend friend);
default List<FriendDto.Response> friendsToResponse (List<Member> friends){
    List<FriendDto.Response> result = new ArrayList<>();
    for (Member member : friends) {
        FriendDto.Response response = new FriendDto.Response();
        response.setFriendNickName(member.getNickname());
        response.setFriendGender(member.getGender().getGender());
        response.setGetFriendProfileImage(member.getProfileImage());
    }
    return  result;
}

}
