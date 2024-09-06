package com.meetsipdrink.friend.controller;


import com.meetsipdrink.friend.dto.FriendDto;
import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.friend.mapper.FriendMapper;
import com.meetsipdrink.friend.service.FriendService;
import com.meetsipdrink.member.dto.MemberDto;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.mapper.MemberMapper;
import com.meetsipdrink.member.repository.MemberRepository;
import com.meetsipdrink.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final FriendMapper friendMapper;
    private final MemberMapper mapper;

    @GetMapping("/{friend-nickname}/{member-nickname}")
    public ResponseEntity getFriend (@PathVariable("friend-nickname") String friendNickname,
                                     @PathVariable("member-nickname") String memberNickname){
        Friend friend = friendService.getFriend(friendNickname,memberNickname);
        FriendDto.Response response = friendMapper.friendToResponse(friend);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/{member-nickname}/friends")
    public ResponseEntity getFriends(@PathVariable("member-nickname") String memberNickname,
                                     @RequestParam("status") Friend.Status status) {

        List<Member> friends = friendService.getsFriends(memberNickname, status);

        if (friends == null || friends.isEmpty()) {
            return new ResponseEntity<>("친구를 찾을 수 없다 ", HttpStatus.NOT_FOUND);
        }

        List<MemberDto.Response> responseList = mapper.membersToResponseDto(friends);
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }





}
