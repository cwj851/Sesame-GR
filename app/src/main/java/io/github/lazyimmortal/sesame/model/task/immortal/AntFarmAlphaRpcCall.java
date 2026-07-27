package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;

public class AntFarmAlphaRpcCall {
    public static String doFarmTask(String bizKey) {
        String args = "[{\"bizKey\":\"" + bizKey + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.doFarmTask", args);
    }

    public static String doFarmDrawTimesTask(String bizKey) {
        String args = "[{\"bizKey\":\"" + bizKey + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_DRAW_TIMES_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.doFarmTask", args);
    }

    // 抽抽乐 限定IP
    public static String listFarmIpDrawTask() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"signSceneCode\":\"\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_IP_DRAW_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.listFarmTask", args);
    }

    public static String doFarmIpDrawTask(String bizKey) {
        String args = "[{\"bizKey\":\"" + bizKey + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_IP_DRAW_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.doFarmTask", args);
    }

    public static String receiveFarmIpDrawTaskAward(String taskId) {
        String args = "[{\"awardType\":\"IP_DRAW_MACHINE_DRAW_TIMES\",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"icon\",\"taskId\":\"" + taskId + "\",\"taskSceneCode\":\"ANTFARM_IP_DRAW_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.receiveFarmTaskAward", args);
    }

    // 家庭任务提示
    public static String familyTaskTips(JSONArray animals) {
        String args = "[{\"animals\":" + animals + ",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_FAMILY_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.familyTaskTips", args);
    }
    public static String listFamilyTask() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.listFamilyTask", args);
    }
    public static String listFarmFamilyTask() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"signSceneCode\":\"\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_FAMILY_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.listFarmTask", args);
    }

    public static String doFarmFamilyTask(String bizKey) {
        String args = "[{\"bizKey\":\"" + bizKey + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_FAMILY_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.doFarmTask", args);
    }

    public static String receiveFarmFamilyTaskAward(String taskId) {
        String args = "[{\"awardType\":\"FAMILY_INTIMACY\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskId\":\"" + taskId + "\",\"taskSceneCode\":\"ANTFARM_FAMILY_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.receiveFarmTaskAward", args);
    }

    public static String feedAnimal(String farmId, String source) {
        String args = "[{\"farmId\":\"" + farmId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"" + source + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.feedAnimal", args);
    }

    /**
     * 帮喂小鸡
     * @param friendFarmId farmId
     * @param source H5 visitChicken(到访小鸡:免费投喂)
     * @return requestString
     */
    public static String feedFriendAnimal(String friendFarmId, String source) {
        String args = "[{\"friendFarmId\":\"" + friendFarmId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"" + source + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.feedFriendAnimal", args);
    }

    public static String familyFeedFriendAnimal(String groupId, String friendFarmId) {
        String args = "[{\"friendFarmId\":\"" + friendFarmId + "\",\"groupId\":\"" + groupId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ChickFamily\",\"source\":\"H5\",\"spaceType\":\"ChickFamily\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.feedFriendAnimal", args);
    }

    /**
     * 雇佣小鸡
     * @param farmId farmId
     * @param animalId animalId
     * @param source H5 visitChicken(到访小鸡:免费雇佣)
     * @return requestString
     */
    public static String hireAnimal(String farmId, String animalId, String source) {
        String args = "[{\"friendFarmId\":\"" + farmId + "\",\"hireActionType\":\"HIRE_IN_FRIEND_FARM\",\"hireAnimalId\":\"" + animalId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"" + source + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.hireAnimal", args);
    }
}
