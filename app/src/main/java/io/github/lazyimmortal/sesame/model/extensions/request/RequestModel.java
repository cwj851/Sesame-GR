package io.github.lazyimmortal.sesame.model.extensions.request;

import android.content.Intent;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.EmptyModelField;
import io.github.lazyimmortal.sesame.ui.ExtensionActivity;

public class RequestModel extends Model {
    @Override
    public String getName() {
        return "请求";
    }

    @Override
    public String getEnableFieldName() {
        return "无需开启";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.OTHER;
    }

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(new EmptyModelField("developerMode", "开发者模式", (c, m) -> {
            Intent intent = new Intent(c, ExtensionActivity.class);
            intent.putExtra("viewName", c.getString(R.string.developer_mode));
            c.startActivity(intent);
        }));
        modelFields.addField(new EmptyModelField("sesameAsk", "芝麻问答", (c, m) -> {
            Intent intent = new Intent(c, ExtensionActivity.class);
            intent.putExtra("viewName", "芝麻问答");
            c.startActivity(intent);
        }));
        return modelFields;
    }
}
