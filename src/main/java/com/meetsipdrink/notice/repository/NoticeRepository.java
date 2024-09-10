package com.meetsipdrink.notice.repository;

import com.meetsipdrink.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
