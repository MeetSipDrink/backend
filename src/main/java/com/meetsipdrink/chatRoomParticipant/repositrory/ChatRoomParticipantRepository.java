package com.meetsipdrink.chatRoomParticipant.repositrory;

import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.chatRoomParticipant.entity.ChatRoomParticipant;
import com.meetsipdrink.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant, Long> {
    List<ChatRoomParticipant> findByChatRoom_ChatRoomId(Long chatRoomId); // 채팅방 ID로 참가자 조회
    Optional<ChatRoomParticipant> findByChatRoom_ChatRoomIdAndParticipant(Long chatRoomId, Member participant); // 수정된 메소드
    Optional<ChatRoomParticipant> findByParticipant(Member member);
    int countByChatRoom_ChatRoomId(Long chatRoomId);
}
