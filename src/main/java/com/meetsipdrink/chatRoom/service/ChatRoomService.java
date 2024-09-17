package com.meetsipdrink.chatRoom.service;

import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.chatRoom.repository.ChatRoomRepository;
import com.meetsipdrink.chatRoomParticipant.entity.ChatRoomParticipant;
import com.meetsipdrink.chatRoomParticipant.repositrory.ChatRoomParticipantRepository;
import com.meetsipdrink.chatRoomParticipant.service.ChatRoomParticipantService;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomParticipantService participantService;

//    채팅방 생성
    public ChatRoom createChatRoom(String roomName, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomName(roomName);
        chatRoom.setHost(member);
        member.setChatRoom(chatRoom);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        participantService.addParticipant(savedChatRoom.getChatRoomId(), memberId);
        return savedChatRoom;
    }

    //참여자 추가  이거 participant 서비스단에사 구현
    public boolean addParticipantToChatRoom(Long chatRoomId, Long memberId) {
        participantService.addParticipant(chatRoomId, memberId);
        return true;
    }

//    public boolean addParticipantToChatRoom(Long chatRoomId, Long memberId) {
//        boolean success = participantService.addParticipant(chatRoomId, memberId);
//        if (!success) {
//            // 실패 처리 로직
//            log.warn("채팅방 ID {}에 참여자를 추가하지 못했습니다. 회원 ID: {}", chatRoomId, memberId);
//        }
//        return success;
//    }


//        ChatRoom chatRoom = findChatRoomById(chatRoomId);
//        if (chatRoom == null) {
//            return false;
//        }
//        boolean added = chatRoom.getParticipants().(memberId);
//        chatRoomRepository.save(chatRoom);
//        return added;


    //참여자 제거 이거 participant 서비스단에사 구현
    public void removeParticipantFromChatRoom(Long chatRoomId, Long memberId) {
        participantService.removeParticipant(chatRoomId, memberId);
        int remainingParticipants = participantService.countParticipantInChatRoom(chatRoomId);
        if (remainingParticipants == 0) {
            chatRoomRepository.deleteById(chatRoomId);
            log.info("참여자가 없습니다. 채팅방 ID: {}", chatRoomId);
        }

        //채팅방삭제 참여자가 아예 없으면 삭제 .... ㅠㅠㅠㅠㅠㅠ
        //채팅방 내용은 삭제 안하고 ...
    }
    public ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElse(null);
    }

    public List<ChatRoom> findAllChatRooms() {
        return chatRoomRepository.findAll();
    }

}
