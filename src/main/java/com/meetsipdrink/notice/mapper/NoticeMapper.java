package com.meetsipdrink.notice.mapper;

import com.meetsipdrink.notice.dto.NoticeDto;
import com.meetsipdrink.notice.entity.Notice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoticeMapper {

    Notice noticePostDtoToNotice(NoticeDto.Post requestBody);

    Notice noticePatchDtoToNotice(NoticeDto.Patch requestBody);

    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(source = "member.nickname", target = "nickname")
    @Mapping(source = "member.profileImage", target = "profileImage")
    NoticeDto.Response noticeToNoticeResponseDto(Notice notice);

    List<NoticeDto.Response> noticesToNoticeResponseDtos(List<Notice> notices);
}
