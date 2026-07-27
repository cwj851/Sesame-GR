package io.github.lazyimmortal.sesame.model.task.immortal;

import io.github.lazyimmortal.sesame.data.RuntimeInfo;
import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.RandomUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class AntFishPondRpcCall {

    // 进入就弹出来的请求，不知道干嘛的
    // 原来是兑换红包
    public static String fishpondExchangeReward() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"source\":\"farmpool\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.fishpondExchangeReward", args);
    }

    public static String fishpondIndex() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"source\":\"farmpool\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.fishpondIndex", args);
    }

    // 开通鱼塘
    public static String fishpondSimple() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"source\":\"ANTFARM_ORCHARD\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.fishpondSimple", args);
    }

    /**
     * 查询奖活动状态
     * activityType : [] 查询所有
     * activityType : [GIFT_BOX] 只查询宝箱
     * @return String
     */
    public static String querySubplotsActivity() {
        String args = "[{\"activityType\":[],\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"source\":\"farmpool\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.querySubplotsActivity", args);
    }

    /**
     * 领取活动奖励
     * @param actionType 行动类型
     *                   FINISH: 完成任务
     *                   receiveAward: 领取奖励
     * @param activityType 活动类型
     *                     FISH_ACTIVITY : 垂钓奖励
     *                     GIFT_BOX : 每日宝箱
     * @return String
     */
    public static String triggerSubplotsActivity(String actionType, String activityType) {
        String args = "[{\"actionType\":\"" + actionType + "\",\"activityType\":\"" + activityType + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"source\":\"farmpool\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.triggerSubplotsActivity", args);
    }

    // 钓鱼begin
    public static String fishpondAngle() {
        String bizNo = RuntimeInfo.getInstance().getString(RuntimeInfo.RuntimeInfoKey.FishPondAngleBizNo);
        String riskToken = RuntimeInfo.getInstance().getString(RuntimeInfo.RuntimeInfoKey.FishPondAngleRiskToken);
        String args = "[{\"bizNo\":\"" + bizNo + "\",\"requestType\":\"NORMAL\",\"riskToken\":\"" + riskToken + "\",\"sceneCode\":\"GameCenter\",\"source\":\"farmpool\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.fishpondAngle", args);
    }

    public static String fishpondAngleUserCheck(String bizNo, Boolean obtain) {
        String args = "[{\"bizNo\":\"" + bizNo + "\",\"obtain\":" + obtain + ",\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"source\":\"farmpool\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.fishpondAngleUserCheck", args);
    }

    /**
     * 垂钓福利鱼
     * @param areaType 区域类型
     *                 BIG_ZONE 大鱼开场(0, 5253.73)
     *                 SUPER_BIG_ZONE 超大鱼入场(5253.73, 8686.56)
     *                 EASTER_EGG_ZONE 复活节彩蛋专场?(9332, 9333.33)
     *                 SPECIAL_BIG_ZONE 特大鱼开场(8686.56, 1000)
     * @param bizNo bizNo
     * @return String
     */
    public static String fishpondAngleRodPositioning(String areaType, String bizNo) {
        String args = "[{\"areaType\":\"" + areaType + "\",\"bizNo\":\"" + bizNo + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"source\":\"farmpool\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.fishpondAngleRodPositioning", args);
    }

    public static String fishpondSyncIndex() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"source\":\"farmpool\",\"syncTypeList\":[\"FISH_ACTIVITY\",\"TASK_DISPLAY\",\"TOMORROW_ROD\",\"LOTTERY_PLUS\",\"AD_INFO\"]}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.fishpondSyncIndex", args);
    }
    // 钓鱼end

    // 广告通知
    public static String fishpondAdNotice(String adBizNo) {
        String args = "[{\"adBizNo\":\"" + adBizNo + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"source\":\"farmpool\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.fishpondAdNotice", args);
    }

    // 任务列表
    public static String listTask() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"source\":\"farmpool\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.listTask", args);
    }
    // 完成任务
    public static String finishTask(String sceneCode, String taskType) {
        String outBizNo = UserIdMap.getCurrentUid() + System.currentTimeMillis();
        String args = "[{\"outBizNo\":\"" + outBizNo + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"" + sceneCode + "\",\"source\":\"farmpool\",\"taskType\":\"" + taskType + "\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.finishTask", args);
    }
    // 完成广告任务
    public static String finishTask(String sceneCode, String taskType, String adBizNo) {
        String outBizNo = taskType + "_" + System.currentTimeMillis() + "_" + RandomUtil.getRandomString(8);
        String args = "[{\"finishBusinessInfo\":{\"pwPreBizId\":\"" + adBizNo + "\"},\"outBizNo\":\"" + outBizNo + "\",\"requestType\":\"RPC\",\"sceneCode\":\"" + sceneCode + "\",\"source\":\"ADBASICLIB\",\"taskType\":\"" + taskType + "\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.finishTask", args);
    }
    public static String taskFinish(String bizId) {
        String args = "[{\"bizId\":\"" + bizId + "\"}]";
        return ApplicationHook.requestString("com.alipay.adtask.biz.mobilegw.service.task.finish", args);
    }
    // 领取任务奖励
    public static String receiveTaskAward(String taskType) {
        String args = "[{\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFISHPOND_TASK\",\"source\":\"farmpool\",\"taskType\":\"" + taskType + "\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.receiveTaskAward", args);
    }
    // 签到
    public static String sign(String signKey) {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"GameCenter\",\"signKey\":\"" + signKey + "\",\"source\":\"farmpool\"}]";
        return ApplicationHook.requestString("com.alipay.antfishpond.sign", args);
    }
}
