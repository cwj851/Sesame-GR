package io.github.lazyimmortal.sesame.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import java.util.concurrent.atomic.AtomicReference;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.ChoiceModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.EmptyModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.IntegerModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.ListModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectAndCountModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectAndCountOneModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectOneModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.StringModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.TextModelField;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;

public class ModelFieldDialog {

    public static void show(Context context, ModelField<?> modelField) {
        show(context, modelField, null);
    }

    /**
     * 展示模块字段对话框
     *
     * @param context    上下文
     * @param modelField 模块字段
     * @param listener   点击确认按钮时的触发事件(没有特殊需求，直接为null就行)
     */
    public static void show(Context context, ModelField<?> modelField, OnModelFieldClickListener listener) {
        if (modelField instanceof EmptyModelField) {
            showMessageDialog(context, modelField, listener);
        } else if (modelField instanceof StringModelField
                || modelField instanceof IntegerModelField
                || modelField instanceof ListModelField
        ) {
            showInputDialog(context, modelField, listener);
        } else if (modelField instanceof TextModelField) {
            showReadDialog(context, modelField, listener);
        } else if (modelField instanceof ChoiceModelField) {
            showChoiceDialog(context, (ChoiceModelField) modelField, listener);
        } else if (modelField instanceof SelectModelField) {
            ListDialog.show(context, (SelectModelField) modelField, listener);
        } else if (modelField instanceof SelectAndCountModelField) {
            ListDialog.show(context, (SelectAndCountModelField) modelField, listener);
        } else if (modelField instanceof SelectOneModelField) {
            ListDialog.show(context, (SelectOneModelField) modelField, listener);
        } else if (modelField instanceof SelectAndCountOneModelField) {
            ListDialog.show(context, (SelectAndCountOneModelField) modelField, listener);
        } else {
            ToastUtil.show(context, "什么也没有发生，因为功能未实现！");
        }
    }

    public static void showMessageDialog(Context context, ModelField<?> modelField,
                                         OnModelFieldClickListener listener) {
        getMessageDialog(context, modelField, listener).show();
    }

    public static void showInputDialog(Context context, ModelField<?> modelField,
                                       OnModelFieldClickListener listener) {
        getInputDialog(context, modelField, listener).show();
    }

    public static void showReadDialog(Context context, ModelField<?> modelField,
                                      OnModelFieldClickListener listener) {
        getReadDialog(context, modelField, listener).show();
    }

    public static void showChoiceDialog(Context context, ChoiceModelField modelField,
                                        OnModelFieldClickListener listener) {
        getChoiceDialog(context, modelField, listener).show();
    }

    private static AlertDialog.Builder getAlertDialogBuilder(Context context, ModelField<?> modelField,
                                                             OnModelFieldClickListener listener) {
        return AlertDialogBuilder.getAlertDialogBuilder(context, modelField)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    if (listener != null) {
                        listener.onClick(context, modelField);
                    }
                });
    }

    private static AlertDialog getMessageDialog(Context context, ModelField<?> modelField,
                                                OnModelFieldClickListener listener) {
        return getAlertDialogBuilder(context, modelField, listener)
                .create();
    }

    private static AlertDialog getInputDialog(Context context, ModelField<?> modelField,
                                              OnModelFieldClickListener listener) {
        EditText edt = new EditText(context);
        edt.setMaxLines(10);
        String modelFieldConfigValue = modelField.getConfigValue();
        if (modelField.getValue() != null && !StringUtil.isEmpty(modelFieldConfigValue)) {
            edt.setText(modelFieldConfigValue);
        }
        return getAlertDialogBuilder(context, modelField, (c, m) -> {
            try {
                Editable text = edt.getText();
                String textString = null;
                if (text != null) {
                    textString = text.toString();
                }
                m.setConfigValue(textString);
            } catch (Throwable e) {
                Log.printStackTrace(e);
            }
            if (listener != null) {
                listener.onClick(c, m);
            }
        }).setView(edt).create();
    }

    private static AlertDialog getReadDialog(Context context, ModelField<?> modelField,
                                             OnModelFieldClickListener listener) {
        EditText edt = new EditText(context);
        edt.setInputType(InputType.TYPE_NULL);
        edt.setTextColor(Color.GRAY);
        AlertDialog alertDialog = getAlertDialogBuilder(context, modelField, listener)
                .setView(edt).create();
        String modelFieldConfigValue = modelField.getConfigValue();
        if (!StringUtil.isEmpty(modelFieldConfigValue)) {
            edt.setText(modelFieldConfigValue);
        }
        alertDialog.setOnShowListener(
                dialog -> alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setText("重置")
        );
        return alertDialog;
    }

    private static AlertDialog getChoiceDialog(Context context, ChoiceModelField modelField,
                                               OnModelFieldClickListener listener) {
        AtomicReference<Integer> selectedIndex = new AtomicReference<>(modelField.getValue());
        return getAlertDialogBuilder(context, modelField, (c, m) -> {
            modelField.setObjectValue(selectedIndex.get());
            if (listener != null) {
                listener.onClick(c, m);
            }
        }).setSingleChoiceItems(modelField.getExpandKey(), modelField.getValue(), (p1, p2) -> {
            selectedIndex.set(p2);
        }).create();
    }

    public interface OnModelFieldClickListener {
        void onClick(Context context, ModelField<?> modelField);
    }
}
