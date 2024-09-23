package com.meetsipdrink.chatRoomParticipant.controller;


import com.meetsipdrink.chatRoomParticipant.dto.ChatRoomParticipantDto;
import com.meetsipdrink.chatRoomParticipant.service.ChatRoomParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms/{chatRoomId}/participants")
public class ChatRoomParticipantController {
    private final ChatRoomParticipantService participantService;

    public ChatRoomParticipantController(ChatRoomParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping("/{memberId}")
    public ResponseEntity<Void> addParticipant(@PathVariable Long chatRoomId,
                                               @PathVariable Long memberId) {
        participantService.addParticipant(chatRoomId, memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long chatRoomId,
                                                  @PathVariable Long memberId) {
        participantService.removeParticipant(chatRoomId, memberId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addParticipant(@PathVariable Long chatRoomId, @RequestBody ChatRoomParticipantDto.Request request) {
        participantService.addParticipant(chatRoomId, request.getMemberId());
        return ResponseEntity.status(HttpStatus.OK).body("Participant added.");
    }

}
