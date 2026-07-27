package io.github.lazyimmortal.sesame.model.task.antMember;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;

public class GameCenterRpcCall {
    /**
     * 游戏中心签到查询
     */
    public static String querySignInBall() {
        return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.querySignInBall", "[{}]");
    }

    /**
     * 游戏中心签到
     */
    public static String continueSignIn() {
        String args = "[{\"sceneId\":\"GAME_CENTER\",\"signType\":\"NORMAL_SIGN\"}]";
        return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.continueSignIn", args);
    }

    /**
     * 游戏中心查询待领取乐豆列表
     */
    public static String queryPointBallList() {
        return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.queryPointBallList", "[{}]");
    }

    /**
     * 领取玩乐豆
     */
    @Deprecated
    public static String receivePointBall(String ballId) {
        String args = "[{\"ballId\":\"" + ballId + "\"}]";
        return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.receivePointBall", args);
    }

    /**
     * 批量领取玩乐豆
     */
    public static String batchReceivePointBall() {
        return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.batchReceivePointBall", "[{}]");
    }

    /**
     * 查询任务列表
     */
    public static String queryModularTaskList() {
        String args = "[{\"deviceLevel\":\"high\",\"source\":\"ch_appcollect__chsub_my-recentlyUsed\",\"sourceTab\":\"luckydraw\",\"unityDeviceLevel\":\"high\"}]";
        return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.queryModularTaskList", args);
    }

    /**
     * 注册任务
     */
    public static String doTaskSignup(String taskId) {
        String args = "[{\"taskId\":\"" + taskId + "\"}]";
        return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.doTaskSignup", args);
    }

    public static String doTaskSend(String taskId) {
        String args = "[{\"taskId\":\"" + taskId + "\"}]";
        return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.doTaskSend", args);
    }

    // 秒玩游戏
    public static String queryGameAggCard(String source) {
        String args = "[{\"appearedCardIds\":[],\"deviceLevel\":\"high\",\"pageSize\":6,\"pageStart\":1,\"source\":\"" + source + "\",\"topGameId\":\"\",\"trafficDriverId\":\"" + source + "\",\"unityDeviceLevel\":\"high\"}]";
        return ApplicationHook.requestString("com.alipay.gamecenterhome.biz.rpc.queryGameAggCard", args);
    }

    public static String consultFloatingBall(String gameId, String gameModuleId) {
        String args = "[{\"gameId\":\"" + gameId + "\",\"gameModuleId\":\"" + gameModuleId + "\",\"source\":\"" + gameModuleId + "\",\"trafficDriverId\":\"" + gameModuleId + "\"}]";
        return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.floatingball.consult", args);
    }
}
