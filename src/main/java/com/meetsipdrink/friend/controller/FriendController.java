package com.meetsipdrink.friend.controller;

import com.meetsipdrink.dto.SingleResponseDto;
import com.meetsipdrink.friend.dto.FriendDto;
import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.friend.mapper.FriendMapper;
import com.meetsipdrink.friend.service.FriendService;
import com.meetsipdrink.member.dto.MemberDto;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.mapper.MemberMapper;
import com.meetsipdrink.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final FriendMapper friendMapper;
    private final MemberMapper mapper;
    private final static String FRIEND_DEFAULT_URL = "/friends";

    @PostMapping
    public ResponseEntity addFriendRequest(@RequestBody FriendDto.RequestDto requestDto) {
        friendService.addFriend(requestDto.getRequesterId(), requestDto.getRecipientId());
        URI location = UriCreator.createUri(FRIEND_DEFAULT_URL, requestDto.getRequesterId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity deleteFriendRequest(@RequestBody FriendDto.RequestDto requestDto) {
        friendService.rejectFriendRequest(requestDto.getRequesterId(), requestDto.getRecipientId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/accept")
    public ResponseEntity acceptFriendRequest(@RequestBody FriendDto.AcceptFriendDto requestDto) {
        friendService.acceptFriendRequest(requestDto.getFriendId(), requestDto.getRecipientId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{member-id}")
    public ResponseEntity getFriends(@PathVariable("member-id") long memberId,
                                     @RequestParam("status") Friend.Status status) {
        List<Member> friends = friendService.getFriends(memberId, status);
        List<MemberDto.Response> responseList = mapper.membersToResponseDto(friends);
        return new ResponseEntity<>(new SingleResponseDto<>(responseList), HttpStatus.OK);
    }

    @GetMapping("/{member-id}/{friend-id}")
    public ResponseEntity getFriend(@PathVariable("member-id") long memberId,
                                    @PathVariable("friend-id") long friendId) {
        Friend friend = friendService.getFriend(memberId, friendId);
        FriendDto.ResponseDto response = friendMapper.friendToResponse(friend);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity removeFriend(@RequestBody FriendDto.RequestDto requestDto) {
        friendService.removeFriend(requestDto.getRequesterId(), requestDto.getRecipientId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
