package io.github.lazyimmortal.sesame.data.modelFieldExt;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Objects;

import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.common.SelectModelFieldFunc;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.entity.idAndName.IdAndName;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;

public class SelectOneModelField extends ModelField<String> implements SelectModelFieldFunc {

    private SelectListFunc selectListFunc;

    private List<? extends IdAndName> expandValue;

    public SelectOneModelField(String code, String name, String value, List<? extends IdAndName> expandValue) {
        super(code, name, value);
        this.expandValue = expandValue;
    }

    public SelectOneModelField(String code, String name, String value, Class<? extends Enum<? extends CustomOption>> enumClass) {
        this(code, name, value, () -> CustomOption.getList(enumClass));
    }
    public SelectOneModelField(String code, String name, String value, SelectListFunc selectListFunc) {
        super(code, name, value);
        this.selectListFunc = selectListFunc;
    }

    public SelectOneModelField(String code, String name, String value, SelectListFunc selectListFunc, String description) {
        super(code, name, value, description);
        this.selectListFunc = selectListFunc;
    }

    @Override
    public String getType() {
        return "SELECT_ONE";
    }

    public List<? extends IdAndName> getExpandValue() {
        return selectListFunc == null ? expandValue : selectListFunc.getList();
    }

    @Override
    public View getView(Context context) {
        Button btn = new MaterialButton(context);
        btn.setText(getText(btn, getName(), getSubTitle()));
        btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        btn.setMinHeight(150);
        btn.setPaddingRelative(40, 0, 40, 0);
        btn.setAllCaps(false);
        btn.setOnClickListener(v -> ModelFieldDialog.show(context, this, (c, m) -> btn.setText(getText(btn, getName(), getSubTitle()))));
        return btn;
    }

    private String getSubTitle() {
        String subTitle = "已选:" + getConfigValue();
        for (IdAndName item : getExpandValue()) {
            if (contains(item.id)) {
                subTitle = subTitle.replace(item.id, item.name);
                break;
            }
        }
        return subTitle;
    }

    @Override
    public void clear() {
        value = defaultValue;
    }

    @Override
    public Integer get(String id) {
        return 0;
    }

    @Override
    public void add(String id, Integer count) {
        value = id;
    }

    @Override
    public void remove(String id) {
        if (Objects.equals(value, id)) {
            value = defaultValue;
        }
    }

    @Override
    public Boolean contains(String id) {
        return Objects.equals(value, id);
    }

    public interface SelectListFunc {
        List<? extends IdAndName> getList();
    }
}