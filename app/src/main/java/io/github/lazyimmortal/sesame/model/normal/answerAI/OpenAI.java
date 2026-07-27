package io.github.lazyimmortal.sesame.model.normal.answerAI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.github.lazyimmortal.sesame.entity.LLMEntity;
import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.StringUtil;
import lombok.Getter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Getter
public class OpenAI implements AnswerAIInterface {
    private final String TAG = this.getClass().getSimpleName();
    private final String baseUrl;
    private final String apiKey;
    private final String model;
    private final String name;

    public OpenAI(String baseUrl, String apiKey, String model, String name) {
        this.baseUrl = baseUrl;
        this.apiKey = StringUtil.isEmpty(apiKey) ? "" : apiKey;
        this.model = model;
        this.name = StringUtil.isEmpty(name) ? TAG : name;
    }

    public OpenAI(String baseUrl, String apiKey, String model) {
        this(baseUrl, apiKey, model, null);
    }

    public OpenAI(String apiKey, LargeLanguageModel largeLanguageModel) {
        this(largeLanguageModel.baseUrl, apiKey, largeLanguageModel.model, largeLanguageModel.nickName);
    }

    public OpenAI(LLMEntity entity) {
        this(entity.getBaseUrl(), entity.getApiKey(), entity.getModel(), entity.getName());
    }

    String getRequestBody(String content) {
        try {
            return new JSONObject()
                    .put("model", model)
                    .put("messages", new JSONArray()
                            .put(new JSONObject("{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}"))
                            .put(new JSONObject("{\"role\": \"user\", \"content\": \"" + content + "\"}"))
                    )
                    .toString();
        } catch (Throwable t) {
            return "";
        }
    }

    protected Request buildRequest(String content) {
        String contentType = "application/json";
        RequestBody body = RequestBody.create(
                getRequestBody(content),
                MediaType.parse(contentType)
        );
        return new Request.Builder()
                .url(baseUrl + "/chat/completions")
                .method("POST", body)
                .addHeader("Content-Type", contentType)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public String getResponseBody(String content) {
        String result = "";
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .build();
            try (Response response = client.newCall(buildRequest(content)).execute()) {
                if (response.body() == null) {
                    return result;
                }
                String json = response.body().string();
                if (!response.isSuccessful()) {
                    Log.other(TAG + "请求失败");
                    Log.i(TAG, "接口异常:" + json);
                    return result;
                }
                result = json;
            }
        } catch (Throwable t) {
            Log.i(TAG, "getResponseBody error");
            Log.printStackTrace(TAG, t);
        }
        return result;
    }

    protected String getResponseContent(String content) {
        Log.chat("User", content);
        String result = "";
        try {
            String json = getResponseBody(content);
            if (!json.isEmpty()) {
                result = JsonUtil.getValueByPath(new JSONObject(json), "choices.[0].message.content");
            }
        } catch (Throwable t) {
            Log.i(TAG, "getResponseContent error");
            Log.printStackTrace(TAG, t);
        }
        Log.chat(name, result);
        return result;
    }

    /**
     * 获取AI回答结果
     *
     * @param text 问题内容
     * @return AI回答结果
     */
    @Override
    public String getAnswerStr(String text) {
        return getResponseContent(text);
    }
}
