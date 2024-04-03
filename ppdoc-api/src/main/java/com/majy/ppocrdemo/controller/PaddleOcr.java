package com.majy.ppocrdemo.controller;

import com.majy.ppocrdemo.utils.PaddleOcrUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class PaddleOcr
{
    @PostMapping("/images")
    public static Map<String, String> ocrTest(List<MultipartFile> files) throws IOException
    {
        Map<String, String> userInfoMap = PaddleOcrUtils.getStringStringMap(files);
        return userInfoMap;
    }
}

