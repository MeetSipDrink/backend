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

    public void addBan(long memberId, long banMemberId) {
        Member blockingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BLOCKING_MEMBER_NOT_FOUND));

        Member bannedMember = memberRepository.findById(banMemberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BANNED_MEMBER_NOT_FOUND));
        Friend friend = friendRepository.findByRequesterAndRecipient(blockingMember, bannedMember);
        if (friend == null) {
            friend = friendRepository.findByRequesterAndRecipient(bannedMember, blockingMember);
        }
        if (friend != null) {
            friendRepository.delete(friend);
        }
        Ban ban = new Ban();
        ban.setBlockingMember(blockingMember);
        ban.setBannedMember(bannedMember);
        Ban savedBan = banRepository.save(ban);
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
    public List<BanDto.Response> getBanList(long memberId) {
        Member blockingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        List<Ban> banList = banRepository.findByBlockingMember(blockingMember);
        return banMapper.banToBanResponseList(banList);
    }

    @Transactional
    public void cancelBan(long banId) {
        Ban ban = banRepository.findById(banId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.BAN_RECORD_NOT_FOUND));
        Member blockingMember = ban.getBlockingMember();
        Member bannedMember = ban.getBannedMember();
        if (blockingMember == null || bannedMember == null) {
            throw new BusinessLogicException(ExceptionCode.INVALID_MEMBER);
        }
        banRepository.delete(ban);
    }

}






