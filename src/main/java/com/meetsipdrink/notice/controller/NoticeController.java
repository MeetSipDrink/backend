package com.meetsipdrink.notice.controller;

import com.meetsipdrink.dto.MultiResponseDto;
import com.meetsipdrink.dto.SingleResponseDto;
import com.meetsipdrink.notice.dto.NoticeDto;
import com.meetsipdrink.notice.entity.Notice;
import com.meetsipdrink.notice.mapper.NoticeMapper;
import com.meetsipdrink.notice.service.NoticeService;
import com.meetsipdrink.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/notices")
public class NoticeController {
    private final static String NOTICE_DEFAULT_URL = "/notices";
    private final NoticeService noticeService;
    private final NoticeMapper mapper;

    public NoticeController(NoticeService noticeService,
                            NoticeMapper mapper) {
        this.noticeService = noticeService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postNotice(@Valid @RequestBody NoticeDto.Post requestBody,
                                     @AuthenticationPrincipal Object principal) {
        Notice notice = mapper.noticePostDtoToNotice(requestBody);
        Notice createNotice = noticeService.createNotice(notice, principal.toString());
        URI location = UriCreator.createUri(NOTICE_DEFAULT_URL, createNotice.getNoticeId());
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{notice-id}")
    public ResponseEntity patchNotice(@PathVariable("notice-id") @Positive long noticeId,
                                      @Valid @RequestBody NoticeDto.Patch requestBody,
                                      @AuthenticationPrincipal Object principal) {
        Notice notice = noticeService.updateNotice(noticeId, mapper.noticePatchDtoToNotice(requestBody), principal.toString());
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.noticeToNoticeResponseDto(notice)), HttpStatus.OK);
    }

    @GetMapping("/{notice-id}")
    public ResponseEntity getNotice(@PathVariable("notice-id") @Positive long noticeId) {
        Notice notice = noticeService.findNotice(noticeId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.noticeToNoticeResponseDto(notice)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getNotices(@Positive @RequestParam int page, @Positive @RequestParam int size,
                                     @RequestParam String sort) {
        Sort sortOrder = Sort.by(sort.split("_")[0]).ascending();
        if(sort.split("_")[1].equalsIgnoreCase("desc")) {
            sortOrder = sortOrder.descending();
        }

        Page<Notice> pageNotice = noticeService.findNoticesSort(page - 1, size, sortOrder);
        List<Notice> notices = pageNotice.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.noticesToNoticeResponseDtos(notices), pageNotice), HttpStatus.OK);
    }

    @DeleteMapping("/{notice-id}")
    public ResponseEntity deleteNotice(@PathVariable("notice-id") @Positive long noticeId,
                                       @AuthenticationPrincipal Object principal) {
        noticeService.deleteNotice(noticeId, principal.toString());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
