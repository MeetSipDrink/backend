package com.meetsipdrink.notice.mapper;

import com.meetsipdrink.notice.dto.NoticeImageDto;
import com.meetsipdrink.notice.entity.NoticeImage;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoticeImageMapper {

    NoticeImage noticeImagePostDtoToNoticeImage(NoticeImageDto.Post requestBody);

    @Named("noticeImageToNoticeImageResponse")
    List<NoticeImageDto.Response> noticeImagesToNoticeImageResponseDtos(List<NoticeImage> noticeImages);
}
