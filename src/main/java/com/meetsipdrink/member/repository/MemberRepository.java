package com.meetsipdrink.member.repository;

import com.meetsipdrink.member.entitiy.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member , Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);

}
