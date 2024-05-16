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

    /**
     * 将PDF文件流转换为图像文件列表。
     *
     * @param pdfInputStream 表示PDF文档的输入流。
     * @return 包含转换后的图像文件的列表。每个PDF页面将转换为一个图像文件。
     */
    public static List<File> pdf2ImageList(InputStream pdfInputStream) {
        // 初始化存储转换后图像文件的列表
        List<File> files = new ArrayList<>();
        try
        {
            // 加载传入的PDF输入流到PDDocument对象
            PDDocument document = PDDocument.load(pdfInputStream);

            // 创建用于渲染PDF页面的PDFRenderer对象
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // 获取PDF文档的总页数
            int pageCount = document.getNumberOfPages();

            // 遍历每一页PDF，将其渲染为图像
            for (int pageIndex = 0; pageIndex < pageCount; pageIndex++) {
                // 渲染指定页码为480DPI的BufferedImage
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, 480);

                // 为当前页面的图像创建一个临时文件
                File tempFile = File.createTempFile("temp", ".tmp");

                // 将BufferedImage保存到临时文件
                FileOutputStream imageOutPutStream = new FileOutputStream(tempFile);
                ImageIO.write(image, "png", imageOutPutStream);

                // 将该图像文件添加到结果列表中
                files.add(pageIndex, tempFile);
            }

            // 关闭加载的PDF文档
            document.close();
        } catch (IOException e)
        {
            // 记录转换失败的错误日志
            log.error("pdf转image失败");
        }
        return files;
    }

}
