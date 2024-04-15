package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.utils.OSSUtils;
import com.majy.ppdocapi.utils.Result;
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

    public URL uploadFile(MultipartFile file) throws IOException
    {
        // 上传文件到OSS
        return (URL) ossUtils.uploadFile(file).getData();
    }


    public Result getResuleSuccess(URL urlResult, Map<String, String> dataMap){
        //构建返回结果
        Map<String, Object> responseMap = new HashMap();
        responseMap.put("url", urlResult);
        responseMap.put("data", dataMap);
        return Result.success(responseMap);
    }
}
