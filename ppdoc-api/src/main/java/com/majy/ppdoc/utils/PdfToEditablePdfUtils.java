package com.majy.ppdoc.utils;

import com.itextpdf.io.util.FileUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import com.itextpdf.text.FontFactory;
import org.junit.Test;


@Slf4j
public class PdfToEditablePdfUtils
{
    public PdfToEditablePdfUtils()
    {

    }

    public static void pdf2EditablePdfUtil(InputStream pdfInputStream, OutputStream pdfOutputStream, List<List<Map>> ocrText) throws IOException
    {

    }

    public static void img2Pdf(String imgPath, String pdfPath)
    {
        try
        {
            BufferedImage img = ImageIO.read(new File(imgPath));
            FileOutputStream fos = new FileOutputStream(pdfPath);
            Document doc = new Document((Rectangle) null, 0.0F, 0.0F, 0.0F, 0.0F);
            doc.setPageSize(new Rectangle((float) img.getWidth(), (float) img.getHeight()));
            Image image = Image.getInstance(imgPath);
            float scalePercentage = 24.0F;
            image.scalePercent(scalePercentage, scalePercentage);
            PdfWriter.getInstance(doc, fos);
            doc.open();
            doc.add(image);
            doc.close();
        } catch (IOException var7)
        {
            var7.printStackTrace();
        } catch (DocumentException var8)
        {
            var8.printStackTrace();
        }

    }

    /**
     * 将图片转换为PDF文件，并保存到指定文件夹中。
     *
     * @param imgPath   图片文件的路径。
     * @param pdfFolder 保存PDF文件的文件夹路径。
     * @return 包含PDF页面宽度和高度的浮点数组。
     */
    public static float[] img2pdf2(String imgPath, String pdfFolder)
    {
        // 构造PDF文件的路径
        String pdfPath = pdfFolder + System.getProperty("file.separator") + FileUtil.getFileName(imgPath) + ".pdf";
        PDDocument doc = new PDDocument();

        float[] wAndH;
        try
        {
            // 创建PDF页面并添加到文档
            PDPage page = new PDPage();
            doc.addPage(page);
            // 从图片文件创建PDImageXObject对象
            PDImageXObject pdImage = PDImageXObject.createFromFile(imgPath, doc);
            // 创建内容流，用于向页面添加图像
            PDPageContentStream contents = new PDPageContentStream(doc, page);

            // 获取页面的边界矩形，用于确定图像尺寸
            PDRectangle pdr = page.getBBox();
            wAndH = new float[]{pdr.getWidth(), pdr.getHeight()};

            // 在页面上绘制图像
            contents.drawImage(pdImage, 0.0F, 0.0F, pdr.getWidth(), pdr.getHeight());
            // 关闭内容流
            contents.close();
            // 保存PDF文档
            doc.save(pdfPath);
        } catch (Exception var17)
        {
            // 处理异常，输出错误信息
            System.out.println("图片转pdf发生错误!!");
            var17.printStackTrace();
            // 发生异常时，返回A4纸的尺寸
            wAndH = new float[]{612.0F, 792.0F};
        } finally
        {
            // 确保文档被关闭
            try
            {
                doc.close();
            } catch (IOException var16)
            {
                // 处理关闭文档时可能发生的IO异常
                var16.printStackTrace();
            }

        }

        return wAndH;
    }

