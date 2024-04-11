package com.majy.ppdoc.utils;

import com.majy.ppdoc.controller.ZhiPuLLM;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LicenseOcrUtils extends PaddleOcrUtils
{
    //全局变量
    private static final String NO_INFO_FOUND = "未找到";

    public static Map<String, String> getStringStringMap(List<List> jsons)
    {
        //调用父类的jsonToString方法，拼接OCR结果
        String trim = jsonToString(jsons);

        String allInfo = trim;
        //List<Map> maps = jsons.get(0);
        String licenseNumber = licenseNumber(trim);
        String licenseCode = licenseCode(trim);
        String licenseEnterpriseName = licenseEnterpriseName(trim);
        String licenseEnterpriseType = licenseEnterpriseType(trim);
        String licenseRegisteredCapital = licenseRegisteredCapital(trim);
        String licenseEstablishDate = licenseEstablishDate(trim);
        String licenseOperationPeriodStart = licenseOperationPeriod(trim)[0];
        String licenseOperationPeriodEnd = licenseOperationPeriod(trim)[1];

        Map<String, String> licenseInfoMap = new HashMap<>();
        licenseInfoMap.put("licenseCode", licenseCode);
        licenseInfoMap.put("licenseNumber", licenseNumber);
        licenseInfoMap.put("licenseEnterpriseName", licenseEnterpriseName);
        licenseInfoMap.put("licenseEnterpriseType", licenseEnterpriseType);
        licenseInfoMap.put("licenseRegisteredCapital", licenseRegisteredCapital);
        licenseInfoMap.put("licenseEstablishDate", licenseEstablishDate);
        licenseInfoMap.put("licenseOperationPeriodStart", licenseOperationPeriodStart);
        licenseInfoMap.put("licenseOperationPeriodEnd", licenseOperationPeriodEnd);
        licenseInfoMap.put("allInfo", allInfo);

        //调用LLM
        Map<String, String> licenseLLMMap = invokeLLM(trim);
        licenseInfoMap.putAll(licenseLLMMap);

        return licenseInfoMap;
    }

    /**
     * 获取统一社会信用代码
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 统一社会信用代码
     */
    private static String licenseCode(String trim)
    {
        String licenseCode = "";
        Pattern pattern = Pattern.compile("统一社会信用代码.(.{18})");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            licenseCode = matcher.group(1);
        }
        else
        {
            licenseCode = NO_INFO_FOUND;
        }
        return licenseCode;
    }

    /**
     * 获取证照编号
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 证照编号
     */
    private static String licenseNumber(String trim)
    {
        String licenseNumber = "";
        Pattern pattern = Pattern.compile("证照编号.(.{18})");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            licenseNumber = matcher.group(1);
        }
        else
        {
            licenseNumber = NO_INFO_FOUND;
        }
        return licenseNumber;
    }

    /**
     * 获取企业名称
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 企业名称
     */
    private static String licenseEnterpriseName(String trim)
    {
        String licenseEnterpriseName = "";
        Pattern pattern = Pattern.compile("名称.(.*)注册资本");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            licenseEnterpriseName = matcher.group(1);
        }
        else
        {
            licenseEnterpriseName = NO_INFO_FOUND;
        }
        return licenseEnterpriseName;
    }

    /**
     * 获取企业类型
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 企业类型
     */
    private static String licenseEnterpriseType(String trim)
    {
        String licenseEnterpriseType = "";
        Pattern pattern = Pattern.compile(".*类型(.*)成立日期");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            licenseEnterpriseType = matcher.group(1);
        }
        else
        {
            licenseEnterpriseType = NO_INFO_FOUND;
        }
        return licenseEnterpriseType;
    }

    /**
     * 获取注册资本
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 注册资本
     */
    private static String licenseRegisteredCapital(String trim)
    {
        String licenseRegisteredCapital = "";
        Pattern pattern = Pattern.compile(".*注册资本(.*)类型");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            licenseRegisteredCapital = matcher.group(1);
        }
        else
        {
            licenseRegisteredCapital = NO_INFO_FOUND;
        }
        return licenseRegisteredCapital;
    }

    /**
     * 获取成立日期
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 成立日期
     */
    private static String licenseEstablishDate(String trim)
    {
        String licenseEstablishDate = "";
        Pattern pattern = Pattern.compile(".*成立日期(\\d{4})年(\\d{2})月(\\d{2})日");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            String year = matcher.group(1);
            String month = matcher.group(2);
            String day = matcher.group(3);
            licenseEstablishDate = year + "-" + month + "-" + day;
        }
        else
        {
            licenseEstablishDate = NO_INFO_FOUND;
        }
        return licenseEstablishDate;
    }

    /**
     * 获取营业期限
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 营业期限
     */
    private static String[] licenseOperationPeriod(String trim)
    {
        String[] licenseOperationPeriod = new String[2];
        Pattern pattern = Pattern.compile(".*营业期限(\\d{4})年(\\d{2})月(\\d{2})日至(\\d{4})年(\\d{2})月(\\d{2})日");
        Matcher matcher = pattern.matcher(trim);
        if (matcher.find())
        {
            String startYear = matcher.group(1);
            String startMonth = matcher.group(2);
            String startDay = matcher.group(3);
            String licenseOperationPeriodStart = startYear + "-" + startMonth + "-" + startDay;
            licenseOperationPeriod[0] = licenseOperationPeriodStart;

            String endYear = matcher.group(4);
            String endMonth = matcher.group(5);
            String endDay = matcher.group(6);
            String licenseOperationPeriodEnd = endYear + "-" + endMonth + "-" + endDay;
            licenseOperationPeriod[1] = licenseOperationPeriodEnd;
        }
        else
        {
            String licenseOperationPeriodStart = NO_INFO_FOUND;
            licenseOperationPeriod[0] = licenseOperationPeriodStart;
            String licenseOperationPeriodEnd = NO_INFO_FOUND;
            ;
            licenseOperationPeriod[1] = licenseOperationPeriodStart;
        }
        return licenseOperationPeriod;
    }

    /**
     * 调用智谱API，分析文本
     *
     * @param trim 拼接后的ocr识别的文字
     * @return 获取经营范围、住所、法定代表人
     */
    private static Map<String, String> invokeLLM(String trim)
    {
        String licenseBusinessScope = "";
        String licenseDomicile = "";
        String licenseLegalRepresentative = "";
        Map<String, String> invoiceLLMMap = new HashMap<>();

        String LLMResult = ZhiPuLLM.sseInvokeExtractInfo(trim, "经营范围，住所，法定代表人");
        log.info("智谱LLM结果是：" + LLMResult);
        Pattern pattern = Pattern.compile("经营范围.(.*)\\s*住所.(.*)\\s*法定代表人.(.*)");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            licenseBusinessScope = matcher.group(1);
            licenseDomicile = matcher.group(2);
            licenseLegalRepresentative = matcher.group(3);
        }
        else
        {
            licenseBusinessScope = NO_INFO_FOUND;
            licenseDomicile = NO_INFO_FOUND;
            licenseLegalRepresentative = NO_INFO_FOUND;
        }
        invoiceLLMMap.put("licenseBusinessScope", licenseBusinessScope);
        invoiceLLMMap.put("licenseDomicile", licenseDomicile);
        invoiceLLMMap.put("licenseLegalRepresentative", licenseLegalRepresentative);
        return invoiceLLMMap;
    }

    @Test
    public void test()
    {
        String text = "经营范围：水处理设备、环保产品及零部件，空气调节设备及其零部件家用电器、燃气器具、电热水器具、太阳能设备、医疗器械的研究、批发、进出口、佣金代理（拍卖除外）并提供相关配套服务。\n" +
                "住所：上海市黄浦区新码头街55号4幢3楼\n" +
                "法定代表人：朱芮" ;
        Pattern pattern = Pattern.compile("经营范围.(.*)\\s*住所.(.*)\\s*法定代表人.(.*)");
        Matcher matcher = pattern.matcher(text);
        String licenseBusinessScope = "";
        String licenseDomicile = "";
        String licenseLegalRepresentative = "";
        log.info("智谱LLM结果是：" + text);
        if (matcher.find())
        {
            licenseBusinessScope = matcher.group(1);
            licenseDomicile = matcher.group(2);
            licenseLegalRepresentative = matcher.group(3);
        }
        else
        {
            licenseBusinessScope = NO_INFO_FOUND;
            licenseDomicile = NO_INFO_FOUND;
            licenseLegalRepresentative = NO_INFO_FOUND;
        }
        System.out.println(licenseBusinessScope);
        System.out.println(licenseDomicile);
        System.out.println(licenseLegalRepresentative);
//        System.out.println(licenseCode(trim));
//        System.out.println(licenseNumber(trim));
//        System.out.println(licenseEnterpriseName(trim));
//        System.out.println(licenseEnterpriseType(trim));
//        System.out.println(licenseRegisteredCapital(trim));
//        System.out.println(licenseEstablishDate(trim));
//        System.out.println(licenseOperationPeriod(trim)[0]+"至"+licenseOperationPeriod(trim)[1]);
    }
}
