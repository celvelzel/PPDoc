package com.majy.ppdoc.controller;

import com.majy.ppdoc.utils.InvoiceOcrUtils;
import com.majy.ppdoc.utils.PaddleOcrUtils;
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

        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        return InvoiceOcrUtils.getStringStringMap(jsons);
    }
}
