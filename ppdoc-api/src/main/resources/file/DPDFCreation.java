package file;

import com.itextpdf.io.util.FileUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.json.JSONArray;
import org.json.JSONObject;

public class DPDFCreation
{
    private static final String LINE = System.getProperty("line.separator");

    public DPDFCreation()
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
 * @param imgPath 图片文件的路径。
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


    public static ByteArrayOutputStream pdf2Dpdf(InputStream input, float[] pdfZise, JSONObject textJO)
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try
        {
            //使用BaseFont加载指定字体（这里是“STSong-Light”，支持中文字符集）
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            //读取原始PDF文件并创建PdfReader对象
            PdfReader reader = new PdfReader(input);

            int ch;
            while ((ch = input.read()) != - 1)
            {
                output.write(ch);
            }

            //创建用于输出新PDF文件的OutputStream和PdfStamper对象
            PdfStamper stamper = new PdfStamper(reader, output);
            //获取PdfContentByte对象，用于在原PDF页面上进行内容添加和修改
            PdfContentByte page = stamper.getOverContent(1);
            //开始文本模式
            page.beginText();
            BaseColor coler = new BaseColor(255, 0, 0, 0);
            page.setColorFill(coler);
            float fontSize = 18.0F;
            if (0 == textJO.getInt("code"))
            {
                JSONObject resultJO = textJO.getJSONObject("result");
                //线条
                JSONArray linesJA = resultJO.getJSONArray("lines");
                //区域
                JSONArray areaJA = resultJO.getJSONArray("coords");
                //识别结果区域宽度
                float iw = (float) areaJA.getInt(2);
                //识别结果区域高度
                float ih = (float) areaJA.getInt(7);
                //原pdf宽度
                float pw = pdfZise[0];
                //原pdf高度
                float ph = pdfZise[1];
                float bs = iw / 1275.0F;

                for (int i = 0; i < linesJA.length(); ++ i)
                {
                    new StringBuilder();
                    JSONObject lineJO = linesJA.getJSONObject(i);
                    JSONArray charsJA = lineJO.getJSONArray("chars");

                    for (int j = 0; j < charsJA.length(); ++ j)
                    {
                        JSONObject charJO = charsJA.getJSONObject(j);
                        JSONArray coordsCode = charJO.getJSONArray("coords");
                        int leftButtomX = coordsCode.getInt(6);
                        if (j + 1 < charsJA.length())
                        {
                            JSONObject charJONext = charsJA.getJSONObject(j + 1);
                            JSONArray coordsCodeNext = charJONext.getJSONArray("coords");
                            int leftButtomNextX = coordsCodeNext.getInt(6);
                            int value = leftButtomNextX - leftButtomX;
                            float size = (float) value / bs * 0.5F;
                            boolean flag = isConcatinsNumber(charJO.getString("code"));
                            if (flag)
                            {
                                size = size < 1.0F ? 16.0F : size;
                                page.setFontAndSize(baseFont, size * 2.0F);
                            }
                            else
                            {
                                size = size < 16.0F ? 16.0F : size;
                                page.setFontAndSize(baseFont, size);
                            }

                            page.setTextMatrix((float) coordsCode.getInt(0) * pw / iw, ph - 16.0F - (float) coordsCode.getInt(1) * ph / ih);
                            page.showText(charJO.getString("code").trim());
                        }
                        else
                        {
                            int width = getWidth(coordsCode);
                            float size = (float) width / bs * 0.5F;
                            size = size < 16.0F ? 16.0F : size;
                            page.setFontAndSize(baseFont, size);
                            System.out.println("宽度为=======" + charJO.getString("code") + "========" + (coordsCode.getInt(7) - coordsCode.getInt(1)));
                            page.setTextMatrix((float) coordsCode.getInt(0) * pw / iw, ph - 16.0F - (float) coordsCode.getInt(1) * ph / ih);
                            page.showText(charJO.getString("code").trim());
                        }
                    }
                }
            }

            stamper.close();
            reader.close();
            input.close();
        } catch (IOException var33)
        {
            var33.printStackTrace();
        } catch (DocumentException var34)
        {
            var34.printStackTrace();
        }

