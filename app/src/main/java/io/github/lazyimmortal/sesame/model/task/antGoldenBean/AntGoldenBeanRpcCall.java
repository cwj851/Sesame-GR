package io.github.lazyimmortal.sesame.model.task.antGoldenBean;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class AntGoldenBeanRpcCall {

    private static final String VERSION = "20260723.01";
    private static final String SOURCE = "babafarm";
    private static final String BIZ_TYPE = "MASTER";
    private static final String SCENE_CODE = "GOLDEN_BEAN_MASTER_TASK";

    public static String goldenBeanIndex() {
        return ApplicationHook.requestString("com.alipay.goldenbean.index",
                "[{\"bizType\":\"" + BIZ_TYPE + "\",\"darwinSceneList\":[],\"source\":\"" + SOURCE + "\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String goldenBeanSign(String signKey) {
        return ApplicationHook.requestString("com.alipay.goldenbean.sign",
                "[{\"bizType\":\"" + BIZ_TYPE + "\",\"signKey\":\"" + signKey + "\",\"source\":\"" + SOURCE + "\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String goldenBeanSync(String... syncTypeList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < syncTypeList.length; i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(syncTypeList[i]).append("\"");
        }
        return ApplicationHook.requestString("com.alipay.goldenbean.sync",
                "[{\"bizType\":\"" + BIZ_TYPE + "\",\"source\":\"" + SOURCE + "\",\"syncTypeList\":[" + sb.toString() + "],\"version\":\"" + VERSION + "\"}]");
    }

    public static String goldenBeanManureExchange(int exchangeBeanAmount) {
        return ApplicationHook.requestString("com.alipay.goldenbean.manureExchange",
                "[{\"bizType\":\"" + BIZ_TYPE + "\",\"exchangeBeanAmount\":" + exchangeBeanAmount + ",\"source\":\"" + SOURCE + "\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String finishTaskantorchard(String taskType) {
        String userId = UserIdMap.getCurrentUid();
        return ApplicationHook.requestString("com.alipay.antieptask.finishTaskantorchard",
                "[{\"bizType\":\"" + BIZ_TYPE + "\",\"finishBusinessInfo\":{\"bizType\":\"" + BIZ_TYPE + "\"},\"outBizNo\":\"" + userId + System.currentTimeMillis() + "\",\"sceneCode\":\"" + SCENE_CODE + "\",\"source\":\"" + SOURCE + "\",\"taskType\":\"" + taskType + "\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String receiveTaskAwardantorchard(String taskType) {
        return ApplicationHook.requestString("com.alipay.antieptask.receiveTaskAwardantorchard",
                "[{\"bizInfo\":{\"bizType\":\"" + BIZ_TYPE + "\"},\"bizType\":\"" + BIZ_TYPE + "\",\"ignoreLimit\":true,\"sceneCode\":\"" + SCENE_CODE + "\",\"source\":\"" + SOURCE + "\",\"taskType\":\"" + taskType + "\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String queryGameList() {
        return ApplicationHook.requestString("com.alipay.charitygamecenter.queryGameList",
                "[{\"bizType\":\"GOLDENBEAN\",\"commonDegradeFilterRequest\":{\"deviceLevel\":\"high\",\"platform\":\"Android\",\"unityDeviceLevel\":\"high\"},\"recentAppRecordList\":[],\"requestType\":\"RPC\",\"sceneCode\":\"GOLDENBEAN\",\"source\":\"" + SOURCE + "\",\"version\":\"10.6.58.8000\"}]");
    }

    public static String drawGameCenterAward(int batchDrawCount) {
        return ApplicationHook.requestString("com.alipay.charitygamecenter.drawGameCenterAward",
                "[{\"batchDrawCount\":" + batchDrawCount + ",\"bizType\":\"GOLDENBEAN\",\"requestType\":\"RPC\",\"sceneCode\":\"GOLDENBEAN\",\"source\":\"" + SOURCE + "\",\"version\":\"" + VERSION + "\"}]");
    }
}
