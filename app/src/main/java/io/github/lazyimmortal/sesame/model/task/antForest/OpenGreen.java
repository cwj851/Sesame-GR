package io.github.lazyimmortal.sesame.model.task.antForest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.model.task.antFarm.AntFarm.TaskStatus;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Statistics;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class OpenGreen {
    private static final String TAG = OpenGreen.class.getSimpleName();

    private static SelectModelField openGreenInviteP2PList;

    public static void run() {
        openGreenInviteP2PList= ModelTask.getModel(AntForestV2.class).inviteP2PList;
       // listTask();
        enterDrawActivity();
    }

    private static void listTask(String activityId,String sceneCode) {
        try {
            JSONObject jo = new JSONObject(OpenGreenRpcCall.listTask(sceneCode));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray taskInfoList = jo.getJSONArray("taskInfoList");
            for (int i = 0; i < taskInfoList.length(); i++) {
                jo = taskInfoList.getJSONObject(i);
                JSONObject taskBaseInfo = jo.getJSONObject("taskBaseInfo");
                TaskStatus taskStatus = TaskStatus.valueOf(taskBaseInfo.getString("taskStatus"));
                String taskType = taskBaseInfo.getString("taskType");
                String title = new JSONObject(taskBaseInfo.getString("bizInfo")).getString("title");
                String taskProdPlayType = taskBaseInfo.getString("taskProdPlayType");
                // taskProdPlayType: EXCHANGE_ASSET XLIGHT_BUSINESS P2P OTHER
                JSONObject taskRights = jo.getJSONObject("taskRights");
                String taskSceneCode=taskBaseInfo.getString("sceneCode");
                int rightsTimes = taskRights.getInt("rightsTimes");
                int rightsTimesLimit = taskRights.getInt("rightsTimesLimit");
                int times = rightsTimesLimit - rightsTimes;
                if (times > 0) {
                    switch (taskProdPlayType) {
                        case "EXCHANGE_ASSET" ->
                               // exchangeTimesFromTask(getActivityId(sceneCode), taskType, title, times);
                                exchangeTimesFromTask(activityId, taskType, title, times);
//                        case "XLIGHT_BUSINESS" -> {
//                            if (finishTask(taskType, title, times)) {
//                                taskStatus = TaskStatus.FINISHED;
//                            }
//                        }
                        case "P2P" -> {
                            String p2pSceneCode = new JSONObject(
                                    taskBaseInfo.getString("prodPlayParam")
                            ).getString("p2pSceneCode");
                            batchInviteP2P(p2pSceneCode);
                        }
                        default -> {
                            if (finishTask(taskType, title, times,taskSceneCode)) {
                                taskStatus = TaskStatus.FINISHED;
                            }
                        }
                    }
                }
                if (taskStatus == TaskStatus.FINISHED) {
                    receiveTaskAward(taskType, title,taskSceneCode);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "listTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void exchangeTimesFromTask(String activityId, String taskType, String title, int times) {
        try {
            for (int i = 0; i < times; i++) {
                JSONObject jo = new JSONObject(OpenGreenRpcCall.exchangeTimesFromTask(activityId, taskType));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.forest("森林寻宝🎰完成任务[" + title + "]#获得[" + jo.getInt("times") + "次抽奖机会]");
                    TimeUtil.sleep(1000L);
                    continue;
                }
                break;
            }
        } catch (Throwable t) {
            Log.i(TAG, "exchangeTimesFromTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static boolean finishTask(String taskType, String title, int times,String sceneCode) {
        boolean result = false;
        try {
            for (int i = 0; i < times; i++) {
                JSONObject jo = new JSONObject(OpenGreenRpcCall.finishTask(taskType,sceneCode));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    result = true;
                    Log.forest("森林寻宝🎰完成任务[" + title + "]");
                    TimeUtil.sleep(1000L);
                    continue;
                }
                break;
            }
        } catch (Throwable t) {
            Log.i(TAG, "finishTask err:");
            Log.printStackTrace(TAG, t);
        }
        return result;
    }

    private static void batchInviteP2P(String sceneCode) {
        try {
            for (String userId : openGreenInviteP2PList.getValue()) {
                if (UserIdMap.get(userId) != null
                        && !Status.hasFlagToday(OpenGreenFlag.INVITE_P2P.flagName(userId))) {
                    // 是好友且未被邀请
                    JSONObject jo = new JSONObject(OpenGreenRpcCall.batchInviteP2P(userId, sceneCode));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Log.forest("森林寻宝🎰邀请助力#邀请好友[" + UserIdMap.getMaskName(userId) + "]");
                    }
                    Status.flagToday(OpenGreenFlag.INVITE_P2P.flagName(userId));
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "batchInviteP2P err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void receiveTaskAward(String taskType, String title,String sceneCode) {
        try {
            JSONObject jo = new JSONObject(OpenGreenRpcCall.receiveTaskAward(taskType,sceneCode));
            if (MessageUtil.checkResponse(TAG, jo)) {
                int incAwardCount = jo.getInt("incAwardCount");
                Log.forest("森林寻宝🎰领取奖励[" + title + "]#获得[" + incAwardCount + "次抽奖机会]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static String getActivityId(String sceneCode) {
        try {
            JSONObject jo = new JSONObject(OpenGreenRpcCall.enterDrawActivity("",sceneCode));
            if (MessageUtil.checkResponse(TAG, jo)) {
                JSONObject drawActivity = jo.getJSONObject("drawActivity");
                return drawActivity.getString("activityId");
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    private static void enterDrawActivity() {
        try {
            JSONObject jo = new JSONObject(OpenGreenRpcCall.enterDrawActivity("","ANTFOREST_NORMAL_DRAW"));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray drawSceneGroups=jo.getJSONArray("drawSceneGroups");
            for (int i = 0; i < drawSceneGroups.length(); i++) {
                jo = drawSceneGroups.getJSONObject(i).getJSONObject("drawActivity");
                String activityId=jo.getString("activityId");
                String sceneCode=jo.getString("sceneCode");
                listTask(activityId,sceneCode);
                enterDrawActivity(activityId,sceneCode);
            }

        } catch (Throwable t) {
            Log.i(TAG, "enterDrawActivity err:");
            Log.printStackTrace(TAG, t);
        }
    }
    private static void enterDrawActivity(String activity,String sceneCode) {
        try {
            JSONObject jo = new JSONObject(OpenGreenRpcCall.enterDrawActivity(activity,sceneCode));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject drawAsset = jo.getJSONObject("drawAsset");
            int blance = drawAsset.getInt("blance");
            if (blance > 0) {
                JSONObject drawActivity = jo.getJSONObject("drawActivity");
                String activityId = drawActivity.getString("activityId");
                draw(activityId, UserIdMap.getCurrentUid(), blance,sceneCode);
            }
        } catch (Throwable t) {
            Log.i(TAG, "enterDrawActivity err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void draw(String activityId, String userId, int times,String sceneCode) {
        try {
            for (int i = 0; i < times; i++) {
                JSONObject jo = new JSONObject(OpenGreenRpcCall.draw(activityId, userId,sceneCode));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    jo = jo.getJSONObject("prizeVO");
                    String prizeName = jo.getString("prizeName");
                    Log.forest("森林寻宝🎰幸运抽奖#获得[" + prizeName + "]");
                    if (Objects.equals("拯救森林过期能量", jo.getJSONObject("extInfo").optString("getPrizePopDesc"))) {
                        String decideAwardCount = jo.getString("decideAwardCount");
                        Statistics.addData(Statistics.DataType.COLLECTED, Integer.parseInt(decideAwardCount));
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "draw err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private enum OpenGreenFlag implements Status.StatusFlag {
        INVITE_P2P;

        public String tag() {
            return "OpenGreen";
        }
        }

}
