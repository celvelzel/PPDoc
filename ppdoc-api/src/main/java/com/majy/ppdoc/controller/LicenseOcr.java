package com.majy.ppdoc.controller;

import com.majy.ppdoc.utils.LicenseOcrUtils;
import com.majy.ppdoc.utils.PaddleOcrUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class LicenseOcr
{
    @PostMapping("/licenses")
    public static Map licenseOcr(MultipartFile file) throws IOException
    {

        List jsons = PaddleOcrUtils.pdfToOcrText(file);
        return LicenseOcrUtils.getStringStringMap(jsons);
    }
}
