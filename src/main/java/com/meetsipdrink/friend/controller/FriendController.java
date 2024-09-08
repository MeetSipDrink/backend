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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final FriendMapper friendMapper;
    private final MemberMapper mapper;


    @PostMapping
    public ResponseEntity addFriendRequest(@RequestBody FriendDto.RequestDto requestDto) {
        friendService.addFriend(requestDto.getRequesterId(), requestDto.getRecipientId());
        return new ResponseEntity<>(HttpStatus.CREATED);
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
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/{member-id}/{friend-id}")
    public ResponseEntity<FriendDto.ResponseDto> getFriend(
            @PathVariable("member-id") long memberId,
            @PathVariable("friend-id") long friendId) {

        Friend friend = friendService.getFriend(memberId, friendId);
        FriendDto.ResponseDto response = friendMapper.friendToResponse(friend);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    public ResponseEntity removeFriend(@RequestBody FriendDto.RequestDto requestDto) {
        friendService.removeFriend(requestDto.getRequesterId(), requestDto.getRecipientId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}







//    @PostMapping("/request")
//    public ResponseEntity addFriend
//
//
//
//    @GetMapping("/{friend-nickname}/{member-nickname}")
//    public ResponseEntity getFriend (@PathVariable("friend-nickname") String friendNickname,
//                                     @PathVariable("member-nickname") String memberNickname){
//        Friend friend = friendService.getFriend(friendNickname,memberNickname);
//        FriendDto.Response response = friendMapper.friendToResponse(friend);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//
//    }
//
//    @GetMapping("/{member-nickname}/friends")
//    public ResponseEntity getFriends(@PathVariable("member-nickname") String memberNickname,
//                                     @RequestParam("status") Friend.Status status) {
//
//        List<Member> friends = friendService.getsFriends(memberNickname, status);
//
//        if (friends == null || friends.isEmpty()) {
//            return new ResponseEntity<>("친구를 찾을 수 없다 ", HttpStatus.NOT_FOUND);
//        }
//
//        List<MemberDto.Response> responseList = mapper.membersToResponseDto(friends);
//        return new ResponseEntity<>(responseList, HttpStatus.OK);
//    }
//
//




