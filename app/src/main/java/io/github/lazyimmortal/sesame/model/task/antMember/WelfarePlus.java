package io.github.lazyimmortal.sesame.model.task.antMember;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.StringUtil;

public class WelfarePlus {
    private static final String TAG = WelfarePlus.class.getSimpleName();

    public static void run() {
        SelectModelField welfarePlusOptions = ModelTask.getModel(AntMember.class).welfarePlusOptions;
        if (welfarePlusOptions.contains(WelfarePlusOption.RECOMMEND_TASK.name())) {
            queryRecommendTask();
        }
        if (welfarePlusOptions.contains(WelfarePlusOption.ORDINARY_TASK.name())) {
            queryOrdinaryTask();
        }
    }

    // 推荐任务
    private static void queryRecommendTask() {
        try {
            JSONObject jo = new JSONObject(WelfarePlusRpcCall.queryRecommendTask());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            // 获取 taskDetailList 数组
            JSONArray taskDetailList = jo.getJSONArray("taskDetailList");
            // 遍历 taskDetailList
            for (int i = 0; i < taskDetailList.length(); i++) {
                jo = taskDetailList.getJSONObject(i);
                String taskProcessStatus = jo.getString("taskProcessStatus");
                if (!Objects.equals(TaskProcessStatus.NOT_DONE.name(), taskProcessStatus)) {
                    continue;
                }
                String appletName = jo.getJSONObject("taskBaseInfo").getString("appletName");
                jo = jo.getJSONObject("taskMaterial");
                String taskCode = jo.getString("taskCode");
                if (Objects.equals("WELFARE_PLUS_ANT_FOREST", taskCode)) {
                    jo = new JSONObject(WelfarePlusRpcCall.queryAntForestTaskList());
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        jo = new JSONObject(WelfarePlusRpcCall.receiveAntForestTaskAward());
                        if (MessageUtil.checkResponse(TAG, jo)) {
                            Log.other("我的快递💌完成任务[" + appletName + "]");
                        }
                    }
                } else if (Objects.equals("WELFARE_PLUS_ANT_OCEAN", taskCode)) {
                    jo = new JSONObject(WelfarePlusRpcCall.queryAntOceanTaskList());
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        jo = new JSONObject(WelfarePlusRpcCall.receiveAntOceanTaskAward());
                        if (MessageUtil.checkResponse(TAG, jo)) {
                            Log.other("我的快递💌完成任务[" + appletName + "]");
                        }
                    }
                } else {
                    String taskId = jo.optString("taskId");
                    String taskCenInfo = jo.optString("taskCenInfo");
                    if (!StringUtil.hasEmpty(taskId, taskCenInfo)) {
                        jo = new JSONObject(WelfarePlusRpcCall.sendTrigger(taskId, taskCenInfo));
                        if (MessageUtil.checkResponse(TAG, jo)) {
                            jo = jo.getJSONObject("appletBaseConfigDTO");
                            appletName = jo.getString("appletName");
                            Log.other("我的快递💌完成任务[" + appletName + "]");
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryRecommendTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 普通任务
    private static void queryOrdinaryTask() {
        try {
            JSONObject jo = new JSONObject(WelfarePlusRpcCall.queryOrdinaryTask());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            String taskCenInfo = "MZVPQ0DScvD6NjaPJzk8iNRgSSvWpCuA";
            JSONArray taskDetailList = jo.getJSONArray("taskDetailList");
            for (int i = 0; i < taskDetailList.length(); i++) {
                jo = taskDetailList.getJSONObject(i);
                String taskId = jo.getString("taskId");
                String sendCampTriggerType = jo.getString("sendCampTriggerType");
                if (Objects.equals("EVENT_TRIGGER", sendCampTriggerType)) {
                    // 不允许完成事件规则任务
                    continue;
                }
                TaskProcessStatus taskProcessStatus = TaskProcessStatus.valueOf(
                        jo.getString("taskProcessStatus")
                );
                if (taskProcessStatus == TaskProcessStatus.NONE_SIGNUP) {
                    jo = new JSONObject(WelfarePlusRpcCall.signupTrigger(taskId, taskCenInfo));
                    if (!MessageUtil.checkResponse(TAG, jo)) {
                        continue;
                    }
                    taskProcessStatus = TaskProcessStatus.SIGNUP_COMPLETE;
                }
                if (taskProcessStatus == TaskProcessStatus.NOT_DONE
                        || taskProcessStatus == TaskProcessStatus.SIGNUP_COMPLETE) {
                    jo = new JSONObject(WelfarePlusRpcCall.sendTrigger(taskId, taskCenInfo));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        jo = jo.getJSONObject("appletBaseConfigDTO");
                        Log.other("我的快递💌完成任务[" + jo.getString("appletName") + "]");
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryOrdinaryTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public enum TaskProcessStatus {
        NOT_DONE,
        RECEIVE_SUCCESS,
        NONE_SIGNUP,
        SIGNUP_COMPLETE
    }

    public enum WelfarePlusOption implements CustomOption {
        RECOMMEND_TASK("推荐任务"),
        ORDINARY_TASK("普通任务");

        private final String nickName;

        WelfarePlusOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }
}
