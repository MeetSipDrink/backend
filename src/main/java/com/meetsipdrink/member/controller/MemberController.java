package com.meetsipdrink.member.controller;


import com.meetsipdrink.member.dto.MemberDto;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.mapper.MemberMapper;
import com.meetsipdrink.member.service.MemberService;
import com.meetsipdrink.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Validated
@RequestMapping
@RequiredArgsConstructor
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberService service;
    private final MemberMapper memberMapper;


    @PostMapping("/members")
    public ResponseEntity postMember (@Valid @RequestBody MemberDto.Post memberDto) {
        Member member = memberMapper.memberPostToMember(memberDto);
        service.ceateMember(member);
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, member.getMemberId());
        return ResponseEntity.created(location).build();
    }


}
