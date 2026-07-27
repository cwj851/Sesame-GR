package io.github.lazyimmortal.sesame.model.task.antMember;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.PromiseSimpleTemplateIdMap;

public class AntGroup {
    private static final String TAG = AntGroup.class.getSimpleName();

    public static void run() {
        if (!queryHome()) {
            return;
        }
        SelectModelField zhiMaCreditOptions = ModelTask.getModel(AntMember.class).zhiMaCreditOptions;
        if (zhiMaCreditOptions.contains(ZhiMaCreditOption.CREDIT_ACCUMULATE.name())) {
            queryListV3();
        }
        if (zhiMaCreditOptions.contains(ZhiMaCreditOption.LIFE_RECORD.name())) {
            promise(ModelTask.getModel(AntMember.class).lifeRecordList);
        }
        if (zhiMaCreditOptions.contains(ZhiMaCreditOption.COLLECT_CREDIT_FEEDBACK.name())) {
            collectCreditFeedback();
        }
    }

    private static Boolean queryHome() {
        try {
            JSONObject jo = new JSONObject(AntGroupRpcCall.queryHome());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            JSONObject entrance = jo.getJSONObject("entrance");
            if (!entrance.optBoolean("openApp")) {
                Log.other("芝麻信用💌未开通");
                return false;
            }
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "queryHome err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void queryListV3() {
        try {
            JSONObject jo = new JSONObject(AntGroupRpcCall.queryListV3());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            JSONObject dailyTaskListVO = jo.getJSONObject("dailyTaskListVO");
            JSONArray waitJoinTaskVOS = dailyTaskListVO.getJSONArray("waitJoinTaskVOS");
            handleTaskList(waitJoinTaskVOS);
            handleTaskList(jo.getJSONArray("toCompleteVOS"));
        } catch (Throwable t) {
            Log.i(TAG, "queryListV3 err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void handleTaskList(JSONArray taskList) {
        try {
            for (int i = 0; i < taskList.length(); i++) {
                handleTask(taskList.getJSONObject(i));
                TimeUtil.sleep(5000);
            }
        } catch (Throwable t) {
            Log.i(TAG, "handleTaskList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void handleTask(JSONObject task) {
        if (!task.has("templateId")) {
            return;
        }
        try {
            if (!task.getBoolean("jumpToPushModel") && !task.getJSONObject("strategyRule").optBoolean("assistiveTouch")) {
                return;
            }
            String templateId = task.getString("templateId");
            int completedNum = task.getInt("completedNum") + 1;
            int needCompleteNum = task.getInt("needCompleteNum");
            String title = task.getString("title") + "(" + completedNum + "/" + needCompleteNum + ")";
            if (!task.has("todayFinish")) {
                // 未领取的任务
                if (taskFeedback(templateId)) {
                    joinActivity(templateId, title);
                }
            } else if (task.has("recordId")) {
                // 完成已加入的任务
                if (taskFeedback(templateId)) {
                    pushActivity(task.getString("recordId"), title);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "handleTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static Boolean taskFeedback(String templateId) {
        try {
            JSONObject jo = new JSONObject(AntGroupRpcCall.taskFeedback(templateId));
            return MessageUtil.checkResponse(TAG, jo);
        } catch (Throwable t) {
            Log.i(TAG, "taskFeedback err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static void joinActivity(String templateId, String title) {
        try {
            JSONObject jo = new JSONObject(AntGroupRpcCall.joinActivity(templateId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                pushActivity(jo.getString("recordId"), title);
            }
        } catch (Throwable t) {
            Log.i(TAG, "joinActivity err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void pushActivity(String recordId, String title) {
        try {
            JSONObject jo = new JSONObject(AntGroupRpcCall.pushActivity(recordId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.other("芝麻信用💌完成任务[" + title + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "pushActivity err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 生活记录
    private static void promise(SelectModelField promiseList) {
        try {
            JSONObject jo = new JSONObject(AntGroupRpcCall.promiseQueryHome());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            JSONArray promiseSimpleTemplates = jo.getJSONArray("promiseSimpleTemplates");
            for (int i = 0; i < promiseSimpleTemplates.length(); i++) {
                jo = promiseSimpleTemplates.getJSONObject(i);
                String templateId = jo.getString("templateId");
                String promiseName = jo.getString("promiseName");
                String status = jo.getString("status");
                if ("un_join".equals(status) && promiseList.contains(templateId)) {
                    promiseJoin(querySingleTemplate(templateId));
                }
                PromiseSimpleTemplateIdMap.getInstance().add(templateId, promiseName);
            }
            PromiseSimpleTemplateIdMap.getInstance().save();
        } catch (Throwable t) {
            Log.i(TAG, "promise err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static JSONObject querySingleTemplate(String templateId) {
        try {
            JSONObject jo = new JSONObject(AntGroupRpcCall.querySingleTemplate(templateId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return null;
            }
            jo = jo.getJSONObject("data");
            JSONObject result = new JSONObject();

            result.put("joinFromOuter", false);
            result.put("templateId", jo.getString("templateId"));
            result.put("autoRenewStatus", Boolean.valueOf(jo.getString("autoRenewStatus")));

            JSONObject joinGuarantyRule = jo.getJSONObject("joinGuarantyRule");
            joinGuarantyRule.put("selectValue", joinGuarantyRule.getJSONArray("canSelectValues").getString(0));
            joinGuarantyRule.remove("canSelectValues");
            result.put("joinGuarantyRule", joinGuarantyRule);

            JSONObject joinRule = jo.getJSONObject("joinRule");
            joinRule.put("selectValue", joinRule.getJSONArray("canSelectValues").getString(0));
            joinRule.remove("joinRule");
            result.put("joinRule", joinRule);

            JSONObject periodTargetRule = jo.getJSONObject("periodTargetRule");
            periodTargetRule.put("selectValue", periodTargetRule.getJSONArray("canSelectValues").getString(0));
            periodTargetRule.remove("canSelectValues");
            result.put("periodTargetRule", periodTargetRule);

            JSONObject dataSourceRule = jo.getJSONObject("dataSourceRule");
            dataSourceRule.put("selectValue", dataSourceRule.getJSONArray("canSelectValues").getJSONObject(0).getString("merchantId"));
            dataSourceRule.remove("canSelectValues");
            result.put("dataSourceRule", dataSourceRule);
            return result;
        } catch (Throwable t) {
            Log.i(TAG, "querySingleTemplate err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    private static void promiseJoin(JSONObject data) {
        if (data == null) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(AntGroupRpcCall.promiseJoin(data));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            String promiseName = jo.getString("promiseName");
            Log.other("生活记录📝添加记录[" + promiseName + "]");
        } catch (Throwable t) {
            Log.i(TAG, "promiseJoin err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 查询持续做明细任务
    private JSONObject promiseQueryDetail(String recordId) throws JSONException {
        JSONObject jo = new JSONObject(AntGroupRpcCall.promiseQueryDetail(recordId));
        if (!jo.optBoolean("success")) {
            return null;
        }
        return jo;
    }

    private static void collectCreditFeedback() {
        try {
            JSONObject jo = new JSONObject(AntGroupRpcCall.queryCreditFeedback());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray ja = jo.getJSONArray("creditFeedbackVOS");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                if (!"UNCLAIMED".equals(jo.getString("status"))) {
                    continue;
                }
                String title = jo.getString("title");
                String creditFeedbackId = jo.getString("creditFeedbackId");
                String potentialSize = jo.getString("potentialSize");
                jo = new JSONObject(AntGroupRpcCall.collectCreditFeedback(creditFeedbackId));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.other("芝麻信用💌领取奖励[" + title + "]#获得[" + potentialSize + "芝麻粒]");
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "collectCreditFeedback err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public enum ZhiMaCreditOption implements CustomOption {
        CREDIT_ACCUMULATE("信用积累"),
        LIFE_RECORD("生活记录"),
        COLLECT_CREDIT_FEEDBACK("收芝麻粒");

        private final String nickName;

        ZhiMaCreditOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }
}
