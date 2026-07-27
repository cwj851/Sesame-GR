package io.github.lazyimmortal.sesame.util;

import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.model.task.antFarm.AntFarm;
import io.github.lazyimmortal.sesame.model.task.antForest.AntForestV2;
import io.github.lazyimmortal.sesame.model.task.antStall.AntStall;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;
import lombok.Data;

@Data
public class Status {

    private static final String TAG = Status.class.getSimpleName();

    public static final Status INSTANCE = new Status();

    // forest
    private final Map<String, Integer> waterFriendLogList = new HashMap<>();
    private final Map<String, Integer> vitalityExchangeBenefitLogList = new HashMap<>();
    private final Map<Integer, Integer> exchangeReserveLogList = new HashMap<>();
    private final Set<String> ancientTreeCityCodeList = new HashSet<>();
    private int doubleClickTimes = 0;

    // farm
    private final Map<String, Integer> feedFriendLogList = new HashMap<>();
    private final Map<String, Integer> visitFriendLogList = new HashMap<>();
    private final Map<String, Integer> useFarmToolLogList = new HashMap<>();
    private final Map<String, Integer> buyFarmMallItemLogList = new HashMap<>();
    private int useSpecialFoodCount = 0;
    private int redPocketCount = 0;

    // orchard
    private final Set<String> orchardShareP2PLogList = new HashSet<>();

    // stall
    private final Map<String, Integer> stallHelpedCountLogList = new HashMap<>();
    private final Set<String> stallShareP2PLogList = new HashSet<>();

    // member
    private final Set<String> memberPointExchangeBenefitLogList = new HashSet<>();

    // other
    private final Set<String> flagLogList = new HashSet<>();

    // 保存时间
    private Long saveTime = 0L;

    /**
     * 放心消费能量
     */
    private boolean easeConsumeGenerateEnergy = false;

    private boolean ugShoppingPollingSign = false;

    /**
     * 绿色经营，收取好友金币已完成用户
     */
    private boolean greenFinancePointFriend = false;

    /**
     * 绿色经营，评级领奖已完成用户
     */
    private final Set<Integer> greenFinancePrizesSet = new HashSet<>();

    public static Boolean hasFlagToday(String tag) {
        return INSTANCE.flagLogList.contains(tag);
    }

    public static void flagToday(String tag) {
        if (!hasFlagToday(tag)) {
            INSTANCE.flagLogList.add(tag);
            save();
        }
    }

    public static Boolean canWaterFriendToday(String id, int newCount) {
        Integer count = INSTANCE.waterFriendLogList.get(id);
        if (count == null) {
            return true;
        }
        return count < newCount;
    }

    public static void waterFriendToday(String id, int count) {
        INSTANCE.waterFriendLogList.put(id, count);
        save();
    }

    public static int getVitalityExchangeBenefitCountToday(String skuId) {
        Integer exchangedCount = INSTANCE.vitalityExchangeBenefitLogList.get(skuId);
        if (exchangedCount == null) {
            exchangedCount = 0;
        }
        return exchangedCount;
    }

    public static Boolean canVitalityExchangeBenefitToday(String skuId, Integer count) {
        return count != null
                &&!hasFlagToday(AntForestV2.AntForestFlag.EXCHANGE_LIMIT.flagName(skuId))
                && getVitalityExchangeBenefitCountToday(skuId) < count;
    }

    public static void vitalityExchangeBenefitToday(String skuId) {
        int count = getVitalityExchangeBenefitCountToday(skuId) + 1;
        INSTANCE.vitalityExchangeBenefitLogList.put(skuId, count);
        save();
    }

    public static int getExchangeReserveCountToday(int id) {
        Integer count = INSTANCE.exchangeReserveLogList.get(id);
        return count == null ? 0 : count;
    }

    public static Boolean canExchangeReserveToday(int id, int count) {
        return getExchangeReserveCountToday(id) < count;
    }

    public static void exchangeReserveToday(int id) {
        int count = getExchangeReserveCountToday(id) + 1;
        INSTANCE.exchangeReserveLogList.put(id, count);
        save();
    }

    public static Boolean canMemberPointExchangeBenefitToday(String benefitId) {
        return !INSTANCE.memberPointExchangeBenefitLogList.contains(benefitId);
    }

