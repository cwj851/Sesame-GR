package io.github.lazyimmortal.sesame.data.modelFieldExt;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.android.material.button.MaterialButton;

import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.ui.HtmlViewerActivity;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;

public class TextModelField extends ModelField<String> {

    public TextModelField(String code, String name, String value) {
        super(code, name, value);
    }

    @Override
    public String getType() {
        return "TEXT";
    }

    @Override
    public String getConfigValue() {
        return value;
    }

    @Override
    public void setConfigValue(String configValue) {
        if (StringUtil.isEmpty(configValue)) {
            configValue = defaultValue;
        }
        value = configValue;
    }

    @JsonIgnore
    public View getView(Context context) {
        Button btn = new MaterialButton(context);
        btn.setText(getText(btn, getName(), getConfigValue()));
        btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        btn.setMinHeight(150);
        btn.setMaxHeight(180);
        btn.setPaddingRelative(40, 0, 40, 0);
        btn.setAllCaps(false);
        btn.setOnClickListener(v -> ModelFieldDialog.show(v.getContext(), this,
                (c, m) -> {
                    reset();
                    ToastUtil.show(c, "文本信息已重置");
                    btn.setText(getText(btn, getName(), getConfigValue()));
                }));
        return btn;
    }

    public static class ReadOnlyTextModelField extends TextModelField {

        public ReadOnlyTextModelField(String code, String name, String value) {
            super(code, name, value);
        }

        @Override
        public String getType() {
            return "READ_TEXT";
        }

        @Override
        public String getValue() {
            return null;
        }

        @Override
        public void setConfigValue(String configValue) {
        }

    }

    public static class UrlTextModelField extends ReadOnlyTextModelField {

        public UrlTextModelField(String code, String name, String value) {
            super(code, name, value);
        }

        @Override
        public String getType() {
            return "URL_TEXT";
        }

        @JsonIgnore
        public View getView(Context context) {
            Button btn = new MaterialButton(context);
            btn.setText(getName());
            btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            btn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            btn.setMinHeight(150);
            btn.setMaxHeight(180);
            btn.setPaddingRelative(40, 0, 40, 0);
            btn.setAllCaps(false);
            btn.setOnClickListener(v -> {
                Context innerContext = v.getContext();
                Intent it = new Intent(innerContext, HtmlViewerActivity.class);
                it.setData(Uri.parse(getConfigValue()));
                innerContext.startActivity(it);
            });
            return btn;
        }

    }

}
