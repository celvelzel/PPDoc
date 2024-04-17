package com.majy.ppdocapi.service;

import com.majy.ppdocapi.entity.dto.PageBean;
import com.majy.ppdocapi.entity.po.Document;
import com.majy.ppdocapi.entity.po.Invoice;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface InvoiceService
{
    PageBean page(Integer start, Integer pageSize);

    void delete(Integer documentId);

    void add(Invoice invoice);

    Map<String, String> file2StringStringMap(MultipartFile file);
}
