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
public class PaddleOcr
{
    /**
     * 通过POST请求上传图片文件列表进行OCR
     *
     * @param files 用户上传的文件列表，类型为MultipartFile的列表
     * @return 返回一个Map，其中包含有关上传文件的OCR识别信息
     */
    @PostMapping("/images")
    public static String paddleOcr(List<MultipartFile> files) throws IOException
    {
        // 遍历文件列表，将每个文件保存到本地。
        for (MultipartFile file : files)
        {
            String originalFilename = file.getOriginalFilename(); // 获取文件原名
            file.transferTo(new File("<LOCAL_PATH_REDACTED>" + originalFilename)); // 保存文件到指定目录
        }

        List<List> jsons = PaddleOcrUtils.getOcrText(files);

        // 需要对识别结果进行拼接，转字符串
        return(PaddleOcrUtils.jsonToString(jsons));
    }
}


