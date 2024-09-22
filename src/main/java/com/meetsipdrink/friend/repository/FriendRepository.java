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
    // 추가한 코드
    Optional<Friend> findByRequester_EmailAndRecipient_MemberIdAndFriendStatusIn(String email, Long recipientId, List<Friend.Status> statuses);

    Optional<Friend> findByRequester_MemberIdAndRecipient_EmailAndFriendStatusIn(Long recipientId, String email, List<Friend.Status> statuses);

    List<Friend> findByRecipientAndFriendStatus(Member recipient, Friend.Status status);

    @Query("SELECT f FROM Friend f WHERE (f.requester.memberId = :memberId1 AND f.recipient.memberId = :memberId2) OR (f.requester.memberId = :memberId2 AND f.recipient.memberId = :memberId1) AND f.friendStatus = :friendStatus")
    List<Friend> findByMemberIdsAndFriendStatus(
            @Param("memberId1") Long memberId1,
            @Param("memberId2") Long memberId2,
            @Param("friendStatus") Friend.Status friendStatus);
}
