package com.meetsipdrink.ban.mapper;

import com.meetsipdrink.ban.dto.BanDto;
import com.meetsipdrink.ban.entity.Ban;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BanMapper {

    @Mapping(source = "blockerMember.memberId", target = "memberId")
    @Mapping(source = "blockedMember.memberId", target = "banMemberId")
    @Mapping(source = "blockerMember.nickname", target = "memberNickname")
    @Mapping(source = "blockedMember.nickname", target = "bannedNickname")
    BanDto.Response banToBanResponse(Ban ban);
    List<BanDto.Response> banToBanResponseList(List<Ban> bans);

}
