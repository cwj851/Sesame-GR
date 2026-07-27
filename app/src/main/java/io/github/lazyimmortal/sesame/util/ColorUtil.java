package io.github.lazyimmortal.sesame.util;

import android.content.Context;
import android.util.TypedValue;

import androidx.core.text.HtmlCompat;

public class ColorUtil {
    public static int getColor(Context context, int colorAttrResId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttrResId, typedValue, true);
        return typedValue.data;
    }

    public static String getColorHtmlValue(Context context, int colorAttrResId) {
        return String.format("#%06X", (0xFFFFFF & getColor(context, colorAttrResId)));
    }

    public static String getColorTitle(Context context, int colorAttrResId, CharSequence title) {
        if (StringUtil.isEmpty(title)) {
            return "";
        }
        return "<font color='" + getColorHtmlValue(context, colorAttrResId) + "'>" + title + "</font>";
    }

    public static String getColorSubTitle(Context context, int colorAttrResId, CharSequence subTitle) {
        if (StringUtil.isEmpty(subTitle)) {
            return "";
        }
        return "<br><small><font color='" + getColorHtmlValue(context, colorAttrResId) + "'>" + subTitle + "</font></small>";
    }

    public static CharSequence getText(Context context, int titleColorAttrResId, CharSequence title) {
        return getText(context, titleColorAttrResId, title, 0, null);
    }

    public static CharSequence getText(Context context,
                                       int titleColorAttrResId, CharSequence title,
                                       int subTitleColorAttrResId, CharSequence subTitle) {
        String source = getColorTitle(context, titleColorAttrResId, title)
                + getColorSubTitle(context, subTitleColorAttrResId, subTitle);
        return HtmlCompat.fromHtml(source, HtmlCompat.FROM_HTML_MODE_COMPACT);
    }

    // 默认颜色
    public static CharSequence getText(String title, String subTitle) {
        if (StringUtil.isEmpty(subTitle)) {
            return title;
        }
        return HtmlCompat.fromHtml(title + "<br><small>" + subTitle + "</small>", HtmlCompat.FROM_HTML_MODE_COMPACT);
    }
}
