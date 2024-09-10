package com.meetsipdrink.ban.controller;

import com.meetsipdrink.ban.entity.Ban;
import com.meetsipdrink.ban.mapper.BanMapper;
import com.meetsipdrink.friend.service.FriendService;
import com.meetsipdrink.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.meetsipdrink.ban.dto.BanDto;
import com.meetsipdrink.ban.service.BanService;

import javax.validation.Valid;
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

    @PostMapping
    public ResponseEntity<BanDto.Response> addBan(@RequestBody BanDto.Post post) {
        try {
            boolean isFriend = friendService.isFriend(post.getBlockerId(), post.getBlockedMemberId());
            BanDto.Response response = banService.addBan(post.getBlockerId(), post.getBlockedMemberId());
            if (response == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
            if (isFriend) {
                friendService.removeFriend(post.getBlockerId(), post.getBlockedMemberId());
            }
            URI location = UriCreator.createUri(BAN_DEFAULT_URL, response.getBanId());
            return ResponseEntity.created(location).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{memberId}")
    public ResponseEntity<List<BanDto.banListResponse>> getBanList(@PathVariable long memberId) {
        List<BanDto.Response> banDtoList = banService.getBanList(memberId);

        List<BanDto.banListResponse> banList = banDtoList.stream()
                .filter(banDto -> banDto != null)
                .map(banDto -> {
                    BanDto.banListResponse response = new BanDto.banListResponse();
                    response.setBanId(banDto.getBanId());
                    response.setMemberId(banDto.getMemberId());
                    response.setBanMemberId(banDto.getBanMemberId());
                    response.setMemberNickname(banDto.getMemberNickname());
                    response.setBannedNickname(banDto.getBannedNickname());
                    return response;
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(banList, HttpStatus.OK);
    }

    @DeleteMapping("/{banId}")
    public ResponseEntity cancelBan(@PathVariable long banId) {
        banService.cancelBan(banId);
        return ResponseEntity.noContent().build();
    }

}
