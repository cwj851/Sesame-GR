package io.github.lazyimmortal.sesame.model.extensions.answerAI;

import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.EmptyModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.StringModelField;
import io.github.lazyimmortal.sesame.entity.LLMEntity;
import io.github.lazyimmortal.sesame.entity.idAndName.AnswerLLM;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.idMap.LLMIdMap;

public class AnswerAIExtension extends Model {
    @Override
    public String getName() {
        return "大模型";
    }

    @Override
    public String getEnableFieldName() {
        return "无需开启";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.OTHER;
    }

    private final StringModelField compatibleLLMBaseUrl = new StringModelField("compatibleLLMBaseUrl", "自定义大模型 | 地址", "", "仅支持自定义兼容OpenAI的大模型\n地址格式:https://api.openai.com/v1");
    private final StringModelField compatibleLLMApiKey = new StringModelField("compatibleLLMApiKey", "自定义大模型 | 令牌", "", "仅支持自定义兼容OpenAI的大模型\n令牌格式:sk-abc1234567890");
    private final StringModelField compatibleLLMModel = new StringModelField("compatibleLLMModel", "自定义大模型 | 模型", "", "仅支持自定义兼容OpenAI的大模型\n模型格式:gpt-3.5-turbo");
    private final StringModelField compatibleLLMName = new StringModelField("compatibleLLMName", "自定义大模型 | 名称", "", "填写你喜欢的名字，不填也可以");

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(compatibleLLMBaseUrl);
        modelFields.addField(compatibleLLMApiKey);
        modelFields.addField(compatibleLLMModel);
        modelFields.addField(compatibleLLMName);
        modelFields.addField(new EmptyModelField("compatibleLLMAdd", "自定义大模型 | 添加", (c, m) -> {
            String baseUrl = compatibleLLMBaseUrl.getValue();
            String apiKey = compatibleLLMApiKey.getValue();
            String model = compatibleLLMModel.getValue();
            String name = compatibleLLMName.getValue();
            if (StringUtil.hasEmpty(baseUrl, apiKey, model)) {
                ToastUtil.show(c, "添加大模型失败:必要参数为空");
                return;
            }
            LLMEntity entity = new LLMEntity(baseUrl, apiKey, model, name);
            LLMIdMap.getInstance().load();
            if (LLMIdMap.getInstance().add(entity)) {
                ToastUtil.show(c, "自定义大模型已添加到列表中");
            } else {
                ToastUtil.show(c, "自定义大模型添加失败");
            }
        }, "确定要添加自定义模型到列表？"));
        modelFields.addField(new EmptyModelField("compatibleLLMList", "自定义大模型 | 列表", (c, m) -> {
            LLMIdMap.getInstance().load();
            SelectModelField selectModelField = new SelectModelField("", "自定义大模型 | 列表", null, AnswerLLM::getList);
            ModelFieldDialog.show(c, selectModelField);
        }));
        return modelFields;
    }
}
