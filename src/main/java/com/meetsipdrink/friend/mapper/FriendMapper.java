package com.meetsipdrink.friend.mapper;

import com.meetsipdrink.friend.dto.FriendDto;
import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendMapper {

    FriendDto.ResponseDto friendToResponse(Friend friend);

    default List<FriendDto.ResponseDto> friendsToResponse(List<Member> friends) {
        List<FriendDto.ResponseDto> result = new ArrayList<>();
        for (Member member : friends) {
            FriendDto.ResponseDto response = new FriendDto.ResponseDto();
            response.setFriendId(member.getMemberId());
            response.setFriendNickName(member.getNickname());
            response.setFriendGender(member.getGender().getGender());
            response.setFriendProfileImage(member.getProfileImage());
            response.setFriendAlcoholType1(member.getAlcoholType1());
            response.setFriendAlcoholType2(member.getAlcoholType2());
            response.setFriendAlcoholType3(member.getAlcoholType3());

            result.add(response);
        }
        return result;
    }
}
