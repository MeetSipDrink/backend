package com.meetsipdrink.member.mapper;

import com.meetsipdrink.member.dto.MemberDto;
import com.meetsipdrink.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostToMember(MemberDto.Post post);
    Member memberPatchToMember(MemberDto.Patch patch);
    MemberDto.Response memberToResponseDto(Member member);
    List<MemberDto.Response> membersToResponseDto(List<Member> members);
}
