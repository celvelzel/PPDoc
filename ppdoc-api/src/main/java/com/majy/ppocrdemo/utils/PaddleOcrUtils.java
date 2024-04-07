package com.majy.ppocrdemo.utils;

import com.mashape.unirest.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PaddleOcrUtils
{

    private PaddleOcrUtils()
    {

    }

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


    public static JSONObject requestOcr(String imgPath) throws IOException
    {
        HttpHeaders headers = new HttpHeaders();
        //设置请求头格式
        headers.setContentType(MediaType.APPLICATION_JSON);
        //构建请求参数
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        byte[] bytes = Files.readAllBytes(Paths.get(imgPath));
        map.add("images", ImageToBase64(bytes));
        //构建请求
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        RestTemplate restTemplate = new RestTemplate();


        //发送请求, springboot内置的restTemplate
        //通过8868端口调用ppocr的服务
        Map jsons = restTemplate.postForEntity("http://127.0.0.1:8868/predict/ocr_system", request, Map.class).getBody();
        //记录日志，记录ocr的结果
        //log.info("OCR结果：" + jsons);
        //Map转成JSONObject
        JSONObject jsonObject = new JSONObject(jsons);
        return jsonObject;
    }

    public static Map<String, String> getStringStringMap(List<MultipartFile> files) throws IOException
    {

        try
        {
            StringBuilder result = new StringBuilder();

            List<List<Map>> jsons = getOcrText(files);

            //System.out.println(jsons);
            log.info("OCR结果：" + jsons);

            //遍历每一页的ocr结果
            for (List<Map> json : jsons)
            {
                for (int i = 0; i < json.size(); i++)
                {
                    log.info("当前的文字是：" + json.get(i).get("text"));
                    // 这里光靠这个trim()有些空格是去除不掉的，所以还需要使用替换这个，双重保险
                    result.append(json.get(i).get("text").toString().trim().replace(" ", ""));
                }
            }
            String trim = result.toString().trim();
            System.out.println("=================拼接后的文字是=========================");
            System.out.println(trim);
            String allInfo = trim;
            System.out.println("=======================使用正则表达提取文字信息===============================");
            List<Map> maps = jsons.get(0);
            String name = predictName(maps);
            if (name.equals("") || name == null)
            {
                name = fullName(trim);
            }
            System.out.println("姓名：" + name);
            String nation = national(maps);
            System.out.println("民族：" + nation);
            String address = address(maps);
            System.out.println("地址：" + address);
            String cardNumber = cardNumber(maps);
            System.out.println("身份证号：" + cardNumber);
            String sex = sex(cardNumber);
            System.out.println("性别：" + sex);
            String birthday = birthday(cardNumber);
            System.out.println("出生：" + birthday);


            Map<String, String> userInfoMap = new HashMap<>();
            userInfoMap.put("name", name);
            userInfoMap.put("nation", nation);
            userInfoMap.put("address", address);
            userInfoMap.put("cardNumber", cardNumber);
            userInfoMap.put("sex", sex);
            userInfoMap.put("birthday", birthday);
            userInfoMap.put("allInfo", allInfo);
            return userInfoMap;
        } catch (RestClientException e)
        {
            throw new RuntimeException(e);
        }
    }

    // 上面的方法，使用了static修饰，下面的方法，也需要使用static修饰，这里使用
    // private修饰的话，在其他类中直接通过IdCardOcrUtils.predictName()这个就访问不到了, 或者protected修饰，
    // 不然其他类访问不就行了吗？
    // 这里唯一能通过IdCardOcrUtils.方法名，访问的是public修饰的方法

    /**
     * 获取身份证姓名
     *
     * @param maps 识别的结果集合
     * @return 姓名
     */
    private static String predictName(List<Map> maps)
    {
        String name = "";
        for (Map map : maps)
        {
            String str = map.get("text").toString().trim().replace(" ", "");
            if (str.contains("姓名") || str.contains("名"))
            {
                String pattern = ".*名[\\u4e00-\\u9fa5]{1,4}";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(str);
                if (m.matches())
                {
                    name = str.substring(str.indexOf("名") + 1);
                }
            }
        }
        return name;
    }

    /**
     * 为了防止第一次得到的名字为空，以后是遇到什么情况就解决什么情况就行了
     *
     * @param result panddleOCR扫描得到的结果拼接：
     *               如：姓名韦小宝性别男民族汉出生1654年12月20日住址北京市东城区景山前街4号紫禁城敬事房公民身份证号码11204416541220243X
     * @return
     */
    private static String fullName(String result)
    {
        String name = "";
        if (result.contains("性") || result.contains("性别"))
        {
            String str = result.substring(0, result.lastIndexOf("性"));
            String pattern = ".*名[\\u4e00-\\u9fa5]{1,4}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            if (m.matches())
            {
                name = str.substring(str.indexOf("名") + 1);
            }
        }
        return name;
    }

    /**
     * 获取民族
     *
     * @param maps 识别的结果集合
     * @return 民族信息
     */
    private static String national(List<Map> maps)
    {
        String nation = "";
        for (Map map : maps)
        {
            String str = map.get("text").toString();
            String pattern = ".*民族[\u4e00-\u9fa5]{1,4}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            if (m.matches())
            {
                nation = str.substring(str.indexOf("族") + 1);
            }
        }
        return nation;
    }

    /**
     * 获取身份证地址
     *
     * @param maps 识别的结果集合
     * @return 身份证地址信息
     */
    private static String address(List<Map> maps)
    {
        String address = "";
        StringBuilder addressJoin = new StringBuilder();
        for (Map map : maps)
        {
            String str = map.get("text").toString().trim().replace(" ", "");
            // 看身份证地址那一栏，具体可以看一下自己的身份证，几乎都包含这些字，具体可以自己debugger看一下就知道了
            // 具体可以自己debugger看一下就知道了
            if (str.contains("住址") || str.contains("址") || str.contains("省") || str.contains("市")
                    || str.contains("县") || str.contains("街") || str.contains("乡") || str.contains("村")
                    || str.contains("镇") || str.contains("区") || str.contains("城") || str.contains("组")
                    || str.contains("号") || str.contains("幢") || str.contains("室")
            )
            {
                addressJoin.append(str);
            }
        }
        String s = addressJoin.toString();
        if (s.contains("省") || s.contains("县") || s.contains("住址") || s.contains("址") || s.contains("公民身份证"))
        {
            // 通过这里的截取可以知道，即使是名字中有上述的那些字段，也不要紧，因为这个ocr识别是一行一行来的，所以名字的会在地址这两个字
            // 前面，除非是名字中也有地址的”地“或者”址“字，这个还可以使用lastIndexOf()来从后往左找，也可以在一定程度上避免这个。
            // 具体看后面的截图，就知道了
            address = s.substring(s.indexOf("址") + 1, s.indexOf("公民身份证"));
        }
        else
        {
            address = s;
        }
        return address;
    }

    /**
     * 获取身份证号
     *
     * @param maps ocr识别的内容列表
     * @return 身份证号码
     */
    private static String cardNumber(List<Map> maps)
    {
        String cardNumber = "";
        for (Map map : maps)
        {
            String str = map.get("text").toString().trim().replace(" ", "");
            // 之里注意了，这里的双斜杆，是因为这里是java，\会转义，所以使用双鞋干\\，去掉试一试就知道了
            String pattern = "\\d{17}[\\d|x|X]|\\d{15}";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(str);
            if (m.matches())
            {
                cardNumber = str;
            }
        }
        return cardNumber;
    }

    /**
     * 二代身份证18位
     * 这里之所以这样做，是因为如果直接从里面截取，也可以，但是从打印的内容中，有时候
     * 性别性别男，是在同一行，有些照片是
     * 性
     * 别
     * 男
     * 等，如果单纯是使用字符串的str.contains("男") ==》 然后返回性别男，
     * str.contains("女") ==> 然后返回性别女
     * 这个万姓名中有男字，地址中有男字，等。而这个人的性别是女。这是可能会按照识别顺序
     * 排序之后，识别的是地址的男字，所以这里直接从身份证倒数第二位的奇偶性判断男女更加准确一点
     * 从身份证号码中提取性别
     *
     * @param cardNumber 身份证号码，二代身份证18位
     * @return 性别
     */
    private static String sex(String cardNumber)
    {
        String sex = "";
        // 取倒身份证倒数第二位的数字的奇偶性判断性别，二代身份证18位
        try
        {
            if (cardNumber.length() >= 18)
            {
                String substring = cardNumber.substring(cardNumber.length() - 2, cardNumber.length() - 1);
                int parseInt = Integer.parseInt(substring);
                if (parseInt % 2 == 0)
                {
                    sex = "女";
                }
                else
                {
                    sex = "男";
                }
            }
        } catch (NumberFormatException e)
        {
            throw new RuntimeException(e);
        }
        return sex;
    }

    /**
     * 从身份证中获取出生信息
     *
     * @param cardNumber 二代身份证，18位
     * @return 出生日期
     */
    private static String birthday(String cardNumber)
    {
        String birthday = "";
        if (cardNumber.length() >= 18)
        {
            String date = cardNumber.substring(6, 14);
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);
            birthday = year + "年" + month + "月" + day + "日";
        }
        return birthday;
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
