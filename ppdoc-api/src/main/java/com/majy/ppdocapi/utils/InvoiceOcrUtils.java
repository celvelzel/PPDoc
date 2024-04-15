package com.majy.ppdocapi.utils;

import com.majy.ppdocapi.controller.ZhiPuLLM;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class InvoiceOcrUtils extends PaddleOcrUtils
{
    //全局变量
    private static final String NO_INFO_FOUND = "未找到";

    public static Map<String, String> getStringStringMap(List<List> jsons)
    {
        //调用父类的jsonToString方法，拼接OCR结果
        String trim = jsonToString(jsons);

        String allInfo = trim;
        List<Map> maps = jsons.get(0);
        String invoiceNumber = invoiceNumber(trim);
        String invoiceCode = invoiceCode(trim);
        String invoiceDate = invoiceDate(trim);
        String invoiceAmount = invoiceAmount(trim);

        Map<String, String> invoiceInfoMap = new HashMap<>();
        invoiceInfoMap.put("invoiceNumber", invoiceNumber);
        invoiceInfoMap.put("invoiceCode", invoiceCode);
        invoiceInfoMap.put("invoiceDate", invoiceDate);
        invoiceInfoMap.put("invoiceAmount", invoiceAmount);
        invoiceInfoMap.put("allInfo", allInfo);

        //调用LLM
        Map<String, String> invoiceLLMMap = invokeLLM(trim);
        invoiceInfoMap.putAll(invoiceLLMMap);

        return invoiceInfoMap;
    }


    /**
     * 获取发票代码
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 发票代码
     */
    private static String invoiceCode(String trim)
    {
        String invoiceCode = "";
        Pattern pattern = Pattern.compile(".*发票代码.(\\d{12})|.*发票代码.(\\d{10})");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            invoiceCode = matcher.group(1);
        }
        else
        {
            invoiceCode = NO_INFO_FOUND;
        }
        return invoiceCode;
    }

    /**
     * 获取发票号码
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 发票号码
     */
    private static String invoiceNumber(String trim)
    {
        String invoiceNumber = "";
        Pattern pattern = Pattern.compile(".*发票号码.(\\d{8})");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            invoiceNumber = matcher.group(1);
        }
        else
        {
            invoiceNumber = NO_INFO_FOUND;
        }
        return invoiceNumber;
    }

    /**
     * 获取开票日期
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 开票日期
     */
    private static String invoiceDate(String trim)
    {
        String invoiceDate = "";
        Pattern pattern = Pattern.compile(".*开票日期.(\\d{4})年(\\d{1,2})月(\\d{1,2})日");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            String year = matcher.group(1);
            String month = matcher.group(2);
            String day = matcher.group(3);
            invoiceDate = year + "-" + month + "-" + day;
        }
        else
        {
            invoiceDate = "2002-10-28";
        }
        return invoiceDate;
    }

    private static String invoiceAmount(String trim)
    {
        String invoiceNumber = "";
        Pattern pattern = Pattern.compile(".*合计¥(\\d+)?(\\.\\d+)?");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            invoiceNumber = matcher.group(1) + matcher.group(2);
        }
        else
        {
            invoiceNumber = NO_INFO_FOUND;
        }
        return invoiceNumber;
    }

    /**
     * 调用智谱API，分析文本
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 获取项目名称、购买方名称、销售方名称
     */
    private static Map<String,String> invokeLLM(String trim)
    {
        String projectName = "";
        String purchaserName = "";
        String sellerName = "";
        Map<String,String> invoiceInfoMap= new HashMap<>();

        String LLMResult = ZhiPuUtils.sseInvokeExtractInfo(trim, "项目名称，购买方名称，销售方名称");
        log.info("智谱LLM结果是：" + LLMResult);
        Pattern pattern = Pattern.compile("项目名称.([\u4e00-\u9fa5]*)\\s*购买方名称.([\u4e00-\u9fa5]*)\\s*销售方名称.([\u4e00-\u9fa5]*)");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            projectName = matcher.group(1);
            purchaserName = matcher.group(2);
            sellerName = matcher.group(3);
        }
        else
        {
            projectName = NO_INFO_FOUND;
            purchaserName = NO_INFO_FOUND;
            sellerName = NO_INFO_FOUND;
        }
        invoiceInfoMap.put("projectName",projectName);
        invoiceInfoMap.put("purchaserName",purchaserName);
        invoiceInfoMap.put("sellerName",sellerName);
        return invoiceInfoMap;
    }


    //测试类
    @Test
    public void test()
    {
        String projectName = "";
        String purchaserName = "";
        String sellerName = "";
        String text = "\"项目名称：餐饮服务\\n购买方名称：太极计算机股份有限公司\\n销售方名称：深圳市珍湘味饮食文化有限公司\"";
        Pattern pattern = Pattern.compile("项目名称.([\u4e00-\u9fa5]*).*购买方名称.([\u4e00-\u9fa5]*).*销售方名称.([\u4e00-\u9fa5]*)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find())
        {
            projectName = matcher.group(1);
            purchaserName = matcher.group(2);
            sellerName = matcher.group(3);
        }
        else
        {
            projectName = NO_INFO_FOUND;
            purchaserName = NO_INFO_FOUND;
            sellerName = NO_INFO_FOUND;
        }
        System.out.println("ProjectN:"+projectName);
        System.out.println("PurchaserN:"+purchaserName);
        System.out.println("sellerN:"+sellerName);
    }
}
