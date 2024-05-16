package com.majy.ppdocapi.utils;

import com.itextpdf.io.util.FileUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.*;
import com.majy.ppdocapi.utils.OCRUtils.PaddleOcrUtils;
import com.majy.ppdocapi.utils.OCRUtils.PdfToImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class PdfToEditablePdfUtils
{
    @Value("${font.path}")
    private String fontPath;
    @Value("${file.pdf.path}")
    private String pdfFolder;

//    private String fontPath = "<LOCAL_PATH_REDACTED>";
//    private String pdfFolder = "<LOCAL_PATH_REDACTED>";

    public void pdf2Dpdf(File pdfFile, List ocrResult, String dPdfFilePath) throws IOException, DocumentException
    {
        // 加载PDF文档
        PDDocument document = PDDocument.load(pdfFile);

        // 获取PDF页数
        int pageCount = document.getNumberOfPages();
        log.info("PDF总页数: {}", pageCount);

        // 将PDF转换为图片
        List<File> imageList = PdfToImageUtils.pdf2ImageList(Files.newInputStream(pdfFile.toPath()));

        // 循环处理每一页
        for (int pageIndex = 0; pageIndex < pageCount; pageIndex++)
        {
            log.info("当前页数: {}", pageIndex + 1);

            //读取图片的宽和高
            BufferedImage image = ImageIO.read(imageList.get(pageIndex));
            float[] imgSize = getImageSize(image);
            log.info("第{}页的图片尺寸: {}", pageIndex + 1, imgSize);

            // 将图片转换为PDF
            float[] pdfSize = convertImageToPdf(document, imageList, pageIndex);
            log.info("第{}页的PDF尺寸: {}", pageIndex + 1, pdfSize);

            // 在 PDF 页面上绘制文本
            annotatePdf(imgSize, pdfSize, pageIndex, ocrResult);
        }
        // 合并并保存双层PDF文件
        mergePdf(pageCount, dPdfFilePath);
        log.info("PDF文件已保存");
        document.close();
    }

    public void image2Dpdf(File imageFile, List ocrResult, String dPdfFilePath) throws IOException, DocumentException
    {
        // 将图像转换为PDF文件，并获取PDF的尺寸
        String imgPath = imageFile.getAbsolutePath();
        float[] pdfSize = img2pdf2(imgPath, pdfFolder);
        log.info("PDF尺寸：[" + pdfSize[0] + "," + pdfSize[1] + "]");
        //读取图片的宽和高
        BufferedImage image = ImageIO.read(new File(imgPath));
        int width = image.getWidth();
        int height = image.getHeight();
        float[] imgSize = new float[]{(float) width, (float) height};
        log.info("图片尺寸：[" + imgSize[0] + "," + imgSize[1] + "]");

        // 根据输入图像路径和文件夹路径，生成PDF路径和双层PDF路径
        String pdfPath = pdfFolder + System.getProperty("file.separator") + FileUtil.getFileName(imgPath) + ".pdf";
        // 根据OCR结果绘制双层PDF文件
        pdf2Dpdf2(pdfPath, pdfSize, imgSize, new JSONArray(ocrResult), dPdfFilePath);
    }


    public float[] getImageSize(BufferedImage image)
    {
        int width = image.getWidth();
        int height = image.getHeight();
        return (width == 0 || height == 0) ? new float[]{612.0F, 792.0F} : new float[]{width, height};
    }

    /**
     * 将图片转换为PDF文件。
     *
     * @param document  用于存放转换结果的PDF文档对象
     * @param imageList 包含要转换为PDF的图片文件的列表
     * @param pageIndex 要转换的图片在列表中的索引
     * @return 转换后的PDF页面尺寸（宽度和高度）
     * @throws IOException 如果处理图片或保存PDF时发生IO错误
     */
    public float[] convertImageToPdf(PDDocument document, List<File> imageList, int pageIndex) throws IOException
    {
        PDDocument image2PdfDoc = new PDDocument();
        float[] pdfSize;
        // 将图片添加到PDF文档中
        try
        {
            // 创建一个新的PDF页面并将其添加到文档中
            PDPage page = new PDPage();
            image2PdfDoc.addPage(page);

            // 根据提供的图片列表中的索引，创建PDImageXObject对象
            PDImageXObject pdImage = PDImageXObject.createFromFileByContent(imageList.get(pageIndex), image2PdfDoc);

            // 创建一个内容流，用于向页面添加图像
            PDPageContentStream contents = new PDPageContentStream(image2PdfDoc, page);

            // 获取指定页面的边界矩形，以确定PDF页面的大小
            PDRectangle pdr = document.getPage(pageIndex).getBBox();
            pdfSize = new float[]{pdr.getWidth(), pdr.getHeight()};

            // 在页面上绘制图像
            contents.drawImage(pdImage, 0.0F, 0.0F, pdr.getWidth(), pdr.getHeight());

            // 关闭内容流以确保所有更改都已保存
            contents.close();

            // 保存转换后的PDF文件
            image2PdfDoc.save(pdfFolder + System.getProperty("file.separator") + "imagePDF" + (pageIndex + 1) + ".pdf");
        } catch (Exception var17)
        {
            // 处理任何在转换过程中发生的异常
            System.out.println("图片转pdf发生错误!!");
            var17.printStackTrace();

            // 发生异常时返回A4纸的尺寸
            pdfSize = new float[]{612.0F, 792.0F};
        } finally
        {
            // 确保在方法退出前关闭PDF文档以释放资源
            try
            {
                image2PdfDoc.close();
            } catch (IOException var16)
            {
                // 处理关闭文档时可能发生的IO异常
                var16.printStackTrace();
            }
        }
        return pdfSize;
    }

    public void annotatePdf(float[] imgSize, float[] pdfSize, int pageIndex, List ocrResult) throws DocumentException, IOException
    {
        FontFactory.registerDirectory(fontPath);
        //FontFactory.getFont("字体名称", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        // 创建字体对象，用于在PDF中显示文字
        BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);


        // 读取图片转成的PDF文档
        FileInputStream input = new FileInputStream(pdfFolder + System.getProperty("file.separator") + "imagePDF" + (pageIndex + 1) + ".pdf");
        PdfReader reader = new PdfReader(input);
        // 对该页图片创建双层pdf临时文件
        // 输出流用于创建新的PDF文件
        OutputStream output = new FileOutputStream(new File(pdfFolder + System.getProperty("file.separator") + "dpdf" + (pageIndex + 1) + ".pdf"));
        PdfStamper stamper = new PdfStamper(reader, output);
        // 获取pdf当前页，接收页数作为变量，1表示第一页
        PdfContentByte page = stamper.getOverContent(1);

        // 开始在PDF页面上绘制文本
        page.beginText();
        page.setFontAndSize(baseFont, 11.0F); // 设置字体大小
        BaseColor color = new BaseColor(255, 0, 0, 0); // 设置文字颜色
        page.setColorFill(color);

        float iw = imgSize[0];//图片的宽度，单位为像素
        float ih = imgSize[1];//图片的高度，单位为像素
        float pw = pdfSize[0]; // 目标PDF的宽度
        float ph = pdfSize[1]; // 目标PDF的高度
        //PDF 页面的尺寸，单位是点（points）
        // 1 点等于 1/72 英寸
        // 这是 PDF 文档中默认的页面尺寸单位，也是 iTextPDF 中的标准单位。

        //对对应页数的pdf文件进行绘制
        List resultArray = (List) ocrResult.get(pageIndex);
        // 对当前页面每个文字区块进行绘制
        for (int j = 0; j < resultArray.size(); j++)
        {
            Map textItem = (Map) resultArray.get(j);
            JSONObject item = new JSONObject(textItem);
            double confidence = item.getDouble("confidence");
            String textContent = item.getString("text");
            JSONArray textRegion = item.getJSONArray("text_region");

            // 处理信息
            //log.info("Confidence: " + confidence + ", Text: " + textContent + ", Text Region: " + textRegion);
            // 设置文字的位置
            JSONArray point = textRegion.getJSONArray(0);
            page.setTextMatrix((float) point.getInt(0) * (pw + 10.0F) / iw, ph - 8.0F - (float) point.getInt(1) * ph / ih);
            // 绘制文字
            page.showText(textContent);
        }
        log.info("结束文本绘制");


        // 结束文本绘制
        page.endText();
        // 关闭相关资源
        stamper.close();
        reader.close();
        input.close();
        output.close();
    }

    /**
     * 合并PDF文件方法
     * 将指定目录下的多个PDF文件合并成一个PDF文件。
     *
     * @param pageCount 需要合并的PDF文件数量。
     * @throws IOException 如果在读取或保存PDF文件时发生IO异常。
     */
    public void mergePdf(int pageCount, String finalPdfPath) throws IOException
    {
        // 创建一个新的PDF文档
        PDDocument Dpdf = new PDDocument();
        for (int i = 1; i <= pageCount; i++)
        {
            // 构建当前PDF文件的路径
            File file = new File(pdfFolder + System.getProperty("file.separator") + "dpdf" + i + ".pdf");
            if (file.exists()) // 检查文件是否存在
            {
                FileInputStream input = new FileInputStream(file);
                PDDocument tempPDF = PDDocument.load(file);
                // 将当前PDF页面添加到合并后的文档中
                Dpdf.addPage(tempPDF.getPage(0));
            }
        }
        try
        {
            // 保存合并后的PDF文件
            Dpdf.save(new File(finalPdfPath));
        } catch (IOException e)
        {
            log.error("保存合并后的双层PDF文件失败");
            throw new RuntimeException(e);
        } finally
        {
            try
            {
                // 确保在方法退出前关闭PDF文档
                Dpdf.close();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 请求PPOCR服务进行OCR识别，并将识别结果绘制在PDF文件中。
     *
     * @param imgPath   图像文件的路径。
     * @param pdfFolder 生成的PDF文件存储的文件夹路径。
     * @throws IOException 如果读取图像文件或处理PDF时发生错误。
     */
    public void requestPPOCR(String imgPath, String pdfFolder) throws IOException
    {
        // 将图像转换为PDF文件，并获取PDF的尺寸
        float[] pdfSize = img2pdf2(imgPath, pdfFolder);
        log.info("PDF尺寸：[" + pdfSize[0] + "," + pdfSize[1] + "]");
        //读取图片的宽和高
        BufferedImage image = ImageIO.read(new File(imgPath));
        int width = image.getWidth();
        int height = image.getHeight();
        float[] imgSize = new float[]{(float) width, (float) height};
        log.info("图片尺寸：[" + imgSize[0] + "," + imgSize[1] + "]");

        // 使用PPOCR服务进行OCR识别，获取OCR识别结果
        JSONObject rerJObject = PaddleOcrUtils.requestOcr(imgPath);
        JSONArray result = null;
        if (0 == rerJObject.getInt("status"))
        {
            result = rerJObject.getJSONArray("results");
        }
        // 根据输入图像路径和文件夹路径，生成PDF路径和双层PDF路径
        String pdfPath = pdfFolder + System.getProperty("file.separator") + FileUtil.getFileName(imgPath) + ".pdf";
        String DpdfPath = pdfFolder + System.getProperty("file.separator") + FileUtil.getFileName(imgPath) + "_d.pdf";
        // 根据OCR结果绘制双层PDF文件
        pdf2Dpdf2(pdfPath, pdfSize, imgSize, result, DpdfPath);
//        pdf2Dpdf6(pdfPath, pdfSize, rerJObject, DpdfPath);
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
     * @param pdfSize  指定的PDF尺寸，数组中第一个元素为宽度，第二个元素为高度。
     * @param textJO   包含转换后文本信息的JSONObject对象。
     * @param DpdfPath 目标PDF文件的路径，即转换后包含文本的PDF文件保存路径。
     */
    public void pdf2Dpdf2(String pdfPath, float[] pdfSize, float[] imgSize, JSONArray textJO, String DpdfPath)
    {
        try
        {
            FontFactory.registerDirectory(fontPath);
            //FontFactory.getFont("字体名称", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            // 创建字体对象，用于在PDF中显示文字
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            // 从文件系统中读取PDF文档
            InputStream input = new FileInputStream(new File(pdfPath));
            PdfReader reader = new PdfReader(input);
            // 输出流用于创建新的PDF文件
            OutputStream output = new FileOutputStream(new File(DpdfPath));
            PdfStamper stamper = new PdfStamper(reader, output);
            PdfContentByte page = stamper.getOverContent(1);//获取PDF第 2 层

            // 开始在PDF页面上绘制文本
            page.beginText();
            page.setFontAndSize(baseFont, 11.0F); // 设置字体大小
            BaseColor coler = new BaseColor(255, 0, 0, 0); // 设置文字颜色
            page.setColorFill(coler);

            float iw = imgSize[0];//图片的宽度，单位为像素
            float ih = imgSize[1];//图片的高度，单位为像素
            float pw = pdfSize[0]; // 目标PDF的宽度
            float ph = pdfSize[1]; // 目标PDF的高度
            //PDF 页面的尺寸，单位是点（points）
            // 1 点等于 1/72 英寸
            // 这是 PDF 文档中默认的页面尺寸单位，也是 iTextPDF 中的标准单位。


            // 解析并处理传入的JSONObject，将文本添加到PDF中
            JSONArray results = textJO;
            // 遍历OCR结果数组
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
//                        System.out.println("Confidence: " + confidence + ", Text: " + textContent + ", Text Region: " + textRegion);
                    // 设置文字的位置
                    JSONArray point = textRegion.getJSONArray(0);
                    page.setTextMatrix((float) point.getInt(0) * (pw + 10.0F) / iw, ph - 8.0F - (float) point.getInt(1) * ph / ih);
                    // 对识别得分较低的字符添加方括号标记
//                        if (0.9 > confidence)
//                        {
//                            textContent = "[" + textContent + "]";
//                        }
                    page.showText(textContent); // 显示文字
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

    /**
     * 判断PDF文件是否可以直接复制文本
     *
     * @return true 表示可以复制文本，false 表示无法复制文本
     */
    public static boolean pdfCopyableChecker(InputStream inputStream) throws IOException
    {
        PDDocument document = PDDocument.load(inputStream);
        if (!document.isEncrypted())
        { // 检查PDF是否被加密
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            if (!text.trim().isEmpty())
            {
                log.info("文档可以直接复制文本。");
                document.close();
                return true;
            } else
            {
                log.info("文档中没有可见的文本，无法直接复制。");
                document.close();
                return false;
            }
        } else
        {
            System.out.println("文档已加密，无法判断文本是否可直接复制。");
            document.close();
            return false;
        }
    }


    /**
     * 从PDF文件中提取文本。
     *
     * @param inputStream 输入流，代表待处理的PDF文件。
     * @return 返回从PDF中提取出的文本，去掉首尾空白字符。
     * @throws RuntimeException 如果IO异常发生，例如PDF文件无法加载。
     */
    public static String getPdfText(InputStream inputStream)
    {
        try
        {
            // 加载PDF文档
            PDDocument document = PDDocument.load(inputStream);
            // 创建PDF文本提取器
            PDFTextStripper stripper = new PDFTextStripper();
            // 提取文档中的文本
            String text = stripper.getText(document);
            // 返回文本
            return text.trim();
        } catch (IOException e)
        {
            // 将IO异常转换为运行时异常抛出
            throw new RuntimeException(e);
        }
    }

    /**
     * 将图片转换为PDF文件。
     *
     * @param imgPath 图片文件的路径。
     * @param pdfPath 生成的PDF文件的路径。
     *                说明：此方法将指定路径的图片转换为指定路径的PDF文件，图片将按原尺寸添加到PDF中。
     */
    public static void img2Pdf(String imgPath, String pdfPath)
    {
        try
        {
            // 从图片路径读取图片
            BufferedImage img = ImageIO.read(new File(imgPath));
            // 创建PDF输出流
            FileOutputStream fos = new FileOutputStream(pdfPath);
            // 创建空的PDF文档，不设置边距
            Document doc = new Document((Rectangle) null, 0.0F, 0.0F, 0.0F, 0.0F);
            // 设置PDF文档的页面大小为图片大小
            doc.setPageSize(new Rectangle((float) img.getWidth(), (float) img.getHeight()));
            // 加载图片
            Image image = Image.getInstance(imgPath);
            // 缩放图片
            float scalePercentage = 24.0F;
            image.scalePercent(scalePercentage, scalePercentage);
            // 创建PDF写入实例
            PdfWriter.getInstance(doc, fos);
            // 打开PDF文档
            doc.open();
            // 将图片添加到PDF文档中
            doc.add(image);
            // 关闭PDF文档
            doc.close();
        } catch (IOException var7)
        {
            // 处理IO异常
            var7.printStackTrace();
        } catch (DocumentException var8)
        {
            // 处理文档异常
            var8.printStackTrace();
        }

    }

    public void pdf2Dpdf6(String pdfPath, float[] pdfZise, JSONObject textJO, String DpdfPath)
    {
        try
        {
            FontFactory.registerDirectory(fontPath);
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
            page.setFontAndSize(baseFont, 11.0F);
            BaseColor coler = new BaseColor(255, 0, 0, 0);
            page.setColorFill(coler);


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
                        JSONArray area1 = textRegion.getJSONArray(3);
                        JSONArray area2 = textRegion.getJSONArray(3);
                        float iw = (float) area1.getInt(0);
                        float ih = (float) area2.getInt(1);
                        float pw = pdfZise[0];
                        float ph = pdfZise[1];

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

            page.endText();
            stamper.close();
            reader.close();
        } catch (Exception var24)
        {
            System.out.println("双层pdf合成失败！");
            var24.printStackTrace();
        }

    }

    @Test
    public void test() throws DocumentException, IOException
    {
//        String jpgPath = "<LOCAL_PATH_REDACTED>";
//        String dpdfFolder = "<LOCAL_PATH_REDACTED>";
//        requestPPOCR(jpgPath, dpdfFolder);

        String pdfFolder = "<LOCAL_PATH_REDACTED>";
        File pdfFile = new File(pdfFolder);
        InputStream input = new FileInputStream(pdfFile);

        List jsons = PaddleOcrUtils.pdfToOcrText(input);

        pdf2Dpdf(pdfFile, jsons, "<LOCAL_PATH_REDACTED>");

//        System.out.println("OCR识别结果" + getPdfText(input));
//        System.out.println("pdf可编辑检验结果：" + pdfCopyableChecker(input));
    }
}

