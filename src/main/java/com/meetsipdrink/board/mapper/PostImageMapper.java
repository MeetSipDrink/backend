package com.meetsipdrink.board.mapper;

import com.meetsipdrink.board.dto.PostImageDto;
import com.meetsipdrink.board.entity.PostImage;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostImageMapper {

    PostImage postImagePostDtoToPostImage(PostImageDto.Post requestBody);

    @Named("postImageToPostImageResponse")
    List<PostImageDto.Response> postImagesToPostImageResponseDtos(List<PostImage> postImages);
}
