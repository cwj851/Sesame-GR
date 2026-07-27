package io.github.lazyimmortal.sesame.model.task.immortal;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.RandomUtil;

public class AntForestAlphaRpcCall {
    public static String itemList() {
        return ApplicationHook.requestString("com.alipay.antiep.itemList",
                "[{\"extendInfo\":\"{}\",\"fromSpuId\":\"\",\"labelType\":\"\",\"pageSize\":10,\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"source\":\"afEntry\",\"startIndex\":0}]");
    }

    public static String secKill() {
        String args = "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"secKillId\":\"ANTFOREST_VITALITY_MALL_SEC_KILL\",\"source\":\"afEntry\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.seckill", args);
    }

    public static String exchangeSkillBenefit(String spuId, String skuId, String requestId) {
        String args = "[{\"requestId\":\"" + requestId + "\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"skuId\":\"" + skuId + "\",\"source\":\"GOOD_DETAIL\",\"spuId\":\"" + spuId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antcommonweal.exchange.h5.exchangeSkillBenefit", args);
    }

    public static String exchangeSkillBenefit(String spuId, String skuId) {
        String requestId = System.currentTimeMillis() + "_" + RandomUtil.getRandom(16);
        return exchangeSkillBenefit(spuId, skuId, requestId);
    }

    public static String exchangeBenefit(String spuId, String skuId, String requestId) {
        String args = "[{\"requestId\":\"" + requestId + "\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"skuId\":\"" + skuId + "\",\"source\":\"GOOD_DETAIL\",\"spuId\":\"" + spuId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antcommonweal.exchange.h5.exchangeBenefit", args);
    }

    public static String exchangeBenefit(String spuId, String skuId) {
        String requestId = System.currentTimeMillis() + "_" + RandomUtil.getRandom(16);
        return exchangeBenefit(spuId, skuId, requestId);
    }

    // 查询活力值商店类别
    public static String itemList(String labelType, int startIndex) {
        String args = "[{\"extendInfo\":\"{}\",\"labelType\":\"" + labelType + "\",\"pageSize\":20,\"requestType\":\"rpc\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"source\":\"afEntry\",\"startIndex\":" + startIndex + "}]";
        return ApplicationHook.requestString("com.alipay.antiep.itemList", args);
    }
}
