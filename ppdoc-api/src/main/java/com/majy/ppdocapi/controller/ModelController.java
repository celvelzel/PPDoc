package com.majy.ppdocapi.controller;

import com.majy.ppdocapi.entity.po.Channel;
import com.majy.ppdocapi.service.ChannelService;
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
    @Autowired
    ChannelService channelService;


    @PostMapping("/api/extractinfo")
    public String extractInfo(@RequestBody String requestBody) throws Exception
    {
        log.info("前端传回的提取字段和OCR结果：{}", requestBody);
        // 创建一个 JSONObject 对象
        JSONObject jsonObject = new JSONObject(requestBody);

        //提取各个字段的值
        String fieldsValue = jsonObject.getString("fields");
        String ocrTextValue = jsonObject.getString("ocr_text");

        Channel channel = channelService.getChannelEnabled();
        return modelService.extractInfo(channel.getChannelType(), channel.getChannelModelName(), ocrTextValue, fieldsValue);
    }

    @PostMapping("/api/generatesummary")
    public String generateSummary(@RequestBody String requestBody)
    {
        log.info("前端传回的摘要选项和OCR结果：{}", requestBody);
        // 创建一个 JSONObject 对象
        JSONObject jsonObject = new JSONObject(requestBody);

        //提取各个字段的值
        String ocrTextValue = jsonObject.getString("ocr_text");
        String summaryOptionValue = jsonObject.getString("summary_option");

        Channel channel = channelService.getChannelEnabled();
        return modelService.generateSummary(channel.getChannelType(), channel.getChannelModelName(), ocrTextValue, summaryOptionValue);
    }

    @PostMapping("/api/classification")
    public String classification(@RequestBody String requestBody)
    {
        //log.info("前端传回的OCR结果：{}", requestBody);
        // 创建一个 JSONObject 对象
        JSONObject jsonObject = new JSONObject(requestBody);

        //提取各个字段的值
        String ocrTextValue = jsonObject.getString("ocr_text");

        Channel channel = channelService.getChannelEnabled();
        return modelService.classification(channel.getChannelType(), channel.getChannelModelName(), ocrTextValue);
    }
}
