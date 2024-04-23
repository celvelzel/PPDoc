package com.majy.ppdocapi.utils.OCRUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PdfToImageUtils
{
    private PdfToImageUtils()
    {

    }

    public void pdf2image(MultipartFile file)
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
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
    * 接受pdf输入流
    * 输出multipartfile list类型的图片列表
    * */
    public static void pdf2ImageList(InputStream pdfInputStream, List<MultipartFile> files, String format) {
        try
        {
            // 加载PDF文档
            PDDocument document = PDDocument.load(pdfInputStream);

            // 创建PDF渲染器
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // 获取PDF页数
            int pageCount = document.getNumberOfPages();


            // 循环处理每一页
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                // 创建一个BufferedImage对象来代表每一页
                // 渲染当前页为BufferedImage
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 480); // DPI分辨率渲染

                // 对该页图片创建临时文件
                File tempFile = File.createTempFile("temp", ".tmp");

                FileOutputStream imageOutPutStream = new FileOutputStream(tempFile);

                // 将BufferedImage写入输出流
                ImageIO.write(image, format, imageOutPutStream);

                //File 转 MultipartFile
                MultipartFile tempMultipartFile;
                try {
                    FileInputStream fileInputStream = new FileInputStream(tempFile);
                    tempMultipartFile = new MockMultipartFile("copy"+tempFile.getName(),tempFile.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(),fileInputStream);
                    //System.out.println(tempMultipartFile.getName());
                    fileInputStream.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                //将一张或多张图片写入MultipartFile List
                files.add(pageIndex, tempMultipartFile);
            }

            // 关闭PDF文档
            document.close();
        } catch (IOException e)
        {

        }
    }
}
