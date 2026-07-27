package io.github.lazyimmortal.sesame.model.task.antForest;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.RandomUtil;

public class OpenGreenRpcCall {
    /**
     * 获取任务列表
     */
    public static String listTask(String sceneCode) {
        String args = "[{\"requestType\":\"RPC\",\"sceneCode\":\""+sceneCode+"_TASK\",\"source\":\"guide\"}]";
        return ApplicationHook.requestString("com.alipay.antieptask.listTaskopengreen", args);
    }



    /**
     * 领取任务奖励
     *
     * @param taskType 任务类型
     */
    public static String receiveTaskAward(String taskType,String sceneCode) {
        String args = "[{\"ignoreLimit\":true,\"requestType\":\"RPC\",\"sceneCode\":\""+sceneCode+"\",\"source\":\"guide\",\"taskType\":\"" + taskType + "\"}]";
        return ApplicationHook.requestString("com.alipay.antieptask.receiveTaskAwardopengreen", args);
    }


    /**
     * 兑换抽奖次数
     *
     * @param activityId 活动ID
     * @param taskType   任务类型
     */
    public static String exchangeTimesFromTask(String activityId, String taskType) {
        String args = "[{\"activityId\":\"" + activityId + "\",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFOREST_NORMAL_DRAW\",\"source\":\"guide\",\"taskSceneCode\":\"ANTFOREST_NORMAL_DRAW_TASK\",\"taskType\":\"" + taskType + "\"}]";
        return ApplicationHook.requestString("com.alipay.antiepdrawprod.exchangeTimesFromTaskopengreen", args);
    }

    /**
     * 邀请好友助力
     *
     * @param beInvitedUserId 要邀请的好友ID
     * @param sceneCode       任务类型
     */
    public static String batchInviteP2P(String beInvitedUserId, String sceneCode) {
        String args = "[{\"inviteP2PVOList\":[{\"beInvitedUserId\":\"" + beInvitedUserId + "\"}],\"requestType\":\"RPC\",\"sceneCode\":\"" + sceneCode + "\",\"source\":\"antforest\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.batchInviteP2P", args);
    }


    /**
     * 完成任务
     *
     * @param taskType 任务类型
     */
    public static String finishTask(String taskType,String sceneCode) {
        String outBizNo = taskType + "_" + System.currentTimeMillis() + "_" + RandomUtil.getRandomString(8);
        String args = "[{\"outBizNo\":\"" + outBizNo + "\",\"requestType\":\"RPC\",\"sceneCode\":\""+sceneCode+"\",\"source\":\"ADBASICLIB\",\"taskType\":\"" + taskType + "\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.finishTask", args);
    }

    /**
     * 进入抽奖活动
     *
     * @param activityId 活动ID
     */
    public static String enterDrawActivity(String activityId,String sceneCode) {
        String args = "[{\"activityId\":\"" + activityId + "\",\"requestType\":\"RPC\",\"sceneCode\":\""+sceneCode+"\",\"source\":\"guide\"}]";
        return ApplicationHook.requestString("com.alipay.antiepdrawprod.enterDrawActivityopengreen", args);
    }

    /**
     * 抽奖
     *
     * @param activityId 活动Id
     * @param userId     用户ID
     */
    public static String draw(String activityId, String userId,String sceneCode) {
        String args = "[{\"activityId\":\"" + activityId + "\",\"requestType\":\"RPC\",\"sceneCode\":\""+sceneCode+"\",\"source\":\"guide\",\"userId\":\"" + userId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antiepdrawprod.drawopengreen", args);
    }
}
