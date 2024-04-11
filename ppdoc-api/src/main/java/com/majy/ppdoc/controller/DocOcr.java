package com.majy.ppdoc.controller;

import com.majy.ppdoc.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DocOcr
{
    private OSSUtils ossUtils;

    @Autowired
    public DocOcr(OSSUtils ossUtils) {
        this.ossUtils = ossUtils;
    }

    @PostMapping("/docs")
    public Result DocOcr(MultipartFile file) throws IOException
    {
        // 上传文件到OSS
        URL urlResult = (URL) ossUtils.uploadFile(file).getData();

        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        Map<String, String> dataMap = DocUtils.getStringStringMap(jsons);

        Map<String, Object> responseMap = new HashMap();
        responseMap.put("url", urlResult);
        responseMap.put("data", dataMap);
        return Result.success(responseMap);
    }
}
