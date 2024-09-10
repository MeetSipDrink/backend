package com.meetsipdrink.ban.entity;


import com.meetsipdrink.audit.Auditable;
import com.meetsipdrink.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity (name = "ban")
@Getter
@Setter
@NoArgsConstructor
public class Ban extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long banId;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "blocker_id")
    private Member blockerMember;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "blocked_member_id")
    private Member blockedMember;

    public void setBlockerMember(Member member) {
        this.blockerMember = member;
        if (!member.getBans().contains(this)) {
            member.addBan(this);
        }
    }


}
