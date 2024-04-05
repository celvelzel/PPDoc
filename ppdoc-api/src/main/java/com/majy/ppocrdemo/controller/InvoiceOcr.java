package com.majy.ppocrdemo.controller;

import com.majy.ppocrdemo.utils.InvoiceOcrUtils;
import com.majy.ppocrdemo.utils.PaddleOcrUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class InvoiceOcr
{
    @PostMapping("/invoices")
    public static Map invoiceOcr(List<MultipartFile> files) throws IOException
    {
        List jsons = PaddleOcrUtils.getOcrText(files);
        return InvoiceOcrUtils.getStringStringMap(jsons);
    }
}
