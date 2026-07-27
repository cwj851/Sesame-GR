package io.github.lazyimmortal.sesame.data.modelFieldExt;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;
import io.github.lazyimmortal.sesame.util.StringUtil;

public class ListModelField extends ModelField<List<String>> {

    private static final TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {
    };

    public ListModelField(String code, String name, List<String> value) {
        super(code, name, value);
    }

    public ListModelField(String code, String name, List<String> value, CharSequence description) {
        super(code, name, value, description);
    }

    @Override
    public String getType() {
        return "LIST";
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
        btn.setOnClickListener(v -> ModelFieldDialog.show(v.getContext(), this,
                (c, m) -> btn.setText(getText(btn, getName(), getConfigValue()))));
        return btn;
    }

    public static class ListJoinCommaToStringModelField extends ListModelField {

        public ListJoinCommaToStringModelField(String code, String name, List<String> value) {
            super(code, name, value);
        }

        public ListJoinCommaToStringModelField(String code, String name, List<String> value, CharSequence description) {
            super(code, name, value, description);
        }

        @Override
        public void setConfigValue(String configValue) {
            if (StringUtil.isEmpty(configValue)) {
                reset();
                return;
            }
            List<String> list = new ArrayList<>();
            String[] split = configValue.split(",");
            if (split.length == 1) {
                String str = split[0];
                if (!str.isEmpty()) {
                    list.add(str);
                }
            } else {
                for (String str : split) {
                    if (!str.isEmpty()) {
                        list.add(str);
                    }
                }
            }
            value = list;
        }

        @Override
        public String getConfigValue() {
            return String.join(",", value);
        }
    }

}
