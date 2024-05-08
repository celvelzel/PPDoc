package com.majy.ppdocapi.utils.ModelUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhipu.oapi.Constants;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class KimiUtils
{
    private static String API_KEY = "<REDACTED_MODEL_API_KEY>";
    private static final String MODELS_URL = "https://api.moonshot.cn/v1/models";
    private static final String FILES_URL = "https://api.moonshot.cn/v1/files";
    private static final String ESTIMATE_TOKEN_COUNT_URL = "https://api.moonshot.cn/v1/tokenizers/estimate-token-count";
    private static final String CHAT_COMPLETION_URL = "https://api.moonshot.cn/v1/chat/completions";
    // 使用的模型名称
//    文本生成模型 Moonshot-v1
//    模型	计费单位	价格
//    moonshot-v1-8k	1M tokens	¥12.00
//    moonshot-v1-32k	1M tokens	¥24.00
//    moonshot-v1-128k	1M tokens	¥60.00
    private static final String MODEL_NAME = "moonshot-v1-8k";

    public String commonChat(String prompt)
    {
        List<Message> messages = CollUtil.newArrayList(
                new Message(RoleEnum.user.name(), prompt)
        );
        return chat(MODEL_NAME, messages);
    }

    public String invokeChat(List<Message> messages)
    {
        return chat(MODEL_NAME, messages);
    }

    /**
     * 向指定模型发送消息并获取回应的聊天功能实现。
     *
     * @param model    指定的聊天模型名称，不可为空。
     * @param messages 要发送的消息列表，不可为空。
     *                 <p>
     *                 注意：此方法需要处理异常，通过@SneakyThrows注解直接抛出异常外，调用方需要处理可能的异常情况。
     */
    @SneakyThrows
    public String chat(@NonNull String model, @NonNull List<Message> messages)
    {
        // 构建请求体
        String requestBody = new JSONObject()
                .putOpt("model", model)
                .putOpt("messages", messages)
                .putOpt("stream", false)
                .toString();
        // 创建一个OkHttpClient实例，并设置超时时间
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)     // 读取超时时间
                .writeTimeout(20, TimeUnit.SECONDS)    // 写入超时时间
                .build();
        // 创建OkHttp请求
        Request okhttpRequest = new Request.Builder()
                .url(CHAT_COMPLETION_URL)
                .post(RequestBody.create(MediaType.get(ContentType.JSON.getValue()), requestBody))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();
        Call call = client.newCall(okhttpRequest);
        Response okhttpResponse = call.execute();
        // 打印响应内容
        String jsonStr = okhttpResponse.body().string();
        System.out.println("kimi返回的内容是" + jsonStr);
        // 使用Hutool解析JSON字符串为JSONObject
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);

        // 获取choices数组中的第一个元素
        JSONObject firstChoice = jsonObject.getJSONArray("choices").getJSONObject(0);

        // 从firstChoice中获取message对象
        JSONObject messageObj = firstChoice.getJSONObject("message");

        // 最后，从message对象中获取content字段的值
        String content = messageObj.getStr("content");

        System.out.println("kimi返回的Content: " + content);
        return content;
        // 读取响应
        //BufferedReader reader = new BufferedReader(okhttpResponse.body().charStream());
