package io.github.lazyimmortal.sesame.hook;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import io.github.lazyimmortal.sesame.data.AlipayInfo;
import io.github.lazyimmortal.sesame.data.ConfigV2;
import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModuleInfo;
import io.github.lazyimmortal.sesame.data.extensions.ExtensionsModel;
import io.github.lazyimmortal.sesame.data.task.BaseTask;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.FriendWatch;
import io.github.lazyimmortal.sesame.model.base.TaskCommon;
import io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandle;
import io.github.lazyimmortal.sesame.model.normal.base.BaseModel;
import io.github.lazyimmortal.sesame.model.task.antMember.AntMemberRpcCall;
import io.github.lazyimmortal.sesame.rpc.bridge.NewRpcBridge;
import io.github.lazyimmortal.sesame.rpc.bridge.OldRpcBridge;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.util.ClassUtil;
import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.HandlerUtil;
import io.github.lazyimmortal.sesame.util.LibraryUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.NotificationUtil;
import io.github.lazyimmortal.sesame.util.PermissionUtil;
import io.github.lazyimmortal.sesame.util.Statistics;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.ThreadUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

/**
 * 负责所有 Xposed Hook 的注册，将原本集中在 ApplicationHook 中的 Hook 注册逻辑拆分出来。
 */
class HookRegistry {

    private static final String TAG = HookRegistry.class.getSimpleName();

