package com.majy.ppdocapi.service.impl;

import com.majy.ppdocapi.service.ModelService;
import com.majy.ppdocapi.utils.ZhiPuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ModelServiceImpl implements ModelService
{
    @Override
    public String extractInfo(String modelName, String ocrResult, String keyInfo)
    {
        if (modelName.equals("zhipuai"))
        {
            return ZhiPuUtils.sseInvokeExtractInfo(ocrResult, keyInfo);
        }
        log.info("提取信息错误：未找到对应的模型");
        return "提取信息错误：未找到对应的模型";
    }

    @Override
    public String generateSummary(String modelName, String ocrResult, String summaryType)
    {
        if (modelName.equals("zhipuai"))
        {
            return ZhiPuUtils.sseInvokeGenerateSummary(ocrResult, summaryType);
        }
        log.info("摘要生成错误：未找到对应的模型");
        return "摘要生成错误：未找到对应的模型";
    }

    @Override
    public String classification(String modelName, String ocrResult)
    {
        if (modelName.equals("zhipuai"))
        {
            return ZhiPuUtils.sseInvokeClassification(ocrResult);
        }
        log.info("分类错误：未找到对应的模型");
        return "分类错误：未找到对应的模型";
    }
}
