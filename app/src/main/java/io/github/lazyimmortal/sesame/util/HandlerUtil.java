package io.github.lazyimmortal.sesame.util;

import android.os.Handler;
import android.os.Looper;

public class HandlerUtil {
    private static HandlerUtil INSTANCE;

    private final Handler handler;

    private HandlerUtil() {
        handler = new Handler(Looper.getMainLooper());
    }

    private static HandlerUtil getInstance() {
        if (INSTANCE == null) {
            return INSTANCE = new HandlerUtil();
        }
        return INSTANCE;
    }

    public static Handler getHandler() {
        return getInstance().handler;
    }

    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }
}
