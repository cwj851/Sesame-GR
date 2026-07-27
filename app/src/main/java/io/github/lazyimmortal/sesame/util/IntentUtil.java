package io.github.lazyimmortal.sesame.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import io.github.lazyimmortal.sesame.model.extensions.logModel.LogType;
import io.github.lazyimmortal.sesame.ui.HtmlViewerActivity;

public class IntentUtil {

    public static final String ACTION_SESAME_STATUS = "io.github.lazyimmortal.sesame.status";
    public static final String ACTION_SESAME_UPDATE = "io.github.lazyimmortal.sesame.update";
    public static final String ACTION_SESAME_REQUEST = "io.github.lazyimmortal.sesame.request";

    public static final String ACTION_ALIPAY_RESTART = "com.eg.android.AlipayGphone.sesame.restart";
    public static final String ACTION_ALIPAY_EXECUTE = "com.eg.android.AlipayGphone.sesame.execute";
    public static final String ACTION_ALIPAY_RE_LOGIN = "com.eg.android.AlipayGphone.sesame.reLogin";
    public static final String ACTION_ALIPAY_STATUS = "com.eg.android.AlipayGphone.sesame.status";
    public static final String ACTION_ALIPAY_REQUEST = "com.eg.android.AlipayGphone.cactus.rpctest";

    public static final String ACTION_TAI_CHI_ACTIVE = "me.weishu.exp.ACTION_ACTIVE";

    public static void startActivity(Context context, Intent intent) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void startActivity(Context context, Class<?> clazz) {
        startActivity(context, new Intent(context, clazz));
    }

    public static void viewLog(Context context, LogType logType) {
        Intent intent = new Intent(context, HtmlViewerActivity.class);
        intent.setData(Uri.parse("file://" + FileUtil.getLogFile(logType).getAbsolutePath()));
        startActivity(context, intent);
    }

    public static void viewLogAndCannotClear(Context context, LogType logType) {
        Intent intent = new Intent(context, HtmlViewerActivity.class);
        intent.setData(Uri.parse("file://" + FileUtil.getLogFile(logType).getAbsolutePath()));
        intent.putExtra("canClear", false);
        startActivity(context, intent);
    }

    public static void viewLogAndNotNextLine(Context context, LogType logType) {
        Intent intent = new Intent(context, HtmlViewerActivity.class);
        intent.setData(Uri.parse("file://" + FileUtil.getLogFile(logType).getAbsolutePath()));
        intent.putExtra("nextLine", false);
        startActivity(context, intent);
    }

    public static void viewWebsite(Context context, String website) {
        Intent intent = new Intent(context, HtmlViewerActivity.class);
        intent.setData(Uri.parse(website));
        startActivity(context, intent);
    }
}
