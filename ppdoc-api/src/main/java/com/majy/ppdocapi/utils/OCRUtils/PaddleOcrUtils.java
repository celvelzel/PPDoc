package com.majy.ppdocapi.utils.OCRUtils;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class PaddleOcrUtils
{
    //全局变量
    private static final String NO_INFO_FOUND = "未找到";

    public PaddleOcrUtils()
    {

    }


    //传入单个pdf,先转图片list，再进行ocr
    public static List pdfToOcrText(MultipartFile file)
    {
        try
        {
            InputStream pdfInputStream = file.getInputStream();

            //创建准备进行ocr操作的图片list
            List<MultipartFile> imageFiles = new ArrayList<MultipartFile>();

            // 写入数据到图片list
            PdfToImageUtils.pdf2ImageList(pdfInputStream, imageFiles, "png");
            log.info("pdf转图片完成");

            //传入图片文件，调用ocr模块进行识别，返回json格式的识别结果
            List res = PaddleOcrUtils.imageList2OcrText(imageFiles);
            log.info("图片提取ocr文本完成");

            pdfInputStream.close();

            //返回OCR内容
            return res;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //传入单个pdf的输入流,先转图片list，再进行ocr
    public static List pdfToOcrText(InputStream pdfInputStream)
    {
        try
        {
            //创建准备进行ocr操作的图片list
            List<MultipartFile> imageFiles = new ArrayList<MultipartFile>();

            // 写入数据到图片list
            PdfToImageUtils.pdf2ImageList(pdfInputStream, imageFiles, "png");
            log.info("pdf转图片完成");

            //传入图片文件，调用ocr模块进行识别，返回json格式的识别结果
            List res = PaddleOcrUtils.imageList2OcrText(imageFiles);
            log.info("图片提取ocr文本完成");

            pdfInputStream.close();

            //返回OCR内容
            return res;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /*接受一个图片list，调用OCR模块的服务，
     * 返回OCR识别的结果中的result部分
     * 包括文字、置信度和文字区域的坐标*/
    public static List imageList2OcrText(List<MultipartFile> files) throws IOException
    {
        HttpHeaders headers = new HttpHeaders();
        //设置请求头格式
        headers.setContentType(MediaType.APPLICATION_JSON);
        //构建请求参数
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        //添加请求参数images，按页将Base64编码的图片列表传入
        for (int index = 0; index < files.size(); index++)
        {
            byte[] bytes = files.get(index).getBytes();
            map.add("images", ImageToBase64(bytes));
        }
        //构建请求
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        //发送请求, springboot内置的restTemplate
        //通过8868端口调用ppocr的服务
        Map json = restTemplate.postForEntity("http://127.0.0.1:8868/predict/ocr_system", request, Map.class).getBody();
        List<List<Map>> jsons = (List<List<Map>>) json.get("results");
        //记录日志，记录ocr的结果
        log.info("OCR结果：" + jsons);
        return jsons;
    }


    /**
     * 发起OCR请求，识别图片中的文字。
     *
     * @param imgPath 图片的文件路径，用于读取图片并上传。
     * @return 返回一个JSONObject对象，包含OCR识别的status和result。
     * @throws IOException 如果读取图片失败，则抛出此异常。
     */
    public static JSONObject requestOcr(String imgPath) throws IOException
    {
        // 初始化HTTP请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 构建请求参数
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        // 读取图片文件并转为Base64编码
        byte[] bytes = Files.readAllBytes(Paths.get(imgPath));
        map.add("images", ImageToBase64(bytes));

        // 构建HTTP请求实体
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        // 创建REST模板客户端
        RestTemplate restTemplate = new RestTemplate();

        // 发送POST请求，调用OCR服务
        Map jsons = restTemplate.postForEntity("http://127.0.0.1:8868/predict/ocr_system", request, Map.class).getBody();

        // 将Map转换为JSONObject
        JSONObject jsonObject = new JSONObject(jsons);
        return jsonObject;
    }

    //将OCR识别结果的json拼接转成字符串
    public static String jsonToString(List<List> jsons)
    {
        StringBuilder result = new StringBuilder();
        //遍历每一页的ocr结果
        for (List<Map> json : jsons)
        {
            for (int i = 0; i < json.size(); i++)
            {
                log.info("当前的文字是：" + json.get(i).get("text"));
                // 这里通过trim()和replace()双重保险去除文字中的空格
                result.append(json.get(i).get("text").toString().trim().replace(" ", ""));
            }
        }
        String trim = result.toString().trim();
        log.info("=================拼接后的文字是=========================");
        log.info(trim);
        return trim;
    }

    /**
     * 获取图片的base64位
     *
     * @param data 图片变成byte数组
     * @return 图片的base64为内容
     */
    private static String ImageToBase64(byte[] data)
    {
        // 直接调用springboot内置的springframework内置的方法
        String encodeToString = Base64Utils.encodeToString(data);
        return encodeToString;
    }
}
