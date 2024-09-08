package com.meetsipdrink.member.controller;


import com.meetsipdrink.dto.MultiResponseDto;
import com.meetsipdrink.dto.SingleResponseDto;
import com.meetsipdrink.friend.service.FriendService;
import com.meetsipdrink.member.dto.MemberDto;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.mapper.MemberMapper;
import com.meetsipdrink.member.service.MemberService;
import com.meetsipdrink.utils.UriCreator;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ResponseEntity getMembers(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {
        Page<Member> pageMembers = service.findMembers(page - 1, size);
        List<Member> members = pageMembers.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(memberMapper.membersToResponseDto(members),
                        pageMembers),
                HttpStatus.OK);

    }

    @DeleteMapping("/{member-id}")
    public ResponseEntity<Void> deleteMember(@PathVariable("member-id") @Positive long memberId) {
        service.deleteMember(memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


//
//    @PostMapping("/friend/{friend-nickname}")
//    public ResponseEntity addFriend(@PathVariable("friend-nickname") String friendNickname,
//                                    @RequestParam("member-nickname") String memberNickname) {
//        friendService.addFriend(memberNickname, friendNickname);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//
//    @PostMapping("/accept/{friend-id}")
//    public ResponseEntity acceptFriendRequest(@PathVariable("friend-id") @Positive long friendId,
//                                              @RequestParam("member-nickname") String memberNickname) {
//        friendService.acceptFriendRequest(friendId, memberNickname);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//
//    @DeleteMapping("/reject/{member-nickname}/{friend-nickname}")
//    public ResponseEntity rejectFriendRequest(@PathVariable("member-nickname") String memberNickname,
//                                              @PathVariable("friend-nickname") String friendNickname) {
//        friendService.rejectFriendRequest(memberNickname, friendNickname);
//
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//
//    }
}


