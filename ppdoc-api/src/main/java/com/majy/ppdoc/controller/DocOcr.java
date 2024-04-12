package com.majy.ppdoc.controller;

import com.majy.ppdoc.utils.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
public class DocOcr extends OcrController
{
    @PostMapping("/docs")
    public Result DocOcr(MultipartFile file) throws IOException
    {
        //调用父类方法，上传文件到OSS
        URL urlResult = uploadFile(file);

        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        Map<String, String> dataMap = DocOcrUtils.getStringStringMap(jsons);

        //调用父类方法，构建返回结果
        return getResuleSuccess(urlResult,dataMap);
    }
}
