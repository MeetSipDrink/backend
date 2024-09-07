package com.meetsipdrink.friend.service;

import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.friend.repository.FriendRepository;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.repository.MemberRepository;
import com.meetsipdrink.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {
    public final FriendRepository repository;
    public final MemberRepository memberRepository;
    public final FriendRepository friendRepository;
    public final MemberService memberService;

    @Transactional
    public void addFriend(String memberNickname, String friendNickname) {
        Member member = memberRepository.findByNickname(memberNickname)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Member friendMember = memberRepository.findByNickname(friendNickname)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Friend friend = new Friend();
        friend.setFriend(friendMember);
        friend.setMember(member);
        friend.setFriendStatus(Friend.Status.PENDING);
        friendRepository.save(friend);
    }

    @Transactional(readOnly = true)
    public List<Member> getsFriends(String memberNickname, Friend.Status status) {
        Member findmember = memberRepository.findByNickname(memberNickname)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        List<Friend> friends = friendRepository.findByMemberAndFriendStatus(findmember, status);
        List<Member> members = new ArrayList<>();
        for (Friend friend : friends) {
            members.add(friend.getFriend());
        }

        return members;
    }



    @Transactional
    public void acceptFriendRequest(long friendId, String memberNickname) {
        Optional<Friend> optionalFriend = friendRepository.findById(friendId);
        Friend friend = optionalFriend.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        friend.setFriendStatus(Friend.Status.ACCEPTED);
        friendRepository.save(friend);
        Friend reverseFriend = new Friend();
        reverseFriend.setMember(friend.getFriend());
        reverseFriend.setFriend(friend.getFriend());
        reverseFriend.setFriendStatus(Friend.Status.ACCEPTED);
        friendRepository.save(reverseFriend);

    }

    //친구 거절
    @Transactional
    public void rejectFriendRequest(String memberNickname, String friendNickname) {
        Member member = memberRepository.findByNickname(memberNickname)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Member friendMember = memberRepository.findByNickname(friendNickname)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Friend friendRequest = friendRepository.findByMemberAndFriend(member, friendMember)
                .orElseThrow(() -> new IllegalArgumentException("친구요청 찾을 수 없음"));

        friendRepository.delete(friendRequest);

    }


    public Friend getFriend(String memberNickname, String friendNickname) {
        Member member = memberRepository.findByNickname(memberNickname)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Member friend = memberRepository.findByEmail(friendNickname)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return friendRepository.findByMemberAndFriend(member, friend)
                .orElseThrow(() -> new IllegalArgumentException("친구요청을 찾을수 없음"));
    }
    @org.springframework.transaction.annotation.Transactional
    public Page<Friend> getFriends(int page, int size) {
        return friendRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }
}






