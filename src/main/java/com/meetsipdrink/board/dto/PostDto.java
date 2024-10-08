package com.meetsipdrink.board.dto;


import com.meetsipdrink.board.entity.PostLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class PostDto {

    @Getter
    @AllArgsConstructor
    public static class Post {
        @Setter
        private String email;

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private String imageUrl1;
        private String imageUrl2;
        private String imageUrl3;
        private String imageUrl4;
        private String imageUrl5;
        private String imageUrl6;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        @Setter
        private String email;

        private String title;

        private String content;

        private String imageUrl1;
        private String imageUrl2;
        private String imageUrl3;
        private String imageUrl4;
        private String imageUrl5;
        private String imageUrl6;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private long postId;
        private long memberId;
        private String title;
        private String content;
        private int views;
        private int likeCount;
        private int commentCount;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private String imageUrl1;
        private String imageUrl2;
        private String imageUrl3;
        private String imageUrl4;
        private String imageUrl5;
        private String imageUrl6;
        private List<PostCommentDto.Response> postCommentList;
        private String nickname;
        private String profileImage;
        private PostLike postLike;
    }
}
