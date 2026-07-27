package io.github.lazyimmortal.sesame.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

import io.github.lazyimmortal.sesame.model.normal.base.BaseModel;
import io.github.lazyimmortal.sesame.ui.BaseActivity;

public class ToastUtil {
    private static final String TAG = ToastUtil.class.getSimpleName();

    public static void show(Context context, @StringRes int resId) {
        show(context, context.getText(resId));
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, context instanceof BaseActivity);
    }

    public static void show(Context context, CharSequence text, boolean force) {
        if (context == null || (!BaseModel.getShowToast().getValue() && !force)) {
            return;
        }
        // 在主线程展示Toast
        try {
            HandlerUtil.post(() -> {
                try {
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                    toast.setGravity(toast.getGravity(), toast.getXOffset(), BaseModel.getToastOffsetY().getValue());
                    toast.show();
                } catch (Throwable t) {
                    Log.i(TAG, "show.run err:");
                    Log.printStackTrace(TAG, t);
                }
            });
        } catch (Throwable t) {
            Log.i(TAG, "show err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
