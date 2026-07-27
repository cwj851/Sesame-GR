package io.github.lazyimmortal.sesame.model.normal.answerAI;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.StringUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Gemini implements AnswerAIInterface {
    private final String TAG = this.getClass().getSimpleName();
    private final String baseUrl;
    private final String apiKey;
    private final String model;

    public Gemini(String apiKey, LargeLanguageModel largeLanguageModel) {
        this.baseUrl = largeLanguageModel.baseUrl;
        this.apiKey = StringUtil.isEmpty(apiKey) ? "" : apiKey;
        this.model = largeLanguageModel.model;
    }

    protected Request buildRequest(String content) {
        String contentType = "application/json";
        RequestBody body = RequestBody.create(
                getRequestBody(content),
                MediaType.parse(contentType)
        );
        return new Request.Builder()
                .url(baseUrl + "/v1beta/models/" + model + ":generateContent?key=" + apiKey)
                .method("POST", body)
                .addHeader("Content-Type", contentType)
                .build();
    }

    String getRequestBody(String content) {
        return "{\"contents\":[{\"parts\":[{\"text\":\"" + content + "\"}]}]}";
    }

    public String getResponseBody(String content) {
        String result = "";
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
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
                result = JsonUtil.getValueByPath(new JSONObject(json), "candidates.[0].content.parts.[0].text");
            }
        } catch (Throwable t) {
            Log.i(TAG, "getResponseContent error");
            Log.printStackTrace(TAG, t);
        }
        Log.chat(TAG, result);
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
