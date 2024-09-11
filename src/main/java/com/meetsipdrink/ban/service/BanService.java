package com.meetsipdrink.ban.service;


import com.meetsipdrink.ban.dto.BanDto;
import com.meetsipdrink.ban.entity.Ban;
import com.meetsipdrink.ban.mapper.BanMapper;
import com.meetsipdrink.ban.repository.BanRepository;
import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.friend.entitiy.Friend;
import com.meetsipdrink.friend.repository.FriendRepository;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class BanService {

    private final MemberRepository memberRepository;
    private final BanRepository banRepository;
    private final FriendRepository friendRepository;
    private final BanMapper banMapper;






    public BanDto.Response addBan(long blockerId, long blockedMemberId) {
        Member blocker = memberRepository.findById(blockerId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BLOCKING_MEMBER_NOT_FOUND));
        Member blockedMember = memberRepository.findById(blockedMemberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BANNED_MEMBER_NOT_FOUND));
        Friend friend1 = friendRepository.findByRequesterAndRecipient(blocker, blockedMember);
        Friend friend2 = friendRepository.findByRequesterAndRecipient(blockedMember, blocker);
        if (friend1 != null) {
            friendRepository.delete(friend1);
        }
        if (friend2 != null) {
            friendRepository.delete(friend2);
        }
        Ban ban = new Ban();
        ban.setBlockerMember(blocker);
        ban.setBlockedMember(blockedMember);
        banRepository.save(ban);
        BanDto.Response response = new BanDto.Response();
        response.setBanId(ban.getBanId());
        response.setMemberId(blocker.getMemberId());
        response.setBanMemberId(blockedMember.getMemberId());
        response.setMemberNickname(blocker.getNickname());
        response.setBannedNickname(blockedMember.getNickname());

        return response;

    }

//    @Transactional
//    public BanDto.Response addBan(BanDto.Request banRequest) {
//        Member blockingMember = memberRepository.findById(banRequest.getMemberId())
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BLOCKING_MEMBER_NOT_FOUND));
//
//        Member bannedMember = memberRepository.findById(banRequest.getBanMemberId())
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BANNED_MEMBER_NOT_FOUND));
//
//        Friend friend = friendRepository.findByRequesterAndRecipient(blockingMember, bannedMember);
//        if (friend == null) {
//            friend = friendRepository.findByRequesterAndRecipient(bannedMember, blockingMember);
//        }
//        if (friend != null) {
//            friendRepository.delete(friend);
//        }
//
//        Ban ban = new Ban();
//        ban.setBlockingMember(blockingMember);
//        ban.setBannedMember(bannedMember);
//        Ban savedBan = banRepository.save(ban);
//        return banMapper.banToBanResponse(savedBan);
//    }

    @Transactional(readOnly = true)
    public List<BanDto.Response> getBanList(long blockerMember) {
        Member blocker = memberRepository.findById(blockerMember)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        List<Ban> banList = banRepository.findByBlockerMember_MemberId(blocker.getMemberId());
        // 매핑 전에 banList 값 확인
        for (Ban ban : banList) {
            System.out.println("Ban ID: " + ban.getBanId());
            System.out.println("Blocker Member ID: " + ban.getBlockerMember().getMemberId());
            System.out.println("Blocker Nickname: " + ban.getBlockerMember().getNickname());
            System.out.println("Banned Member ID: " + ban.getBlockedMember().getMemberId());
            System.out.println("Banned Nickname: " + ban.getBlockedMember().getNickname());
        }
        return banMapper.banToBanResponseList(banList);

    }





    @Transactional
    public void cancelBan(Member blockerMember, Member blockedMember) {
        Ban ban = banRepository.findByBlockerMemberAndBlockedMember(blockerMember, blockedMember)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BAN_RECORD_NOT_FOUND));

        banRepository.delete(ban);
    }




}








