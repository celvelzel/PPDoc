package com.majy.ppocrdemo.controller;

import com.majy.ppocrdemo.utils.InvoiceOcrUtils;
import com.majy.ppocrdemo.utils.PaddleOcrUtils;
import com.majy.ppocrdemo.utils.PdfToImageUtils;
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
    public static Map invoiceOcr(MultipartFile file) throws IOException
    {

        List jsons = PdfToImage.pdf2OcrText(file);
        return InvoiceOcrUtils.getStringStringMap(jsons);
    }
}