    public static void memberPointExchangeBenefitToday(String benefitId) {
        if (canMemberPointExchangeBenefitToday(benefitId)) {
            INSTANCE.memberPointExchangeBenefitLogList.add(benefitId);
            save();
        }
    }

    public static Boolean canAncientTreeToday(String cityCode) {
        return !INSTANCE.ancientTreeCityCodeList.contains(cityCode);
    }

    public static void ancientTreeToday(String cityCode) {
        Status stat = INSTANCE;
        if (!stat.ancientTreeCityCodeList.contains(cityCode)) {
            stat.ancientTreeCityCodeList.add(cityCode);
            save();
        }
    }

    private static int getFeedFriendCountToday(String id) {
        Integer count = INSTANCE.feedFriendLogList.get(id);
        return count == null ? 0 : count;
    }

    public static Boolean canFeedFriendToday(String id, int countLimit) {
        return !hasFlagToday(AntFarm.AntFarmFlag.FEED_FRIEND_ANIMAL_LIMIT.flagName())
                && getFeedFriendCountToday(id) < countLimit;
    }

    public static void feedFriendToday(String id) {
        int count = getFeedFriendCountToday(id) + 1;
        INSTANCE.feedFriendLogList.put(id, count);
        save();
    }

    private static int getVisitFriendCountToday(String id) {
        Integer count = INSTANCE.visitFriendLogList.get(id);
        return count == null ? 0 : count;
    }

    public static Boolean canVisitFriendToday(String id, int countLimit) {
        countLimit = Math.max(countLimit, 0);
        countLimit = Math.min(countLimit, 3);
        return !hasFlagToday(AntFarm.AntFarmFlag.VISIT_FRIEND_LIMIT.flagName(id))
                && getVisitFriendCountToday(id) < countLimit;
    }

    public static void visitFriendToday(String id) {
        int count = getVisitFriendCountToday(id) + 1;
        INSTANCE.visitFriendLogList.put(id, count);
        save();
    }

    public static void visitFriendToday(String id, int count) {
        INSTANCE.visitFriendLogList.put(id, count);
        save();
    }

    public static boolean canStallHelpToday(String id) {
        Integer count = INSTANCE.stallHelpedCountLogList.get(id);
        if (count == null) {
            return true;
        }
        return count < 3;
    }

    public static void stallHelpToday(String id, boolean limited) {
        Integer count = INSTANCE.stallHelpedCountLogList.get(id);
        if (count == null) {
            count = 0;
        }
        if (limited) {
            count = 3;
        } else {
            count += 1;
        }
        INSTANCE.stallHelpedCountLogList.put(id, count);
        save();
    }

    public static Integer getUseFarmToolCountToday(String toolType) {
        Integer count = INSTANCE.useFarmToolLogList.get(toolType);
        return count == null ? 0 : count;
    }

    public static Boolean canUseFarmToolToday(String toolType) {
        AntFarm task = ModelTask.getModel(AntFarm.class);
        if (task == null) {
            return false;
        }
        Integer countLimit = task.getUseFarmToolList().get(toolType);
        if (countLimit == null) {
            return false;
        }
        return getUseFarmToolCountToday(toolType) < countLimit
                && !hasFlagToday(AntFarm.AntFarmFlag.USE_FARM_TOOL_LIMIT.flagName(toolType));
    }

    public static void useFarmToolToday(String toolType) {
        INSTANCE.useFarmToolLogList.put(toolType, getUseFarmToolCountToday(toolType) + 1);
        save();
    }

    public static Integer getBuyFarmMallItemCountToday(String skuId) {
        Integer count = INSTANCE.buyFarmMallItemLogList.get(skuId);
        return count == null ? 0 : count;
    }

    public static Boolean canBuyFarmMallItemCountToday(String skuId) {
        AntFarm task = ModelTask.getModel(AntFarm.class);
        if (task == null) {
            return false;
        }
        Integer countLimit = task.getFarmMallItemList().get(skuId);
        if (countLimit == null) {
            return false;
        }
        return getBuyFarmMallItemCountToday(skuId) < countLimit;
    }

    public static void buyFarmMallItemCountToday(String skuId) {
        INSTANCE.useFarmToolLogList.put(skuId, getBuyFarmMallItemCountToday(skuId) + 1);
        save();
    }



    public static Boolean canUseSpecialFoodToday() {
        AntFarm task = ModelTask.getModel(AntFarm.class);
        if (task == null) {
            return false;
        }
        int countLimit = task.getUseSpecialFoodCountLimit().getValue();
        if (countLimit == 0) {
            return true;
        }
        return INSTANCE.useSpecialFoodCount < countLimit;
    }

