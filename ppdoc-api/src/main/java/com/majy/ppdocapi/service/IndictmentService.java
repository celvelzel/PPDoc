package com.majy.ppdocapi.service;


import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.po.Indictment;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IndictmentService
{
    PageBean page(Integer start, Integer pageSize);

    void delete(Integer documentId);

    void add(Indictment indictment);

    Map<String, String> file2StringStringMap(MultipartFile file);

    Indictment getByIndictmentId(Integer indictmentId);
}
