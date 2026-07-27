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
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.StringUtil;
import lombok.Getter;

@Getter
public class IntegerModelField extends ModelField<Integer> {

    protected final Integer minLimit;

    protected final Integer maxLimit;

    public IntegerModelField(String code, String name, Integer value) {
        this(code, name, value, null, null);
    }

    public IntegerModelField(String code, String name, Integer value, Integer minLimit, Integer maxLimit) {
        super(code, name, value);
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
    }

    @Override
    public String getType() {
        return "INTEGER";
    }

    @Override
    public String getConfigValue() {
        return String.valueOf(value);
    }

    @Override
    public void setConfigValue(String configValue) {
        this.value = getNewValue(configValue, defaultValue);
    }

    public Integer getNewValue(String configValue, Integer defaultValue) {
        Integer newValue = defaultValue;
        if (!StringUtil.isEmpty(configValue)) {
            try {
                newValue = Integer.parseInt(configValue);
                if (minLimit != null) {
                    newValue = Math.max(minLimit, newValue);
                }
                if (maxLimit != null) {
                    newValue = Math.min(maxLimit, newValue);
                }
            } catch (Exception e) {
                Log.printStackTrace(e);
            }
        }
        return newValue;
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
                (c, m) -> btn.setText(getText(btn, getName(), getConfigValue()))
        ));
        return btn;
    }

    @Getter
    public static class MultiplyIntegerModelField extends IntegerModelField {

        private final Integer multiple;

        public MultiplyIntegerModelField(String code, String name, Integer value, Integer minLimit, Integer maxLimit, Integer multiple) {
            super(code, name, value * multiple, minLimit, maxLimit);
            this.multiple = multiple;
        }

        @Override
        public String getType() {
            return "MULTIPLY_INTEGER";
        }

        @Override
        public void setConfigValue(String configValue) {
            try {
                value = getNewValue(configValue, defaultValue / multiple) * multiple;
            } catch (Exception e) {
                reset();
            }
        }

        @Override
        public String getConfigValue() {
            return String.valueOf(value / multiple);
        }
    }

}
