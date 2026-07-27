package io.github.lazyimmortal.sesame.model.task.antFarm;

import org.json.JSONArray;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.RandomUtil;
import io.github.lazyimmortal.sesame.util.StringUtil;

public class AntFarmRpcCall {
    private static final String VERSION = "1.8.2302070202.46";

    public static String enterFarm(String userId) {
        String args = "[{\"queryLastRecordNum\":true,\"recall\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"userId\":\"" + userId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.enterFarm", args);
    }

    public static String enterFarm(String farmId, String userId) {
        return ApplicationHook.requestString("com.alipay.antfarm.enterFarm",
                "[{\"animalId\":\"\",\"farmId\":\"" + farmId +
                        "\",\"gotoneScene\":\"\",\"gotoneTemplateId\":\"\",\"masterFarmId\":\"\",\"queryLastRecordNum\":true,\"recall\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"ANTFOREST\",\"touchRecordId\":\"\",\"userId\":\""
                        + userId + "\",\"version\":\"" + VERSION + "\"}]");
    }
    // 一起拿小鸡饲料
    public static String letsGetChickenFeedTogether() {
        String args1 = "[{\"needHasInviteUserByCycle\":\"true\",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM_P2P\",\"source\":\"ANTFARM\",\"startIndex\":0," +
                "\"version\":\""+VERSION+"\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.canInvitePersonListP2P", args1);
    }
    // 赠送饲料
    public static String giftOfFeed(String bizTraceId,String userId) {
        String args1 = "[{\"beInvitedUserId\":\"" + userId +
                "\",\"bizTraceId\":\"" + bizTraceId +
                "\",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM_P2P\"," +
                "\"source\":\"ANTFARM\",\"version\":\"" + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antiep.inviteP2P", args1);
    }
    public static String syncAnimalStatus(String farmId) {
        String args = "[{\"farmId\":\"" + farmId + "\",\"operTag\":\"SYNC_RESUME\",\"operType\":\"QUERY_ALL\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.syncAnimalStatus", args);
    }

    public static String sleep(String groupId) {
        String args;
        if (StringUtil.isEmpty(groupId)) {
            args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"LOVECABIN\"}]";
        } else {

            args = "[{\"groupId\":\"" + groupId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"spaceType\":\"ChickFamily\"}]";
        }
        return ApplicationHook.requestString("com.alipay.antfarm.sleep", args);
    }

    public static String wakeUp(boolean isSleepInFamily) {
        String args;
        if (isSleepInFamily) {
            args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"aixinxiaowutojiating\"}]";
        } else {
            args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"LOVECABIN\"}]";
        }
        return ApplicationHook.requestString("com.alipay.antfarm.wakeUp", args);
    }

    public static String queryLoveCabin(String userId) {
        String args = "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"LOVECABIN\",\"userId\":\"" + userId + "\"}]";
//        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"ENTERFARM\",\"userId\":\"" + userId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.queryLoveCabin", args);
    }

    public static String rewardFriend(String consistencyKey, String friendId, String productNum, String time) {
        String args1 = "[{\"canMock\":true,\"consistencyKey\":\"" + consistencyKey
                + "\",\"friendId\":\"" + friendId + "\",\"operType\":\"1\",\"productNum\":" + productNum +
                ",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"time\":"
                + time + ",\"version\":\"" + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.rewardFriend", args1);
    }

    public static String recallAnimal(String animalId, String currentFarmId, String masterFarmId) {
        String args1 = "[{\"animalId\":\"" + animalId + "\",\"currentFarmId\":\""
                + currentFarmId + "\",\"masterFarmId\":\"" + masterFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.recallAnimal", args1);
    }

    public static String orchardRecallAnimal(String animalId, String userId) {
        String args1 = "[{\"animalId\":\"" + animalId + "\",\"orchardUserId\":\"" + userId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"zhuangyuan_zhaohuixiaoji\",\"version\":\"0.1.2403061630.6\"}]";
        return ApplicationHook.requestString("com.alipay.antorchard.recallAnimal", args1);
    }

    public static String sendBackAnimal(String sendType, String animalId, String currentFarmId, String masterFarmId) {
        String args1 = "[{\"animalId\":\"" + animalId + "\",\"currentFarmId\":\""
                + currentFarmId + "\",\"masterFarmId\":\"" + masterFarmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"sendType\":\""
                + sendType + "\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.sendBackAnimal", args1);
    }

    public static String harvestProduce(String farmId) {
        String args1 = "[{\"canMock\":true,\"farmId\":\"" + farmId +
                "\",\"giftType\":\"\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.harvestProduce", args1);
    }

    public static String getCharityAccount(String userId) {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"userId\":\"" + userId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.getCharityAccount", args);
    }

    public static String listActivityInfo() {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.listActivityInfo", args1);
    }

    public static String getProjectInfo(String projectId) {
        String args = "[{\"activityId\":\"\",\"projectId\":\"" + projectId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"ANTFARM\",\"version\":\"" + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.getProjectInfo", args);
    }

    public static String donation(String activityId, int donationAmount) {
        String args1 = "[{\"activityId\":\"" + activityId + "\",\"donationAmount\":" + donationAmount +
                ",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.donation", args1);
    }

    public static String listFarmTask() {
        String args1 = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.listFarmTask", args1);
    }

    public static String getAnswerInfo() {
        String args1 = "[{\"answerSource\":\"foodTask\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.getAnswerInfo", args1);
    }

    public static String answerQuestion(String quesId, int answer) {
        String args1 = "[{\"answers\":\"[{\\\"questionId\\\":\\\"" + quesId + "\\\",\\\"answers\\\":[" + answer +
                "]}]\",\"bizkey\":\"ANSWER\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.doFarmTask", args1);
    }

    public static String receiveFarmTaskAward(String taskId) {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskId\":\"" + taskId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.receiveFarmTaskAward", args);
    }

    public static String listToolTaskDetails() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.listToolTaskDetails", args);
    }

    public static String receiveToolTaskReward(String rewardType, int rewardCount, String taskType) {
        String args1 = "[{\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"rewardCount\":" + rewardCount
                + ",\"rewardType\":\"" + rewardType + "\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskType\":\""
                + taskType + "\",\"version\":\"" + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.receiveToolTaskReward", args1);
    }

    public static String feedAnimal(String farmId) {
        String args1 = "[{\"animalType\":\"CHICK\",\"canMock\":true,\"farmId\":\"" + farmId +
                "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.feedAnimal", args1);
    }

    public static String listFarmTool() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.listFarmTool", args);
    }

    public static String useFarmTool(String targetFarmId, String toolId, String toolType) {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"targetFarmId\":\"" + targetFarmId + "\",\"toolId\":\"" + toolId + "\",\"toolType\":\"" + toolType + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.useFarmTool", args);
    }

    public static String rankingList(int pageStartSum) {
        String args1 = "[{\"pageSize\":20,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"startNum\":" + pageStartSum + "}]";
        return ApplicationHook.requestString("com.alipay.antfarm.rankingList", args1);
    }

    public static String notifyFriend(String animalId, String notifiedFarmId) {
        String args = "[{\"animalId\":\"" + animalId + "\",\"animalType\":\"CHICK\",\"canBeGuest\":true,\"notifiedFarmId\":\""
                + notifiedFarmId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.notifyFriend", args);
    }

    public static String feedFriendAnimal(String friendFarmId, String groupId) {
        String args;
        if (StringUtil.isEmpty(groupId)) {
            args = "[{\"friendFarmId\":\"" + friendFarmId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        } else {
            args = "[{\"friendFarmId\":\"" + friendFarmId + "\",\"groupId\":\"" + groupId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ChickFamily\",\"source\":\"H5\",\"spaceType\":\"ChickFamily\"}]";
        }
        return ApplicationHook.requestString("com.alipay.antfarm.feedFriendAnimal", args);
    }

    public static String farmId2UserId(String farmId) {
        int beginIndex = farmId.length() / 2;
        return farmId.substring(beginIndex);
    }

    public static String collectManurePot(String manurePotNO) {
        String args = "[{\"manurePotNOs\":\"" + manurePotNO + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.collectManurePot", args);
    }

    public static String sign() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.sign", args);
    }

    public static String initFarmGame(String gameType) {
        String args = "[{\"gameType\":\"" + gameType + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"toolTypes\":\"STEALTOOL,ACCELERATETOOL,SHARETOOL\"}]";
        if ("flyGame".equals(gameType)) {
            args = "[{\"gameType\":\"flyGame\",\"requestType\":\"RPC\",\"sceneCode\":\"FLAYGAME\",\"source\":\"FARM_game_yundongfly\",\"toolTypes\":\"ACCELERATETOOL,SHARETOOL,NONE\"}]";
        }
        return ApplicationHook.requestString("com.alipay.antfarm.initFarmGame", args);
    }

    public static int RandomScore(String str) {
        if ("starGame".equals(str)) {
            return RandomUtil.nextInt(300, 400);
        } else if ("jumpGame".equals(str)) {
            return RandomUtil.nextInt(250, 270) * 10;
        } else if ("flyGame".equals(str)) {
            return RandomUtil.nextInt(4000, 8000);
        } else if ("hitGame".equals(str)) {
            return RandomUtil.nextInt(80, 120);
        } else {
            return 210;
        }
    }

    public static String recordFarmGame(String gameType) {
        String uuid = getUuid();
        String md5String = getMD5(uuid);
        int score = RandomScore(gameType);
        if ("flyGame".equals(gameType)) {
            int foodCount = score / 50;
            return ApplicationHook.requestString("com.alipay.antfarm.recordFarmGame",
                    "[{\"foodCount\":" + foodCount + ",\"gameType\":\"flyGame\",\"md5\":\"" + md5String
                            + "\",\"requestType\":\"RPC\",\"sceneCode\":\"FLAYGAME\",\"score\":" + score
                            + ",\"source\":\"ANTFARM\",\"toolTypes\":\"ACCELERATETOOL,SHARETOOL,NONE\",\"uuid\":\"" + uuid
                            + "\",\"version\":\"\"}]");
        }
        return ApplicationHook.requestString("com.alipay.antfarm.recordFarmGame",
                "[{\"gameType\":\"" + gameType + "\",\"md5\":\"" + md5String
                        + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"score\":" + score
                        + ",\"source\":\"H5\",\"toolTypes\":\"STEALTOOL,ACCELERATETOOL,SHARETOOL\",\"uuid\":\"" + uuid
                        + "\"}]");
    }

    private static String getUuid() {
        StringBuilder sb = new StringBuilder();
        for (String str : UUID.randomUUID().toString().split("-")) {
            sb.append(str.substring(str.length() / 2));
        }
        return sb.toString();
    }

    public static String getMD5(String password) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuilder buffer = new StringBuilder();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.printStackTrace(e);
            return "";
        }
    }

    public static String queryGameList() {
        String args = "[{\"commonDegradeResult\":{\"deviceLevel\":\"high\",\"resultReason\":0,\"resultType\":0},\"platform\":\"Android\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.queryGameList", args);
    }

    public static String drawGameCenterAward() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.drawGameCenterAward", args);
    }


    // 乐园集市
    public static String getMallHome() {
        String args = "[{\"bizType\":\"ANTFARM_GAME_CENTER\",\"pageSize\":10,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"startIndex\":0}]";
        return ApplicationHook.requestString("com.alipay.charitygamecenter.getMallHome", args);
    }

    public static String getMallItemDetail(String itemId) {
        String args = "[{\"bizType\":\"ANTFARM_GAME_CENTER\",\"itemId\":\"" + itemId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.charitygamecenter.getMallItemDetail", args);
    }

    public static String buyMallItem(String itemId, String subItemId) {
        return buyMallItem(itemId, subItemId, false);
    }

    public static String buyMallItem(String itemId, String subItemId, boolean ignoreHoldLimit) {
        String args = "[{\"bizType\":\"ANTFARM_GAME_CENTER\",\"ignoreHoldLimit\":" + ignoreHoldLimit + ",\"itemId\":\"" + itemId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"subItemId\":\"" + subItemId + "\"}]";
        return ApplicationHook.requestString("com.alipay.charitygamecenter.buyMallItem", args);
    }

    /* 小鸡厨房 */
    public static String enterKitchen(String userId) {
        String args = "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"userId\":\"" + userId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.enterKitchen", args);
    }

    public static String collectDailyFoodMaterial(int dailyFoodMaterialAmount) {
        String args = "[{\"collectDailyFoodMaterialAmount\":" + dailyFoodMaterialAmount + ",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.collectDailyFoodMaterial", args);
    }

    public static String queryFoodMaterialPack() {
        String args = "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"kitchen\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.queryFoodMaterialPack", args);
    }

    public static String collectDailyLimitedFoodMaterial(int dailyLimitedFoodMaterialAmount) {
        String args = "[{\"collectDailyLimitedFoodMaterialAmount\":" + dailyLimitedFoodMaterialAmount + ",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"kitchen\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.collectDailyLimitedFoodMaterial", args);
    }

    public static String farmFoodMaterialCollect() {
        String args = "[{\"collect\":true,\"requestType\":\"RPC\",\"sceneCode\":\"ORCHARD\",\"source\":\"VILLA\"}]";
        return ApplicationHook.requestString("com.alipay.antorchard.farmFoodMaterialCollect", args);
    }

    public static String cook(String userId) {
        String args = "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarmzuofanrw\",\"userId\":\"" + userId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.cook", args);
    }

    public static String useFarmFood(String cookbookId, String cuisineId) {
        return ApplicationHook.requestString("com.alipay.antfarm.useFarmFood",
                "[{\"cookbookId\":\"" + cookbookId + "\",\"cuisineId\":\"" + cuisineId
                        + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"useCuisine\":true,\"version\":\""
                        + VERSION + "\"}]");
    }

    public static String collectKitchenGarbage() {
        String args = "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"VILLA\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.collectKitchenGarbage", args);
    }

    /* 日常任务 */

    public static String queryTabVideoUrl() {
        return ApplicationHook.requestString("com.alipay.antfarm.queryTabVideoUrl",
                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION
                        + "\"}]");
    }

    public static String videoDeliverModule(String bizId) {
        return ApplicationHook.requestString("alipay.content.reading.life.deliver.module",
                "[{\"bizId\":\"" + bizId
                        + "\",\"bizType\":\"CONTENT\",\"chInfo\":\"ch_antFarm\",\"refer\":\"antFarm\",\"timestamp\":\""
                        + System.currentTimeMillis() + "\"}]");
    }

    public static String videoTrigger(String bizId) {
        return ApplicationHook.requestString("alipay.content.reading.life.prize.trigger",
                "[{\"bizId\":\"" + bizId
                        + "\",\"bizType\":\"CONTENT\",\"prizeFlowNum\":\"VIDEO_TASK\",\"prizeType\":\"farmFeed\"}]");
    }

    /* 惊喜礼包 */
    public static String drawLotteryPlus() {
        return ApplicationHook.requestString("com.alipay.antfarm.drawLotteryPlus",
                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5 \",\"version\":\"\"}]");
    }

    /* 小麦 */

    public static String acceptGift() {
        return ApplicationHook.requestString("com.alipay.antfarm.acceptGift",
                "[{\"ignoreLimit\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                        + VERSION + "\"}]");
    }

    public static String visitFriend(String friendFarmId) {
        return ApplicationHook.requestString("com.alipay.antfarm.visitFriend",
                "[{\"friendFarmId\":\"" + friendFarmId
                        + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\""
                        + VERSION + "\"}]");
    }

    /* 小鸡日记 */
    public static String queryChickenDiaryList() {
        return ApplicationHook.requestString("com.alipay.antfarm.queryChickenDiaryList",
                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"DIARY\",\"source\":\"antfarm_icon\"}]");
    }

    public static String queryChickenDiary(String queryDayStr) {
        return ApplicationHook.requestString("com.alipay.antfarm.queryChickenDiary",
                "[{\"queryDayStr\":\"" + queryDayStr
                        + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"DIARY\",\"source\":\"antfarm_icon\"}]");
    }

    public static String diaryTietie(String diaryDate, String roleId) {
        return ApplicationHook.requestString("com.alipay.antfarm.diaryTietie",
                "[{\"diaryDate\":\"" + diaryDate + "\",\"requestType\":\"NORMAL\",\"roleId\":\"" + roleId
                        + "\",\"sceneCode\":\"DIARY\",\"source\":\"antfarm_icon\"}]");
    }

    public static String moodDynamic() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"antfarm_icon\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.moodDynamic", args);
    }

    public static String visitAnimal() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.visitAnimal", args);
    }

    public static String feedFriendAnimalVisit(String friendFarmId) {
        String args = "[{\"friendFarmId\":\"" + friendFarmId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"visitChicken\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.feedFriendAnimal", args);
    }

    public static String visitAnimalSendPrize(String token) {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"token\":\"" + token + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.visitAnimalSendPrize", args);
    }

    /* 抽抽乐 */
    public static String enterDrawMachine() {
        return ApplicationHook.requestString("com.alipay.antfarm.enterDrawMachine",
                "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"icon\"}]");
    }

    public static String drawPrize() {
        return ApplicationHook.requestString("com.alipay.antfarm.DrawPrize",
                "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]");
    }

    public static String listFarmDrawTimesTask() {
        return ApplicationHook.requestString("com.alipay.antfarm.listFarmTask",
                "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"signSceneCode\":\"\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_DRAW_TIMES_TASK\",\"topTask\":\"\"}]");
    }

    public static String receiveFarmDrawTimesTaskAward(String taskId) {
        return ApplicationHook.requestString("com.alipay.antfarm.receiveFarmTaskAward",
                "[{\"awardType\":\"DRAW_TIMES\",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskId\":\""
                        + taskId + "\",\"taskSceneCode\":\"ANTFARM_DRAW_TIMES_TASK\"}]");
    }

    // 装扮抽抽乐 限定IP
    public static String queryDrawMachineActivity() {
        String args = "[{\"otherScenes\":[\"dailyDrawMachine\"],\"requestType\":\"RPC\",\"scene\":\"ipDrawMachine\",\"sceneCode\":\"ANTFARM\",\"source\":\"icon\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.queryDrawMachineActivity", args);
    }

    public static String drawMachine() {
        String args = "[{\"requestType\":\"RPC\",\"scene\":\"ipDrawMachine\",\"sceneCode\":\"ANTFARM\",\"source\":\"icon\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.drawMachine", args);
    }

    public static String listFarmIpDrawTask() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"signSceneCode\":\"\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_IP_DRAW_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.listFarmTask", args);
    }

    public static String receiveFarmIpDrawTaskAward(String taskId) {
        String args = "[{\"awardType\":\"IP_DRAW_MACHINE_DRAW_TIMES\",\"requestType\":\"RPC\",\"sceneCode\":\"ANTFARM\",\"source\":\"icon\",\"taskId\":\"" + taskId + "\",\"taskSceneCode\":\"ANTFARM_IP_DRAW_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.receiveFarmTaskAward", args);
    }

    public static String hireAnimal(String friendFarmId, String hireAnimalId) {
        String args = "[{\"friendFarmId\":\"" + friendFarmId + "\",\"hireActionType\":\"HIRE_IN_FRIEND_FARM\",\"hireAnimalId\":\"" + hireAnimalId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"sendCardChat\":false,\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.hireAnimal", args);
    }

    // 小鸡换装
    public static String listOrnaments() {
        return ApplicationHook.requestString("com.alipay.antfarm.listOrnaments",
                "[{\"pageNo\":\"1\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"setsType\":\"ACHIEVEMENTSETS\",\"source\":\"H5\",\"subType\":\"sets\",\"type\":\"apparels\",\"version\":\"" + VERSION + "\"}]");
    }

    public static String saveOrnaments(String animalId, String farmId, String ornaments) {
        return ApplicationHook.requestString("com.alipay.antfarm.saveOrnaments",
                "[{\"animalId\":\"" + animalId + "\",\"farmId\":\"" + farmId + "\",\"ornaments\":\"" + ornaments + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"" + VERSION + "\"}]");
    }

    // 亲密家庭
    public static String enterFamily() {
        String args = "[{\"fromAnn\":false,\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.enterFamily", args);
    }

    public static String queryFamilyInfo() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.queryFamilyInfo", args);
    }

    /**
     * 同步家庭状态
     * @param groupId 家庭ID
     * @param operType 要同步的信息类型
     *                 INTIMACY_VALUE : 亲密度
     *                 FAMILY_INTERACT_ACTION : 进行的活动
     *                 ANIMAL_STATUS : 小鸡状态
     *                 SLEEP_INFO|ANIMAL_STATUS : 小鸡状态与睡眠信息
     *                 DRAW_TIMES : 家庭抽奖次数
     * @param syncUserIds 要同步的用户ID
     * @return String
     */
    public static String syncFamilyStatus(String groupId, String operType, String syncUserIds) {
        // INTIMACY_VALUE
        // FAMILY_INTERACT_ACTION
        // SLEEP_INFO|ANIMAL_STATUS
        // ANIMAL_STATUS|SLEEP_INFO
        // ANIMAL_STATUS
        String args = "[{\"groupId\":\"" + groupId + "\",\"operType\":\"" + operType + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"syncUserIds\":[\"" + syncUserIds + "\"]}]";
        return ApplicationHook.requestString("com.alipay.antfarm.syncFamilyStatus", args);
    }

