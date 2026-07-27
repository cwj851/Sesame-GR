package io.github.lazyimmortal.sesame.model.task.antMember;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;

public class GoldBillRpcCall {
    public static String index() {
        String args = "[{}]";
        return ApplicationHook.requestString("com.alipay.finaggexpbff.needle.weeklyWelfare.index", args);
    }

    public static String trigger(String type) {
        String args = "[{\"type\":\"" + type + "\"}]";
        return ApplicationHook.requestString("com.alipay.finaggexpbff.needle.weeklyWelfare.trigger", args);
    }

    public static String collect(String campId) {
        String args = "[{\"campId\":\"" + campId + "\",\"directModeDisableCollect\":true,\"from\":\"antfarm\",\"trigger\":\"Y\"}]";
        return ApplicationHook.requestString("com.alipay.wealthgoldtwa.goldbill.v2.index.collect", args);
    }

    /**
     * 黄金票收取
     *
     * @param str signInfo
     * @return 结果
     */
    @Deprecated
    public static String goldBillCollect(String str) {
        String args = "[{" + str + "\"trigger\":\"Y\"}]";
        return ApplicationHook.requestString("com.alipay.wealthgoldtwa.goldbill.v2.index.collect", args);
    }
}
