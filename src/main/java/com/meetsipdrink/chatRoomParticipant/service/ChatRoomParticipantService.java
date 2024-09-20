package com.meetsipdrink.chatRoomParticipant.service;


import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.chatRoom.repository.ChatRoomRepository;
import com.meetsipdrink.chatRoomParticipant.entity.ChatRoomParticipant;
import com.meetsipdrink.chatRoomParticipant.repositrory.ChatRoomParticipantRepository;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class ChatRoomParticipantService {
    private final ChatRoomParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomParticipantService(ChatRoomParticipantRepository participantRepository, MemberRepository memberRepository, ChatRoomRepository chatRoomRepository) {
        this.participantRepository = participantRepository;
        this.memberRepository = memberRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    //입장 시 참여자 추가
    @Transactional
    public void addParticipant(Long chatRoomId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID"));

        // 멤버가 이미 다른 채팅방에 참여 중인지 확인
        Optional<ChatRoomParticipant> existingParticipant = participantRepository.findByParticipant(member);
        if (existingParticipant.isPresent()) {
            throw new IllegalStateException("This member is already participating in another chat room.");
        }

        // 참여자가 없으면 새로운 채팅방에 참여
        ChatRoomParticipant participant = new ChatRoomParticipant(chatRoom, member);
        participantRepository.save(participant);

        member.setChatRoomStatus(true);
        memberRepository.save(member);
    }

    // 참여자 제거
    @Transactional
    public void removeParticipant(Long chatRoomId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        ChatRoomParticipant participant = participantRepository
                .findByChatRoom_ChatRoomIdAndParticipant(chatRoomId, member)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));

        participantRepository.delete(participant);
        member.setChatRoomStatus(false);
        memberRepository.save(member);

        // 채팅방에 남은 참여자가 있는지 확인
        int remainingParticipants = participantRepository.countByChatRoom_ChatRoomId(chatRoomId);
        if (remainingParticipants == 0) {
            // 참여자가 0명이면 채팅방 삭제
            chatRoomRepository.deleteById(chatRoomId);
        }
    }

    // 특정 채팅방 참여자 수 세기
    public int countParticipantInChatRoom(Long chatRoomId) {
        return participantRepository.countByChatRoom_ChatRoomId(chatRoomId);
    }
}
