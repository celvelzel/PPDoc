package com.majy.ppdocapi.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.majy.ppdocapi.utils.ZhiPuUtils;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
public class ZhiPuLLM
{
    @PostMapping("/extractinfo")
    public String zhiPuExtractInfo(@RequestBody String requestBody) throws Exception
    {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        log.info("前端传回的提取字段和OCR结果：{}", requestBody);
        // 创建一个 JSONObject 对象
        JSONObject jsonObject = new JSONObject(requestBody);

        //提取各个字段的值
        String fieldsValue = jsonObject.getString("fields");
        String ocrTextValue = jsonObject.getString("ocr_text");

        return ZhiPuUtils.sseInvokeExtractInfo(ocrTextValue, fieldsValue);
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

        return ZhiPuUtils.sseInvokeGenerateSummary(ocrTextValue, summaryOptionValue);
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

        return ZhiPuUtils.sseInvokeClassification(ocrTextValue);
    }
}
