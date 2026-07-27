package io.github.lazyimmortal.sesame.data.modelFieldExt;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;
import io.github.lazyimmortal.sesame.util.StringUtil;

public class StringModelField extends ModelField<String> {

    public StringModelField(String code, String name, String value) {
        super(code, name, value);
    }

    public StringModelField(String code, String name, String value, CharSequence description) {
        super(code, name, value, description);
    }

    @Override
    public String getType() {
        return "STRING";
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

    @Override
    public View getView(Context context) {
        Button btn = new MaterialButton(context);
        btn.setText(getText(btn, getName(), getConfigValue()));
        btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        btn.setMinHeight(150);
        btn.setMaxHeight(180);
        btn.setPaddingRelative(40, 0, 40, 0);
        btn.setAllCaps(false);
        btn.setOnClickListener(v -> {
            ModelFieldDialog.show(v.getContext(), this, (c, m) -> {
                btn.setText(getText(btn, getName(), getConfigValue()));
            });
        });
        return btn;
    }

}
