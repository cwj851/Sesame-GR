package io.github.lazyimmortal.sesame.ui.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.IntegerModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectAndCountModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectAndCountOneModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectOneModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.common.SelectModelFieldFunc;
import io.github.lazyimmortal.sesame.entity.idAndName.AlipayUser;
import io.github.lazyimmortal.sesame.entity.idAndName.FriendWatch;
import io.github.lazyimmortal.sesame.entity.idAndName.IdAndName;
import io.github.lazyimmortal.sesame.util.ColorUtil;
import io.github.lazyimmortal.sesame.util.idMap.AbstractIdMap;

public class ListAdapter extends BaseAdapter {
    // 所有列表元素
    private final List<IdAndName> list = new ArrayList<>();
    // 过滤列表元素
    private final List<IdAndName> filterList = new ArrayList<>();
    // 已选列表元素
    private final Set<String> selectedList = new HashSet<>();
    // 已删列表元素
    private final Set<String> removeList = new HashSet<>();

    // 传址
    private final SelectModelFieldFunc selectModelField;
    // 存值
    private final SelectModelFieldFunc selectModelFieldFunc;

    // 视图
    private final LayoutInflater inflater;
    // 映射
    private final AbstractIdMap<?> idMap;
    // 列表类型
    public final ListType listType;

    public ListAdapter(Context context, List<? extends IdAndName> list, SelectModelFieldFunc selectModelFieldFunc) {
        this.inflater = LayoutInflater.from(context);
        this.list.addAll(list);
        this.filterList.addAll(list);
        this.selectModelField = selectModelFieldFunc;
        this.idMap = list.isEmpty() ? null : list.get(0).getIdMapInstance();
        if (selectModelFieldFunc instanceof SelectModelField
                && ((SelectModelField) selectModelFieldFunc).getDefaultValue() != null) {
            listType = ListType.CHECK;
            this.selectModelFieldFunc = new SelectModelField("", "", ((SelectModelField) selectModelFieldFunc).getDefaultValue(), list);
        } else if (selectModelFieldFunc instanceof SelectAndCountModelField) {
            listType = ListType.CHECK;
            this.selectModelFieldFunc = new SelectAndCountModelField("", "", ((SelectAndCountModelField) selectModelFieldFunc).getDefaultValue(), list);
        } else if (selectModelFieldFunc instanceof SelectOneModelField) {
            listType = ListType.RADIO;
            this.selectModelFieldFunc = new SelectOneModelField("", "", ((SelectOneModelField) selectModelFieldFunc).getDefaultValue(), list);
        } else if (selectModelFieldFunc instanceof SelectAndCountOneModelField) {
            listType = ListType.RADIO;
            this.selectModelFieldFunc = new SelectAndCountOneModelField("", "", ((SelectAndCountOneModelField) selectModelFieldFunc).getDefaultValue(), list);
        } else {
            listType = ListType.SHOW;
            this.selectModelFieldFunc = SelectModelFieldFunc.newMapInstance();
        }
        initSelectModelFieldFunc();
    }

    public void setFilterList(CharSequence cs) {
        filterList.clear();
        for (IdAndName item : list) {
            if (item.name.contains(cs) && !removeList.contains(item.id)) {
                filterList.add(item);
            }
        }
        sortList();
        notifyDataSetChanged();
    }

    private void sortList() {
        // 已选排在前面
        Collections.sort(filterList, (o1, o2) -> {
            if (this.selectModelFieldFunc.contains(o1.id) == this.selectModelFieldFunc.contains(o2.id)) {
                if (this.selectedList.contains(o1.id) == this.selectedList.contains(o2.id)) {
                    return o1.compareTo(o2);
                }
                return this.selectedList.contains(o1.id) ? -1 : 1;
            }
            return this.selectModelFieldFunc.contains(o1.id) ? -1 : 1;
        });
    }

    private void initSelectModelFieldFunc() {
        if (listType == ListType.SHOW) {
            return;
        }
        for (IdAndName item : list) {
            if (selectModelField.contains(item.id)) {
                selectModelFieldFunc.add(item.id, selectModelField.get(item.id));
                if (listType == ListType.CHECK && !hasCount()) {
                    selectedList.add(item.id);
                }
            }
        }
        sortList();
    }

    public CharSequence getDescription() {
        return ((ModelField<?>) selectModelField).getDescription();
    }

    public boolean hasCount() {
        return selectModelField instanceof SelectAndCountModelField
                || selectModelField instanceof SelectAndCountOneModelField;
    }

    public void selectAll() {
        for (IdAndName item : filterList) {
            selectedList.add(item.id);
        }
        updateSelectModelFieldFuncValue();
        notifyDataSetChanged();
    }

    public void selectInvert() {
        for (IdAndName item : filterList) {
            if (selectedList.contains(item.id)) {
                selectedList.remove(item.id);
            } else {
                selectedList.add(item.id);
            }
        }
        updateSelectModelFieldFuncValue();
        notifyDataSetChanged();
    }

    private void updateSelectModelFieldFuncValue() {
        if (listType != ListType.CHECK || hasCount()) {
            // 只有 SelectModelField 需要更新 selectModelFieldFunc
            return;
        }
        // 为了刷新标题颜色，更新 selectModelFieldFunc
        for (IdAndName item : filterList) {
            if (selectedList.contains(item.id)) {
                selectModelFieldFunc.add(item.id, 0);
            } else {
                selectModelFieldFunc.remove(item.id);
            }
        }
    }