//    public static String familyListFarmTask() {
//        String args = "[{\"bizKey\":\"FAMILY_SIGN_TASK\",\"requestType\":\"NORMAL\",\"sceneCode\":\"familySign\",\"signSceneCode\":\"\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_FAMILY_TASK\"}]";
//        return ApplicationHook.requestString("com.alipay.antfarm.listFarmTask", args);
//    }

    public static String familyReceiveFarmTaskAward(String taskId) {
        String args = "[{\"awardType\":\"FAMILY_INTIMACY\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskId\":\"" + taskId + "\",\"taskSceneCode\":\"ANTFARM_FAMILY_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.receiveFarmTaskAward", args);
    }

    public static String familyAwardList() {
        String args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.familyAwardList", args);
    }

    public static String receiveFamilyAward(String rightId) {
        String args = "[{\"requestType\":\"NORMAL\",\"rightId\":\"" + rightId + "\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.receiveFamilyAward", args);
    }

    public static String familyEatTogether(String groupId, JSONArray cuisines, JSONArray friendUserIdList) {
        String args = "[{\"cuisines\":" + cuisines + ",\"friendUserIds\":" + friendUserIdList + ",\"groupId\":\"" + groupId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"spaceType\":\"ChickFamily\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.familyEatTogether", args);
    }

    /**
     * 顶梁柱特权指派
     * @param assignAction 指派活动
     * @param beAssignUser 被指派用户
     * @return String
     */
    public static String assignFamilyMember(String assignAction, String beAssignUser) {
        String args = "[{\"assignAction\":\"" + assignAction + "\",\"beAssignUser\":\"" + beAssignUser + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.assignFamilyMember", args);
    }

    public static String queryFamilyDrawActivity() {
        String args = "[{\"bizType\":\"ANTFARM_GAME_CENTER\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.queryFamilyDrawActivity", args);
    }

    public static String familyDraw() {
        String args = "[{\"bizType\":\"ANTFARM_GAME_CENTER\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.familyDraw", args);
    }

    public static String listFarmFamilyDrawTask() {
        String args = "[{\"bizType\":\"ANTFARM_GAME_CENTER\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM_FAMILY_DRAW_TASK\",\"signSceneCode\":\"\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_FAMILY_DRAW_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.listFarmTask", args);
    }

    public static String receiveFarmFamilyDrawTaskAward(String taskId) {
        String args = "[{\"awardType\":\"FAMILY_DRAW_TIME\",\"bizType\":\"ANTFARM_GAME_CENTER\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"taskId\":\"" + taskId + "\",\"taskSceneCode\":\"ANTFARM_FAMILY_DRAW_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.receiveFarmTaskAward", args);
    }

    public static String receiveFamilyVisitAward(String friendUserId, String uniqueId) {
        String args = "[{\"friendUserId\":\"" + friendUserId + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"uniqueId\":\"" + uniqueId + "\"}]";
        return ApplicationHook.requestString("com.alipay.antfarm.receiveFamilyVisitAward", args);
    }
}