    /**
     * 将PDF文本转换为指定尺寸，并添加到另一个PDF中。
     *
     * @param pdfPath  原始PDF文件的路径。
     * @param pdfZise  指定的PDF尺寸，数组中第一个元素为宽度，第二个元素为高度。
     * @param textJO   包含转换后文本信息的JSONObject对象。
     * @param DpdfPath 目标PDF文件的路径，即转换后包含文本的PDF文件保存路径。
     */
    public static void pdf2Dpdf2(String pdfPath, float[] pdfZise, float[] imgSize, JSONObject textJO, String DpdfPath)
    {
        try
        {
            FontFactory.registerDirectory("<LOCAL_PATH_REDACTED>");
            //FontFactory.getFont("字体名称", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            // 创建字体对象，用于在PDF中显示文字
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            // 从文件系统中读取PDF文档
            InputStream input = new FileInputStream(new File(pdfPath));
            PdfReader reader = new PdfReader(input);
            // 输出流用于创建新的PDF文件
            OutputStream output = new FileOutputStream(new File(DpdfPath));
            PdfStamper stamper = new PdfStamper(reader, output);
            PdfContentByte page = stamper.getOverContent(1);

            // 开始在PDF页面上绘制文本
            page.beginText();
            page.setFontAndSize(baseFont, 8.0F); // 设置字体大小
            BaseColor coler = new BaseColor(255, 0, 0, 0); // 设置文字颜色
            page.setColorFill(coler);

            float iw = imgSize[0];//图片的宽度，单位为像素
            float ih = imgSize[1];//图片的高度，单位为像素
            float pw = pdfZise[0]; // 目标PDF的宽度
            float ph = pdfZise[1]; // 目标PDF的高度
            //PDF 页面的尺寸，单位是点（points）
            // 1 点等于 1/72 英寸
            // 这是 PDF 文档中默认的页面尺寸单位，也是 iTextPDF 中的标准单位。


            // 解析并处理传入的JSONObject，将文本添加到PDF中
            if (0 == textJO.getInt("status"))
            {
                JSONArray results = (JSONArray) textJO.get("results");
                // 遍历结果数组
                for (int i = 0; i < results.length(); i++)
                {
                    JSONArray resultArray = results.getJSONArray(i);
                    for (int j = 0; j < resultArray.length(); j++)
                    {
                        JSONObject item = resultArray.getJSONObject(j);
                        double confidence = item.getDouble("confidence");
                        String textContent = item.getString("text");
                        JSONArray textRegion = item.getJSONArray("text_region");

                        // 处理信息
                        System.out.println("Confidence: " + confidence + ", Text: " + textContent + ", Text Region: " + textRegion);
                        // 设置文字的位置
                        JSONArray point = textRegion.getJSONArray(0);
                        page.setTextMatrix((float) point.getInt(0) * pw / iw, ph - 14.0F - (float) point.getInt(1) * ph / ih);
                        // 对识别得分较低的字符添加方括号标记
                        if (0.9 > confidence)
                        {
                            textContent = "[" + textContent + "]";
                        }
                        page.showText(textContent); // 显示文字
                    }
                }
            }
            log.info("结束文本绘制");
            // 结束文本绘制
            page.endText();
            // 关闭相关资源
            stamper.close();
            reader.close();
            input.close();
            output.close();
        } catch (IOException var26)
        {
            var26.printStackTrace();
        } catch (DocumentException var27)
        {
            var27.printStackTrace();
        }

    }

    public static void requestPPOCR(String imgPath, String pdfFolder) throws IOException
    {
        float[] pdfZise = img2pdf2(imgPath, pdfFolder);
        //读取图片的宽和高
        BufferedImage image = ImageIO.read(new File(imgPath));
        int width = image.getWidth();
        int height = image.getHeight();
        float[] imgSize = new float[]{(float) width, (float) height};
        log.info("图片尺寸：["+imgSize[0]+","+imgSize[1]+"]");

        //请求ppocr，获取OCR结果
        JSONObject jsonObject = PaddleOcrUtils.requestOcr(imgPath);
        JSONObject rerJObject = jsonObject;
        System.out.println(rerJObject.toString());
        String pdfPath = pdfFolder + System.getProperty("file.separator") + FileUtil.getFileName(imgPath) + ".pdf";
        String DpdfPath = pdfFolder + System.getProperty("file.separator") + FileUtil.getFileName(imgPath) + "_d.pdf";
        pdf2Dpdf2(pdfPath, pdfZise, imgSize, rerJObject, DpdfPath);
    }

    @Test
    public void test() throws DocumentException, IOException
    {
        String jpgPath = "<LOCAL_PATH_REDACTED>";
        String dpdfFolder = "<LOCAL_PATH_REDACTED>";
        requestPPOCR(jpgPath, dpdfFolder);
    }
}

