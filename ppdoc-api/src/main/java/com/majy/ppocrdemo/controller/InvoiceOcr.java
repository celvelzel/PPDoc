package com.majy.ppocrdemo.controller;

import com.majy.ppocrdemo.utils.PaddleOcrUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InvoiceOcr
{
    @PostMapping("/invoices")
    public static Map<String, String> ocrTest(List<MultipartFile> files) throws IOException
    {
        Map<String, String> userInfoMap = PaddleOcrUtils.getOcrText(files);
        return userInfoMap;
    }
}
