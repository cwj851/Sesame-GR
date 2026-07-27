package io.github.lazyimmortal.sesame.model.task.antOrchard;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.BooleanModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.IntegerModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.entity.idAndName.AlipayUser;
import io.github.lazyimmortal.sesame.model.base.TaskCommon;
import io.github.lazyimmortal.sesame.model.extensions.messagePush.MessagePush;
import io.github.lazyimmortal.sesame.model.task.antFarm.AntFarm.TaskStatus;
import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.NotificationUtil;
import io.github.lazyimmortal.sesame.util.RandomUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class AntOrchard extends ModelTask {
    private static final String TAG = AntOrchard.class.getSimpleName();

    private String userId;
    private String treeLevel;

    private String[] wuaList;

    private IntegerModelField executeInterval;
    private BooleanModelField orchardListTask;
    private IntegerModelField orchardSpreadManureCount;
    private BooleanModelField batchHireAnimal;
    private SelectModelField dontHireList;
    private SelectModelField dontWeedingList;
    private BooleanModelField assistFriend;
    private SelectModelField assistFriendList;

    @Override
    public String getName() {
        return "农场";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.ORCHARD;
    }

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(executeInterval = new IntegerModelField("executeInterval", "执行间隔(毫秒)", 500, 500, Integer.MAX_VALUE));
        modelFields.addField(orchardListTask = new BooleanModelField("orchardListTask", "农场任务", false));
        modelFields.addField(orchardSpreadManureCount = new IntegerModelField("orchardSpreadManureCount", "农场每日施肥次数", 0));
        modelFields.addField(assistFriend = new BooleanModelField("assistFriend", "分享助力 | 开启", false));
        modelFields.addField(assistFriendList = new SelectModelField("assistFriendList", "分享助力 | 好友列表", new LinkedHashSet<>(), AlipayUser::getList));
        modelFields.addField(batchHireAnimal = new BooleanModelField("batchHireAnimal", "一键捉鸡除草", false));
        modelFields.addField(dontHireList = new SelectModelField("dontHireList", "除草 | 不雇佣好友列表", new LinkedHashSet<>(), AlipayUser::getList));
        modelFields.addField(dontWeedingList = new SelectModelField("dontWeedingList", "除草 | 不除草好友列表", new LinkedHashSet<>(), AlipayUser::getList));
        return modelFields;
    }

    @Override
    public Boolean check() {
        if (TaskCommon.IS_ENERGY_TIME) {
            Log.farm("任务暂停⏸️芭芭农场:当前为只收能量时间");
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            NotificationUtil.sendTaskNotification(this);
            JSONObject jo = new JSONObject(AntOrchardRpcCall.orchardIndex());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            if (!jo.optBoolean("userOpenOrchard")) {
                getEnableField().setValue(false);
                Log.record("请先开启芭芭农场！");
            }

            JSONObject joo = new JSONObject(AntOrchardRpcCall.mowGrassInfo());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            userId = joo.getString("userId");
            if (jo.has("lotteryPlusInfo")) {
                drawLotteryPlus(jo.getJSONObject("lotteryPlusInfo"));
            }
            extraInfoGet();
            if (batchHireAnimal.getValue()) {
                if (!joo.optBoolean("hireCountOnceLimit", true)
                        && !joo.optBoolean("hireCountOneDayLimit", true)) {
                    batchHireAnimalRecommend();
                }
            }

            if (orchardListTask.getValue()) {
                orchardListTask();
            }

            // 施肥
            Integer orchardSpreadManureCountValue = orchardSpreadManureCount.getValue();
            if (orchardSpreadManureCountValue > 0 && !Status.hasFlagToday(AntOrchardFlag.spreadManureLimit.flagName())) {
                orchardSpreadManure();
            }
            if (orchardSpreadManureCountValue >= 3 && orchardSpreadManureCountValue < 10) {
                querySubplotsActivity(3);
            } else if (orchardSpreadManureCountValue >= 10) {
                querySubplotsActivity(10);
            }

            // 助力
            if (assistFriend.getValue()) {
                orchardAssistFriend();
            }
        } catch (Throwable t) {
            Log.i(TAG, "start.run err:");
            Log.printStackTrace(TAG, t);
        } finally {
            NotificationUtil.removeTaskNotification(this);
        }
    }

    private String getWua() {
        if (wuaList == null) {
            try {
                String content = FileUtil.readFromFile(FileUtil.getWuaFile());
                wuaList = content.split("\n");
            } catch (Throwable ignored) {
                wuaList = new String[0];
            }
        }
        if (wuaList.length > 0) {
            return wuaList[RandomUtil.nextInt(0, wuaList.length - 1)];
        }
        return "null";
    }

    private boolean canSpreadManureContinue(int stageBefore, int stageAfter) {
        if (stageAfter - stageBefore > 1) {
            return true;
        }
        Log.record("施肥只加0.01%进度今日停止施肥！");
        return false;
    }

    private void orchardSpreadManure() {
        try {
            do {
                try {
                    JSONObject jo = new JSONObject(AntOrchardRpcCall.orchardIndex());
                    if (!MessageUtil.checkResponse(TAG, jo)) {
                        return;
                    }
                    if (jo.has("spreadManureActivity")) {
                        JSONObject spreadManureStage = jo.getJSONObject("spreadManureActivity")
                                .getJSONObject("spreadManureStage");
                        if ("FINISHED".equals(spreadManureStage.getString("status"))) {
                            String sceneCode = spreadManureStage.getString("sceneCode");
                            String taskType = spreadManureStage.getString("taskType");
                            int awardCount = spreadManureStage.getInt("awardCount");
                            JSONObject joo = new JSONObject(AntOrchardRpcCall.receiveTaskAward(sceneCode, taskType));
                            if (joo.optBoolean("success")) {
                                Log.farm("丰收礼包🎁[肥料*" + awardCount + "]");
                            } else {
                                Log.record(joo.getString("desc"));
                                Log.i(joo.toString());
                            }
                        }
                    }
                    String taobaoData = jo.getString("taobaoData");
                    jo = new JSONObject(taobaoData);
                    JSONObject plantInfo = jo.getJSONObject("gameInfo").getJSONObject("plantInfo");
                    boolean canExchange = plantInfo.getBoolean("canExchange");
                    if (canExchange) {
                        Log.farm("农场果树似乎可以兑换了！");
                        String exchangeUrl = plantInfo.getString("exchangeUrl");
                        FileUtil.saveExchangeUrl(exchangeUrl);
                        Context context = ApplicationHook.getContext();
                        context.sendBroadcast(new Intent("com.eg.android.AlipayGphone.exchange"));
                        MessagePush.sendMessage(
                                MessagePush.MessagePushChannel.ANT_ORCHARD,
                                UserIdMap.getMaskName(userId) + ">>芭芭农场果树似乎可以兑换了！"
                        );
                        return;
                    }
                    JSONObject seedStage = plantInfo.getJSONObject("seedStage");
                    if (seedStage.has("seedCode")) {
                        treeLevel = Integer.toString(seedStage.getInt("stageLevel"));
                        JSONObject accountInfo = jo.getJSONObject("gameInfo").getJSONObject("accountInfo");
                        int happyPoint = Integer.parseInt(accountInfo.getString("happyPoint"));
                        int wateringCost = accountInfo.getInt("wateringCost");
                        int wateringLeftTimes = accountInfo.getInt("wateringLeftTimes");
                        if (happyPoint > wateringCost && wateringLeftTimes > 0
                                && (200 - wateringLeftTimes < orchardSpreadManureCount.getValue())) {
                            jo = new JSONObject(AntOrchardRpcCall.orchardSpreadManure(getWua()));
                            if (!MessageUtil.checkResponse(TAG, jo)) {
                                return;
                            }
                            taobaoData = jo.getString("taobaoData");
                            jo = new JSONObject(taobaoData);
                            String stageText = jo.getJSONObject("currentStage").getString("stageText");
                            Log.farm("农场施肥💩[" + stageText + "]");
                            if (!canSpreadManureContinue(seedStage.getInt("totalValue"), jo.getJSONObject("currentStage").getInt("totalValue"))) {
                                Status.flagToday(AntOrchardFlag.spreadManureLimit.flagName());
                                return;
                            }
                            continue;
                        }
                    }else {
                        treeLevel = "-1";
                        jo = new JSONObject(AntOrchardRpcCall.orchardSelectSeed());
                        if (MessageUtil.checkResultCodeString(TAG, jo)) {
                            Log.farm("农场奖励🧧[选择红包]");
                            TimeUtil.sleep(500);
                            orchardSpreadManure();
                        }
                    }
                } finally {
                    TimeUtil.sleep(executeInterval.getValue());
                }
                break;
            } while (true);
        } catch (Throwable t) {
            Log.i(TAG, "orchardSpreadManure err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void extraInfoGet() {
        try {
            JSONObject jo = new JSONObject(AntOrchardRpcCall.extraInfoGet());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject fertilizerPacket = jo.getJSONObject("data")
                    .getJSONObject("extraData")
                    .getJSONObject("fertilizerPacket");
            if (!Objects.equals("todayFertilizerWaitTake", fertilizerPacket.getString("status"))) {
                return;
            }
            int todayFertilizerNum = fertilizerPacket.getInt("todayFertilizerNum");
            jo = new JSONObject(AntOrchardRpcCall.extraInfoSet());
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.farm("每日肥料💩[" + todayFertilizerNum + "g]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "extraInfoGet err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void drawLotteryPlus(JSONObject lotteryPlusInfo) {
        if (!lotteryPlusInfo.has("userSevenDaysGiftsItem")) {
            return;
        }
        try {
            String itemId = lotteryPlusInfo.getString("itemId");
            JSONObject jo = lotteryPlusInfo.getJSONObject("userSevenDaysGiftsItem");
            JSONArray ja = jo.getJSONArray("userEverydayGiftItems");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                if (Objects.equals(itemId, jo.getString("itemId"))) {
                    if (jo.getBoolean("received")) {
                        Log.record("七日礼包已领取");
                        break;
                    }
                    jo = new JSONObject(AntOrchardRpcCall.drawLottery());
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        JSONArray userEverydayGiftItems = jo.getJSONObject("lotteryPlusInfo")
                                .getJSONObject("userSevenDaysGiftsItem").getJSONArray("userEverydayGiftItems");
                        for (int j = 0; j < userEverydayGiftItems.length(); j++) {
                            jo = userEverydayGiftItems.getJSONObject(j);
                            if (Objects.equals(itemId, jo.getString("itemId"))) {
                                int awardCount = jo.optInt("awardCount", 1);
                                Log.farm("芭芭农场🎁七日礼包#获得[" + awardCount + "g肥料]");
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "drawLotteryPlus err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void orchardListTask() {
        try {
            JSONObject jo = new JSONObject(AntOrchardRpcCall.orchardListTask());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            if (jo.has("signTaskInfo")) {
                orchardSign(jo.getJSONObject("signTaskInfo"));
            }
            JSONArray ja = jo.getJSONArray("taskList");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                String taskStatus = jo.getString("taskStatus");
                if (TaskStatus.RECEIVED.name().equals(taskStatus)) {
                    continue;
                }
                if (TaskStatus.TODO.name().equals(taskStatus)) {
                    if (!finishOrchardTask(jo)) {
                        continue;
                    }
                    TimeUtil.sleep(500);
                }
                String taskId = jo.getString("taskId");
                String taskPlantType = jo.getString("taskPlantType");
                String title = jo.getJSONObject("taskDisplayConfig").getString("title");
                triggerTbTask(taskId, taskPlantType, title);
            }
        } catch (Throwable t) {
            Log.i(TAG, "orchardListTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void orchardSign(JSONObject signTaskInfo) {
        if (Status.hasFlagToday(AntOrchardFlag.sign.flagName())) {
            return;
        }
        try {
            boolean signed = signTaskInfo.getJSONObject("currentSignItem").getBoolean("signed");
            if (!signed) {
                JSONObject jo = new JSONObject(AntOrchardRpcCall.orchardSign());
                if (MessageUtil.checkResponse(TAG, jo)) {
                    jo = jo.getJSONObject("signTaskInfo").getJSONObject("currentSignItem");
                    int currentContinuousCount = jo.getInt("currentContinuousCount");
                    int awardCount = jo.getInt("awardCount");
                    Log.farm("农场任务📅七天签到[第" + currentContinuousCount + "天]#获得[" + awardCount + "g肥料]");
                    signed = true;
                }
            } else {
                Log.record("农场今日已签到");
            }
            if (signed) {
                Status.flagToday(AntOrchardFlag.sign.flagName());
            }
        } catch (Throwable t) {
            Log.i(TAG, "orchardSign err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static Boolean finishOrchardTask(JSONObject task) {
        try {
            String title = task.getJSONObject("taskDisplayConfig").getString("title");
            String actionType = task.getString("actionType");
            if (Objects.equals("TRIGGER", actionType)
                    || Objects.equals("ADD_HOME", actionType)
                    || Objects.equals("PUSH_SUBSCRIBE", actionType)) {
                String taskId = task.getString("taskId");
                String sceneCode = task.getString("sceneCode");
                JSONObject jo = new JSONObject(AntOrchardRpcCall.finishTask(sceneCode, taskId));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.farm("农场任务🧾完成任务[" + title + "]");
                    return true;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "finishOrchardTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void triggerTbTask(String taskId, String taskPlantType, String title) {
        try {
            JSONObject jo = new JSONObject(AntOrchardRpcCall.triggerTbTask(taskId, taskPlantType));
            if (MessageUtil.checkResponse(TAG, jo)) {
                int incAwardCount = jo.getInt("incAwardCount");
                Log.farm("农场任务🎖️领取奖励[" + title + "]#获得[" + incAwardCount + "g肥料]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "triggerTbTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void querySubplotsActivity(int taskRequire) {
        try {
            if (!"-1".equals(treeLevel)) {
            JSONObject jo = new JSONObject(AntOrchardRpcCall.querySubplotsActivity(treeLevel));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray subplotsActivityList = jo.getJSONArray("subplotsActivityList");
            for (int i = 0; i < subplotsActivityList.length(); i++) {
                jo = subplotsActivityList.getJSONObject(i);
                if (!"WISH".equals(jo.getString("activityType")))
                    continue;
                String activityId = jo.getString("activityId");
                if ("NOT_STARTED".equals(jo.getString("status"))) {
                    String extend = jo.getString("extend");
                    jo = new JSONObject(extend);
                    JSONArray wishActivityOptionList = jo.getJSONArray("wishActivityOptionList");
                    String optionKey = null;
                    for (int j = 0; j < wishActivityOptionList.length(); j++) {
                        jo = wishActivityOptionList.getJSONObject(j);
                        if (taskRequire == jo.getInt("taskRequire")) {
                            optionKey = jo.getString("optionKey");
                            break;
                        }
                    }
                    if (optionKey != null) {
                        jo = new JSONObject(
                                AntOrchardRpcCall.triggerSubplotsActivity(activityId, "WISH", optionKey)
                        );
                        if (MessageUtil.checkResponse(TAG, jo)) {
                            Log.farm("农场许愿✨[每日施肥" + taskRequire + "次]");
                        }
                    }
                } else if ("FINISHED".equals(jo.getString("status"))) {
                    jo = new JSONObject(AntOrchardRpcCall.receiveOrchardRights(activityId, "WISH"));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Log.farm("许愿奖励✨[肥料" + jo.getInt("amount") + "g]");
                        querySubplotsActivity(taskRequire);
                        return;
                    }
                }
            }
            }
        } catch (Throwable t) {
            Log.i(TAG, "triggerTbTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void batchHireAnimalRecommend() {
        try {
            JSONObject jo = new JSONObject(AntOrchardRpcCall.batchHireAnimalRecommend(UserIdMap.getCurrentUid()));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray recommendGroupList = jo.optJSONArray("recommendGroupList");
            if (recommendGroupList != null && recommendGroupList.length() > 0) {
                List<String> GroupList = new ArrayList<>();
                for (int i = 0; i < recommendGroupList.length(); i++) {
                    jo = recommendGroupList.getJSONObject(i);
                    String animalUserId = jo.getString("animalUserId");
                    if (dontHireList.contains(animalUserId)) {
                        continue;
                    }
                    int earnManureCount = jo.getInt("earnManureCount");
                    String groupId = jo.getString("groupId");
                    String orchardUserId = jo.getString("orchardUserId");
                    if (dontWeedingList.contains(orchardUserId)) {
                        continue;
                    }
                    GroupList.add("{\"animalUserId\":\"" + animalUserId + "\",\"earnManureCount\":"
                            + earnManureCount + ",\"groupId\":\"" + groupId + "\",\"orchardUserId\":\""
                            + orchardUserId + "\"}");
                }
                if (!GroupList.isEmpty()) {
                    jo = new JSONObject(AntOrchardRpcCall.batchHireAnimal(GroupList));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Log.farm("一键捉鸡🐣[除草]");
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "batchHireAnimalRecommend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 助力
    private void orchardAssistFriend() {
        if (Status.hasFlagToday(AntOrchardFlag.shareP2PLimit.flagName())) {
            return;
        }
        try {
            for (String friendUserId : assistFriendList.getValue()) {
                if (!Status.canOrchardShareP2PToday(friendUserId)) {
                    continue;
                }
                JSONObject jo = new JSONObject(AntOrchardRpcCall.achieveBeShareP2P(friendUserId));
                TimeUtil.sleep(5000);
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.farm("农场助力🎉成功助力[" + UserIdMap.getMaskName(friendUserId) + "]");
                    Status.orchardShareP2PToday(friendUserId);
                } else if (Objects.equals("600000027", jo.getString("code"))) {
                    Status.flagToday(AntOrchardFlag.shareP2PLimit.flagName());
                    return;
                } else {
                    Status.flagToday(AntOrchardFlag.shareP2PLimit.flagName(friendUserId));
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "orchardAssistFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private enum AntOrchardFlag implements Status.StatusFlag {
        sign,
        shareP2PLimit,
        spreadManureLimit
    }
}