    public static void useSpecialFoodToday() {
        INSTANCE.useSpecialFoodCount += 1;
        save();
    }

    public static Boolean canOrchardShareP2PToday(String friendUserId) {
        return !hasFlagToday(AntStall.AntStallFlag.SHARE_P2P_LIMIT.flagName())
                && !hasFlagToday(AntStall.AntStallFlag.THROW_MANURE_LIMIT.flagName(friendUserId))
                && !INSTANCE.orchardShareP2PLogList.contains(friendUserId);
    }

    public static void orchardShareP2PToday(String friendUserId) {
        if (canOrchardShareP2PToday(friendUserId)) {
            INSTANCE.orchardShareP2PLogList.add(friendUserId);
            save();
        }
    }

    public static Boolean canStallShareP2PToday(String friendUserId) {
        return !hasFlagToday(AntStall.AntStallFlag.SHARE_P2P_LIMIT.flagName())
                && !hasFlagToday(AntStall.AntStallFlag.SHARE_P2P_LIMIT.flagName(friendUserId))
                && !INSTANCE.stallShareP2PLogList.contains(friendUserId);
    }

    public static void stallShareP2PToday(String friendUserId) {
        if (canStallShareP2PToday(friendUserId)) {
            INSTANCE.stallShareP2PLogList.add(friendUserId);
            save();
        }
    }

    public static boolean canDoubleClickToday(int doubleClickCountLimit) {
        return INSTANCE.doubleClickTimes < doubleClickCountLimit;
    }

    public static void doubleClickToday() {
        INSTANCE.doubleClickTimes += 1;
        save();
    }

    public static boolean canGetRedPocketToday(int redPocketCountLimit) {
        return INSTANCE.redPocketCount < redPocketCountLimit;
    }

    public static void getRedPocketToday() {
        INSTANCE.redPocketCount += 1;
        save();
    }

    /**
     * 绿色经营-是否可以收好友金币
     *
     * @return true是，false否
     */
    public static boolean canGreenFinancePointFriend() {
        return !INSTANCE.greenFinancePointFriend;
    }

    /**
     * 绿色经营-收好友金币完了
     */
    public static void greenFinancePointFriend() {
        Status stat = INSTANCE;
        if (!stat.greenFinancePointFriend) {
            stat.greenFinancePointFriend = true;
            save();
        }
    }

    public static boolean canEaseConsumeGenerateEnergy() {
        return !INSTANCE.easeConsumeGenerateEnergy;
    }

    public static void easeConsumeGenerateEnergy() {
        Status stat = INSTANCE;
        if (!stat.easeConsumeGenerateEnergy) {
            stat.easeConsumeGenerateEnergy = true;
            save();
        }
    }

    public static boolean canUgShoppingPollingSign() {
        return !INSTANCE.ugShoppingPollingSign;
    }

    public static void ugShoppingPollingSign() {
        Status stat = INSTANCE;
        if (!stat.ugShoppingPollingSign) {
            stat.ugShoppingPollingSign = true;
            save();
        }
    }
    /**
     * 绿色经营-是否可以做评级任务
     *
     * @return true是，false否
     */
    public static boolean canGreenFinancePrizesMap() {
        int week = TimeUtil.getWeekNumber(new Date());
        return !INSTANCE.greenFinancePrizesSet.contains(week);
    }

    /**
     * 绿色经营-评级任务完了
     */
    public static void greenFinancePrizesMap() {
        int week = TimeUtil.getWeekNumber(new Date());
        Status stat = INSTANCE;
        if (!stat.greenFinancePrizesSet.contains(week)) {
            stat.greenFinancePrizesSet.add(week);
            save();
        }
    }

