package com.majy.ppdocapi.service;


import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Indictment;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IndictmentService
{
    PageBean page(Integer start, Integer pageSize);

    Result delete(Integer documentId);

    Result add(Indictment indictment);

    Map<String, String> file2StringStringMap(MultipartFile file);

    Result handlePdfFile(MultipartFile file);

    Result getByIndictmentId(Integer indictmentId);

    Result update(Indictment indictment);
}
