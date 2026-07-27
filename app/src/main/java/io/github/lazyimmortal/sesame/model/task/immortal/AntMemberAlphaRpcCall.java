package io.github.lazyimmortal.sesame.model.task.immortal;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;

public class AntMemberAlphaRpcCall {
    public static String ngfeUpdate(String tagCode) {
        return ApplicationHook.requestString("com.alipay.csprod.prom.camp.ngfe.update",
                "[{\"tagCode\":\"" + tagCode + "\"}]");
    }

    /**
     * 查询签到日历
     * @param month 补签月份 202408
     * @return String
     */
    public static String queryMemberSigninCalendar(String month) {
        String args = "[{\"autoSignIn\":true,\"month\":\"" + month + "\",\"sceneCode\":\"QUERY\"}]";
        return ApplicationHook.requestString("com.alipay.amic.biz.rpc.signin.h5.queryMemberSigninCalendar", args);
    }

    // 查询已兑换的权益信息
    public static String querySingleExchangeOrderDetail(String benefitId, String outBizNo) {
        String args = "[{\"benefitId\":\"" + benefitId + "\",\"bizType\":\"PROMO\",\"outBizNo\":\"" + outBizNo + "\"}]";
        return ApplicationHook.requestString("com.alipay.alipaymember.biz.rpc.config.h5.querySingleBenefitDetail", args);
    }

    // 查询补签卡信息
    public static String queryReSignInCardInfo() {
        String args = "[{}]";
        return ApplicationHook.requestString("com.alipay.amic.biz.rpc.signin.h5.queryReSignInCardInfo", args);
    }

    /**
     * 会员补签
     * @param cardId 补签卡ID
     * @param reSignInDate 补签日期 20240831
     * @return String
     */
    public static String reSignIn(String cardId, String reSignInDate) {
        String args = "[{\"cardId\":\"" + cardId + "\",\"reSignInDate\":\"" + reSignInDate + "\"}]";
        return ApplicationHook.requestString("com.alipay.amic.biz.rpc.signin.h5.reSignIn", args);
    }
}
