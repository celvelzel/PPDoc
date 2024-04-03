package com.majy.ppocrdemo.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.fontbox.ttf.TrueTypeCollection;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.json.JSONArray;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PdfToEditablePdfUtils
{
    public PdfToEditablePdfUtils()
    {

    }

    public static void pdf2EditablePdfUtil(InputStream pdfInputStream, OutputStream pdfOutputStream, List<List<Map>> ocrText) throws IOException
    {
        // 创建文档对象
        // 加载单层PDF文档
        PDDocument document = PDDocument.load(pdfInputStream);
        // 创建一个新的PDF文档用于保存双层PDF
        PDDocument newDocument = new PDDocument();

        // 遍历原始PDF文档的每一页
        for (int i = 0; i < document.getNumberOfPages(); i++)
        {
            //单位是pt?
            float width = document.getPage(i).getMediaBox().getWidth();
            float height = document.getPage(i).getMediaBox().getHeight();
            //System.out.println("Pdf的宽度为"+width+"\nPdf的高度为"+height);
            // 创建新页面, 添加空白页面
            PDRectangle customSize = new PDRectangle(width, height);
            PDPage newPage = new PDPage(customSize);
            newDocument.addPage(newPage);

//            for (Map map:ocrText.get(i))
//            {
//                String text = (String) map.get("text");
//                ArrayList textRegion = (ArrayList) map.get("text_region");
//                //System.out.println("Text: " + text + ", Text Region: " + textRegion.toString());
//                ArrayList<Integer> textRegionGroupInteger = (ArrayList<Integer>) textRegion.get(0);
//                ArrayList<Float> textRegionGroup = new ArrayList<>();
//                for (int value : textRegionGroupInteger) {
//                    textRegionGroup.add((float) value);
//                }
//                textRegion.add(textRegionGroup);
//                float text_x = textRegionGroup.get(0);
//                float text_y = textRegionGroup.get(1);
//
//
//                //使用PDFBox的PDPageContentStream向新页面添加文本内容
//                try (PDPageContentStream contentStream = new PDPageContentStream(newDocument, newPage,PDPageContentStream.AppendMode.APPEND,false,false)) {
//                    contentStream.setFont(getPDFont(document), 12);
//                    contentStream.beginText();
//                    contentStream.newLineAtOffset(100,700); // 设置文本的起始位置
//                    contentStream.showText("test"); // 设置需要添加的文本内容
//                    contentStream.endText();
//                }
//            }
            //使用PDFBox的PDPageContentStream向新页面添加文本内容
            try (PDPageContentStream contentStream = new PDPageContentStream(newDocument, newPage, PDPageContentStream.AppendMode.APPEND, false, false))
            {
                File file = new File("font/simsun.ttc");
                PDFont font = PDType0Font.load(document, file);
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700); // 设置文本的起始位置
                contentStream.showText("test"); // 设置需要添加的文本内容
                contentStream.endText();
            }

            // 保存新的双层PDF文档
            //newDocument.save("双层pdf.pdf");

            // 关闭文档
            document.close();
            newDocument.close();
        }
//    public static Map<String, TrueTypeFont> tccFontMaps = new HashMap<>();
//
//    static {
//        //静态初始化默认字体SimSun【宋体】 key = SimSun
//        try {
//            TrueTypeCollection ttc = new TrueTypeCollection(new ClassPathResource("font/simsun.ttc").getStream());
//            tccFontMaps.put("SimSun", ttc.getFontByName("SimSun"));
//        } catch (IOException e) {
//            throw new RuntimeException("系统默认【宋体】字体加载失败，请检查字体文件");
//        }
//    }
//    public static PDFont getPDFont(PDDocument document, String key) throws IOException {
//        return PDType0Font.load(document, tccFontMaps.get(key), true);
//    }
//    public static PDFont getPDFont(PDDocument document) throws IOException {
//        return getPDFont(document,"SimSun");
//    }
    }
}

