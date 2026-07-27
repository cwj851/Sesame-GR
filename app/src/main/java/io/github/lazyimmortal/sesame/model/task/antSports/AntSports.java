package io.github.lazyimmortal.sesame.model.task.antSports;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.BooleanModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.ChoiceModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.IntegerModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectOneModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.AlipayUser;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.entity.idAndName.FlashSale;
import io.github.lazyimmortal.sesame.entity.idAndName.NeverLandBenefit;
import io.github.lazyimmortal.sesame.entity.idAndName.WalkPath;
import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.model.base.TaskCommon;
import io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandle;
import io.github.lazyimmortal.sesame.model.task.antMember.MerchantServiceRpcCall;
import io.github.lazyimmortal.sesame.model.task.immortal.AntForestAlpha;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestMethod;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.NotificationUtil;
import io.github.lazyimmortal.sesame.util.RandomUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.ThreadUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.FlashSaleIdMap;
import io.github.lazyimmortal.sesame.util.idMap.MerchantSeckillIdMap;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;
import io.github.lazyimmortal.sesame.util.idMap.NeverLandBenefitIdMap;

public class AntSports extends ModelTask {

    private static final String TAG = AntSports.class.getSimpleName();

    private int tmpStepCount = -1;
    private BooleanModelField walk;
    private ChoiceModelField walkPathTheme;
    private SelectOneModelField walkCustomPathList;
    private ChoiceModelField donateCharityCoinType;
    private IntegerModelField donateCharityCoinAmount;
    private IntegerModelField minExchangeCount;
    private IntegerModelField latestExchangeTime;
    private IntegerModelField syncStepCount;
    private BooleanModelField tiyubiz;
    private BooleanModelField club;
    private ChoiceModelField clubTrainItemType;
    private ChoiceModelField clubTradeMemberType;
    private SelectModelField clubTradeMemberList;
    private BooleanModelField sportsHealthCoinCenter;
    private SelectModelField sportsHealthCoinCenterOptions;
    private BooleanModelField neverLand;
    private SelectModelField neverLandOptions;
    private SelectModelField neverLandBenefitList;

    private BooleanModelField flashSaleSeckill;
    private SelectModelField flashSaleSeckillList;

    private IntegerModelField energyKeepCount;

    private IntegerModelField redPocketCountLimit;

