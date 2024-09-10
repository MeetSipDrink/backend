package com.meetsipdrink.ban.repository;

import com.meetsipdrink.ban.entity.Ban;
import com.meetsipdrink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BanRepository extends JpaRepository<Ban, Long> {
    List<Ban> findByBlockedMember_MemberId(Long memberId);
}

