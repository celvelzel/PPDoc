package com.majy.ppdocapi.utils.OCRUtils;

import com.majy.ppdocapi.service.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class IndictmentOcrUtils extends PaddleOcrUtils
{
    @Autowired
    private ModelService modelService;

    //全局变量
    private static final String NO_INFO_FOUND = "正则匹配未找到信息";

    /**
     * 将JSON格式数据处理成Map形式，用于存储OCR结果和LLM处理后的信息。
     *
     * @param jsons 包含OCR结果的JSON列表。
     * @return 返回一个Map，其中包含合并后的所有信息以及通过LLM处理得到的信息。
     */
    public Map<String, String> getStringStringMap(List<List> jsons)
    {
        // 将JSON列表转换为字符串，用于后续处理
        String trim = jsonToString(jsons);

        Map<String, String> indictmentInfoMap = new HashMap<>();

        // 将拼接后的所有信息存储到map中
        String allInfo = trim;
        indictmentInfoMap.put("allInfo", allInfo);

        // 调用LLM进行处理，并将结果添加到map中
        Map<String, String> invoiceLLMMap = invokeLLM(trim);
        indictmentInfoMap.putAll(invoiceLLMMap);

        return indictmentInfoMap;
    }


    /**
     * 调用智谱API，分析文本
     *
     * @param trim 拼接后的ocr识别的文字
     */
    public Map<String, String> invokeLLM(String trim)
    {
        String LLMResult = modelService.extractInfo("zhipuai", trim, "案件类型,原告名称,原告id（公民身份号码或统一认证代码）,原告类型（如果有性别就是‘个人’）,原告地址,原告联系方式,被告名称,被告id（公民身份号码或统一认证代码）,被告类型（如果有性别就是‘个人’）,被告地址,被告联系方式,诉讼请求,事实背景,法律依据,证据清单,法院名称,起诉状日期");
        //log.info("LLM结果是：" + LLMResult);

        //提取信息
        Map<String, String> invoiceLLMMap = new HashMap<>();
        String caseType = caseType(LLMResult);
        String plaintiffName = plaintiffName(LLMResult);
        String plaintiffId = plaintiffId(LLMResult);
        String plaintiffType = plaintiffType(LLMResult);
        String plaintiffAddress = plaintiffAddress(LLMResult);
        String plaintiffContact = plaintiffContact(LLMResult);
        String defendantName = defendantName(LLMResult);
        String defendantId = defendantId(LLMResult);
        String defendantType = defendantType(LLMResult);
        String defendantAddress = defendantAddress(LLMResult);
        String defendantContact = defendantContact(LLMResult);
        String litigationRequest = litigationRequest(LLMResult);
        String factsBackground = factsBackground(LLMResult);
        String legalBasis = legalBasis(LLMResult);
        String evidenceList = evidenceList(LLMResult);
        String courtName = courtName(LLMResult);
        String indictmentDate = indictmentDate(LLMResult);

        //放入map
        invoiceLLMMap.put("caseType", caseType);
        invoiceLLMMap.put("plaintiffName", plaintiffName);
        invoiceLLMMap.put("plaintiffId", plaintiffId);
        invoiceLLMMap.put("plaintiffType", plaintiffType);
        invoiceLLMMap.put("plaintiffAddress", plaintiffAddress);
        invoiceLLMMap.put("plaintiffContact", plaintiffContact);
        invoiceLLMMap.put("defendantName", defendantName);
        invoiceLLMMap.put("defendantId", defendantId);
        invoiceLLMMap.put("defendantType", defendantType);
        invoiceLLMMap.put("defendantAddress", defendantAddress);
        invoiceLLMMap.put("defendantContact", defendantContact);
        invoiceLLMMap.put("litigationRequest", litigationRequest);
        invoiceLLMMap.put("factsBackground", factsBackground);
        invoiceLLMMap.put("legalBasis", legalBasis);
        invoiceLLMMap.put("evidenceList", evidenceList);
        invoiceLLMMap.put("courtName", courtName);
        invoiceLLMMap.put("indictmentDate", indictmentDate);
        //返回Map
        return invoiceLLMMap;
    }

    private String caseType(String LLMResult)
    {
        String caseType = "";
        Pattern pattern = Pattern.compile("案件类型.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            caseType = matcher.group(1);
        }
        else
        {
            caseType = NO_INFO_FOUND;
        }
        return caseType;
    }

    private String plaintiffName(String LLMResult)
    {
        String plaintiffName = "";
        Pattern pattern = Pattern.compile("原告名称.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            plaintiffName = matcher.group(1);
        }
        else
        {
            plaintiffName = NO_INFO_FOUND;
        }
        return plaintiffName;
    }

    private String plaintiffId(String LLMResult)
    {
        String plaintiffId = "";
        Pattern pattern = Pattern.compile("原告id.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            plaintiffId = matcher.group(1);
        }
        else
        {
            plaintiffId = NO_INFO_FOUND;
        }
        return plaintiffId;
    }

    private String plaintiffType(String LLMResult)
    {
        String plaintiffType = "";
        Pattern pattern = Pattern.compile("原告类型.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            plaintiffType = matcher.group(1);
        }
        else
        {
            plaintiffType = NO_INFO_FOUND;
        }
        return plaintiffType;
    }

    private String plaintiffAddress(String LLMResult)
    {
        String plaintiffAddress = "";
        Pattern pattern = Pattern.compile("原告地址.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            plaintiffAddress = matcher.group(1);
        }
        else
        {
            plaintiffAddress = NO_INFO_FOUND;
        }
        return plaintiffAddress;
    }

    private String plaintiffContact(String LLMResult)
    {
        String plaintiffContact = "";
        Pattern pattern = Pattern.compile("原告联系方式.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            plaintiffContact = matcher.group(1);
        }
        else
        {
            plaintiffContact = NO_INFO_FOUND;
        }
        return plaintiffContact;
    }

    private String defendantName(String LLMResult)
    {
        String defendantName = "";
        Pattern pattern = Pattern.compile("被告名称.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            defendantName = matcher.group(1);
        }
        else
        {
            defendantName = NO_INFO_FOUND;
        }
        return defendantName;
    }

    private String defendantId(String LLMResult)
    {
        String defendantId = "";
        Pattern pattern = Pattern.compile("被告id.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            defendantId = matcher.group(1);
        }
        else
        {
            defendantId = NO_INFO_FOUND;
        }
        return defendantId;
    }

    private String defendantType(String LLMResult)
    {
        String defendantType = "";
        Pattern pattern = Pattern.compile("被告类型.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            defendantType = matcher.group(1);
        }
        else
        {
            defendantType = NO_INFO_FOUND;
        }
        return defendantType;
    }

    private String defendantAddress(String LLMResult)
    {
        String defendantAddress = "";
        Pattern pattern = Pattern.compile("被告地址.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            defendantAddress = matcher.group(1);
        }
        else
        {
            defendantAddress = NO_INFO_FOUND;
        }
        return defendantAddress;
    }

    private String defendantContact(String LLMResult)
    {
        String defendantContact = "";
        Pattern pattern = Pattern.compile("被告联系方式.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            defendantContact = matcher.group(1);
        }
        else
        {
            defendantContact = NO_INFO_FOUND;
        }
        return defendantContact;
    }

    private String litigationRequest(String LLMResult)
    {
        String litigationRequest = "";
        Pattern pattern = Pattern.compile("诉讼请求.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            litigationRequest = matcher.group(1);
        }
        else
        {
            litigationRequest = NO_INFO_FOUND;
        }
        return litigationRequest;
    }

    private String factsBackground(String LLMResult)
    {
        String factsBackground = "";
        Pattern pattern = Pattern.compile("事实背景.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            factsBackground = matcher.group(1);
        }
        else
        {
            factsBackground = NO_INFO_FOUND;
        }
        return factsBackground;
    }

    private String legalBasis(String LLMResult)
    {
        String legalBasis = "";
        Pattern pattern = Pattern.compile("法律依据.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            legalBasis = matcher.group(1);
        }
        else
        {
            legalBasis = NO_INFO_FOUND;
        }
        return legalBasis;
    }

    private String evidenceList(String LLMResult)
    {
        String evidenceList = "";
        Pattern pattern = Pattern.compile("证据清单.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            evidenceList = matcher.group(1);
        }
        else
        {
            evidenceList = NO_INFO_FOUND;
        }
        return evidenceList;
    }

    private String courtName(String LLMResult)
    {
        String courtName = "";
        Pattern pattern = Pattern.compile("法院名称.(.*)\\s");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            courtName = matcher.group(1);
        }
        else
        {
            courtName = NO_INFO_FOUND;
        }
        return courtName;
    }

    private String indictmentDate(String LLMResult)
    {
        String indictmentDate = "";
        Pattern pattern = Pattern.compile("起诉状日期.(\\d{4})年(\\d{1,2})月(\\d{1,2})日");
        Matcher matcher = pattern.matcher(LLMResult);
        if (matcher.find())
        {
            String year = matcher.group(1);
            String month = matcher.group(2);
            String day = matcher.group(3);
            indictmentDate = year + "-" + month + "-" + day;
        }
        else
        {
            indictmentDate = NO_INFO_FOUND;
        }
        return indictmentDate;
    }
}
