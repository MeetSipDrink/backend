package com.meetsipdrink.notice.service;

import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.service.MemberService;
import com.meetsipdrink.notice.entity.Notice;
import com.meetsipdrink.notice.repository.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberService memberService;

    public NoticeService(NoticeRepository noticeRepository,
                         MemberService memberService) {
        this.noticeRepository = noticeRepository;
        this.memberService = memberService;
    }

    public Notice createNotice(Notice notice) throws IllegalArgumentException {
        Member member = memberService.findVerifiedMember(notice.getMember().getMemberId());
        notice.setMember(member);

        if (member.getMemberId() != 1) {
            throw new BusinessLogicException(ExceptionCode.NOTICE_UNAUTHORIZED_ACTION); // 관리자 검증 실패
        }

        return noticeRepository.save(notice);
    }

    public Notice updateNotice(long noticeId, Notice notice) {
        Notice findNotice = findVerifiedNotice(noticeId);

        if (findNotice.getMember().getMemberId() != 1) {
            throw new BusinessLogicException(ExceptionCode.NOTICE_UNAUTHORIZED_ACTION); // 관리자 검증 실패
        }

        Optional.ofNullable(notice.getTitle())
                .ifPresent(title -> findNotice.setTitle(title));
        Optional.ofNullable(notice.getContent())
                .ifPresent(content -> findNotice.setContent(content));

        return noticeRepository.save(findNotice);
    }

    public synchronized Notice findNotice(long noticeId) {
        Notice findNotice = findVerifiedNotice(noticeId);
        findNotice.setViews(findNotice.getViews() + 1);
        return findNotice;
    }

    @Transactional(readOnly = true)
    public Page<Notice> findNoticesSort(int page, int size, Sort sort) {
        Pageable pageable = PageRequest.of(page, size, sort);
        return noticeRepository.findAll(pageable);
    }

    public void deleteNotice(long noticeId) {
        Notice findNotice = findVerifiedNotice(noticeId);

        if (findNotice.getMember().getMemberId() != 1) {
            throw new BusinessLogicException(ExceptionCode.NOTICE_UNAUTHORIZED_ACTION); // 관리자 검증 실패
        }

        noticeRepository.delete(findNotice);
    }

    public Notice findVerifiedNotice(long noticeId) {
        Optional<Notice> optionalNotice = noticeRepository.findById(noticeId);
        Notice findNotice = optionalNotice.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.NOTICE_NOT_FOUND));
        return findNotice;
    }

    public Notice findNoticeById(long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found for id : " + noticeId));
    }
}
