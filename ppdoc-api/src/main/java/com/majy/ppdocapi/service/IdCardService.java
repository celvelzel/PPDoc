package com.majy.ppdocapi.service;

import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.IdCard;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface IdCardService
{
    Result getById(Integer id);

    PageBean page(Integer start, Integer pageSize);

    Result delete(Integer documentId);

    Result add(IdCard idCard);

    Map<String, String> pdfFile2StringStringMap(MultipartFile file);

    Map<String, String> imageFile2StringStringMap(MultipartFile file) throws IOException;

    Result update(IdCard idCard);
}
