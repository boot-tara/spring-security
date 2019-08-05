package com.boot.security.service;

import com.boot.security.model.FileInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileInfoService {
    PageInfo<FileInfo> getFileInfosByPage(Integer pno, Integer psize);

    FileInfo save(MultipartFile file) throws IOException;

    void delete(String id);
}
