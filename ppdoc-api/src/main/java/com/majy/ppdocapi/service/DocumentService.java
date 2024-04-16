package com.majy.ppdocapi.service;

import com.majy.ppdocapi.pojo.Document;
import com.majy.ppdocapi.pojo.PageBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface DocumentService
{
    PageBean page(Integer start, Integer pageSize);

    void delete(Integer id);

    void add(Document document);

    Map<String, String> file2StringStringMap(MultipartFile file);
}
