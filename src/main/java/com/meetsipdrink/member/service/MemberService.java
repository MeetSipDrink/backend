package com.meetsipdrink.member.service;

import com.meetsipdrink.auth.utils.CustomAuthorityUtils;
import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    public Member ceateMember(Member member) {
        verifyExistMember(member.getEmail());
        verifyNickName(member.getNickname());

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        Member verifiedMember = memberRepository.save(member);
        return verifiedMember;

    }

    public Member updateMember(Member member) {
        Member findMember = isvalidMember(member.getMemberId());
        if (!findMember.getNickname().equals(member.getNickname())) {
            verifyNickName(member.getNickname());
            Optional.ofNullable(member.getNickname())
                    .ifPresent(nickname -> findMember.setNickname(nickname));
        }
        Optional.ofNullable(member.getProfileImage())
                .ifPresent(profileImage -> findMember.setProfileImage(profileImage));
        Optional.ofNullable(member.getPassword())
                .ifPresent(password -> findMember.setPassword(password));
        Optional.ofNullable(member.getAlcoholType1())
                .ifPresent(alcoholType1 -> findMember.setAlcoholType1(alcoholType1));
        Optional.ofNullable(member.getAlcoholType2())
                .ifPresent(alcoholType2 -> findMember.setAlcoholType1(alcoholType2));
        Optional.ofNullable(member.getAlcoholType3())
                .ifPresent(alcoholType3 -> findMember.setAlcoholType1(alcoholType3));
        return memberRepository.save(findMember);
    }


    public Member findMember(long memberId) {
        return isvalidMember(memberId);
    }


    public Page<Member> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }


    public void deleteMember (long memberId) {
        Member member = findMember(memberId);
        member.setStatus(Member.memberStatus.isInactive);
        member.setNickname("탈퇴한회원" + memberId);
        memberRepository.save(member);
    }




    private void verifyExistMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }


    public void verifyNickName(String nickName) {
        Optional<Member> member = memberRepository.findByNickname(nickName);
        if(member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.NICKNAME_EXISTS);
        }
    }
    private Member findVerifiedMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private Member isvalidMember (long memberId)  {
         return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

}
