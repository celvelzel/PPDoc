package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.utils.IdCardOcrUtils;
import com.majy.ppdocapi.utils.PaddleOcrUtils;
import com.majy.ppdocapi.pojo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/idcards")
public class IdCardController extends OcrController
{

    @PostMapping("/pdf/upload")
    public Result idCardPdfOcr(MultipartFile file) throws IOException
    {
        // 调用父类方法，上传文件到OSS
        URL urlResult = uploadFile(file);

        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        Map<String, String> dataMap = IdCardOcrUtils.getStringStringMap(jsons);

        //调用父类方法，构建返回结果
        return getResuleSuccess(urlResult,dataMap);
    }

    @PostMapping("/image/upload")
    public Result idCardImageOcr(MultipartFile file) throws IOException
    {
        //调用父类方法，上传文件到OSS
        URL urlResult = uploadFile(file);

        //使用PaddleOcrUtils工具对上传的文件进行OCR处理，并获取识别结果。
        List<MultipartFile> files = new java.util.ArrayList<>();
        files.add(file);
        List<List> jsons = PaddleOcrUtils.getOcrText(files);

        // 对识别结果进行信息提取，转换为Map类型，并返回给前端。
        Map<String, String> dataMap = IdCardOcrUtils.getStringStringMap(jsons);

        //调用父类方法，构建返回结果
        return getResuleSuccess(urlResult,dataMap);
    }
}
