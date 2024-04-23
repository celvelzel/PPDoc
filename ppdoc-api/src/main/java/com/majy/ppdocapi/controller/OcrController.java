package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.utils.OSSUtils;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.utils.PdfToEditablePdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class OcrController
{
    @Autowired
    OSSUtils ossUtils;
    @Autowired
    PdfToEditablePdfUtils pdfToEditablePdfUtils;

    public URL uploadFile(MultipartFile file) throws IOException
    {
        // 上传文件到OSS
        return (URL) ossUtils.uploadFile(file).getData();
    }


    /**
     * 构建并返回一个成功结果的响应体。
     *
     * @param urlResult 表示结果中的URL对象。
     * @param dataMap   包含结果数据的键值对映射。
     * @return 返回一个封装了结果数据的成功响应对象。
     */
    public Result getResuleSuccess(URL urlResult, Map<String, String> dataMap)
    {
        // 构建响应结果的映射
        Map<String, Object> responseMap = new HashMap();
        responseMap.put("url", urlResult); // 将URL对象放入响应映射中
        responseMap.put("data", dataMap); // 将数据映射放入响应映射中
        // 返回一个表示成功的Result对象，其中包含了构建的响应映射
        return Result.success(responseMap);
    }

}
