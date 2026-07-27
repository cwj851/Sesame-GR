package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;

public class AntBank {
    private static final String TAG = AntBank.class.getSimpleName();

    private static SelectModelField antBankOptions;

    public static void virtualProfit() {
        antBankOptions = ModelTask.getModel(Immortal.class).antBankOptions;
        taskQuery("AP1269301");
        if (antBankOptions.contains(
                AntBankOption.VIRTUAL_PROFIT_QUERY.name())) {
            queryEnableVirtualProfit();
        }
    }

    private static Boolean myBankSignIn() {
        if (!antBankOptions.contains(
                AntBankOption.VIRTUAL_PROFIT_SIGN.name())) {
            return false;
        }
        try {
            JSONObject jo = new JSONObject(AntBankRpcCall.myBankSignInConsult());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            jo = jo.getJSONObject("result").getJSONObject("todaySignInfo");
            if (!jo.getBoolean("signApplyDone")) {
                return myBankSignInApply();
            }
        } catch (Throwable th) {
            Log.i(TAG, "myBankSignIn err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    private static Boolean myBankSignInApply() {
        try {
            JSONObject jo = new JSONObject(AntBankRpcCall.myBankSignInApply());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            jo = jo.getJSONObject("result").getJSONObject("todaySignInfo");
            int hasSignedContinuousCnt = jo.getInt("hasSignedContinuousCnt");
            jo = jo.getJSONObject("signPrizeSentPoint");
            int point = jo.getInt("point");
            Log.other("网商银行🪙连续签到[第" + hasSignedContinuousCnt + "天]#获得[" + point + "福利金]");
            return true;
        } catch (Throwable th) {
            Log.i(TAG, "myBankSignInApply err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    private static void queryEnableVirtualProfit() {
        try {
            JSONObject jo = new JSONObject(AntBankRpcCall.queryEnableVirtualProfitV2());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("result");
            JSONArray ja = jo.getJSONArray("virtualProfitList");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                String sceneDesc = jo.getString("sceneDesc");
                JSONArray virtualProfitIds = jo.getJSONArray("virtualProfitIds");
                int reward = jo.getInt("reward") * virtualProfitIds.length();
                if (batchUseVirtualProfit(virtualProfitIds)) {
                    if (virtualProfitIds.length() > 1) {
                        sceneDesc = sceneDesc + "×" + virtualProfitIds.length() + "笔";
                    }
                    Log.other("网商银行🪙领取奖励[" + sceneDesc + "]#获得[" + reward + "福利金]");
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "queryEnableVirtualProfit err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static Boolean batchUseVirtualProfit(JSONArray virtualProfitIdList) {
        try {
            JSONObject jo = new JSONObject(AntBankRpcCall.batchUseVirtualProfit(virtualProfitIdList));
            return MessageUtil.checkResponse(TAG, jo);
        } catch (Throwable th) {
            Log.i(TAG, "queryEnableVirtualProfitV err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    private static void taskQuery(String appletId) {
        if (myBankSignIn()) {
            TimeUtil.sleep(1000);
        }
        if (!antBankOptions.contains(
                AntBankOption.VIRTUAL_PROFIT_TASK.name())) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(AntBankRpcCall.taskQuery(appletId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray ja = jo.getJSONObject("result").getJSONArray("taskDetailList");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);

                if (!"USER_TRIGGER".equals(jo.optString("sendCampTriggerType"))) {
                    continue;
                }
                String taskProcessStatus = jo.optString("taskProcessStatus");
                String taskId = jo.getString("taskId");
                if (TaskProcessStatus.NONE_SIGNUP.name().equals(taskProcessStatus)) {
                    if (!taskTrigger(appletId, "signup", taskId)) {
                        continue;
                    }
                    taskProcessStatus = TaskProcessStatus.SIGNUP_COMPLETE.name();
                    TimeUtil.sleep(1000);
                }
                if (TaskProcessStatus.SIGNUP_COMPLETE.name().equals(taskProcessStatus)) {
                    if (taskTrigger(appletId, "send", taskId)) {
                        jo = jo.getJSONObject("taskExtProps");
                        jo = new JSONObject(jo.getString("TASK_MORPHO_DETAIL"));
                        String prizeType = jo.getString("prizeType");
                        String title = jo.getString("title");
                        int prizeNum = jo.getInt("prizeNum");
                        Log.other("网商银行🪙完成任务[" + title + "]#获得[" + prizeNum + prizeType + "]");
                        TimeUtil.sleep(1000);
                    }
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "taskQuery err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static Boolean taskTrigger(String appletId, String stageCode, String taskCenId) {
        try {
            JSONObject jo = new JSONObject(AntBankRpcCall.taskTrigger(appletId, stageCode, taskCenId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
        } catch (Throwable th) {
            Log.i(TAG, "taskTrigger err:");
            Log.printStackTrace(TAG, th);
        }
        return true;
    }

    private enum TaskProcessStatus {
        NONE_SIGNUP, SIGNUP_COMPLETE, RECEIVE_SUCCESS;
    }

    public enum AntBankOption implements CustomOption {
        VIRTUAL_PROFIT_SIGN("福利金签到"),
        VIRTUAL_PROFIT_TASK("福利金任务"),
        VIRTUAL_PROFIT_QUERY("福利金领取");

        private final String nickName;

        AntBankOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

}
