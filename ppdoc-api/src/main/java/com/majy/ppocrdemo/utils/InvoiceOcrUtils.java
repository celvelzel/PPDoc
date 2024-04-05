package com.majy.ppocrdemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@Slf4j
public class InvoiceOcrUtils
{
    /*接受一个图片list，调用OCR模块的服务，
     * 返回OCR识别的结果
     * 包括文字、置信度和文字区域的坐标*/
    public static List getOcrText(List<MultipartFile> files) throws IOException
    {
        HttpHeaders headers = new HttpHeaders();
        //设置请求头格式
        headers.setContentType(MediaType.APPLICATION_JSON);
        //构建请求参数
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        //添加请求参数images，并将Base64编码的图片传入
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
    public static Map<String, String> getStringStringMap(List<List> jsons)
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
        System.out.println("=================拼接后的文字是=========================");
        System.out.println(trim);
        String allInfo = trim;
        System.out.println("=======================使用正则表达提取文字信息===============================");
        List<Map> maps = jsons.get(0);
//        String name = predictName(maps);
//        if (name.equals("") || name == null)
//        {
//            name = fullName(trim);
//        }
//        System.out.println("姓名：" + name);
//        String nation = national(maps);
//        System.out.println("民族：" + nation);
//        String address = address(maps);
//        System.out.println("地址：" + address);
//        String cardNumber = cardNumber(maps);
//        System.out.println("身份证号：" + cardNumber);
//        String sex = sex(cardNumber);
//        System.out.println("性别：" + sex);
//        String birthday = birthday(cardNumber);
//        System.out.println("出生：" + birthday);


        Map<String, String> invoiceMap = new HashMap<>();
//        userInfoMap.put("name", name);
//        userInfoMap.put("nation", nation);
//        userInfoMap.put("address", address);
//        userInfoMap.put("cardNumber", cardNumber);
//        userInfoMap.put("sex", sex);
//        userInfoMap.put("birthday", birthday);
        invoiceMap.put("allInfo", allInfo);
        return invoiceMap;
    }
}

