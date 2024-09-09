package com.meetsipdrink.ban.entity;


import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity (name = "ban")
@Getter
@Setter
@NoArgsConstructor
public class Ban extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long banId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "ban_member_id", nullable = false)
    private Long banMemberId;

    @ManyToOne
    @JoinColumn(name = "member_id", insertable = false, updatable = false)
    private Member blockingMember;

    @ManyToOne
    @JoinColumn(name = "ban_member_id", insertable = false, updatable = false)
    private Member bannedMember;
}

