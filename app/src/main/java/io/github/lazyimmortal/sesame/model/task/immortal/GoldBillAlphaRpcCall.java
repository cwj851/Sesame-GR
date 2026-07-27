package io.github.lazyimmortal.sesame.model.task.immortal;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;

public class GoldBillAlphaRpcCall {
    public static String index() {
        String args = "[{\"taskId\":\"\"}]";
        return ApplicationHook.requestString("com.alipay.wealthgoldtwa.needle.v2.index", args);
    }

    public static String collect(String campId) {
        String args = "[{\"campId\":\"" + campId + "\",\"directModeDisableCollect\":true,\"from\":\"antfarm\",\"trigger\":\"Y\"}]";
        return ApplicationHook.requestString("com.alipay.wealthgoldtwa.goldbill.v2.index.collect", args);
    }
}
