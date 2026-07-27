package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.github.lazyimmortal.sesame.model.task.antFarm.AntFarm.TaskStatus;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;

public class AntFarmAlpha {
    private static final String TAG = AntFarmAlpha.class.getSimpleName();

    public static Boolean doFarmTask(JSONObject task) {
        try {
            if (task.has("taskType")) {
                return false;
            }
            /* 禁用美食任务
            String awardType = task.getString("awardType");
            if (Objects.equals(awardType, "CUISINE")) {
                return false;
            }
             */
            String bizKey = task.getString("bizKey");
            JSONObject jo = new JSONObject(AntFarmAlphaRpcCall.doFarmTask(bizKey));
            return MessageUtil.checkResponse(TAG, jo);
        } catch (Throwable t) {
            Log.i(TAG, "doFarmTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    public static Boolean doFarmDrawTimesTask(JSONObject task) {
        boolean isDoTask = false;
        try {
            /* 禁用捐赠任务
            String innerAction = task.optString("innerAction");
            if (Objects.equals(innerAction, "DONATION")) {
                return false;
            }
             */
            String title = task.getString("title");
            String bizKey = task.getString("bizKey");
            if("20250728_chouchoulechoukuan2".equals(bizKey)){
                return true;
            }
            int rightsTimes = task.optInt("rightsTimes", 0);
            int rightsTimesLimit = task.optInt("rightsTimesLimit", 0);
            int times = rightsTimesLimit - rightsTimes;
            for (int i = 0; i < times; i++) {
                JSONObject jo = new JSONObject(AntFarmAlphaRpcCall.doFarmDrawTimesTask(bizKey));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    break;
                }
                isDoTask = true;
                // int awardCount = jo.getInt("awardCount");
                Log.farm("装扮抽奖🎟️️完成任务[" + title + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "doFarmDrawTimesTask err:");
            Log.printStackTrace(TAG, t);
        }
        return isDoTask;
    }

    // 装扮抽抽乐 限定IP
    public static void listFarmIpDrawTask() {
        try {
            JSONObject jo = new JSONObject(AntFarmAlphaRpcCall.listFarmIpDrawTask());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray ja = jo.getJSONArray("farmTaskList");
            for (int i = 0;i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                if (Objects.equals(TaskStatus.RECEIVED.name(), jo.getString("taskStatus"))) {
                    continue;
                }
                int rightsTimes = jo.getInt("rightsTimes");
                int rightsTimesLimit = jo.getInt("rightsTimesLimit");
                String title = jo.getString("title");
                String bizKey = jo.getString("bizKey");
                if (!doFarmIpDrawTask(bizKey, title, rightsTimesLimit - rightsTimes)) {
                    continue;
                }
                receiveFarmIpDrawTaskAward(bizKey, title);
            }
        } catch (Throwable t) {
            Log.i(TAG, "listFarmIpDrawTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static Boolean doFarmIpDrawTask(String bizKey, String title, int times) {
        try {
            for (int i = 0; i < times; i++) {
                JSONObject jo = new JSONObject(AntFarmAlphaRpcCall.doFarmIpDrawTask(bizKey));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return false;
                }
                int awardCount = jo.getInt("awardCount");
                Log.farm("装扮抽奖🎟️️完成任务[" + title + "]#获得[" + awardCount + "次IP抽奖机会]");
            }
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "doFarmIpDrawTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void receiveFarmIpDrawTaskAward(String taskId, String title) {
        try {
            JSONObject jo = new JSONObject(AntFarmAlphaRpcCall.receiveFarmIpDrawTaskAward(taskId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.farm("装扮抽奖🎟️领取奖励[" + title + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveFarmIpDrawTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public static void listFamilyTask(JSONArray animals) {
        try {
            JSONObject jo = new JSONObject(AntFarmAlphaRpcCall.listFamilyTask());
            if (MessageUtil.checkResponse(TAG, jo)) {
                doFarmFamilyHideTask(doFarmFamilyTask(jo.getJSONArray("familyTasks")));
            }
            jo = new JSONObject(AntFarmAlphaRpcCall.familyTaskTips(animals));
            if (MessageUtil.checkResponse(TAG, jo) && jo.has("familyTaskTips")) {
                doFarmFamilyTask(jo.getJSONArray("familyTaskTips"));
            }
        } catch (Throwable t) {
            Log.i(TAG, "listFamilyTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void doFarmFamilyHideTask(Boolean isFinishedHideTask) {
        if (!Objects.equals(null, isFinishedHideTask)) {
            // 临时屏蔽隐藏任务 解除则把 null 改为 Boolean.FALSE
            return;
        }
        try {
            for (FamilyTask familyTask : FamilyTask.values()) {
                if (!doFarmFamilyTask(familyTask.name(), familyTask.title, familyTask.rightsTimesLimit)) {
                    continue;
                }
                receiveFarmFamilyTaskAward(familyTask.name(), familyTask.title);
            }
        } catch (Throwable t) {
            Log.i(TAG, "doFarmFamilyTask(isFinishedHideTask) err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static Boolean doFarmFamilyTask(JSONArray farmTaskList) {
        boolean finishedHideTask = false;
        try {
            for (int i = 0; i < farmTaskList.length(); i++) {
                JSONObject jo = farmTaskList.getJSONObject(i);
                int rightsTimes = jo.getInt("rightsTimes");
                int rightsTimesLimit = jo.getInt("rightsTimesLimit");
                String bizKey = jo.getString("bizKey");
                String taskId = jo.getString("taskId");
                String taskStatus = jo.getString("taskStatus");
                String title = jo.getString("title");
                if (Objects.equals(FamilyTask.INVITE_SPECIFIC_USER.name(), bizKey)) {
                    finishedHideTask = true;
                }
                if (TaskStatus.RECEIVED.name().equals(taskStatus)) {
                    continue;
                }
                if (TaskStatus.TODO.name().equals(taskStatus)
                        && !doFarmFamilyTask(bizKey, title, rightsTimesLimit - rightsTimes)) {
                    continue;
                }
                // FINISHED
                receiveFarmFamilyTaskAward(taskId, title);
            }
        } catch (Throwable t) {
            Log.i(TAG, "doFarmFamilyTask(farmTaskList) err:");
            Log.printStackTrace(TAG, t);
        }
        return finishedHideTask;
    }

    private static Boolean doFarmFamilyTask(String bizKey, String title, int times) {
        if (!FamilyTask.canAutoFinish(bizKey)) {
            return false;
        }
        try {
            for (int i = 0; i < times; i++) {
                JSONObject jo = new JSONObject(AntFarmAlphaRpcCall.doFarmFamilyTask(bizKey));
                if (MessageUtil.checkResponse(TAG, jo) && jo.has("awardCount")) {
                    Log.farm("亲密家庭🏠完成任务[" + title + "]#获得[" + jo.getInt("awardCount") + "亲密度]");
                    TimeUtil.sleep(1000L);
                } else return false;
            }
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "doFarmFamilyTask(bizKey, title, times) err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void receiveFarmFamilyTaskAward(String taskId, String title) {
        try {
            JSONObject jo = new JSONObject(AntFarmAlphaRpcCall.receiveFarmFamilyTaskAward(taskId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.farm("亲密家庭🏠提交任务[" + title + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveFarmFamilyTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private enum FamilyTask {
        GREETING("道早安", 1, true),
        SLEEP_TOGETHER("去睡觉", 2, true),
        INVITE_ELDER("邀请家长入队", 5, false),
        INVITE_SPECIFIC_USER("邀请好友助力", 3, true),
        ASSIGN_FAMILY_MEMBER_TASK("使用顶梁柱特权", 1, false);

        private final String title;
        private final int rightsTimesLimit;
        private final boolean canAutoFinish;
        FamilyTask(String title, int rightsTimesLimit, boolean canAutoFinish) {
            this.title = title;
            this.rightsTimesLimit = rightsTimesLimit;
            this.canAutoFinish = canAutoFinish;
        }

        private static final Map<String, FamilyTask> MAP;
        static {
            MAP = new HashMap<>();
            for (FamilyTask familyTask : FamilyTask.values()) {
                MAP.put(familyTask.name(), familyTask);
            }
        }

        public static Boolean canAutoFinish(String bizKey) {
            FamilyTask familyTask = MAP.get(bizKey);
            return familyTask == null || familyTask.canAutoFinish;
        }
    }
}
