package io.github.lazyimmortal.sesame.ui.dialog;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.common.SelectModelFieldFunc;
import io.github.lazyimmortal.sesame.util.StringUtil;

public class AlertDialogBuilder {

    public static AlertDialog.Builder getAlertDialogBuilder(Context context) {
        return new MaterialAlertDialogBuilder(context);
    }

    public static AlertDialog.Builder getAlertDialogBuilder(Context context, CharSequence title, CharSequence message) {
        AlertDialog.Builder builder = getAlertDialogBuilder(context).setTitle(title);
        if (!StringUtil.isEmpty(message)) {
            builder.setMessage(message);
        }
        return builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
    }

    public static AlertDialog.Builder getAlertDialogBuilder(Context context, ModelField<?> modelField) {
        CharSequence message = modelField instanceof SelectModelFieldFunc ? null : modelField.getDescription();
        return getAlertDialogBuilder(context, modelField.getName(), message);
    }

    public static AlertDialog createLoadingAlertDialog(Context context) {
        return getAlertDialogBuilder(context).setView(R.layout.dialog_loading).setCancelable(false).create();
    }
}
