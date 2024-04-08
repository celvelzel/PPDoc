package com.majy.ppocrdemo.controller;

import com.majy.ppocrdemo.utils.IdCardOcrUtils;
import com.majy.ppocrdemo.utils.PaddleOcrUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class IdCardOcr
{
    @PostMapping("/idcards/pdf")
    public static Map<String, String> idCardPdfOcr(MultipartFile file) throws IOException
    {
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        return IdCardOcrUtils.getStringStringMap(jsons);
    }

    @PostMapping("/idcards/image")
    public static Map<String, String> idCardImageOcr(MultipartFile file) throws IOException
    {
        //String originalFilename = file.getOriginalFilename(); // 获取文件原名
        //file.transferTo(new File("<LOCAL_PATH_REDACTED>" + originalFilename)); // 保存文件到指定目录

        //使用PaddleOcrUtils工具对上传的文件进行OCR处理，并获取识别结果。
        List<MultipartFile> files = new java.util.ArrayList<>();
        files.add(file);
        List<List> jsons = PaddleOcrUtils.getOcrText(files);

        // 对识别结果进行信息提取，转换为Map类型，并返回给前端。
        return IdCardOcrUtils.getStringStringMap(jsons);
    }
}
