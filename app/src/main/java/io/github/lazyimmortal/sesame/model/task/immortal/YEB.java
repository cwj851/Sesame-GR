package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;

public class YEB {
    private static final String TAG = YEB.class.getSimpleName();

    public static void incomePlus() {
        SelectModelField incomePlusOptions = ModelTask.getModel(Immortal.class).incomePlusOptions;
        try {
            JSONObject jo = new JSONObject(YEBRPCCall.index());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("result");
            if (!jo.getBoolean("isYebOpen")) {
                Log.record("余额宝未开通");
                return;
            }
            String contractId = jo.getString("contractId");
            receiveGold(contractId, jo.getJSONArray("coinOrderList"));
            if (incomePlusOptions.getValue().contains(
                    IncomePlusOption.RECEIVE_INCOME_FOOD.name())) {
                if (!jo.optBoolean("hasReceivedIncomeFood", true)) {
                    // 如果未领取鱼粮
                    receiveIncomeFood(contractId, jo.getString("incomeFood"));
                }
            }
            if (incomePlusOptions.getValue().contains(
                    IncomePlusOption.INCOME_PLUS_SIGN_IN.name())) {
                incomePlusSignIn();
            }
            if (incomePlusOptions.getValue().contains(
                    IncomePlusOption.INCOME_PLUS_FEED_TASK.name())) {
                incomePlusFeedTaskList();
            }
            if (incomePlusOptions.getValue().contains(
                    IncomePlusOption.INCOME_PLUS_FEEDING_FISH.name())) {
                feedFish();
            }
        } catch (Throwable t) {
            Log.i(TAG, "incomePlus error:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void receiveGold(String contractId, JSONArray coinOrderList) {
        try {
            for (int i = 0; i < coinOrderList.length(); i++) {
                JSONObject jo = coinOrderList.getJSONObject(i);
                if (jo.has("orderId")) {
                    String amount = jo.getString("amount");
                    jo = new JSONObject(YEBRPCCall.receiveGold(contractId, jo.getString("orderId")));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Log.other("收益养鱼🐟领金泡泡#获得[" + amount + "金泡泡]");
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveGold error:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void receiveIncomeFood(String contractId, String incomeFood) {
        try {
            JSONObject jo = new JSONObject(YEBRPCCall.receiveIncomeFood(incomeFood, contractId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.other("收益养鱼🐟领取鱼粮[" + incomeFood + "g]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveIncomeFood error:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void feedFish() {
        try {
            JSONObject jo = new JSONObject(YEBRPCCall.index());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("result");
            String contractId = jo.getString("contractId");
            String amount = jo.getJSONObject("foodAmount").getString("amount");
            // 没有鱼粮不投喂
            if (Integer.parseInt(amount) <= 0) {
                return;
            }
            if (feedingFish(contractId, amount)) {
                playAfterFeed(contractId);
            }
        } catch (Throwable t) {
            Log.i(TAG, "feedFish error:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static Boolean feedingFish(String contractId, String amount) {
        try {

            JSONObject jo = new JSONObject(YEBRPCCall.feedingFish(amount, contractId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.other("收益养鱼🐟投喂鱼粮[" + amount + "g]");
                return true;
            }
        } catch (Throwable th) {
            Log.i(TAG, "feedingFish err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    private static void playAfterFeed(String contractId) {
        try {
            JSONObject jo = new JSONObject(YEBRPCCall.playAfterFeed(contractId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("result");
            if (!jo.has("prizeList")) {
                return;
            }
            JSONArray prizeList = jo.getJSONArray("prizeList");
            for (int i = 0; i < prizeList.length(); i++) {
                jo = prizeList.getJSONObject(i);
                String prizeName = jo.getString("prizeName");
                String price = jo.getString("price");
                Log.other("收益养鱼🐟投喂奖励[" + prizeName + "*" + price + "]");
            }
            YEBRPCCall.refresh("completeTask");
        } catch (Throwable th) {
            Log.i(TAG, "playAfterFeed err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void incomePlusSignIn() {
        try {
            JSONObject jo = new JSONObject(YEBRPCCall.registrationQuery("SIGN_IN_CALENDAR_RECALL", "INCOME_PLUS_SIGN_IN_AWARD"));
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("result");
                JSONArray prizeDetailList = jo.getJSONArray("prizeDetailList");
                for (int i = 0; i < prizeDetailList.length(); i++) {
                    jo = prizeDetailList.getJSONObject(i);
                    if (Objects.equals(SignStatus.SIGN_IN.name(), jo.getString("signStatus"))) {
                        String prizeId = jo.getString("prizeId");
                        String prizeDayText = jo.getString("prizeDayText");
                        String prizeAmountText = jo.getString("prizeAmountText");
                        String prizeName = jo.getString("prizeName");
                        jo = new JSONObject(YEBRPCCall.incomePlusSignIn(prizeId));
                        if (MessageUtil.checkResponse(TAG, jo)) {
                            Log.other("收益养鱼🐟七天签到[" + prizeDayText + "]#获得[" + prizeAmountText + prizeName + "]");
                        }
                    }
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "incomePlusSignIn err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void incomePlusFeedTaskList() {
        try {
            JSONObject jo = new JSONObject(YEBRPCCall.incomePlusFeedTaskList());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("result");
            jo = jo.getJSONObject("taskListInfo");
            JSONArray uncompletedList = jo.getJSONArray("uncompletedList");
            for (int i = 0; i < uncompletedList.length(); i++) {
                jo = uncompletedList.getJSONObject(i);
                String appletId = jo.getString("appletId");
                String taskId = jo.getString("taskId");
                String taskProcessStatus = jo.getString("taskProcessStatus");
                JSONObject taskExtProps = jo.getJSONObject("taskExtProps");
                String title = taskExtProps.getJSONObject("TASK_MORPHO_DETAIL").getString("title");
                if (Objects.equals("NONE_SIGNUP", taskProcessStatus)) {
                    // 未注册: 注册任务
                    jo = new JSONObject(YEBRPCCall.promosdkIndexForward(appletId, taskId, "trigger"));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Log.other("收益养鱼🐟注册任务[" + title + "]");
                        taskProcessStatus = "SIGNUP_COMPLETE";
                    }
                }
                if (Objects.equals("NOT_DONE", taskProcessStatus) || Objects.equals("SIGNUP_COMPLETE", taskProcessStatus)) {
                    // 未完成: 完成任务
                    jo = new JSONObject(YEBRPCCall.promosdkIndexForward(appletId, taskId, "complete"));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Log.other("收益养鱼🐟完成任务[" + title + "]");
                        taskProcessStatus = "TO_RECEIVE";
                    }
                }
                if (Objects.equals("TO_RECEIVE", taskProcessStatus)) {
                    // 已完成: 领取奖励
                    jo = new JSONObject(YEBRPCCall.promosdkIndexForward(appletId, taskId, "receive"));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Log.other("收益养鱼🐟领取奖励[" + title + "]");
                    }
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "incomePlusFeedTaskList err:");
            Log.printStackTrace(TAG, th);
        }
    }

    public enum SignStatus {
        SIGNED_IN, SIGN_IN, NOT_STARTED;
    }

    public enum IncomePlusOption implements CustomOption {
        RECEIVE_INCOME_FOOD("收益加 | 领取鱼粮"),
        INCOME_PLUS_SIGN_IN("收益加 | 七天签到"),
        INCOME_PLUS_FEED_TASK("收益加 | 鱼粮任务"),
        INCOME_PLUS_FEEDING_FISH("收益加 | 投喂鱼粮");

        private final String nickName;

        IncomePlusOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }
}
