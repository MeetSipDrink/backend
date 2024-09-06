package com.meetsipdrink.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PostImageDto {
    @Getter
    @AllArgsConstructor
    public static class Post {

        @NotBlank
        private String imageUrl;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private long postId;
        private long postImageId;
        private String imageUrl;
    }
}
