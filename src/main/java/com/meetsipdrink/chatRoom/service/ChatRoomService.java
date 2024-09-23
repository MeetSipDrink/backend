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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomParticipantService participantService;

    // 채팅방 생성 메서드
    private static final Logger logger = LoggerFactory.getLogger(ChatRoomService.class);

    @Transactional
    public ChatRoom createChatRoom(String roomName, Long memberId) {
        logger.info("Creating chat room with name: {} for member: {}", roomName, memberId);

        if (roomName == null || roomName.trim().isEmpty()) {
            logger.error("Room name is null or empty");
            throw new IllegalArgumentException("Room name cannot be null or empty");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    logger.error("Invalid member ID: {}", memberId);
                    return new IllegalArgumentException("Invalid member ID: " + memberId);
                });

        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findByHost(member);
        if (existingChatRoom.isPresent()) {
            logger.error("Member (ID: {}) already has a chat room", memberId);
            throw new IllegalStateException("This member (ID: " + memberId + ") already has a chat room.");
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomName(roomName.trim());
        chatRoom.setHost(member);
        member.setChatRoom(chatRoom);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        logger.info("Chat room saved: {}", savedChatRoom);

        participantService.addParticipant(savedChatRoom.getChatRoomId(), memberId);
        logger.info("Participant added to chat room");

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
        ChatRoom chatRoom = findChatRoomById(chatRoomId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        participantService.removeParticipant(chatRoomId, memberId);
        // 마지막 참여자일 경우 채팅방 삭제
        int remainingParticipants = participantService.countParticipantInChatRoom(chatRoomId);
        if (remainingParticipants == 0) {
            deleteChatRoom(chatRoomId);
        }
    }
    private void deleteChatRoom(Long chatRoomId) {
        chatRoomRepository.deleteById(chatRoomId);
        log.info("참여자가 없으므로 채팅방을 삭제했습니다. 채팅방 ID: {}", chatRoomId);
    }



    public ChatRoom findChatRoomById(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID"));
        // 필요 시 Lazy 로딩된 필드를 접근
        chatRoom.getHost().getMemberId(); // Lazy Loading 방지
        return chatRoom;
    }



    public List<ChatRoom> findAllChatRooms() {
        return chatRoomRepository.findAll();
    }

}
