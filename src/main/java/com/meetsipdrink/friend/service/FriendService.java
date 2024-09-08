package com.meetsipdrink.friend.service;

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
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void addFriend(long requesterId, long recipientId) {
        Member requester = memberRepository.findById(requesterId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Member recipient = memberRepository.findById(recipientId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        // 기존에 친구 요청이 있는지 확인
        Friend existingRequest = friendRepository.findByRequesterAndRecipient(requester, recipient);
        if (existingRequest != null) {
            throw new BusinessLogicException(ExceptionCode.FRIEND_REQUEST_ALREADY_EXISTS);
        }

        // 상대방이 이미 친구 요청을 보냈는지 확인
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
            // 새로운 친구 요청 생성
            Friend friendRequest = new Friend();
            friendRequest.setRequester(requester);
            friendRequest.setRecipient(recipient);
            friendRequest.setFriendStatus(Friend.Status.PENDING);
            friendRepository.save(friendRequest);
        }
    }

    @Transactional
    public void acceptFriendRequest(long friendId, long recipientId) {
        Member member = memberRepository.findById(recipientId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Friend friendRequest = friendRepository.findById(friendId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.FRIEND_REQUEST_NOT_FOUND));

        if (!friendRequest.getRecipient().equals(member)) {
            throw new BusinessLogicException(ExceptionCode.NOT_FRIEND_RECIPIENT);
        }

        friendRequest.setFriendStatus(Friend.Status.ACCEPTED);
        friendRepository.save(friendRequest);

        // 역으로 친구 요청을 생성
        Friend reverseFriendRequest = new Friend();
        reverseFriendRequest.setRequester(member);
        reverseFriendRequest.setRecipient(friendRequest.getRequester());
        reverseFriendRequest.setFriendStatus(Friend.Status.ACCEPTED);
        friendRepository.save(reverseFriendRequest);
    }

    @Transactional
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

    @Transactional
    public void removeFriend(long requesterId, long recipientId) {
        Member requester = memberRepository.findById(requesterId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Member recipient = memberRepository.findById(recipientId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        Friend friendRequest = friendRepository.findByRequesterAndRecipient(requester, recipient);
        if (friendRequest != null) {
            friendRepository.delete(friendRequest);
        }
        Friend reverseFriendRequest = friendRepository.findByRequesterAndRecipient(recipient, requester);
        if (reverseFriendRequest != null) {
            friendRepository.delete(reverseFriendRequest);
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
}
