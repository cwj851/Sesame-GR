package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.lazyimmortal.sesame.model.task.antMember.AntMember;
import io.github.lazyimmortal.sesame.model.task.antMember.AntMemberRpcCall;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.TimeUtil;

public class AntMemberAlpha {
    private static final String TAG = AntMemberAlpha.class.getSimpleName();

    // 蚂蚁积分-做其他任务
    public static void doOtherTask(JSONArray taskList) {
        try {
            for (int i = 0; i < taskList.length(); i++) {
                JSONObject task = taskList.getJSONObject(i);
                if (task.getBoolean("hybrid")) {
                    int periodCurrentCount = Integer
                            .parseInt(task.getJSONObject("extInfo").getString("PERIOD_CURRENT_COUNT"));
                    int periodTargetCount = Integer
                            .parseInt(task.getJSONObject("extInfo").getString("PERIOD_TARGET_COUNT"));
                    int count = periodTargetCount > periodCurrentCount ? periodTargetCount - periodCurrentCount : 0;
                    if (count > 0) {
                        doOtherTask(task, periodCurrentCount, periodTargetCount);
                    }
                } else {
                    doOtherTask(task, 0, 1);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "doOtherTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void doOtherTask(JSONObject task, int left, int right) {
        try {
            // String status = task.optString("status");
            JSONObject simpleTaskConfig = task.getJSONObject("simpleTaskConfig");
            String title = simpleTaskConfig.getString("title");
            Long configId = simpleTaskConfig.getLong("configId");
            String awardParamPoint = simpleTaskConfig.getJSONArray("stageVOList").getJSONObject(0).getJSONObject("awardParam")
                    .optString("awardParamPoint");
            String targetBusiness = task.getJSONArray("targetBusiness")
                    .getString(0);
            if (!targetBusiness.startsWith("ngfe")) {
                return;
            }
            String businessType = simpleTaskConfig.getString("businessType");
            if ("uvChangeBusinessType".equals(businessType)) {
                String[] targetBusinessArray = targetBusiness.split("#");
                String tagCode = targetBusinessArray[0];
                for (int i = left; i < right; i++) {
                    JSONObject jo = new JSONObject(AntMemberRpcCall.applyTask(title, configId));
                    TimeUtil.sleep(300);
                    if (!MessageUtil.checkResponse(TAG + " doOtherTask.applyTask", jo)) {
                        continue;
                    }
                    jo = new JSONObject(AntMemberAlphaRpcCall.ngfeUpdate(tagCode));
                    TimeUtil.sleep(300);
                    if (!MessageUtil.checkResponse(TAG + " doOtherTask.ngfeUpdate", jo)) {
                        continue;
                    }
                    String ex = right == 1 && left == 0 ? "" : "(" + (i + 1) + "/" + right + ")";
                    Log.other("会员任务🎖️完成任务[" + title + ex + "]#获得[" + awardParamPoint + "积分]");
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "doOtherTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public static void reSignIn() {
        if (!Status.hasFlagToday(AntMember.AntMemberFlag.SIGN_IN.flagName())
                || Status.hasFlagToday(AntMemberFlag.RE_SIGN_IN.flagName())) {
            return;
        }
        try {
            boolean reSignIn;
            do {
                JSONObject jo = new JSONObject(AntMemberRpcCall.queryMemberSigninCalendar());
                if (!MessageUtil.checkResponse(TAG, jo) || !jo.has("latestBreakDate")) {
                    break;
                }
                // 有断签日期
                String latestBreakDate = jo.getString("latestBreakDate");
                int latestRepairedDays = jo.getInt("latestRepairedDays");
                String cardId = queryReSignInCardId();
                if (cardId == null) {
                    Log.record("没有足够的补签卡");
                    break;
                }
                reSignIn = false;
                jo = new JSONObject(AntMemberAlphaRpcCall.reSignIn(cardId, latestBreakDate));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.other("会员签到📅签到补签[连续" + latestRepairedDays + "天]");
                    reSignIn = true;
                }
            } while (reSignIn);
            Status.flagToday(AntMemberFlag.RE_SIGN_IN.flagName());
        } catch (Throwable t) {
            Log.i(TAG, "reSignIn err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static String queryReSignInCardId() {
        try {
            JSONObject jo = new JSONObject(AntMemberAlphaRpcCall.queryReSignInCardInfo());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return null;
            }
            int remaining = jo.getInt("remaining");
            if (remaining > 0) {
                return jo.getJSONArray("cardIds").getString(0);
            }
            if (exchangeReSignInCard()) {
                return queryReSignInCardId();
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryReSignInCardId err:");
            Log.printStackTrace(TAG, t);
        }
        return null;
    }

    private static Boolean exchangeReSignInCard() {
        String benefitId = "202402010507472218";
        String itemId = "IT20240201000400084448";
        try {
            JSONObject jo = new JSONObject(AntMemberRpcCall.exchangeBenefit(benefitId, itemId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                String orderId = jo.getString("orderId");
                jo = new JSONObject(AntMemberAlphaRpcCall.querySingleExchangeOrderDetail(itemId, orderId));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    jo = jo.getJSONObject("exchangeOrderDetailConfigInfo");
                    String benefitName = jo.getString("benefitName");
                    int point = jo.getInt("point");
                    Log.other("会员积分🎐兑换权益[" + benefitName + "]#消耗[" + point + "积分]");
                }
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "exchangeReSignInCard err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    public enum AntMemberFlag implements Status.StatusFlag {
        RE_SIGN_IN;
    }
}
