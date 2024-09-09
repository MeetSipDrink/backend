package com.meetsipdrink.notice.service;

import com.meetsipdrink.board.entity.PostImage;
import com.meetsipdrink.exception.BusinessLogicException;
import com.meetsipdrink.exception.ExceptionCode;
import com.meetsipdrink.notice.entity.Notice;
import com.meetsipdrink.notice.entity.NoticeImage;
import com.meetsipdrink.notice.repository.NoticeImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class NoticeImageService {
    private final NoticeService noticeService;
    private final NoticeImageRepository noticeImageRepository;

    public NoticeImageService(NoticeService noticeService,
                              NoticeImageRepository noticeImageRepository) {
        this.noticeService = noticeService;
        this.noticeImageRepository = noticeImageRepository;
    }

    public NoticeImage createNoticeImage(NoticeImage noticeImage) throws IllegalArgumentException {
        Notice notice = noticeService.findVerifiedNotice(noticeImage.getNotice().getNoticeId());

        noticeImage.setNotice(notice);

        return noticeImageRepository.save(noticeImage);
    }

    public void deleteNoticeImage(long noticeImageId) {
        NoticeImage findNoticeImage = findVerifiedNoticeImage(noticeImageId);
        noticeImageRepository.delete(findNoticeImage);
    }

    public NoticeImage findVerifiedNoticeImage(long noticeImageId) {
        Optional<NoticeImage> optionalNoticeImage = noticeImageRepository.findById(noticeImageId);
        NoticeImage findNoticeImage = optionalNoticeImage.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.NOTICE_IMAGE_NOT_FOUND));
        return findNoticeImage;
    }
}
