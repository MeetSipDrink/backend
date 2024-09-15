package com.meetsipdrink.friend.service;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.friend.repository.FriendRepository;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {
    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    public void addFriend(long requesterId, long recipientId) {
        Member requester = memberRepository.findById(requesterId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Member recipient = memberRepository.findById(recipientId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Friend existingRequest = friendRepository.findByRequesterAndRecipient(requester, recipient);
        if (existingRequest != null) {
            throw new BusinessLogicException(ExceptionCode.FRIEND_REQUEST_ALREADY_EXISTS);
        }

        Friend reverseRequest = friendRepository.findByRequesterAndRecipient(recipient, requester);
        if (reverseRequest != null) {
            // 상대방이 이미 친구 요청을 보낸 경우, 두 요청 모두 ACCEPTED 상태로 변경
            reverseRequest.setFriendStatus(Friend.Status.ACCEPTED);
            friendRepository.save(reverseRequest);

            Friend friendRequest = new Friend();
            friendRequest.setRequester(requester);
            friendRequest.setRecipient(recipient);
            friendRequest.setFriendStatus(Friend.Status.ACCEPTED);
            friendRepository.save(friendRequest);
        } else {
            Friend friendRequest = new Friend();
            friendRequest.setRequester(requester);
            friendRequest.setRecipient(recipient);
            friendRequest.setFriendStatus(Friend.Status.PENDING);
            friendRepository.save(friendRequest);
        }
    }

    public void acceptFriendRequest(long requesterId, long recipientId) {
        Member recipient = memberRepository.findById(recipientId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Friend friendRequest = friendRepository.findByRequester_MemberIdAndRecipient_MemberId(requesterId, recipientId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.FRIEND_REQUEST_NOT_FOUND));

        if (!friendRequest.getRecipient().equals(recipient)) {
            throw new BusinessLogicException(ExceptionCode.NOT_FRIEND_RECIPIENT);
        }

        friendRequest.setFriendStatus(Friend.Status.ACCEPTED);
        friendRepository.save(friendRequest);

        Friend reverseFriendRequest = new Friend();
        reverseFriendRequest.setRequester(friendRequest.getRecipient());
        reverseFriendRequest.setRecipient(friendRequest.getRequester());
        reverseFriendRequest.setFriendStatus(Friend.Status.ACCEPTED);
        friendRepository.save(reverseFriendRequest);
    }


    public void rejectFriendRequest(long requesterId, long recipientId) {
        Member requester = memberRepository.findById(requesterId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Member recipient = memberRepository.findById(recipientId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Friend friendRequest = friendRepository.findByRequesterAndRecipient(requester, recipient);
        if (friendRequest == null) {
            throw new BusinessLogicException(ExceptionCode.FRIEND_REQUEST_NOT_FOUND);
        }
        friendRepository.delete(friendRequest);
    }

//    public void removeFriend(long requesterId, long recipientId) {
//        Member requester = memberRepository.findById(requesterId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//        Member recipient = memberRepository.findById(recipientId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
//        Friend friendRequest = friendRepository.findByRequesterAndRecipient(requester, recipient);
//        if (friendRequest != null) {
//            friendRepository.delete(friendRequest);
//        }
//        Friend reverseFriendRequest = friendRepository.findByRequesterAndRecipient(recipient, requester);
//        if (reverseFriendRequest != null) {
//            friendRepository.delete(reverseFriendRequest);
//        }
//    }

    public void removeFriend(long memberId, long friendId) {
        // 1. 친구 관계 조회
        List<Friend> friendRelations = friendRepository.findByRequester_MemberIdOrRecipient_MemberIdAndFriendStatus(memberId, friendId, Friend.Status.ACCEPTED);

        // 2. 양방향으로 존재하는 친구 관계 삭제
        for (Friend friend : friendRelations) {
            if (friend.getRequester().getMemberId() == friendId || friend.getRecipient().getMemberId() == friendId) {
                friendRepository.delete(friend);
            }
        }

        Member requester = new Member();
        requester.setMemberId(friendId);
        Member recipient = new Member();
        recipient.setMemberId(memberId);

        Friend reverseFriend = friendRepository.findByRequesterAndRecipient(requester, recipient);
        if (reverseFriend != null) {
            friendRepository.delete(reverseFriend);
        }
    }



    @Transactional(readOnly = true)
    public List<Member> getFriends(long memberId, Friend.Status status) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        List<Friend> friends = friendRepository.findByRequesterAndFriendStatus(member, status);
        List<Member> friendMembers = new ArrayList<>();
        for (Friend friend : friends) {
            friendMembers.add(friend.getRecipient());
        }
        System.out.println("memberId: " + memberId + ", status: " + status);
        return friendMembers;
    }


    //특정 회원이랑 비교 근데 없어도 될 듯
    @Transactional(readOnly = true)
    public Friend getFriend(long memberId, long friendId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Member friend = memberRepository.findById(friendId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Friend friendRequest = friendRepository.findByRequesterAndRecipient(member, friend);
        if (friendRequest == null) {
            throw new BusinessLogicException(ExceptionCode.FRIEND_REQUEST_NOT_FOUND);
        }
        return friendRequest;
    }


    public boolean isFriend(long memberId, long friendId) {
        List<Friend.Status> statuses = Arrays.asList(Friend.Status.ACCEPTED, Friend.Status.PENDING);
        return friendRepository.findByRequester_MemberIdAndRecipient_MemberIdAndFriendStatusIn(memberId, friendId, statuses).isPresent() ||
                friendRepository.findByRequester_MemberIdAndRecipient_MemberIdAndFriendStatusIn(friendId, memberId, statuses).isPresent();
    }

    public Friend.Status convertToFriendStatus(String status){
        try {
            return Friend.Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e){
            throw new BusinessLogicException(ExceptionCode.INVALID_FRIEND_STATUS);
        }
    }


    }


