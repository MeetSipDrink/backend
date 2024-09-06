package com.meetsipdrink.board.dto;

import com.meetsipdrink.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PostCommentDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotNull
        private long memberId;

        @NotBlank
        private String content;

        private Long parentCommentId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch {
        @NotNull
        private long postCommentId;

        @NotNull
        private long memberId;

        @NotBlank
        private String content;

        private Long parentCommentId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response extends Auditable {
        private long memberId;
        private long postId;
        private long postCommentId;
        private Long parentCommentId;
        private String content;
        private String nickname;
        private String profileImage;
    }
}
