package com.meetsipdrink.notice.service;

import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.member.entity.Member;
import com.meetsipdrink.member.service.MemberService;
import com.meetsipdrink.notice.entity.Notice;
import com.meetsipdrink.notice.repository.NoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
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

    public Notice createNotice(Notice notice, String email) throws IllegalArgumentException {
        Member member = memberService.findMemberByEmail(email);
        notice.setMember(member);

        if (!member.getRoles().toString().contains("ADMIN")) {
            throw new BusinessLogicException(ExceptionCode.NOTICE_UNAUTHORIZED_ACTION);
        }

        if (notice.getImageUrls() != null && notice.getImageUrls().isEmpty()) {
            notice.setImageUrls(notice.getImageUrls());  // 리스트를 통째로 추가
        }

        return noticeRepository.save(notice);
    }

    public Notice updateNotice(long noticeId, Notice notice, String email) {
        Notice findNotice = findVerifiedNotice(noticeId);

        if (!email.equals("admin@gmail.com")) {
            throw new BusinessLogicException(ExceptionCode.NOTICE_UNAUTHORIZED_ACTION); // 관리자 검증 실패
        }

        Optional.ofNullable(notice.getTitle())
                .ifPresent(title -> findNotice.setTitle(title));
        Optional.ofNullable(notice.getContent())
                .ifPresent(content -> findNotice.setContent(content));


        findNotice.setImageUrls(notice.getImageUrls());


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

    public void deleteNotice(long noticeId, String email) {
        Notice findNotice = findVerifiedNotice(noticeId);

        if (!email.equals("admin@gmail.com")) {
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