    // 批量设置数量
    public void batchSetupCount(int count) {
        for (String id : selectedList) {
            if (count > 0) {
                selectModelFieldFunc.add(id, count);
            } else {
                selectModelFieldFunc.remove(id);
            }
        }
        selectedList.clear();
        notifyDataSetChanged();
    }

    public void updateModelFieldValue() {
        if (!removeList.isEmpty()) {
            for (String id : removeList) {
                idMap.remove(id);
            }
            idMap.save();
        }
        if (listType == ListType.SHOW) {
            return;
        }
        selectModelField.clear();
        for (IdAndName item : list) {
            if (selectModelFieldFunc.contains(item.id)) {
                selectModelField.add(item.id, selectModelFieldFunc.get(item.id));
            } else {
                selectModelField.remove(item.id);
            }
        }
    }

    private void setSelectedColor(Context context, TextView textView) {
        textView.setTextColor(ColorUtil.getColor(context, com.google.android.material.R.attr.colorPrimary));
    }

    private void setNotSelectedColor(Context context, TextView textView) {
        textView.setTextColor(ColorUtil.getColor(context, com.google.android.material.R.attr.colorOnSurfaceVariant));
    }

    @Override
    public int getCount() {
        return filterList.size();
    }

    @Override
    public Object getItem(int i) {
        return filterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.textView = view.findViewById(R.id.tv_idn);
            holder.checkBox = view.findViewById(R.id.cb_list);
            if (listType == ListType.SHOW) {
                holder.checkBox.setVisibility(View.GONE);
            }
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // 获取元素
        IdAndName item = filterList.get(i);
        // 设置显示名称
        Integer integer = selectModelFieldFunc.get(item.id);
        CharSequence title = ColorUtil.getText((i + 1) + ". " + item.name,
                integer != null && integer > 0 ? integer.toString() : null);
        holder.textView.setText(title);
        // 设置文本颜色
        if (selectModelFieldFunc.contains(item.id)) {
            setSelectedColor(view.getContext(), holder.textView);
        } else {
            setNotSelectedColor(view.getContext(), holder.textView);
        }
        // 设置勾选状态
        holder.checkBox.setChecked(selectedList.contains(item.id)
                || (listType == ListType.RADIO && selectModelFieldFunc.contains(item.id)));
        // 处理点击事件
        View.OnClickListener inputListener = v -> {
            String modelFieldName = selectedList.isEmpty() ? item.name : "批量设置" + selectedList.size() + "项";
            IntegerModelField integerModelField = new IntegerModelField("", modelFieldName, 0);
            integerModelField.setValue(integer);
            integerModelField.setDescription(getDescription());
            ModelFieldDialog.show(v.getContext(), integerModelField, (c, m) -> {
                if (selectedList.isEmpty()) {
                    selectedList.add(item.id);
                }
                batchSetupCount(integerModelField.getValue());
            });
        };
        View.OnClickListener clickListener = v -> {
            if (listType == ListType.RADIO) {
                if (!hasCount()) {
                    if (selectModelFieldFunc.contains(item.id)) {
                        selectModelFieldFunc.remove(item.id);
                    } else {
                        selectModelFieldFunc.add(item.id, 0);
                    }
                } else {
                    inputListener.onClick(v);
                }
                notifyDataSetChanged();
            } else if (listType == ListType.CHECK) {
                if (selectedList.contains(item.id)) {
                    selectedList.remove(item.id);
                    holder.checkBox.setChecked(false);
                    if (!hasCount()) {
                        selectModelFieldFunc.remove(item.id);
                        setNotSelectedColor(v.getContext(), holder.textView);
                    }
                } else {
                    selectedList.add(item.id);
                    holder.checkBox.setChecked(true);
                    if (!hasCount()) {
                        selectModelFieldFunc.add(item.id, 0);
                        setSelectedColor(v.getContext(), holder.textView);
                    }
                }
            }
        };
        View.OnLongClickListener longClickListener = v -> {
            if (idMap != null) {
                AlertDialogBuilder.getAlertDialogBuilder(v.getContext(),
                                "移除列表项",
                                "确认从列表中移除" + item.name + "？")
                        .setPositiveButton(R.string.ok, (dialogInterface, index) -> {
                            filterList.remove(i);
                            removeList.add(item.id);
                            notifyDataSetChanged();
                        })
                        .create()
                        .show();
            } else if (item instanceof AlipayUser || item instanceof FriendWatch) {
                OptionsAdapter optionsAdapter = new OptionsAdapter(v.getContext(), item.id);
                AlertDialogBuilder.getAlertDialogBuilder(v.getContext(), "选项", null)
                        .setAdapter(optionsAdapter, optionsAdapter.getOnClickListener(v.getContext()))
                        .create()
                        .show();
            }
            return true;
        };
        if (listType == ListType.CHECK && hasCount()) {
            holder.textView.setOnClickListener(inputListener);
            holder.checkBox.setOnClickListener(clickListener);
            holder.textView.setOnLongClickListener(longClickListener);
        } else {
            view.setOnClickListener(clickListener);
            view.setOnLongClickListener(longClickListener);
        }
        return view;
    }

    static class ViewHolder {
        TextView textView;
        CheckBox checkBox;
    }

    public enum ListType {
        RADIO, CHECK, SHOW
    }
}
