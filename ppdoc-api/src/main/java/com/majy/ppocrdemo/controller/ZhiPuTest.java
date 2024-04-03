package com.majy.ppocrdemo.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.gson.JsonObject;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.embedding.EmbeddingApiResponse;
import com.zhipu.oapi.service.v4.embedding.EmbeddingRequest;
import com.zhipu.oapi.service.v4.file.FileApiResponse;
import com.zhipu.oapi.service.v4.file.QueryFileApiResponse;
import com.zhipu.oapi.service.v4.file.QueryFilesRequest;
import com.zhipu.oapi.service.v4.fine_turning.*;
import com.zhipu.oapi.service.v4.image.CreateImageRequest;
import com.zhipu.oapi.service.v4.image.ImageApiResponse;
import com.zhipu.oapi.service.v4.model.*;
import io.reactivex.Flowable;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class ZhiPuTest
{
    private static final String API_SECRET_KEY = "<REDACTED_MODEL_API_KEY>";

    private static final ClientV4 client = new ClientV4.Builder(API_SECRET_KEY).build();

    private static final ObjectMapper mapper = defaultObjectMapper();
    private JSON field;

    public static ObjectMapper defaultObjectMapper() {
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

    private static final Logger logger = LoggerFactory.getLogger(ZhiPuTest.class);

    //测试类
    public static void main(String[] args)
    {
        String ocrTextValue = "姓名马冀远性别男民族汉出生2013年05月06日住址湖南省长沙市开福区巡道街幸福小区居民组公民身份证号码430512198908131367";
        String fieldsValue = "[姓名],[性别]";
        String res = testSseInvoke(ocrTextValue,fieldsValue);

        // 正则表达式，用于匹配 JSON 对象
        String jsonRegex = "\\{\\s*[\"\\w\\s]*:[ \"'].*?[\"]\\s*[,}]\\s*\\}";
        Pattern pattern = Pattern.compile(jsonRegex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(res);

        // 查找所有匹配的 JSON 字符串
        while (matcher.find()) {
            // 构建完整的 JSON 字符串
            StringBuilder jsonStringBuilder = new StringBuilder();
            jsonStringBuilder.append("{");
            jsonStringBuilder.append(matcher.group(0));
            jsonStringBuilder.append("}");
            String jsonStr = jsonStringBuilder.toString();

            // 解析 JSON 字符串为 JSONObject
            JSONObject jsonObj = new JSONObject(jsonStr);

            // 输出解析后的 JSON 对象
            System.out.println("解析后的 JSON 对象: " + jsonObj.toString());
            break; // 如果只需要第一个 JSON 对象，可以取消注释并删除 break 语句
        }
    }

    @PostMapping("/extractinfo")
    public String zhiPuTest(@RequestBody String requestBody) throws Exception
    {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        // 1. sse-invoke调用模型，使用标准Listener，直接返回结果
        System.out.println("前端传回的提取字段和OCR结果："+requestBody);
        // 创建一个 JSONObject 对象
        JSONObject jsonObject = new JSONObject(requestBody);

        //提取各个字段的值
        String fieldsValue = jsonObject.getString("fields");
        String ocrTextValue = jsonObject.getString("ocr_text");


        String res = testSseInvoke(ocrTextValue,fieldsValue);
        return res;
    }

    /**
     * sse调用
     */
    private static String  testSseInvoke(String ocr_result, String keyInfo) {
        List<ChatMessage> messages = new ArrayList<>();

        /**
         * prompt
         */
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(),
                "你现在的任务是从OCR文字识别的结果中提取我指定的关键信息。" +
                        "\n" +
                        "OCR的文字识别结果使用符号包围，包含所识别出来的文字，顺序在原始图片中从左至右、从上至下。" +
                        "\n" +
                        "我指定的关键信息使用[]符号包围。请注意OCR的文字识别结果可能存在长句子换行被切断、不合理的分词、对应错位等问题，" +
                        "你需要结合上下文语义进行综合判断，以抽取准确的关键信息。" +
                        "\n" +
                        "在返回结果时使用文本格式，包含一个key-value对，key值为我指定的关键信息，value值为所抽取的结果。" +
                        "如果认为OCR识别结果中没有关键信息key，则将value赋值为“未找到相关信息”。 " +
                        "请只输出文本格式的结果，不要包含其它多余文字，不要使用markdown语法，不要用换行符等转义字符！" +
                        "输出示例是：姓名：张三" +
                        "下面正式开始：" +
                        "\n" +
                        "OCR文字："+ ocr_result +"要抽取的关键信息："+ keyInfo);
//      ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), "你能帮我查询2024年1月1日从北京南站到上海的火车票吗？");
        // ChatMessage对象存储用户的消息，并将其添加到消息列表中。

        messages.add(chatMessage);
        String requestId = String.format(requestIdTemplate, System.currentTimeMillis());//生成一个请求ID，用于标识这次API调用

        // 函数调用参数构建部分
        List<ChatTool> chatToolList = new ArrayList<>();
        ChatTool chatTool = new ChatTool();
        chatTool.setType(ChatToolType.FUNCTION.value());
        ChatFunctionParameters chatFunctionParameters = new ChatFunctionParameters();
        chatFunctionParameters.setType("object");
        Map<String, Object> properties = new HashMap<>();
        properties.put("departure", new HashMap<String, Object>() {{
            put("type", "string");
            put("description", "出发城市或车站");
        }});
        properties.put("destination", new HashMap<String, Object>() {{
            put("type", "string");
            put("description", "目的地城市或车站");
        }});
        properties.put("date", new HashMap<String, Object>() {{
            put("type", "string");
            put("description", "要查询的车次日期");
        }});
        List<String> required = new ArrayList<>();
        required.add("departure");
        required.add("destination");
        required.add("date");
        chatFunctionParameters.setProperties(properties);
        ChatFunction chatFunction = ChatFunction.builder()
                .name("query_train_info")
                .description("根据用户提供的信息，查询对应的车次")
                .parameters(chatFunctionParameters)
                .required(required)
                .build();
        chatTool.setFunction(chatFunction);
        chatToolList.add(chatTool);

        /* 构建一个ChatCompletionRequest对象，它包含了调用模型API所需的所有信息，
        如模型名称、是否使用流式响应、消息列表、请求ID、工具列表和工具选择策略。 */
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.TRUE)
                .messages(messages)
                .requestId(requestId)
                .tools(chatToolList)
                .toolChoice("auto")
                .build();


        /* 使用client.invokeModelApi方法调用模型API，并获取响应 */
        ModelApiResponse sseModelApiResp = client.invokeModelApi(chatCompletionRequest);


        /*如果响应成功，使用mapStreamToAccumulator方法处理响应流，并实时打印工具调用和内容。*/
        String res ="";
        StringBuilder sb = new StringBuilder();
        if (sseModelApiResp.isSuccess()) {
            AtomicBoolean isFirst = new AtomicBoolean(true);
            ChatMessageAccumulator chatMessageAccumulator = mapStreamToAccumulator(sseModelApiResp.getFlowable())
                    .doOnNext(accumulator -> {
                        {
                            if (isFirst.getAndSet(false)) {
                                System.out.print("Response: ");
                            }
                            if (accumulator.getDelta() != null && accumulator.getDelta().getTool_calls() != null) {
                                String jsonString = mapper.writeValueAsString(accumulator.getDelta().getTool_calls());
                                System.out.println("tool_calls: " + jsonString);
                            }
                            if (accumulator.getDelta() != null && accumulator.getDelta().getContent() != null) {
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

    public static Flowable<ChatMessageAccumulator> mapStreamToAccumulator(Flowable<ModelData> flowable) {
        return flowable.map(chunk -> {
            return new ChatMessageAccumulator(chunk.getChoices().get(0).getDelta(), null, chunk.getChoices().get(0), chunk.getUsage(), chunk.getCreated(), chunk.getId());
        });
    }
}
