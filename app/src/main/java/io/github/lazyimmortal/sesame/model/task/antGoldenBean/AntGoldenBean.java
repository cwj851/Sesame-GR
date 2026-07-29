package io.github.lazyimmortal.sesame.model.task.antGoldenBean;

import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.BooleanModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.AlipayAntGoldenBeanTaskList;
import io.github.lazyimmortal.sesame.model.task.antGame.GameTask;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.AntGoldenBeanTaskListMap;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;

public class AntGoldenBean extends ModelTask {
    private static final String TAG = "AntGoldenBean";
    private static final String NAME = "金豆夺宝";
    private static final ModelGroup GROUP = ModelGroup.ORCHARD;

    private BooleanModelField goldenBeanTask;
    private SelectModelField AntGoldenBeanTaskList;
    private BooleanModelField goldenBeanExchange;
    private BooleanModelField goldenBeanGame;
    private BooleanModelField goldenBeanSmashedEgg;

    private String userId;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ModelGroup getGroup() {
        return GROUP;
    }

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(goldenBeanTask = new BooleanModelField("goldenBeanTask", "金豆任务", true));
        modelFields.addField(AntGoldenBeanTaskList = new SelectModelField("AntGoldenBeanTaskList", "金豆任务 | 黑名单列表", new LinkedHashSet<>(), AlipayAntGoldenBeanTaskList::getList));
        modelFields.addField(goldenBeanExchange = new BooleanModelField("goldenBeanExchange", "肥料兑换金豆", true));
        modelFields.addField(goldenBeanGame = new BooleanModelField("goldenBeanGame", "金豆乐园游戏", true));
        modelFields.addField(goldenBeanSmashedEgg = new BooleanModelField("goldenBeanSmashedEgg", "砸金蛋开宝箱", false));
        return modelFields;
    }

    @Override
    public Boolean check() {
        return true;
    }

    @Override
    public void run() {
        try {
            userId = UserIdMap.getCurrentUid();
            if (!checkOpen()) {
                return;
            }

            if (goldenBeanTask.getValue()) {
                if (!Status.hasFlagToday("BlackList::initAntGoldenBean")) {
                    AntGoldenBeanTaskListMap.load();
                    Status.flagToday("BlackList::initAntGoldenBean");
                }
                doSignIn();
                doTasks();
            }

            if (goldenBeanExchange.getValue()) {
                doManureExchange();
            }

            if (goldenBeanGame.getValue()) {
                doGameCenter();
            }

            if (goldenBeanSmashedEgg.getValue()) {
                doSmashedGoldenEgg();
            }

        } catch (Throwable t) {
            Log.i(TAG, "run err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private boolean checkOpen() {
        try {
            JSONObject jo = new JSONObject(AntGoldenBeanRpcCall.goldenBeanIndex());
            return MessageUtil.checkResultCode(TAG, jo);
        } catch (Throwable t) {
            Log.i(TAG, "checkOpen err:");
            Log.printStackTrace(TAG, t);
            return false;
        }
    }

    private void doSignIn() {
        if (Status.hasFlagToday("goldenBeanSign")) {
            return;
        }
        try {
            JSONObject syncJo = new JSONObject(AntGoldenBeanRpcCall.goldenBeanSync("SIGN"));
            if (!MessageUtil.checkResultCode(TAG, syncJo)) {
                return;
            }
            JSONObject signInfo = syncJo.optJSONObject("signInfo");
            if (signInfo == null) {
                return;
            }
            if (signInfo.optBoolean("todaySigned")) {
                Log.record("金豆夺宝今日已签到");
                Status.flagToday("goldenBeanSign", userId);
                return;
            }

            String todayKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            JSONObject signJo = new JSONObject(AntGoldenBeanRpcCall.goldenBeanSign(todayKey));
            if (MessageUtil.checkResultCode(TAG, signJo)) {
                JSONObject resultSign = signJo.optJSONObject("signInfo");
                if (resultSign != null) {
                    int days = resultSign.optInt("currentContinuousCount", 0);
                    int award = resultSign.optInt("awardCount", 0);
                    Log.record("金豆夺宝📅签到[连续" + days + "天]#获得[" + award + "金豆]");
                }
                Status.flagToday("goldenBeanSign", userId);
            }
        } catch (Throwable t) {
            Log.i(TAG, "doSignIn err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void doTasks() {
        try {
            JSONObject syncJo = new JSONObject(AntGoldenBeanRpcCall.goldenBeanSync("TASK_LIST"));
            if (!MessageUtil.checkResultCode(TAG, syncJo)) {
                return;
            }

            JSONArray taskList = syncJo.optJSONArray("taskList");
            if (taskList == null || taskList.length() == 0) {
                return;
            }

            for (int i = 0; i < taskList.length(); i++) {
                JSONObject task = taskList.getJSONObject(i);
                String actionType = task.optString("actionType", "");
                String taskStatus = task.optString("taskStatus", "");
                String taskId = task.optString("taskId", "");

                JSONObject displayConfig = task.optJSONObject("taskDisplayConfig");
                String title = displayConfig != null ? displayConfig.optString("title", taskId) : taskId;

                if ("VISIT".equals(actionType)) {
                    continue;
                }
                if ("MANURE_EXCHANGE".equals(actionType)) {
                    continue;
                }

                if (AntGoldenBeanTaskList.getValue().contains(title)) {
                    continue;
                }

                if ("TODO".equals(taskStatus)) {
                    String finishResult = AntGoldenBeanRpcCall.finishTaskantorchard(taskId);
                    JSONObject finishJo = new JSONObject(finishResult);
                    MessageUtil.checkResultCodeAndMarkTaskBlackList("AntGoldenBeanTaskList", title, finishJo);
                    if (MessageUtil.checkResultCode(TAG, finishJo)) {
                        Log.record("金豆任务🧾完成[" + title + "]");
                    } else {
                        Log.record("金豆任务完成失败[" + title + "]: " + finishJo.optString("desc"));
                    }
                    TimeUtil.sleep(500);
                }

                if ("FINISHED".equals(taskStatus) || "TODO".equals(taskStatus)) {
                    String awardResult = AntGoldenBeanRpcCall.receiveTaskAwardantorchard(taskId);
                    JSONObject awardJo = new JSONObject(awardResult);
                    MessageUtil.checkResultCodeAndMarkTaskBlackList("AntGoldenBeanTaskList", title, awardJo);
                    if (MessageUtil.checkResultCode(TAG, awardJo)) {
                        int inc = awardJo.optInt("incAwardCount", 0);
                        if (inc > 0) {
                            Log.record("金豆奖励🎖️领取[" + title + "]#获得[" + inc + "金豆]");
                        }
                    }
                    TimeUtil.sleep(500);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "doTasks err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void doManureExchange() {
        if (Status.hasFlagToday("goldenBeanExchange")) {
            return;
        }
        try {
            JSONObject syncJo = new JSONObject(AntGoldenBeanRpcCall.goldenBeanSync("EXCHANGE_MANURE"));
            if (!MessageUtil.checkResultCode(TAG, syncJo)) {
                return;
            }

            JSONObject exchangeInfo = syncJo.optJSONObject("manureExchangeInfo");
            if (exchangeInfo == null) {
                return;
            }

            int remainQuota = exchangeInfo.optInt("remainQuota", 0);
            if (remainQuota <= 0) {
                Log.record("金豆兑换今日额度已用完");
                Status.flagToday("goldenBeanExchange", userId);
                return;
            }

            int minAmount = exchangeInfo.optInt("minExchangeAmount", 800);
            int exchangeAmount = Math.min(minAmount, remainQuota);

            JSONObject exchangeJo = new JSONObject(AntGoldenBeanRpcCall.goldenBeanManureExchange(exchangeAmount));
            if (MessageUtil.checkResultCode(TAG, exchangeJo)) {
                int beanDelta = exchangeJo.optInt("beanDelta", 0);
                int manureCost = exchangeJo.optInt("manureCost", 0);
                Log.record("金豆兑换🌱消耗[" + manureCost + "g肥料]#获得[" + beanDelta + "金豆]");
            }
            Status.flagToday("goldenBeanExchange", userId);
        } catch (Throwable t) {
            Log.i(TAG, "doManureExchange err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void doGameCenter() {
        try {
            JSONObject gameListJo = new JSONObject(AntGoldenBeanRpcCall.queryGameList());
            if (!"SUCCESS".equals(gameListJo.optString("desc")) && !MessageUtil.checkResultCode(TAG, gameListJo)) {
                return;
            }

            JSONObject drawRights = gameListJo.optJSONObject("gameCenterDrawRights");
            if (drawRights == null) {
                return;
            }

            int quotaCanUse = drawRights.optInt("quotaCanUse", 0);
            int quotaLimit = drawRights.optInt("quotaLimit", 0);
            int usedQuota = drawRights.optInt("usedQuota", 0);

            if (usedQuota + quotaCanUse >= quotaLimit) {
                Log.record("金豆乐园今日游戏次数已用完");
                return;
            }

            int playTimes = quotaLimit - usedQuota - quotaCanUse;

            GameTask.Goldenbean_ncscc.report("金豆夺宝", playTimes);
            Log.record("金豆乐园🎮开始玩游戏[目标" + playTimes + "次]");

            TimeUtil.sleep(5000);

            JSONObject afterSync = new JSONObject(AntGoldenBeanRpcCall.goldenBeanSync("TASK_LIST"));
            if (MessageUtil.checkResultCode(TAG, afterSync)) {
                JSONArray taskList = afterSync.optJSONArray("taskList");
                if (taskList != null) {
                    for (int i = 0; i < taskList.length(); i++) {
                        JSONObject task = taskList.getJSONObject(i);
                        if ("FINISHED".equals(task.optString("taskStatus"))) {
                            String taskId = task.optString("taskId");
                            JSONObject displayConfig = task.optJSONObject("taskDisplayConfig");
                            String title = displayConfig != null ? displayConfig.optString("title", taskId) : taskId;
                            if (AntGoldenBeanTaskList.getValue().contains(title)) {
                                continue;
                            }
                            String awardResult = AntGoldenBeanRpcCall.receiveTaskAwardantorchard(taskId);
                            JSONObject awardJo = new JSONObject(awardResult);
                            MessageUtil.checkResultCodeAndMarkTaskBlackList("AntGoldenBeanTaskList", title, awardJo);
                            if (MessageUtil.checkResultCode(TAG, awardJo)) {
                                int inc = awardJo.optInt("incAwardCount", 0);
                                if (inc > 0) {
                                    Log.record("金豆游戏🎮领取[" + title + "]#获得[" + inc + "金豆]");
                                }
                            }
                            TimeUtil.sleep(500);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "doGameCenter err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void doSmashedGoldenEgg() {
        try {
            JSONObject gameListJo = new JSONObject(AntGoldenBeanRpcCall.queryGameList());
            if (!"SUCCESS".equals(gameListJo.optString("desc")) && !MessageUtil.checkResultCode(TAG, gameListJo)) {
                return;
            }

            JSONObject drawRights = gameListJo.optJSONObject("gameCenterDrawRights");
            if (drawRights == null) {
                return;
            }

            int quotaCanUse = drawRights.optInt("quotaCanUse", 0);
            int quotaPerTime = drawRights.optInt("quotaPerTime", 1);

            int drawTimes = quotaCanUse / quotaPerTime;
            if (drawTimes <= 0) {
                Log.record("金豆砸蛋今日无抽奖次数");
                return;
            }

            int totalAward = 0;
            for (int i = 0; i < drawTimes; i++) {
                JSONObject drawJo = new JSONObject(AntGoldenBeanRpcCall.drawGameCenterAward(1));
                if (!"SUCCESS".equals(drawJo.optString("resultCode")) && !MessageUtil.checkResultCode(TAG, drawJo)) {
                    Log.record("金豆砸蛋失败: " + drawJo.optString("desc"));
                    break;
                }

                JSONArray awardList = drawJo.optJSONArray("gameCenterDrawAwardList");
                if (awardList != null && awardList.length() > 0) {
                    JSONObject award = awardList.getJSONObject(0);
                    int awardCount = award.optInt("awardCount", 0);
                    totalAward += awardCount;
                    Log.record("金豆砸蛋💥获得[" + awardCount + "金豆]");
                }

                JSONObject rights = drawJo.optJSONObject("gameCenterDrawRights");
                if (rights != null && rights.optInt("quotaCanUse", 0) <= 0) {
                    break;
                }

                TimeUtil.sleep(500);
            }

            if (totalAward > 0) {
                Log.record("金豆砸蛋🎉共获得[" + totalAward + "金豆]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "doSmashedGoldenEgg err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
