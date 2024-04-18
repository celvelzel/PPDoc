package com.majy.ppdocapi.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class KimiUtils
{
    private static final String API_BASE_URL = "https://api.moonshot.cn/v1"; // 假设的API基础URL
    private static final String KIMI_API_KEY = "<REDACTED_MODEL_API_KEY>";

    /**
     * 发送一个GET请求到Moonshot API。
     * @return API响应的字符串
     * @throws IOException 如果网络请求出现问题
     */
    public static String getApiResponse() throws IOException
    {
        URL url = new URL(API_BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer "+ KIMI_API_KEY);
        connection.setDoOutput(true);
        //设置请求内容
        String requestBody = "";


        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNext()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        return response.toString();
    }

}
