package com.boot.security.service.impl;

import com.boot.security.dao.NoticeDao;
import com.boot.security.model.Notice;
import com.boot.security.model.NoticeVo;
import com.boot.security.service.NoticeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    NoticeDao noticeDao;

    @Override
    public PageInfo getNoticeByPage(Integer pno, Integer psize, Notice notice) {
        PageHelper.startPage(pno,psize);
        List<Notice>lists=noticeDao.getNoticeByPage(notice);
        return new PageInfo(lists);
    }

    @Override
    public NoticeVo getNoticeVoByNoticeId(Integer noticeId) {

        return noticeDao.getNoticeVoByNoticeId(noticeId);
    }

    @Override
    public void save(Notice notice) {
        noticeDao.save(notice);
    }
}
