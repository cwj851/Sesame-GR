package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import io.github.lazyimmortal.sesame.data.RuntimeInfo;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.model.extensions.messagePush.MessagePush;
import io.github.lazyimmortal.sesame.model.task.antFarm.AntFarm;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.NotificationUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class AntFishPond {
    private static final String TAG = AntFishPond.class.getSimpleName();
    private static SelectModelField antFishPondOptions;

    public static void run() {
        antFishPondOptions = ModelTask.getModel(Immortal.class).antFishPondOptions;
        if (!fishpondIndex()) {
            Log.record("福气鱼塘未开通");
            return;
        }
        if (antFishPondOptions.contains(AntFishPondOption.GIFT_BOX.name())
                || antFishPondOptions.contains(AntFishPondOption.TOMORROW_ROD.name())) {
            querySubplotsActivity();
        }
        if (antFishPondOptions.contains(AntFishPondOption.SIGN.name())
                || antFishPondOptions.contains(AntFishPondOption.TASK.name())) {
            listTask();
        }
        if (antFishPondOptions.contains(AntFishPondOption.FISHING.name())
                || antFishPondOptions.contains(AntFishPondOption.LOTTERY_PLUS.name())
                || antFishPondOptions.contains(AntFishPondOption.EXCHANGE_REWARD.name())) {
            fishpondSyncIndex();
        }
    }

    // 检查福气鱼塘是否已经开通
    private static Boolean fishpondIndex() {
        try {
            JSONObject jo = new JSONObject(AntFishPondRpcCall.fishpondIndex());
            if (MessageUtil.checkResponse(TAG, jo)) {
                return jo.optBoolean("open");
            }
        } catch (Throwable t) {
            Log.i(TAG, "fishpondIndex error:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void querySubplotsActivity() {
        try {
            JSONObject jo = new JSONObject(AntFishPondRpcCall.querySubplotsActivity());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray subplotsActivityList = jo.getJSONArray("subplotsActivityList");
            for (int i = 0; i < subplotsActivityList.length(); i++) {
                jo = subplotsActivityList.getJSONObject(i);
                if (Objects.equals("GIFT_BOX", jo.getString("activityType"))
                        && Objects.equals("TODO", jo.getString("status"))
                        && antFishPondOptions.contains(AntFishPondOption.GIFT_BOX.name())
                ) {
                    openGiftBox();
                } else if (Objects.equals("TOMORROW_ROD", jo.getString("activityType"))
                        && Objects.equals("TODAY_TODO", jo.getString("status"))
                        && antFishPondOptions.contains(AntFishPondOption.TOMORROW_ROD.name())
                ) {
                    tomorrowRod();
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "querySubplotsActivity error:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void openGiftBox() {
        try {
            JSONObject jo = new JSONObject(AntFishPondRpcCall.triggerSubplotsActivity("receiveAward", "GIFT_BOX"));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("triggerSubplotsActivity");
                String awardCount = new JSONObject(jo.getString("extend")).getString("awardCount");
                Log.farm("福气鱼塘🐟触发奖励[每日宝箱]#获得[" + awardCount + "根钓竿]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "openGiftBox error:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void tomorrowRod() {
        try {
            JSONObject jo = new JSONObject(AntFishPondRpcCall.triggerSubplotsActivity("FINISH", "TOMORROW_ROD"));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("triggerSubplotsActivity");
                int receivedRodCount = new JSONObject(jo.getString("extend")).getInt("receivedRodCount");
                Log.farm("福气鱼塘🐟触发奖励[明日钓竿]#获得[" + receivedRodCount + "根钓竿]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "tomorrowRod error:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void lotteryPlus(JSONObject lotteryPlusInfo) {
        if (lotteryPlusInfo == null
                || !antFishPondOptions.contains(AntFishPondOption.LOTTERY_PLUS.name())
                || Status.hasFlagToday(AntFishPondFlag.LOTTERY_PLUS.flagName())) {
            return;
        }
        try {
            long startTime = lotteryPlusInfo.getLong("startTime");
            long endTime = lotteryPlusInfo.getLong("endTime");
            String itemId = lotteryPlusInfo.getString("itemId");
            JSONArray userEverydayGiftItems = lotteryPlusInfo
                    .getJSONObject("userSevenDaysGiftsItem")
                    .getJSONArray("userEverydayGiftItems");
            for (int i = 0; i < userEverydayGiftItems.length(); i++) {
                JSONObject jo = userEverydayGiftItems.getJSONObject(i);
                if (!Objects.equals(itemId, jo.getString("itemId"))) {
                    continue;
                }
                if (!jo.getBoolean("received")
                        && System.currentTimeMillis() >= startTime
                        && System.currentTimeMillis() <= endTime) {
                    jo = new JSONObject(AntFishPondRpcCall.triggerSubplotsActivity("RECEIVE", "LOTTERY_PLUS"));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Status.flagToday(AntFishPondFlag.LOTTERY_PLUS.flagName());
                        jo = jo.getJSONObject("triggerSubplotsActivity");
                        String targetRewardCount = new JSONObject(jo.getString("extend")).getString("targetRewardCount");
                        Log.farm("福气鱼塘🐟触发奖励[七日红包]#获得[" + targetRewardCount + "元]");
                    }
                }
                return;
            }
        } catch (Throwable t) {
            Log.i(TAG, "lotteryPlus error:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static Boolean fishpondExchangeReward() {
        if (!antFishPondOptions.contains(AntFishPondOption.EXCHANGE_REWARD.name())) {
            return false;
        }
        try {
            JSONObject jo = new JSONObject(AntFishPondRpcCall.fishpondExchangeReward());
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("exchangeRewardResult");
                String targetRewardCount = jo.getString("targetRewardCount");
                Log.farm("福气鱼塘🐟兑换奖励[支付红包]#获得[" + targetRewardCount + "元]");
                MessagePush.sendMessage(
                        MessagePush.MessagePushChannel.SPECIAL_EVENT,
                        UserIdMap.getMaskName(UserIdMap.getCurrentUid()) + ">>福气鱼塘兑换了[" + targetRewardCount + "]元红包"
                );
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "fishpondExchangeReward error:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void fishpondSyncIndex() {
        try {
            do {
                JSONObject jo = new JSONObject(AntFishPondRpcCall.fishpondSyncIndex());
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return;
                }
                lotteryPlus(jo.optJSONObject("lotteryPlusInfo"));
                if (jo.has("lastAdInfo")) {
                    // 输出奖励记录
                    JSONObject lastAdInfo = jo.getJSONObject("lastAdInfo");
                    if (Objects.equals("ADFISH", lastAdInfo.getString("awardType"))) {
                        String awardCount = lastAdInfo.getString("awardCount");
                        Log.farm("福气鱼塘🐟触发奖励[广告奖励]#获得[(玩一玩奖励)广告鱼(" + awardCount + "斤)]");
                    } else if (Objects.equals("1", lastAdInfo.getString("awardType"))) {
                        String awardCount = lastAdInfo.getString("awardCount");
                        Log.farm("福气鱼塘🐟触发奖励[广告奖励]#获得[" + awardCount + "根钓竿]");
                    }
                }
                if (jo.getJSONObject("roundInfo").getBoolean("canExchange")) {
                    if (!fishpondExchangeReward()) {
                        Log.farm("福气鱼塘🐟存在未领取的红包");
                        return;
                    }
                }
                int remainAngleCount = jo.getJSONObject("tomorrowRod").optInt("remainAngleCount", 1);
                // 获取钓竿数量
                int rodSumCount = jo.getInt("rodSumCount");
                if (jo.has("fishActivity")) {
                    jo = jo.getJSONObject("fishActivity");
                    if (Objects.equals("FINISHED", jo.getString("status"))) {
                        String awardCount = jo.getString("awardCount");
                        jo = new JSONObject(AntFishPondRpcCall.triggerSubplotsActivity("receiveAward", "FISH_ACTIVITY"));
                        if (MessageUtil.checkResponse(TAG, jo)) {
                            Log.farm("福气鱼塘🐟触发奖励[累计垂钓]#获得[" + awardCount + "根钓竿]");
                            jo = new JSONObject(jo.getJSONObject("triggerSubplotsActivity").getString("extend"));
                            // 更新钓竿数
                            rodSumCount = jo.getInt("rodSumCount");
                            if (fishpondAdNotice(jo.optJSONObject("adInfo"))) {
                                continue;
                            }
                        }
                    }
                }
                if (rodSumCount < remainAngleCount
                        || Status.hasFlagToday(AntFishPondFlag.FISHING_LIMIT.flagName())) {
                    return;
                }
            } while (angle());
        } catch (Throwable t) {
            Log.i(TAG, "fishpondSyncIndex error:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 检查垂钓Token是否失效
    private static Boolean isAngleTokenExpiry() {
        return RuntimeInfo.getInstance().getLong(RuntimeInfo.RuntimeInfoKey.FishPondAngleTokenUpdateTime)
                <= RuntimeInfo.getInstance().getLong(RuntimeInfo.RuntimeInfoKey.FishPondAngleTokenExpiryTime);
    }

    private static Boolean angle() {
        if (!antFishPondOptions.contains(AntFishPondOption.FISHING.name())) {
            return false;
        }
        if (isAngleTokenExpiry()) {
            NotificationUtil.sendNotification(
                    ApplicationHook.getContext(),
                    NotificationUtil.getNotificationId(AntFishPond.class),
                    "福气鱼塘",
                    "垂钓福鱼Token已失效，请手动垂钓一次",
                    true
            );
            Log.farm("福气鱼塘🐟请先手动垂钓一次");
            MessagePush.sendMessage(
                    MessagePush.MessagePushChannel.SPECIAL_EVENT,
                    UserIdMap.getMaskName(UserIdMap.getCurrentUid()) + ">>垂钓福鱼Token已失效，请手动垂钓一次"
            );
            return false;
        }
        try {
            JSONObject jo = new JSONObject(AntFishPondRpcCall.fishpondAngle());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                if (Objects.equals("C05", jo.getString("resultCode"))) {
                    Status.flagToday(AntFishPondFlag.FISHING_LIMIT.flagName());
                }
                return false;
            }
            String bizNo = jo.getJSONObject("angleResultInfo").getString("bizNo");
            boolean needUserClick = jo.getJSONObject("angleResultInfo").getBoolean("needUserClick");
            boolean needRodPositioning = jo.getBoolean("needRodPositioning") && jo.has("moveSegments");
            if (needRodPositioning) {
                // 需要定位区域
                jo = new JSONObject(AntFishPondRpcCall.fishpondAngleRodPositioning("SPECIAL_BIG_ZONE", bizNo));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return false;
                }
            } else if (needUserClick) {
                // 需要用户点击
                jo = new JSONObject(AntFishPondRpcCall.fishpondAngleUserCheck(bizNo, true));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return false;
                }
            }
            jo = jo.getJSONObject("angleResultInfo");
            String fishName = jo.getString("fishName");
            String fishType = jo.getString("fishType");
            try {
                fishType = FishType.valueOf(fishType).nickName;
            } catch (IllegalArgumentException e) {
                Log.record("发现未知鱼类:(" + fishType + ")" + fishName);
            }
            String fishWeight = jo.getString("fishWeight");
            if (Objects.equals("0.01", fishWeight)) {
                RuntimeInfo.getInstance().put(RuntimeInfo.RuntimeInfoKey.FishPondAngleTokenExpiryTime, System.currentTimeMillis());
            }
            Log.farm("福气鱼塘🐟垂钓福鱼#获得[(" + fishType + ")" + fishName + "(" + fishWeight + "斤)]");
            fishpondAdNotice(jo.optJSONObject("angleAdInfo"));
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "angle error:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static Boolean fishpondAdNotice(JSONObject adInfo) {
        if (adInfo == null) {
            return false;
        }
        try {
            JSONObject jo = new JSONObject(AntFishPondRpcCall
                    .fishpondAdNotice(adInfo.getString("adBizNo")));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            TimeUtil.sleep(3000L);
            if (Objects.equals("FISHROD", adInfo.getString("awardType"))) {
                jo = new JSONObject(
                        AntFishPondRpcCall.finishTask(
                                adInfo.getString("sceneCode"),
                                adInfo.getString("taskId"),
                                adInfo.getString("adBizNo")
                        )
                );
            } else if (adInfo.getString("targetUrl").contains("&bizId=")) {
                String bizId = StringUtil.getSubString(adInfo.getString("targetUrl"), "&bizId=", "&");
                jo = new JSONObject(AntFishPondRpcCall.taskFinish(bizId));
            }
            return MessageUtil.checkResponse(TAG, jo);
        } catch (Throwable t) {
            Log.i(TAG, "fishpondAdNotice error:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void listTask() {
        try {
            JSONObject jo = new JSONObject(AntFishPondRpcCall.listTask());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }

            if (sign(jo.getJSONObject("signInfo"))) {
                Status.flagToday(AntFishPondFlag.SIGN.flagName());
            }

            if (!antFishPondOptions
                    .contains(AntFishPondOption.TASK.name())) {
                return;
            }
            JSONArray taskList = jo.getJSONArray("taskList");
            for (int i = 0; i < taskList.length(); i++) {
                jo = taskList.getJSONObject(i);
                AntFarm.TaskStatus taskStatus = AntFarm.TaskStatus.valueOf(jo.getString("taskStatus"));
                if (taskStatus == AntFarm.TaskStatus.RECEIVED) {
                    continue;
                }
                if (taskStatus == AntFarm.TaskStatus.TODO) {
                    if (!finishTask(jo)) {
                        continue;
                    }
                    TimeUtil.sleep(1000);
                }
                if (receiveTaskAward(jo)) {
                    TimeUtil.sleep(1000);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "listTask error:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static Boolean finishTask(JSONObject task) {
        try {
            // 暂时不做分享任务与钓鱼任务
            String actionType = task.getString("actionType");
            if (Objects.equals("SHARE", actionType)
                    || Objects.equals("OFFLINE_SHARE", actionType)
                    || Objects.equals("GOFISH", actionType)) {
                return false;
            }
            int rightsTimes = task.getInt("rightsTimes");
            int rightsTimesLimit = task.getInt("rightsTimesLimit");
            String sceneCode = task.getString("sceneCode");
            String taskId = task.getString("taskId");
            if (Status.hasFlagToday(AntFishPondFlag.CAN_NOT_FINISH_TASK.flagName(taskId))) {
                // 不支持rpc完成的任务
                return false;
            }
            String title = task.getJSONObject("taskDisplayConfig").getString("title");
            for (int i = rightsTimes; i < rightsTimesLimit; i++) {
                JSONObject jo = new JSONObject(AntFishPondRpcCall.finishTask(sceneCode, taskId));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    if (Objects.equals("400000040", jo.optString("code"))) {
                        // 不支持rpc完成的任务
                        Status.flagToday(AntFishPondFlag.CAN_NOT_FINISH_TASK.flagName(taskId));
                    }
                    return false;
                }
                Log.farm("福气鱼塘🐟完成任务[" + title + "]");
                TimeUtil.sleep(15000L);
            }
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "finishTask error:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static Boolean receiveTaskAward(JSONObject task) {
        try {
            String taskId = task.getString("taskId");
            String title = task.getJSONObject("taskDisplayConfig").getString("title");
            JSONObject jo = new JSONObject(AntFishPondRpcCall.receiveTaskAward(taskId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                int incAwardCount = jo.getInt("incAwardCount");
                Log.farm("福气鱼塘🐟领取奖励[" + title + "]#获得[" + incAwardCount + "根钓竿]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveTaskAward error:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static Boolean sign(JSONObject signInfo) {
        if (!antFishPondOptions.contains(AntFishPondOption.SIGN.name())
                || Status.hasFlagToday(AntFishPondFlag.SIGN.flagName())) {
            return false;
        }
        try {
            JSONArray list = signInfo.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject jo = list.getJSONObject(i);
                if (!jo.getBoolean("today")) {
                    // 不是当日
                    continue;
                }
                if (jo.getBoolean("signed")) {
                    Log.record("福气鱼塘今日已签到");
                    return true;
                }
                String signKey = jo.getString("signKey");
                jo = new JSONObject(AntFishPondRpcCall.sign(signKey));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    jo = jo.getJSONObject("signTaskInfo");
                    int awardCount = jo.getInt("awardCount");
                    int currentContinuousCount = jo.getInt("currentContinuousCount");
                    Log.farm("福气鱼塘🐟七日签到[第" + currentContinuousCount + "天]#获得[" + awardCount + "根钓竿]");
                    return true;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "sign error:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    public enum AntFishPondFlag implements Status.StatusFlag {
        SIGN,
        FISHING_LIMIT,
        LOTTERY_PLUS,
        CAN_NOT_FINISH_TASK
    }

    public enum FishType {
        BIG_FISH("传说鱼"),
        SMALL_FISH("稀有鱼"),
        MEDIUM_FISH("普通鱼"),
        WELFARE_FISH("福利鱼"),
        MISS_ROD("失误杆");

        public final String nickName;

        FishType(String nickName) {
            this.nickName = nickName;
        }
    }

    public enum AntFishPondOption implements CustomOption {
        SIGN("七日签到"),
        TASK("鱼塘任务"),
        FISHING("垂钓福鱼"),
        GIFT_BOX("每日宝箱"),
        TOMORROW_ROD("明日钓竿"),
        LOTTERY_PLUS("七日红包"),
        EXCHANGE_REWARD("兑换红包");

        private final String nickName;

        AntFishPondOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }
}
