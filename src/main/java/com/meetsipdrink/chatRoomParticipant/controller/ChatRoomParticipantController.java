package com.meetsipdrink.chatRoomParticipant.controller;


import com.meetsipdrink.chatRoomParticipant.service.ChatRoomParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatrooms/{chatRoomId}")
public class ChatRoomParticipantController {
    private final ChatRoomParticipantService participantService;

    public ChatRoomParticipantController(ChatRoomParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping("/{memberId}")
    public ResponseEntity addParticipant (@PathVariable Long chatRoomId ,
                                          @PathVariable Long memberId){
        participantService.addParticipant(chatRoomId , memberId);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity removeParticipant (@PathVariable Long chatRoomId,
                                             @PathVariable Long memberId){
        participantService.removeParticipant(chatRoomId,memberId);
        return ResponseEntity.ok().build();
    }


}
