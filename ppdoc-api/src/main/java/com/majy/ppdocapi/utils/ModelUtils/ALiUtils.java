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
import org.springframework.stereotype.Component;


@Component
public class ALiUtils
{
    public static void callWithMessage()
            throws NoApiKeyException, ApiException, InputRequiredException
    {
        Generation gen = new Generation();
        MessageManager msgManager = new MessageManager(10);
        Message systemMsg =
                Message.builder().role(Role.SYSTEM.getValue()).content("You are a helpful assistant.").build();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content("你好").build();
        msgManager.add(systemMsg);
        msgManager.add(userMsg);
        QwenParam param =
                QwenParam.builder().model(Generation.Models.QWEN_TURBO).messages(msgManager.get())
                        .resultFormat(QwenParam.ResultFormat.MESSAGE)
                        .build();
        GenerationResult result = gen.call(param);
        System.out.println(result);
    }


    public static void main(String[] args)
    {
        try
        {
            callWithMessage();
        } catch (ApiException | NoApiKeyException | InputRequiredException e)
        {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}

