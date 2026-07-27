package io.github.lazyimmortal.sesame.model.task.antFarm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.BooleanModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.ChoiceModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.IntegerModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.ListModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectAndCountModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.StringModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.AlipayUser;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.entity.idAndName.FarmAchievementOrnament;
import io.github.lazyimmortal.sesame.entity.idAndName.FarmMallItem;
import io.github.lazyimmortal.sesame.model.base.TaskCommon;
import io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandle;
import io.github.lazyimmortal.sesame.model.extensions.messagePush.MessagePush;
import io.github.lazyimmortal.sesame.model.normal.answerAI.AnswerAI;
import io.github.lazyimmortal.sesame.rpc.intervallimit.RpcIntervalLimit;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestMethod;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.ListUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.NotificationUtil;
import io.github.lazyimmortal.sesame.util.RandomUtil;
import io.github.lazyimmortal.sesame.util.Statistics;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.AchievementOrnamentIdMap;
import io.github.lazyimmortal.sesame.util.idMap.MallItemIdMap;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;
import lombok.Getter;

public class AntFarm extends ModelTask {
    private static final String TAG = AntFarm.class.getSimpleName();

    private String ownerFarmId;
    private String ownerUserId;
    private Animal[] animals;
    private Animal ownerAnimal = new Animal();
    private int foodStock;
    private int foodStockLimit;
    private String rewardProductNum;
    private RewardFriend[] rewardList;
    private double benevolenceScore;
    private double harvestBenevolenceScore;
    private int unReceiveTaskAward = 0;
    private int foodInTrough = 0;

    private final List<FarmTool> farmToolList = new ArrayList<>();

    @Override
    public String getName() {
        return "庄园";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.FARM;
    }

