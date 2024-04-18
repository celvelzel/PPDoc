package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.service.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ModelController
{
    @Autowired
    ModelService modelService;

    String tempModelName = "zhipuai";

    @PostMapping("/extractinfo")
    public String extractInfo(@RequestBody String requestBody) throws Exception
    {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        log.info("前端传回的提取字段和OCR结果：{}", requestBody);
        // 创建一个 JSONObject 对象
        JSONObject jsonObject = new JSONObject(requestBody);

        //提取各个字段的值
        String fieldsValue = jsonObject.getString("fields");
        String ocrTextValue = jsonObject.getString("ocr_text");

        return modelService.extractInfo(tempModelName,ocrTextValue, fieldsValue);
    }

    @PostMapping("/generatesummary")
    public String zhiPuGenerateSummary(@RequestBody String requestBody)
    {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        log.info("前端传回的摘要选项和OCR结果：{}", requestBody);
        // 创建一个 JSONObject 对象
        JSONObject jsonObject = new JSONObject(requestBody);

        //提取各个字段的值
        String ocrTextValue = jsonObject.getString("ocr_text");
        String summaryOptionValue = jsonObject.getString("summary_option");

        return modelService.generateSummary(tempModelName,ocrTextValue, summaryOptionValue);
    }

    @PostMapping("/classification")
    public String zhiPuClassification(@RequestBody String requestBody)
    {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        //log.info("前端传回的OCR结果：{}", requestBody);
        // 创建一个 JSONObject 对象
        JSONObject jsonObject = new JSONObject(requestBody);

        //提取各个字段的值
        String ocrTextValue = jsonObject.getString("ocr_text");

        return modelService.classification(tempModelName,ocrTextValue);
    }
}
