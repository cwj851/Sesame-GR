package io.github.lazyimmortal.sesame.model.task.immortal;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.RandomUtil;

public class AntForestAlphaRpcCall {
    public static String secKill() {
        String args = "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"secKillId\":\"ANTFOREST_VITALITY_MALL_SEC_KILL\",\"source\":\"afEntry\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.seckill", args);
    }

    public static String exchangeBenefit(String spuId, String skuId, String requestId) {
        String args = "[{\"requestId\":\"" + requestId + "\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"skuId\":\"" + skuId + "\",\"source\":\"GOOD_DETAIL\",\"spuId\":\"" + spuId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antcommonweal.exchange.h5.exchangeSkillBenefit", args);
    }

    public static String exchangeBenefit(String spuId, String skuId) {
        String requestId = System.currentTimeMillis() + "_" + RandomUtil.getRandom(16);
        return exchangeBenefit(spuId, skuId, requestId);
    }
}
