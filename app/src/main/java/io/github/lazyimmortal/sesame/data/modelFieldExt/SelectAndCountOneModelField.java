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
import io.github.lazyimmortal.sesame.entity.KVNode;
import io.github.lazyimmortal.sesame.entity.idAndName.IdAndName;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;

public class SelectAndCountOneModelField extends ModelField<KVNode<String, Integer>> implements SelectModelFieldFunc {

    private SelectListFunc selectListFunc;

    private List<? extends IdAndName> expandValue;

    public SelectAndCountOneModelField(String code, String name, KVNode<String, Integer> value, List<? extends IdAndName> expandValue) {
        super(code, name, value);
        this.expandValue = expandValue;
    }

    public SelectAndCountOneModelField(String code, String name, KVNode<String, Integer> value, SelectListFunc selectListFunc) {
        super(code, name, value);
        this.selectListFunc = selectListFunc;
    }

    @Override
    public String getType() {
        return "SELECT_AND_COUNT_ONE";
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
        btn.setOnClickListener(v -> ModelFieldDialog.show(v.getContext(), this, (c, m) -> btn.setText(getText(btn, getName(), getSubTitle()))));
        return btn;
    }

    private String getSubTitle() {
        String subTitle = "已设:" + getConfigValue();
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
        KVNode<String, Integer> kvNode = getValue();
        if (kvNode != null && Objects.equals(kvNode.getKey(), id)) {
            return kvNode.getValue();
        }
        return 0;
    }

    @Override
    public void add(String id, Integer count) {
        value = new KVNode<>(id, count);
    }

    @Override
    public void remove(String id) {
        KVNode<String, Integer> kvNode = getValue();
        if (kvNode != null && Objects.equals(kvNode.getKey(), id)) {
            value = defaultValue;
        }
    }

    @Override
    public Boolean contains(String id) {
        KVNode<String, Integer> kvNode = getValue();
        return kvNode != null && Objects.equals(kvNode.getKey(), id);
    }

    public interface SelectListFunc {
        List<? extends IdAndName> getList();
    }
}