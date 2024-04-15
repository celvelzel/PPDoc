package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.utils.InvoiceOcrUtils;
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
@RequestMapping("/invoices")
public class InvoiceController extends OcrController
{

    @PostMapping("/upload")
    public Result invoiceOcr(MultipartFile file) throws IOException
    {
        // 调用父类方法，上传文件到OSS
        URL urlResult = uploadFile(file);

        // pdf识别，提取文字
        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        // 根据识别文本提取信息
        Map<String, String> dataMap = InvoiceOcrUtils.getStringStringMap(jsons);

        //调用父类方法，构建返回结果
        return getResuleSuccess(urlResult,dataMap);
    }
}
