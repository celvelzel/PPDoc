package com.majy.ppdocapi.utils.ModelUtils;

import cn.hutool.json.JSONUtil;
import com.baidubce.qianfan.Qianfan;
import com.baidubce.qianfan.core.auth.Auth;
import com.baidubce.qianfan.model.chat.ChatResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

//        "expires_in":2592000,
//        "scope":"public brain_all_scope wenxinworkshop_mgr ai_custom_yiyan_com ai_custom_yiyan_com_eb_instant ai_custom_yiyan_com_bloomz7b1 ai_custom_yiyan_com_emb_text ai_custom_yiyan_com_llama2_7b ai_custom_yiyan_com_llama2_13b ai_custom_yiyan_com_llama2_70b ai_custom_yiyan_com_chatglm2_6b_32k ai_custom_yiyan_com_aquilachat_7b ai_custom_yiyan_com_emb_bge_large_zh ai_custom_yiyan_com_emb_bge_large_en ai_custom_yiyan_com_qianfan_chinese_llama_2_7b ai_custom_qianfan_bloomz_7b_compressed ai_custom_yiyan_com_eb_pro ai_custom_yiyan_com_sd_xl ai_custom_yiyan_com_tokenizer_eb ai_custom_yiyan_com_ai_apaas ai_custom_yiyan_com_qf_chinese_llama_2_13b ai_custom_yiyan_com_sqlcoder_7b ai_custom_yiyan_com_codellama_7b_ins ai_custom_yiyan_com_xuanyuan_70b_chat ai_custom_yiyan_com_yi_34b ai_custom_yiyan_com_chatlaw ai_custom_yiyan_com_emb_tao_8k ai_custom_yiyan_com_eb_turbo_pro ai_custom_yiyan_com_mixtral_8x7b ai_custom_yiyan_com_prmtv ai_custom_yiyan_com_eb_pro_prmtv ai_custom_yiyan_com_eb_turbo_pro_128k ai_custom_yiyan_com_ernie_35_4k_0205 ai_custom_yiyan_com_ernie_35_8k_0205 ai_custom_yiyan_com_ernie_35_8k_1222 ai_custom_yiyan_com_ernie_lite_8k ai_custom_yiyan_com_gemma_7b_it ai_custom_yiyan_com_bce_reranker_base ai_custom_yiyan_com_fuyu_8b ai_custom_yiyan_com_ernie_tiny_8k ai_custom_yiyan_com_ernie_char_8k ai_custom_yiyan_com_ernie_35_8k_preview ai_custom_yiyan_com_ernie_40_8k_preview ai_custom_yiyan_com_llama3_8b ai_custom_yiyan_com_llama3_70b wise_adapt lebo_resource_base lightservice_public hetu_basic lightcms_map_poi kaidian_kaidian ApsMisTest_Test权限 vis-classify_flower lpq_开放 cop_helloScope ApsMis_fangdi_permission smartapp_snsapi_base smartapp_mapp_dev_manage iop_autocar oauth_tp_app smartapp_smart_game_openapi oauth_sessionkey smartapp_swanid_verify smartapp_opensource_openapi smartapp_opensource_recapi fake_face_detect_开放Scope vis-ocr_虚拟人物助理 idl-video_虚拟人物助理 smartapp_component smartapp_search_plugin avatar_video_test b2b_tp_openapi b2b_tp_openapi_online smartapp_gov_aladin_to_xcx",

/**
 * 百度文心开放平台-对话机器人-文本交互式问答
 * 流式响应式接口
 */
@Component
@Slf4j
public class BaiDuUTtils
{

    @Value("${baidu.api-key}")
    private String apiKey;

    @Value("${baidu.secret-key}")
    private String secretKey;

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().readTimeout(30, TimeUnit.SECONDS).build();

    public String invokeChatBySDK(String systemPrompt, String userPrompt)
    {
        ChatResponse response = new Qianfan(Auth.TYPE_OAUTH, apiKey, secretKey).chatCompletion()
                .model("ERNIE-Bot") // 使用model指定预置模型
                // .endpoint("completions_pro") // 也可以使用endpoint指定任意模型 (二选一)
                .addMessage("system", systemPrompt)
                .addMessage("user", userPrompt) // 添加用户消息 (此方法可以调用多次，以实现多轮对话的消息传递)
                .temperature(0.7) // 自定义超参数
                .execute(); // 发起请求
        log.info("baidu返回的内容是:" + response.toString());
        return response.getResult();
    }

    public String invokeChatBySDK(String prompt)
    {
        ChatResponse response = new Qianfan(Auth.TYPE_OAUTH, apiKey, secretKey).chatCompletion()
                .model("ERNIE-Bot") // 使用model指定预置模型
                // .endpoint("completions_pro") // 也可以使用endpoint指定任意模型 (二选一)
                .addMessage("user", prompt) // 添加用户消息 (此方法可以调用多次，以实现多轮对话的消息传递)
                .temperature(0.7) // 自定义超参数
                .execute(); // 发起请求
        log.info("baidu返回的内容是:" + response.toString());
        return response.getResult();
    }

    public static double getResponseTime(String apiKey, String secretKey, String modelName)
    {
        long startTime = System.currentTimeMillis();

        ChatResponse response = new Qianfan(Auth.TYPE_OAUTH, apiKey, secretKey).chatCompletion()
                .model(modelName) // 使用model指定预置模型
                // .endpoint("completions_pro") // 也可以使用endpoint指定任意模型 (二选一)
                .addMessage("user", "你好") // 添加用户消息 (此方法可以调用多次，以实现多轮对话的消息传递)
                .temperature(0.7) // 自定义超参数
                .execute(); // 发起请求
        long endTime = System.currentTimeMillis();
        log.info("baidu返回的内容是:" + response.toString());
        long elapsedTime = endTime - startTime;
        log.info("baidu响应时间是:{}ms", elapsedTime);

        return (double) elapsedTime / 1000;
    }

    //ERNIE-3.5-8K
    @SneakyThrows
    public String invokeChat(String prompt)
    {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"user_id\":\"MaJY\",\"messages\":[{\"role\":\"user\",\"content\":\"" + prompt + "\"}],\"temperature\":0.95,\"top_p\":0.8,\"penalty_score\":1,\"disable_search\":false,\"enable_citation\":false,\"response_format\":\"text\"}");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        String jsonStr = response.body().string();
        log.info("baidu返回的内容是" + jsonStr);

        // 使用Hutool解析JSON字符串为JSONObject
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        // 从返回结果json对象中获取result字段的值
        String content = jsonObject.getStr("result");

        log.info("baidu返回的Content: " + content);
        return content;
    }

    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    String getAccessToken() throws IOException
    {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + apiKey
                + "&client_secret=" + secretKey);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }

}
