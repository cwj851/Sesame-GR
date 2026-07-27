package io.github.lazyimmortal.sesame.model.normal.answerAI;

import java.util.List;
import java.util.Objects;

import io.github.lazyimmortal.sesame.entity.idAndName.AnswerLLM;
import io.github.lazyimmortal.sesame.util.idMap.LLMIdMap;

public enum LargeLanguageModel {
    Qwen_Max("Qwen Max", "https://dashscope.aliyuncs.com/compatible-mode/v1", "qwen-max"),
    Qwen_Plus("Qwen Plus", "https://dashscope.aliyuncs.com/compatible-mode/v1", "qwen-plus"),
    Qwen_Turbo("Qwen Turbo", "https://dashscope.aliyuncs.com/compatible-mode/v1", "qwen-turbo"),
    Qwen_Long("Qwen Long", "https://dashscope.aliyuncs.com/compatible-mode/v1", "qwen-long"),
    DeepSeek_V3("DeepSeek V3", "https://api.deepseek.com/v1", "deepseek-chat"),
    DeepSeek_R1("DeepSeek R1", "https://api.deepseek.com/v1", "deepseek-reasoner"),
    Gemini_1_5_Flash("Gemini 1.5 Flash", "https://api.genai.gd.edu.kg/google", "gemini-1.5-flash"),
    Gemini_2_0_Flash("Gemini 2.0 Flash", "https://api.genai.gd.edu.kg/google", "gemini-2.0-flash");

    public final String nickName;
    public final String baseUrl;
    public final String model;

    LargeLanguageModel(String nickName, String baseUrl, String model) {
        this.nickName = nickName;
        this.baseUrl = baseUrl;
        this.model = model;
    }

    public static List<AnswerLLM> getList() {
        LLMIdMap.getInstance().load();
        List<AnswerLLM> list = AnswerLLM.getList();
        for (LargeLanguageModel largeLanguageModel : LargeLanguageModel.values()) {
            list.add(new AnswerLLM(largeLanguageModel.model, "内置大模型:" + largeLanguageModel.nickName));
        }
        return list;
    }

    public static LargeLanguageModel getInstance(String model) {
        for (LargeLanguageModel largeLanguageModel : LargeLanguageModel.values()) {
            if (Objects.equals(largeLanguageModel.model, model)) {
                return largeLanguageModel;
            }
        }
        return null;
    }
}
