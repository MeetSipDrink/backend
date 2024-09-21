package com.meetsipdrink.notice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Notice extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long noticeId;

    @Column(name = "notice_title", nullable = false)
    private String title;

    @Column(name = "notice_content", nullable = false)
    private String content;

    @Column(name = "notice_views")
    private int views = 0;

    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    @JsonBackReference
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if(member.getNotice() != this) {
            member.setNotice(this);
        }
    }

}
