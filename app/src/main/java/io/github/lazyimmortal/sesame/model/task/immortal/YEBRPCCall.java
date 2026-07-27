package io.github.lazyimmortal.sesame.model.task.immortal;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;

public class YEBRPCCall {

    // 福利加首页
    public static String index() {
        String args = "[{\"bizScenario\":\"YEB_HOME\",\"newScene\":\"oldGold\",\"version\":\"V1\"}]";
        return ApplicationHook.requestString("com.alipay.yebscenebff.needle.incomePlus.index", args);
    }

    /**
     *
     * @param playActionCode playActionCode
     *                       SIGN_IN_CALENDAR_RECALL:查询签到日历
     * @param playEntrance playEntrance
     *                     INCOME_PLUS_SIGN_IN_AWARD:查询签到奖励
     * @return String
     */
    public static String registrationQuery(String playActionCode, String playEntrance) {
        String args = "[{\"playActionCode\":\"" + playActionCode + "\",\"playEntrance\":\"" + playEntrance + "\"}]";
        return ApplicationHook.requestString("com.alipay.yebscenebff.needle.registration.query", args);
    }

    /**
     *
     * @param playActionCode playActionCode
     *                       SIGNIN_TRIGGER:触发签到
     * @param playEntrance playEntrance
     *                     INCOME_PLUS_SIGN_IN_AWARD:签到奖励
     * @param prizeId prizeId
     * @return String
     */
    public static String registrationTrigger(String playActionCode, String playEntrance, String prizeId) {
        String args = "[{\"playActionCode\":\"" + playActionCode + "\",\"playEntrance\":\"" + playEntrance + "\",\"prizeId\":\"" + prizeId + "\"}]";
        return ApplicationHook.requestString("com.alipay.yebscenebff.needle.registration.trigger", args);
    }

    // 收益加签到
    public static String incomePlusSignIn(String prizeId) {
        return registrationTrigger("SIGNIN_TRIGGER", "INCOME_PLUS_SIGN_IN_AWARD", prizeId);
    }

    // 领取金泡泡
    public static String receiveGold(String contractId, String orderId) {
        String args = "[{\"contractId\":\"" + contractId + "\",\"orderId\":\"" + orderId + "\"}]";
        return ApplicationHook.requestString("com.alipay.yebscenebff.needle.incomePlus.receiveGold", args);
    }

    // 领取鱼粮
    public static String receiveIncomeFood(String amount, String contractId) {
        String args = "[{\"amount\":\"" + amount + "\",\"contractId\":\"" + contractId + "\"}]";
        return ApplicationHook.requestString("com.alipay.yebscenebff.needle.incomePlus.receiveIncomeFood", args);
    }

    // 喂鱼
    public static String feedingFish(String amount, String contractId) {
        String args = "[{\"amount\":\"" + amount + "\",\"contractId\":\"" + contractId + "\"}]";
        return ApplicationHook.requestString("com.alipay.yebscenebff.needle.incomePlus.feedingFish", args);
    }

    // 喂鱼后续
    public static String playAfterFeed(String contractId) {
        String args = "[{\"contractId\":\"" + contractId + "\"}]";
        return ApplicationHook.requestString("com.alipay.yebscenebff.needle.incomePlus.playAfterFeed", args);
    }

    /**
     *
     * @param cardList
     *        completeTask:完成任务
     *        foodQuery:查询鱼粮余量
     * @return String
     */
    public static String refresh(String cardList) {
        String args = "[{\"cardList\":[\"" + cardList + "\"]}]";
        return ApplicationHook.requestString("com.alipay.yebscenebff.needle.incomePlus.refresh", args);
    }

    // 鱼粮任务列表
    public static String incomePlusFeedTaskList() {
        return ApplicationHook.requestString("com.alipay.yebscenebff.needle.incomePlusFeedTaskList", "[{}]");
    }

    /**
     *
     * @param appletId 小程序ID
     * @param taskId 任务ID
     * @param path 任务触发状态
     *             trigger: 触发任务
     *             complete: 完成任务
     *             receive: 领取奖励
     * @return String
     */
    public static String promosdkIndexForward(String appletId, String taskId, String path) {
        String args = "[{\"appName\":\"yebscenebff\",\"extParams\":{\"appletId\":\"" + appletId + "\",\"taskId\":\"" + taskId + "\",\"version\":2},\"path\":\"task." + path + "\",\"withJson\":false}]";
        return ApplicationHook.requestString("com.alipay.yebscenebff.promosdk.index.forward", args);
    }
}
