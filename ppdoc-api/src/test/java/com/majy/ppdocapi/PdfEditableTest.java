package com.majy.ppdocapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Slf4j
public class PdfEditableTest
{
    public static void main(String[] args) throws IOException
    {
        try
        {
            String pdfPath = args.length > 0 ? args[0] : "path/to/example.pdf";
            InputStream inputStream = Files.newInputStream(new File(pdfPath).toPath());
            PDDocument document = PDDocument.load(inputStream);
            document.getClass();
            //使用PDFTextStripper 工具
            PDFTextStripper tStripper = new PDFTextStripper();
            //设置文本排序，有规则输出
            tStripper.setSortByPosition(true);
            log.info("before");
            //获取所有文字信息
            String info = tStripper.getText(document);
            log.info("after");
            log.info("start--{}--end",info);
            if (null == info){
                log.info("获取pdf文本信息失败,means pdf isnt editable");
            }
            inputStream.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
