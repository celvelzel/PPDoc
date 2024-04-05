package com.majy.ppocrdemo.controller;

import com.majy.ppocrdemo.utils.PdfToImageUtils;
import com.majy.ppocrdemo.utils.PaddleOcrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
public class PdfToImage
{
    //网页中上传pdf的对应服务
    @PostMapping("/pdfs")
    public Map<String, String> pdf2image(MultipartFile file)
    {
        try
        {

            FileInputStream pdfInputStream = (FileInputStream) file.getInputStream();

            //创建准备进行ocr操作的图片list
            List<MultipartFile> imageFiles = new ArrayList<MultipartFile>();

            // 写入数据到临时文件
            // 写入数据到图片list
            PdfToImageUtils.pdf2ImageList(pdfInputStream, imageFiles, "png");
            log.info("pdf转image完成");

            pdfInputStream.close();

            //传入图片list，调用paddleOcr模块进行识别
            Map<String, String> userInfoMap = PaddleOcr.ocrTest(imageFiles);
            return userInfoMap;
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    //传入单个pdf,先转图片list，再进行ocr
    public static List pdf2OcrText(MultipartFile file)
    {
        try
        {
            InputStream pdfInputStream = file.getInputStream();

            //创建准备进行ocr操作的图片list
            List<MultipartFile> imageFiles = new ArrayList<MultipartFile>();

            // 写入数据到图片list
            PdfToImageUtils.pdf2ImageList(pdfInputStream, imageFiles, "png");
            log.info("pdf转image完成");


            //传入图片文件，调用ocr模块进行识别，返回json格式的识别结果
            List res = PaddleOcrUtils.getOcrText(imageFiles);
            log.info("image导出ocr内容完成");


            pdfInputStream.close();

            //返回OCR内容
            return res;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
