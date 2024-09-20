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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity addFriendRequest(@RequestBody FriendDto.RequestDto requestDto,
                                           @AuthenticationPrincipal Object principal) {
        requestDto.setEmail(principal.toString());
        Long requesterId = friendService.addFriend(requestDto.getEmail(), requestDto.getRecipientId());
        URI location = UriCreator.createUri(FRIEND_DEFAULT_URL, requesterId);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping
    public ResponseEntity deleteFriendRequest(@RequestBody FriendDto.RequestDto requestDto,
                                              @AuthenticationPrincipal Object principal) {
        requestDto.setEmail(principal.toString());
        friendService.rejectFriendRequest(requestDto.getEmail(), requestDto.getRecipientId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptFriendRequest(@RequestBody FriendDto.AcceptFriendDto requestDto,
                                                    @AuthenticationPrincipal Object principal) {
        friendService.acceptFriendRequest(principal.toString(), requestDto.getRecipientId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
//    @GetMapping("/{member-id}/{status}")
//    public ResponseEntity getFriends(@PathVariable("member-id") long memberId,
//                                     @PathVariable("status") String status) {
//        Friend.Status enumFriendStatus = friendService.convertToFriendStatus(status);
//        List<Member> friends = friendService.getFriends(memberId, enumFriendStatus);
//        List<FriendDto.ResponseDto> responseList = friendMapper.friendsToResponse(friends);
//        return new ResponseEntity<>(new SingleResponseDto<>(responseList), HttpStatus.OK);
//    }


    @GetMapping("/{status}")
    public ResponseEntity getFriends(@AuthenticationPrincipal Object principal,
                                     @PathVariable("status") String status) {
        Friend.Status enumFriendStatus = friendService.convertToFriendStatus(status);
        List<Member> friends = friendService.getFriends(principal.toString(), enumFriendStatus);
        List<FriendDto.ResponseDto> responseList = friendMapper.friendsToResponse(friends);
        return new ResponseEntity<>(new SingleResponseDto<>(responseList), HttpStatus.OK);
    }





    @GetMapping("/friend/{friend-id}")
    public ResponseEntity getFriend(@AuthenticationPrincipal Object principal,
                                    @PathVariable("friend-id") long friendId) {
        Friend friend = friendService.getFriend(principal.toString(), friendId);
        FriendDto.ResponseDto response = friendMapper.friendToResponse(friend);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity removeFriend(@RequestBody FriendDto.RequestDto requestDto,
                                       @AuthenticationPrincipal Object principal) {
        friendService.removeFriend(principal.toString(), requestDto.getRecipientId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
