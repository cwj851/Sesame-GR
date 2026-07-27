package io.github.lazyimmortal.sesame.model.task.antMember;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.RandomUtil;

public class WelfarePlusRpcCall {

    public static String listQuery(String args) {
        return ApplicationHook.requestString("alipay.promoprod.task.listQuery", args);
    }
    // 天天领 快递专属福利
    public static String queryRecommendTask() {
        String args = "[{\"consultAccessFlag\":true,\"extInfo\":{\"componentCode\":\"musi_test\"},\"taskCenInfo\":\"MZVPQ0DScvD6NjaPJzk8iCCWtq%2FRt4kh\"}]";
        return listQuery(args);
    }

    // 更多任务
    public static String queryOrdinaryTask() {
        String args = "[{\"consultAccessFlag\":true,\"taskCenInfo\":\"MZVPQ0DScvD6NjaPJzk8iNRgSSvWpCuA\"}]";
        return listQuery(args);
    }

    // 森林活力值
    public static String queryAntForestHomePage() {
        String args1 = "[{\"activityParam\":{},\"configVersionMap\":{\"wateringBubbleConfig\":\"0\"},\"skipWhackMole\":false,\"source\":\"kuaidivitality\",\"version\":\"20240606\"}]";
        return ApplicationHook.requestString("alipay.antforest.forest.h5.queryHomePage", args1);
    }

    public static String queryAntForestTaskList() {
        String args = "[{\"extend\":{\"firstTaskType\":\"KUAIDI_VITALITY\"},\"fromAct\":\"home_task_list\",\"source\":\"kuaidivitality\",\"version\":\"20240105\"}]";
        return ApplicationHook.requestString("alipay.antforest.forest.h5.queryTaskList", args);
    }

    public static String receiveAntForestTaskAward() {
        String args = "[{\"ignoreLimit\":false,\"requestType\":\"H5\",\"sceneCode\":\"ANTFOREST_VITALITY_TASK\",\"source\":\"ANTFOREST\",\"taskType\":\"KUAIDI_VITALITY\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.receiveTaskAward", args);
    }

    // 海洋碎片
    public static String queryAntOceanHomePage() {
        String args = "[{\"firstTaskType\":\"DAOLIU_WODEKUAIDIQUANYI\",\"source\":\"wodekuaidiquanyi\",\"uniqueId\":\"" + getUniqueId() + "\",\"version\":\"20240115\"}]";
        return ApplicationHook.requestString("alipay.antocean.ocean.h5.queryHomePage", args);
    }

    public static String queryAntOceanTaskList() {
        String args = "[{\"extend\":{\"firstTaskType\":\"DAOLIU_WODEKUAIDIQUANYI\"},\"fromAct\":\"dynamic_task\",\"sceneCode\":\"ANTOCEAN_TASK\",\"source\":\"wodekuaidiquanyi\",\"uniqueId\":\"" + getUniqueId() + "\",\"version\":\"20240115\"}]";
        return ApplicationHook.requestString("alipay.antocean.ocean.h5.queryTaskList", args);
    }

    public static String receiveAntOceanTaskAward() {
        String args = "[{\"ignoreLimit\":false,\"requestType\":\"RPC\",\"sceneCode\":\"ANTOCEAN_TASK\",\"source\":\"ANT_FOREST\",\"taskType\":\"DAOLIU_WODEKUAIDIQUANYI\",\"uniqueId\":\"" + getUniqueId() + "\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.receiveTaskAward", args);
    }

    private static String getUniqueId() {
        return String.valueOf(System.currentTimeMillis()) + RandomUtil.nextLong();
    }

    // 任务触发
    public static String trigger(String appletId, String taskCenInfo, String stageCode) {
        String args = "[{\"appletId\":\"" + appletId + "\",\"stageCode\":\"" + stageCode + "\",\"taskCenInfo\":\"" + taskCenInfo + "\"}]";
        return ApplicationHook.requestString("alipay.promoprod.applet.trigger", args);
    }

    public static String sendTrigger(String appletId, String taskCenInfo) {
        return trigger(appletId, taskCenInfo, "send");
    }

    public static String signupTrigger(String appletId, String taskCenInfo) {
        return trigger(appletId, taskCenInfo, "signup");
    }
}
