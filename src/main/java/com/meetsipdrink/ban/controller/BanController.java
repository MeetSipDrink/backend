package com.meetsipdrink.ban.controller;

import com.meetsipdrink.ban.entity.Ban;
import com.meetsipdrink.ban.mapper.BanMapper;
import com.meetsipdrink.friend.service.FriendService;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.service.MemberService;
import com.meetsipdrink.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import com.meetsipdrink.ban.dto.BanDto;
import com.meetsipdrink.ban.service.BanService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ban")
@RequiredArgsConstructor
public class BanController {

    private final BanService banService;
    private final BanMapper banMapper;
    private final static String BAN_DEFAULT_URL = "/ban";
    private final FriendService friendService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<BanDto.Response> addBan(@RequestBody BanDto.Post post) {
        try {
            boolean isFriend = friendService.isFriend(post.getEmail(), post.getBlockedMemberId());
            BanDto.Response response = banService.addBan(post.getEmail(), post.getBlockedMemberId());
            if (response == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
            if (isFriend) {
                friendService.removeFriend(post.getEmail(), post.getBlockedMemberId());
            }
            URI location = UriCreator.createUri(BAN_DEFAULT_URL, response.getBanId());
            return ResponseEntity.created(location).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<BanDto.banListResponse>> getBanList(@AuthenticationPrincipal Object principal) {
        List<BanDto.Response> banDtoList = banService.getBanList(principal.toString());
        List<BanDto.banListResponse> banList = banDtoList.stream()
                .filter(ban -> ban != null)
                .map(ban -> {
                    BanDto.banListResponse response = new BanDto.banListResponse();
                    response.setBanId(ban.getBanId());
                    response.setMemberId(ban.getMemberId());
                    response.setBanMemberId(ban.getBanMemberId());
                    response.setMemberNickname(ban.getMemberNickname());
                    response.setBannedNickname(ban.getBannedNickname());
                    return response;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(banList, HttpStatus.OK);
    }

    @DeleteMapping("/{blockedId}")
    public ResponseEntity<Void> cancelBan(@AuthenticationPrincipal Object principal,
                                          @PathVariable("blockedId") @Positive long blockedId) {
        Member blockerMember = memberService.findMemberByEmail(principal.toString());
        Member blockedMember = memberService.findVerifiedMember(blockedId);

        banService.cancelBan(blockerMember, blockedMember);
        return ResponseEntity.noContent().build();
    }
}
