package io.github.lazyimmortal.sesame.data.modelFieldExt;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.android.material.button.MaterialButton;

import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;
import io.github.lazyimmortal.sesame.util.ToastUtil;

public class EmptyModelField extends ModelField<Object> {

    private final ModelFieldDialog.OnModelFieldClickListener listener;

    public EmptyModelField(String code, String name) {
        this(code, name, null);
    }

    public EmptyModelField(String code, String name, ModelFieldDialog.OnModelFieldClickListener listener) {
        this(code, name, listener, null);
    }

    public EmptyModelField(String code, String name, ModelFieldDialog.OnModelFieldClickListener listener, CharSequence description) {
        super(code, name, null, description);
        if (listener == null) {
            this.listener = (context, modelField) -> ToastUtil.show(context, "无配置项");
        } else if (description == null) {
            this.listener = listener;
        } else {
            this.listener = ((context, modelField) -> ModelFieldDialog.show(context, modelField, listener));
        }
    }

    @Override
    public String getType() {
        return "EMPTY";
    }

    @Override
    public void setObjectValue(Object value) {
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
        btn.setOnClickListener(v -> listener.onClick(v.getContext(), this));
        return btn;
    }
}
