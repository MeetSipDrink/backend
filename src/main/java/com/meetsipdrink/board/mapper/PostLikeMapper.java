package com.meetsipdrink.board.mapper;

import com.meetsipdrink.board.dto.PostLikeDto;
import com.meetsipdrink.board.entity.PostLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostLikeMapper {

    @Mapping(source = "memberId", target = "member.memberId")
    PostLike postLikePostDtoToPostLike(PostLikeDto.Post requestBody);
}
