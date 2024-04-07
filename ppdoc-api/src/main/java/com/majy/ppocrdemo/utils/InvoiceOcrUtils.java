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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
            map.add("images", Base64Utils.encodeToString(bytes));
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


    //测试类
    public static void main(String[] args)
    {
        String text = "发票代码：044032200111发票号码：28164546国统开票日期：2022年12月03日深圳市税务局机器编号：校验码：09976900364593599737917001472768名称：太极计算机股份有限公司0009＊0/3>9<<1*8＊＊3636<<</-0-购纳税人识别号：9１１１0000１0１137049C**/08*/3518670>+31641-2>5/<-买码地址、电话:>9*>5-38955>7<04+30<6<>41*63方区开户行及账号：<+623+07*1-642014<9/19*9>310单位项目名称规格型号数量单价金额税率税额免税*餐饮服务*餐饮服务283.00283.00***1合计¥283.00贰佰捌拾叁圆整价税合计(大写)（小写）¥283.00名称：深圳市珍湘味饮食文化有限公司销备纳税人识别号：91440300MA5G4A9QXN售地址、电话：107523975507深圳市福田区莲花街道彩虹社区莲花支路1011号润鹏花园莲花路1092号11方注开户行及账号：中国民生银行深圳彩田支行161984645开票人：汪永香91440300MA5G4A9QXN复核：陈凌收款人：汪燕珍销售方：（章发票专用章";

        Pattern pattern = Pattern.compile(
                "(发票代码：)(.*?)(发票号码：)(.*?)(开票日期：)(.*?)(机器编号：)(.*?)(校验码：)(.*?)(名称：)(.*?)(纳税人识别号：)(.*?)(开户行及账号：)(.*?)"
        );
        Matcher matcher = pattern.matcher(text);

        if (matcher.find())
        {
            System.out.println("发票代码：" + matcher.group(2));
            System.out.println("发票号码：" + matcher.group(4));
            System.out.println("开票日期：" + matcher.group(6));
            System.out.println("机器编号：" + matcher.group(8));
            System.out.println("校验码：" + matcher.group(10));
            System.out.println("名称：" + matcher.group(12));
            System.out.println("纳税人识别号：" + matcher.group(14));
            System.out.println("开户行及账号：" + matcher.group(16));
        }
    }

}
