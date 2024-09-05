package com.meetsipdrink.board.dto;


import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.board.entity.PostLike;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class PostDto {

    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotNull
        private long memberId;

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private List<String> imageUrls;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        @NotNull
        private long memberId;

        @NotNull
        private long postId;

        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private List<String> imageUrls;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response extends Auditable {
        private long postId;
        private long memberId;
        private String title;
        private String content;
        private int views;
        private int likeCount;
        private int commentCount;
        private List<String> imageUrls;
        private List<PostCommentDto.Response> postCommentList;
        private String nickname;
        private String profileImage;
        private PostLike postLike;
    }
}
