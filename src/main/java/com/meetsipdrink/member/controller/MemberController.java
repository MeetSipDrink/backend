package com.meetsipdrink.member.controller;


import com.meetsipdrink.ban.dto.BanDto;
import com.meetsipdrink.ban.service.BanService;
import com.meetsipdrink.dto.MultiResponseDto;
import com.meetsipdrink.dto.SingleResponseDto;
import com.meetsipdrink.friend.service.FriendService;
import com.meetsipdrink.member.dto.MemberDto;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.mapper.MemberMapper;
import com.meetsipdrink.member.service.MemberService;
import com.meetsipdrink.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberService service;
    private final MemberMapper memberMapper;
    private final MemberService memberService;
    private final FriendService friendService;
    private final BanService banService;

    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody MemberDto.Post requestBody) {
        Member member = memberMapper.memberPostToMember(requestBody);
        service.createMember(member);
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, member.getMemberId());
        return ResponseEntity.created(location).build();
    }


    @PatchMapping("/{member-id}")
    public ResponseEntity PatchMember(@Valid @RequestBody MemberDto.Patch requestBody,
                                      @PathVariable("member-id") @Positive long memberId) {
        Member member = memberMapper.memberPatchToMember(requestBody);
        member.setMemberId(memberId);
        Member updateMember = service.updateMember(member);
        return new ResponseEntity<>(
                new SingleResponseDto<>(memberMapper.memberToResponseDto(updateMember)), HttpStatus.OK);

    }

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(
            @PathVariable("member-id") @Positive long memberId) {
        Member member = service.findMember(memberId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(memberMapper.memberToResponseDto(member))
                , HttpStatus.OK);
    }

//    @GetMapping("/{memberId}")
//    public ResponseEntity<List<BanDto.banListResponse>> getBanList(@PathVariable long memberId) {
//        List<BanDto.Response> banDtoList = banService.getBanList(memberId);
//
//        List<BanDto.banListResponse> banList = banDtoList.stream()
//                .filter(banDto -> banDto != null)
//                .map(banDto -> {
//                    BanDto.banListResponse response = new BanDto.banListResponse();
//                    response.setBanId(banDto.getBanId());
//                    response.setMemberId(banDto.getMemberId());
//                    response.setBanMemberId(banDto.getBanMemberId());
//                    response.setMemberNickname(banDto.getMemberNickname());
//                    response.setBannedNickname(banDto.getBannedNickname());
//                    return response;
//                })
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(banList, HttpStatus.OK);
//    }


    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId) {
        service.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}


