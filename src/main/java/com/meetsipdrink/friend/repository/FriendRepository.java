package com.meetsipdrink.friend.repository;

import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findByRequesterAndFriendStatus(Member requester, Friend.Status status);

    Friend findByRequesterAndRecipient(Member requester, Member recipient);


    List<Friend> findByRequester_MemberIdOrRecipient_MemberIdAndFriendStatus(
            Long requesterId, Long recipientId, Friend.Status status);
    Optional<Friend> findByRequester_MemberIdAndRecipient_MemberId(Long requesterId ,  Long recipientId);

    Optional<Friend> findByRequester_MemberIdAndRecipient_MemberIdAndFriendStatusIn(Long requesterId, Long recipientId, List<Friend.Status> statuses);

}
