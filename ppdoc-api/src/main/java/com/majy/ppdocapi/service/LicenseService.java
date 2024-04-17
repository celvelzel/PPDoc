package com.majy.ppdocapi.service;

import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.License;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface LicenseService
{
    PageBean page(Integer start, Integer pageSize);

    void delete(Integer documentiId);

    void add(License license);

    Map<String, String> file2StringStringMap(MultipartFile file);
}
