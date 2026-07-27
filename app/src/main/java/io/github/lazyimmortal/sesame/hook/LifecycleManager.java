package io.github.lazyimmortal.sesame.hook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Objects;

import io.github.lazyimmortal.sesame.data.ConfigV2;
import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModuleInfo;
import io.github.lazyimmortal.sesame.data.extensions.ExtensionsModel;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.FriendWatch;
import io.github.lazyimmortal.sesame.model.normal.base.BaseModel;
import io.github.lazyimmortal.sesame.rpc.bridge.NewRpcBridge;
import io.github.lazyimmortal.sesame.rpc.bridge.OldRpcBridge;
import io.github.lazyimmortal.sesame.rpc.intervallimit.RpcIntervalLimit;
import io.github.lazyimmortal.sesame.rpc.request.RequestHandler;
import io.github.lazyimmortal.sesame.util.ClassUtil;
import io.github.lazyimmortal.sesame.util.HandlerUtil;
import io.github.lazyimmortal.sesame.util.IntentUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.NotificationUtil;
import io.github.lazyimmortal.sesame.util.PermissionUtil;
import io.github.lazyimmortal.sesame.util.Statistics;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

/**
 * 负责模块生命周期管理：初始化、销毁、执行调度、日期更新、重新登录、广播接收器注册。
 */
class LifecycleManager {

    private static final String TAG = LifecycleManager.class.getSimpleName();

    private final AlarmTaskManager alarmTaskManager = new AlarmTaskManager();

    synchronized Boolean initHandler(Boolean force) {
        if (ApplicationHook.service == null) {
            return false;
        }
        ApplicationHook.running = true;
        destroyHandler(force);
        try {
            if (force) {
                String userId = ApplicationHook.getUserId();
                if (userId == null) {
                    Log.record("用户未登录");
                    ToastUtil.show(ApplicationHook.context, "用户未登录");
                    return false;
                }
                if (!PermissionUtil.checkAlarmPermissions()) {
                    Log.record("支付宝无闹钟权限");
                    HandlerUtil.postDelayed(() -> {
                        if (!PermissionUtil.checkOrRequestAlarmPermissions(ApplicationHook.context)) {
                            ToastUtil.show(ApplicationHook.context, "请授予支付宝使用闹钟权限");
                        }
                    }, 2000);
                    return false;
                }
                UserIdMap.initUser(userId);
                Model.initAllModel();
                ExtensionsModel.initAllModel();
                Log.record("模块版本：" + ModuleInfo.getVersionNameAndCode());
                Log.record("开始加载");
                ConfigV2.load(userId);
                if (!Model.getModel(BaseModel.class).getEnableField().getValue()) {
                    Log.record("仙人掌已禁用");
                    ToastUtil.show(ApplicationHook.context, "仙人掌已禁用");
                    return false;
                }
                if (BaseModel.getBatteryPerm().getValue() && !ApplicationHook.init
                        && !PermissionUtil.checkBatteryPermissions()) {
                    Log.record("支付宝无始终在后台运行权限");
                    HandlerUtil.postDelayed(() -> {
                        if (!PermissionUtil.checkOrRequestBatteryPermissions(ApplicationHook.context)) {
                            ToastUtil.show(ApplicationHook.context, "请授予支付宝终在后台运行权限");
                        }
                    }, 2000);
                }
                if (BaseModel.getNewRpc().getValue()) {
                    ApplicationHook.rpcBridge = new NewRpcBridge();
                } else {
                    ApplicationHook.rpcBridge = new OldRpcBridge();
                }
                ApplicationHook.rpcBridge.load();
                ApplicationHook.rpcVersion = ApplicationHook.rpcBridge.getVersion();
                if (BaseModel.getStayAwake().getValue()) {
                    try {
                        PowerManager pm = (PowerManager) ApplicationHook.service.getSystemService(
                                Context.POWER_SERVICE);
                        ApplicationHook.wakeLock = pm.newWakeLock(
                                PowerManager.PARTIAL_WAKE_LOCK,
                                ApplicationHook.service.getClass().getName());
                        ApplicationHook.wakeLock.acquire();
                    } catch (Throwable t) {
                        Log.printStackTrace(t);
                    }
                }
                alarmTaskManager.setWakenAtTimeAlarm();
                if (BaseModel.getNewRpc().getValue()) {
                    HookRegistry.rpcRecordHooks(ApplicationHook.classLoader);
                }
                NotificationUtil.initNotification(ApplicationHook.service);
                Model.bootAllModel(ApplicationHook.classLoader);
                Status.load();
                updateDay();
                BaseModel.initData();
                Log.record("加载完成");
                ToastUtil.show(ApplicationHook.context, "仙人掌加载成功");
            }
            ApplicationHook.offline = false;
            execHandler();
            return true;
        } catch (Throwable th) {
            Log.i(TAG, "startHandler err:");
            Log.printStackTrace(TAG, th);
            ToastUtil.show(ApplicationHook.context, "仙人掌加载失败");
            return false;
        } finally {
            ApplicationHook.running = false;
        }
    }

