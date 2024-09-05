package com.meetsipdrink.board.mapper;

import com.meetsipdrink.board.dto.PostCommentDto;
import com.meetsipdrink.board.entity.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostCommentMapper {

    @Mapping(source = "memberId", target = "member.memberId")
    @Mapping(source = "parentCommentId", target = "parentComment.postCommentId", ignore = true)
    PostComment postCommentPostDtoToPostComment(PostCommentDto.Post requestBody);

    @Mapping(source = "memberId", target = "member.memberId")
    @Mapping(source = "parentCommentId", target = "parentComment.postCommentId", ignore = true)
    PostComment postCommentPatchDtoToPostComment(PostCommentDto.Patch requestBody);

    @Mapping(source = "member.memberId", target = "memberId")
    @Mapping(source = "post.postId", target = "postId")
    @Mapping(source = "member.nickname", target = "nickname")
    @Mapping(source = "member.profileImage", target = "profileImage")
    @Mapping(source = "parentComment.postCommentId", target = "parentCommentId")
    PostCommentDto.Response postCommentToPostCommentResponseDto(PostComment postComment);

    @Named("postCommentToPostCommentResponse")
    List<PostCommentDto.Response> postCommentsToPostCommentResponseDtos(List<PostComment> postComments);
}
