package com.meetsipdrink.chatRoom.controller;

import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.chatRoom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chatrooms")
public class ChatRoomController {
    private ChatRoomService service;

    @PostMapping
    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam String roomName, @RequestParam Long memberId) {
        ChatRoom chatRoom = service.createChatRoom(roomName, memberId);
        return ResponseEntity.ok(chatRoom);
    }

        @GetMapping("/{chatRoomId}")
        public ResponseEntity<ChatRoom> getChatRoomById(@PathVariable Long chatRoomId) {
            ChatRoom chatRoom = service.findChatRoomById(chatRoomId);
            return ResponseEntity.ok(chatRoom);
        }

    @GetMapping
    public ResponseEntity<List<ChatRoom>> getAllChatRooms() {
        List<ChatRoom> chatRooms = service.findAllChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    @PostMapping("/{chatRoomId}/participant")
    public ResponseEntity<String> addParticipant(@PathVariable Long chatRoomId, @RequestParam Long memberId) {
        service.addParticipantToChatRoom(chatRoomId, memberId);
        return ResponseEntity.ok("Participant added.");
    }

    // 채팅방에서 참여자 제거
    @DeleteMapping("/{chatRoomId}/participant")
    public ResponseEntity<String> removeParticipant(@PathVariable Long chatRoomId, @RequestParam Long memberId) {
        service.removeParticipantFromChatRoom(chatRoomId, memberId);
        return ResponseEntity.ok("Participant removed.");
    }

    //get 으로  프론트에서 받은 결과로 백에서 찾아주면 된다 .
    //채팅방 이름이랑 해시테그 post 요청오면
    // 방 list 를 보내주기 room 이름에 이 글자가 있고 이 해시테그가 있는
    // 이 두개가 부합되는 걸 보내줘야한다 ...

    }



