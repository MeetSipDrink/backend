package com.meetsipdrink.friend.repository;

import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    List<Friend> findByRequesterAndFriendStatus(Member requester, Friend.Status status);

    List<Friend> findByRecipientAndFriendStatus(Member recipient, Friend.Status status);

    Friend findByRequesterAndRecipient(Member requester, Member recipient);
}
