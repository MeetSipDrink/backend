package com.meetsipdrink.member.repository;

import com.meetsipdrink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member , Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    @Query(value = "SELECT member_fcm_token FROM member WHERE member_fcm_token IS NOT NULL AND member_id != 1", nativeQuery = true)
    List<String> findAllFcmTokens();
}
