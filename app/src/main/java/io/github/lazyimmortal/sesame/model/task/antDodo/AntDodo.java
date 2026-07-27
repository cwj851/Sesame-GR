package io.github.lazyimmortal.sesame.model.task.antDodo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.BooleanModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.ChoiceModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectOneModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.AlipayUser;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.model.base.TaskCommon;
import io.github.lazyimmortal.sesame.model.task.antFarm.AntFarm.TaskStatus;
import io.github.lazyimmortal.sesame.model.task.antForest.AntForestV2;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.NotificationUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class AntDodo extends ModelTask {
    private static final String TAG = AntDodo.class.getSimpleName();

    @Override
    public String getName() {
        return "神奇物种";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.FOREST;
    }

    private BooleanModelField useProp;
    private SelectModelField usePropList;
    private ChoiceModelField useCollectTimingType;
    private ChoiceModelField useUniversalCardBookStatusType;
    private ChoiceModelField useUniversalCardBookCollectedStatusType;
    private ChoiceModelField useUniversalCardMedalGenerationStatusType;
    private ChoiceModelField useUniversalCardFantasticLevelType;
    private BooleanModelField bookMedal;
    private SelectModelField bookMedalOptions;
    private ChoiceModelField collectToFriendType;
    private SelectModelField collectToFriendList;
    private BooleanModelField giftToFriend;
    private ChoiceModelField giftToFriendBookStatusType;
    private ChoiceModelField giftToFriendBookCollectedStatusType;
    private ChoiceModelField giftToFriendMedalGenerationStatusType;
    private ChoiceModelField giftToFriendFantasticLevelType;
    private SelectOneModelField giftToFriendTargetUserList;

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(useProp = new BooleanModelField("useProp", "使用道具 | 开启", false));
        modelFields.addField(usePropList = new SelectModelField("usePropList", "使用道具 | 道具列表", new LinkedHashSet<>(), AntDodoProp.class));
        modelFields.addField(useCollectTimingType = new ChoiceModelField("useCollectTimingType", "抽卡道具 | 使用时机", TimingType.EVERY_DAY, TimingType.nickNames));
        modelFields.addField(useUniversalCardBookStatusType = new ChoiceModelField("useUniversalCardBookStatusType", "万能卡片 | 图鉴状态类型", BookStatusType.END, BookStatusType.nickNames));
        modelFields.addField(useUniversalCardBookCollectedStatusType = new ChoiceModelField("useUniversalCardBookCollectedStatusType", "万能卡片 | 图鉴收集状态", BookCollectedStatusType.ALL, BookCollectedStatusType.nickNames));
        modelFields.addField(useUniversalCardMedalGenerationStatusType = new ChoiceModelField("useUniversalCardMedalGenerationStatusType", "万能卡片 | 勋章合成状态", MedalGenerationStatusType.ALL, MedalGenerationStatusType.nickNames));
        modelFields.addField(useUniversalCardFantasticLevelType = new ChoiceModelField("useUniversalCardFantasticLevelType", "万能卡片 | 最低等级", FantasticLevelType.MAGIC, FantasticLevelType.nickNames));
        modelFields.addField(bookMedal = new BooleanModelField("bookMedal", "图鉴勋章 | 开启", false));
        modelFields.addField(bookMedalOptions = new SelectModelField("bookMedalOptions", "图鉴勋章 | 选项", new LinkedHashSet<>(), AntDodoBookMedalOption.class));
        modelFields.addField(collectToFriendType = new ChoiceModelField("collectToFriendType", "帮抽卡片 | 动作", CollectToFriendType.NONE, CollectToFriendType.nickNames));
        modelFields.addField(collectToFriendList = new SelectModelField("collectToFriendList", "帮抽卡片 | 好友列表", new LinkedHashSet<>(), AlipayUser::getList));
        modelFields.addField(giftToFriend = new BooleanModelField("giftToFriend", "赠送卡片 | 开启", false));
        modelFields.addField(giftToFriendBookStatusType = new ChoiceModelField("giftToFriendBookStatusType", "赠送卡片 | 图鉴状态类型", BookStatusType.ALL, BookStatusType.nickNames));
        modelFields.addField(giftToFriendBookCollectedStatusType = new ChoiceModelField("giftToFriendBookCollectedStatusType", "赠送卡片 | 图鉴收集状态", BookCollectedStatusType.ALL, BookCollectedStatusType.nickNames));
        modelFields.addField(giftToFriendMedalGenerationStatusType = new ChoiceModelField("giftToFriendMedalGenerationStatusType", "赠送卡片 | 勋章合成状态", MedalGenerationStatusType.ALL, MedalGenerationStatusType.nickNames));
        modelFields.addField(giftToFriendFantasticLevelType = new ChoiceModelField("giftToFriendFantasticLevelType", "赠送卡片 | 最低等级", FantasticLevelType.COMMON, FantasticLevelType.nickNames));
        modelFields.addField(giftToFriendTargetUserList = new SelectOneModelField("giftToFriendTargetUserList", "赠送卡片 | 好友列表", null, AlipayUser::getList, "会赠送所有满足条件的卡片给已选择的好友"));
        return modelFields;
    }

    @Override
    public Boolean check() {
        if (TaskCommon.IS_ENERGY_TIME) {
            Log.forest("任务暂停⏸️神奇物种:当前为只收能量时间");
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            NotificationUtil.sendTaskNotification(this);
            collect();
            taskList();
            if (useProp.getValue()) {
                propList();
            }
            if (collectToFriendType.getValue() != CollectToFriendType.NONE) {
                collectToFriend();
            }
            if (bookMedal.getValue()) {
                generateBookMedal();
            }
            if (giftToFriend.getValue()) {
                giftToFriend();
            }
        } catch (Throwable t) {
            Log.i(TAG, "start.run err:");
            Log.printStackTrace(TAG, t);
        } finally {
            NotificationUtil.removeTaskNotification(this);
        }
    }

    /*
     * 神奇物种
     */
    private long getEndDateTime() {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.homePage());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return 0;
            }
            jo = jo.getJSONObject("data");
            jo = jo.getJSONObject("animalBook");
            String endDate = jo.getString("endDate") + " 23:59:59";
            return Log.timeToStamp(endDate);
        } catch (Throwable t) {
            Log.i(TAG, "getEndDateTime err:");
            Log.printStackTrace(TAG, t);
        }
        return 0;
    }

    private boolean isLastDay() {
        return getEndDateTime() - TimeUnit.DAYS.toMillis(1) < System.currentTimeMillis();
    }

    private void collect() {
        if (Status.hasFlagToday(AntDodoFlag.COLLECT.flagName())) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.queryAnimalStatus());
            if (MessageUtil.checkResponse(TAG, jo)) {
                JSONObject data = jo.getJSONObject("data");
                if (data.getBoolean("collect")) {
                    Log.record("神奇物种卡片今日收集完成！");
                } else {
                    collectAnimalCard();
                }
                Status.flagToday(AntDodoFlag.COLLECT.flagName());
            }
        } catch (Throwable t) {
            Log.i(TAG, "collect err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void collectAnimalCard() {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.homePage());
            if (MessageUtil.checkResponse(TAG, jo)) {
                JSONObject data = jo.getJSONObject("data");
                JSONArray ja = data.getJSONArray("limit");
                int index = -1;
                for (int i = 0; i < ja.length(); i++) {
                    jo = ja.getJSONObject(i);
                    if ("DAILY_COLLECT".equals(jo.getString("actionCode"))) {
                        index = i;
                        break;
                    }
                }
                if (index >= 0) {
                    int leftFreeQuota = jo.getInt("leftFreeQuota");
                    for (int j = 0; j < leftFreeQuota; j++) {
                        jo = new JSONObject(AntDodoRpcCall.collect());
                        if (MessageUtil.checkResponse(TAG, jo)) {
                            data = jo.getJSONObject("data");
                            JSONObject animal = data.getJSONObject("animal");
                            Log.forest("神奇物种🦕每日抽卡" + getAnimalInfo(animal));
                            checkAnimalAndGiftToFriend(animal);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "collectAnimalCard err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void taskList() {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.taskList());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            JSONArray taskGroupInfoList = jo.getJSONArray("taskGroupInfoList");
            for (int i = 0; i < taskGroupInfoList.length(); i++) {
                jo = taskGroupInfoList.getJSONObject(i);
                String taskGroupName = jo.getString("taskGroupName");
                jo = jo.getJSONArray("taskInfoList").getJSONObject(0).getJSONObject("taskBaseInfo");
                TaskStatus taskStatus = TaskStatus.valueOf(jo.getString("taskStatus"));
                if (taskStatus == TaskStatus.RECEIVED) {
                    continue;
                }
                String sceneCode = jo.getString("sceneCode");
                String taskType = jo.getString("taskType");
                if (taskStatus == TaskStatus.TODO) {
                    if (!Objects.equals("SEND_FRIEND_CARD", taskType)
                            || !Objects.equals("AD_BIODIVERSITY_MASTERCARD", taskType)
                            || !finishTask(sceneCode, taskType, taskGroupName)) {
                        continue;
                    }
                    TimeUtil.sleep(1000);
                    taskStatus = TaskStatus.FINISHED;
                }
                if (taskStatus == TaskStatus.FINISHED) {
                    receiveTaskAward(sceneCode, taskType, taskGroupName);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "taskList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean finishTask(String sceneCode, String taskType, String taskTitle) {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.finishTask(sceneCode, taskType));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.forest("神奇物种🦕完成任务[" + taskTitle + "]");
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "finishTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private void receiveTaskAward(String sceneCode, String taskType, String taskTitle) {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.receiveTaskAward(sceneCode, taskType));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.forest("神奇物种🦕领取奖励[" + taskTitle + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "receiveTaskAward err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void propList() {
        try {
            th:
            do {
                JSONObject jo = new JSONObject(AntDodoRpcCall.propList());
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    break;
                }
                jo = jo.getJSONObject("data");
                JSONArray propList = jo.getJSONArray("propList");
                for (int i = 0; i < propList.length(); i++) {
                    JSONObject prop = propList.getJSONObject(i);
                    String propType = prop.getString("propType");
                    String propGroup = prop.getJSONObject("propConfig").getString("propGroup");
                    JSONArray propIdList = prop.getJSONArray("propIdList");
                    String propId = propIdList.getString(0);
                    long recentExpireTime = prop.getLong("recentExpireTime");
                    boolean willExpireSoon = recentExpireTime - TimeUnit.DAYS.toMillis(1) < System.currentTimeMillis();
                    boolean isUseProp = usePropList.contains(propType);
                    if (!isUseProp && !willExpireSoon) {
                        continue;
                    }
                    if (PropGroup.UNIVERSAL_CARD.name().equals(propGroup)) {
                        if (!usePropUniversalCard(propId, propType)) {
                            continue;
                        }
                    } else {
                        if (PropGroup.COLLECT_ANIMAL.name().equals(propGroup)
                                && !willExpireSoon
                                && useCollectTimingType.getValue() == TimingType.LAST_DAY
                                && !isLastDay()) {
                            continue;
                        }
                        if (!consumeProp(propId, propType)) {
                            continue;
                        }
                    }
                    if (prop.optInt("holdsNum", 1) > 1) {
                        continue th;
                    }
                }
                break;
            } while (true);
        } catch (Throwable th) {
            Log.i(TAG, "propList err:");
            Log.printStackTrace(TAG, th);
        }
    }

    // 使用万能卡
    private Boolean usePropUniversalCard(String propId, String propType) {
        try {
            boolean hasMore;
            int pageStart = 0;
            JSONObject animal = null;
            do {
                JSONObject jo = new JSONObject(AntDodoRpcCall.queryBookList(9, pageStart));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    break;
                }
                jo = jo.getJSONObject("data");
                hasMore = jo.getBoolean("hasMore");
                pageStart += 9;
                JSONArray bookForUserList = jo.getJSONArray("bookForUserList");
                for (int i = 0; i < bookForUserList.length(); i++) {
                    jo = bookForUserList.getJSONObject(i);
                    if (isQueryBookInfo(jo, 0)) {
                        JSONObject animalBookResult = jo.getJSONObject("animalBookResult");
                        String bookId = animalBookResult.getString("bookId");
                        animal = queryUniversalAnimal(bookId, animal);
                    }
                }
            } while (hasMore);
            if (animal != null && consumeProp(propId, propType, animal.getString("animalId"))) {
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "usePropUniversalCard err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private Boolean isQueryBookInfo(JSONObject bookForUser,
                                    int type) {
        int statusType = type == 0
                ? useUniversalCardBookStatusType.getValue()
                : giftToFriendBookStatusType.getValue();
        String bookStatus = bookForUser.optString("bookStatus");
        if (!BookStatus.valueOf(bookStatus).match(
                BookStatusType.types[statusType])) {
            return false;
        }

        int bookCollectedStatusType = type == 0
                ? useUniversalCardBookCollectedStatusType.getValue()
                : giftToFriendBookCollectedStatusType.getValue();
        String bookCollectedStatus = bookForUser.optString("bookCollectedStatus");
        if (!BookCollectedStatus.valueOf(bookCollectedStatus).match(
                BookCollectedStatusType.types[bookCollectedStatusType])) {
            return false;
        }

        int medalGenerationStatusType = type == 0
                ? useUniversalCardMedalGenerationStatusType.getValue()
                : giftToFriendMedalGenerationStatusType.getValue();
        String medalGenerationStatus = bookForUser.optString("medalGenerationStatus");
        return MedalGenerationStatus.valueOf(medalGenerationStatus).match(
                MedalGenerationStatusType.types[medalGenerationStatusType]);
    }

    private JSONObject queryUniversalAnimal(String bookId, JSONObject animal) {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.queryBookInfo(bookId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return animal;
            }
            // data: animalBookResult{}
            // data: animalForUserList[]
            JSONArray animalForUserList = jo.getJSONObject("data").getJSONArray("animalForUserList");
            for (int i = 0; i < animalForUserList.length(); i++) {
                jo = animalForUserList.getJSONObject(i);
                int star = jo.getInt("star");
                if (star < FantasticLevelType.stars[useUniversalCardFantasticLevelType.getValue()]) {
                    break;
                }
                JSONObject collectDetail = jo.getJSONObject("collectDetail");
                int count = collectDetail.optInt("count", 1 << 30);
                if (animal == null
                        || count < animal.getInt("count")
                        || (count == animal.getInt("count")
                        && star > animal.getInt("star"))) {
                    animal = jo.getJSONObject("animal");
                    animal.put("star", star);
                    animal.put("count", count);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryUniversalAnimal err:");
            Log.printStackTrace(TAG, t);
        }
        return animal;
    }

    private Boolean consumeProp(String propId, String propType) {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.consumeProp(propId, propType));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }

            jo = jo.getJSONObject("data");
            String propName = jo.getJSONObject("propConfig").getString("propName");

            JSONObject animal = jo.getJSONObject("useResult").optJSONObject("animal");
            Log.forest("使用道具🎭神奇物种[" + propName + "]" + getAnimalInfo(animal));
            checkAnimalAndGiftToFriend(animal);
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "consumeProp err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private Boolean consumeProp(String propId, String propType, String animalId) {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.consumeProp(propId, propType, animalId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            jo = jo.getJSONObject("data");
            String propName = jo.getJSONObject("propConfig").getString("propName");
            JSONObject animal = jo.getJSONObject("useResult").optJSONObject("animal");
            Log.forest("使用道具🎭神奇物种[" + propName + "]" + getAnimalInfo(animal));
            checkAnimalAndGiftToFriend(animal);
            return true;
        } catch (Throwable th) {
            Log.i(TAG, "consumeProp err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    private void collectToFriend() {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.queryFriend());
            if (MessageUtil.checkResponse(TAG, jo)) {
                int count = 0;
                JSONArray limitList = jo.getJSONObject("data").getJSONObject("extend").getJSONArray("limit");
                for (int i = 0; i < limitList.length(); i++) {
                    JSONObject limit = limitList.getJSONObject(i);
                    if (limit.getString("actionCode").equals("COLLECT_TO_FRIEND")) {
                        if (limit.getLong("startTime") > System.currentTimeMillis()) {
                            return;
                        }
                        count = limit.getInt("leftLimit");
                        break;
                    }

                }
                JSONArray friendList = jo.getJSONObject("data").getJSONArray("friends");
                for (int i = 0; i < friendList.length() && count > 0; i++) {
                    JSONObject friend = friendList.getJSONObject(i);
                    if (friend.getBoolean("dailyCollect")) {
                        continue;
                    }
                    String useId = friend.getString("userId");
                    boolean isCollectToFriend = collectToFriendList.contains(useId);
                    if (collectToFriendType.getValue() != CollectToFriendType.COLLECT) {
                        isCollectToFriend = !isCollectToFriend;
                    }
                    if (!isCollectToFriend) {
                        continue;
                    }
                    jo = new JSONObject(AntDodoRpcCall.collect(useId));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        String userName = UserIdMap.getMaskName(useId);
                        JSONObject animal = jo.getJSONObject("data").optJSONObject("animal");
                        Log.forest("帮抽卡片🦕帮助好友[" + userName + "]" + getAnimalInfo(animal));
                        count--;
                    }
                }

            }
        } catch (Throwable t) {
            Log.i(TAG, "collectHelpFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void generateBookMedal() {
        // 图鉴合成状态 合成 可以合成 不能合成
        // medalGenerationStatus: GENERATED CAN_GENERATE CAN_NOT_GENERATE

        // 卡片收集情况 完成 未完成
        // bookCollectedStatus: COMPLETED NOT_COMPLETED

        // 卡片收集进度
        // collectProgress 10/10 2/10
        try {
            boolean hasMore;
            int pageStart = 0;
            do {
                JSONObject jo = new JSONObject(AntDodoRpcCall.queryBookList(9, pageStart));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    break;
                }
                jo = jo.getJSONObject("data");
                hasMore = jo.getBoolean("hasMore");
                pageStart += 9;
                JSONArray bookForUserList = jo.getJSONArray("bookForUserList");
                for (int i = 0; i < bookForUserList.length(); i++) {
                    jo = bookForUserList.getJSONObject(i);
                    MedalGenerationStatus medalGenerationStatus = MedalGenerationStatus.valueOf(
                            jo.optString("medalGenerationStatus")
                    );
                    if (medalGenerationStatus == MedalGenerationStatus.CAN_GENERATE) {
                        if (bookMedalOptions.contains(AntDodoBookMedalOption.GENERATE_BOOK_MEDAL.name())) {
                            JSONObject animalBookResult = jo.getJSONObject("animalBookResult");
                            String bookId = animalBookResult.getString("bookId");
                            String ecosystem = animalBookResult.getString("ecosystem");
                            jo = new JSONObject(AntDodoRpcCall.generateBookMedal(bookId));
                            if (!MessageUtil.checkResponse(TAG, jo)) {
                                break;
                            }
                            Log.forest("图鉴勋章🦕合成勋章[" + ecosystem + "]");
                        }
                    } else if (medalGenerationStatus == MedalGenerationStatus.CAN_NOT_GENERATE) {
                        if (bookMedalOptions.contains(AntDodoBookMedalOption.COLLECT_HISTORY_ANIMAL.name())
                                && Objects.equals(BookStatus.END.name(), jo.optString("bookStatus"))) {
                            if (Status.canVitalityExchangeBenefitToday(AntForestV2.PropType.COLLECT_HISTORY_ANIMAL_7_DAYS.getSkuId(), 1)) {
                                AntForestV2.exchangeBenefit(AntForestV2.PropType.COLLECT_HISTORY_ANIMAL_7_DAYS);
                            }
                        }
                    }
                }
            } while (hasMore);
        } catch (Throwable t) {
            Log.i(TAG, "generateBookMedal err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static String getAnimalInfo(JSONObject animal) {
        if (animal == null) {
            return "";
        }
        String ecosystem = animal.optString("ecosystem", "未知专辑");
        String name = animal.optString("name", "未知动物");
        String fantasticLevel = animal.optString("fantasticLevel", "Unknown");
        return "#[(" + ecosystem + ")" + name + "(" + FantasticLevel.valueOf(fantasticLevel).nickName() + ")]";
    }

    private void checkAnimalAndGiftToFriend(JSONObject animal) {
        if (animal == null
                || !giftToFriend.getValue()
                || useCollectTimingType.getValue() != TimingType.LAST_DAY) {
            return;
        }
        String targetUserId = giftToFriendTargetUserList.getValue();
        if (targetUserId == null || Objects.equals(targetUserId, UserIdMap.getCurrentUid())) {
            return;
        }
        try {
            if (!FantasticLevel.MAGIC.name().equals(animal.getString("fantasticLevel"))) {
                return;
            }
            String bookId = animal.getString("bookId");
            JSONObject jo = new JSONObject(AntDodoRpcCall.homePage());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data").getJSONObject("animalBook");
            if (!bookId.equals(jo.getString("bookId"))) {
                return;
            }
            giftToFriend(animal, targetUserId);
        } catch (Throwable t) {
            Log.i(TAG, "checkAnimalAndGiftToFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void giftToFriend() {
        String targetUserId = giftToFriendTargetUserList.getValue();
        if (targetUserId == null || Objects.equals(targetUserId, UserIdMap.getCurrentUid())) {
            return;
        }
        giftToFriend(targetUserId);
    }

    private void giftToFriend(String targetUserId) {
        try {
            boolean hasMore;
            int pageStart = 0;
            do {
                JSONObject jo = new JSONObject(AntDodoRpcCall.queryBookList(9, pageStart));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    break;
                }
                jo = jo.getJSONObject("data");
                hasMore = jo.getBoolean("hasMore");
                pageStart += 9;
                JSONArray bookForUserList = jo.getJSONArray("bookForUserList");
                for (int i = 0; i < bookForUserList.length(); i++) {
                    jo = bookForUserList.getJSONObject(i);
                    String collectProgress = jo.getString("collectProgress");
                    if (collectProgress.startsWith("0/")
                            || !isQueryBookInfo(jo, 1)) {
                        continue;
                    }
                    String bookId = jo.getJSONObject("animalBookResult").getString("bookId");
                    giftToFriend(bookId, targetUserId);
                }
            } while (hasMore);
        } catch (Throwable t) {
            Log.i(TAG, "giftToFriend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private void giftToFriend(String bookId, String targetUserId) {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.queryBookInfo(bookId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray animalForUserList = jo.getJSONObject("data").optJSONArray("animalForUserList");
            if (animalForUserList == null) {
                return;
            }
            int star = FantasticLevelType.stars[giftToFriendFantasticLevelType.getValue()];
            for (int i = 0; i < animalForUserList.length(); i++) {
                JSONObject animalForUser = animalForUserList.getJSONObject(i);
                if (animalForUser.optInt("star") < star) {
                    continue;
                }
                int count = animalForUser.getJSONObject("collectDetail").optInt("count");
                if (count <= 0) {
                    continue;
                }
                JSONObject animal = animalForUser.getJSONObject("animal");
                for (int j = 0; j < count; j++) {
                    if (!giftToFriend(animal, targetUserId)) {
                        return;
                    }
                    TimeUtil.sleep(500L);
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "giftToFriend err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private Boolean giftToFriend(JSONObject animal, String targetUserId) {
        try {
            String animalId = animal.getString("animalId");
            JSONObject jo = new JSONObject(AntDodoRpcCall.social(animalId, targetUserId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Log.forest("赠送卡片🦕赠送好友[" + UserIdMap.getMaskName(targetUserId) + "]" + getAnimalInfo(animal));
                return true;
            }
        } catch (Throwable th) {
            Log.i(TAG, "giftToFriend err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    public enum PropGroup {
        COLLECT_ANIMAL, COLLECT_HISTORY_ANIMAL, ADD_COLLECT_TO_FRIEND_LIMIT, UNIVERSAL_CARD;

        public final String[] nickNames = {"抽卡道具", "历史图鉴随机卡道具", "抽好友卡道具", "万能卡道具"};

        public String nickName() {
            return nickNames[ordinal()];
        }
    }

    public enum BookStatus {
        NOT_START, DOING, END;

        public final String[] nickNames = {"未开启", "进行中", "已结束"};

        public String nickName() {
            return nickNames[ordinal()];
        }

        public Boolean match(String status) {
            if (name().equals(NOT_START.name())) {
                return false;
            }
            return name().equals(status) || "ALL".equals(status);
        }
    }

    public enum BookCollectedStatus {
        NOT_COMPLETED, COMPLETED;

        public Boolean match(String status) {
            return name().equals(status) || "ALL".equals(status);
        }
    }

    public enum MedalGenerationStatus {
        CAN_NOT_GENERATE, CAN_GENERATE, GENERATED;

        public final String[] nickNames = {"收集中", "已集齐", "已合成"};

        public String nickName() {
            return nickNames[ordinal()];
        }

        public Boolean match(String status) {
            return name().equals(status) || "ALL".equals(status);
        }
    }

    public enum FantasticLevel {
        COMMON("普通"),
        RARE("稀有"),
        MAGIC("神奇");

        private final String nickName;

        FantasticLevel(String nickName) {
            this.nickName = nickName;
        }

        public String nickName() {
            return nickName;
        }
    }

    private enum AntDodoFlag implements Status.StatusFlag {
        COLLECT
    }

    public enum AntDodoProp implements CustomOption {
        COLLECT_TIMES_7_DAYS("抽卡道具"),
        COLLECT_HISTORY_ANIMAL_7_DAYS("历史图鉴随机卡道具"),
        COLLECT_TO_FRIEND_TIMES_7_DAYS("抽好友卡道具"),
        UNIVERSAL_CARD_7_DAYS("万能卡道具");

        private final String nickName;

        AntDodoProp(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    public enum AntDodoBookMedalOption implements CustomOption {
        GENERATE_BOOK_MEDAL("自动合成图鉴勋章"),
        COLLECT_HISTORY_ANIMAL("自动收集历史物种");

        private final String nickName;

        AntDodoBookMedalOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    public interface TimingType {
        int EVERY_DAY = 0;
        int LAST_DAY = 1;

        String[] nickNames = {"每天使用", "专辑最后一天"};
    }

    public interface CollectToFriendType {

        int NONE = 0;
        int COLLECT = 1;
        int NOT_COLLECT = 2;

        String[] nickNames = {"不帮抽", "帮抽已选好友", "帮抽未选好友"};

    }

    public interface BookStatusType {
        int ALL = 0;
        int END = 1;
        int DOING = 2;

        String[] nickNames = {"全部图鉴", "往期图鉴", "本期图鉴"};
        String[] types = {"ALL", "END", "DOING"};
    }

    public interface BookCollectedStatusType {
        int ALL = 0;
        int NOT_COMPLETED = 1;
        int COMPLETED = 2;

        String[] nickNames = {"全部状态", "未完成收集", "已完成收集"};
        String[] types = {"ALL", "NOT_COMPLETED", "COMPLETED"};
    }

    public interface MedalGenerationStatusType {
        int ALL = 0;
        int CAN_NOT_GENERATE = 1;
        int CAN_GENERATE = 2;
        int GENERATED = 3;

        String[] nickNames = {"全部类型", "未能合成", "可以合成", "已经合成"};
        String[] types = {"ALL", "CAN_NOT_GENERATE", "CAN_GENERATE", "GENERATED"};
    }

    public interface FantasticLevelType {
        int COMMON = 0;
        int RARE = 1;
        int MAGIC = 2;

        String[] nickNames = {"普通", "稀有", "神奇"};
        int[] stars = {1, 2, 3};
    }
}