    private StringModelField sleepTime;
    private IntegerModelField sleepMinutes;
    private BooleanModelField feedAnimal;
    private BooleanModelField rewardFriend;
    private ChoiceModelField sendBackAnimalWay;
    private ChoiceModelField sendBackAnimalType;
    private SelectModelField sendBackAnimalList;
    private ChoiceModelField recallAnimalType;
    private BooleanModelField receiveFarmToolReward;
    private BooleanModelField farmGame;
    private SelectModelField farmGameOptions;
    private ListModelField.ListJoinCommaToStringModelField farmGameTime;
    @Getter
    private SelectAndCountModelField farmMallItemList;
    private BooleanModelField kitchen;
    private BooleanModelField useSpecialFood;
    @Getter
    private IntegerModelField useSpecialFoodCountLimit;
    private BooleanModelField useFarmTool;
    @Getter
    private SelectAndCountModelField useFarmToolList;
    private BooleanModelField harvestProduce;
    private ChoiceModelField donationType;
    private IntegerModelField donationAmount;
    private BooleanModelField receiveFarmTaskAward;
    private SelectModelField useAccelerateToolOptions;
    private BooleanModelField feedFriendAnimal;
    private SelectAndCountModelField feedFriendAnimalList;
    private ChoiceModelField notifyFriendType;
    private SelectModelField notifyFriendList;
    private BooleanModelField acceptGift;
    private SelectAndCountModelField visitFriendList;
    private BooleanModelField chickenDiary;
    private BooleanModelField drawMachine;
    private BooleanModelField ornamentsDressUp;
    private SelectModelField ornamentsDressUpList;
    private IntegerModelField ornamentsDressUpDays;
    private ChoiceModelField hireAnimalType;
    private SelectModelField hireAnimalList;
    private ChoiceModelField getFeedType;
    private SelectModelField getFeedList;
    private BooleanModelField family;
    private SelectModelField familyOptions;

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(useFarmTool = new BooleanModelField("useFarmTool", "使用道具 | 开启", false));
        modelFields.addField(useFarmToolList = new SelectAndCountModelField("useFarmToolList", "使用道具 | 道具列表", new LinkedHashMap<>(), ToolType.class, "请填写使用次数(每日)"));
        modelFields.addField(useAccelerateToolOptions = new SelectModelField("useAccelerateToolOptions", "加速卡 | 选项", new LinkedHashSet<>(), AntFarmAccelerateToolOption.class));
        modelFields.addField(useSpecialFood = new BooleanModelField("useSpecialFood", "特殊食品 | 使用", false));
        modelFields.addField(useSpecialFoodCountLimit = new IntegerModelField("useSpecialFoodCountLimit", "特殊食品 | 使用上限(无限:0)", 0));
        modelFields.addField(rewardFriend = new BooleanModelField("rewardFriend", "打赏好友", false));
        modelFields.addField(recallAnimalType = new ChoiceModelField("recallAnimalType", "召回小鸡", RecallAnimalType.ALWAYS, RecallAnimalType.nickNames));
        modelFields.addField(feedAnimal = new BooleanModelField("feedAnimal", "投喂小鸡", false));
        modelFields.addField(feedFriendAnimal = new BooleanModelField("feedFriendAnimal", "帮喂小鸡 | 开启", true));
        modelFields.addField(feedFriendAnimalList = new SelectAndCountModelField("feedFriendAnimalList", "帮喂小鸡 | 好友列表", new LinkedHashMap<>(), AlipayUser::getList, "请填写帮喂次数(每日)"));
        modelFields.addField(hireAnimalType = new ChoiceModelField("hireAnimalType", "雇佣小鸡 | 动作", HireAnimalType.NONE, HireAnimalType.nickNames));
        modelFields.addField(hireAnimalList = new SelectModelField("hireAnimalList", "雇佣小鸡 | 好友列表", new LinkedHashSet<>(), AlipayUser::getList));
        modelFields.addField(sendBackAnimalWay = new ChoiceModelField("sendBackAnimalWay", "遣返小鸡 | 方式", SendBackAnimalWay.NORMAL, SendBackAnimalWay.nickNames));
        modelFields.addField(sendBackAnimalType = new ChoiceModelField("sendBackAnimalType", "遣返小鸡 | 动作", SendBackAnimalType.NONE, SendBackAnimalType.nickNames));
        modelFields.addField(sendBackAnimalList = new SelectModelField("sendFriendList", "遣返小鸡 | 好友列表", new LinkedHashSet<>(), AlipayUser::getList));
        modelFields.addField(notifyFriendType = new ChoiceModelField("notifyFriendType", "通知赶鸡 | 动作", NotifyFriendType.NONE, NotifyFriendType.nickNames));
        modelFields.addField(notifyFriendList = new SelectModelField("notifyFriendList", "通知赶鸡 | 好友列表", new LinkedHashSet<>(), AlipayUser::getList));
        modelFields.addField(ornamentsDressUp = new BooleanModelField("ornamentsDressUp", "装扮焕新 | 开启", false));
        modelFields.addField(ornamentsDressUpList = new SelectModelField("ornamentsDressUpList", "装扮焕新 | 套装列表", new LinkedHashSet<>(), FarmAchievementOrnament::getList));
        modelFields.addField(ornamentsDressUpDays = new IntegerModelField("ornamentsDressUpDays", "装扮焕新 | 焕新频率(天)", 7));
        modelFields.addField(drawMachine = new BooleanModelField("drawMachine", "装扮抽抽乐", false));
        modelFields.addField(donationType = new ChoiceModelField("donationType", "每日捐蛋 | 方式", DonationType.ZERO, DonationType.nickNames));
        modelFields.addField(donationAmount = new IntegerModelField("donationAmount", "每日捐蛋 | 倍数(每项)", 1));
        modelFields.addField(family = new BooleanModelField("family", "亲密家庭 | 开启", false));
        modelFields.addField(familyOptions = new SelectModelField("familyOptions", "亲密家庭 | 选项", new LinkedHashSet<>(), AntFarmFamilyOption.class));
        modelFields.addField(sleepTime = new StringModelField("sleepTime", "小鸡睡觉 | 时间(关闭:-1)", "2001"));
        modelFields.addField(sleepMinutes = new IntegerModelField("sleepMinutes", "小鸡睡觉 | 时长(分钟)", 10 * 59, 1, 10 * 60));
        modelFields.addField(farmGame = new BooleanModelField("farmGame", "小鸡乐园 | 开启", false));
        modelFields.addField(farmGameOptions = new SelectModelField("farmGameOptions", "小鸡乐园 | 选项", new LinkedHashSet<>(), AntFarmGameOption.class));
        modelFields.addField(farmGameTime = new ListModelField.ListJoinCommaToStringModelField("farmGameTime", "小鸡乐园 | 游戏时间(范围)", ListUtil.newArrayList("2200-2400")));
        modelFields.addField(farmMallItemList = new SelectAndCountModelField("farmMallItemList", "小鸡乐园 | 乐园集市兑换列表", new LinkedHashMap<>(), FarmMallItem::getList, "请填写兑换次数(每日)"));
        modelFields.addField(kitchen = new BooleanModelField("kitchen", "小鸡厨房", false));
        modelFields.addField(chickenDiary = new BooleanModelField("chickenDiary", "小鸡日记", false));
        modelFields.addField(harvestProduce = new BooleanModelField("harvestProduce", "收取爱心鸡蛋", false));
        modelFields.addField(receiveFarmToolReward = new BooleanModelField("receiveFarmToolReward", "收取道具奖励", false));
        modelFields.addField(receiveFarmTaskAward = new BooleanModelField("receiveFarmTaskAward", "收取饲料奖励", false));
        modelFields.addField(getFeedType = new ChoiceModelField("getFeedType", "一起拿饲料 | 动作", GetFeedType.NONE, GetFeedType.nickNames));
        modelFields.addField(getFeedList = new SelectModelField("getFeedList", "一起拿饲料 | 好友列表", new LinkedHashSet<>(), AlipayUser::getList));
        modelFields.addField(acceptGift = new BooleanModelField("acceptGift", "收取麦子", false));
        modelFields.addField(visitFriendList = new SelectAndCountModelField("visitFriendList", "赠送麦子 | 好友列表", new LinkedHashMap<>(), AlipayUser::getList, "请填写赠送次数(每日)"));
        return modelFields;
    }

    @Override
    public void boot(ClassLoader classLoader) {
        super.boot(classLoader);
        RpcIntervalLimit.addIntervalLimit("com.alipay.antfarm.enterFarm", 2000);
    }

    @Override
    public Boolean check() {
        if (TaskCommon.IS_ENERGY_TIME) {
            Log.farm("任务暂停⏸️蚂蚁庄园:当前为只收能量时间");
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            NotificationUtil.sendTaskNotification(this);
            if (enterFarm() == null) {
                return;
            }

            if (rewardFriend.getValue()) {
                rewardFriend();
            }

            if (sendBackAnimalType.getValue() != SendBackAnimalType.NONE) {
                sendBackAnimal();
            }

            if (recallAnimalType.getValue() != RecallAnimalType.NEVER) {
                recallAnimal();
            }

            if (receiveFarmToolReward.getValue()) {
                listFarmTool();
                listToolTaskDetails();
            }

            if (kitchen.getValue()) {
                collectDailyFoodMaterial(ownerUserId);
                collectDailyLimitedFoodMaterial();
                cook(ownerUserId);
            }

            if (chickenDiary.getValue()) {
                queryChickenDiary("");
                queryChickenDiaryList();
            }

            if (harvestProduce.getValue() && benevolenceScore >= 1) {
                Log.record("有可收取的爱心鸡蛋");
                harvestProduce(ownerFarmId);
            }

            if (donationType.getValue() != DonationType.ZERO) {
                donation();
            }

            if (receiveFarmTaskAward.getValue()) {
                listFarmTask(TaskStatus.TODO);
                listFarmTask(TaskStatus.FINISHED);
            }

            if (feedAnimal.getValue()) {
                feedAnimal();
            }

            if (useFarmTool.getValue()) {
                useFarmTool();
            }

            if (farmGame.getValue()) {
                farmGame();
            }

            // 小鸡换装
            if (ornamentsDressUp.getValue()) {
                ornamentsDressUp();
            }

            // 到访小鸡送礼
            visitAnimal();

            // 送麦子
            visitFriend();

            // 帮好友喂鸡
            if (feedFriendAnimal.getValue()) {
                feedFriend();
            }

            // 通知好友赶鸡
            if (notifyFriendType.getValue() != NotifyFriendType.NONE) {
                notifyFriend();
            }

            // 抽抽乐
            if (drawMachine.getValue()) {
                //drawMachine();
                enterDrawMachine();
                queryDrawMachineActivity();
            }

            // 雇佣小鸡
            if (hireAnimalType.getValue() != HireAnimalType.NONE) {
                autoHireAnimal();
                if (hireAnimal()) {
                    autoFeedAnimal();
                }
            }

            if (getFeedType.getValue() != GetFeedType.NONE) {
                letsGetChickenFeedTogether();
            }

            if (family.getValue()) {
                family();
            }

            //小鸡睡觉&起床
            animalSleepAndWake();

        } catch (Throwable t) {
            Log.i(TAG, "start.run err:");
            Log.printStackTrace(TAG, t);
        } finally {
            NotificationUtil.removeTaskNotification(this);
        }
    }

    private void animalSleepAndWake() {
        String sleepTimeStr = sleepTime.getValue();
        if ("-1".equals(sleepTimeStr)) {
            return;
        }
        animalWakeUpNow();
        Calendar animalSleepTimeCalendar = TimeUtil.getTodayCalendarByTimeStr(sleepTimeStr);
        if (animalSleepTimeCalendar == null) {
            return;
        }
        Integer sleepMinutesInt = sleepMinutes.getValue();
        Calendar animalWakeUpTimeCalendar = (Calendar) animalSleepTimeCalendar.clone();
        animalWakeUpTimeCalendar.add(Calendar.MINUTE, sleepMinutesInt);
        long animalSleepTime = animalSleepTimeCalendar.getTimeInMillis();
        long animalWakeUpTime = animalWakeUpTimeCalendar.getTimeInMillis();
        if (animalSleepTime > animalWakeUpTime) {
            Log.record("小鸡睡觉设置有误，请重新设置");
            return;
        }
        Calendar now = TimeUtil.getNow();
        boolean afterSleepTime = now.compareTo(animalSleepTimeCalendar) > 0;
        boolean afterWakeUpTime = now.compareTo(animalWakeUpTimeCalendar) > 0;
        if (afterSleepTime && afterWakeUpTime) {
            //睡觉时间后
            if (hasSleepToday()) {
                return;
            }
            Log.record("已错过小鸡今日睡觉时间");
            return;
        }
        if (afterSleepTime) {
            //睡觉时间内
            if (!hasSleepToday()) {
                animalSleepNow();
            }
            animalWakeUpTime(animalWakeUpTime);
            return;
        }
        //睡觉时间前
        animalWakeUpTimeCalendar.add(Calendar.HOUR_OF_DAY, -24);
        if (now.compareTo(animalWakeUpTimeCalendar) <= 0) {
            animalWakeUpTime(animalWakeUpTimeCalendar.getTimeInMillis());
        }
        animalSleepTime(animalSleepTime);
        animalWakeUpTime(animalWakeUpTime);
    }

    private JSONObject enterFarm() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterFarm("", UserIdMap.getCurrentUid()));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return null;
            }
            rewardProductNum = jo.getJSONObject("dynamicGlobalConfig").getString("rewardProductNum");
            JSONObject joFarmVO = jo.getJSONObject("farmVO");
            foodStock = joFarmVO.getInt("foodStock");
            foodStockLimit = joFarmVO.getInt("foodStockLimit");
            harvestBenevolenceScore = joFarmVO.getDouble("harvestBenevolenceScore");
            parseSyncAnimalStatusResponse(joFarmVO.toString());
            ownerUserId = joFarmVO.getJSONObject("masterUserInfoVO").getString("userId");

            if (useSpecialFood.getValue()) {
                JSONArray cuisineList = jo.getJSONArray("cuisineList");
                if (AnimalInteractStatus.HOME.name().equals(ownerAnimal.animalInteractStatus)
                        && !AnimalFeedStatus.SLEEPY.name().equals(ownerAnimal.animalFeedStatus)
                        && Status.canUseSpecialFoodToday()) {
                    useFarmFood(cuisineList);
                }
            }

            if (jo.has("lotteryPlusInfo")) {
                drawLotteryPlus(jo.getJSONObject("lotteryPlusInfo"));
            }
            if (acceptGift.getValue() && joFarmVO.getJSONObject("subFarmVO").has("giftRecord")
                    && foodStockLimit - foodStock >= 10) {
                acceptGift();
            }
            return jo;
        } catch (Throwable t) {
            Log.i(TAG, "enterFarm err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    private synchronized void autoFeedAnimal() {
        syncAnimalStatus(ownerFarmId);
        if (!AnimalFeedStatus.EATING.name().equals(ownerAnimal.animalFeedStatus)) {
            return;
        }
        double foodHaveEatten = 0d;
        double consumeSpeed = 0d;
        long nowTime = System.currentTimeMillis();
        for (Animal animal : animals) {
            foodHaveEatten += (nowTime - animal.startEatTime) / 1000 * animal.consumeSpeed;
            consumeSpeed += animal.consumeSpeed;
        }
        long nextFeedTime = nowTime + (long) ((foodInTrough - foodHaveEatten) / consumeSpeed) * 1000;
        String taskId = "FA|" + ownerFarmId;
        if (hasChildTask(taskId) && getChildTask(taskId).getExecTime() == nextFeedTime) {
            return;
        }
        if (addChildTask(new ChildModelTask(taskId, "FA", () -> {
            syncAnimalStatus(ownerFarmId);
            feedAnimal(ownerFarmId);
        }, nextFeedTime))) {
            Log.record("添加蹲点投喂🥣[" + UserIdMap.getCurrentMaskName() + "]在[" + TimeUtil.getCommonDateTime(nextFeedTime) + "]执行");
        }
    }

    private void animalSleepTime(long animalSleepTime) {
        String sleepTaskId = "AS|" + animalSleepTime;
        if (!hasChildTask(sleepTaskId)) {
            addChildTask(new ChildModelTask(sleepTaskId, "AS", this::animalSleepNow, animalSleepTime));
            Log.record("添加定时睡觉🛌[" + UserIdMap.getCurrentMaskName() + "]在[" + TimeUtil.getCommonDateTime(animalSleepTime) + "]执行");
        }
    }

    private void animalWakeUpTime(long animalWakeUpTime) {
        String wakeUpTaskId = "AW|" + animalWakeUpTime;
        if (!hasChildTask(wakeUpTaskId)) {
            addChildTask(new ChildModelTask(wakeUpTaskId, "AW", this::animalWakeUpNow, animalWakeUpTime));
            Log.record("添加定时起床🔆[" + UserIdMap.getCurrentMaskName() + "]在[" + TimeUtil.getCommonDateTime(animalWakeUpTime) + "]执行");
        }
    }

    private Boolean hasSleepToday() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryLoveCabin(ownerUserId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            jo = jo.getJSONObject("sleepNotifyInfo");
            return jo.optBoolean("hasSleepToday", false);
        } catch (Throwable t) {
            Log.i(TAG, "hasSleepToday err:");
            Log.printStackTrace(t);
        }
        return false;
    }

    private Boolean animalSleepNow() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryLoveCabin(UserIdMap.getCurrentUid()));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            JSONObject sleepNotifyInfo = jo.getJSONObject("sleepNotifyInfo");
            if (!sleepNotifyInfo.optBoolean("canSleep", false)) {
                Log.farm("小鸡无需睡觉🛌");
                return false;
            }
            return animalSleep();
        } catch (Throwable t) {
            Log.i(TAG, "animalSleepNow err:");
            Log.printStackTrace(t);
        }
        return false;
    }

    private Boolean animalWakeUpNow() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryLoveCabin(UserIdMap.getCurrentUid()));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            JSONObject ownAnimal = jo.getJSONObject("ownAnimal");
            JSONObject sleepInfo = ownAnimal.getJSONObject("sleepInfo");
            if (sleepInfo.getInt("countDown") == 0) {
                return false;
            }
            if (sleepInfo.getLong("sleepBeginTime")
                    + TimeUnit.MINUTES.toMillis(sleepMinutes.getValue())
                    <= System.currentTimeMillis()) {
                return animalWakeUp(jo.has("spaceType"));
            } else {
                Log.farm("小鸡无需起床🔆");
            }
        } catch (Throwable t) {
            Log.i(TAG, "animalWakeUpNow err:");
            Log.printStackTrace(t);
        }
        return false;
    }

    private Boolean animalSleep() {
        String groupId = null;
        if (family.getValue()) {
            groupId = getFamilyGroupId();
        }
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.sleep(groupId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            if (StringUtil.isEmpty(groupId)) {
                Log.farm("小鸡睡觉🛌");
            } else {
                Log.farm("亲密家庭🏠小鸡睡觉");
                syncFamilyStatus(groupId, "SLEEP_INFO|ANIMAL_STATUS");
                syncFamilyStatus(groupId, "INTIMACY_VALUE");
            }
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "animalSleep err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private Boolean animalWakeUp(boolean isSleepInFamily) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.wakeUp(isSleepInFamily));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.farm(isSleepInFamily ? "亲密家庭🏠小鸡起床" : "小鸡起床🔆");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "animalWakeUp err:");
            Log.printStackTrace(t);
        }
        return false;
    }

    private void syncAnimalStatus(String farmId) {
        try {
            String s = AntFarmRpcCall.syncAnimalStatus(farmId);
            parseSyncAnimalStatusResponse(s);
        } catch (Throwable t) {
            Log.i(TAG, "syncAnimalStatus err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void syncAnimalStatusAtOtherFarm(String farmId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterFarm(farmId, ""));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("farmVO").getJSONObject("subFarmVO");
            JSONArray jaAnimals = jo.getJSONArray("animals");
            for (int i = 0; i < jaAnimals.length(); i++) {
                jo = jaAnimals.getJSONObject(i);
                if (jo.getString("masterFarmId").equals(ownerFarmId)) {
                    Animal newOwnerAnimal = new Animal();
                    JSONObject animal = jaAnimals.getJSONObject(i);
                    newOwnerAnimal.animalId = animal.getString("animalId");
                    newOwnerAnimal.currentFarmId = animal.getString("currentFarmId");
                    newOwnerAnimal.currentFarmMasterUserId = animal.getString("currentFarmMasterUserId");
                    newOwnerAnimal.masterFarmId = ownerFarmId;
                    newOwnerAnimal.animalBuff = animal.getString("animalBuff");
                    newOwnerAnimal.locationType = animal.optString("locationType", "");
                    newOwnerAnimal.subAnimalType = animal.getString("subAnimalType");
                    animal = animal.getJSONObject("animalStatusVO");
                    newOwnerAnimal.animalFeedStatus = animal.getString("animalFeedStatus");
                    newOwnerAnimal.animalInteractStatus = animal.getString("animalInteractStatus");
                    ownerAnimal = newOwnerAnimal;
                    break;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "syncAnimalStatusAtOtherFarm err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void rewardFriend() {
        try {
            if (rewardList != null) {
                for (RewardFriend rewardFriend : rewardList) {
                    JSONObject jo = new JSONObject(
                            AntFarmRpcCall.rewardFriend(
                                    rewardFriend.consistencyKey,
                                    rewardFriend.friendId,
                                    rewardProductNum,
                                    rewardFriend.time
                            )
                    );
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        double rewardCount = benevolenceScore - jo.getDouble("farmProduct");
                        benevolenceScore -= rewardCount;
                        Log.farm("打赏好友💰[" + UserIdMap.getMaskName(rewardFriend.friendId) + "]#获得"
                                + rewardCount + "颗爱心鸡蛋");
                    }
                }
                rewardList = null;
            }
        } catch (Throwable t) {
            Log.i(TAG, "rewardFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void recallAnimal(String animalId, String currentFarmId, String masterFarmId, String user) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.recallAnimal(animalId, currentFarmId, masterFarmId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            double foodHaveStolen = jo.getDouble("foodHaveStolen");
            Log.farm("召回小鸡📣在[" + user + "]家#偷吃了[" + foodHaveStolen + "g饲料]");
            // 这里不需要加
            // add2FoodStock((int)foodHaveStolen);
        } catch (Throwable t) {
            Log.i(TAG, "recallAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void sendBackAnimal() {
        if (animals == null) {
            return;
        }
        try {
            for (Animal animal : animals) {
                if (AnimalInteractStatus.STEALING.name().equals(animal.animalInteractStatus)
                        && !SubAnimalType.GUEST.name().equals(animal.subAnimalType)
                        && !SubAnimalType.WORK.name().equals(animal.subAnimalType)) {
                    // 赶鸡
                    String user = AntFarmRpcCall.farmId2UserId(animal.masterFarmId);
                    boolean isSendBackAnimal = sendBackAnimalList.contains(user);
                    if (sendBackAnimalType.getValue() != SendBackAnimalType.BACK) {
                        isSendBackAnimal = !isSendBackAnimal;
                    }
                    if (!isSendBackAnimal) {
                        continue;
                    }
                    int sendTypeInt = sendBackAnimalWay.getValue();
                    user = UserIdMap.getMaskName(user);
                    JSONObject jo = new JSONObject(
                            AntFarmRpcCall.sendBackAnimal(
                                    SendBackAnimalWay.nickNames[sendTypeInt],
                                    animal.animalId,
                                    animal.currentFarmId,
                                    animal.masterFarmId
                            )
                    );
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        String s = "遣返小鸡🧶";
                        if (sendTypeInt == SendBackAnimalWay.HIT) {
                            s += "胖揍[" + user + "]";
                            if (jo.has("hitLossFood")) {
                                s += "#收回[" + jo.getInt("hitLossFood") + "g饲料]";
                                if (jo.has("finalFoodStorage"))
                                    foodStock = jo.getInt("finalFoodStorage");
                            } else {
                                s += "#对方躲开了攻击";
                            }
                        } else {
                            s += "驱赶[" + user + "]";
                        }
                        Log.farm(s);
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "sendBackAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void recallAnimal() {
        try {
            if (Objects.equals(AnimalInteractStatus.HOME.name(), ownerAnimal.animalInteractStatus)) {
                return;
            }
            if ("ORCHARD".equals(ownerAnimal.locationType)) {
                Log.farm("庄园通知📣[你家的小鸡给拉去除草了！]");
                JSONObject jo = new JSONObject(AntFarmRpcCall.orchardRecallAnimal(
                        ownerAnimal.animalId, ownerAnimal.currentFarmMasterUserId)
                );
                int manureCount = jo.getInt("manureCount");
                Log.farm("召回小鸡📣收获[" + manureCount + "g肥料]");
                return;
            }
            syncAnimalStatusAtOtherFarm(ownerAnimal.currentFarmId);
            boolean guest = false;
            switch (SubAnimalType.valueOf(ownerAnimal.subAnimalType)) {
                case GUEST:
                    guest = true;
                    Log.record("小鸡到好友家去做客了");
                    break;
                case NORMAL:
                    Log.record("小鸡太饿，离家出走了");
                    break;
                case PIRATE:
                    Log.record("小鸡外出探险了");
                    break;
                case WORK:
                    Log.record("小鸡出去工作啦");
                    break;
                default:
                    Log.record("小鸡不在庄园" + " " + ownerAnimal.subAnimalType);
            }

            boolean hungry = false;
            String userName = UserIdMap.getMaskName(AntFarmRpcCall.farmId2UserId(ownerAnimal.currentFarmId));
            switch (AnimalFeedStatus.valueOf(ownerAnimal.animalFeedStatus)) {
                case HUNGRY:
                    hungry = true;
                    Log.record("小鸡在[" + userName + "]的庄园里挨饿");
                    break;

                case EATING:
                    Log.record("小鸡在[" + userName + "]的庄园里吃得津津有味");
                    break;
            }

            boolean recall = switch (recallAnimalType.getValue()) {
                case RecallAnimalType.ALWAYS -> true;
                case RecallAnimalType.WHEN_THIEF -> !guest;
                case RecallAnimalType.WHEN_HUNGRY -> hungry;
                default -> false;
            };
            if (recall) {
                recallAnimal(ownerAnimal.animalId, ownerAnimal.currentFarmId, ownerFarmId, userName);
                syncAnimalStatus(ownerFarmId);
            }
        } catch (Throwable t) {
            Log.i(TAG, "recallAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void listToolTaskDetails() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.listToolTaskDetails());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray list = jo.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                jo = list.getJSONObject(i);
                if (!TaskStatus.FINISHED.name().equals(jo.optString("taskStatus"))) {
                    continue;
                }
                JSONObject bizInfo = new JSONObject(jo.getString("bizInfo"));
                String awardType = bizInfo.getString("awardType");
                ToolType toolType = ToolType.valueOf(awardType);
                int awardCount = bizInfo.getInt("awardCount");
                String taskType = jo.getString("taskType");
                String taskTitle = bizInfo.getString("taskTitle");
                receiveToolTaskReward(toolType, awardCount, taskType, taskTitle);
            }
        } catch (Throwable t) {
            Log.i(TAG, "listToolTaskDetails err:");
            Log.printStackTrace(TAG, t);
        }
    }


    private void receiveToolTaskReward(ToolType toolType, int awardCount, String taskType, String taskTitle) {
        for (FarmTool farmTool : farmToolList) {
            if (farmTool.toolType != toolType) {
                continue;
            }
            if (farmTool.toolCount == farmTool.toolHoldLimit) {
                if (toolType == ToolType.NEWEGGTOOL) {
                    if (useFarmTool(ownerFarmId, farmTool)) {
                        break;
                    }
                }
                Log.record("领取道具[" + toolType.nickName() + "]#已满，暂不领取");
                return;
            }
            break;
        }
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.receiveToolTaskReward(toolType.name(), awardCount, taskType));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.farm("道具任务🎖️领取奖励[" + taskTitle + "]#获得[" + awardCount + "张" + toolType.nickName() + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveToolTaskReward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void harvestProduce(String farmId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.harvestProduce(farmId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            double harvest = jo.getDouble("harvestBenevolenceScore");
            harvestBenevolenceScore = jo.getDouble("finalBenevolenceScore");
            Log.farm("爱心鸡蛋🥚收取[" + harvest + "颗]#剩余[" + harvestBenevolenceScore + "颗]");
        } catch (Throwable t) {
            Log.i(TAG, "harvestProduce err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /* 捐赠爱心鸡蛋 */
    private void donation() {
        if (!canDonationToday()) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.listActivityInfo());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            boolean isDonation = false;// 记录是否捐赠
            JSONArray activityInfos = jo.getJSONArray("activityInfos");
            for (int i = 0; i < activityInfos.length(); i++) {
                jo = activityInfos.getJSONObject(i);
                int donationTotal = jo.getInt("donationTotal");
                int donationLimit = jo.getInt("donationLimit");

                int donationNum = Math.min(donationAmount.getValue(), donationLimit - donationTotal);
                if (donationNum == 0) {
                    continue;
                }
                String activityId = jo.getString("activityId");
                String projectName = jo.getString("projectName");
                String projectId = jo.getString("projectId");
                int projectDonationNum = getProjectDonationNum(projectId);
                donationNum = Math.min(donationNum, donationAmount.getValue() - projectDonationNum % donationAmount.getValue());
                if (donationNum != donationAmount.getValue()) {
                    // 如果未达捐赠倍数，捐赠到捐赠倍数
                    isDonation = donation(activityId, projectName, 1, donationNum);
                } else if (projectDonationNum == 0
                        || donationType.getValue() == DonationType.ALL
                        || donationType.getValue() == DonationType.ONE) {
                    // 如果已达捐赠倍数且(从未捐赠过 或 未开启智能捐赠)，则捐赠一次捐赠倍数
                    isDonation = donation(activityId, projectName, donationNum, 1);
                } else if (donationType.getValue() == DonationType.ANY) {
                    // 如果已达捐赠倍数且已开启智能捐赠
                    if (hasMonthDoll || isDonation || i + 1 != activityInfos.length()) {
                        // 如果有本月捐赠公仔 或者 今日已经捐赠 或者 不是今日最后一个捐赠项目
                        continue;
                    }
                    // 捐赠一次捐赠倍数
                    isDonation = donation(activityId, projectName, donationNum, 1);
                }
                if (isDonation && donationType.getValue() == DonationType.ONE) {
                    // 如果今日已经捐赠且开启了每日只捐赠一次，不再查看下一个项目
                    return;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "donation err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean donation(String activityId, String activityName, int donationAmount, int count) {
        boolean isDonation = false;
        for (int i = 0; i < count; i++) {
            if (!donation(activityId, activityName, donationAmount)) {
                break;
            }
            isDonation = true;
            TimeUtil.sleep(1000L);
        }
        return isDonation;
    }

    private Boolean donation(String activityId, String activityName, int donationAmount) {
        if (harvestBenevolenceScore < donationAmount) {
            return false;
        }
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.donation(activityId, donationAmount));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            jo = jo.getJSONObject("donation");
            harvestBenevolenceScore = jo.getDouble("harvestBenevolenceScore");
            int donationTimesStat = jo.getInt("donationTimesStat");
            Log.farm("公益捐赠❤️[捐爱心蛋:" + activityName + "]捐赠" + donationAmount + "颗爱心蛋#累计捐赠" + donationTimesStat + "次");
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "donation err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private int getProjectDonationNum(String projectId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.getProjectInfo(projectId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return 0;
            }
            return jo.optInt("userProjectDonationNum");
        } catch (Throwable t) {
            Log.i(TAG, "getProjectDonationNum err:");
            Log.printStackTrace(TAG, t);
        }
        return 0;
    }

    private Boolean hasMonthDoll = false;

    private Boolean canDonationToday() {
        if (Status.hasFlagToday(AntFarmFlag.DONATION.flagName())) {
            return false;
        }
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.getCharityAccount(ownerUserId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            JSONArray charityRecords = jo.getJSONArray("charityRecords");
            if (donationType.getValue() == DonationType.ANY) {
                // 检查是否达成本月捐赠目标
                jo = jo.getJSONObject("achievementInfoV2Page");
                JSONArray achievementsInfoV2 = jo.getJSONArray("achievementsInfoV2");
                long currentTime = System.currentTimeMillis();
                for (int i = 0; i < achievementsInfoV2.length(); i++) {
                    jo = achievementsInfoV2.getJSONObject(i);
                    if (jo.getString("dollId").contains("_MONTH_DOLL")
                            && currentTime >= jo.getLong("startTime")
                            && currentTime < jo.getLong("endTime")) {
                        hasMonthDoll = jo.getBoolean("acquired");
                        break;
                    }
                }

            }
            if (charityRecords.length() == 0) {
                return true;
            }
            jo = charityRecords.getJSONObject(0);
            long charityTime = jo.optLong("charityTime", System.currentTimeMillis());
            if (TimeUtil.isLessThanNowOfDays(charityTime)) {
                return true;
            }
            Status.flagToday(AntFarmFlag.DONATION.flagName());
        } catch (Throwable t) {
            Log.i(TAG, "canDonationToday err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void farmGame() {
        try {
            if (farmGameOptions.contains(AntFarmGameOption.RECORD_FARM_GAME.name())) {
                if (TimeUtil.checkInTimeRange(System.currentTimeMillis(), farmGameTime.getValue())) {
                    recordFarmGame(GameType.starGame);
                    recordFarmGame(GameType.jumpGame);
                    recordFarmGame(GameType.flyGame);
                    recordFarmGame(GameType.hitGame);
                }
            }
            if (farmGameOptions.contains(AntFarmGameOption.DRAW_GAME_CENTER_AWARD.name())) {
                drawGameCenterAward();
            }
            if (farmGameOptions.contains(AntFarmGameOption.BUY_MALL.name())) {
                getMallHome();
            }
        } catch (Throwable t) {
            Log.i(TAG, "farmGame err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void recordFarmGame(GameType gameType) {
        try {
            do {
                try {
                    JSONObject jo = new JSONObject(AntFarmRpcCall.initFarmGame(gameType.name()));
                    if (!MessageUtil.checkResponse(TAG, jo)) {
                        return;
                    }
                    if (jo.getJSONObject("gameAward").getBoolean("level3Get")) {
                        return;
                    }
                    if (jo.optInt("remainingGameCount", 1) == 0) {
                        return;
                    }
                    jo = new JSONObject(AntFarmRpcCall.recordFarmGame(gameType.name()));
                    if (!MessageUtil.checkResponse(TAG, jo)) {
                        return;
                    }
                    JSONArray awardInfos = jo.getJSONArray("awardInfos");
                    StringBuilder award = new StringBuilder();
                    for (int i = 0; i < awardInfos.length(); i++) {
                        JSONObject awardInfo = awardInfos.getJSONObject(i);
                        award.append(awardInfo.getString("awardName")).append("*").append(awardInfo.getInt("awardCount"));
                    }
                    if (jo.has("receiveFoodCount")) {
                        award.append(";肥料*").append(jo.getString("receiveFoodCount"));
                    }
                    Log.farm("小鸡乐园🎮玩游戏[" + gameType.gameName() + "]#获得[" + award + "]");
                    if (jo.optInt("remainingGameCount", 0) > 0) {
                        continue;
                    }
                    break;
                } finally {
                    TimeUtil.sleep(2000);
                }
            } while (true);
        } catch (Throwable t) {
            Log.i(TAG, "recordFarmGame err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void drawGameCenterAward() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryGameList());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject gameDrawAwardActivity = jo.getJSONObject("gameDrawAwardActivity");
            int canUseTimes = gameDrawAwardActivity.getInt("canUseTimes");
            while (canUseTimes > 0) {
                jo = new JSONObject(AntFarmRpcCall.drawGameCenterAward());
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return;
                }
                canUseTimes = jo.getInt("drawRightsTimes");
                JSONArray gameCenterDrawAwardList = jo.getJSONArray("gameCenterDrawAwardList");
                ArrayList<String> awards = new ArrayList<>();
                for (int i = 0; i < gameCenterDrawAwardList.length(); i++) {
                    JSONObject gameCenterDrawAward = gameCenterDrawAwardList.getJSONObject(i);
                    int awardCount = gameCenterDrawAward.getInt("awardCount");
                    String awardName = gameCenterDrawAward.getString("awardName");
                    awards.add(awardName + "*" + awardCount);
                }
                Log.farm("小鸡乐园🎮开宝箱#获得[" + StringUtil.collectionJoinString(",", awards) + "]");
                TimeUtil.sleep(3000);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryChickenDiaryList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void getMallHome() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.getMallHome());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray ja = jo.getJSONArray("mallItemSimpleList");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                getMallItemDetail(jo.getString("spuId"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "getMallHome err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void getMallItemDetail(String itemId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.getMallItemDetail(itemId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("mallItemDetail");
                JSONArray ja = jo.getJSONArray("mallSubItemDetailList");
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    int price = jo.getInt("price");
                    String skuId = jo.getString("skuId");
                    String skuName = jo.getString("skuName");
                    MallItemIdMap.getInstance().add(skuId, skuName);
                    if (jo.getJSONArray("itemStatusList").length() > 0) {
                        continue;
                    }
                    while (buyMallItem(itemId, skuId)) {
                        TimeUtil.sleep(500);
                        Log.farm("小鸡乐园🎮集市兑换[" + skuName + "]#消耗[" + price + "乐园币]");
                    }
                }
                MallItemIdMap.getInstance().save();
            }
        } catch (Throwable t) {
            Log.i(TAG, "getMallItemDetail err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean buyMallItem(String itemId, String subItemId) {
        if (!Status.canBuyFarmMallItemCountToday(subItemId)) {
            return false;
        }
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.buyMallItem(itemId, subItemId));
            if (MessageUtil.checkResponse(TAG, jo) && jo.getBoolean("provideResult")) {
                Status.buyFarmMallItemCountToday(subItemId);
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "buyMallItem err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void listFarmTask(TaskStatus Mode) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.listFarmTask());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject signList = jo.getJSONObject("signList");
            if (sign(signList)) {
                TimeUtil.sleep(1000);
            }
            JSONArray ja = jo.getJSONArray("farmTaskList");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                TaskStatus taskStatus = TaskStatus.valueOf(jo.getString("taskStatus"));
                if (taskStatus == TaskStatus.RECEIVED || taskStatus != Mode) {
                    continue;
                }
                if (taskStatus == TaskStatus.TODO && !doFarmTask(jo)) {
                    continue;
                }
                if (taskStatus == TaskStatus.FINISHED && !receiveFarmTaskAward(jo)) {
                    continue;
                }
                TimeUtil.sleep(1000);
            }
        } catch (Throwable t) {
            Log.i(TAG, "listFarmTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean sign(JSONObject SignList) {
        if (Status.hasFlagToday(AntFarmFlag.SIGN.flagName())) {
            return false;
        }
        boolean signed = false;
        try {
            String currentSignKey = SignList.getString("currentSignKey");
            JSONArray signList = SignList.getJSONArray("signList");
            for (int i = 0; i < signList.length(); i++) {
                JSONObject jo = signList.getJSONObject(i);
                if (!currentSignKey.equals(jo.getString("signKey"))) {
                    continue;
                }
                if (jo.optBoolean("signed")) {
                    Log.record("庄园今日已签到");
                    signed = true;
                    return false;
                }
                int awardCount = jo.getInt("awardCount");
                if (awardCount + foodStock > foodStockLimit) {
                    return false;
                }
                int currentContinuousCount = jo.getInt("currentContinuousCount");
                jo = new JSONObject(AntFarmRpcCall.sign());
                if (MessageUtil.checkResponse(TAG, jo)) {
                    foodStock = jo.getInt("foodStock");
                    Log.farm("饲料任务📅连续签到[坚持" + currentContinuousCount + "天]#获得[" + awardCount + "g饲料]");
                    signed = true;
                    return true;
                }
                return false;
            }
        } catch (Throwable t) {
            Log.i(TAG, "sign err:");
            Log.printStackTrace(TAG, t);
        } finally {
            if (signed) {
                Status.flagToday(AntFarmFlag.SIGN.flagName());
            }
        }
        return false;
    }

    private Boolean doVideoTask() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryTabVideoUrl());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            String videoUrl = jo.getString("videoUrl");
            String contentId = videoUrl.substring(videoUrl.indexOf("&contentId=") + 1,
                    videoUrl.indexOf("&refer"));
            jo = new JSONObject(AntFarmRpcCall.videoDeliverModule(contentId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                TimeUtil.sleep(15100);
                jo = new JSONObject(AntFarmRpcCall.videoTrigger(contentId));
                return MessageUtil.checkResponse(TAG, jo);
            }
        } catch (Throwable t) {
            Log.i(TAG, "doVideoTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private Boolean doAnswerTask() {
        try {
            JSONObject jo = new JSONObject(DadaDailyRpcCall.home("100"));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            JSONObject question = jo.getJSONObject("question");
            long questionId = question.getLong("questionId");
            JSONArray labels = question.getJSONArray("label");
            String answer = AnswerAI.getAnswer(question.getString("title"), JsonUtil.jsonArrayToList(labels));
            if (StringUtil.isEmpty(answer)) {
                answer = labels.getString(0);
            }
            jo = new JSONObject(DadaDailyRpcCall.submit("100", answer, questionId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            JSONObject extInfo = jo.getJSONObject("extInfo");
            boolean correct = jo.getBoolean("correct");
            String award = extInfo.getString("award");
            Log.record("庄园答题📝回答" + (correct ? "正确" : "错误") + "#获得[" + award + "g饲料]");
            JSONArray operationConfigList = jo.getJSONArray("operationConfigList");
            savePreviewQuestion(operationConfigList);
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "doAnswerTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void savePreviewQuestion(JSONArray operationConfigList) {
        try {
            for (int i = 0; i < operationConfigList.length(); i++) {
                JSONObject jo = operationConfigList.getJSONObject(i);
                String type = jo.getString("type");
                if (Objects.equals(type, "PREVIEW_QUESTION")) {
                    String question = jo.getString("title");
                    JSONArray ja = new JSONArray(jo.getString("actionTitle"));
                    for (int j = 0; j < ja.length(); j++) {
                        jo = ja.getJSONObject(j);
                        if (jo.getBoolean("correct")) {
                            Statistics.saveQuestion(question, jo.getString("title"));
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "saveAnswerList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean doFarmTask(JSONObject task) {
        boolean isDoTask = false;
        try {
            String title = task.getString("title");
            if (Objects.equals(title, "庄园小视频")) {
                isDoTask = doVideoTask();
            } else if (Objects.equals(title, "庄园小课堂")) {
                isDoTask = doAnswerTask();
            } else {
                isDoTask = Objects.equals(
                        ExtensionsHandle.handleRequest(new Request(RequestType.DO_FARM_TASK, task)),
                        Boolean.TRUE);
            }
            if (isDoTask) {
                Log.farm("饲料任务🧾完成任务[" + title + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "doFarmTask err:");
            Log.printStackTrace(TAG, t);
        }
        return isDoTask;
    }

    private Boolean receiveFarmTaskAward(JSONObject task) {
        try {
            String taskId = task.getString("taskId");
            String awardType = task.getString("awardType");
            int awardCount = task.getInt("awardCount");
            if (Objects.equals(awardType, "ALLPURPOSE")) {
                if (awardCount + foodStock > foodStockLimit) {
                    unReceiveTaskAward++;
                    // Log.record("领取" + awardCount + "克饲料后将超过[" + foodStockLimit + "克]上限，终止领取");
                    return false;
                }
            }
            JSONObject jo = new JSONObject(AntFarmRpcCall.receiveFarmTaskAward(taskId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            if (awardType.equals("ALLPURPOSE")) {
                add2FoodStock(awardCount);
                String title = task.getString("title");
                Log.farm("饲料任务🎖️领取奖励[" + title + "]#获得[" + awardCount + "g饲料]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveFarmTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void checkUnReceiveTaskAward() {
        if (unReceiveTaskAward > 0) {
            Log.record("还有待领取的饲料");
            unReceiveTaskAward = 0;
            listFarmTask(TaskStatus.FINISHED);
        }
    }

    private void feedAnimal() {
        if (!AnimalInteractStatus.HOME.name().equals(ownerAnimal.animalInteractStatus)) {
            return;
        }
        if (AnimalFeedStatus.HUNGRY.name().equals(ownerAnimal.animalFeedStatus)) {
            Log.record("小鸡在挨饿");
            feedAnimal(ownerFarmId);
        } else if (AnimalFeedStatus.EATING.name().equals(ownerAnimal.animalFeedStatus)) {
            autoFeedAnimal();
        }
    }

    private void feedAnimal(String farmId) {
        try {
            if (foodStock < 180) {
                Log.record("剩余饲料不足以投喂小鸡");
                return;
            }
            JSONObject jo = new JSONObject(AntFarmRpcCall.feedAnimal(farmId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                syncAnimalStatus(ownerFarmId);
                Log.farm("投喂小鸡🥣剩余[" + foodStock + "g饲料]");
                TimeUtil.sleep(1000);
                useAccelerateTool();
            }
        } catch (Throwable t) {
            Log.i(TAG, "feedAnimal err:");
            Log.printStackTrace(TAG, t);
        } finally {
            long updateTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);
            String taskId = "UPDATE|FA|" + farmId;
            addChildTask(new ChildModelTask(taskId, "UPDATE", this::autoFeedAnimal, updateTime));
        }
    }

    private void listFarmTool() {
        farmToolList.clear();
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.listFarmTool());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray toolList = jo.getJSONArray("toolList");
            for (int i = 0; i < toolList.length(); i++) {
                jo = toolList.getJSONObject(i);
                FarmTool farmTool = new FarmTool();
                farmTool.toolType = ToolType.valueOf(jo.getString("toolType"));
                farmTool.toolId = jo.optString("toolId");
                farmTool.toolCount = jo.getInt("toolCount");
                farmTool.toolHoldLimit = jo.optInt("toolHoldLimit", 20);
                farmToolList.add(farmTool);
            }
        } catch (Throwable t) {
            Log.i(TAG, "listFarmTool err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void useFarmTool() {
        try {
            while (useFarmTool(ownerFarmId, ToolType.NEWEGGTOOL)) {
                TimeUtil.sleep(1000);
            }
            if (AnimalInteractStatus.HOME.name().equals(ownerAnimal.animalInteractStatus)
                    && AnimalFeedStatus.EATING.name().equals(ownerAnimal.animalFeedStatus)) {
                if (useAccelerateTool() && feedAnimal.getValue()) {
                    autoFeedAnimal();
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "useFarmTool err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean useFarmTool(String targetFarmId, ToolType toolType) {
        if (!Status.canUseFarmToolToday(toolType.name())) {
            return false;
        }
        listFarmTool();
        for (FarmTool farmTool : farmToolList) {
            return useFarmTool(targetFarmId, farmTool);
        }
        return false;
    }

    private Boolean useFarmTool(String targetFarmId, FarmTool farmTool) {
        if (farmTool.toolCount > 0 && useFarmTool(targetFarmId, farmTool.toolId, farmTool.toolType.name())) {
            Log.farm("使用道具🎭蚂蚁庄园[" + farmTool.toolType.nickName() + "]#剩余" + (farmTool.toolCount - 1) + "张");
            return true;
        }
        return false;
    }

    private Boolean useFarmTool(String targetFarmId, String toolId, String toolType) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.useFarmTool(targetFarmId, toolId, toolType));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Status.useFarmToolToday(toolType);
                syncAnimalStatus(ownerFarmId);
                return true;
            }
            if (Objects.equals("3D16", jo.getString("resultCode"))) {
                Status.flagToday(AntFarmFlag.USE_FARM_TOOL_LIMIT.flagName(toolType));
            }
        } catch (Throwable t) {
            Log.i(TAG, "useFarmTool err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }
    /**
     * 使用加速卡
     *
     * @param countdown 进食倒计时
     * @return 是否使用了加速卡
     */
    private Boolean useAccelerateTool() {
        boolean hasUseAccelerateTool = false;
        while (Status.canUseFarmToolToday(ToolType.ACCELERATETOOL.name())) {
            if (!useAccelerateToolOptions.contains(
                    AntFarmAccelerateToolOption.USE_ACCELERATE_TOOL_CONTINUE.name())
                    && AnimalBuff.ACCELERATING.name().equals(ownerAnimal.animalBuff)) {
                break;
            }
            if (useAccelerateToolOptions.contains(
                    AntFarmAccelerateToolOption.USE_ACCELERATE_TOOL_WHEN_MAX_EMOTION.name())
                    && !isMaxEmotion()) {
                break;
            }
            double consumeSpeed = 0d;
            double foodHaveEatten = 0d;
            long nowTime = System.currentTimeMillis() / 1000;
            for (Animal animal : animals) {
                if (animal.masterFarmId.equals(ownerFarmId)) {
                    consumeSpeed = animal.consumeSpeed;
                }
                foodHaveEatten += animal.consumeSpeed * (nowTime - animal.startEatTime / 1000);
            }
            // consumeSpeed: g/s
            // AccelerateTool: -1h = -60m = -3600s
            if (foodInTrough - foodHaveEatten < consumeSpeed * 3600) {
                break;
            }
            if (!useFarmTool(ownerFarmId, ToolType.ACCELERATETOOL)) {
                break;
            }
            TimeUtil.sleep(1000);
            hasUseAccelerateTool = true;
        }
        return hasUseAccelerateTool;
    }

    private Boolean isMaxEmotion() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.moodDynamic());
            if (MessageUtil.checkResponse(TAG, jo) && jo.has("emotionInfo")) {
                jo = jo.getJSONObject("emotionInfo");
                return jo.optDouble("finalScore") == 100;
            }
        } catch (Throwable t) {
            Log.i(TAG, "isMaxEmotion err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void feedFriend() {
        try {
            for (Map.Entry<String, Integer> entry : feedFriendAnimalList.getValue().entrySet()) {
                String userId = entry.getKey();
                if (userId.equals(UserIdMap.getCurrentUid()))
                    continue;
                if (!Status.canFeedFriendToday(userId, entry.getValue()))
                    continue;
                JSONObject jo = new JSONObject(AntFarmRpcCall.enterFarm("", userId));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    continue;
                }
                jo = jo.getJSONObject("farmVO").getJSONObject("subFarmVO");
                String friendFarmId = jo.getString("farmId");
                JSONArray jaAnimals = jo.getJSONArray("animals");
                for (int j = 0; j < jaAnimals.length(); j++) {
                    jo = jaAnimals.getJSONObject(j);
                    String masterFarmId = jo.getString("masterFarmId");
                    if (masterFarmId.equals(friendFarmId)) {
                        jo = jo.getJSONObject("animalStatusVO");
                        if (AnimalInteractStatus.HOME.name().equals(jo.getString("animalInteractStatus"))
                                && AnimalFeedStatus.HUNGRY.name().equals(jo.getString("animalFeedStatus"))) {
                            feedFriendAnimal(friendFarmId);
                        }
                        break;
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "feedFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void feedFriendAnimal(String friendFarmId) {
        try {
            String userId = AntFarmRpcCall.farmId2UserId(friendFarmId);
            String maskName = UserIdMap.getMaskName(userId);
            Log.record("[" + maskName + "]的小鸡在挨饿");
            if (foodStock < 180) {
                Log.record("喂鸡饲料不足");
                checkUnReceiveTaskAward();
                if (foodStock < 180) {
                    return;
                }
            }
            String groupId = null;
            if (family.getValue()) {
                List<String> list = getFamilyMemberList(false, false);
                if (list.contains(userId)) {
                    groupId = getFamilyGroupId();
                }
            }
            if (feedFriendAnimal(friendFarmId, groupId)) {
                String s = StringUtil.isEmpty(groupId) ? "帮喂小鸡🥣帮喂好友" : "亲密家庭🏠帮喂成员";
                Log.farm(s + "[" + maskName + "]" + "#剩余[" + foodStock + "g饲料]");
                Status.feedFriendToday(AntFarmRpcCall.farmId2UserId(friendFarmId));
            }
        } catch (Throwable t) {
            Log.i(TAG, "feedFriendAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean feedFriendAnimal(String friendFarmId, String groupId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.feedFriendAnimal(friendFarmId, groupId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                if (Objects.equals("391", jo.optString("resultCode"))) {
                    Status.flagToday(AntFarmFlag.FEED_FRIEND_ANIMAL_LIMIT.flagName());
                }
                return false;
            }
            foodStock = jo.getInt("foodStock");
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "feedFriendAnimal err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void notifyFriend() {
        if (foodStock >= foodStockLimit) {
            return;
        }
        try {
            boolean hasNext;
            int pageStartSum = 0;
            do {
                JSONObject jo = new JSONObject(AntFarmRpcCall.rankingList(pageStartSum));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    break;
                }
                hasNext = jo.optBoolean("hasNext");
                JSONArray rankingList = jo.getJSONArray("rankingList");
                pageStartSum += rankingList.length();
                for (int i = 0; i < rankingList.length(); i++) {
                    jo = rankingList.getJSONObject(i);
                    String userId = jo.getString("userId");
                    boolean isNotifyFriend = notifyFriendList.contains(userId);
                    if (notifyFriendType.getValue() != NotifyFriendType.NOTIFY) {
                        isNotifyFriend = !isNotifyFriend;
                    }
                    if (!isNotifyFriend || userId.equals(UserIdMap.getCurrentUid())) {
                        continue;
                    }
                    boolean starve = jo.has("actionType") &&
                            Objects.equals("starve_action", jo.getString("actionType"));
                    if (jo.getBoolean("stealingAnimal") && !starve) {
                        if (notifyFriend(userId) || foodStock >= foodStockLimit) {
                            return;
                        }
                    }
                }
            } while (hasNext);
            Log.record("庄园剩余[" + foodStock + "g饲料]");
        } catch (Throwable t) {
            Log.i(TAG, "notifyFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean notifyFriend(String friendUserId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterFarm("", friendUserId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            jo = jo.getJSONObject("farmVO").getJSONObject("subFarmVO");
            String friendFarmId = jo.getString("farmId");
            JSONArray jaAnimals = jo.getJSONArray("animals");
            for (int j = 0; j < jaAnimals.length(); j++) {
                jo = jaAnimals.getJSONObject(j);
                String animalId = jo.getString("animalId");
                String masterFarmId = jo.getString("masterFarmId");
                if (!Objects.equals(masterFarmId, friendFarmId)
                        && !Objects.equals(masterFarmId, ownerFarmId)) {
                    jo = jo.getJSONObject("animalStatusVO");
                    if (AnimalInteractStatus.STEALING.name().equals(jo.getString("animalInteractStatus"))
                            && AnimalFeedStatus.EATING.name().equals(jo.getString("animalFeedStatus"))) {
                        if (notifyFriend(friendFarmId, animalId)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "notifyFriend err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private Boolean notifyFriend(String friendFarmId, String animalId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.notifyFriend(animalId, friendFarmId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            int rewardCount = (int) jo.getDouble("rewardCount");
            if (jo.getBoolean("refreshFoodStock"))
                foodStock = (int) jo.getDouble("finalFoodStock");
            else add2FoodStock(rewardCount);
            String userMaskName = UserIdMap.getMaskName(AntFarmRpcCall.farmId2UserId(friendFarmId));
            Log.farm("通知赶鸡📧提醒好友[" + userMaskName + "]被偷吃#获得[" + rewardCount + "g饲料]");
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "notifyFriend err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void parseSyncAnimalStatusResponse(String resp) {
        try {
            JSONObject jo = new JSONObject(resp);
            if (!jo.has("subFarmVO")) {
                return;
            }
            JSONObject subFarmVO = jo.getJSONObject("subFarmVO");
            if (subFarmVO.has("foodStock")) {
                foodStock = subFarmVO.getInt("foodStock");
            }
            if (subFarmVO.has("foodInTrough")) {
                foodInTrough = subFarmVO.getInt("foodInTrough");
            }
            if (subFarmVO.has("manureVO")) {
                JSONArray manurePotList = subFarmVO.getJSONObject("manureVO").getJSONArray("manurePotList");
                for (int i = 0; i < manurePotList.length(); i++) {
                    JSONObject manurePot = manurePotList.getJSONObject(i);
                    if (manurePot.getInt("manurePotNum") >= 100) {
                        JSONObject joManurePot = new JSONObject(
                                AntFarmRpcCall.collectManurePot(
                                        manurePot.getString("manurePotNO")
                                )
                        );
                        if (MessageUtil.checkResponse(TAG, joManurePot)) {
                            int collectManurePotNum = joManurePot.getInt("collectManurePotNum");
                            Log.farm("打扫鸡屎🧹获得[" + collectManurePotNum + "g肥料]");
                        }
                    }
                }
            }
            ownerFarmId = subFarmVO.getString("farmId");
            JSONObject farmProduce = subFarmVO.getJSONObject("farmProduce");
            benevolenceScore = farmProduce.getDouble("benevolenceScore");
            if (subFarmVO.has("rewardList")) {
                JSONArray jaRewardList = subFarmVO.getJSONArray("rewardList");
                if (jaRewardList.length() > 0) {
                    rewardList = new RewardFriend[jaRewardList.length()];
                    for (int i = 0; i < rewardList.length; i++) {
                        JSONObject joRewardList = jaRewardList.getJSONObject(i);
                        if (rewardList[i] == null)
                            rewardList[i] = new RewardFriend();
                        rewardList[i].consistencyKey = joRewardList.getString("consistencyKey");
                        rewardList[i].friendId = joRewardList.getString("friendId");
                        rewardList[i].time = joRewardList.getString("time");
                    }
                }
            }
            JSONArray jaAnimals = subFarmVO.getJSONArray("animals");
            animals = new Animal[jaAnimals.length()];
            for (int i = 0; i < animals.length; i++) {
                Animal animal = new Animal();
                JSONObject animalJsonObject = jaAnimals.getJSONObject(i);
                animal.animalId = animalJsonObject.getString("animalId");
                animal.currentFarmId = animalJsonObject.getString("currentFarmId");
                animal.masterFarmId = animalJsonObject.getString("masterFarmId");
                animal.animalBuff = animalJsonObject.getString("animalBuff");
                animal.subAnimalType = animalJsonObject.getString("subAnimalType");
                animal.currentFarmMasterUserId = animalJsonObject.getString("currentFarmMasterUserId");
                animal.locationType = animalJsonObject.optString("locationType", "");
                JSONObject animalStatusVO = animalJsonObject.getJSONObject("animalStatusVO");
                animal.animalFeedStatus = animalStatusVO.getString("animalFeedStatus");
                animal.animalInteractStatus = animalStatusVO.getString("animalInteractStatus");
                animal.animalInteractStatus = animalStatusVO.getString("animalInteractStatus");
                animal.startEatTime = animalJsonObject.optLong("startEatTime");
                animal.beHiredEndTime = animalJsonObject.optLong("beHiredEndTime");
                animal.consumeSpeed = animalJsonObject.optDouble("consumeSpeed");
                animal.foodHaveEatten = animalJsonObject.optDouble("foodHaveEatten");
                if (animal.masterFarmId.equals(ownerFarmId)) {
                    ownerAnimal = animal;
                }
                animals[i] = animal;
            }
        } catch (Throwable t) {
            Log.i(TAG, "parseSyncAnimalStatusResponse err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void add2FoodStock(int i) {
        foodStock += i;
        if (foodStock > foodStockLimit) {
            foodStock = foodStockLimit;
        }
        if (foodStock < 0) {
            foodStock = 0;
        }
    }

    private void collectDailyFoodMaterial(String userId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterKitchen(userId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            boolean canCollectDailyFoodMaterial = jo.getBoolean("canCollectDailyFoodMaterial");
            int dailyFoodMaterialAmount = jo.getInt("dailyFoodMaterialAmount");
            int garbageAmount = jo.optInt("garbageAmount", 0);
            if (jo.has("orchardFoodMaterialStatus")) {
                JSONObject orchardFoodMaterialStatus = jo.getJSONObject("orchardFoodMaterialStatus");
                if ("FINISHED".equals(orchardFoodMaterialStatus.optString("foodStatus"))) {
                    jo = new JSONObject(AntFarmRpcCall.farmFoodMaterialCollect());
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Log.farm("小鸡厨房👨🏻‍🍳领取食材[农场食材]#获得[" + jo.getInt("foodMaterialAddCount") + "g食材]");
                    }
                }
            }
            if (canCollectDailyFoodMaterial) {
                jo = new JSONObject(AntFarmRpcCall.collectDailyFoodMaterial(dailyFoodMaterialAmount));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.farm("小鸡厨房👨🏻‍🍳领取食材[小鸡厨房:每日食材]#获得[" + dailyFoodMaterialAmount + "g食材]");
                }
            }
            if (garbageAmount > 0) {
                jo = new JSONObject(AntFarmRpcCall.collectKitchenGarbage());
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.farm("小鸡厨房👨🏻‍🍳收集厨余#获得[" + jo.getInt("recievedKitchenGarbageAmount") + "g肥料]");
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "collectDailyFoodMaterial err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void collectDailyLimitedFoodMaterial() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryFoodMaterialPack());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            boolean canCollectDailyLimitedFoodMaterial = jo.getBoolean("canCollectDailyLimitedFoodMaterial");
            if (canCollectDailyLimitedFoodMaterial) {
                int dailyLimitedFoodMaterialAmount = jo.getInt("dailyLimitedFoodMaterialAmount");
                jo = new JSONObject(AntFarmRpcCall.collectDailyLimitedFoodMaterial(dailyLimitedFoodMaterialAmount));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.farm("小鸡厨房👨🏻‍🍳领取食材[爱心食材店:每日限量食材]#获得[" + dailyLimitedFoodMaterialAmount + "g食材]");
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "collectDailyLimitedFoodMaterial err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void cook(String userId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterKitchen(userId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            int cookTimesAllowed = jo.getInt("cookTimesAllowed");
            if (cookTimesAllowed > 0) {
                for (int i = 0; i < cookTimesAllowed; i++) {
                    jo = new JSONObject(AntFarmRpcCall.cook(userId));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        JSONObject cuisineVO = jo.getJSONObject("cuisineVO");
                        Log.farm("小鸡厨房👨🏻‍🍳制作美食[" + cuisineVO.getString("name") + "]");
                    }
                    TimeUtil.sleep(RandomUtil.delay());
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "cook err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private List<JSONObject> getSortedCuisineList(JSONArray cuisineList) {
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < cuisineList.length(); i++) {
            list.add(cuisineList.optJSONObject(i));
        }
        Collections.sort(list,
                (jsonObject1, jsonObject2)
                        -> jsonObject2.optInt("count") - jsonObject1.optInt("count"));
        return list;
    }

    private void useFarmFood(JSONArray cuisineList) {
        try {
            List<JSONObject> list = getSortedCuisineList(cuisineList);
            for (int i = 0; i < list.size(); i++) {
                if (!useFarmFood(list.get(i))) {
                    return;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "useFarmFood err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean useFarmFood(JSONObject cuisine) {
        if (!Status.canUseSpecialFoodToday()) {
            return false;
        }
        try {
            String cookbookId = cuisine.getString("cookbookId");
            String cuisineId = cuisine.getString("cuisineId");
            String name = cuisine.getString("name");
            int count = cuisine.getInt("count");
            for (int j = 0; j < count; j++) {
                JSONObject jo = new JSONObject(AntFarmRpcCall.useFarmFood(cookbookId, cuisineId));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return false;
                }
                double deltaProduce = jo.getJSONObject("foodEffect").getDouble("deltaProduce");
                Log.farm("特殊美食🍱使用[" + name + "]#加速[" + deltaProduce + "颗爱心鸡蛋]");
                Status.useSpecialFoodToday();
                if (!Status.canUseSpecialFoodToday()) {
                    break;
                }
            }
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "useFarmFood err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void drawLotteryPlus(JSONObject lotteryPlusInfo) {
        try {
            if (!lotteryPlusInfo.has("userSevenDaysGiftsItem"))
                return;
            String itemId = lotteryPlusInfo.getString("itemId");
            JSONObject jo = lotteryPlusInfo.getJSONObject("userSevenDaysGiftsItem");
            JSONArray ja = jo.getJSONArray("userEverydayGiftItems");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                if (jo.getString("itemId").equals(itemId)) {
                    if (!jo.getBoolean("received")) {
                        String singleDesc = jo.getString("singleDesc");
                        int awardCount = jo.getInt("awardCount");
                        if (singleDesc.contains("饲料") && awardCount + foodStock > foodStockLimit) {
                            Log.record("暂停领取[" + awardCount + "]克饲料，上限为[" + foodStockLimit + "]克");
                            break;
                        }
                        jo = new JSONObject(AntFarmRpcCall.drawLotteryPlus());
                        if (MessageUtil.checkResponse(TAG, jo)) {
                            Log.farm("惊喜礼包🎁[" + singleDesc + "*" + awardCount + "]");
                        }
                    } else {
                        Log.record("当日奖励已领取");
                    }
                    break;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "drawLotteryPlus err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void visitFriend() {
        for (Map.Entry<String, Integer> entry : visitFriendList.getValue().entrySet()) {
            String userId = entry.getKey();
            Integer countLimit = entry.getValue();
            if (userId.equals(UserIdMap.getCurrentUid())) {
                continue;
            }
            if (Status.canVisitFriendToday(userId, countLimit)) {
                visitFriend(userId, countLimit);
            }
        }
    }

    private void visitFriend(String userId, int countLimit) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterFarm(userId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject farmVO = jo.getJSONObject("farmVO");
            foodStock = farmVO.getInt("foodStock");
            JSONObject subFarmVO = farmVO.getJSONObject("subFarmVO");
            if (subFarmVO.optBoolean("visitedToday", true)) {
                Status.flagToday(AntFarmFlag.VISIT_FRIEND_LIMIT.flagName(userId));
                return;
            }
            String farmId = subFarmVO.getString("farmId");
            while (Status.canVisitFriendToday(userId, countLimit) && foodStock >= 10) {
                jo = new JSONObject(AntFarmRpcCall.visitFriend(farmId));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    break;
                }
                TimeUtil.sleep(1000);
                Status.visitFriendToday(userId);
                foodStock = jo.getInt("foodStock");
                Log.farm("赠送麦子🌾赠送好友[" + UserIdMap.getMaskName(userId) + "]#消耗[" + jo.getInt("giveFoodNum") + "g饲料]");
                if (jo.optBoolean("isReachLimit")) {
                    Log.record("今日给[" + UserIdMap.getMaskName(userId) + "]送麦子已达上限");
                    Status.flagToday(AntFarmFlag.VISIT_FRIEND_LIMIT.flagName(userId));
                    break;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "visitFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void acceptGift() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.acceptGift());
            if (MessageUtil.checkResponse(TAG, jo)) {
                int receiveFoodNum = jo.getInt("receiveFoodNum");
                Log.farm("收取麦子🌾获得[" + receiveFoodNum + "g饲料]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "acceptGift err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void queryChickenDiary(String queryDayStr) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryChickenDiary(queryDayStr));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject data = jo.getJSONObject("data");
            JSONObject chickenDiary = data.getJSONObject("chickenDiary");
            String diaryDateStr = chickenDiary.getString("diaryDateStr");
            if (data.has("hasTietie")) {
                if (!data.optBoolean("hasTietie", true)) {
                    jo = new JSONObject(AntFarmRpcCall.diaryTietie(diaryDateStr, "NEW"));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        String prizeType = jo.getString("prizeType");
                        int prizeNum = jo.optInt("prizeNum", 0);
                        Log.farm("小鸡日记💞贴贴小鸡#获得[" + prizeType + "*" + prizeNum + "]");
                    }
                    if (!chickenDiary.has("statisticsList"))
                        return;
                    JSONArray statisticsList = chickenDiary.getJSONArray("statisticsList");
                    if (statisticsList.length() > 0) {
                        for (int i = 0; i < statisticsList.length(); i++) {
                            JSONObject tietieStatus = statisticsList.getJSONObject(i);
                            String tietieRoleId = tietieStatus.getString("tietieRoleId");
                            jo = new JSONObject(AntFarmRpcCall.diaryTietie(diaryDateStr, tietieRoleId));
                            if (MessageUtil.checkResponse(TAG, jo)) {
                                String prizeType = jo.getString("prizeType");
                                int prizeNum = jo.optInt("prizeNum", 0);
                                Log.farm("小鸡日记💞贴贴小鸡#获得[" + prizeType + "*" + prizeNum + "]");
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryChickenDiary err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void queryChickenDiaryList() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryChickenDiaryList());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray chickenDiaryBriefList = jo.getJSONObject("data").optJSONArray("chickenDiaryBriefList");
            if (chickenDiaryBriefList != null && chickenDiaryBriefList.length() > 0) {
                for (int i = 0; i < chickenDiaryBriefList.length(); i++) {
                    jo = chickenDiaryBriefList.getJSONObject(i);
                    if (!jo.optBoolean("read", true)) {
                        String dateStr = jo.getString("dateStr");
                        queryChickenDiary(dateStr);
                        TimeUtil.sleep(300);
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryChickenDiaryList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void visitAnimal() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.visitAnimal());
            if (!MessageUtil.checkResponse(TAG, jo) || !jo.has("talkConfigs")) {
                return;
            }

            JSONArray talkNodes = jo.getJSONArray("talkNodes");
            JSONArray talkConfigs = jo.getJSONArray("talkConfigs");
            JSONObject data = talkConfigs.getJSONObject(0);
            String farmId = data.getString("farmId");
            jo = new JSONObject(AntFarmRpcCall.feedFriendAnimalVisit(farmId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray actionNodes = null;
            for (int i = 0; i < talkNodes.length(); i++) {
                jo = talkNodes.getJSONObject(i);
                if (jo.has("actionNodes")) {
                    actionNodes = jo.getJSONArray("actionNodes");
                    break;
                }
            }
            if (actionNodes == null) {
                return;
            }
            for (int i = 0; i < actionNodes.length(); i++) {
                jo = actionNodes.getJSONObject(i);
                if (!Objects.equals("FEED", jo.getString("type"))) {
                    continue;
                }
                String consistencyKey = jo.getString("consistencyKey");
                jo = new JSONObject(AntFarmRpcCall.visitAnimalSendPrize(consistencyKey));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    String prizeName = jo.getString("prizeName");
                    String userMaskName = UserIdMap.getMaskName(AntFarmRpcCall.farmId2UserId(farmId));
                    Log.farm("小鸡到访💞爱心投喂[" + userMaskName + "]#获得[" + prizeName + "]");
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "visitAnimal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /* 抽抽乐 */
    private void enterDrawMachine() {
        doDrawTimesTask();
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterDrawMachine());
            int leftDrawTimes = jo.getJSONObject("userInfo").optInt("leftDrawTimes", 0);
            for (int i = 0; i < leftDrawTimes; i++) {
                if (!drawPrize()) {
                    return;
                }
                TimeUtil.sleep(5000);
            }
        } catch (Throwable t) {
            Log.i(TAG, "drawMachine err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void doDrawTimesTask() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.listFarmDrawTimesTask());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray farmTaskList = jo.getJSONArray("farmTaskList");
            for (int i = 0; i < farmTaskList.length(); i++) {
                jo = farmTaskList.getJSONObject(i);
                String taskStatus = jo.getString("taskStatus");
                if (TaskStatus.RECEIVED.name().equals(taskStatus)) {
                    continue;
                }
                if (TaskStatus.TODO.name().equals(taskStatus)) {
                    if (!Objects.equals(
                            ExtensionsHandle.handleRequest(new Request(RequestType.DO_FARM_DRAW_TIMES_TASK, jo)),
                            Boolean.TRUE)) {
                        continue;
                    }
                    TimeUtil.sleep(3000);
                }
                TimeUtil.sleep(2000);
                String taskId = jo.getString("taskId");
                String title = jo.getString("title");
                receiveFarmDrawTimesTaskAward(taskId, title);
            }
        } catch (Throwable t) {
            Log.i(TAG, "doFarmDrawTimesTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void receiveFarmDrawTimesTaskAward(String taskId, String title) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.receiveFarmDrawTimesTaskAward(taskId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.farm("装扮抽奖🎟️领取奖励[" + title + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveFarmDrawTimesTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean drawPrize() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.drawPrize());
            if (MessageUtil.checkResponse(TAG, jo)) {
                String title = jo.optString("title");
                Log.farm("装扮抽奖🎟️抽中奖品[" + title + "]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "drawPrize err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void queryDrawMachineActivity() {
       // ExtensionsHandle.handleRequest(new Request(RequestType.DO_FARM_IP_DRAW_TASK));
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryDrawMachineActivity());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            int drawTimes = jo.getInt("drawTimes");
            for (int i = 0; i < drawTimes; i++) {
                if (!drawMachine()) {
                    return;
                }
                TimeUtil.sleep(5000);
            }

        } catch (Throwable t) {
            Log.i(TAG, "queryDrawMachineActivity err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean drawMachine() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.drawMachine());
            if (MessageUtil.checkResponse(TAG, jo)) {
                String title = jo.getJSONObject("drawMachinePrize").getString("title");
                Log.farm("装扮抽奖🎟️抽中奖品[" + title + "]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "drawMachine err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    public static void listFarmIpDrawTask() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.listFarmIpDrawTask());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray ja = jo.getJSONArray("farmTaskList");
            for (int i = 0;i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                if (Objects.equals(TaskStatus.FINISHED.name(), jo.getString("taskStatus"))) {
                    String title = jo.getString("title");
                    String taskId = jo.getString("taskId");
                    receiveFarmIpDrawTaskAward(taskId, title);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "listFarmIpDrawTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void receiveFarmIpDrawTaskAward(String taskId, String title) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.receiveFarmIpDrawTaskAward(taskId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.farm("装扮抽奖🎟️领取奖励[" + title + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveFarmIpDrawTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /* 雇佣好友小鸡 */
    private synchronized Boolean hireAnimal() {
        if (!AnimalFeedStatus.EATING.name().equals(ownerAnimal.animalFeedStatus)) {
            return false;
        }
        int count = 3 - animals.length;
        boolean hasHireAnimal = false;
        try {
            int pageStartSum = 0;
            boolean hasNext = true;
            while (hasNext & count > 0) {
                if (foodStock < 50) {
                    Log.record("饲料不足，暂停雇佣");
                    break;
                }
                JSONObject jo = new JSONObject(AntFarmRpcCall.rankingList(pageStartSum));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    break;
                }
                JSONArray rankingList = jo.getJSONArray("rankingList");
                hasNext = jo.getBoolean("hasNext");
                pageStartSum += rankingList.length();
                for (int i = 0; i < rankingList.length() && count > 0; i++) {
                    jo = rankingList.getJSONObject(i);
                    String userId = jo.getString("userId");
                    boolean isHireAnimal = hireAnimalList.contains(userId);
                    if (hireAnimalType.getValue() != HireAnimalType.HIRE) {
                        isHireAnimal = !isHireAnimal;
                    }
                    if (!isHireAnimal || Objects.equals(userId, UserIdMap.getCurrentUid())) {
                        continue;
                    }
                    String actionTypeListStr = jo.getJSONArray("actionTypeList").toString();
                    if (actionTypeListStr.contains("can_hire_action")) {
                        if (hireAnimal(userId)) {
                            count--;
                            hasHireAnimal = true;
                        }
                    }
                }
            }
            if (count > 0) {
                Log.record("没有足够的小鸡可以雇佣");
            }
        } catch (Throwable t) {
            Log.i(TAG, "hireAnimal err:");
            Log.printStackTrace(TAG, t);
        }
        return hasHireAnimal;
    }

    private synchronized Boolean hireAnimal(String userId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterFarm("", userId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            jo = jo.getJSONObject("farmVO").getJSONObject("subFarmVO");
            String farmId = jo.getString("farmId");
            JSONArray animals = jo.getJSONArray("animals");
            for (int i = 0, len = animals.length(); i < len; i++) {
                JSONObject animal = animals.getJSONObject(i);
                if (Objects.equals(animal.getJSONObject("masterUserInfoVO").getString("userId"), userId)) {
                    String animalId = animal.getString("animalId");
                    return hireAnimal(farmId, animalId);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "hireAnimal(userId) err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private synchronized Boolean hireAnimal(String friendFarmId, String hireAnimalId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.hireAnimal(friendFarmId, hireAnimalId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                foodStock = jo.getInt("foodStock");
                int reduceFoodNum = jo.getInt("reduceFoodNum");
                String userMaskName = UserIdMap.getMaskName(AntFarmRpcCall.farmId2UserId(friendFarmId));
                Log.farm("雇佣小鸡👷雇佣好友[" + userMaskName + "]#消耗[" + reduceFoodNum + "g饲料]");
                JSONArray animals = jo.getJSONArray("animals");
                for (int i = 0; i < animals.length(); i++) {
                    jo = animals.getJSONObject(i);
                    if (!Objects.equals(hireAnimalId, jo.optString("animalId"))) {
                        continue;
                    }
                    String taskId = "UPDATE|HA|" + hireAnimalId;
                    long beHiredEndTime = jo.getLong("beHiredEndTime");
                    addChildTask(new ChildModelTask(taskId, "UPDATE", () -> {
                        autoHireAnimal(friendFarmId, hireAnimalId, beHiredEndTime);
                    }, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)));
                    return true;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "hireAnimal(friendFarmId, hireAnimalId) err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private synchronized void autoHireAnimal() {
        syncAnimalStatus(ownerFarmId);
        for (Animal animal : animals) {
            if (!SubAnimalType.WORK.name().equals(animal.subAnimalType)) {
                continue;
            }
            autoHireAnimal(animal.masterFarmId, animal.animalId, animal.beHiredEndTime);
        }
    }

    private synchronized void autoHireAnimal(String friendFarmId, String hireAnimalId, long beHiredEndTime) {
        String taskId = "HIRE|" + hireAnimalId;
        if (hasChildTask(taskId) && getChildTask(taskId).getExecTime() == beHiredEndTime) {
            return;
        }
        if (addChildTask(new ChildModelTask(taskId, "HIRE", () -> {
            if (!hireAnimal(friendFarmId, hireAnimalId)) {
                hireAnimal();
            }
            autoFeedAnimal();
        }, beHiredEndTime))) {
            String userMaskName = UserIdMap.getMaskName(AntFarmRpcCall.farmId2UserId(friendFarmId));
            Log.record("添加蹲点雇佣👷[" + userMaskName + "]在[" + TimeUtil.getCommonDateTime(beHiredEndTime) + "]执行");
        }
    }

    // 装扮焕新
    private void ornamentsDressUp() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.listOrnaments());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            List<JSONObject> list = new ArrayList<>();
            JSONArray achievementOrnaments = jo.getJSONArray("achievementOrnaments");
            long takeOffTime = System.currentTimeMillis();
            for (int i = 0; i < achievementOrnaments.length(); i++) {
                jo = achievementOrnaments.getJSONObject(i);
                if (!jo.optBoolean("acquired")) {
                    continue;
                }
                if (jo.has("takeOffTime")) {
                    takeOffTime = jo.getLong("takeOffTime");
                }
                String resourceKey = jo.getString("resourceKey");
                String name = jo.getString("name");
                if (ornamentsDressUpList.contains(resourceKey)) {
                    list.add(jo);
                }
                AchievementOrnamentIdMap.getInstance().add(resourceKey, name);
            }
            AchievementOrnamentIdMap.getInstance().save();
            if (list.isEmpty() || takeOffTime
                    + TimeUnit.DAYS.toMillis(ornamentsDressUpDays.getValue() - 15)
                    > System.currentTimeMillis()) {
                return;
            }

            jo = list.get(RandomUtil.nextInt(0, list.size() - 1));
            if (saveOrnaments(jo)) {
                Log.farm("装扮焕新✨保存装扮[" + jo.getString("name") + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "ornamentsDressUp err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean saveOrnaments(JSONObject ornaments) {
        try {
            String animalId = ownerAnimal.animalId;
            String farmId = ownerFarmId;
            String ornamentsSets = getOrnamentsSets(ornaments.getJSONArray("sets"));
            JSONObject jo = new JSONObject(AntFarmRpcCall.saveOrnaments(animalId, farmId, ornamentsSets));
            return MessageUtil.checkResponse(TAG, jo);
        } catch (Throwable t) {
            Log.i(TAG, "saveOrnaments err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private String getOrnamentsSets(JSONArray sets) {
        StringBuilder ornamentsSets = new StringBuilder();
        try {
            for (int i = 0; i < sets.length(); i++) {
                JSONObject set = sets.getJSONObject(i);
                if (i > 0) {
                    ornamentsSets.append(",");
                }
                ornamentsSets.append(set.getString("id"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "getOrnamentsSets err:");
            Log.printStackTrace(TAG, t);
        }
        return ornamentsSets.toString();
    }

    // 一起拿小鸡饲料
    private void letsGetChickenFeedTogether() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.letsGetChickenFeedTogether());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            String bizTraceId = jo.getString("bizTraceId");
            JSONArray p2pCanInvitePersonDetailList = jo.getJSONArray("p2pCanInvitePersonDetailList");

            int canInviteCount = 0;
            int hasInvitedCount = 0;
            List<String> userIdList = new ArrayList<>(); // 保存 userId
            for (int i = 0; i < p2pCanInvitePersonDetailList.length(); i++) {
                JSONObject personDetail = p2pCanInvitePersonDetailList.getJSONObject(i);
                String inviteStatus = personDetail.getString("inviteStatus");
                String userId = personDetail.getString("userId");

                if (inviteStatus.equals("CAN_INVITE")) {
                    userIdList.add(userId);
                    canInviteCount++;
                } else if (inviteStatus.equals("HAS_INVITED")) {
                    hasInvitedCount++;
                }
            }

            int invitedToday = hasInvitedCount;

            int remainingInvites = 5 - invitedToday;
            int invitesToSend = Math.min(canInviteCount, remainingInvites);

            if (invitesToSend == 0) {
                return;
            }

            if (getFeedType.getValue() == GetFeedType.GIVE) {
                for (String userId : userIdList) {
                    if (invitesToSend <= 0) {
//                            Log.record("已达到最大邀请次数限制，停止发送邀请。");
                        break;
                    }
                    if (getFeedList.contains(userId)) {
                        jo = new JSONObject(AntFarmRpcCall.giftOfFeed(bizTraceId, userId));
                        if (MessageUtil.checkResponse(TAG, jo)) {
                            Log.record("一起拿小鸡饲料🥡 [送饲料：" + UserIdMap.getMaskName(userId) + "]");
                            invitesToSend--; // 每成功发送一次邀请，减少一次邀请次数
                        } else {
                            Log.record("邀请失败：" + jo);
                            break;
                        }
                    } else {
//                            Log.record("用户 " + UserIdMap.getMaskName(userId) + " 不在勾选的好友列表中，不发送邀请。");
                    }
                }
            } else {
                Random random = new Random();
                for (int j = 0; j < invitesToSend; j++) {
                    int randomIndex = random.nextInt(userIdList.size());
                    String userId = userIdList.get(randomIndex);

                    jo = new JSONObject(AntFarmRpcCall.giftOfFeed(bizTraceId, userId));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Log.record("一起拿小鸡饲料🥡 [送饲料：" + UserIdMap.getMaskName(userId) + "]");
                    } else {
                        Log.record("邀请失败：" + jo);
                        break;
                    }
                    userIdList.remove(randomIndex);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "letsGetChickenFeedTogether err:");
            Log.printStackTrace(t);
        }
    }

    private void family() {
        try {
            JSONObject jo = enterFamily();
            if (jo == null) {
                return;
            }
            String groupId = jo.getString("groupId");
            int familyAwardNum = jo.getInt("familyAwardNum");
            boolean familySignTips = jo.getBoolean("familySignTips");

            JSONArray familyInteractActions = jo.getJSONArray("familyInteractActions");
            JSONObject assignFamilyMemberInfo = jo.getJSONObject("assignFamilyMemberInfo");
            JSONObject familyDrawInfo = jo.getJSONObject("familyDrawInfo");

            JSONArray familyAnimals = jo.getJSONArray("animals");
            ExtensionsHandle.handleRequest(
                    new Request(RequestType.ENABLE_DEVELOPER_MODE, RequestMethod.ANT_FARM_FAMILY, familyAnimals)
            );
            List<String> friendUserIds = getFamilyMemberList(false, false);
            for (int i = 0; i < familyAnimals.length(); i++) {
                jo = familyAnimals.getJSONObject(i);
                if (!friendUserIds.contains(jo.optString("userId"))) {
                    continue;
                }
                String farmId = jo.getString("farmId");
                JSONObject animalStatusVO = jo.getJSONObject("animalStatusVO");
                String animalFeedStatus = animalStatusVO.getString("animalFeedStatus");
                String animalInteractStatus = animalStatusVO.getString("animalInteractStatus");
                if (AnimalInteractStatus.HOME.name().equals(animalInteractStatus)
                        && AnimalFeedStatus.HUNGRY.name().equals(animalFeedStatus)) {
                    if (familyOptions.contains(AntFarmFamilyOption.FAMILY_FEED.name())) {
                        feedFriendAnimal(farmId);
                    }
                }
            }

            boolean canEatTogether = true;
            for (int i = 0; i < familyInteractActions.length(); i++) {
                jo = familyInteractActions.getJSONObject(i);
                if ("EatTogether".equals(jo.optString("familyInteractType"))) {
                    canEatTogether = false;
                }
            }

            if (familySignTips && familyOptions.contains(
                    AntFarmFamilyOption.FAMILY_SIGN.name())) {
                familySign();
            }
            if (canEatTogether && familyOptions.contains(
                    AntFarmFamilyOption.FAMILY_EAT_TOGETHER.name())) {
                familyEatTogether(groupId, getFamilyMemberList(true, true));
            }
            if (familyOptions.contains(
                    AntFarmFamilyOption.ASSIGN_FAMILY_MEMBER.name())) {
                assignFamilyMember(groupId, assignFamilyMemberInfo, getFamilyMemberList(true, false));
            }
            if (familyAwardNum > 0 && familyOptions.contains(
                    AntFarmFamilyOption.FAMILY_AWARD_LIST.name())) {
                familyAwardList();
            }
            if (familyDrawInfo.has("visitFamilyAnimalList") && familyOptions.contains(
                    AntFarmFamilyOption.RECEIVE_FAMILY_VISIT_AWARD.name())) {
                receiveFamilyVisitAward(familyDrawInfo.getJSONArray("visitFamilyAnimalList"));
            }
            if (familyDrawInfo.has("activityId") && familyOptions.contains(
                    AntFarmFamilyOption.FAMILY_DRAW.name())) {
                queryFamilyDrawActivity(groupId);
            }
        } catch (Throwable t) {
            Log.i(TAG, "family err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private String getFamilyGroupId() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryFamilyInfo());
            if (MessageUtil.checkResponse(TAG, jo)) {
                return jo.optString("groupId");
            }
        } catch (Throwable t) {
            Log.i(TAG, "getGroupId err:");
            Log.printStackTrace(t);
        }
        return null;
    }

    private JSONObject enterFamily() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterFamily());
            if (MessageUtil.checkResponse(TAG, jo)) {
                return jo;
            }
        } catch (Throwable t) {
            Log.i(TAG, "enterFamily err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    private void familyAwardList() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.familyAwardList());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray ja = jo.getJSONArray("familyAwardRecordList");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                if (jo.optBoolean("expired") || jo.optBoolean("received", true)) {
                    continue;
                }
                if (jo.has("linkUrl") || (jo.has("operability") && !jo.getBoolean("operability"))) {
                    MessagePush.sendMessage(
                            MessagePush.MessagePushChannel.SPECIAL_EVENT,
                            UserIdMap.getMaskName(UserIdMap.getCurrentUid()) + ">>有需要手动领取的亲密家庭奖励"
                    );
                    continue;
                }
                String rightId = jo.getString("rightId");
                String awardName = jo.getString("awardName");
                int count = jo.optInt("count", 1);
                receiveFamilyAward(rightId, awardName, count);
            }
        } catch (Throwable t) {
            Log.i(TAG, "familyAwardList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void receiveFamilyAward(String rightId, String awardName, int count) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.receiveFamilyAward(rightId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.farm("亲密家庭🏠领取奖励[" + awardName + "*" + count + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "familyAwardList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void familyReceiveFarmTaskAward(String taskId, String title) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.familyReceiveFarmTaskAward(taskId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.farm("亲密家庭🏠提交任务[" + title + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "familyReceiveFarmTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private JSONArray queryRecentFarmFood(int needCount) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.syncAnimalStatus(ownerFarmId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return null;
            }
            JSONArray cuisineList = jo.getJSONArray("cuisineList");
            if (cuisineList.length() == 0) {
                return null;
            }
            List<JSONObject> list = getSortedCuisineList(cuisineList);
            JSONArray result = new JSONArray();
            int count = 0;
            for (int i = 0; i < list.size() && count < needCount; i++) {
                jo = list.get(i);
                int countTemp = jo.getInt("count");
                if (count + countTemp >= needCount) {
                    countTemp = needCount - count;
                    jo.put("count", countTemp);
                }
                count += countTemp;
                result.put(jo);
            }
            if (count == needCount) {
                return result;
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryRecentFarmFood err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    private List<String> getFamilyMemberList(boolean containsNotFriend, boolean containsOwner) {
        List<String> familyMemberList = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryFamilyInfo());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return familyMemberList;
            }
            JSONArray familyMemberInfoList = jo.getJSONArray("familyMemberInfoList");
            for (int i = 0; i < familyMemberInfoList.length(); i++) {
                jo = familyMemberInfoList.getJSONObject(i);
                String userId = jo.getString("userId");
                if (jo.optBoolean("currentUser")
                        ? containsOwner
                        : (jo.optBoolean("friend") || containsNotFriend)
                ) {
                    familyMemberList.add(userId);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "getFamilyMemberList err:");
            Log.printStackTrace(TAG, t);
        }
        return familyMemberList;
    }

    private void familyEatTogether(String groupId, List<String> friendUserIds) {
        long currentTime = System.currentTimeMillis();
        String periodName;
        if (TimeUtil.isAfterTimeStr(currentTime, "0600") && TimeUtil.isBeforeTimeStr(currentTime, "1100")) {
            periodName = "早餐";
        } else if (TimeUtil.isAfterTimeStr(currentTime, "1100") && TimeUtil.isBeforeTimeStr(currentTime, "1600")) {
            periodName = "午餐";
        } else if (TimeUtil.isAfterTimeStr(currentTime, "1600") && TimeUtil.isBeforeTimeStr(currentTime, "2000")) {
            periodName = "晚餐";
        } else {
            return;
        }
        try {
            JSONArray cuisines = queryRecentFarmFood(friendUserIds.size());
            if (cuisines == null) {
                return;
            }
            JSONObject jo = new JSONObject(AntFarmRpcCall.familyEatTogether(groupId, cuisines, new JSONArray(friendUserIds)));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.farm("亲密家庭🏠美食请客[" + periodName + "]#消耗[" + friendUserIds.size() + "份美食]");
                syncFamilyStatus(groupId, "FAMILY_INTERACT_ACTION");
                syncFamilyStatus(groupId, "INTIMACY_VALUE");
            }
        } catch (Throwable t) {
            Log.i(TAG, "familyEatTogether err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void familySign() {
        familyReceiveFarmTaskAward("FAMILY_SIGN_TASK", "每日签到");
    }

    private void assignFamilyMember(String groupId, JSONObject assignFamilyMemberInfo, List<String> beAssignUserIds) {
        try {
            JSONObject jo = assignFamilyMemberInfo.getJSONObject("assignRights");
            if (!Objects.equals(ownerUserId, jo.optString("assignRightsOwner"))
                    || !Objects.equals("NOT_USED", jo.optString("status"))) {
                return;
            }
            if (beAssignUserIds.isEmpty()) {
                Log.record("顶梁柱特权:没有可以指派的家庭成员");
                return;
            }
            JSONArray assignConfigList = assignFamilyMemberInfo.getJSONArray("assignConfigList");
            jo = assignConfigList.getJSONObject(RandomUtil.nextInt(0, assignConfigList.length() - 1));
            String beAssignUser = beAssignUserIds.get(RandomUtil.nextInt(0, beAssignUserIds.size() - 1));
            String assignAction = jo.getString("assignAction");
            String assignDesc = jo.getString("assignDesc");
            jo = new JSONObject(AntFarmRpcCall.assignFamilyMember(assignAction, beAssignUser));
            if (MessageUtil.checkResponse(TAG, jo)) {
                String userMaskName = UserIdMap.getMaskName(beAssignUser);
                Log.farm("亲密家庭🏠指派成员[" + userMaskName + "]#" + assignDesc);
                syncFamilyStatus(groupId, "INTIMACY_VALUE");
            }
        } catch (Throwable t) {
            Log.i(TAG, "assignFamilyMember err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private JSONObject syncFamilyStatus(String groupId, String operType) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.syncFamilyStatus(groupId, operType, ownerUserId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                return jo;
            }
        } catch (Throwable t) {
            Log.i(TAG, "syncFamilyStatus err:");
            Log.printStackTrace(TAG, t);
        }
        return new JSONObject();
    }

    private void queryFamilyDrawActivity(String groupId) {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.queryFamilyDrawActivity());
            if (MessageUtil.checkResponse(TAG, jo)) {
                listFarmFamilyDrawTask();
                jo = syncFamilyStatus(groupId, "DRAW_TIMES");
                int drawTimes = jo.optInt("drawTimes");
                for (int i = 0; i < drawTimes; i++) {
                    if (!familyDraw()) {
                        return;
                    }
                    TimeUtil.sleep(5000);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryFamilyDrawActivity err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean familyDraw() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.familyDraw());
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("familyDrawPrize");
                String familyDrawPrize = jo.getString("title") + "*" + jo.getDouble("awardCount");
                Log.farm("亲密家庭🏠家庭抽奖#获得[" + familyDrawPrize + "]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "familyDraw err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void listFarmFamilyDrawTask() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.listFarmFamilyDrawTask());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray farmTaskList = jo.getJSONArray("farmTaskList");
            for (int i = 0; i < farmTaskList.length(); i++) {
                jo = farmTaskList.getJSONObject(i);
                if (TaskStatus.FINISHED.name().equals(jo.getString("taskStatus"))) {
                    receiveFarmFamilyDrawTaskAward(jo);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "listFarmFamilyDrawTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void receiveFarmFamilyDrawTaskAward(JSONObject task) {
        try {
            String taskId = task.getString("taskId");
            JSONObject jo = new JSONObject(AntFarmRpcCall.receiveFarmFamilyDrawTaskAward(taskId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            String title = task.getString("title");
            int awardCount = task.getInt("awardCount");
            Log.farm("亲密家庭🏠领取奖励[扭蛋任务:" + title + "]#获得[" + awardCount + "个扭蛋]");
        } catch (Throwable t) {
            Log.i(TAG, "receiveFarmFamilyDrawTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void receiveFamilyVisitAward(JSONArray visitFamilyAnimalList) {
        try {
            for (int i = 0; i < visitFamilyAnimalList.length(); i++) {
                JSONObject jo = visitFamilyAnimalList.getJSONObject(i);
                String friendUserId = jo.getString("sendCCUserId");
                String uniqueId = jo.getString("uniqueId");
                jo = new JSONObject(AntFarmRpcCall.receiveFamilyVisitAward(friendUserId, uniqueId));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    String userMaskName = UserIdMap.getMaskName(friendUserId);
                    Log.farm("亲密家庭🏠领取奖励[好友到访:" + userMaskName + "]");
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveFamilyVisitAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public interface RecallAnimalType {

        int ALWAYS = 0;
        int WHEN_THIEF = 1;
        int WHEN_HUNGRY = 2;
        int NEVER = 3;

        String[] nickNames = {"始终召回", "偷吃召回", "饥饿召回", "暂不召回"};
    }

    public interface SendBackAnimalWay {

        int HIT = 0;
        int NORMAL = 1;

        String[] nickNames = {"攻击", "常规"};

    }

    public interface SendBackAnimalType {

        int NONE = 0;
        int BACK = 1;
        int NOT_BACK = 2;

        String[] nickNames = {"不遣返小鸡", "遣返已选好友", "遣返未选好友"};

    }

    public enum AnimalBuff {
        ACCELERATING, INJURED, NONE
    }

    public enum AnimalFeedStatus {
        HUNGRY, EATING, SLEEPY
    }

    public enum AnimalInteractStatus {
        HOME, GOTOSTEAL, STEALING
    }

    public enum SubAnimalType {
        NORMAL, GUEST, PIRATE, WORK
    }

    public enum ToolType implements CustomOption {
        STEALTOOL("蹭饭卡"),
        ACCELERATETOOL("加速卡"),
        SHARETOOL("亲情卡"),//救济卡
        FENCETOOL("篱笆卡"),
        NEWEGGTOOL("新蛋卡"),
        DOLLTOOL("公仔补签卡"),
        ORDINARY_ORNAMENT_TOOL("普通装扮补签卡"),
        ADVANCE_ORNAMENT_TOOL("高级装扮补签卡"),
        RARE_ORNAMENT_TOOL("稀有装扮补签卡"),
        BIG_EATER_TOOL("加饭卡");

        private final String nickName;

        ToolType(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    public enum GameType {
        starGame, jumpGame, flyGame, hitGame;

        public static final CharSequence[] gameNames = {"星星球", "登山赛", "飞行赛", "欢乐揍小鸡"};

        public CharSequence gameName() {
            return gameNames[ordinal()];
        }
    }

    private static class Animal {
        public String animalId, currentFarmId, masterFarmId,
                animalBuff, subAnimalType, animalFeedStatus, animalInteractStatus;
        public String locationType;

        public String currentFarmMasterUserId;

        public Long startEatTime, beHiredEndTime;

        public Double consumeSpeed;

        public Double foodHaveEatten;

    }

    public enum TaskStatus {
        TODO, FINISHED, RECEIVED
    }

    public enum AntFarmFlag implements Status.StatusFlag {
        SIGN,
        DONATION,
        USE_FARM_TOOL_LIMIT,
        FEED_FRIEND_ANIMAL_LIMIT,
        VISIT_FRIEND_LIMIT
    }

    public enum AntFarmGameOption implements CustomOption {
        RECORD_FARM_GAME("玩游戏(星星球、登山赛、飞行赛、欢乐揍小鸡)"),
        DRAW_GAME_CENTER_AWARD("开宝箱"),
        BUY_MALL("乐园集市");

        private final String nickName;

        AntFarmGameOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    public enum AntFarmAccelerateToolOption implements CustomOption {
        USE_ACCELERATE_TOOL_CONTINUE("连续使用"),
        USE_ACCELERATE_TOOL_WHEN_MAX_EMOTION("仅在满状态时使用");

        private final String nickName;

        AntFarmAccelerateToolOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    public enum AntFarmFamilyOption implements CustomOption {
        FAMILY_SIGN("每日签到"),
        FAMILY_FEED("帮喂成员"),
        FAMILY_EAT_TOGETHER("美食请客"),
        FAMILY_AWARD_LIST("领取奖励"),
        ASSIGN_FAMILY_MEMBER("指派成员(使用顶梁柱特权)"),
        FAMILY_DRAW("家庭抽奖(开扭蛋)"),
        RECEIVE_FAMILY_VISIT_AWARD("领取家庭到访奖励");

        private final String nickName;

        AntFarmFamilyOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    private static class RewardFriend {
        public String consistencyKey, friendId, time;
    }

    private static class FarmTool {
        private ToolType toolType;
        private String toolId;
        private int toolCount, toolHoldLimit;
    }

    public interface HireAnimalType {

        int NONE = 0;
        int HIRE = 1;
        int NOT_HIRE = 2;

        String[] nickNames = {"不雇佣小鸡", "雇佣已选好友", "雇佣未选好友"};

    }

    public interface GetFeedType {

        int NONE = 0;
        int GIVE = 1;
        int RANDOM = 2;

        String[] nickNames = {"不赠送饲料", "赠送已选好友", "赠送随机好友"};

    }

    public interface NotifyFriendType {

        int NONE = 0;
        int NOTIFY = 1;
        int NOT_NOTIFY = 2;

        String[] nickNames = {"不通知赶鸡", "通知已选好友", "通知未选好友"};

    }

    public interface DonationType {

        int ZERO = 0;
        int ONE = 1;
        int ALL = 2;
        int ANY = 3;

        String[] nickNames = {"不捐赠", "捐赠一个项目", "捐赠所有项目", "捐赠任意项目"};

    }
}