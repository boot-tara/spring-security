package com.boot.security.service.impl;

import com.boot.security.dao.FileInfoDao;
import com.boot.security.model.FileInfo;
import com.boot.security.service.FileInfoService;
import com.boot.security.util.FileUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Value("${files.path}")
    private String filesPath;
    @Autowired
    FileInfoDao fileInfoDao;

    @Override
    public PageInfo<FileInfo> getFileInfosByPage(Integer pno, Integer psize) {
        PageHelper.startPage(pno,psize);
        List<FileInfo>lists=fileInfoDao.getFileInfosByPage(pno,psize);
        return new PageInfo<>(lists);
    }

    @Override
    public FileInfo save(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if(!originalFilename.contains(".")){
            throw new IllegalArgumentException("缺少后缀名");
        }
        //判断是否长传过
        String md5 = FileUtil.fileMd5(file.getInputStream());
        //从数据库中查找
        FileInfo file1=fileInfoDao.getFile(md5);
        if(file1!=null){
            //修改上传时间
            fileInfoDao.updateTime(md5);
            return file1;
        }
        //获得地址
        String fileOrigName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String pathname=FileUtil.getPath()+md5+fileOrigName;
        String fullPath = filesPath + pathname;//全路径 d:files   /...
        //上传到D盘
        FileUtil.saveFile(file,fullPath);
        //添加到数据库

        long size = file.getSize();
        String contentType = file.getContentType();

        file1 = new FileInfo();
        file1.setId(md5);
        file1.setContentType(contentType);
        file1.setSize(size);
        file1.setPath(fullPath);
        file1.setUrl(pathname);
        file1.setType(contentType.startsWith("image/") ? 1 : 0);  //图片1  文件0

        fileInfoDao.save(file1);
        return file1;
    }

    @Override
    public void delete(String id) {
        FileInfo fileInfo = fileInfoDao.getFile(id);//数据库中存储的位置
        if (fileInfo != null) {
            String fullPath = fileInfo.getPath();
            FileUtil.deleteFile(fullPath);

            fileInfoDao.delete(id);
        }
    }
}
