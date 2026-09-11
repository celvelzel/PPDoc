package com.majy.ppdocapi.utils.ModelUtils;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class ZhiPuUtils
{
    @Value("${zhipu.api-key}")
    private String apiSecretKey;

    private ClientV4 client;

    @PostConstruct
    public void initClient()
    {
        client = new ClientV4.Builder(apiSecretKey).build();
    }

    private static final ObjectMapper mapper = defaultObjectMapper();

    public static ObjectMapper defaultObjectMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.addMixIn(ChatFunction.class, ChatFunctionMixIn.class);
        mapper.addMixIn(ChatCompletionRequest.class, ChatCompletionRequestMixIn.class);
        mapper.addMixIn(ChatFunctionCall.class, ChatFunctionCallMixIn.class);
        return mapper;
    }

    // 请自定义自己的业务id
    private static final String requestIdTemplate = "MaJY_3120005347";

    public String sseInvokeChat(String prompt)
    {
        List<ChatMessage> messages = new ArrayList<>();
        // ChatMessage对象存储用户的消息，并将其添加到消息列表中。
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        //ocr_text和keyInfo是前端传入的OCR识别结果文本和用户指定的关键词

        messages.add(chatMessage);
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());//生成一个请求ID，用于标识这次API调用


        /* 构建一个ChatCompletionRequest对象，它包含了调用模型API所需的所有信息，
        如模型名称、是否使用流式响应、消息列表、请求ID、工具列表和工具选择策略。 */
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4) //ModelChatGLM4、ModelChatGLM3TURBO
                .stream(Boolean.TRUE)
                .messages(messages)
                .requestId(requestId)
//                .tools(chatToolList) //删除工具列表，不传递任何工具信息
                .toolChoice("auto")
                .build();


        /* 使用client.invokeModelApi方法调用模型API，并获取响应 */
        ModelApiResponse sseModelApiResp = client.invokeModelApi(chatCompletionRequest);


        /*如果响应成功，使用mapStreamToAccumulator方法处理响应流，并实时打印工具调用和内容。*/
        String res = "";
        StringBuilder sb = new StringBuilder();
        if (sseModelApiResp.isSuccess())
        {
            AtomicBoolean isFirst = new AtomicBoolean(true);
            ChatMessageAccumulator chatMessageAccumulator = mapStreamToAccumulator(sseModelApiResp.getFlowable())
                    .doOnNext(accumulator ->
                    {
                        {
                            if (isFirst.getAndSet(false))
                            {
                                System.out.print("Response: ");
                            }
                            if (accumulator.getDelta() != null && accumulator.getDelta().getTool_calls() != null)
                            {
                                String jsonString = mapper.writeValueAsString(accumulator.getDelta().getTool_calls());
                                System.out.println("tool_calls: " + jsonString);
                            }
                            if (accumulator.getDelta() != null && accumulator.getDelta().getContent() != null)
                            {
                                System.out.print(accumulator.getDelta().getContent());
                                sb.append(accumulator.getDelta().getContent());
                            }
                        }
                    })
                    .doOnComplete(System.out::println)
                    .lastElement()
                    .blockingGet();

            //当响应完成时，创建一个Choice对象，并将其添加到choices列表中
            Choice choice = new Choice(chatMessageAccumulator.getChoice().getFinishReason(), 0L, chatMessageAccumulator.getDelta());
            List<Choice> choices = new ArrayList<>();
            choices.add(choice);
            //创建一个ModelData对象，并将其设置为响应数据
            ModelData data = new ModelData();
            data.setChoices(choices);
            data.setUsage(chatMessageAccumulator.getUsage());
            data.setId(chatMessageAccumulator.getId());
            data.setCreated(chatMessageAccumulator.getCreated());
            data.setRequestId(chatCompletionRequest.getRequestId());
            sseModelApiResp.setFlowable(null);
            sseModelApiResp.setData(data);
        }
        res = sb.toString();
        return res;
    }

    @Test
    public void chatTest()
    {
        String prompt = "你好";
        String res = sseInvokeChat(prompt);
        System.out.println(res);
    }


    public static Flowable<ChatMessageAccumulator> mapStreamToAccumulator(Flowable<ModelData> flowable)
    {
        return flowable.map(chunk ->
        {
            return new ChatMessageAccumulator(chunk.getChoices().get(0).getDelta(), null, chunk.getChoices().get(0), chunk.getUsage(), chunk.getCreated(), chunk.getId());
        });
    }

    public static double getResponseTime(String apiKey, String modelName)
    {
        if (modelName.equals("GLM-4"))
        {
            long startTime = System.currentTimeMillis();
            // 带参数的响应时间测试
            String result = HttpRequest.post("https://open.bigmodel.cn/api/paas/v4/chat/completions")
                    .header(Header.AUTHORIZATION, "Bearer " + apiKey)//头信息，多个头信息多次调用此方法即可
                    .contentType("application/json")
                    .body("{\"model\":\"" + Constants.ModelChatGLM4 + "\",\"messages\":[{\"role\":\"user\",\"content\":\"你好\"}]}")
                    .timeout(20000)//超时，毫秒
                    .execute().body();
            log.info("带参数的响应内容Response Body: {}", result);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("{}响应时间Response Time: {} ms", modelName, elapsedTime);
            return (double) elapsedTime / 1000;
        }
        else if (modelName.equals("GLM-3"))
        {
            long startTime = System.currentTimeMillis();
            // 带参数的响应时间测试
            String result = HttpRequest.post("https://open.bigmodel.cn/api/paas/v4/chat/completions")
                    .header(Header.AUTHORIZATION, "Bearer " + apiKey)//头信息，多个头信息多次调用此方法即可
                    .contentType("application/json")
                    .body("{\"model\":\"" + Constants.ModelChatGLM3TURBO + "\",\"messages\":[{\"role\":\"user\",\"content\":\"你好\"}]}")
                    .timeout(20000)//超时，毫秒
                    .execute().body();
            log.info("带参数的响应内容Response Body: {}", result);
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            log.info("{}响应时间Response Time: {} ms", modelName, elapsedTime);
            return (double) elapsedTime / 1000;
        }
        // 默认响应时间
        return 12.12;

//        try (CloseableHttpClient httpClient = HttpClients.createDefault())
//        {
//            HttpGet httpGet = new HttpGet(url);
//            // 无参数的响应时间测试
//            CloseableHttpResponse response = httpClient.execute(httpGet);
//
//            try
//            {
//                // 确保读取完整的响应内容，以便计算完整的响应时间
//                String responseBody = EntityUtils.toString(response.getEntity());
//                log.info("不带参数的响应内容Response Body: {}", responseBody);
//
//                long endTime = System.currentTimeMillis();
//                long elapsedTime = endTime - startTime;
//                log.info("响应时间Response Time: {} ms", elapsedTime);
//                return elapsedTime;
//            } finally
//            {
//                response.close();
//            }
//        } catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        return elapsedTime;
    }

    /*
    * 调用ChatGLM-3-Turbo模型进行对话
     */
    public String sseInvokeChatGLM3Turbo(String prompt)
    {
        List<ChatMessage> messages = new ArrayList<>();
        // ChatMessage对象存储用户的消息，并将其添加到消息列表中。
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        //ocr_text和keyInfo是前端传入的OCR识别结果文本和用户指定的关键词

        messages.add(chatMessage);
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());//生成一个请求ID，用于标识这次API调用


        /* 构建一个ChatCompletionRequest对象，它包含了调用模型API所需的所有信息，
        如模型名称、是否使用流式响应、消息列表、请求ID、工具列表和工具选择策略。 */
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM3TURBO) //ModelChatGLM4、ModelChatGLM3TURBO
                .stream(Boolean.TRUE)
                .messages(messages)
                .requestId(requestId)
