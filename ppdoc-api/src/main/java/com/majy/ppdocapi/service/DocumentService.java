package com.majy.ppdocapi.service;

import com.majy.ppdocapi.entity.dto.Result;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.dto.PageBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface DocumentService
{
    PageBean page(Integer start, Integer pageSize);

    Result delete(Integer id);

    void add(Document document);

    Result handlePdfFile(MultipartFile file);

    Object getAllTypes();

    Result update(Document document);

    Result getById(Integer documentId);
}
