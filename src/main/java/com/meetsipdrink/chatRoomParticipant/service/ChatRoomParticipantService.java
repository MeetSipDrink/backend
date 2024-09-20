package com.meetsipdrink.chatRoomParticipant.service;


import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.chatRoom.repository.ChatRoomRepository;
import com.meetsipdrink.chatRoomParticipant.entity.ChatRoomParticipant;
import com.meetsipdrink.chatRoomParticipant.repositrory.ChatRoomParticipantRepository;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        ChatRoomParticipant participant = new ChatRoomParticipant(chatRoom, member);
        participantRepository.save(participant);
    }

    //참여자 제거
    @Transactional
    public void removeParticipant(Long chatRoomId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        ChatRoomParticipant participant = participantRepository
                .findByChatRoom_ChatRoomIdAndParticipant(chatRoomId, member)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        participantRepository.delete(participant);
    }




    //특정 채팅방 참여자 수 세기
    public int countParticipantInChatRoom(Long chatRoomId) {
        return participantRepository.countByChatRoom_ChatRoomId(chatRoomId);
    }


}
