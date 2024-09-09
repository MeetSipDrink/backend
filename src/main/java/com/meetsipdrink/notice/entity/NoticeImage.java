package com.meetsipdrink.notice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class NoticeImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long noticeImageId;

    @NotNull
    @Column(name = "notice_image_URL", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "NOTICE_ID")
    private Notice notice;

    public void setNotice(Notice notice) {
        this.notice = notice;
        if(!notice.getNoticeImageList().contains(this)) {
            notice.setNoticeImages(this);
        }
    }
}