        return output;
    }

    public static void pdf2Dpdf6Word(String pdfPath, float[] pdfZise, JSONObject textJO, String DpdfPath)
    {
        try
        {
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            InputStream input = new FileInputStream(new File(pdfPath));
            PdfReader reader = new PdfReader(input);
            OutputStream output = new FileOutputStream(new File(DpdfPath));
            PdfStamper stamper = new PdfStamper(reader, output);
            PdfContentByte page = stamper.getOverContent(1);
            page.beginText();
            BaseColor coler = new BaseColor(255, 0, 0, 0);
            page.setColorFill(coler);
            if (0 == textJO.getInt("code"))
            {
                JSONObject resultJO = textJO.getJSONObject("result");
                JSONArray linesJA = resultJO.getJSONArray("lines");
                JSONArray areaJA = resultJO.getJSONArray("coords");
                float iw = (float) areaJA.getInt(2);
                float ih = (float) areaJA.getInt(7);
                float pw = pdfZise[0];
                float ph = pdfZise[1];
                float bs = iw / 1275.0F;

                for (int i = 0; i < linesJA.length(); ++ i)
                {
                    new StringBuilder();
                    JSONObject lineJO = linesJA.getJSONObject(i);
                    JSONArray charsJA = lineJO.getJSONArray("chars");

                    for (int j = 0; j < charsJA.length(); ++ j)
                    {
                        JSONObject charJO = charsJA.getJSONObject(j);
                        JSONArray coordsCode = charJO.getJSONArray("coords");
                        int leftButtomX = coordsCode.getInt(6);
                        if (j + 1 < charsJA.length())
                        {
                            JSONObject charJONext = charsJA.getJSONObject(j + 1);
                            JSONArray coordsCodeNext = charJONext.getJSONArray("coords");
                            int leftButtomNextX = coordsCodeNext.getInt(6);
                            int value = leftButtomNextX - leftButtomX;
                            float size = (float) value / bs * 0.5F;
                            boolean flag = isConcatinsNumber(charJO.getString("code"));
                            if (flag)
                            {
                                size = size < 1.0F ? 16.0F : size;
                                page.setFontAndSize(baseFont, size * 2.0F);
                            }
                            else
                            {
                                size = size < 16.0F ? 16.0F : size;
                                page.setFontAndSize(baseFont, size);
                            }

                            page.setTextMatrix((float) coordsCode.getInt(0) * pw / iw, ph - 16.0F - (float) coordsCode.getInt(1) * ph / ih);
                            page.showText(charJO.getString("code").trim());
                        }
                        else
                        {
                            int width = getWidth(coordsCode);
                            float size = (float) width / bs * 0.5F;
                            size = size < 16.0F ? 16.0F : size;
                            page.setFontAndSize(baseFont, size);
                            page.setTextMatrix((float) coordsCode.getInt(0) * pw / iw, ph - 16.0F - (float) coordsCode.getInt(1) * ph / ih);
                            page.showText(charJO.getString("code").trim());
                        }
                    }
                }
            }

            page.endText();
            stamper.close();
            reader.close();
            output.close();
        } catch (Exception var33)
        {
            System.out.println("双层pdf合成失败！");
            var33.printStackTrace();
        }

    }

    //判断给定字符串是否包含数字字符
    private static boolean isConcatinsNumber(String code)
    {
        return code.contains("1") || code.contains("2") || code.contains("3") || code.contains("4") || code.contains("5") || code.contains("6") || code.contains("7") || code.contains("8") || code.contains("9") || code.contains("0");
    }

    //返回给定字符的宽度（从左边界到右边界
    private static int getWidth(JSONArray coords)
    {
        System.out.println("x" + coords.getInt(4) + "---y" + coords.get(0));
        return coords.getInt(4) - coords.getInt(0);
    }

    /**
     * 将PDF文本转换为指定尺寸，并添加到另一个PDF中。
     *
     * @param pdfPath  原始PDF文件的路径。
     * @param pdfZise  指定的PDF尺寸，数组中第一个元素为宽度，第二个元素为高度。
     * @param textJO   包含转换后文本信息的JSONObject对象。
     * @param DpdfPath 目标PDF文件的路径，即转换后包含文本的PDF文件保存路径。
     */
    public static void pdf2Dpdf2(String pdfPath, float[] pdfZise, JSONObject textJO, String DpdfPath)
    {
        try
        {
            // 创建字体对象，用于在PDF中显示文字
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
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

            // 解析并处理传入的JSONObject，将文本添加到PDF中
            if (0 == textJO.getInt("code"))
            {
                JSONObject resultJO = textJO.getJSONObject("result");
                JSONArray linesJA = resultJO.getJSONArray("lines");//一行
                JSONArray areaJA = resultJO.getJSONArray("coords");//区域
                float iw = (float) areaJA.getInt(2); // 获取原始PDF的宽度
                float ih = (float) areaJA.getInt(7); // 获取原始PDF的高度
                float pw = pdfZise[0]; // 目标PDF的宽度
                float ph = pdfZise[1]; // 目标PDF的高度

                // 遍历并处理每行文字
                for (int i = 0; i < linesJA.length(); ++ i)
                {
                    JSONObject lineJO = linesJA.getJSONObject(i);
                    JSONArray charsJA = lineJO.getJSONArray("chars");

                    // 遍历并处理每个字符
                    for (int j = 0; j < charsJA.length(); ++ j)
                    {
                        JSONObject charJO = charsJA.getJSONObject(j);
                        JSONArray coordsJA = charJO.getJSONArray("coords");
                        int score = charJO.getInt("score");
                        // 设置文字的位置
                        page.setTextMatrix((float) coordsJA.getInt(0) * pw / iw, ph - 14.0F - (float) coordsJA.getInt(1) * ph / ih);
                        String word = charJO.getString("code");
                        // 对识别得分较低的字符添加方括号标记
                        if (650 > score)
                        {
                            word = "[" + word + "]";
                        }

                        // 在行尾添加换行符
                        if (j == charsJA.length() - 1)
                        {
                            word = word + "\n";
                        }

                        page.showText(word); // 显示文字
                    }
                }
            }

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



    public static void pdf2Dpdf3(String pdfPath, float[] pdfZise, JSONObject textJO, String DpdfPath)
    {
        try
        {
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            InputStream input = new FileInputStream(new File(pdfPath));
            PdfReader reader = new PdfReader(input);
            OutputStream output = new FileOutputStream(new File(DpdfPath));
            PdfStamper stamper = new PdfStamper(reader, output);
            PdfContentByte page = stamper.getOverContent(1);
            page.beginText();
            page.setFontAndSize(baseFont, 16.0F);
            if (0 == textJO.getInt("code"))
            {
                JSONObject resultJO = textJO.getJSONObject("result");
                JSONArray linesJA = resultJO.getJSONArray("lines");
                JSONArray areaJA = resultJO.getJSONArray("coords");
                float iw = (float) areaJA.getInt(2);
                float ih = (float) areaJA.getInt(7);
                float pw = pdfZise[0];
                float ph = pdfZise[1];

                for (int i = 0; i < linesJA.length(); ++ i)
                {
                    JSONObject lineJO = linesJA.getJSONObject(i);
                    JSONArray charsJA = lineJO.getJSONArray("chars");

                    for (int j = 0; j < charsJA.length(); ++ j)
                    {
                        JSONObject charJO = charsJA.getJSONObject(j);
                        JSONArray coordsJA = charJO.getJSONArray("coords");
                        int score = charJO.getInt("score");
                        page.setTextMatrix((float) coordsJA.getInt(0) * pw / iw, ph - 16.0F - (float) coordsJA.getInt(1) * ph / ih);
                        BaseColor coler;
                        if (600 < score)
                        {
                            coler = new BaseColor(0, 0, 255);
                        }
                        else
                        {
                            coler = new BaseColor(255, 0, 0);
                        }

                        page.setColorFill(coler);
                        page.showText(charJO.getString("code"));
                    }
                }
            }

            page.endText();
            stamper.close();
            reader.close();
            input.close();
            output.close();
        } catch (IOException var25)
        {
            var25.printStackTrace();
        } catch (DocumentException var26)
        {
            var26.printStackTrace();
        }

    }

    public static void pdf2Dpdf4(String pdfPath, float[] pdfZise, JSONObject textJO, String DpdfPath)
    {
        try
        {
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            InputStream input = new FileInputStream(new File(pdfPath));
            PdfReader reader = new PdfReader(input);
            OutputStream output = new FileOutputStream(new File(DpdfPath));
            PdfStamper stamper = new PdfStamper(reader, output);
            PdfContentByte page = stamper.getOverContent(1);
            page.beginText();
            page.setFontAndSize(baseFont, 14.0F);
            BaseColor coler = new BaseColor(255, 0, 0, 0);
            page.setColorFill(coler);
            if (0 == textJO.getInt("code"))
            {
                JSONObject resultJO = textJO.getJSONObject("result");
                Map<Float, JSONObject> contentAll = new TreeMap();
                JSONArray areaJA = resultJO.getJSONArray("coords");
                float iw = (float) areaJA.getInt(2);
                float ih = (float) areaJA.getInt(7);
                float pw = pdfZise[0];
                float ph = pdfZise[1];
                JSONArray textsJA;
                int i;
                JSONObject itemJO;
                JSONArray linesJA;
                int j;
                if (resultJO.has("tables") && resultJO.getJSONArray("tables").length() >= 1)
                {
                    textsJA = resultJO.getJSONArray("tables");
                    i = 0;

                    label73:
                    while (true)
                    {
                        JSONObject linesJO;
                        if (i >= textsJA.length())
                        {
                            if (resultJO.has("texts"))
                            {
                                JSONArray textsJA = resultJO.getJSONArray("texts");

                                for (int i = 0; i < textsJA.length(); ++ i)
                                {
                                    StringBuilder sb = new StringBuilder();
                                    JSONObject itemJO = textsJA.getJSONObject(i);
                                    linesJO = itemJO.getJSONArray("lines").getJSONObject(0);
                                    getResult(linesJO, sb, pw, iw, ph, ih, contentAll);
                                }
                            }

                            Map<Float, JSONObject> contentAll = ((TreeMap) contentAll).descendingMap();
                            Iterator var34 = contentAll.entrySet().iterator();

                            while (true)
                            {
                                if (! var34.hasNext())
                                {
                                    break label73;
                                }

                                Map.Entry<Float, JSONObject> entry = (Map.Entry) var34.next();
                                JSONObject js = (JSONObject) entry.getValue();
                                float x = (float) js.getInt("x");
                                float y = (float) js.getInt("y");
                                page.setTextMatrix(x, y);
                                page.showText(js.getString("content"));
                            }
                        }

                        itemJO = textsJA.getJSONObject(i);
                        linesJA = itemJO.getJSONArray("cells");

                        for (j = 0; j < linesJA.length(); ++ j)
                        {
                            linesJO = linesJA.getJSONObject(j);
                            JSONArray linesJA = linesJO.getJSONArray("lines");
                            if (linesJA.length() >= 1)
                            {
                                for (int j = 0; j < linesJA.length(); ++ j)
                                {
                                    StringBuilder sb = new StringBuilder();
                                    JSONObject lineJO = linesJA.getJSONObject(j);
                                    getResult(lineJO, sb, pw, iw, ph, ih, contentAll);
                                }
                            }
                        }

                        ++ i;
                    }
                }
                else
                {
                    textsJA = resultJO.getJSONArray("texts");

                    for (i = 0; i < textsJA.length(); ++ i)
                    {
                        itemJO = textsJA.getJSONObject(i);
                        linesJA = itemJO.getJSONArray("lines");

                        for (j = 0; j < linesJA.length(); ++ j)
                        {
                            StringBuilder sb = new StringBuilder();
                            JSONObject lineJO = linesJA.getJSONObject(j);
                            JSONArray charsJA = lineJO.getJSONArray("chars");
                            JSONArray coordsJA = lineJO.getJSONArray("coords");

                            for (int k = 0; k < charsJA.length(); ++ k)
                            {
                                JSONObject charJO = charsJA.getJSONObject(k);
                                sb.append(charJO.getString("code"));
                            }

                            sb.append("\n");
                            page.setTextMatrix((float) coordsJA.getInt(0) * pw / iw, ph - 16.0F - (float) coordsJA.getInt(1) * ph / ih);
                            page.showText(sb.toString());
                        }
                    }
                }
            }

            page.endText();
            stamper.close();
            reader.close();
            input.close();
            output.close();
        } catch (IOException var29)
        {
            var29.printStackTrace();
        } catch (DocumentException var30)
        {
            var30.printStackTrace();
        }

    }

    public static void pdf2Dpdf5(String pdfPath, float[] pdfZise, JSONObject textJO, String DpdfPath)
    {
        try
        {
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            InputStream input = new FileInputStream(new File(pdfPath));
            PdfReader reader = new PdfReader(input);
            OutputStream output = new FileOutputStream(new File(DpdfPath));
            PdfStamper stamper = new PdfStamper(reader, output);
            PdfContentByte page = stamper.getOverContent(1);
            page.beginText();
            page.setFontAndSize(baseFont, 14.0F);
            BaseColor coler = new BaseColor(255, 0, 0, 0);
            page.setColorFill(coler);
            if (0 == textJO.getInt("code"))
            {
                JSONObject resultJO = textJO.getJSONObject("result");
                JSONArray areaJA = resultJO.getJSONArray("coords");
                float iw = (float) areaJA.getInt(2);
                float ih = (float) areaJA.getInt(7);
                float pw = pdfZise[0];
                float ph = pdfZise[1];
                JSONArray textsJA = resultJO.getJSONArray("texts");

                for (int i = 0; i < textsJA.length(); ++ i)
                {
                    JSONObject itemJO = textsJA.getJSONObject(i);
                    JSONArray linesJA = itemJO.getJSONArray("lines");

                    for (int j = 0; j < linesJA.length(); ++ j)
                    {
                        StringBuilder sb = new StringBuilder();
                        JSONObject lineJO = linesJA.getJSONObject(j);
                        String text = lineJO.getString("text");
                        JSONArray coordsJA = lineJO.getJSONArray("coords");
                        sb.append(text);
                        float x = (float) coordsJA.getInt(0) * pw / iw;
                        float y = ph - 36.0F - (float) coordsJA.getInt(1) * ph / (ih * 1.5F);
                        page.setTextMatrix(x, y);
                        page.showText(sb.toString());
                    }
                }
            }

            page.endText();
            stamper.close();
            reader.close();
        } catch (Exception var28)
        {
            System.out.println("双层pdf合成失败！");
            var28.printStackTrace();
        }

    }

    public static void pdf2Dpdf6(String pdfPath, float[] pdfZise, JSONObject textJO, String DpdfPath)
    {
        try
        {
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            InputStream input = new FileInputStream(new File(pdfPath));
            PdfReader reader = new PdfReader(input);
            OutputStream output = new FileOutputStream(new File(DpdfPath));
            PdfStamper stamper = new PdfStamper(reader, output);
            PdfContentByte page = stamper.getOverContent(1);
            page.beginText();
            page.setFontAndSize(baseFont, 14.0F);
            BaseColor coler = new BaseColor(255, 0, 0, 0);
            page.setColorFill(coler);
            if (0 == textJO.getInt("code"))
            {
                JSONObject resultJO = textJO.getJSONObject("result");
                JSONArray areaJA = resultJO.getJSONArray("coords");
                float iw = (float) areaJA.getInt(2);
                float ih = (float) areaJA.getInt(7);
                float pw = pdfZise[0];
                float ph = pdfZise[1];
                JSONArray textsJA = resultJO.getJSONArray("lines");

                for (int i = 0; i < textsJA.length(); ++ i)
                {
                    JSONObject itemJO = textsJA.getJSONObject(i);
                    String text = itemJO.getString("text");
                    JSONArray coordsJA = itemJO.getJSONArray("coords");
                    float x = (float) coordsJA.getInt(0) * pw / iw;
                    float y = ph - 14.0F - (float) coordsJA.getInt(1) * ph / ih;
                    page.setTextMatrix(x, y);
                    page.showText(text);
                }
            }

            page.endText();
            stamper.close();
            reader.close();
        } catch (Exception var24)
        {
            System.out.println("双层pdf合成失败！");
            var24.printStackTrace();
        }

    }

    public static void requestHWOCR(String imgPath, String pdfFolder)
    {
        String base64img = Base64Util.getFileByteString(imgPath);
        String key = "demo";
        String length = Integer.toString(base64img.length());
        String url = "http://114.113.225.41:8109/ocrft";

        HttpResponse jsonResponse;
        try
        {
            jsonResponse = Unirest.post(url).header("Content-type", "application/x-www-form-urlencoded").field("key", "demo").field("base64img", base64img).asJson();
        } catch (UnirestException var11)
        {
            System.out.println("发送ocr结果失败");
            var11.printStackTrace();
            return;
        }

        float[] pdfZise = img2pdf2(imgPath, pdfFolder);
        JSONObject rerJObject = ((JsonNode) jsonResponse.getBody()).getObject();
        System.out.println(rerJObject.toString());
        String pdfPath = pdfFolder + System.getProperty("file.separator") + FileUtil.getFileName(imgPath) + ".pdf";
        String DpdfPath = pdfFolder + System.getProperty("file.separator") + FileUtil.getFileName(imgPath) + "_d.pdf";
        pdf2Dpdf6(pdfPath, pdfZise, rerJObject, pdfPath);
    }

    public static String parseOCRResultToText(JSONObject jo)
    {
        StringBuilder sb = new StringBuilder();
        if (0 == jo.getInt("code"))
        {
            JSONObject resultJO = jo.getJSONObject("result");
            JSONArray linesJA = resultJO.getJSONArray("lines");

            for (int i = 0; i < linesJA.length(); ++ i)
            {
                JSONObject lineJO = linesJA.getJSONObject(i);
                JSONArray charsJA = lineJO.getJSONArray("chars");

                for (int j = 0; j < charsJA.length(); ++ j)
                {
                    JSONObject charJO = charsJA.getJSONObject(j);
                    sb.append(charJO.getString("code"));
                }

                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private static void getResult(JSONObject lineJO, StringBuilder sb, float pw, float iw, float ph, float ih, Map<Float, JSONObject> contentAll)
    {
        JSONArray charsJA = lineJO.getJSONArray("chars");
        JSONArray coordsJA = lineJO.getJSONArray("coords");

        for (int j = 0; j < charsJA.length(); ++ j)
        {
            JSONObject charJO = charsJA.getJSONObject(j);
            sb.append(charJO.getString("code"));
        }

        sb.append("\n");
        float x = (float) coordsJA.getInt(0) * pw / iw;
        float y = ph - 16.0F - (float) coordsJA.getInt(1) * ph / ih;
        sortContent(contentAll, x, y, sb);
    }

    private static void sortContent(Map<Float, JSONObject> contentAll, float x, float y, StringBuilder sb)
    {
        String content = sb.toString();
        JSONObject jsMu;
        if (contentAll.containsKey(y))
        {
            jsMu = (JSONObject) contentAll.get(y);
            float xM = jsMu.getFloat("x");
            String conMu = jsMu.getString("content");
            String conNew = "";
            float xNew = 0.0F;
            if (xM < x)
            {
                conNew = conMu.replaceAll(LINE, "") + " " + content.replaceAll(LINE, "") + LINE;
                xNew = xM;
            }
            else
            {
                conNew = content.replaceAll(LINE, "") + " " + conMu.replaceAll(LINE, "") + LINE;
                xNew = x;
            }

            jsMu.put("x", xNew);
            jsMu.put("content", conNew);
            contentAll.put(y, jsMu);
        }
        else
        {
            jsMu = new JSONObject();
            jsMu.put("x", x);
            jsMu.put("y", y);
            jsMu.put("content", content);
            contentAll.put(y, jsMu);
        }

    }

    public static void main(String[] args) throws DocumentException, IOException
    {
        String jpgPath = "<LOCAL_PATH_REDACTED>";
        String dpdfFolder = "<LOCAL_PATH_REDACTED>";
        requestHWOCR(jpgPath, dpdfFolder);
    }
}
