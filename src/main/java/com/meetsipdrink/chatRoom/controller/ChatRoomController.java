package com.meetsipdrink.chatRoom.controller;

import com.meetsipdrink.chatRoom.dto.ChatRoomDto;
import com.meetsipdrink.chatRoom.entity.ChatRoom;
import com.meetsipdrink.chatRoom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chatrooms")
public class ChatRoomController {
    private  final ChatRoomService service;

//    @PostMapping
//    public ResponseEntity<ChatRoom> createChatRoom(@RequestParam String roomName, @RequestParam Long memberId) {
//        ChatRoom chatRoom = service.createChatRoom(roomName, memberId);
//        return ResponseEntity.ok(chatRoom);
//    }

    private static final Logger logger = LoggerFactory.getLogger(ChatRoomController.class);


    @PostMapping
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomDto.Request request) {
        logger.info("Received request to create chat room: {}", request);

        if (request.getRoomName() == null || request.getRoomName().trim().isEmpty()) {
            logger.error("Room name is null or empty");
            return ResponseEntity.badRequest().body("Room name cannot be null or empty");
        }

        if (request.getMemberId() == null) {
            logger.error("Member ID is null");
            return ResponseEntity.badRequest().body("Member ID cannot be null");
        }

        try {
            ChatRoom chatRoom = service.createChatRoom(request.getRoomName().trim(), request.getMemberId());
            logger.info("Chat room created: {}", chatRoom);
            return ResponseEntity.ok(chatRoom);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating chat room: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error creating chat room", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatRoomDto.ChatRoomResponse> getChatRoomById(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = service.findChatRoomById(chatRoomId);

        // ChatRoomResponse DTO로 변환
        ChatRoomDto.ChatRoomResponse response = new ChatRoomDto.ChatRoomResponse();
        response.setChatRoomId(chatRoom.getChatRoomId());
        response.setMemberId(chatRoom.getHost().getMemberId());
        response.setChatRoomName(chatRoom.getRoomName());
        response.setParticipant(chatRoom.getParticipant());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ChatRoom>> getAllChatRooms() {
        List<ChatRoom> chatRooms = service.findAllChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

//    @PostMapping("/{chatRoomId}/participant")
//    public ResponseEntity<String> addParticipant(@PathVariable Long chatRoomId, @RequestParam Long memberId) {
//        service.addParticipantToChatRoom(chatRoomId, memberId);
//        return ResponseEntity.ok("Participant added.");
//    }
//
//    // 채팅방에서 참여자 제거
//    @DeleteMapping("/{chatRoomId}/participant")
//    public ResponseEntity<String> removeParticipant(@PathVariable Long chatRoomId, @RequestParam Long memberId) {
//        service.removeParticipantFromChatRoom(chatRoomId, memberId);
//        return ResponseEntity.ok("Participant removed.");
//    }

    //get 으로  프론트에서 받은 결과로 백에서 찾아주면 된다 .
    //채팅방 이름이랑 해시테그 post 요청오면
    // 방 list 를 보내주기 room 이름에 이 글자가 있고 이 해시테그가 있는
    // 이 두개가 부합되는 걸 보내줘야한다 ...

    // 채팅방에 참가자 추가 API
    @PostMapping("/{chatRoomId}/participant")
    public ResponseEntity<String> addParticipant(@PathVariable Long chatRoomId, @RequestParam Long memberId) {
        service.addParticipantToChatRoom(chatRoomId, memberId);
        return ResponseEntity.ok("Participant added.");
    }

    // 채팅방에서 참가자 제거 API
    @DeleteMapping("/{chatRoomId}/participant")
    public ResponseEntity<String> removeParticipant(@PathVariable Long chatRoomId, @RequestParam Long memberId) {
        service.removeParticipantFromChatRoom(chatRoomId, memberId);
        return ResponseEntity.ok("Participant removed.");
    }



}
