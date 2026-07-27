package io.github.lazyimmortal.sesame.model.task.antForest;

import org.json.JSONArray;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;

public class ForestSquareRpcCall {
    public static String queryEnergyRainHome() {
        return ApplicationHook.requestString("alipay.antforest.forest.h5.queryEnergyRainHome", "[{}]");
    }

    public static String queryEnergyRainCanGrantList() {
        return ApplicationHook.requestString("alipay.antforest.forest.h5.queryEnergyRainCanGrantList", "[{}]");
    }

    public static String grantEnergyRainChance(String targetUserId) {
        return ApplicationHook.requestString("alipay.antforest.forest.h5.grantEnergyRainChance",
                "[{\"targetUserId\":" + targetUserId + "}]");
    }

    public static String startEnergyRain() {
        return ApplicationHook.requestString("alipay.antforest.forest.h5.startEnergyRain", "[{}]");
    }

    public static String energyRainSettlement(int saveEnergy, String token) {
        return ApplicationHook.requestString("alipay.antforest.forest.h5.energyRainSettlement",
                "[{\"activityPropNums\":0,\"saveEnergy\":" + saveEnergy + ",\"token\":\"" + token + "\"}]");
    }

    /* 6秒拼手速 打地鼠 */
    public static String startWhackMole() {
        String args = "[{\"source\":\"senlinguangchangdadishu\"}]";
        return ApplicationHook.requestString("alipay.antforest.forest.h5.startWhackMole", args);
    }

    public static String settlementWhackMole(JSONArray moleIdList, String token) {
        String args = "[{\"moleIdList\":" + moleIdList + ",\"settlementScene\":\"NORMAL\",\"source\":\"senlinguangchangdadishu\",\"token\":\"" + token + "\"}]";
        return ApplicationHook.requestString("alipay.antforest.forest.h5.settlementWhackMole", args);
    }

    public static String closeWhackMole() {
        return ApplicationHook.requestString("alipay.antforest.forest.h5.updateUserConfig", "[{\"configMap\":{\"whackMole\":\"N\"},\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]");
    }
}
