package io.github.lazyimmortal.sesame.model.task.antMember;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.RandomUtil;

public class MerchantServiceRpcCall {

    /* 商家服务 */
    public static String transcodeCheck() {
        return ApplicationHook.requestString("alipay.mrchservbase.mrchbusiness.sign.transcode.check",
                "[{}]");
    }

    // 商家服务签到
    public static String signIn() {
        return ApplicationHook.requestString("alipay.mrchservbase.mrchpoint.sqyj.homepage.signin.v1",
                "[{}]");
    }

    public static String queryMoreTask() {
        return ApplicationHook.requestString("alipay.mrchservbase.task.more.query",
                "[{}]");
    }

    /**
     * 招财金签到(已过期)
     *
     * @deprecated 请使用 {@link #signIn()} 替代
     */
    @Deprecated
    private static String zcjViewInvoke(String compId) {
        String args = "[{\"compId\":\"" + compId + "\"}]";
        return ApplicationHook.requestString("alipay.mrchservbase.zcj.view.invoke", args);
    }

    /**
     * @deprecated 请使用 {@link #signIn()} 替代
     */
    @Deprecated
    public static String zcjSignInQuery() {
        return zcjViewInvoke("ZCJ_SIGN_IN_QUERY");
    }

    /**
     * @deprecated 请使用 {@link #signIn()} 替代
     */
    @Deprecated
    public static String zcjSignInExecute() {
        return zcjViewInvoke("ZCJ_SIGN_IN_EXECUTE");
    }

    // 签到任务

    /**
     * @deprecated 请使用 {@link #taskListQueryV2()} 替代
     */
    @Deprecated
    public static String taskListQuery() {
        String args = "[{\"compId\":\"ZCJ_TASK_LIST\",\"params\":{\"activityCode\":\"ZCJ\",\"clientVersion\":\"10.3.36\",\"extInfo\":{},\"platform\":\"Android\",\"underTakeTaskCode\":\"\"}}]";
        return ApplicationHook.requestString("alipay.mrchservbase.zcj.taskList.query", args);
    }

    public static String taskListQueryV2(String taskItemCode) {
        String args = "[{\"taskItemCode\":\"" + taskItemCode + "\"}]";
        return ApplicationHook.requestString("alipay.mrchservbase.zcj.taskList.query.v2", args);
    }

    /**
     * @deprecated 请使用 {@link #queryMoreTask()} 替代
     */
    @Deprecated
    public static String taskListQueryV2() {
        return taskListQueryV2("");
    }

    public static String taskFinish(String bizId) {
        String args = "[{\"bizId\":\"" + bizId + "\"}]";
        return ApplicationHook.requestString("com.alipay.adtask.biz.mobilegw.service.task.finish", args);
    }

    public static String taskReceive(String taskCode) {
        String args = "[{\"compId\":\"ZTS_TASK_RECEIVE\",\"extInfo\":{\"taskCode\":\"" + taskCode + "\"}}]";
        return ApplicationHook.requestString("alipay.mrchservbase.sqyj.task.receive", args);
    }

    public static String taskQueryByActionCode(String actionCode) {
        String args = "[{\"actionCode\":\"" + actionCode + "\"}]";
        return ApplicationHook.requestString("alipay.mrchservbase.task.query.by.actioncode", args);
    }

    public static String taskActionProduce(String actionCode) {
        String args = "[{\"actionCode\":\"" + actionCode + "\"}]";
        return ApplicationHook.requestString("alipay.mrchservbase.biz.task.action.produce", args);
    }

    public static String ballQueryV1() {
        return ApplicationHook.requestString("alipay.mrchservbase.mrchpoint.ball.query.v1", "[{}]");
    }

    public static String ballReceive(String ballIds) {
        String args = "[{\"ballIds\":[\"" + ballIds + "\"],\"channel\":\"MRCH_SELF\",\"outBizNo\":\"" + RandomUtil.getRandomUUID() + "\"}]";
        return ApplicationHook.requestString("alipay.mrchservbase.mrchpoint.ball.receive", args);
    }


    public static String mrchpoint_ttms_query() {
        String args = "[{\"compId\":\"MRCH_POINT_TTMS_QUERY\",\"extInfo\":{\"seckillVersion\":\"6\"}}]";
        return ApplicationHook.requestString("alipay.mrchservbase.mrchpoint.ttms.query", args);
    }

    public static String mrchpoint_ttms_page(String productCode) {
        String args = "[{\"compId\":\"MRCH_POINT_TTMS_PAGE_DETAIL\",\"extInfo\":{\"channelSource\":\"zcjMall\",\"filterCondition\":{\"lottery\":\"N\",\"seckill\":\"Y\"},\"productCode\":\"" + productCode + "\",\"seckillPushRoundInstanceId\":\"\"}}]";
        return ApplicationHook.requestString("alipay.mrchservbase.mrchpoint.ttms.page.detail", args);
    }

    public static String mrchpoint_item_seckill(String itemCode, int pointAmount, String roundInstanceId) {
        String args = "[{\"compId\":\"MRCH_POINT_ITEM_SECKILL\",\"extInfo\":{\"channelSource\":\"zcjMall\",\"exchangeStartTime\":\"\",\"itemCode\":\"" + itemCode + "\",\"moneyAmount\":\"\",\"pointAmount\":" + pointAmount + ",\"roundInstanceId\":\"" + roundInstanceId + "\"}}]";
        return ApplicationHook.requestString("alipay.mrchservbase.mrchpoint.item.seckill", args);
    }

    // 开门打卡
    @Deprecated(since = "2025.03.01")
    public static String KMDKQueryActivity() {
        String args = "[{\"scene\":\"activityCenter\"}]";
        return ApplicationHook.requestString("alipay.merchant.kmdk.query.activity", args);
    }

    @Deprecated(since = "2025.03.01")
    public static String KMDKSignIn(String activityNo) {
        String args = "[{\"activityNo\":\"" + activityNo + "\"}]";
        return ApplicationHook.requestString("alipay.merchant.kmdk.signIn", args);
    }

    @Deprecated(since = "2025.03.01")
    public static String KMDKSignUp(String activityNo) {
        String args = "[{\"activityNo\":\"" + activityNo + "\"}]";
        return ApplicationHook.requestString("alipay.merchant.kmdk.signUp", args);
    }
}