//                .tools(chatToolList) //删除工具列表，不传递任何工具信息
                .toolChoice("auto")
                .build();


        /* 使用client.invokeModelApi方法调用模型API，并获取响应 */
        ModelApiResponse sseModelApiResp = client.invokeModelApi(chatCompletionRequest);


        /*如果响应成功，使用mapStreamToAccumulator方法处理响应流，并实时打印工具调用和内容。*/
        String res = "";
        StringBuilder sb = new StringBuilder();
        if (sseModelApiResp.isSuccess())
        {
            AtomicBoolean isFirst = new AtomicBoolean(true);
            ChatMessageAccumulator chatMessageAccumulator = mapStreamToAccumulator(sseModelApiResp.getFlowable())
                    .doOnNext(accumulator ->
                    {
                        {
                            if (isFirst.getAndSet(false))
                            {
                                System.out.print("Response: ");
                            }
                            if (accumulator.getDelta() != null && accumulator.getDelta().getTool_calls() != null)
                            {
                                String jsonString = mapper.writeValueAsString(accumulator.getDelta().getTool_calls());
                                System.out.println("tool_calls: " + jsonString);
                            }
                            if (accumulator.getDelta() != null && accumulator.getDelta().getContent() != null)
                            {
                                System.out.print(accumulator.getDelta().getContent());
                                sb.append(accumulator.getDelta().getContent());
                            }
                        }
                    })
                    .doOnComplete(System.out::println)
                    .lastElement()
                    .blockingGet();

            //当响应完成时，创建一个Choice对象，并将其添加到choices列表中
            Choice choice = new Choice(chatMessageAccumulator.getChoice().getFinishReason(), 0L, chatMessageAccumulator.getDelta());
            List<Choice> choices = new ArrayList<>();
            choices.add(choice);
            //创建一个ModelData对象，并将其设置为响应数据
            ModelData data = new ModelData();
            data.setChoices(choices);
            data.setUsage(chatMessageAccumulator.getUsage());
            data.setId(chatMessageAccumulator.getId());
            data.setCreated(chatMessageAccumulator.getCreated());
            data.setRequestId(chatCompletionRequest.getRequestId());
            sseModelApiResp.setFlowable(null);
            sseModelApiResp.setData(data);
        }
        res = sb.toString();
        return res;
    }
}
