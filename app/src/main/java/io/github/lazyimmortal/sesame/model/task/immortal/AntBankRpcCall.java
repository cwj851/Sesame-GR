package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;

import java.util.Objects;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;

public class AntBankRpcCall {

    /**
     * 签到查询与请求
     * @param channel miniApp 支付宝小程序
     *               myBankApp 网商银行应用
     * @param operation signConsult 查询当天签到信息
     *                 signCalendarQuery 查询日历签到信息
     *                 signApply 签到请求
     * @param playId PLAY100177545
     * @return String
     */
    private static String signinPlay(String channel, String operation, String playId) {
        String args = "[{\"channel\":\"" + channel + "\",\"needMultiple\":false,\"operation\":\"" + operation + "\",\"playId\":\"" + playId + "\"}]";
        if (Objects.equals(operation, "signCalendarQuery")) {
            args = "[{\"channel\":\"" + channel + "\",\"needMultiple\":false,\"operation\":\"" + operation + "\",\"playId\":\"" + playId + "\",\"size\":7}]";
        }
        return ApplicationHook.requestString("com.alipay.loanpromoweb.member.play.signinPlay", args);
    }

    public static String myBankSignInConsult() {
        return signinPlay("myBankApp", "signConsult", "PLAY100177545");
    }

    public static String myBankSignInApply() {
        return signinPlay("myBankApp", "signApply", "PLAY100177545");
    }

    // 福利金领取
    public static String queryEnableVirtualProfitV2() {
        String args = "[{\"profitType\":\"ANTBANK_WELFARE_POINT\",\"sceneCode\":[\"FULICenter_JKJML\",\"FULICenter_JZN\",\"BC3_BC3V1\",\"BC3_BC3V2\",\"BC3_BC3V3\",\"SQB_SQBV0\",\"SQB_SQBV1\",\"SQB_SQBV2\",\"SQB_SQBV3\",\"SQB_SQBV4\",\"SQB_SQBV5\",\"SQB_SQBV6\",\"SQB_SQBV7\",\"SQB_SQBV8\",\"SQB_SQBV9\",\"SQB_SQBV10\",\"SQB_SQBV11\",\"SQB_SQBSIGN\",\"FULICenter_JKJQW\",\"FULICenter_WSWF\",\"FULICenter_FLKZS\",\"FULICenter_KGJXBBF\",\"FULICenter_AXHZXB\",\"FULICenter_BBF\",\"FULICenter_V1\",\"FULICenter_V2\",\"FULICenter_V3\",\"FULICenter_V4\",\"FULICenter_V5\",\"FULICenter_V6\",\"FULICenter_V7\",\"FULICenter_YulibaoAUM\",\"FULICenter_PayByMybank\",\"FULICenter_DepositAUM\",\"FULICenter_YYYYH\",\"FULICenter_QYZ\",\"FULICenter_V7PLUS\",\"FULICenter_V6PLUS\",\"FULICenter_V5PLUS\",\"FULICenter_V8\",\"FULICenter_V9\",\"FULICenter_V10\",\"FULICenter_LCCZ\",\"FULICenter_LCTZ\"]}]";
        return ApplicationHook.requestString("com.alipay.loanpromoweb.promo.virtualProfit.queryEnableVirtualProfitV2", args);
    }

    public static String batchUseVirtualProfit(JSONArray virtualProfitIdList) {
        String args = "[{\"virtualProfitIdList\":" + virtualProfitIdList + "}]";
        return ApplicationHook.requestString("com.alipay.loanpromoweb.promo.virtualProfit.batchUseVirtualProfit", args);
    }

    /**
     * 任务查询
     * @param appletId 小程序ID
     * @return String
     */
    public static String taskQuery(String appletId) {
        String args = "[{\"appletId\":\"" + appletId + "\"}]";
        return ApplicationHook.requestString("com.alipay.loanpromoweb.promo.task.taskQuery", args);
    }

    /**
     * 任务触发
     * @param appletId 小程序ID
     * @param stageCode 阶段代码: signup send receive
     * @param taskCenId 任务ID --> 阿里写反了，内部给它调换回来
     * @return String
     */
    public static String taskTrigger(String appletId, String stageCode, String taskCenId) {
        String args = "[{\"appletId\":\"" + taskCenId + "\",\"stageCode\":\"" + stageCode + "\",\"taskCenId\":\"" + appletId + "\"}]";
        return ApplicationHook.requestString("com.alipay.loanpromoweb.promo.task.taskTrigger", args);
    }

    /*发发日*/
    public static String promoCertQuery() {
        String args = "[{\"certTemplateIdSet\":[\"CT02048186\",\"CT32675397\"]}]";
        return ApplicationHook.requestString("com.alipay.loanpromoweb.promo.cert.query", args);
    }

    public static String taskQuery() {
        String args = "[{\"appletId\":\"AP1269301\"}]";
        return ApplicationHook.requestString("com.alipay.loanpromoweb.promo.task.taskQuery", args);
    }
}
