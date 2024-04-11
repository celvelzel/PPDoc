package com.majy.ppdoc.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService
{
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "files";

    public void uploadFile(MultipartFile file) throws IOException
    {
        String originalFilename = file.getOriginalFilename();

        //构造唯一的文件名  uuid（通用唯一识别码--长度固定的字符串 de49685b-6lc0-4b11-80fa-c7le95924018）
        int index = originalFilename.lastIndexOf(".");
        String extName = originalFilename.substring(index);//获取后缀文件名
        String newFileName= UUID.randomUUID().toString()+extName;
        log.info("获取到的文件名：{}",newFileName);

        //将文件存入指定的目录
        file.transferTo(new File(UPLOAD_DIR));
    }

    @Test
    public void test()
    {
        System.out.println("FileStorageService");
    }
}
