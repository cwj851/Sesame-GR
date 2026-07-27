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

public class ChoiceModelField extends ModelField<Integer> {

    private String[] choiceArray;

    public ChoiceModelField(String code, String name, Integer value) {
        super(code, name, value);
    }

    public ChoiceModelField(String code, String name, Integer value, String[] choiceArray) {
        super(code, name, value);
        this.choiceArray = choiceArray;
    }

    @Override
    public String getType() {
        return "CHOICE";
    }

    public String[] getExpandKey() {
        return choiceArray;
    }

    @Override
    public View getView(Context context) {
        Button btn = new MaterialButton(context);
        btn.setText(getText(btn, getName(), getSubTitle()));
        btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        btn.setMinHeight(150);
        btn.setMaxHeight(180);
        btn.setPaddingRelative(40, 0, 40, 0);
        btn.setAllCaps(false);
        btn.setOnClickListener(v -> ModelFieldDialog.show(v.getContext(), this, (c, m) -> btn.setText(getText(btn, getName(), getSubTitle()))));
        return btn;
    }

    private String getSubTitle() {
        if (getValue() >= 0 && getValue() < choiceArray.length) {
            return choiceArray[getValue()];
        } else {
            return choiceArray[getDefaultValue()];
        }
    }
}