//        String line;
//        while ((line = reader.readLine()) != null)
//        {
//            if (StrUtil.isBlank(line))
//            {
//                continue;
//            }
//            // 处理错误信息
//            if (JSONUtil.isTypeJSON(line))
//            {
//                Optional.of(JSONUtil.parseObj(line))
//                        .map(x -> x.getJSONObject("error"))
//                        .map(x -> x.getStr("message"))
//                        .ifPresent(x -> System.out.println("error: " + x));
//                return;
//            }
//            // 移除数据前缀并检查是否完成或非JSON格式，若是则退出循环
//            line = StrUtil.replace(line, "data: ", StrUtil.EMPTY);
//            if (StrUtil.equals("[DONE]", line) || ! JSONUtil.isTypeJSON(line))
//            {
//                return;
//            }
//            // 解析并累加有效回复内容
//            Optional.of(JSONUtil.parseObj(line))
//                    .map(x -> x.getJSONArray("choices"))
//                    .filter(CollUtil::isNotEmpty)
//                    .map(x -> (JSONObject) x.get(0))
//                    .map(x -> x.getJSONObject("delta"))
//                    .map(x -> x.getStr("content"))
//                    .ifPresent(x -> sb.append(x));
//        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Message
    {

        private String role;

        private String content;

    }

    public enum RoleEnum
    {
        system,
        user,
        assistant;
    }

    @Test
    public void chatTest()
    {
        List<Message> messages = CollUtil.newArrayList(
                new Message(RoleEnum.system.name(), "你是kimi AI"),
                new Message(RoleEnum.user.name(), "请你介绍一下hutool")
        );
        System.out.println(invokeChat(messages));
    }

    public static double getResponseTime(String apiKey,String modelName)
    {
        long startTime = System.currentTimeMillis();

        // 带参数的响应时间测试
        String result = HttpRequest.post("https://api.moonshot.cn/v1/chat/completions")
                .header(Header.AUTHORIZATION, "Bearer " + apiKey)//头信息，多个头信息多次调用此方法即可
                .contentType("application/json")
                .body("{\"model\": \""+modelName+"\",\"messages\": [{\"role\": \"system\", \"content\": \"你好\"}]}")
                .timeout(20000)//超时，毫秒
                .execute().body();
        log.info("带参数的响应内容Response Body: {}", result);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        log.info("{}响应时间Response Time: {} ms", modelName, elapsedTime);

//        try (CloseableHttpClient httpClient = HttpClients.createDefault())
//        {
//            HttpGet httpGet = new HttpGet(url);
//            // 无参数的响应时间测试
//            CloseableHttpResponse response = httpClient.execute(httpGet);
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
        return (double) elapsedTime / 1000;
    }

    @Test
    public void getModelListTest()
    {
        System.out.println(getModelList());
    }

    @Test
    public void uploadFileTest()
    {
        System.out.println(uploadFile(FileUtil.file("/Users/celcelcel/Desktop/test.pdf")));
    }

    @Test
    public void getFileListTest()
    {
        System.out.println(getFileList());
    }

    @Test
    public void deleteFileTest()
    {
        System.out.println(deleteFile("co17orilnl9coc91noh0"));
        System.out.println(getFileList());
    }

    @Test
    public void getFileContentTest()
    {
        System.out.println(getFileContent("co18sokudu6bc6fqdhhg"));
    }

    @Test
    public void getFileDetailTest()
    {
        System.out.println(getFileDetail("co18sokudu6bc6fqdhhg"));
    }

    @Test
    public void estimateTokenCountTest()
    {
        List<Message> messages = CollUtil.newArrayList(
                new Message(RoleEnum.system.name(), "你是kimi AI"),
                new Message(RoleEnum.user.name(), "hello")
        );
        System.out.println(estimateTokenCount("moonshot-v1-8k", messages));
    }

    public String getModelList()
    {
        return getCommonRequest(MODELS_URL)
                .execute()
                .body();
    }

    public String uploadFile(@NonNull File file)
    {
        return getCommonRequest(FILES_URL)
                .method(Method.POST)
                .header("purpose", "file-extract")
                .form("file", file)
                .execute()
                .body();
    }

    public String getFileList()
    {
        return getCommonRequest(FILES_URL)
                .execute()
                .body();
    }

    public String deleteFile(@NonNull String fileId)
    {
        return getCommonRequest(FILES_URL + "/" + fileId)
                .method(Method.DELETE)
                .execute()
                .body();
    }

    public String getFileDetail(@NonNull String fileId)
    {
        return getCommonRequest(FILES_URL + "/" + fileId)
                .execute()
                .body();
    }

    public String getFileContent(@NonNull String fileId)
    {
        return getCommonRequest(FILES_URL + "/" + fileId + "/content")
                .execute()
                .body();
    }

    public String estimateTokenCount(@NonNull String model, @NonNull List<Message> messages)
    {
        String requestBody = new JSONObject()
                .putOpt("model", model)
                .putOpt("messages", messages)
                .toString();
        return getCommonRequest(ESTIMATE_TOKEN_COUNT_URL)
                .method(Method.POST)
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .body(requestBody)
                .execute()
                .body();
    }

    private HttpRequest getCommonRequest(@NonNull String url)
    {
        return HttpRequest.of(url).header(Header.AUTHORIZATION, "Bearer " + API_KEY);
    }
}
