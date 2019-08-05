package com.boot.security.service;

import com.boot.security.model.Notice;
import com.boot.security.model.NoticeVo;
import com.github.pagehelper.PageInfo;

public interface NoticeService {
    PageInfo getNoticeByPage(Integer pno, Integer psize, Notice notice);

    NoticeVo getNoticeVoByNoticeId(Integer noticeId);

    void save(Notice notice);
}
