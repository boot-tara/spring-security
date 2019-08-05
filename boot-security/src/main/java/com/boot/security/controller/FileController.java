package com.boot.security.controller;

import com.boot.security.model.FileInfo;
import com.boot.security.model.LayuiFile;
import com.boot.security.model.LayuiFileData;
import com.boot.security.service.FileInfoService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileInfoService fileInfoService;

    //分页
    @GetMapping("/{pno}/{psize}")
    @PreAuthorize("hasAuthority('sys:file:query')")
    public PageInfo<FileInfo> fileInfos(@PathVariable Integer pno, @PathVariable Integer psize){
        return fileInfoService.getFileInfosByPage(pno,psize);
    }

    //上传文件
    @PostMapping
    public FileInfo uploadFile(MultipartFile file) throws IOException {
        return fileInfoService.save(file);
    }

    //删除文件
    @PreAuthorize("hasAuthority('sys:file:del')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        fileInfoService.delete(id);
    }


    //上传图片之富文本剪辑器
    @PostMapping("/layui")
    public LayuiFile uploadLayuiFile(MultipartFile file,String domain) throws IOException{
        FileInfo fileInfo = fileInfoService.save(file);
        //图片上传完返回要求的对象
        LayuiFile layuiFile=new LayuiFile();
        LayuiFileData data=new LayuiFileData(domain+"/statics"+fileInfo.getUrl(),file.getOriginalFilename());
        layuiFile.setCode(0);
        layuiFile.setData(data);
        return layuiFile;
    }
}
