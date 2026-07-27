package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONObject;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;

public class AntStallAlphaRpcCall {
    public static String xlightPlugin() {
        String args = "[{\"positionRequest\":{\"extMap\":{},\"referInfo\":{},\"searchInfo\":{},\"spaceCode\":\"ANT_FARM_NEW_VILLAGE\"},\"sdkPageInfo\":{\"adComponentType\":\"FEEDS\",\"adComponentVersion\":\"4.19.7\",\"enableFusion\":true,\"networkType\":\"WIFI\",\"pageFrom\":\"ch_url-https://68687809.h5app.alipay.com/www/game.html\",\"pageNo\":1,\"pageUrl\":\"https://render.alipay.com/p/yuyan/180020010001256918/multi-stage-task.html?caprMode=sync&spaceCodeFeeds=ANT_FARM_NEW_VILLAGE&usePlayLink=true\",\"session\":\"u_8becbe5e5396c_1926d9b8b05\",\"unionAppId\":\"2060090000304921\",\"usePlayLink\":\"true\",\"xlightSDKType\":\"h5\",\"xlightSDKVersion\":\"4.19.7\"}}]";
        return ApplicationHook.requestString("com.alipay.adexchange.ad.facade.xlightPlugin", args);
    }

    public static String finish(String iepTaskSceneCode, String iepTaskType, String playBizId, JSONObject playEventInfo) {
        String args = "[{\"extendInfo\":{\"iepTaskSceneCode\":\"" + iepTaskSceneCode + "\",\"iepTaskType\":\"" + iepTaskType + "\"},\"playBizId\":\"" + playBizId + "\",\"playEventInfo\":" + playEventInfo + ",\"source\":\"adx\"}]";
        return ApplicationHook.requestString("com.alipay.adtask.biz.mobilegw.service.interaction.finish", args);
    }

    public static String finishTask(String taskType) {
        String outBizNo = taskType + "_" + System.currentTimeMillis();
        String args = "[{\"outBizNo\":\"" + outBizNo + "\",\"requestType\":\"RPC\",\"sceneCode\":\"ANTSTALL_TASK\",\"source\":\"AST\",\"taskType\":\"" + taskType + "\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.finishTask", args);
    }
}
