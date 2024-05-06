package com.majy.ppdocapi.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.majy.ppdocapi.service.ModelService;
import com.majy.ppdocapi.utils.LLMUtils.BaiDuUTtils;
import com.majy.ppdocapi.utils.LLMUtils.KimiUtils;
import com.majy.ppdocapi.utils.LLMUtils.ZhiPuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ModelServiceImpl implements ModelService
{
    @Autowired
    private ZhiPuUtils zhiPuUtils;
    @Autowired
    private KimiUtils kimiUtils;
    @Autowired
    private BaiDuUTtils baiDuUTtils;

    @Override
    public String extractInfo(String modelName, String ocrResult, String keyInfo)
    {
        String systemPrompt = "你现在的任务是从OCR文字识别的结果中提取我指定的关键信息。" +
                "请注意OCR的文字识别结果可能存在长句子换行被切断、不合理的分词、对应错位等问题，" +
                "你需要结合上下文语义进行综合判断，以抽取准确的关键信息。" +
                "\n" +
                "OCR的文字识别结果使用符号<OCR>包围，包含所识别出来的文字，顺序在原始图片中从左至右、从上至下。" +
                "\n" +
                "我指定的关键信息使用<key_info>符号包围,多个关键信息之间由逗号分隔。" +
                "\n" +
                "在返回结果时使用文本格式，包含一个key-value对，key值为我指定的关键信息，value值为所抽取的结果。" +
                "如果认为OCR识别结果中没有关键信息key，则将value赋值为“无”。 " +
                "请只输出文本格式的结果，不要包含其它多余文字，不要使用markdown语法，不要用换行符等转义字符！" +
                "\n" +
                "输出示例是：姓名：张三";
        String userPrompt = "下面正式开始：" +
                "\n" +
                "<OCR>\n" + ocrResult + "</OCR>\n要抽取的关键信息：<key_info>\n" + keyInfo + "\n</key_info>";
        //ocr_text和keyInfo是前端传入的OCR识别结果文本和用户指定的关键词
        String finalPrompt = systemPrompt + userPrompt;
        if (modelName.equals("zhipu"))
        {
            return zhiPuUtils.sseInvokeChat(finalPrompt);
        }
        else if (modelName.equals("kimi"))
        {
            List<KimiUtils.Message> messages = CollUtil.newArrayList(
                    new KimiUtils.Message(KimiUtils.RoleEnum.system.name(), systemPrompt),
                    new KimiUtils.Message(KimiUtils.RoleEnum.user.name(), userPrompt)
            );
            return kimiUtils.invokeChat(messages);
        }
        else if (modelName.equals("baidu"))
        {
            return baiDuUTtils.invokeChatBySDK(systemPrompt, userPrompt);
        }
        log.info("提取信息错误：未找到对应的模型");
        return "提取信息错误：未找到对应的模型";
    }

    @Override
    public String generateSummary(String modelName, String ocrResult, String summaryType)
    {
        //摘要选项
        Map<String, String> summaryOptions = new HashMap<>();
        summaryOptions.put("1", "文档的核心信息或主题，一句话概括");
        summaryOptions.put("2", "文档的主要观点或事件摘要，包含2-3个关键信息点");
        summaryOptions.put("3", "文档内容的全面概述，包括主要事件、关键数据、重要结论或建议，以及任何附加的背景信息，组织成一段连贯的文本");
        summaryOptions.put("4", "文档的详细摘要，包括所有主要部分和子部分的概要，每个部分用一段来描述，确保包含所有关键信息和细节，同时保持摘要的连贯性和易读性");
        summaryOptions.put("5", "针对专业或商务文档的执行摘要，包括文档的目的、关键发现、分析、建议和结论，以及任何对决策者或执行人员重要的信息，通常以清晰、简洁的格式呈现");
        String summaryOption = summaryOptions.get(summaryType);

        String systemPrompt = "你现在的任务是根据用<OCR>标签OCR文字识别的结果生成一份摘要。请根据我用<requirement>标签包裹的要求总结文本的内容。" +
                "请注意OCR的文字识别结果可能存在长句子换行被切断、不合理的分词、对应错位等问题，但您应该尽力理解文档的主要内容并提取关键信息。" +
//                        "请确保您的摘要准确、清晰，并包含所有重要细节。" +
//                        "如果OCR结果中有任何不确定或缺失的信息，请在摘要中明确指出" +
                "\n" +
                "我给出的OCR识别结果文本使用<OCR>包围，包含所识别出来的文字，顺序在原始图片中从左至右、从上至下。" +
                "\n" +
                "在返回结果时 " +
                "请只输出文本格式的结果，不要包含其它多余文字，不要使用markdown语法，不要用换行符等转义字符！" +
                "我对你的返回结果的要求是：<requirement>\n" + summaryOption + "\n</requirement>";
        String userPrompt = "下面正式开始：" +
                "\n" +
                "OCR文字：<OCR>\n" + ocrResult + "\n</OCR>";
        String finalPrompt = systemPrompt + userPrompt;
        if (modelName.equals("zhipu"))
        {
            return zhiPuUtils.sseInvokeChat(finalPrompt);
        }
        else if (modelName.equals("kimi"))
        {
            List<KimiUtils.Message> messages = CollUtil.newArrayList(
                    new KimiUtils.Message(KimiUtils.RoleEnum.system.name(), systemPrompt),
                    new KimiUtils.Message(KimiUtils.RoleEnum.user.name(), userPrompt)
            );
            return kimiUtils.invokeChat(messages);
        }
        else if (modelName.equals("baidu"))
        {
            return baiDuUTtils.invokeChatBySDK(systemPrompt, userPrompt);
        }
        log.info("摘要生成错误：未找到对应的模型");
        return "摘要生成错误：未找到对应的模型";
    }

    @Override
    public String classification(String modelName, String ocrResult)
    {
        String systemPrompt = "您的任务是根据文档的OCR结果对文档进行分类和打标签。" +
                "OCR结果可能包含一些错误或不完整的文本，但您应该尽力理解文档的主要内容并提取关键信息。" +
                "请根据文档的内容和上下文，将其归入适当的分类，并为其添加相关标签。" +
                "如果OCR结果中有任何不确定或缺失的信息，请在分类和打标签时保持谨慎。\n" +
                "输入是用<OCR>标签包裹的OCR结果文本]\n" +
                "输出格式为：[文档分类] - [相关标签1, 标签2, 标签3, ...]\n";
        String userPrompt = "下面正式开始：" +
                "\n" +
                "OCR结果：<OCR>\n" + ocrResult + "\n</OCR>";
        String finalPrompt = systemPrompt + userPrompt;
        if (modelName.equals("zhipu"))
        {
            return zhiPuUtils.sseInvokeChat(finalPrompt);
        }
        else if (modelName.equals("kimi"))
        {
            List<KimiUtils.Message> messages = CollUtil.newArrayList(
                    new KimiUtils.Message(KimiUtils.RoleEnum.system.name(), systemPrompt),
                    new KimiUtils.Message(KimiUtils.RoleEnum.user.name(), userPrompt)
            );
            return kimiUtils.invokeChat(messages);
        }
        else if (modelName.equals("baidu"))
        {
            return baiDuUTtils.invokeChatBySDK(systemPrompt, userPrompt);
        }
        log.info("分类错误：未找到对应的模型");
        return "分类错误：未找到对应的模型";
    }
}
