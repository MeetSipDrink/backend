package com.meetsipdrink.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class NoticeDto {

    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private List<String> imageUrls;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private String title;

        private String content;

        private List<String> imageUrls;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private long noticeId;
        private long memberId;
        private String title;
        private String content;
        private int views;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private List<String> imageUrls;
        private String nickname;
        private String profileImage;
    }
}