    static void attachHooks(ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                ApplicationHook.context = (Context) param.args[0];
                super.afterHookedMethod(param);
            }
        });
    }

    static void matchVersionHook(ClassLoader classLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                    "com.alipay.mobile.nebulaappproxy.api.rpc.H5AppRpcUpdate", classLoader,
                    "matchVersion", classLoader.loadClass(ClassUtil.H5PAGE_NAME), Map.class, String.class,
                    XC_MethodReplacement.returnConstant(false));
            Log.i(TAG, "hook matchVersion successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook matchVersion err:");
            Log.printStackTrace(TAG, t);
        }
    }

    static void loginHook(ClassLoader classLoader) {
        try {
            XposedHelpers.findAndHookMethod("com.alipay.mobile.quinox.LauncherActivity", classLoader,
                    "onResume", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            Log.i(TAG, "Activity onResume");
                            String targetUid = ApplicationHook.getUserId();
                            if (targetUid == null) {
                                Log.record("用户未登录");
                                ToastUtil.show(ApplicationHook.context, "用户未登录");
                                return;
                            }
                            if (!ApplicationHook.init) {
                                ThreadUtil.start(() -> {
                                    if (!ApplicationHook.running
                                            && ApplicationHook.lifecycleManager.initHandler(true)) {
                                        ApplicationHook.init = true;
                                    }
                                });
                                return;
                            }
                            String currentUid = UserIdMap.getCurrentUid();
                            if (!targetUid.equals(currentUid)) {
                                if (currentUid != null) {
                                    ThreadUtil.start(() -> {
                                        ApplicationHook.lifecycleManager.initHandler(true);
                                        Log.record("用户已切换");
                                        ToastUtil.show(ApplicationHook.context, "用户已切换");
                                    });
                                    return;
                                }
                                UserIdMap.initUser(targetUid);
                            }
                            if (ApplicationHook.offline) {
                                ApplicationHook.offline = false;
                                ApplicationHook.lifecycleManager.execHandler();
                                ((Activity) param.thisObject).finish();
                                Log.i(TAG, "Activity reLogin");
                            }
                        }
                    });
            Log.i(TAG, "hook login successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook login err:");
            Log.printStackTrace(TAG, t);
        }
    }

    static void serviceOnCreateHook(ClassLoader classLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                    "android.app.Service", classLoader, "onCreate", new XC_MethodHook() {

                        @SuppressWarnings("UnsafeDynamicallyLoadedCode")
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            Service appService = (Service) param.thisObject;
                            if (!ClassUtil.CURRENT_USING_SERVICE.equals(
                                    appService.getClass().getCanonicalName())) {
                                return;
                            }
                            Log.i(TAG, "Service onCreate");
                            ApplicationHook.context = appService.getApplicationContext();
                            System.load(LibraryUtil.getLibSesamePath(ApplicationHook.context));
                            ApplicationHook.service = appService;
                            ApplicationHook.mainTask = BaseTask.newInstance("MAIN_TASK", new Runnable() {

                                private volatile long lastExecTime = 0;

                                @Override
                                public void run() {
                                    if (!ApplicationHook.init) {
                                        return;
                                    }
                                    Log.record("应用版本：" + AlipayInfo.getVersionNameAndCode());
                                    Log.record("模块版本：" + ModuleInfo.getVersionNameAndCode());
                                    Log.record("开始执行");
                                    try {
                                        int checkInterval = BaseModel.getCheckInterval().getValue();
                                        if (lastExecTime + 2000 > System.currentTimeMillis()) {
                                            Log.record("执行间隔较短，跳过执行");
                                            ApplicationHook.lifecycleManager.execDelayedHandler(checkInterval);
                                            return;
                                        }
                                        ApplicationHook.lifecycleManager.updateDay();
                                        String targetUid = ApplicationHook.getUserId();
                                        String currentUid = UserIdMap.getCurrentUid();
                                        if (targetUid == null || currentUid == null) {
                                            Log.record("用户为空，放弃执行");
                                            ApplicationHook.lifecycleManager.reLogin();
                                            return;
                                        }
                                        if (!targetUid.equals(currentUid)) {
                                            Log.record("开始切换用户");
                                            ToastUtil.show(ApplicationHook.context, "开始切换用户");
                                            ApplicationHook.lifecycleManager.reLogin();
                                            return;
                                        }
                                        lastExecTime = System.currentTimeMillis();
                                        try {
                                        FutureTask<Boolean> checkTask =
                                                new FutureTask<>(AntMemberRpcCall::check);
                                            Thread checkThread = new Thread(checkTask);
                                            checkThread.start();
                                            if (!checkTask.get(10, TimeUnit.SECONDS)) {
                                                long waitTime = 10000 - System.currentTimeMillis() + lastExecTime;
                                                if (waitTime > 0) {
                                                    Thread.sleep(waitTime);
                                                }
                                                Log.record("执行失败：检查超时");
                                                ApplicationHook.lifecycleManager.reLogin();
                                                return;
                                            }
                                            ApplicationHook.reLoginCount.set(0);
                                        } catch (InterruptedException
                                                 | ExecutionException
                                                 | TimeoutException e) {
                                            Log.record("执行失败：检查中断");
                                            ApplicationHook.lifecycleManager.reLogin();
                                            return;
                                        } catch (Exception e) {
                                            Log.record("执行失败：检查异常");
                                            ApplicationHook.lifecycleManager.reLogin();
                                            Log.printStackTrace(TAG, e);
                                            return;
                                        }
                                        TaskCommon.update();
                                        ModelTask.startAllTask();
                                        lastExecTime = System.currentTimeMillis();

                                        try {
                                            List<String> execAtTimeList =
                                                    BaseModel.getExecAtTimeList().getValue();
                                            if (execAtTimeList != null) {
                                                Calendar lastExecTimeCalendar =
                                                        TimeUtil.getCalendarByTimeMillis(lastExecTime);
                                                Calendar nextExecTimeCalendar =
                                                        TimeUtil.getCalendarByTimeMillis(lastExecTime + checkInterval);
                                                for (String execAtTime : execAtTimeList) {
                                                    Calendar execAtTimeCalendar =
                                                            TimeUtil.getTodayCalendarByTimeStr(execAtTime);
                                                    if (execAtTimeCalendar != null
                                                            && lastExecTimeCalendar.compareTo(execAtTimeCalendar) < 0
                                                            && nextExecTimeCalendar.compareTo(execAtTimeCalendar) > 0) {
                                                        Log.record("设置定时执行:" + execAtTime);
                                                        ApplicationHook.lifecycleManager.execDelayedHandler(
                                                                execAtTimeCalendar.getTimeInMillis() - lastExecTime);
                                                        FileUtil.clearLog();
                                                        return;
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            Log.i(TAG, "execAtTime err:");
                                            Log.printStackTrace(TAG, e);
                                        }

                                        ApplicationHook.lifecycleManager.execDelayedHandler(checkInterval);
                                        FileUtil.clearLog();
                                    } catch (Exception e) {
                                        Log.record("执行异常:");
                                        Log.printStackTrace(e);
                                    }
                                }
                            });
                            ApplicationHook.lifecycleManager.registerBroadcastReceiver(appService);
                            ApplicationHook.dayCalendar = Calendar.getInstance();
                            Statistics.load();
                            FriendWatch.load();
                            if (ApplicationHook.lifecycleManager.initHandler(true)) {
                                ApplicationHook.init = true;
                            }
                        }
                    });
            Log.i(TAG, "hook service onCreate successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook service onCreate err:");
            Log.printStackTrace(TAG, t);
        }
    }

    static void serviceOnDestroyHook(ClassLoader classLoader) {
        try {
            XposedHelpers.findAndHookMethod("android.app.Service", classLoader, "onDestroy",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            Service service = (Service) param.thisObject;
                            if (!ClassUtil.CURRENT_USING_SERVICE.equals(
                                    service.getClass().getCanonicalName())) {
                                return;
                            }
                            Log.record("支付宝前台服务被销毁");
                            NotificationUtil.updateNotification("支付宝前台服务被销毁");
                            ApplicationHook.lifecycleManager.destroyHandler(true);
                            FriendWatch.unload();
                            Statistics.unload();
                            ApplicationHook.restartByBroadcast();
                        }
                    });
        } catch (Throwable t) {
            Log.i(TAG, "hook service onDestroy err:");
            Log.printStackTrace(TAG, t);
        }
    }

    static void foregroundHooks(ClassLoader classLoader) {
        try {
            XposedHelpers.findAndHookMethod(
                    "com.alipay.mobile.common.fgbg.FgBgMonitorImpl", classLoader,
                    "isInBackground", XC_MethodReplacement.returnConstant(false));
        } catch (Throwable t) {
            Log.i(TAG, "hook FgBgMonitorImpl method 1 err:");
            Log.printStackTrace(TAG, t);
        }
        try {
            XposedHelpers.findAndHookMethod(
                    "com.alipay.mobile.common.fgbg.FgBgMonitorImpl", classLoader,
                    "isInBackground", boolean.class, XC_MethodReplacement.returnConstant(false));
        } catch (Throwable t) {
            Log.i(TAG, "hook FgBgMonitorImpl method 2 err:");
            Log.printStackTrace(TAG, t);
        }
        try {
            XposedHelpers.findAndHookMethod(
                    "com.alipay.mobile.common.fgbg.FgBgMonitorImpl", classLoader,
                    "isInBackgroundV2", XC_MethodReplacement.returnConstant(false));
        } catch (Throwable t) {
            Log.i(TAG, "hook FgBgMonitorImpl method 3 err:");
            Log.printStackTrace(TAG, t);
        }
        try {
            XposedHelpers.findAndHookMethod(
                    "com.alipay.mobile.common.transport.utils.MiscUtils", classLoader,
                    "isAtFrontDesk", classLoader.loadClass("android.content.Context"),
                    XC_MethodReplacement.returnConstant(true));
            Log.i(TAG, "hook MiscUtils successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook MiscUtils err:");
            Log.printStackTrace(TAG, t);
        }
    }

    static void rpcRecordHooks(ClassLoader classLoader) {
        try {
            ApplicationHook.rpcRequestUnhook = XposedHelpers.findAndHookMethod(
                    "com.alibaba.ariver.commonability.network.rpc.RpcBridgeExtension", classLoader,
                    "rpc",
                    String.class, boolean.class, boolean.class, String.class,
                    classLoader.loadClass(ClassUtil.JSON_OBJECT_NAME), String.class,
                    classLoader.loadClass(ClassUtil.JSON_OBJECT_NAME), boolean.class, boolean.class,
                    int.class, boolean.class, String.class,
                    classLoader.loadClass("com.alibaba.ariver.app.api.App"),
                    classLoader.loadClass("com.alibaba.ariver.app.api.Page"),
                    classLoader.loadClass("com.alibaba.ariver.engine.api.bridge.model.ApiContext"),
                    classLoader.loadClass("com.alibaba.ariver.engine.api.bridge.extension.BridgeCallback"),
                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Object[] args = param.args;
                            Object object = args[15];
                            Object[] recordArray = new Object[4];
                            recordArray[0] = System.currentTimeMillis();
                            recordArray[1] = args[0];
                            recordArray[2] = args[4];
                            ApplicationHook.rpcHookMap.put(object, recordArray);
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Object object = param.args[15];
                            Object[] recordArray = ApplicationHook.rpcHookMap.remove(object);
                            ExtensionsHandle.handleRequest(new Request(
                                    RequestType.RECORD_RUNTIME_INFO,
                                    recordArray)
                            );
                            if (!BaseModel.getDebugMode().getValue()) {
                                return;
                            }
                            if (recordArray != null) {
                                Log.debug("记录\n时间: " + recordArray[0] + "\n方法: " + recordArray[1]
                                        + "\n参数: " + recordArray[2] + "\n数据: " + recordArray[3] + "\n");
                            } else {
                                Log.debug("删除记录ID: " + object.hashCode());
                            }
                        }
                    });
            Log.i(TAG, "hook record request successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook record request err:");
            Log.printStackTrace(TAG, t);
        }
        try {
            ApplicationHook.rpcResponseUnhook = XposedHelpers.findAndHookMethod(
                    "com.alibaba.ariver.engine.common.bridge.internal.DefaultBridgeCallback", classLoader,
                    "sendJSONResponse",
                    classLoader.loadClass(ClassUtil.JSON_OBJECT_NAME),
                    new XC_MethodHook() {

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            Object object = param.thisObject;
                            Object[] recordArray = ApplicationHook.rpcHookMap.get(object);
                            if (recordArray != null) {
                                recordArray[3] = String.valueOf(param.args[0]);
                            }
                        }
                    });
            Log.i(TAG, "hook record response successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook record response err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