    public static synchronized Status load() {
        String currentUid = UserIdMap.getCurrentUid();
        try {
            if (StringUtil.isEmpty(currentUid)) {
                Log.i(TAG, "用户为空，状态加载失败");
                throw new RuntimeException("用户为空，状态加载失败");
            }
            File statusFile = FileUtil.getStatusFile(currentUid);
            if (statusFile.exists()) {
                String json = FileUtil.readFromFile(statusFile);
                JsonUtil.copyMapper().readerForUpdating(INSTANCE).readValue(json);
                String formatted = JsonUtil.toFormatJsonString(INSTANCE);
                if (formatted != null && !formatted.equals(json)) {
                    Log.i(TAG, "重新格式化 status.json");
                    Log.system(TAG, "重新格式化 status.json");
                    FileUtil.write2File(formatted, FileUtil.getStatusFile(currentUid));
                }
            } else {
                JsonUtil.copyMapper().updateValue(INSTANCE, new Status());
                Log.i(TAG, "初始化 status.json");
                Log.system(TAG, "初始化 status.json");
                FileUtil.write2File(JsonUtil.toFormatJsonString(INSTANCE), FileUtil.getStatusFile(currentUid));
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
            Log.i(TAG, "状态文件格式有误，已重置");
            Log.system(TAG, "状态文件格式有误，已重置");
            try {
                JsonUtil.copyMapper().updateValue(INSTANCE, new Status());
                FileUtil.write2File(JsonUtil.toFormatJsonString(INSTANCE), FileUtil.getStatusFile(currentUid));
            } catch (JsonMappingException e) {
                Log.printStackTrace(TAG, e);
            }
        }
        if (INSTANCE.saveTime == 0) {
            INSTANCE.saveTime = System.currentTimeMillis();
        }
        return INSTANCE;
    }

    public static synchronized void unload() {
        try {
            JsonUtil.copyMapper().updateValue(INSTANCE, new Status());
        } catch (JsonMappingException e) {
            Log.printStackTrace(TAG, e);
        }
    }

    public static synchronized void save() {
        save(Calendar.getInstance());
    }

    public static synchronized void save(Calendar nowCalendar) {
        String currentUid = UserIdMap.getCurrentUid();
        if (StringUtil.isEmpty(currentUid)) {
            Log.record("用户为空，状态保存失败");
            throw new RuntimeException("用户为空，状态保存失败");
        }
        if (updateDay(nowCalendar)) {
            Log.system(TAG, "重置 status.json");
        } else {
            Log.system(TAG, "保存 status.json");
        }
        long lastSaveTime = INSTANCE.saveTime;
        try {
            INSTANCE.saveTime = System.currentTimeMillis();
            FileUtil.write2File(JsonUtil.toFormatJsonString(INSTANCE), FileUtil.getStatusFile(currentUid));
        } catch (Exception e) {
            INSTANCE.saveTime = lastSaveTime;
            throw e;
        }
    }

    public static Boolean updateDay(Calendar nowCalendar) {
        if (TimeUtil.isLessThanSecondOfDays(INSTANCE.saveTime, nowCalendar.getTimeInMillis())) {
            Status.unload();
            return true;
        } else {
            return false;
        }
    }

    @Data
    private static class WaterFriendLog {
        String userId;
        int waterCount = 0;

        public WaterFriendLog() {
        }

        public WaterFriendLog(String id) {
            userId = id;
        }
    }

    @Data
    private static class ReserveLog {
        String projectId;
        int applyCount = 0;

        public ReserveLog() {
        }

        public ReserveLog(String id) {
            projectId = id;
        }
    }

    @Data
    private static class BeachLog {
        String cultivationCode;
        int applyCount = 0;

        public BeachLog() {
        }

        public BeachLog(String id) {
            cultivationCode = id;
        }
    }

    @Data
    private static class FeedFriendLog {
        String userId;
        int feedCount = 0;

        public FeedFriendLog() {
        }

        public FeedFriendLog(String id) {
            userId = id;
        }
    }

    @Data
    private static class VisitFriendLog {
        String userId;
        int visitCount = 0;

        public VisitFriendLog() {
        }

        public VisitFriendLog(String id) {
            userId = id;
        }
    }

    @Data
    private static class StallShareIdLog {
        String userId;
        String shareId;

        public StallShareIdLog() {
        }

        public StallShareIdLog(String uid, String sid) {
            userId = uid;
            shareId = sid;
        }
    }

    @Data
    private static class StallHelpedCountLog {
        String userId;
        int helpedCount = 0;
        int beHelpedCount = 0;

        public StallHelpedCountLog() {
        }

        public StallHelpedCountLog(String id) {
            userId = id;
        }
    }

    public interface StatusFlag {
        String name();

        default String flagName() {
            return this.getClass().getSimpleName().replace("Flag", "") + "::" + name();
        }

        default String flagName(String info) {
            return flagName() + "::" + info;
        }
    }
}