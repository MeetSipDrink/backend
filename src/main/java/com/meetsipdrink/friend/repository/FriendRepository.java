package com.meetsipdrink.friend.repository;

import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend ,Long> {
    Optional<Friend> findByMemberAndFriend(Member member, Member friend);
//    Optional<Friend> findByMember_EmailAndFriend_Nickname(String member_email, Member friend);
    List<Friend> findByMemberAndFriendStatus(Member member, Friend.Status status);
}
