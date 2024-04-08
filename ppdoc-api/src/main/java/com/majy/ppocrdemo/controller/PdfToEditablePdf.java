package com.majy.ppocrdemo.controller;

import com.majy.ppocrdemo.utils.PaddleOcrUtils;
import com.majy.ppocrdemo.utils.PdfToEditablePdfUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class PdfToEditablePdf
{
    public static MultipartFile pdfToEditablePdf(MultipartFile file)
    {
        try
        {
            // 创建临时文件
            File tempFile = File.createTempFile("temp", ".tmp");


            InputStream pdfInputStream = file.getInputStream();
            OutputStream editablePdfOutputStream = new FileOutputStream(tempFile);

            //传入pdf文件，获取ocr结果json文件
            List ocr_context = PaddleOcrUtils.pdfToOcrText(file);


            PdfToEditablePdfUtils.pdf2EditablePdfUtil(pdfInputStream, editablePdfOutputStream, ocr_context);
            //log.info("pdf转双层pdf完成");

            // 创建MockMultipartFile对象
            MockMultipartFile multipartFile = new MockMultipartFile("file", tempFile.getName(), null, new FileInputStream(tempFile));

            // 删除临时文件
            tempFile.delete();

            editablePdfOutputStream.close();
            pdfInputStream.close();

            //返回双层pdf文件
            return multipartFile;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    //测试类
    public static void main(String[] args)
    {
        String filePath = "<LOCAL_PATH_REDACTED>";
        File file = new File(filePath);

        try
        {
            // 使用FileInputStream读取文件
            FileInputStream input = new FileInputStream(file);
            // 创建MultipartFile对象
            MultipartFile multipartFile = new MockMultipartFile("file",
                    file.getName(), "application/pdf", input);

            MultipartFile result = pdfToEditablePdf(multipartFile);
            // 指定保存文件的目录和文件名
            String outputFilePath = "output_editable.pdf";

            // 将MultipartFile对象转换为InputStream
            InputStream inputStream = result.getInputStream();

            if (Files.exists(Paths.get("output_editable.pdf")))
            {
                try {
                    Files.delete(Paths.get("output_editable.pdf"));
                    log.info("文件 " + outputFilePath + "已被删除");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 使用Files.copy()方法将文件保存到本地目录
            Files.copy(inputStream, Paths.get(outputFilePath));
            log.info("文件 " + outputFilePath + "已生成");

            // 关闭InputStream
            inputStream.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
