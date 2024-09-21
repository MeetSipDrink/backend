package com.meetsipdrink.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PostCommentDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @Setter
        private String email;

        @NotBlank
        private String content;

        private Long parentCommentId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch {
        private String email;

        @NotBlank
        private String content;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private long memberId;
        private long postId;
        private long postCommentId;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private Long parentCommentId;
        private String content;
        private String nickname;
        private String profileImage;
    }
}
