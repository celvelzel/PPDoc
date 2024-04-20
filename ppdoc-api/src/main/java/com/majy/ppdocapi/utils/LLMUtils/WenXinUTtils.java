package com.majy.ppdocapi.utils.LLMUtils;

import okhttp3.*;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;


/**
 * 百度文心开放平台-对话机器人-文本交互式问答
 * 流式响应式接口
 */
public class WenXinUTtils
{

    public static final String API_KEY = "<REDACTED_SECRET>";
    public static final String SECRET_KEY = "<REDACTED_SECRET>";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static void main(String[] args) throws IOException
    {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\n" +
                        "  \"messages\": [\n" +
                        "    {\"role\":\"user\", \"content\": \"给我推荐一些广东附近的旅游好去处\"}\n" +
                        "  ],\n" +
                        "  \"stream\": false\n" +
                        "}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
//            Response response = HTTP_CLIENT.newCall(request).execute();
//            System.out.println(response.body().string());

        // 流式响应式接口
        Response response = HTTP_CLIENT.newCall(request).execute();
        try (ResponseBody responseBody = response.body())
        {
            if (responseBody != null)
            {
                try (BufferedReader reader = new BufferedReader(responseBody.charStream()))
                {
                    String line;
                    while ((line = reader.readLine()) != null)
                    {
                        System.out.println(line);
                    }
                }
            }
        }
    }

    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }
}