    @Override
    public String getName() {
        return "运动";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.SPORTS;
    }

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(walk = new BooleanModelField("walk", "走路线 | 开启", false));
        modelFields.addField(walkPathTheme = new ChoiceModelField("walkPathTheme", "走路线 | 路线主题", WalkPathTheme.DA_MEI_ZHONG_GUO, WalkPathTheme.nickNames));
        modelFields.addField(walkCustomPathList = new SelectOneModelField("walkCustomPathList", "走路线 | 自定义路线列表", null, WalkPath::getList, "请选择要循环行走的路线"));
        modelFields.addField(club = new BooleanModelField("club", "抢好友 | 开启", false));
        modelFields.addField(clubTrainItemType = new ChoiceModelField("clubTrainItemType", "抢好友 | 训练动作", TrainItemType.NONE, TrainItemType.nickNames));
        modelFields.addField(clubTradeMemberType = new ChoiceModelField("clubTradeMemberType", "抢好友 | 抢购动作", TradeMemberType.NONE, TradeMemberType.nickNames));
        modelFields.addField(clubTradeMemberList = new SelectModelField("clubTradeMemberList", "抢好友 | 好友列表", new LinkedHashSet<>(), AlipayUser::getList));
        modelFields.addField(sportsHealthCoinCenter = new BooleanModelField("sportsHealthCoinCenter", "运动币 | 开启", false));
        modelFields.addField(sportsHealthCoinCenterOptions = new SelectModelField("sportsHealthCoinCenterOptions", "运动币 | 选项", new LinkedHashSet<>(), SportsHealthCoinCenterOption.class));
        modelFields.addField(donateCharityCoinType = new ChoiceModelField("donateCharityCoinType", "运动币 | 捐赠方式", DonateCharityCoinType.ZERO, DonateCharityCoinType.nickNames));
        modelFields.addField(donateCharityCoinAmount = new IntegerModelField("donateCharityCoinAmount", "运动币 | 捐赠数量(每次)", 100));
        modelFields.addField(tiyubiz = new BooleanModelField("tiyubiz", "文体中心", false));
        modelFields.addField(minExchangeCount = new IntegerModelField("minExchangeCount", "行走捐 | 最小捐步步数", 0));
        modelFields.addField(latestExchangeTime = new IntegerModelField("latestExchangeTime", "行走捐 | 最晚捐步时间(24小时制)", 22));
        modelFields.addField(syncStepCount = new IntegerModelField("syncStepCount", "自定义同步步数", 22000, 0, 100000));
        modelFields.addField(neverLand = new BooleanModelField("neverLand", "健康岛 | 开启", false));
        modelFields.addField(neverLandOptions = new SelectModelField("neverLandOptions", "健康岛 | 选项", new LinkedHashSet<>(), NeverLandOption.class));
        modelFields.addField(energyKeepCount = new IntegerModelField("energyKeepCount", "健康岛 | 能量保留", 2000));
        modelFields.addField(redPocketCountLimit = new IntegerModelField("redPocketCountLimit", "红包碎片 | 获得次数限制", 34));
        modelFields.addField(neverLandBenefitList = new SelectModelField("neverLandBenefitList", "健康岛 | 权益列表", new LinkedHashSet<>(), NeverLandBenefit::getList));
        modelFields.addField(flashSaleSeckill = new BooleanModelField("flashSaleSeckill", "权益兑换秒杀 | 开启", false));
        modelFields.addField(flashSaleSeckillList = new SelectModelField("flashSaleSeckillList", "权益兑换秒杀 | 列表", new LinkedHashSet<>(), FlashSale::getList));
        return modelFields;
    }

    @Override
    public void boot(ClassLoader classLoader) {
        try {
            XposedHelpers.findAndHookMethod("com.alibaba.health.pedometer.core.datasource.PedometerAgent", classLoader,
                    "readDailyStep", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            int originStep = (Integer) param.getResult();
                            int step = tmpStepCount();
                            if (TaskCommon.IS_AFTER_6AM && originStep < step) {
                                param.setResult(step);
                            }
                        }
                    });
            Log.i(TAG, "hook readDailyStep successfully");
        } catch (Throwable t) {
            Log.i(TAG, "hook readDailyStep err:");
            Log.printStackTrace(TAG, t);
        }
    }

    @Override
    public Boolean check() {
        if (TaskCommon.IS_ENERGY_TIME) {
            Log.other("任务暂停⏸️运动模块:当前为只收能量时间");
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            NotificationUtil.sendTaskNotification(this);
            if (!Status.hasFlagToday(AntSportsFlag.SYNC_STEP.flagName()) && TaskCommon.IS_AFTER_6AM) {
                ThreadUtil.start(this::syncStepCount);
            }

            if (flashSaleSeckill.getValue()) {
                queryFlashSaleItemList();
            }
            if (walk.getValue()) {
                walk();
            }

            if (donateCharityCoinType.getValue() != DonateCharityCoinType.ZERO)
                queryProjectList();

            if (minExchangeCount.getValue() > 0)
                queryWalkStep();

            if (tiyubiz.getValue()) {
                userTaskGroupQuery("SPORTS_DAILY_SIGN_GROUP");
                userTaskGroupQuery("SPORTS_DAILY_GROUP");
                userTaskRightsReceive();
                pathFeatureQuery();
                participate();
            }

            if (club.getValue()) {
                queryClubHome();
            }

            if (sportsHealthCoinCenter.getValue()) {
                if (sportsHealthCoinCenterOptions.contains(
                        SportsHealthCoinCenterOption.QUERY_COIN_TASK_PANEL.name())) {
                    sportsTasks();
                }
                if (sportsHealthCoinCenterOptions.contains(
                        SportsHealthCoinCenterOption.RECEIVE_COIN_ASSET.name())) {
                    receiveCoinAsset();
                }
                if (sportsHealthCoinCenterOptions.contains(
                        SportsHealthCoinCenterOption.COIN_EXCHANGE_DOUBLE_CARD.name())) {
                    coinExchangeItem("AMS2024032927086104");
                }
            }
            if (neverLand.getValue()) {
                querySign();
                queryTaskInfo();
                queryTaskCenter();
                queryBubbleTask();
                if (neverLandOptions.contains(NeverLandOption.QUERY_ITEM_LIST.name())) {
                    queryItemList();
                }
                if (neverLandOptions.contains(NeverLandOption.QUERY_COIN_TASK_PANEL.name())) {
                    queryCoinTaskPanel();
                }
                queryBaseInfo();
                queryEnergyBubbleModule();
            }
        } catch (Throwable t) {
            Log.i(TAG, "start.run err:");
            Log.printStackTrace(TAG, t);
        } finally {
            NotificationUtil.removeTaskNotification(this);
        }
    }

    private int tmpStepCount() {
        if (tmpStepCount >= 0) {
            return tmpStepCount;
        }
        tmpStepCount = syncStepCount.getValue();
        if (tmpStepCount > 0) {
            tmpStepCount = RandomUtil.nextInt(tmpStepCount, tmpStepCount + 2000);
            if (tmpStepCount > 100000) {
                tmpStepCount = 100000;
            }
        }
        return tmpStepCount;
    }

    public void syncStepCount() {
        try {
            int step = tmpStepCount();
            ClassLoader classLoader = ApplicationHook.getClassLoader();
            if ((Boolean) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(classLoader.loadClass("com.alibaba.health.pedometer.intergation.rpc.RpcManager"), "a"), "a", new Object[]{step, Boolean.FALSE, "system"})) {
                Log.other("同步步数🏃🏻‍♂️[" + step + "步]");
            } else {
                Log.record("同步运动步数失败:" + step);
            }
            Status.flagToday(AntSportsFlag.SYNC_STEP.flagName());
        } catch (Throwable t) {
            Log.i(TAG, "syncStepCount err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 运动
    private void sportsTasks() {
        try {
            signInCoinTask();
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryCoinTaskPanel());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            JSONArray taskList = jo.getJSONArray("taskList");
            for (int i = 0; i < taskList.length(); i++) {
                jo = taskList.getJSONObject(i);

                String taskStatus = jo.getString("taskStatus");
                if (TaskStatus.HAS_RECEIVED.name().equals(taskStatus)) {
                    return;
                }

                String taskName = jo.getString("taskName");
                if (TaskStatus.WAIT_RECEIVE.name().equals(taskStatus)) {
                    String assetId = jo.getString("assetId");
                    int prizeAmount = jo.getInt("prizeAmount");
                    if (receiveCoinAsset(assetId, prizeAmount, taskName)) {
                        TimeUtil.sleep(1000);
                    }
                    continue;
                }

                if (!jo.has("taskAction")) {
                    continue;
                }
                if (TaskStatus.WAIT_COMPLETE.name().equals(taskStatus)) {
                    String taskAction = jo.getString("taskAction");
                    String taskId = jo.getString("taskId");
                    if (jo.optBoolean("multiTask")) {
                        int currentNum = jo.getInt("currentNum") + 1;
                        int limitConfigNum = jo.getInt("limitConfigNum");
                        taskName = taskName.replaceAll("（.*/.*）", "(" + currentNum + "/" + limitConfigNum + ")");
                    }
                    if (jo.optBoolean("needSignUp") && !signUpTask(taskId)) {
                        continue;
                    }
                    if (completeTask(taskAction, taskId, taskName)) {
                        TimeUtil.sleep(1000);
                    }
                    continue;
                }

                Log.record("Found New Sport TaskStatus:" + taskStatus);
            }
        } catch (Throwable t) {
            Log.i(TAG, "sportsTasks err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean signUpTask(String taskId) {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.signUpTask(taskId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "signUpTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private Boolean completeTask(String taskAction, String taskId, String taskName) {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.completeTask(taskAction, taskId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.other("运动任务🧾[做任务得运动币:" + taskName + "]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "completeTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void signInCoinTask() {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.signInCoinTask());

            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject data = jo.getJSONObject("data");
            if (!data.getBoolean("signed")) {
                JSONObject subscribeConfig;
                if (data.has("subscribeConfig")) {
                    subscribeConfig = data.getJSONObject("subscribeConfig");
                    Log.other("运动任务🧾[做任务得运动币:签到"
                            + subscribeConfig.getString("subscribeExpireDays") + "天]#获得["
                            + data.getString("toast") + "运动币]");
                } else {
//                        Log.record("没有签到");
                }
            } else {
                Log.record("运动签到今日已签到");
            }
        } catch (Throwable t) {
            Log.i(TAG, "signInCoinTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void receiveCoinAsset() {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryCoinBubbleModule());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject data = jo.getJSONObject("data");
            if (!data.has("receiveCoinBubbleList"))
                return;
            JSONArray ja = data.getJSONArray("receiveCoinBubbleList");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                String assetId = jo.getString("assetId");
                int coinAmount = jo.getInt("coinAmount");
                String simpleSourceName = jo.optString("simpleSourceName");
                if (receiveCoinAsset(assetId, coinAmount, simpleSourceName)) {
                    TimeUtil.sleep(500);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveCoinAsset err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean receiveCoinAsset(String assetId, int coinAmount, String title) {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.receiveCoinAsset(assetId, coinAmount));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.other("收运动币💰领取奖励[" + title + "]#获得[" + coinAmount + "运动币]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveCoinAsset err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    /*
     * 新版行走路线 -- begin
     */
    private void walk() {
        String goingPathId = queryGoingPathId();
        do {
            String tempPathId = (String) ExtensionsHandle.handleRequest(
                    new Request(
                            RequestType.ENABLE_DEVELOPER_MODE,
                            RequestMethod.ANT_SPORTS_WALK_PATH
                    )
            );
            if (tempPathId != null) {
                goingPathId = tempPathId;
            }
            TimeUtil.sleep(1000);
            if (isNeedJoinNewPath(goingPathId)) {
                String joinPathId = queryJoinPathId();
                if (checkJoinPathId(joinPathId)) {
                    if (!joinPath(joinPathId)) {
                        return;
                    }
                    goingPathId = joinPathId;
                }
            }
        } while (walkGo(queryPath(goingPathId)));
    }

    private Boolean isNeedJoinNewPath(String goingPathId) {
        if (goingPathId.isEmpty()) {
            return true;
        }
        try {
            JSONObject jo = queryPath(goingPathId);
            jo = jo.getJSONObject("userPathStep");
            if (jo.optBoolean("dayLimit")) {
                return true;
            }
            String pathCompleteStatus = jo.getString("pathCompleteStatus");
            if (PathCompleteStatus.COMPLETED.name().equals(pathCompleteStatus)) {
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "isNeedJoinNewPath err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private Boolean hasTreasureBox() {
        if (Status.hasFlagToday(AntSportsFlag.TREASURE_BOX_LIMIT.flagName())) {
            return false;
        }
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryMailList());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            JSONArray ja = jo.getJSONArray("userMailList");
            int count = 0;
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                String templateId=jo.optString("templateId");
                if ("SPORTSPROD_GOPATH_AWARD_BOX".equals(templateId)||"SPORTSPROD_GOPATH_AWARD_BOX_V2".equals(templateId)) {
                    if (!TimeUtil.isToday(jo.getLong("receiveTime"))) {
                        break;
                    }
                    count++;
                }
            }
            if (count < 20) {
                return true;
            }
            Status.flagToday(AntSportsFlag.TREASURE_BOX_LIMIT.flagName());
        } catch (Throwable t) {
            Log.i(TAG, "hasTreasureBox err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private Boolean walkGo(JSONObject pathData) {
        try {
            JSONObject path = pathData.getJSONObject("path");
            JSONObject userPathStep = pathData.getJSONObject("userPathStep");
            int minGoStepCount = path.getInt("minGoStepCount");
            int pathStepCount = path.getInt("pathStepCount");
            if (path.has("dailyMaxGoStepCount")) {
                pathStepCount = path.getInt("dailyMaxGoStepCount");
            }
            int forwardStepCount = userPathStep.getInt("forwardStepCount");
            int remainStepCount = userPathStep.getInt("remainStepCount");
            boolean dayLimit = userPathStep.getBoolean("dayLimit");
            int useStepCount = Math.min(
                    Math.min(remainStepCount, hasTreasureBox() ? RandomUtil.nextInt(500, 1000) : remainStepCount),
                    Math.max(pathStepCount - forwardStepCount % pathStepCount, minGoStepCount)
            );
            if (useStepCount < minGoStepCount || dayLimit) {
                return false;
            }
            String pathId = path.getString("pathId");
            String pathName = path.getString("name");
            return walkGo(pathName, pathId, useStepCount);
        } catch (Throwable t) {
            Log.i(TAG, "walkGo err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private Boolean walkGo(String pathName, String pathId, int useStepCount) {
        boolean result = false;
        try {
            String date = Log.getFormatDate();
            JSONObject jo = new JSONObject(AntSportsRpcCall.walkGo(date, pathId, useStepCount));
            if (MessageUtil.checkResponse(TAG, jo)) {
                result = true;
                Log.other("行走路线🚶🏻‍♂️行走路线[" + pathName + "]#前进了" + useStepCount + "步");
                if(jo.has("resData")){
                    jo = jo.getJSONObject("resData");
                }
                jo = jo.getJSONObject("data");
                if (jo.has("completeInfo")) {
                    Log.other("行走路线🚶🏻‍♂️完成路线[" + pathName + "]");
                }
                parseRewardsByJSONObjectData(jo);
            }
        } catch (Throwable t) {
            Log.i(TAG, "walkGo err:");
            Log.printStackTrace(TAG, t);
        }
        return result;
    }

    private JSONObject queryWorldMap(String themeId) {
        JSONObject theme = null;
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryWorldMap(themeId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                theme = jo.getJSONObject("data");
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryWorldMap err:");
            Log.printStackTrace(TAG, t);
        }
        return theme;
    }

    private JSONObject queryCityPath(String cityId) {
        JSONObject city = null;
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryCityPath(cityId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                city = jo.getJSONObject("data");
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryCityPath err:");
            Log.printStackTrace(TAG, t);
        }
        return city;
    }

    private static JSONObject queryPath(String pathId) {
        JSONObject path = null;
        try {
            String date = Log.getFormatDate();
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryPath(date, pathId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                path = jo.getJSONObject("data");
                parseRewardsByJSONObjectData(path);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryPath err:");
            Log.printStackTrace(TAG, t);
        }
        return path;
    }

    private static void openTreasureBox(JSONArray treasureBoxList) {
        try {
            for (int i = 0; i < treasureBoxList.length(); i++) {
                JSONObject treasureBox = treasureBoxList.getJSONObject(i);
                receiveEvent(treasureBox.getString("boxNo"));
                TimeUtil.sleep(1000);
            }
        } catch (Throwable t) {
            Log.i(TAG, "openTreasureBox err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void receiveEvent(String eventBillNo) {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.receiveEvent(eventBillNo));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                parseRewardsByJSONArrayRewards(jo.getJSONArray("rewards"), 0);
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveEvent err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void parseRewardsByJSONArrayRewards(JSONArray rewards, int rewardsType) {
        String rewardsTypeName;
        switch (rewardsType) {
            case 0:
                rewardsTypeName = "宝箱奖励";
                break;
            case 1:
                rewardsTypeName = "中奖奖励";
                break;
            case 2:
                rewardsTypeName = "终点奖励";
                break;
            default:
                rewardsTypeName = "未知奖励";
                break;
        }
        try {
            for (int i = 0; i < rewards.length(); i++) {
                JSONObject jo = rewards.getJSONObject(i);
                if (jo.has("rewardStatus")
                        && !"SUCCESS".equals(jo.getString("rewardStatus"))) {
                    // rewardStatus : SUCCESS NOT_HIT
                    continue;
                }
                Log.other("行走路线🚶🏻‍♂️" + rewardsTypeName
                        + "[" + jo.getString("rewardName") + "*" + jo.getInt("count") + "]"
                );
            }
        } catch (Throwable t) {
            Log.i(TAG, "parseRewardsByJSONArrayRewards err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void parseRewardsByJSONObjectData(JSONObject data) {
        try {
            JSONArray treasureBoxList = data.getJSONArray("treasureBoxList");
            openTreasureBox(treasureBoxList);
            if (data.has("brandRewardVOs")) {
                JSONArray brandRewardVOs = data.getJSONArray("brandRewardVOs");
                parseRewardsByJSONArrayRewards(brandRewardVOs, 1);
            }
            if (data.has("completeInfo")) {
                data = data.getJSONObject("completeInfo");
                JSONArray completeRewards = data.getJSONArray("completeRewards");
                parseRewardsByJSONArrayRewards(completeRewards, 2);
            }
        } catch (Throwable t) {
            Log.i(TAG, "parseRewardsByJSONObjectData err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private String queryGoingPathId() {
        String goingPathId = "";
        try {
            String date = Log.getFormatDate();
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryPath(date, ""));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                goingPathId = jo.optString("goingPathId");
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryGoingPathId err:");
            Log.printStackTrace(TAG, t);
        }
        return goingPathId;
    }

    private String queryJoinPathId() {
        String pathId = walkCustomPathList.getValue();
        if (pathId != null) {
            return pathId;
        }

        try {
            String themeId = WalkPathTheme.walkPathThemeIds[walkPathTheme.getValue()];
            JSONObject theme = queryWorldMap(themeId);
            if (theme == null) {
                return pathId;
            }
            JSONArray cityList = theme.getJSONArray("cityList");
            for (int i = 0; i < cityList.length(); i++) {
                String cityId = cityList.getJSONObject(i).getString("cityId");
                JSONObject city = queryCityPath(cityId);
                if (city == null) {
                    continue;
                }
                JSONArray cityPathList = city.getJSONArray("cityPathList");
                for (int j = 0; j < cityPathList.length(); j++) {
                    JSONObject cityPath = cityPathList.getJSONObject(j);
                    pathId = cityPath.getString("pathId");
                    String pathCompleteStatus = cityPath.getString("pathCompleteStatus");
                    if (!PathCompleteStatus.COMPLETED.name().equals(pathCompleteStatus)) {
                        return pathId;
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryJoinPathId err:");
            Log.printStackTrace(TAG, t);
        }
        return pathId;
    }

    public static Boolean checkJoinPathId(String joinPathId) {
        try {
            JSONObject jo = queryPath(joinPathId);
            String goingPathId = jo.optString("goingPathId");
            if (Objects.equals(goingPathId, joinPathId)) {
                return false;
            }
            jo = jo.getJSONObject("userPathStep");
            return !jo.optBoolean("dayLimit");
        } catch (Throwable t) {
            Log.i(TAG, "checkJoinPathId err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    public static Boolean joinPath(String pathId) {
        if (pathId == null) {
            // 守护体育梦
            pathId = "p000202408231708";
        }
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.joinPath(pathId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                JSONObject pathData = queryPath(pathId);
                String pathName = pathData.getJSONObject("path").getString("name");
                Log.other("行走路线🚶🏻‍♂️加入路线[" + pathName + "]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "joinPath err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    /*
     * 新版行走路线 -- end
     */
    private Boolean canDonateCharityCoinToday() {
        if (Status.hasFlagToday(AntSportsFlag.DONATE_CHARITY_COIN.flagName())) {
            return false;
        }
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryDonateRecord());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            JSONArray footballFieldLongModel = jo.getJSONArray("footballFieldLongModel");
            if (footballFieldLongModel.length() == 0) {
                return true;
            }
            jo = footballFieldLongModel.getJSONObject(0);
            jo = jo.getJSONObject("personStatModel");
            long lastDonationTime = jo.getLong("lastDonationTime");
            if (TimeUtil.isLessThanNowOfDays(lastDonationTime)) {
                return true;
            }
            Status.flagToday(AntSportsFlag.DONATE_CHARITY_COIN.flagName());
        } catch (Throwable t) {
            Log.i(TAG, "canDonateCharityCoinToday err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void queryProjectList() {
        if (!canDonateCharityCoinToday()) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryProjectList(0));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            int charityCoinCount = jo.getInt("charityCoinCount");
            int donateCharityCoin = donateCharityCoinAmount.getValue();
            if (charityCoinCount < donateCharityCoin) {
                return;
            }
            JSONArray ja = jo.getJSONObject("projectPage").getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i).getJSONObject("basicModel");
                if (jo.optInt("acwProjectStatus") == 0) {
                    // acwProjectStatus: 0 1
                    continue;
                }
                // footballFieldStatus: OPENING_DONATE DONATE_COMPLETED
                if ("DONATE_COMPLETED".equals(jo.getString("footballFieldStatus"))) {
                    break;
                }
                if (donate(donateCharityCoin, jo.getString("projectId"), jo.getString("title"))) {
                    charityCoinCount -= donateCharityCoin;
                    if (donateCharityCoinType.getValue() != DonateCharityCoinType.ALL) {
                        break;
                    }
                    if (charityCoinCount < donateCharityCoin) {
                        break;
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryProjectList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean donate(int donateCharityCoin, String projectId, String title) {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.donate(donateCharityCoin, projectId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.other("公益捐赠❤️[捐赠运动币:" + title + "]#捐赠" + donateCharityCoin + "运动币");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "donate err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private Boolean canDonateWalkExchangeToday() {
        if (Status.hasFlagToday(AntSportsFlag.DONATE_WALK.flagName())) {
            return false;
        }
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.donateExchangeRecord());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            JSONArray userExchangeRecords = jo.getJSONArray("userExchangeRecords");
            if (userExchangeRecords.length() == 0) {
                return true;
            }
            jo = userExchangeRecords.getJSONObject(0);
            long gmtCreate = jo.getLong("gmtCreate");
            if (TimeUtil.isLessThanNowOfDays(gmtCreate)) {
                return true;
            }
            Status.flagToday(AntSportsFlag.DONATE_WALK.flagName());
        } catch (Throwable t) {
            Log.i(TAG, "canDonateWalkExchangeToday err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void queryWalkStep() {
        if (!canDonateWalkExchangeToday()) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryWalkStep());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("dailyStepModel");
            int produceQuantity = jo.getInt("produceQuantity");
            int hour = Integer.parseInt(Log.getFormatTime().split(":")[0]);
            if (produceQuantity < minExchangeCount.getValue() && hour < latestExchangeTime.getValue()) {
                return;
            }

            AntSportsRpcCall.walkDonateSignInfo(produceQuantity);
            jo = new JSONObject(AntSportsRpcCall.donateWalkHome(produceQuantity));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject walkDonateHomeModel = jo.getJSONObject("walkDonateHomeModel");
            JSONObject walkUserInfoModel = walkDonateHomeModel.getJSONObject("walkUserInfoModel");
            if (!walkUserInfoModel.has("exchangeFlag")) {
                return;
            }

            String donateToken = walkDonateHomeModel.getString("donateToken");
            JSONObject walkCharityActivityModel = walkDonateHomeModel.getJSONObject("walkCharityActivityModel");
            String activityId = walkCharityActivityModel.getString("activityId");

            jo = new JSONObject(AntSportsRpcCall.donateWalkExchange(activityId, produceQuantity, donateToken));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject donateExchangeResultModel = jo.getJSONObject("donateExchangeResultModel");
            int userCount = donateExchangeResultModel.getInt("userCount");
            double amount = donateExchangeResultModel.getJSONObject("userAmount").getDouble("amount");
            String donateTitle = donateExchangeResultModel.getString("donateTitle");
            Log.other("公益捐赠❤️[捐步做公益:" + donateTitle + "]#捐赠" + userCount + "步,兑换" + amount + "元公益金");
        } catch (Throwable t) {
            Log.i(TAG, "queryWalkStep err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /* 文体中心 */// SPORTS_DAILY_SIGN_GROUP SPORTS_DAILY_GROUP
    private void userTaskGroupQuery(String groupId) {
        try {
            String s = AntSportsRpcCall.userTaskGroupQuery(groupId);
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                jo = jo.getJSONObject("group");
                JSONArray userTaskList = jo.getJSONArray("userTaskList");
                for (int i = 0; i < userTaskList.length(); i++) {
                    jo = userTaskList.getJSONObject(i);
                    if (!"TODO".equals(jo.getString("status")))
                        continue;
                    JSONObject taskInfo = jo.getJSONObject("taskInfo");
                    String bizType = taskInfo.getString("bizType");
                    String taskId = taskInfo.getString("taskId");
                    jo = new JSONObject(AntSportsRpcCall.userTaskComplete(bizType, taskId));
                    if (jo.optBoolean("success")) {
                        String taskName = taskInfo.optString("taskName", taskId);
                        Log.other("完成任务🧾[" + taskName + "]");
                    } else {
                        Log.record("文体每日任务" + " " + jo);
                    }
                }
            } else {
                Log.record("文体每日任务" + " " + s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "userTaskGroupQuery err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void participate() {
        try {
            String s = AntSportsRpcCall.queryAccount();
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                double balance = jo.getDouble("balance");
                if (balance < 100)
                    return;
                jo = new JSONObject(AntSportsRpcCall.queryRoundList());
                if (jo.optBoolean("success")) {
                    JSONArray dataList = jo.getJSONArray("dataList");
                    for (int i = 0; i < dataList.length(); i++) {
                        jo = dataList.getJSONObject(i);
                        if (!"P".equals(jo.getString("status")))
                            continue;
                        if (jo.has("userRecord"))
                            continue;
                        JSONArray instanceList = jo.getJSONArray("instanceList");
                        int pointOptions = 0;
                        String roundId = jo.getString("id");
                        String InstanceId = null;
                        String ResultId = null;
                        for (int j = instanceList.length() - 1; j >= 0; j--) {
                            jo = instanceList.getJSONObject(j);
                            if (jo.getInt("pointOptions") < pointOptions)
                                continue;
                            pointOptions = jo.getInt("pointOptions");
                            InstanceId = jo.getString("id");
                            ResultId = jo.getString("instanceResultId");
                        }
                        jo = new JSONObject(AntSportsRpcCall.participate(pointOptions, InstanceId, ResultId, roundId));
                        if (jo.optBoolean("success")) {
                            jo = jo.getJSONObject("data");
                            String roundDescription = jo.getString("roundDescription");
                            int targetStepCount = jo.getInt("targetStepCount");
                            Log.other("走路挑战🚶🏻‍♂️[" + roundDescription + "]#" + targetStepCount);
                        } else {
                            Log.record("走路挑战赛" + " " + jo);
                        }
                    }
                } else {
                    Log.record("queryRoundList" + " " + jo);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "participate err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void userTaskRightsReceive() {
        try {
            String s = AntSportsRpcCall.userTaskGroupQuery("SPORTS_DAILY_GROUP");
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                jo = jo.getJSONObject("group");
                JSONArray userTaskList = jo.getJSONArray("userTaskList");
                for (int i = 0; i < userTaskList.length(); i++) {
                    jo = userTaskList.getJSONObject(i);
                    if (!"COMPLETED".equals(jo.getString("status")))
                        continue;
                    String userTaskId = jo.getString("userTaskId");
                    JSONObject taskInfo = jo.getJSONObject("taskInfo");
                    String taskId = taskInfo.getString("taskId");
                    jo = new JSONObject(AntSportsRpcCall.userTaskRightsReceive(taskId, userTaskId));
                    if (jo.optBoolean("success")) {
                        String taskName = taskInfo.optString("taskName", taskId);
                        JSONArray rightsRuleList = taskInfo.getJSONArray("rightsRuleList");
                        StringBuilder award = new StringBuilder();
                        for (int j = 0; j < rightsRuleList.length(); j++) {
                            jo = rightsRuleList.getJSONObject(j);
                            award.append(jo.getString("rightsName")).append("*").append(jo.getInt("baseAwardCount"));
                        }
                        Log.other("领取奖励🎖️[" + taskName + "]#" + award);
                    } else {
                        Log.record("文体中心领取奖励");
                        Log.i(jo.toString());
                    }
                }
            } else {
                Log.record("文体中心领取奖励");
                Log.i(s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "userTaskRightsReceive err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void pathFeatureQuery() {
        try {
            String s = AntSportsRpcCall.pathFeatureQuery();
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                JSONObject path = jo.getJSONObject("path");
                String pathId = path.getString("pathId");
                String title = path.getString("title");
                int minGoStepCount = path.getInt("minGoStepCount");
                if (jo.has("userPath")) {
                    JSONObject userPath = jo.getJSONObject("userPath");
                    String userPathRecordStatus = userPath.getString("userPathRecordStatus");
                    if ("COMPLETED".equals(userPathRecordStatus)) {
                        pathMapHomepage(pathId);
                        pathMapJoin(title, pathId);
                    } else if ("GOING".equals(userPathRecordStatus)) {
                        pathMapHomepage(pathId);
                        String countDate = Log.getFormatDate();
                        jo = new JSONObject(AntSportsRpcCall.stepQuery(countDate, pathId));
                        if (jo.optBoolean("success")) {
                            int canGoStepCount = jo.getInt("canGoStepCount");
                            if (canGoStepCount >= minGoStepCount) {
                                String userPathRecordId = userPath.getString("userPathRecordId");
                                tiyubizGo(countDate, title, canGoStepCount, pathId, userPathRecordId);
                            }
                        }
                    }
                } else {
                    pathMapJoin(title, pathId);
                }
            } else {
                Log.i(TAG, jo.getString("resultDesc"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "pathFeatureQuery err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void pathMapHomepage(String pathId) {
        try {
            String s = AntSportsRpcCall.pathMapHomepage(pathId);
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                if (!jo.has("userPathGoRewardList"))
                    return;
                JSONArray userPathGoRewardList = jo.getJSONArray("userPathGoRewardList");
                for (int i = 0; i < userPathGoRewardList.length(); i++) {
                    jo = userPathGoRewardList.getJSONObject(i);
                    if (!"UNRECEIVED".equals(jo.getString("status")))
                        continue;
                    String userPathRewardId = jo.getString("userPathRewardId");
                    jo = new JSONObject(AntSportsRpcCall.rewardReceive(pathId, userPathRewardId));
                    if (jo.optBoolean("success")) {
                        jo = jo.getJSONObject("userPathRewardDetail");
                        JSONArray rightsRuleList = jo.getJSONArray("userPathRewardRightsList");
                        StringBuilder award = new StringBuilder();
                        for (int j = 0; j < rightsRuleList.length(); j++) {
                            jo = rightsRuleList.getJSONObject(j).getJSONObject("rightsContent");
                            award.append(jo.getString("name")).append("*").append(jo.getInt("count"));
                        }
                        Log.other("文体宝箱🎁[" + award + "]");
                    } else {
                        Log.record("文体中心开宝箱");
                        Log.i(jo.toString());
                    }
                }
            } else {
                Log.record("文体中心开宝箱");
                Log.i(s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "pathMapHomepage err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void pathMapJoin(String title, String pathId) {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.pathMapJoin(pathId));
            if (jo.optBoolean("success")) {
                Log.other("加入线路🚶🏻‍♂️[" + title + "]");
                pathFeatureQuery();
            } else {
                Log.i(TAG, jo.toString());
            }
        } catch (Throwable t) {
            Log.i(TAG, "pathMapJoin err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void tiyubizGo(String countDate, String title, int goStepCount, String pathId,
                           String userPathRecordId) {
        try {
            String s = AntSportsRpcCall.tiyubizGo(countDate, goStepCount, pathId, userPathRecordId);
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                jo = jo.getJSONObject("userPath");
                Log.other("行走线路🚶🏻‍♂️[" + title + "]#前进了" + jo.getInt("userPathRecordForwardStepCount") + "步");
                pathMapHomepage(pathId);
                boolean completed = "COMPLETED".equals(jo.getString("userPathRecordStatus"));
                if (completed) {
                    Log.other("行走路线🚶🏻‍♂️完成线路[" + title + "]");
                    pathFeatureQuery();
                }
            } else {
                Log.i(TAG, s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "tiyubizGo err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /* 抢好友大战 */
    private void queryClubHome() {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryClubHome());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray roomList = jo.getJSONArray("roomList");
            for (int i = 0; i < roomList.length(); i++) {
                JSONObject room = roomList.getJSONObject(i);
                String roomId = room.getString("roomId");
                queryClubRoom(roomId);
                TimeUtil.sleep(1000);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryClubHome err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void queryClubRoom(String roomId) {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryClubRoom(roomId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            if (jo.has("bubbleList")) {
                JSONArray bubbleList = jo.getJSONArray("bubbleList");
                for (int i = 0; i < bubbleList.length(); i++) {
                    JSONObject bubble = bubbleList.getJSONObject(i);
                    collectBubble(bubble);
                }
            }
            JSONArray memberDetailList = jo.getJSONArray("memberDetailList");
            if (memberDetailList.length() == 0) {
                if (clubTradeMemberType.getValue() != TradeMemberType.NONE) {
                    JSONObject member = queryMemberPriceRanking();
                    if (buyMember(roomId, member)) {
                        queryClubRoom(roomId);
                    }
                }
                return;
            }
            if (clubTrainItemType.getValue() != TrainItemType.NONE) {
                for (int i = 0; i < memberDetailList.length(); i++) {
                    JSONObject member = memberDetailList.getJSONObject(i);
                    member = member.getJSONObject("memberModel");
                    trainMember(member);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryClubRoom err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 抢好友大战-收集运动币
    private void collectBubble(JSONObject bubble) {
        try {
            String bubbleId = bubble.getString("bubbleId");
            JSONObject jo = new JSONObject(AntSportsRpcCall.collectBubble(bubbleId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                int collectCoin = jo.getInt("collectCoin");
                Log.other("好友大战🥋收取奖励#获得[" + collectCoin + "运动币]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "collectBubble err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 抢好友大战-训练好友
    private void trainMember(JSONObject member) {
        try {
            String roomId = member.getString("roomId");
            String memberId = member.getString("memberId");
            String originBossId = member.getString("originBossId");
            JSONObject trainInfo = member.getJSONObject("trainInfo");

            String userName = UserIdMap.getMaskName(originBossId);
            if (!trainInfo.getBoolean("training")) {
                String itemType = TrainItemType.itemTypes[clubTrainItemType.getValue()];
                if (StringUtil.isEmpty(itemType)) {
                    return;
                }
                JSONObject jo = new JSONObject(AntSportsRpcCall.trainMember(itemType, memberId, originBossId));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return;
                }
                String name = TrainItemType.nickNames[clubTrainItemType.getValue()];
                Log.other("好友大战🥋训练好友[" + userName + "]" + name);
                trainInfo = jo.getJSONObject("trainInfo");
            }

            Long gmtEnd = trainInfo.getLong("gmtEnd");
            long updateTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);
            addChildTask(new ChildModelTask(roomId, "", () -> {
                autoTrainMember(roomId, gmtEnd);
            }, updateTime));
        } catch (Throwable t) {
            Log.i(TAG, "trainMember err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 抢好友大战-蹲点训练
    private void autoTrainMember(String roomId, Long gmtEnd) {
        String taskId = "TRAIN|" + roomId;
        if (!hasChildTask(taskId)) {
            addChildTask(new ChildModelTask(taskId, "TRAIN", () -> {
                queryClubRoom(roomId);
            }, gmtEnd));
            int roomIdInt = Integer.parseInt(roomId.substring(2, 8));
            Log.record("添加蹲点训练🥋[" + roomIdInt + "号房嘉宾]在[" + TimeUtil.getCommonDateTime(gmtEnd) + "]执行");
        }
    }

    // 抢好友大战-抢购好友
    private JSONObject queryMemberPriceRanking() {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryMemberPriceRanking());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return null;
            }
            int coinBalance = jo.getInt("coinBalance");
            jo = jo.getJSONObject("rank");
            JSONArray ja = jo.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                int price = jo.getInt("price");
                if (price > coinBalance) {
                    continue;
                }
                String originBossId = jo.getString("originBossId");
                boolean isTradeMember = clubTradeMemberList.contains(originBossId);
                if (clubTradeMemberType.getValue() != TradeMemberType.TRADE) {
                    isTradeMember = !isTradeMember;
                }
                if (!isTradeMember) {
                    continue;
                }
                return queryClubMember(jo);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryMemberPriceRanking err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    private JSONObject queryClubMember(JSONObject member) {
        try {
            String memberId = member.getString("memberId");
            String originBossId = member.getString("originBossId");
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryClubMember(memberId, originBossId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                JSONObject priceInfo = jo.getJSONObject("member").getJSONObject("priceInfo");
                member.put("priceInfo", priceInfo);
                return member;
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryClubMember err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    private Boolean buyMember(String roomId, JSONObject member) {
        if (member == null) {
            return false;
        }
        try {
            String currentBossId = member.getString("currentBossId");
            String memberId = member.getString("memberId");
            String originBossId = member.getString("originBossId");
            JSONObject priceInfo = member.getJSONObject("priceInfo");
            JSONObject jo = new JSONObject(AntSportsRpcCall.buyMember(currentBossId, memberId, originBossId, priceInfo, roomId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                String userName = UserIdMap.getMaskName(originBossId);
                int price = member.getInt("price");
                Log.other("好友大战🥋抢购好友[" + userName + "]#消耗[" + price + "运动币]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "buyMember err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void coinExchangeItem(String itemId) {
        try {
            JSONObject jo = new JSONObject(AntSportsRpcCall.queryItemDetail(itemId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            if (!"OK".equals(jo.optString("exchangeBtnStatus"))) {
                return;
            }
            jo = jo.getJSONObject("itemBaseInfo");
            String itemTitle = jo.getString("itemTitle");
            int valueCoinCount = jo.getInt("valueCoinCount");
            jo = new JSONObject(AntSportsRpcCall.exchangeItem(itemId, valueCoinCount));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            if (jo.optBoolean("exgSuccess")) {
                Log.other("运动好礼🎐兑换权益[" + itemTitle + "]#消耗[" + valueCoinCount + "运动币]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "trainMember err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public enum PathCompleteStatus {
        NOT_JOIN, JOIN, NOT_COMPLETED, COMPLETED, INTERRUPT;
    }

    public enum TaskStatus {
        WAIT_COMPLETE, WAIT_RECEIVE, HAS_RECEIVED;
    }

    private enum AntSportsFlag implements Status.StatusFlag {
        SYNC_STEP,
        DONATE_WALK,
        DONATE_CHARITY_COIN,
        TREASURE_BOX_LIMIT
    }

    public enum SportsHealthCoinCenterOption implements CustomOption {
        RECEIVE_COIN_ASSET("运动币领取"),
        QUERY_COIN_TASK_PANEL("运动币任务"),
        COIN_EXCHANGE_DOUBLE_CARD("运动币兑换限时能量双击卡");

        private final String nickName;

        SportsHealthCoinCenterOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    public interface WalkPathTheme {
        int DA_MEI_ZHONG_GUO = 0;
        int GONG_YI_YI_XIAO_BU = 1;
        int DENG_DING_ZHI_MA_SHAN = 2;
        int WEI_C_DA_TIAO_ZHAN = 3;
        int LONG_NIAN_QI_FU = 4;
        int SHOU_HU_TI_YU_MENG = 5;

        String[] nickNames = {"大美中国", "公益一小步", "登顶芝麻山", "维C大挑战", "龙年祈福", "守护体育梦"};
        String[] walkPathThemeIds = {"M202308082226", "M202401042147", "V202405271625", "202404221422", "WF202312050200", "V202409061650"};
    }

    public interface DonateCharityCoinType {

        int ZERO = 0;
        int ONE = 1;
        int ALL = 2;

        String[] nickNames = {"不捐赠", "捐赠一个项目", "捐赠所有项目"};

    }

    public interface TradeMemberType {

        int NONE = 0;
        int TRADE = 1;
        int NOT_TRADE = 2;

        String[] nickNames = {"不抢购", "抢购已选好友", "抢购未选好友"};

    }

    public interface TrainItemType {

        int NONE = 0;
        int BALLET = 1;
        int SANDBAG = 2;
        int BARBELL = 3;
        int YANGKO = 4;
        int SKATE = 5;
        int MUD = 6;

        String[] nickNames = {"不训练", "跳芭蕾", "打沙包", "举杠铃", "扭秧歌", "玩滑板", "踩泥坑"};
        String[] itemTypes = {"", "ballet", "sandbag", "barbell", "yangko", "skate", "mud"};

    }

    private void querySign() {
        if (Status.hasFlagToday(NeverLandFlag.SIGN.flagName())) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.querySign());
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                int signCount = jo.getInt("signCount");//签到天数
                JSONArray days = jo.getJSONArray("days");
                for (int i = 0; i < days.length(); i++) {
                    jo = days.getJSONObject(i);
                    if (jo.optBoolean("current")) {
                        if (jo.optBoolean("signIn") || takeSign(signCount)) {
                            Status.flagToday(NeverLandFlag.SIGN.flagName());
                        }
                        break;
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "querySign err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private boolean takeSign(int signCount) {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.takeSign());
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                List<String> list = parseUserItemsToAwardList(jo.getJSONArray("userItems"));
                Log.other("悦动健康🗺️累计签到[第" + (signCount + 1) + "天]#获得" + list);
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "takeSign err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    /**
     * 浏览商品15s得健康能量
     */
    private void queryTaskInfo() {
        try {
            boolean doubleCheck;
            do {
                doubleCheck = false;
                JSONObject jo = new JSONObject(NeverLandRpcCall.queryTaskInfo());
                if (MessageUtil.checkResponse(TAG, jo)) {
                    jo = jo.getJSONObject("data");
                    if (jo.has("taskInfos")) {
                        JSONArray taskInfos = jo.getJSONArray("taskInfos");
                        for (int i = 0; i < taskInfos.length(); i++) {
                            jo = taskInfos.getJSONObject(i);
                            int viewSec = jo.getInt("viewSec");
                            TimeUtil.sleep(TimeUnit.SECONDS.toMillis(viewSec));
                            if (energyReceive(jo)) {
                                doubleCheck = true;
                            }
                        }
                    }
                }
            } while (doubleCheck);
        } catch (Throwable t) {
            Log.i(TAG, "queryTaskInfo err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private boolean energyReceive(JSONObject taskInfo) {
        if (!taskInfo.has("encryptValue") || !taskInfo.has("energyNum")) {
            // 没有加密值和奖励数量，任务无法完成
            return false;
        }
        try {
            taskInfo.put("type", "LIGHT_FEEDS_TASK");
            JSONObject jo = new JSONObject(NeverLandRpcCall.energyReceive(taskInfo));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                int energyNum = 0;
                JSONArray prizes = jo.getJSONArray("prizes");
                for (int i = 0; i < prizes.length(); i++) {
                    energyNum += prizes.getJSONObject(i).getInt("prizeCount");
                }
                String title = taskInfo.optString("title", "浏览商品15s得健康能量");
                Log.other("悦动健康🗺️完成任务[" + title + "]#获得[" + energyNum + "健康能量]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "energyReceive err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void queryTaskCenter() {
        try {
            boolean doubleCheck;
            do {
                doubleCheck = false;
                JSONObject jo = new JSONObject(NeverLandRpcCall.queryTaskCenter());
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return;
                }
                jo = jo.getJSONObject("data");
                JSONArray taskCenterTaskVOS = jo.getJSONArray("taskCenterTaskVOS");
                for (int i = 0; i < taskCenterTaskVOS.length(); i++) {
                    jo = taskCenterTaskVOS.getJSONObject(i);
                    if (Objects.equals("SIGNUP_COMPLETE", jo.getString("taskStatus"))) {
                        switch (jo.getString("taskType")) {
                            case "LIGHT_TASK" -> {
                                JSONObject logExtMap = jo.getJSONObject("logExtMap");
                                String taskType = logExtMap.getString("taskType");
                                String bizId = logExtMap.getString("bizId");
                                if (Objects.equals("50000", taskType)
                                        || Objects.equals("90000", taskType)
                                        || Objects.equals("440000", taskType)) {
                                    return;
                                }
                                if (serviceTaskFinish(bizId)) {
                                    doubleCheck = true;
                                }
                            }
                            case "PROMOKERNEL_TASK" -> {
                                if (taskSend(jo)) {
                                    jo.put("taskStatus", "TO_RECEIVE");
                                    TimeUtil.sleep(1000L);
                                }
                            }
                        }
                    }
                    if (Objects.equals("TO_RECEIVE", jo.getString("taskStatus"))) {
                        if (taskReceive(jo)) {
                            doubleCheck = true;
                        }
                    }
                }
            } while (doubleCheck);
        } catch (Throwable t) {
            Log.i(TAG, "takeSign err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public static Boolean serviceTaskFinish(String bizId) {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.serviceTaskFinish(bizId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("extendInfo");
                String taskTitle = jo.getJSONObject("taskInfo").getString("taskTitle");
                StringBuilder builder = new StringBuilder();
                if (jo.has("rewardInfo")) {
                    jo = jo.getJSONObject("rewardInfo");
                    if (!Objects.equals("无权益", jo.getString("rewardTypeName"))) {
                        builder.append("#获得[").append(jo.getString("rewardAmount"))
                                .append(jo.getString("rewardTypeName")).append("]");
                    }
                }
                Log.other("广告服务🎖️完成任务[" + taskTitle + "]" + builder);
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "serviceTaskFinish err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private boolean taskSend(JSONObject task) {
        try {
            task.put("scene", "MED_TASK_HALL");
            JSONObject jo = new JSONObject(NeverLandRpcCall.taskSend(task));
            if (MessageUtil.checkResponse(TAG, jo)) {
                String title = task.getString("title");
                Log.other("悦动健康🗺️完成任务[" + title + "]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "taskSend err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private boolean taskReceive(JSONObject task) {
        try {
            task.put("scene", "MED_TASK_HALL").put("source", "jkdprizesign");
            JSONObject jo = new JSONObject(NeverLandRpcCall.taskReceive(task));
            if (MessageUtil.checkResponse(TAG, jo)) {
                String title = task.getString("title");
                jo = jo.getJSONObject("data");
                List<String> list = parseUserItemsToAwardList(jo.getJSONArray("userItems"));
                Log.other("悦动健康🗺️领取奖励[" + title + "]#获得" + list);
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "taskReceive err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    /**
     * 查询健康能量任务
     */
    private void queryBubbleTask() {
        try {
            boolean doubleCheck;
            do {
                doubleCheck = false;
                JSONObject jo = new JSONObject(NeverLandRpcCall.queryBubbleTask());
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return;
                }
                jo = jo.getJSONObject("data");
                JSONArray bubbleTaskVOS = jo.getJSONArray("bubbleTaskVOS");
                for (int i = 0; i < bubbleTaskVOS.length(); i++) {
                    jo = bubbleTaskVOS.getJSONObject(i);
                    if (!jo.has("bubbleTaskStatus")) {
                        continue;
                    }
                    String title = jo.getString("title");
                    BubbleTaskStatus bubbleTaskStatus = BubbleTaskStatus.valueOf(jo.getString("bubbleTaskStatus"));
                    if (bubbleTaskStatus == BubbleTaskStatus.INIT) {
                        if (Objects.equals("AD_BALL", jo.getString("taskId"))) {
                            if (energyReceive(jo.put("lightTaskId", "adBubble"))) {
                                doubleCheck = true;
                            }
                        }
                    }
                    if (bubbleTaskStatus == BubbleTaskStatus.TO_RECEIVE) {
                        String medEnergyBallInfoRecordId = jo.getString("medEnergyBallInfoRecordId");
                        if (pickBubbleTaskEnergy(medEnergyBallInfoRecordId, title)) {
                            TimeUtil.sleep(1000L);
                        }
                    }
                }
            } while (doubleCheck);
        } catch (Throwable t) {
            Log.i(TAG, "queryBubbleTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /**
     * 点击健康能量球
     *
     * @param medEnergyBallInfoRecordId 能量球ID
     * @param title                     任务标题
     * @return 是否领取成功
     */
    private boolean pickBubbleTaskEnergy(String medEnergyBallInfoRecordId, String title) {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.pickBubbleTaskEnergy(medEnergyBallInfoRecordId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                String changeAmount = jo.getString("changeAmount");
                Log.other("悦动健康🗺️领取奖励[" + title + "]#获得[" + changeAmount + "健康能量]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "pickBubbleTaskEnergy err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    /**
     * 双倍领取离线奖励
     */
    private void offlineAward() {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.offlineAward());
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                List<String> list = parseUserItemsToAwardList(jo.getJSONArray("userItems"));
                if (!list.isEmpty()) {
                    Log.other("悦动健康🗺️领取奖励[离线奖励]#获得" + list);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "offlineAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /**
     * 查询用户健康能量
     *
     * @return 健康能量值
     */
    private int queryUserEnergyAsset() {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.queryUserAccount());
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                return Integer.parseInt(jo.getString("balance"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryUserEnergyAsset err:");
            Log.printStackTrace(TAG, t);
        }
        return 0;
    }

    private void queryBaseInfo() {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.queryBaseInfo());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            JSONArray offlineAwards = jo.getJSONArray("offlineAwards");
            if (offlineAwards.length() > 0) {
                offlineAward();
            }
            if (neverLandOptions.contains(NeverLandOption.WALK_GRID.name())) {
                String branchId = jo.getString("branchId");
                String mapId = jo.getString("mapId");
                String mapName = jo.getString("mapName");
                if (queryMapInfo(branchId, mapId) && queryUserEnergyAsset() >= energyKeepCount.getValue()) {
                    while (walkGrid(branchId, mapId, mapName)) {
                        TimeUtil.sleep(1000L);
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryBaseInfo err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /**
     * 查询地图信息
     *
     * @param branchId 分支ID
     * @param mapId    地图ID
     * @return 地图是否可以能量泵
     */
    private boolean queryMapInfo(String branchId, String mapId) {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.queryMapInfo(branchId, mapId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                JSONObject starData=jo.getJSONObject("starData");
                int count=starData.optInt("count");
                int curr=starData.optInt("curr");
                int rewardLevel=starData.optInt("rewardLevel");
                int currLevel=0;
                if("MM11".equals(mapId)){
                    currLevel=Math.floorDiv(curr,20)+1;
                }else{
                    currLevel=Math.floorDiv(curr,50)+1;
                }
                if(currLevel>rewardLevel){
                    for(int i=rewardLevel;i<currLevel;i++){
                        mapStageReward(mapId,i);
                    }
                }
                if(curr==count){
                    Log.other("悦动健康🗺️[该地图已完成]");
                    if(!Status.canGetRedPocketToday(redPocketCountLimit.getValue())){
                        return false;
                    }
                }
                return jo.getBoolean("canWalk");
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryMapInfo err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void mapStageReward(String mapId,int level) {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.mapStageReward(mapId,level));
            if (MessageUtil.checkResponse(TAG, jo)) {
                int modifyCount = jo.getJSONObject("data").getJSONObject("receiveResult").getInt("modifyCount");
                Log.other("悦动健康🗺️领取[" + modifyCount + "]#红包碎片");
            }
        } catch (Throwable t) {
            Log.i(TAG, "mapStageReward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /**
     * 能量泵
     *
     * @param branchId 分支ID
     * @param mapId    地图ID
     * @param mapName  地图名
     * @return 可以继续使用能量泵
     */
    private boolean walkGrid(String branchId, String mapId, String mapName) {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.walkGrid(branchId, mapId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                int step = jo.getJSONArray("mapAwards").getJSONObject(0).getInt("step");
                int leftCount = jo.getInt("leftCount");
                Log.other("悦动健康🗺️能量泵[" + mapName + "]#前进[" + step + "步]");
                List<String> list = parseUserItemsToAwardList(jo.getJSONArray("userItems"));
                if (!list.isEmpty()) {
                    Log.other("悦动健康🗺️能量泵[" + mapName + "]#获得" + list);
                    Status.getRedPocketToday();
                }
                if(jo.has("upgradeBuildingInfo")){
                    if(jo.getJSONObject("upgradeBuildingInfo").optBoolean("startResult")){
                        Log.other("悦动健康🗺️能量泵[" + mapName + "]#获得1颗星星");
                    }
                }
                int curr = jo.getJSONObject("starData").getInt("curr");
                int count = jo.getJSONObject("starData").getInt("count");
                if(curr==count){
                    if(!Status.canGetRedPocketToday(redPocketCountLimit.getValue())){
                        return false;
                    }
                }
                return leftCount >= energyKeepCount.getValue();//&& curr < count;
            }
        } catch (Throwable t) {
            Log.i(TAG, "walkGrid err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    /**
     * 解析 userItems 为 awardList
     */
    private List<String> parseUserItemsToAwardList(JSONArray userItems) {
        List<String> list = new ArrayList<>();
        try {
            for (int i = 0; i < userItems.length(); i++) {
                JSONObject jo = userItems.getJSONObject(i);
                int modifyCount = jo.optInt("modifyCount");
                if (modifyCount > 0) {
                    list.add(modifyCount + jo.getString("name"));
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "parseUserItemToAwardInfo err:");
            Log.printStackTrace(TAG, t);
        }
        return list;
    }

    /**
     * 查询权益商品列表
     */
    private void queryItemList() {
        try {
            boolean hasMore;
            int pageNum = 1;
            int point = queryUserEnergyAsset();
            do {
                JSONObject jo = new JSONObject(NeverLandRpcCall.queryItemList("FEEDS_VIRTUAL_EQUITY", pageNum++));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return;
                }
                jo = jo.getJSONObject("data");
                hasMore = jo.optBoolean("hasMore");
                JSONArray itemVOList = jo.getJSONArray("itemVOList");
                for (int i = 0; i < itemVOList.length(); i++) {
                    jo = itemVOList.getJSONObject(i);
                    String materialType = jo.getString("materialType");
                    // materialType: lightAd FixedItem benefitItem
                    if (!Objects.equals("benefitItem", materialType)) {
                        continue;
                    }
                    String benefitId = jo.getString("benefitId");
                    String itemId = jo.getString("itemId");
                    String itemName = jo.getString("itemName");
                    NeverLandBenefitIdMap.getInstance().add(itemId, itemName);
                    if (jo.getInt("remainCount") < 1 || !neverLandBenefitList.contains(itemId)) {
                        // 剩余数量少于1 或 未开启兑换
                        continue;
                    }
                    int salePoint = Integer.parseInt(jo.getString("salePoint"));
                    if (point < salePoint) {
                        // 健康能量不足
                        continue;
                    }
                    ItemStatus status = ItemStatus.valueOf(jo.getString("status"));
                    if (status == ItemStatus.ITEM_SALE) {
                        if (createOrder(benefitId, itemId)) {
                            Log.other("悦动健康🗺️兑换权益[" + itemName + "]#消耗[" + salePoint + "健康能量]");
                            point -= salePoint;
                        }
                    }
                }
            } while (hasMore && pageNum < 2);
            NeverLandBenefitIdMap.getInstance().save();
        } catch (Throwable t) {
            Log.i(TAG, "queryItemList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private boolean createOrder(String benefitId, String itemId) {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.createOrder(benefitId, itemId));
            return MessageUtil.checkResponse(TAG, jo);
        } catch (Throwable t) {
            Log.i(TAG, "createOrder err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    /*运动健康任务*/

    private boolean sportsHealthCompleteTask(String taskAction,String taskId,String taskName) {
        try {
                JSONObject jo = new JSONObject(NeverLandRpcCall.completeTask(taskAction, taskId));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    jo = jo.getJSONObject("data");
                    String assetId = jo.optString("assetId");
                    if (assetId.isEmpty()) {
                        return false;
                    }
                    if (pickBubbleTaskEnergy(assetId, taskName)) {
                        TimeUtil.sleep(1000L);
                        return true;
                    }
                }

        } catch (Throwable t) {
            Log.i(TAG, "sportsHealthCompleteTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    /**
     * 查询运动健康能量任务
     */
    private void queryCoinTaskPanel() {
        try {
            boolean doubleCheck;
            do {
                doubleCheck = false;
                JSONObject jo = new JSONObject(NeverLandRpcCall.queryCoinTaskPanel());
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return;
                }
                jo = jo.getJSONObject("data");
                JSONArray taskList = jo.getJSONArray("taskList");
                for (int i = 0; i < taskList.length(); i++) {
                    jo = taskList.getJSONObject(i);
                    if (!jo.has("taskStatus")) {
                        continue;
                    }
                    String taskName = jo.getString("taskName");
                    String taskStatus=jo.getString("taskStatus");
                    if (Objects.equals("WAIT_COMPLETE", taskStatus)) {
                        String taskType=jo.getString("taskType");
                        if(Objects.equals("TRANSFORMER", taskType)||Objects.equals("BROWSER", taskType)){
                            boolean needSignUp=jo.optBoolean("needSignUp");
                            if(needSignUp){
                                continue;
                            }
                            int currentNum = jo.optInt("currentNum");
                            int limitConfigNum = jo.optInt("limitConfigNum");
                            int count = limitConfigNum > currentNum ? limitConfigNum - currentNum : 0;
                            if(count>0){
                                String taskAction=jo.getString("taskAction");
                                String taskId=jo.getString("taskId");
                                if(sportsHealthCompleteTask(taskAction,taskId,taskName)){
                                    doubleCheck=true;
                                };
                            }
                        }
                    }else if (Objects.equals("WAIT_RECEIVE", taskStatus)) {
                        String assetId = jo.optString("assetId");
                        if (assetId.isEmpty()) {
                            continue;
                        }
                        if (pickBubbleTaskEnergy(assetId, taskName)) {
                            TimeUtil.sleep(1000L);
                        }
                    }
                }
            } while (doubleCheck);
        } catch (Throwable t) {
            Log.i(TAG, "queryCoinTaskPanel err:");
            Log.printStackTrace(TAG, t);
        }
    }
    /**
     * 运动健康兑换秒杀任务
     */
    private void queryFlashSaleItemList() {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.queryFlashSaleItemList());
            if (!MessageUtil.checkResponse(TAG, jo) || !jo.has("data")) {
                return;
            }
            JSONObject data = jo.getJSONObject("data");
            if (!data.has("itemVOList")) {
                return;
            }
            JSONArray itemVOList = data.getJSONArray("itemVOList");
            for (int i = 0; i < itemVOList.length(); i++) {
                jo = itemVOList.getJSONObject(i);
                String flashSaleType=jo.optString("flashSaleType");
                if (!"LIMIT_TIMERANGE".equals(flashSaleType)) {
                    continue;
                }
                String benefitId = jo.getString("benefitId");
                String itemName = jo.getString("itemName");
                String displayPrice=jo.getString("displayPrice");
                String displayName=itemName+"("+displayPrice+")";
                FlashSaleIdMap.getInstance().add(benefitId, displayName);
            }
            FlashSaleIdMap.getInstance().save();
            for (int j = 0; j < itemVOList.length(); j++) {
                jo = itemVOList.getJSONObject(j);
                String flashSaleType=jo.optString("flashSaleType");
                if (!"LIMIT_TIMERANGE".equals(flashSaleType)) {
                    continue;
                }
                String benefitId = jo.getString("benefitId");
                if (!flashSaleSeckillList.contains(benefitId)) {
                    continue;
                }
                String itemName = jo.getString("itemName");
                String itemId = jo.getString("itemId");
                String status = jo.getString("status");
                if ("ITEM_SALE_OUT".equals(status)) {
                    long pageFreshTime = data.getLong("pageFreshTime");
                    String taskId = "SK|" + benefitId;
                    if(System.currentTimeMillis()<pageFreshTime){
                    if (addChildTask(new ChildModelTask(taskId, "SK", () -> {
                        long secKillEndTime = TimeUnit.MINUTES.toMillis(1) + System.currentTimeMillis();
                        while (!flashSaleSeckill(itemId, benefitId)) {
                            // 尝试抢购到秒杀时间后1分钟
                            if (secKillEndTime < System.currentTimeMillis()) {
                                break;
                            }
                        }
                        boolean isKilled = Status.hasFlagToday(AntForestAlpha.AntForestAlphaFlag.SEC_KILL.flagName(benefitId));
                        Log.other("蹲点秒杀⚡[" + itemName + "]" + (isKilled ? "成功🎉" : "失败💔"));
                    }, pageFreshTime))) {
                        Log.record("添加蹲点秒杀⏰[" + itemName + "]在[" + TimeUtil.getCommonDateTime(pageFreshTime) + "]执行");
                    }
                    }
                }
            }

        } catch (Throwable t) {
            Log.i(TAG, "queryFlashSaleItemList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static Boolean flashSaleSeckill(String itemCode, String roundInstanceId) {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.createOrder(roundInstanceId,itemCode));
            if(jo.has("errorCode")){
                String errorCode=jo.getString("errorCode");
                if("POINT_NOT_ENOUGH".equals(errorCode)||"ITEM_PURCHASE_ERROR".equals(errorCode)){
                    // 积分不足
                    return true;
                }
            }
            if (MessageUtil.checkResponse(TAG, jo)) {
                Status.flagToday(AntForestAlpha.AntForestAlphaFlag.SEC_KILL.flagName(roundInstanceId));
                return true;
            }
        } catch (Throwable th) {
            Log.i(TAG, "flashSaleSeckill err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    private void queryEnergyBubbleModule() {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.queryEnergyBubbleModule());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            if(!jo.has("receiveBubbleList")){
                return;
            }
            JSONArray receiveBubbleList = jo.getJSONArray("receiveBubbleList");
            if (receiveBubbleList.length() > 0) {
                pickAllBubbleTaskEnergy();
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryEnergyBubbleModule err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void pickAllBubbleTaskEnergy() {
        try {
            JSONObject jo = new JSONObject(NeverLandRpcCall.pickAllBubbleTaskEnergy());
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                String changeAmount = jo.getString("changeAmount");
                Log.other("悦动健康🗺️领取奖励[首页能量球]#获得[" + changeAmount + "健康能量]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "pickAllBubbleTaskEnergy err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private enum BubbleTaskStatus {
        NO_AUTH,
        INIT,
        TO_RECEIVE,
        RECEIVE_SUCCESS
    }

    private enum ItemStatus {
        ITEM_SALE("立即兑换"),
        ITEM_SALE_OUT("已抢光");

        ItemStatus(String ignoreDesc) {
        }
    }

    private enum NeverLandOption implements CustomOption {
        WALK_GRID("能量泵"),
        QUERY_ITEM_LIST("健康能量兑好礼"),
        QUERY_COIN_TASK_PANEL("运动健康任务");

        private final String nickName;

        NeverLandOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    private enum NeverLandFlag implements Status.StatusFlag {
        SIGN;
    }
}