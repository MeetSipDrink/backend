package com.meetsipdrink.ban.repository;

import com.meetsipdrink.ban.entity.Ban;
import com.meetsipdrink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban, Long> {
    List<Ban> findByBlockerMember_MemberId(Long memberId);
    Optional<Ban> findByBlockerMemberAndBlockedMember(Member blockerMember, Member blockedMember);

}

