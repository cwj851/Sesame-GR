package io.github.lazyimmortal.sesame.model.normal.answerAI;

import java.util.List;
import java.util.Objects;

import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectOneModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.StringModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.TextModelField;
import io.github.lazyimmortal.sesame.entity.LLMEntity;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.RandomUtil;
import io.github.lazyimmortal.sesame.util.Statistics;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.idMap.LLMIdMap;

public class AnswerAI extends Model {

    private static final String TAG = AnswerAI.class.getSimpleName();

    @Override
    public String getName() {
        return "AI答题";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.OTHER;
    }

    private static AnswerAIInterface answerAIInterface;
    private final SelectOneModelField largeLanguageModelList = new SelectOneModelField("largeLanguageModelList", "大模型 | 启用列表", null, LargeLanguageModel::getList);
    private final TextModelField.UrlTextModelField getQwenApiKey = new TextModelField.UrlTextModelField("getQwenApiKey", "Qwen | 获取令牌", "https://help.aliyun.com/zh/model-studio/developer-reference/get-api-key");
    private final StringModelField setQwenApiKey = new StringModelField("setTongyiAIToken", "Qwen | 设置令牌", "");
    private final TextModelField.UrlTextModelField getGeminiApiKey = new TextModelField.UrlTextModelField("getGeminiAIToken", "Gemini | 获取令牌", "https://aistudio.google.com/app/apikey");
    private final StringModelField setGeminiApiKey = new StringModelField("useGeminiAIToken", "Gemini | 设置令牌", "");
    private final TextModelField.UrlTextModelField getDeepSeekApiKey = new TextModelField.UrlTextModelField("getDeepSeekApiKey", "DeepSeek | 获取令牌", "https://platform.deepseek.com/api_keys");
    private final StringModelField setDeepSeekApiKey = new StringModelField("setDeepSeekApiKey", "DeepSeek | 设置令牌", "");

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(largeLanguageModelList);
        modelFields.addField(getQwenApiKey);
        modelFields.addField(setQwenApiKey);
        modelFields.addField(getGeminiApiKey);
        modelFields.addField(setGeminiApiKey);
        modelFields.addField(getDeepSeekApiKey);
        modelFields.addField(setDeepSeekApiKey);
        return modelFields;
    }

    @Override
    public void boot(ClassLoader classLoader) {
        if (!isEnable()) {
            return;
        }
        String model = largeLanguageModelList.getValue();
        if (StringUtil.isEmpty(model)) {
            // 未选择任何模型:实例化一个复读机
            answerAIInterface = AnswerAIInterface.getInstance();
            return;
        }
        LargeLanguageModel largeLanguageModel = LargeLanguageModel.getInstance(model);
        if (largeLanguageModel == null) {
            // 未选择任何内置大模型
            LLMIdMap.getInstance().load();
            LLMEntity entity = LLMIdMap.getInstance().get(model);
            if (entity != null) {
                answerAIInterface = new OpenAI(entity);
                return;
            }
            // 不包含在自定义大模型中:实例化一个复读机
            Log.record("不支持的大模型:" + model);
            answerAIInterface = AnswerAIInterface.getInstance();
            return;
        }
        String apiKey = null;
        switch (largeLanguageModel) {
            case Qwen_Max:
            case Qwen_Plus:
            case Qwen_Turbo:
            case Qwen_Long:
                apiKey = setQwenApiKey.getValue();
                answerAIInterface = new Qwen(apiKey, largeLanguageModel);
                break;
            case Gemini_1_5_Flash:
            case Gemini_2_0_Flash:
                apiKey = setGeminiApiKey.getValue();
                answerAIInterface = new Gemini(apiKey, largeLanguageModel);
                break;
            case DeepSeek_V3:
            case DeepSeek_R1:
                apiKey = setDeepSeekApiKey.getValue();
                answerAIInterface = new DeepSeek(apiKey, largeLanguageModel);
                break;
        }
        if (!StringUtil.isEmpty(apiKey)) {
            return;
        }
        // 选了内置大模型但未填写apiKey:实例化一个复读机
        answerAIInterface = AnswerAIInterface.getInstance();
    }

    /**
     * 获取AI回答结果
     *
     * @param text 问题内容
     * @return AI回答结果
     */
    public static String getAnswer(String text) {
        try {
            if (answerAIInterface != null) {
                Log.record("AI🧠答题，问题：[" + text + "]");
                return answerAIInterface.getAnswerStr(text);
            } else {
                Log.record("开始答题，问题：[" + text + "]");
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        }
        return "";
    }

    /**
     * 获取答案
     *
     * @param title      问题
     * @param answerList 答案集合
     * @return 空没有获取到
     */
    public static String getAnswer(String title, List<String> answerList) {
        String answerStr = "";
        try {
            Log.record("知识问答🧠题目[" + title + "]#选项" + answerList);
            if (answerAIInterface != null) {
                int answer = answerAIInterface.getAnswer(title, answerList);
                if (answer >= 0 && answer < answerList.size()) {
                    answerStr = answerList.get(answer);
                    Log.record("智能回答🧠[" + answerStr + "]");
                }
            }
            if (answerStr.isEmpty() && !answerList.isEmpty()) {
                // 随机乱答
                answerStr = answerList.get(RandomUtil.nextInt(0, answerList.size() - 1));
                Log.record("普通回答🤖[" + answerStr + "]");
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        }
        String doubleCheckAnswer = Statistics.getQuestionAnswer(title);
        if (doubleCheckAnswer != null && !Objects.equals(answerStr, doubleCheckAnswer)) {
            answerStr = doubleCheckAnswer;
            Log.record("检测即将提交错误的回答，已自动纠正!新回答:" + answerStr);
        }
        return answerStr;
    }
}