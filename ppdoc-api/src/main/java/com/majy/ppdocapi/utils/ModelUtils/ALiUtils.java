package com.majy.ppdocapi.utils.ModelUtils;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MessageManager;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ALiUtils
{
    public static String invokeChat(String userPrompt)
    {
        MessageManager msgManager = new MessageManager(10);
        Message userMsg = Message.builder().role(Role.USER.getValue()).content(userPrompt).build();
        msgManager.add(userMsg);
        try
        {
            return callWithMessage(msgManager);
        } catch (NoApiKeyException e)
        {
            throw new RuntimeException(e);
        } catch (InputRequiredException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String invokeChat(String systemPrompt, String userPrompt)
    {
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg =
                Message.builder().role(Role.SYSTEM.getValue()).content(systemPrompt).build();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content(userPrompt).build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        try
        {
            return callWithMessage(msgManager);
        } catch (NoApiKeyException e)
        {
            throw new RuntimeException(e);
        } catch (InputRequiredException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String callWithMessage(MessageManager msgManager) throws NoApiKeyException, InputRequiredException
    {
        Generation gen = new Generation();
        QwenParam param =
                QwenParam.builder().model(Generation.Models.QWEN_TURBO).messages(msgManager.get())
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .build();
        GenerationResult result = gen.call(param);
        String resultText = result.getOutput().getChoices().get(0).getMessage().getContent();
        log.info("阿里通义大模型的返回消息是：{}", resultText);
        return resultText;
    }


    public static double getResponseTime(String modelName)
    {
        long startTime = System.currentTimeMillis();

        invokeChat("你好");

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("{}响应时间Response Time: {} ms", modelName, elapsedTime);

        return (double) elapsedTime / 1000;
    }


    public static void main(String[] args)
    {
        try
        {
            System.out.println("阿里通义大模型的响应内容Response Body: "+invokeChat("你好"));
        } catch (ApiException e)
        {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}

