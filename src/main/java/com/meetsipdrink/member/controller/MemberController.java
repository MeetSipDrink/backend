package com.meetsipdrink.member.controller;


import com.meetsipdrink.dto.SingleResponseDto;
import com.meetsipdrink.member.dto.MemberDto;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.mapper.MemberMapper;
import com.meetsipdrink.member.service.MemberService;
import com.meetsipdrink.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@Validated
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberService service;
    private final MemberMapper memberMapper;


    @PostMapping
    public ResponseEntity postMember (@Valid @RequestBody MemberDto.Post requestBody) {
        Member member = memberMapper.memberPostToMember(requestBody);
        service.ceateMember(member);
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, member.getMemberId());
        return ResponseEntity.created(location).build();
    }


    @PatchMapping("/{member-id}")
    public ResponseEntity PatchMember (@Valid @RequestBody MemberDto.Patch requestBody ,
                                       @PathVariable("member-id")  @Positive long memberId){
        Member member = memberMapper.memberPatchToMember(requestBody);
        member.setMemberId(memberId);
        Member updateMember = service.updateMember(member);
        return new ResponseEntity<>(
                new SingleResponseDto<>(memberMapper.memberToResponseDto(updateMember)), HttpStatus.OK);

    }
    }


