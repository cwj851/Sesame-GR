package io.github.lazyimmortal.sesame.hook;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import io.github.lazyimmortal.sesame.data.task.BaseTask;
import io.github.lazyimmortal.sesame.entity.RpcEntity;
import io.github.lazyimmortal.sesame.rpc.bridge.RpcBridge;
import io.github.lazyimmortal.sesame.rpc.bridge.RpcVersion;
import io.github.lazyimmortal.sesame.util.ClassUtil;
import io.github.lazyimmortal.sesame.util.IntentUtil;
import io.github.lazyimmortal.sesame.util.Log;
import lombok.Getter;

public class ApplicationHook {

    private static final String TAG = ApplicationHook.class.getSimpleName();

    static final Map<Object, Object[]> rpcHookMap = new ConcurrentHashMap<>();

    static final Map<String, PendingIntent> wakenAtTimeAlarmMap = new ConcurrentHashMap<>();

    @Getter
    static ClassLoader classLoader = null;

    @Getter
    static Object microApplicationContextObject = null;

    @Getter
    @SuppressLint("StaticFieldLeak")
    static Context context = null;

    @Getter
    static volatile boolean hooked = false;

    static volatile boolean running = false;
    static volatile boolean init = false;

    static volatile Calendar dayCalendar;

    @Getter
    static volatile boolean offline = false;

    @Getter
    static final AtomicInteger reLoginCount = new AtomicInteger(0);

    @SuppressLint("StaticFieldLeak")
    static Service service;

    static BaseTask mainTask;

    static RpcBridge rpcBridge;

    @Getter
    static RpcVersion rpcVersion;

    static PowerManager.WakeLock wakeLock;

    static PendingIntent alarm0Pi;

    static XC_MethodHook.Unhook rpcRequestUnhook;

    static XC_MethodHook.Unhook rpcResponseUnhook;

    static final LifecycleManager lifecycleManager = new LifecycleManager();

    public static void setOffline(boolean offline) {
        ApplicationHook.offline = offline;
    }

    public static void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!Objects.equals(ClassUtil.PACKAGE_NAME, lpparam.packageName)
                || !Objects.equals(ClassUtil.PACKAGE_NAME, lpparam.processName)
                || hooked) {
            return;
        }
        classLoader = lpparam.classLoader;

        HookRegistry.attachHooks(classLoader);
        HookRegistry.matchVersionHook(classLoader);
        HookRegistry.loginHook(classLoader);
        HookRegistry.serviceOnCreateHook(classLoader);
        HookRegistry.serviceOnDestroyHook(classLoader);
        HookRegistry.foregroundHooks(classLoader);

        hooked = true;
        Log.i(TAG, "load success: " + lpparam.packageName);
    }

    public static String requestString(RpcEntity rpcEntity) {
        return rpcBridge.requestString(rpcEntity, 3, -1);
    }

    public static String requestString(RpcEntity rpcEntity, int tryCount, int retryInterval) {
        return rpcBridge.requestString(rpcEntity, tryCount, retryInterval);
    }

    public static String requestString(String method, String data) {
        return rpcBridge.requestString(method, data);
    }

    public static String requestString(String method, String data, String relation) {
        return rpcBridge.requestString(method, data, relation);
    }

    public static String requestString(String method, String data, int tryCount, int retryInterval) {
        return rpcBridge.requestString(method, data, tryCount, retryInterval);
    }

    public static String requestString(String method, String data, String relation, int tryCount, int retryInterval) {
        return rpcBridge.requestString(method, data, relation, tryCount, retryInterval);
    }

    public static RpcEntity requestObject(RpcEntity rpcEntity) {
        return rpcBridge.requestObject(rpcEntity, 3, -1);
    }

    public static RpcEntity requestObject(RpcEntity rpcEntity, int tryCount, int retryInterval) {
        return rpcBridge.requestObject(rpcEntity, tryCount, retryInterval);
    }

    public static RpcEntity requestObject(String method, String data) {
        return rpcBridge.requestObject(method, data);
    }

    public static RpcEntity requestObject(String method, String data, String relation) {
        return rpcBridge.requestObject(method, data, relation);
    }

    public static RpcEntity requestObject(String method, String data, int tryCount, int retryInterval) {
        return rpcBridge.requestObject(method, data, tryCount, retryInterval);
    }

    public static RpcEntity requestObject(String method, String data, String relation, int tryCount, int retryInterval) {
        return rpcBridge.requestObject(method, data, relation, tryCount, retryInterval);
    }

    public static void reLoginByBroadcast() {
        try {
            context.sendBroadcast(new Intent(IntentUtil.ACTION_ALIPAY_RE_LOGIN));
        } catch (Throwable th) {
            Log.i(TAG, "sesame sendBroadcast reLogin err:");
            Log.printStackTrace(TAG, th);
        }
    }

    public static void restartByBroadcast() {
        try {
            context.sendBroadcast(new Intent(IntentUtil.ACTION_ALIPAY_RESTART));
        } catch (Throwable th) {
            Log.i(TAG, "sesame sendBroadcast restart err:");
            Log.printStackTrace(TAG, th);
        }
    }

    public static Object getMicroApplicationContext() {
        if (microApplicationContextObject == null) {
            microApplicationContextObject = XposedHelpers.callMethod(
                    XposedHelpers.callStaticMethod(
                            XposedHelpers.findClass(
                                    "com.alipay.mobile.framework.AlipayApplication", classLoader),
                            "getInstance"),
                    "getMicroApplicationContext");
        }
        return microApplicationContextObject;
    }

    public static Object getServiceObject(String service) {
        try {
            return XposedHelpers.callMethod(getMicroApplicationContext(), "findServiceByInterface", service);
        } catch (Throwable th) {
            Log.i(TAG, "getServiceObject err");
            Log.printStackTrace(TAG, th);
        }
        return null;
    }

    public static Object getUserObject() {
        try {
            return XposedHelpers.callMethod(
                    getServiceObject(XposedHelpers.findClass(
                            "com.alipay.mobile.personalbase.service.SocialSdkContactService",
                            classLoader).getName()),
                    "getMyAccountInfoModelByLocal");
        } catch (Throwable th) {
            Log.i(TAG, "getUserObject err");
            Log.printStackTrace(TAG, th);
        }
        return null;
    }

    public static String getUserId() {
        try {
            Object userObject = getUserObject();
            if (userObject != null) {
                return (String) XposedHelpers.getObjectField(userObject, "userId");
            }
        } catch (Throwable th) {
            Log.i(TAG, "getUserId err");
            Log.printStackTrace(TAG, th);
        }
        return null;
    }
}