    synchronized void destroyHandler(Boolean force) {
        try {
            if (force) {
                if (ApplicationHook.service != null) {
                    stopHandler();
                    BaseModel.destroyData();
                    Status.unload();
                    NotificationUtil.removeAllNotification(ApplicationHook.service);
                    RpcIntervalLimit.clearIntervalLimit();
                    ConfigV2.unload();
                    Model.destroyAllModel();
                    UserIdMap.unload();
                }
                if (ApplicationHook.rpcResponseUnhook != null) {
                    try {
                        ApplicationHook.rpcResponseUnhook.unhook();
                    } catch (Exception e) {
                        Log.printStackTrace(e);
                    }
                }
                if (ApplicationHook.rpcRequestUnhook != null) {
                    try {
                        ApplicationHook.rpcRequestUnhook.unhook();
                    } catch (Exception e) {
                        Log.printStackTrace(e);
                    }
                }
                if (ApplicationHook.wakeLock != null && ApplicationHook.wakeLock.isHeld()) {
                    ApplicationHook.wakeLock.release();
                    ApplicationHook.wakeLock = null;
                }
                if (ApplicationHook.rpcBridge != null) {
                    ApplicationHook.rpcVersion = null;
                    ApplicationHook.rpcBridge.unload();
                    ApplicationHook.rpcBridge = null;
                }
            } else {
                ModelTask.stopAllTask();
            }
        } catch (Throwable th) {
            Log.i(TAG, "stopHandler err:");
            Log.printStackTrace(TAG, th);
        }
    }

    void execHandler() {
        ApplicationHook.mainTask.startTask();
    }

    void execDelayedHandler(long delayMillis) {
        HandlerUtil.postDelayed(() -> ApplicationHook.mainTask.startTask(), delayMillis);
        try {
            NotificationUtil.updateNotification(System.currentTimeMillis() + delayMillis);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
    }

    private void stopHandler() {
        ApplicationHook.mainTask.stopTask();
        ModelTask.stopAllTask();
    }

    void updateDay() {
        Calendar nowCalendar = Calendar.getInstance();
        try {
            int nowYear = nowCalendar.get(Calendar.YEAR);
            int nowMonth = nowCalendar.get(Calendar.MONTH);
            int nowDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
            if (ApplicationHook.dayCalendar.get(Calendar.YEAR) != nowYear
                    || ApplicationHook.dayCalendar.get(Calendar.MONTH) != nowMonth
                    || ApplicationHook.dayCalendar.get(Calendar.DAY_OF_MONTH) != nowDay) {
                ApplicationHook.dayCalendar = (Calendar) nowCalendar.clone();
                ApplicationHook.dayCalendar.set(Calendar.HOUR_OF_DAY, 0);
                ApplicationHook.dayCalendar.set(Calendar.MINUTE, 0);
                ApplicationHook.dayCalendar.set(Calendar.SECOND, 0);
                Log.record("日期更新为：" + nowYear + "-" + (nowMonth + 1) + "-" + nowDay);
                alarmTaskManager.setWakenAtTimeAlarm();
            }
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        try {
            Statistics.save(nowCalendar);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        try {
            Status.save(nowCalendar);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        try {
            FriendWatch.updateDay();
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
    }

    void reLogin() {
        HandlerUtil.post(() -> {
            if (ApplicationHook.reLoginCount.get() < 5) {
                execDelayedHandler(ApplicationHook.reLoginCount.getAndIncrement() * 5000L);
            } else {
                execDelayedHandler(Math.max(BaseModel.getCheckInterval().getValue(), 180_000));
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName(ClassUtil.PACKAGE_NAME, ClassUtil.CURRENT_USING_ACTIVITY);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ApplicationHook.offline = true;
            ApplicationHook.context.startActivity(intent);
        });
    }

    void registerBroadcastReceiver(Context context) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(IntentUtil.ACTION_ALIPAY_RESTART);
            intentFilter.addAction(IntentUtil.ACTION_ALIPAY_EXECUTE);
            intentFilter.addAction(IntentUtil.ACTION_ALIPAY_RE_LOGIN);
            intentFilter.addAction(IntentUtil.ACTION_ALIPAY_STATUS);
            intentFilter.addAction(IntentUtil.ACTION_ALIPAY_REQUEST);
            ContextCompat.registerReceiver(context, new AlipayBroadcastReceiver(),
                    intentFilter, ContextCompat.RECEIVER_EXPORTED);
            Log.i(TAG, "hook registerBroadcastReceiver successfully");
        } catch (Throwable th) {
            Log.i(TAG, "hook registerBroadcastReceiver err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private class AlipayBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("sesame broadcast action:" + action + " intent:" + intent);
            if (action == null) {
                return;
            }
            switch (action) {
                case IntentUtil.ACTION_ALIPAY_RESTART:
                    String userId = intent.getStringExtra("userId");
                    if (StringUtil.isEmpty(userId)
                            || Objects.equals(UserIdMap.getCurrentUid(), userId)) {
                        initHandler(true);
                    }
                    break;
                case IntentUtil.ACTION_ALIPAY_EXECUTE:
                    initHandler(false);
                    break;
                case IntentUtil.ACTION_ALIPAY_RE_LOGIN:
                    reLogin();
                    break;
                case IntentUtil.ACTION_ALIPAY_STATUS:
                    try {
                        context.sendBroadcast(new Intent(IntentUtil.ACTION_SESAME_STATUS));
                    } catch (Throwable th) {
                        Log.i(TAG, "sesame sendBroadcast status err:");
                        Log.printStackTrace(TAG, th);
                    }
                    break;
                case IntentUtil.ACTION_ALIPAY_REQUEST:
                    try {
                        String method = intent.getStringExtra("method");
                        String data = intent.getStringExtra("data");
                        String type = intent.getStringExtra("type");
                        RequestHandler.start(method, data, type);
                    } catch (Throwable th) {
                        Log.i(TAG, "sesame request err:");
                        Log.printStackTrace(TAG, th);
                    }
                    break;
            }
        }
    }
}
