package io.github.lazyimmortal.sesame.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.ServiceCompat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import io.github.lazyimmortal.sesame.data.ConfigV2;
import io.github.lazyimmortal.sesame.data.RuntimeInfo;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.model.normal.base.BaseModel;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class NotificationUtil {

    private static final String CHANNEL_ID = "io.github.lazyimmortal.sesame.NOTIFICATION_CHANNEL";
    private static final int SESAME_NOTIFICATION_ID = 2003_10_15;
    private static final int ANT_FOREST_NOTIFICATION_ID = 2016_08_27;
    private static final int[] NOTIFICATION_ID_ARRAY = {SESAME_NOTIFICATION_ID, ANT_FOREST_NOTIFICATION_ID};
    private static final AtomicInteger notificationIdGenerator = new AtomicInteger(SESAME_NOTIFICATION_ID);
    private static final Map<Object, Integer> notificationIdMap = new ConcurrentHashMap<>();

    // 创建通知
    public static Notification createNotification(Context context, String title, CharSequence text, boolean isAutoCancel) {
        // 如果是 Android 8.0 及以上版本，先创建通知频道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("alipays://platformapi/startapp?appId="));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // 使用 NotificationCompat.Builder 创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        boolean isOngoing = !isAutoCancel
                && BaseModel.getSendNotificationOptions().contains(
                BaseModel.SendNotificationOption.SEND_ONGOING_NOTIFICATION.name()
        );
        builder.setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.sym_def_app_icon) // 设置图标
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), android.R.drawable.sym_def_app_icon))
                .setCategory(NotificationCompat.CATEGORY_STATUS)
                .setPriority(NotificationCompat.PRIORITY_LOW) // 设置优先级
                .setAutoCancel(isAutoCancel) // 设置点击后自动消失
                .setOngoing(isOngoing);
        return builder.build();
    }

    // 创建通知频道
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context) {
        // 如果通知渠道不存在，则创建它
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // 创建通知渠道
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "仙人掌",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setShowBadge(false);
            notificationChannel.setDescription("自动化辅助工具");
            // 注册通知渠道
            notificationManager.createNotificationChannel(notificationChannel);
        }
        // 移除过时的通知频道
        if (BaseModel.getSendNotificationOptions().contains(BaseModel.SendNotificationOption.REMOVE_NOTIFICATION_CHANNEL.name())) {
            for (NotificationChannel channel : notificationManager.getNotificationChannels()) {
                if (channel.getId().contains("ANTFOREST_NOTIFY_CHANNEL")) {
                    notificationManager.deleteNotificationChannel(channel.getId());
                }
            }
            BaseModel.getSendNotificationOptions().remove(BaseModel.SendNotificationOption.REMOVE_NOTIFICATION_CHANNEL.name());
            ConfigV2.save(UserIdMap.getCurrentUid(), false);
        }
    }

    // 发送通知
    public static void sendNotification(Context context, int notificationId, String title, CharSequence text, boolean isAutoCancel) {
        if (!BaseModel.getSendNotification().getValue()) {
            return;
        }
        // 创建新的通知内容
        Notification notification = createNotification(context, title, text, isAutoCancel);

        if (context instanceof Service
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE
                && notificationId == SESAME_NOTIFICATION_ID) {
            ((Service) context).startForeground(notificationId, notification);
        } else {
            // 获取通知管理器
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager == null) {
                return;
            }
            // 通过通知 ID 显示通知，ID 相同的通知会覆盖旧的通知
            notificationManager.notify(notificationId, notification);
        }
    }

    public static void sendNotification(Context context, int notificationId, String title, CharSequence text) {
        sendNotification(context, notificationId, title, text, false);
    }

    public static void removeAllNotification(Context context) {
        for (int notificationId : NOTIFICATION_ID_ARRAY) {
            removeNotification(context, notificationId);
        }
        for (Map.Entry<Object, Integer> entry : notificationIdMap.entrySet()) {
            removeNotification(context, entry.getValue());
        }
    }

    // 移除通知
    public static void removeNotification(Context context, int notificationId) {
        if (context instanceof Service
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE
                && notificationId == SESAME_NOTIFICATION_ID) {
            ServiceCompat.stopForeground((Service) context, ServiceCompat.STOP_FOREGROUND_REMOVE);
        } else {
            // 获取通知管理器
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // 如果通知管理器不为 null，调用 cancel() 方法移除通知
            if (notificationManager != null) {
                notificationManager.cancel(notificationId);  // 使用通知 ID 取消通知
            }
        }
    }

    public static void initNotification(Context context) {
        removeAllNotification(context);
        sendNotification(context, SESAME_NOTIFICATION_ID, "仙人掌", "正在运行");
    }

    public static void updateNotification(long nextExecTime) {
        CharSequence text = "下次执行时间：" + (nextExecTime > 0 ? TimeUtil.getTimeStr(nextExecTime) : "暂无计划");
        updateNotification(text);
    }

    public static void updateNotification(CharSequence text) {
        sendNotification(ApplicationHook.getContext(), SESAME_NOTIFICATION_ID, "仙人掌", text);
        sendAntForestErrorNotification();
    }

    public static void sendAntForestNotification(int totalCollected, int totalHelpCollected) {
        String content = "收:" + totalCollected + " 帮:" + totalHelpCollected;
        String text = "上次执行时间：" + TimeUtil.getTimeStr(System.currentTimeMillis()) + "（" + content + "）";
        sendAntForestNotification(text);
    }

    public static void sendAntForestNotification(CharSequence text) {
        sendNotification(ApplicationHook.getContext(), ANT_FOREST_NOTIFICATION_ID, "蚂蚁森林", text);
        sendAntForestErrorNotification();
    }

    public static void sendAntForestErrorNotification() {
        long forestPauseTime = RuntimeInfo.getInstance().getLong(RuntimeInfo.RuntimeInfoKey.ForestPauseTime);
        if (forestPauseTime > System.currentTimeMillis()) {
            CharSequence text = "触发异常等待：" + TimeUtil.getCommonDateTime(forestPauseTime);
            sendNotification(ApplicationHook.getContext(), ANT_FOREST_NOTIFICATION_ID, "蚂蚁森林", text);
            Log.record("触发异常,等待至" + TimeUtil.getCommonDateTime(forestPauseTime));
        }
    }

    public static void sendTaskNotification(ModelTask modelTask) {
        if (!BaseModel.getSendNotificationOptions().contains(BaseModel.SendNotificationOption.SEND_RUNTIME_NOTIFICATION.name())) {
            return;
        }
        sendNotification(ApplicationHook.getContext(), getNotificationId(modelTask), modelTask.getName(), "正在运行");
    }

    public static void removeTaskNotification(ModelTask modelTask) {
        removeNotification(ApplicationHook.getContext(), getNotificationId(modelTask));
    }

    public static synchronized int getNotificationId(Object object) {
        Integer notificationId = notificationIdMap.get(object);
        if (notificationId == null) {
            notificationId = notificationIdGenerator.incrementAndGet();
            notificationIdMap.put(object, notificationId);
        }
        return notificationId;
    }
}
