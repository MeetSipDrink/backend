package com.meetsipdrink.member.controller;


import com.meetsipdrink.ban.dto.BanDto;
import com.meetsipdrink.ban.service.BanService;
import com.meetsipdrink.dto.MultiResponseDto;
import com.meetsipdrink.dto.SingleResponseDto;
import com.meetsipdrink.friend.service.FriendService;
import com.meetsipdrink.member.dto.MemberDto;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.mapper.MemberMapper;
import com.meetsipdrink.member.service.MemberService;
import com.meetsipdrink.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberService service;
    private final MemberMapper memberMapper;
    private final MemberService memberService;
    private final FriendService friendService;
    private final BanService banService;



    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @RestController
    @RequestMapping("/members")
    @RequiredArgsConstructor
    public class ChatController {

        private final MemberService memberService;
        private final MemberMapper memberMapper;
        private final Logger logger = LoggerFactory.getLogger(ChatController.class);


        @GetMapping("/by-token-email")
        public ResponseEntity<SingleResponseDto<MemberDto.Response>> getMemberByEmailFromToken(@AuthenticationPrincipal Object principal) {
            logger.info("Received request to get member by token email for principal: {}", principal);

            try {
                if (principal == null) {
                    logger.error("Principal is null, cannot fetch member info");
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                String email = principal.toString(); // JWT에서 이메일 추출
                logger.info("Extracted email from token: {}", email);

                Member member = memberService.findMemberByEmail(email);
                if (member == null) {
                    logger.error("Member not found for email: {}", email);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                // Log member details for debugging
                logger.info("Found member: {}", member);

                // 응답 DTO로 변환
                MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
                return new ResponseEntity<>(new SingleResponseDto<>(responseDto), HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error occurred while fetching member info by token email", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }


        @GetMapping("/me")
        public ResponseEntity<SingleResponseDto<MemberDto.Response>> getMemberInfo(@AuthenticationPrincipal Object principal) {
            logger.info("Received request to get member info for principal: {}", principal);

            try {
                if (principal == null) {
                    logger.error("Principal is null, cannot fetch member info");
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                String email = principal.toString();
                logger.info("Extracted email: {}", email);

                Member member = memberService.findMemberByEmail(email);
                if (member == null) {
                    logger.error("Member not found for email: {}", email);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                // Log member details for debugging
                logger.info("Found member: {}", member);

                // 응답 DTO로 변환
                MemberDto.Response responseDto = memberMapper.memberToResponseDto(member);
                return new ResponseEntity<>(new SingleResponseDto<>(responseDto), HttpStatus.OK);
            } catch (Exception e) {
                logger.error("Error occurred while fetching member info", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // 사용자 닉네임을 JWT 토큰의 이메일을 통해 반환하는 메서드
        @GetMapping("/me/nickname")
        public ResponseEntity<SingleResponseDto<String>> getNicknameByEmail(@AuthenticationPrincipal Object principal) {
            logger.info("Received request to get nickname for principal: {}", principal);

            try {
                if (principal == null) {
                    logger.error("Principal is null, cannot fetch nickname");
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                String email = principal.toString();
                logger.info("Extracted email: {}", email);

                Member member = memberService.findMemberByEmail(email);
                if (member == null) {
                    logger.error("Member not found for email: {}", email);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }

                logger.info("Found member: {}", member);

                return new ResponseEntity<>(
                        new SingleResponseDto<>(member.getNickname()),  // 닉네임을 응답으로 반환
                        HttpStatus.OK
                );
            } catch (Exception e) {
                logger.error("Error occurred while fetching nickname", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }


    @PostMapping
    public ResponseEntity postMember(@Valid @RequestBody MemberDto.Post requestBody) {
        Member member = memberMapper.memberPostToMember(requestBody);
        service.createMember(member);
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, member.getMemberId());
        return ResponseEntity.created(location).build();
    }


    @PatchMapping
    public ResponseEntity PatchMember(@Valid @RequestBody MemberDto.Patch requestBody,
                                      @AuthenticationPrincipal Object principal) {
        Member member = memberMapper.memberPatchToMember(requestBody);
        member.setEmail(principal.toString());
        Member updateMember = service.updateMember(member);
        return new ResponseEntity<>(
                new SingleResponseDto<>(memberMapper.memberToResponseDto(updateMember)), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity getMember(@AuthenticationPrincipal Object principal) {
        Member member = service.findMemberByEmail(principal.toString());
        return new ResponseEntity<>(
                new SingleResponseDto<>(memberMapper.memberToResponseDto(member))
                , HttpStatus.OK);
    }

    @GetMapping("/{nickname}")
    public ResponseEntity getMember(@PathVariable("nickname") String nickname) {
        Member member = service.findMemberByNickName(nickname);
        return new ResponseEntity<>(
                new SingleResponseDto<>(memberMapper.memberToResponseDto(member))
                , HttpStatus.OK);
    }

//    @GetMapping("/{memberId}")
//    public ResponseEntity<List<BanDto.banListResponse>> getBanList(@PathVariable long memberId) {
//        List<BanDto.Response> banDtoList = banService.getBanList(memberId);
//
//        List<BanDto.banListResponse> banList = banDtoList.stream()
//                .filter(banDto -> banDto != null)
//                .map(banDto -> {
//                    BanDto.banListResponse response = new BanDto.banListResponse();
//                    response.setBanId(banDto.getBanId());
//                    response.setMemberId(banDto.getMemberId());
//                    response.setBanMemberId(banDto.getBanMemberId());
//                    response.setMemberNickname(banDto.getMemberNickname());
//                    response.setBannedNickname(banDto.getBannedNickname());
//                    return response;
//                })
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(banList, HttpStatus.OK);
//    }


    @DeleteMapping
    public ResponseEntity deleteMember(@AuthenticationPrincipal Object principal) {
        service.deleteMember(principal.toString());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}


