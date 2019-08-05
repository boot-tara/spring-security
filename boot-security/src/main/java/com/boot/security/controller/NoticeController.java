package com.boot.security.controller;

import com.boot.security.dao.NoticeDao;
import com.boot.security.model.Notice;
import com.boot.security.model.NoticeStatus;
import com.boot.security.model.NoticeVo;
import com.boot.security.model.Status;
import com.boot.security.service.NoticeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    @Autowired
    NoticeDao noticeDao;

    @PreAuthorize("hasAuthority('notice:query')")
    @GetMapping("/{pno}/{psize}")
    public PageInfo<Notice> getNoticeByPage(@PathVariable Integer pno, @PathVariable Integer psize, Notice notice ){
            return noticeService.getNoticeByPage(pno,psize,notice);
    }

    @GetMapping("/{noticeId}")
    @PreAuthorize("hasAuthority('notice:query')")
    public NoticeVo getNoticeVoByNoticeId(@PathVariable Integer noticeId){
        return noticeService.getNoticeVoByNoticeId(noticeId);
    }

    //富文本编辑器存入数据库
    @PostMapping
    @PreAuthorize("hasAuthority('notice:add')")
    public Notice saveNotice( Notice notice) {
        noticeService.save(notice);

        return notice;
    }


    //修改
    @PutMapping
    @PreAuthorize("hasAuthority('notice:add')")
    public Notice updateNotice(@RequestBody Notice notice) {
        Notice no = noticeDao.getById(notice.getId());
        if (no.getStatus() == NoticeStatus.PUBLISH) {
            throw new IllegalArgumentException("发布状态的不能修改");
        }
        noticeDao.update(notice);

        return notice;
    }
}
