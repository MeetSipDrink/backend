package com.meetsipdrink.board.mapper;

import com.meetsipdrink.board.dto.PostDto;
import com.meetsipdrink.board.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PostCommentMapper.class})
public interface PostMapper {

    @Mapping(source = "memberId", target = "member.memberId")
    Post postPostDtoToPost(PostDto.Post requestBody);

    @Mapping(source = "memberId", target = "member.memberId")
    Post postPatchDtoToPost(PostDto.Patch requestBody);

    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(source = "member.nickname", target = "nickname")
    @Mapping(source = "member.profileImage", target = "profileImage")
    @Mapping(target = "postCommentList", qualifiedByName = "postCommentToPostCommentResponse")
    PostDto.Response postToPostResponseDto(Post post);

    List<PostDto.Response> postsToPostResponseDtos(List<Post> posts);
}
