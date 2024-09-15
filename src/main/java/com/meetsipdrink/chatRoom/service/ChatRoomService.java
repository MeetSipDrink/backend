package com.meetsipdrink.chatRoom.service;

import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.chatRoom.repository.ChatRoomRepository;
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

    //채팅방 생성
//    public ChatRoom createChatRoom(String name, Long memberId) {
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
//
//        ChatRoom chatRoom = new ChatRoom(name, member);
//        member.addChatRoom(chatRoom); // 양방향 관계 설정
//
//        return chatRoomRepository.save(chatRoom);
//    }


    //입장
    public boolean addParticipantToChatRoom(Long chatRoomId, String username) {
        ChatRoom chatRoom = findChatRoomById(chatRoomId);
        if (chatRoom == null) {
            return false;
        }
        boolean added = chatRoom.addParticipant(username);
        chatRoomRepository.save(chatRoom);
        return added;
    }

    //퇴장
    public void removeParticipantFromChatRoom(Long chatRoomId, String username) {
        ChatRoom chatRoom = findChatRoomById(chatRoomId);
        if (chatRoom != null) {
            chatRoom.removeParticipant(username);
            chatRoomRepository.save(chatRoom);
        }
    }



    public ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElse(null);
    }

    public List<ChatRoom> findAllChatRooms(Long chatRoomId) {
        return chatRoomRepository.findAll();
    }

}
