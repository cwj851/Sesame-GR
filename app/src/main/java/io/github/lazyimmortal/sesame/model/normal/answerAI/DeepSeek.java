package io.github.lazyimmortal.sesame.model.normal.answerAI;

import org.json.JSONArray;
import org.json.JSONObject;

public class DeepSeek extends OpenAI {

    public DeepSeek(String apiKey, LargeLanguageModel largeLanguageModel) {
        super(apiKey, largeLanguageModel);
    }

    @Override
    String getRequestBody(String content) {
        try {
            return new JSONObject()
                    .put("model", getModel())
                    .put("messages", new JSONArray()
                            .put(new JSONObject("{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}"))
                            .put(new JSONObject("{\"role\": \"user\", \"content\": \"" + content + "\"}"))
                    )
                    .put("stream", false)
                    .toString();
        } catch (Throwable t) {
            return "";
        }
    }
}