package io.github.lazyimmortal.sesame.model.normal.base;

import java.util.LinkedHashSet;

import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.BooleanModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.ChoiceModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.IntegerModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.ListModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectOneModelField;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.model.task.protectEcology.ProtectEcology;
import io.github.lazyimmortal.sesame.util.ListUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.ThreadUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.AnimalIdMap;
import io.github.lazyimmortal.sesame.util.idMap.BeachIdMap;
import io.github.lazyimmortal.sesame.util.idMap.MarathonIdMap;
import io.github.lazyimmortal.sesame.util.idMap.NewAncientTreeIdMap;
import io.github.lazyimmortal.sesame.util.idMap.ReserveIdMap;
import io.github.lazyimmortal.sesame.util.idMap.TreeIdMap;
import lombok.Getter;

/**
 * 基础配置模块
 */
public class BaseModel extends Model {

    @Getter
    private static final BooleanModelField stayAwake = new BooleanModelField("stayAwake", "保持唤醒", true);
    @Getter
    private static final IntegerModelField.MultiplyIntegerModelField checkInterval = new IntegerModelField.MultiplyIntegerModelField("checkInterval", "执行间隔(分钟)", 50, 1, 12 * 60, 60_000);
    @Getter
    private static final ListModelField.ListJoinCommaToStringModelField execAtTimeList = new ListModelField.ListJoinCommaToStringModelField("execAtTimeList", "定时执行(关闭:-1)", ListUtil.newArrayList("065530", "2359", "24"));
    @Getter
    private static final ListModelField.ListJoinCommaToStringModelField wakenAtTimeList = new ListModelField.ListJoinCommaToStringModelField("wakenAtTimeList", "定时唤醒(关闭:-1)", ListUtil.newArrayList("0650", "2350"));
    @Getter
    private static final ListModelField.ListJoinCommaToStringModelField energyTime = new ListModelField.ListJoinCommaToStringModelField("energyTime", "只收能量时间(范围)", ListUtil.newArrayList("0700-0731"));
    @Getter
    private static final ChoiceModelField timedTaskModel = new ChoiceModelField("timedTaskModel", "定时任务模式", TimedTaskModel.SYSTEM, TimedTaskModel.nickNames);
    @Getter
    private static final BooleanModelField timeoutRestart = new BooleanModelField("timeoutRestart", "超时重启", true);
    @Getter
    private static final IntegerModelField.MultiplyIntegerModelField waitWhenException = new IntegerModelField.MultiplyIntegerModelField("waitWhenException", "异常等待时间(分钟)", 60, 0, 24 * 60, 60_000);
    @Getter
    private static final BooleanModelField newRpc = new BooleanModelField("newRpc", "使用新接口(最低支持v10.3.96.8100)", true);
    @Getter
    private static final BooleanModelField debugMode = new BooleanModelField("debugMode", "开启抓包(基于新接口)", false);
    @Getter
    private static final BooleanModelField batteryPerm = new BooleanModelField("batteryPerm", "为支付宝申请后台运行权限", true);
    @Getter
    private static final BooleanModelField recordLog = new BooleanModelField("recordLog", "记录日志", true);
    @Getter
    private static final BooleanModelField showToast = new BooleanModelField("showToast", "气泡提示", true);
    @Getter
    private static final IntegerModelField toastOffsetY = new IntegerModelField("toastOffsetY", "气泡纵向偏移", 0);
    @Getter
    private static final BooleanModelField sendNotification = new BooleanModelField("sendNotification", "发送通知 | 开启", false);
    @Getter
    private static final SelectModelField sendNotificationOptions = new SelectModelField("sendNotificationOptions", "发送通知 | 选项", new LinkedHashSet<>(), SendNotificationOption.class);
    private static final SelectOneModelField blockCaptchaDialogOptions = new SelectOneModelField("blockCaptchaDialogOptions", "屏蔽弹窗 | 选项", null, CaptchaDialogOption.class);

    @Override
    public String getName() {
        return "基础";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.BASE;
    }

    @Override
    public String getEnableFieldName() {
        return "启用模块";
    }

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(stayAwake);
        modelFields.addField(checkInterval);
        modelFields.addField(execAtTimeList);
        modelFields.addField(wakenAtTimeList);
        modelFields.addField(energyTime);
        modelFields.addField(timedTaskModel);
        modelFields.addField(timeoutRestart);
        modelFields.addField(waitWhenException);
        modelFields.addField(newRpc);
        modelFields.addField(debugMode);
        modelFields.addField(batteryPerm);
        modelFields.addField(recordLog);
        modelFields.addField(showToast);
        modelFields.addField(sendNotification);
        modelFields.addField(sendNotificationOptions);
        modelFields.addField(toastOffsetY);
        modelFields.addField(blockCaptchaDialogOptions);
        return modelFields;
    }

    public static long getNextRunTime() {
        return BaseModel.waitWhenException.getValue() + System.currentTimeMillis();
    }

    public static int getBlockCaptchaDialogMode() {
        if (blockCaptchaDialogOptions.contains(CaptchaDialogOption.NORMAL_CAPTCHA_DIALOG.name())) {
            return 1;
        } else if (blockCaptchaDialogOptions.contains(CaptchaDialogOption.SLIDE_CAPTCHA_DIALOG.name())) {
            return 2;
        } else {
            return 0;
        }
    }


    public static void initData() {
        ThreadUtil.start(() -> {
            try {
                TimeUtil.sleep(5000);
                ProtectEcology.initForest();
                ProtectEcology.initOcean();
            } catch (Exception e) {
                Log.printStackTrace(e);
            }
        });
    }

    public static void destroyData() {
        try {
            TreeIdMap.getInstance().clear();
            ReserveIdMap.getInstance().clear();
            AnimalIdMap.getInstance().clear();
            MarathonIdMap.getInstance().clear();
            NewAncientTreeIdMap.getInstance().clear();
            BeachIdMap.getInstance().clear();
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
    }

    public interface TimedTaskModel {

        int SYSTEM = 0;

        int PROGRAM = 1;

        String[] nickNames = {"系统计时", "程序计时"};

    }

    public enum SendNotificationOption implements CustomOption {
        SEND_ERROR_NOTIFICATION("发送请求异常通知"),
        SEND_RUNTIME_NOTIFICATION("发送任务运行通知"),
        SEND_ONGOING_NOTIFICATION("通知常驻通知栏"),
        REMOVE_NOTIFICATION_CHANNEL("移除过时通知频道(只会执行一次)");

        private final String nickName;

        SendNotificationOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    public enum CaptchaDialogOption implements CustomOption {
        NORMAL_CAPTCHA_DIALOG("普通验证"),
        SLIDE_CAPTCHA_DIALOG("滑动验证");

        private final String nickName;

        CaptchaDialogOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
        }

}
