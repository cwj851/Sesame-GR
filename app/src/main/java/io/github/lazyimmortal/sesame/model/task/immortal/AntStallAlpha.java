package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.lazyimmortal.sesame.model.task.readingDada.ReadingDada;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;

public class AntStallAlpha {
    private static final String TAG = AntStallAlpha.class.getSimpleName();

    private static final List<String> taskTypeList = new ArrayList<>();

    static {
        // 开启收新村收益提醒
        taskTypeList.add("ANTSTALL_NORMAL_OPEN_NOTICE");
        // 添加首页
        taskTypeList.add("tianjiashouye");
        // 去饿了么果园逛一逛
        taskTypeList.add("ANTSTALL_ELEME_VISIT");
        // 去点淘赚元宝提现
        taskTypeList.add("ANTSTALL_TASK_diantao202311");
        // 去玩解压小游戏
        taskTypeList.add("ANTSTALL_TASK_nongchangleyuan");
    }

    public static Boolean doStallTask(JSONObject task) {
        try {
            JSONObject bizInfo = new JSONObject(task.getString("bizInfo"));
            String title = bizInfo.getString("title");
            String taskType = task.getString("taskType");
            String actionType = bizInfo.getString("actionType");
            if (Objects.equals("VISIT_AUTO_FINISH", actionType)
                    || Objects.equals("APP_CENTER_VISIT", actionType)
                    || Objects.equals("OPEN_CARD", actionType)
                    || Objects.equals("AROUSE_APP", actionType)
                    || taskTypeList.contains(taskType)
            ) {
                JSONObject jo = new JSONObject(AntStallAlphaRpcCall.finishTask(taskType));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.farm("新村任务🧾完成[" + title + "]");
                    return true;
                }
            } else if (Objects.equals("ANTSTALL_XLIGHT_VARIABLE_AWARD", taskType)) {
                // 【木兰市集】逛精选好物
                return doXLightTask(task);
            } else if (Objects.equals("ANTSTALL_NORMAL_DAILY_QA", taskType)) {
                if (ReadingDada.answerQuestion(bizInfo)) {
                    Log.farm("新村任务🧾完成[" + title + "]");
                }
                return false;
            }
        } catch (Throwable t) {
            Log.i(TAG, "doMulanTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    public static Boolean doXLightTask(JSONObject task) {
        try {
            JSONObject bizInfo = new JSONObject(task.getString("bizInfo"));
            String title = bizInfo.getString("title");
            String targetUrl = bizInfo.getString("targetUrl");
            String iepTaskSceneCode = StringUtil.getSubString(targetUrl, "&iepTaskSceneCode=", "&");
            String iepTaskType = StringUtil.getSubString(targetUrl, "&iepTaskType=", "&");
            JSONObject jo = new JSONObject(AntStallAlphaRpcCall.xlightPlugin());
            if (!jo.has("playingResult")) {
                return false;
            }
            jo = jo.getJSONObject("playingResult");
            String playingBizId = jo.getString("playingBizId");
            jo = jo.getJSONObject("eventRewardDetail");
            JSONArray ja = jo.getJSONArray("eventRewardInfoList");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                jo = new JSONObject(AntStallAlphaRpcCall.finish(iepTaskSceneCode, iepTaskType, playingBizId, jo));
                TimeUtil.sleep(15000);
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.farm("新村任务🧾完成[" + title + "]");
                }
            }
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "doXLightTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }
}
