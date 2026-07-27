package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.data.task.ModelTask.ChildModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.model.task.antForest.AntForestV2;
import io.github.lazyimmortal.sesame.model.task.antForest.AntForestV2.ItemStatus;
import io.github.lazyimmortal.sesame.model.task.antForest.AntForestV2.PropGroup;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class AntForestAlpha {
    private static final String TAG = AntForestAlpha.class.getSimpleName();

    private static void run() {
        AntForestV2 task = ModelTask.getModel(AntForestV2.class);
        if (task != null) {
            task.run();
        }
    }

    private static Boolean addChildTask(ChildModelTask childTask) {
        AntForestV2 task = ModelTask.getModel(AntForestV2.class);
        return task != null && task.addChildTask(childTask);
    }

    // 定时使用道具

    /**
     * 多个时间点使用道具(时间点为字符串)
     *
     * @param propGroup          道具组
     * @param usePropTimeStrList 使用道具的时间(字符串列表:多个时间点)
     * @param usePropOptions     使用道具选项
     */
    private static void addUsePropTask(String propGroup, List<String> usePropTimeStrList, SelectModelField usePropOptions) {
        for (String usePropTimeStr : usePropTimeStrList) {
            addUsePropTask(propGroup, usePropTimeStr, usePropOptions);
        }
    }

    /**
     * 定时使用道具(时间点为字符串)
     *
     * @param propGroup      道具组
     * @param usePropTimeStr 使用道具的时间点(字符串)
     * @param usePropOptions 使用道具选项
     */
    private static void addUsePropTask(String propGroup, String usePropTimeStr, SelectModelField usePropOptions) {
        Calendar usePropTimeCalendar = TimeUtil.getTodayCalendarByTimeStr(usePropTimeStr);
        if (usePropTimeCalendar == null) {
            return;
        }
        long usePropTimeStamp = usePropTimeCalendar.getTimeInMillis();
        if (usePropTimeStamp < System.currentTimeMillis()) {
            usePropTimeStamp += TimeUnit.DAYS.toMillis(1);
        }
        addUsePropTask(propGroup, usePropTimeStamp, usePropOptions);
    }

    /**
     * 定时使用道具(时间点为时间戳)
     *
     * @param propGroup        道具组
     * @param usePropTimeStamp 使用道具的时间点(时间戳)
     * @param usePropOptions   使用道具选项
     */
    private static void addUsePropTask(String propGroup, Long usePropTimeStamp, SelectModelField usePropOptions) {
        if (usePropTimeStamp == null) {
            usePropTimeStamp = System.currentTimeMillis();
        }
        String taskId = "PROP|" + propGroup.toUpperCase() + "|" + UserIdMap.getCurrentUid();
        if (addChildTask(new ChildModelTask(taskId, "PROP", () -> {
            boolean isOnlyUseLimitTimeProp = usePropOptions.contains(AntForestV2.AntForestPropOption.ONLY_USE_LIMIT_TIME_PROP.name());
            boolean isExchangeLimitTimeProp = usePropOptions.contains(AntForestV2.AntForestPropOption.EXCHANGE_LIMIT_TIME_PROP.name());
            if (usePropByGroup(propGroup, isOnlyUseLimitTimeProp, isExchangeLimitTimeProp)) {
                // 如果成功使用的道具是加速器 马上执行森林收取能量
                if (PropGroup.boost.name().equals(propGroup)) {
                    run();
                }
            }
        }, usePropTimeStamp))) {
            String propGroupName = PropGroup.valueOf(propGroup).nickName();
            Log.record("添加定时使用⏱️[" + propGroupName + "]在[" + TimeUtil.getCommonDateTime(usePropTimeStamp) + "]执行");
        }
    }

    private static Boolean usePropByGroup(String propGroup, Boolean isOnlyUseLimitTimeProp, Boolean isExchangeLimitTimeProp) {
        try {
            List<JSONObject> list = AntForestV2.getPropGroup(AntForestV2.getForestPropVOList(), propGroup);
            if (!list.isEmpty()) {
                JSONObject jo = list.get(0);
                if (!jo.has("recentExpireTime")) {
                    // 是永久道具
                    if (isExchangeLimitTimeProp) {
                        // 开启了自动兑换限时道具
                        if (exchangeLimitTimeProp(propGroup)) {
                            // 更新道具列表
                            jo = AntForestV2.getPropGroup(AntForestV2.getForestPropVOList(), propGroup).get(0);
                        }
                    }
                    if (isOnlyUseLimitTimeProp && !jo.has("recentExpireTime")) {
                        // 开启了仅使用限时道具 且 道具是永久道具
                        return false;
                    }
                }
                return AntForestV2.consumeProp(jo);
            }
        } catch (Throwable th) {
            Log.i(TAG, "usePropByGroup err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    private static Boolean exchangeLimitTimeProp(String propGroup) {
        try {
            switch (PropGroup.valueOf(propGroup)) {
                case shield:
                    return AntForestV2.exchangeBenefit("CR20230517000497", "CR20230516000370", "限时3天内使用能量保护罩")
                            || AntForestV2.exchangeBenefit("SP20240905001863", "SK20240905004810", "限时3天内使用敦煌飞天保护罩");
                case boost:
                    return AntForestV2.exchangeBenefit("SP20230521000081", "SK20230521000204", "限时3天内使用时光加速器");
                case stealthCard:
                    return AntForestV2.exchangeBenefit("SP20230521000082", "SK20230521000206", "限时3天内使用隐身卡");
                case doubleClick:
                    return AntForestV2.exchangeBenefit("SP20240805001834", "SK20240805004754", "限时31天内使用31天长效双击卡")
                            || AntForestV2.exchangeBenefit("CR20230516000362", "CR20230516000363", "限时3天内使用能量双击卡");
            }
        } catch (Throwable th) {
            Log.i(TAG, "exchangeLimitTimeProp err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    public static void updateUsingProps() {
        // 获取道具结束时间
        Map<String, Long> usingProps = AntForestV2.usingProps;
        Immortal immortal = ModelTask.getModel(Immortal.class);
        if (immortal.stealthCard.getValue()) {
            Long stealthCardTime = usingProps.get(AntForestV2.PropGroup.stealthCard.name());
            SelectModelField stealthCardOptions = immortal.stealthCardOptions;
            addUsePropTask(AntForestV2.PropGroup.stealthCard.name(), stealthCardTime, stealthCardOptions);
        }
        if (immortal.bubbleBoost.getValue()) {
            List<String> bubbleBoostTime = immortal.bubbleBoostTime.getValue();
            SelectModelField bubbleBoostOptions = immortal.bubbleBoostOptions;
            addUsePropTask(AntForestV2.PropGroup.boost.name(), bubbleBoostTime, bubbleBoostOptions);
        }
        if (immortal.energyShield.getValue()) {
            Long energyShieldTime = usingProps.get(AntForestV2.PropGroup.shield.name());
            SelectModelField energyShieldOptions = immortal.energyShieldOptions;
            addUsePropTask(AntForestV2.PropGroup.shield.name(), energyShieldTime, energyShieldOptions);
        }
    }

    // 使用道具 能量雨机会
    public static void useEnergyRainChance() {
        try {
            while (usePropByGroup(PropGroup.energyRain.name(), true, false)) {
                TimeUtil.sleep(3000);
            }
        } catch (Throwable th) {
            Log.i(TAG, "useEnergyRainChance err:");
            Log.printStackTrace(TAG, th);
        }
    }

    // 活力值扩展
    public static void vitality() {
        SelectModelField antForestVitalityOptions = ModelTask.getModel(Immortal.class).antForestVitalityOptions;
        // 限量秒杀
        if (antForestVitalityOptions.getValue().contains(AntForestVitalityOptions.SEC_KILL.name())) {
            querySecKillItems();
        }
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) % 7 != 0) {
            return;
        }
        // 搜刮皮肤
        if (antForestVitalityOptions.getValue().contains(AntForestVitalityOptions.SKIN.name())) {
            queryVitalityItemList(AntForestVitalityOptions.SKIN.name());
        }
        // 搜刮挂件
        if (antForestVitalityOptions.getValue().contains(AntForestVitalityOptions.JEWELRY.name())) {
            queryVitalityItemList(AntForestVitalityOptions.JEWELRY.name());
        }
    }

    // 限量秒杀
    private static void querySecKillItems() {
        try {
            JSONObject jo = new JSONObject(AntForestAlphaRpcCall.secKill());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            if (!jo.has("secKillSkuModelList")) {
                return;
            }
            JSONArray secKillSkuModelList = jo.getJSONArray("secKillSkuModelList");
            for (int i = 0; i < secKillSkuModelList.length(); i++) {
                jo = secKillSkuModelList.getJSONObject(i);
                JSONArray itemStatusList = jo.getJSONArray("itemStatusList");
                if (!checkSecKillItemStatusList(itemStatusList)) {
                    continue;
                }
                String spuId = jo.getString("spuId");
                String skuId = jo.getString("skuId");
                String skuName = jo.getString("skuName");
                String taskId = "SK|" + skuId;
                long secKillTime = jo.getLong("secKillStartTime");
                if (addChildTask(new ChildModelTask(taskId, "SK", () -> {
                    long secKillEndTime = TimeUnit.MINUTES.toMillis(1) + System.currentTimeMillis();
                    while (!exchangeSkillBenefit(spuId, skuId)) {
                        // 尝试抢购到秒杀时间后1分钟
                        if (secKillEndTime < System.currentTimeMillis()) {
                            break;
                        }
                    }
                    boolean isKilled = Status.hasFlagToday(AntForestAlphaFlag.SEC_KILL.flagName(skuId));
                    Log.forest("蹲点秒杀⚡[" + skuName + "]" + (isKilled ? "成功🎉" : "失败💔"));
                }, secKillTime))) {
                    Log.record("添加蹲点秒杀⏰[" + skuName + "]在[" + TimeUtil.getCommonDateTime(secKillTime) + "]执行");
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "querySecKillItems err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static Boolean checkSecKillItemStatusList(JSONArray itemStatusList) {
        try {
            for (int i = 0; i < itemStatusList.length(); i++) {
                String itemStatus = itemStatusList.getString(i);
                if (ItemStatus.REACH_LIMIT.name().equals(itemStatus)
                        || ItemStatus.NO_ENOUGH_STOCK.name().equals(itemStatus)
                        || ItemStatus.SECKILL_HAS_END.name().equals(itemStatus)
                        || ItemStatus.HAS_NEVER_EXPIRE_DRESS.name().equals(itemStatus)) {
                    return false;
                }
            }
            return true;
        } catch (Throwable th) {
            Log.i(TAG, "checkSecKillItemStatusList err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    private static Boolean exchangeSkillBenefit(String spuId, String skuId) {
        try {
            JSONObject jo = new JSONObject(AntForestAlphaRpcCall.exchangeBenefit(spuId, skuId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Status.flagToday(AntForestAlphaFlag.SEC_KILL.flagName(skuId));
                return true;
            }
            if (Objects.equals("QUOTA_NOT_ENOUGH", jo.optString("resultCode"))) {
                // 库存不足
                return true;
            }
        } catch (Throwable th) {
            Log.i(TAG, "exchangeSkillBenefit err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    // 获取活力值商店列表
    private static void queryVitalityItemList(String labelType) {
        if (Status.hasFlagToday(AntForestAlphaFlag.VITALITY_BENEFIT.flagName(labelType))) {
            return;
        }
        try {
            boolean hasMore;
            int startIndex = 0;
            do {
                JSONObject jo = new JSONObject(AntForestAlphaRpcCall.itemList(labelType, startIndex));
                TimeUtil.sleep(3000);
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    break;
                }
                hasMore = jo.optBoolean("hasMore");
                startIndex = jo.getInt("nextStartIndex");
                JSONArray itemInfoVOList = jo.getJSONArray("itemInfoVOList");
                for (int i = 0; i < itemInfoVOList.length(); i++) {
                    jo = itemInfoVOList.getJSONObject(i);
                    JSONArray itemStatusList = jo.getJSONArray("itemStatusList");
                    if (itemStatusList.length() == 0) {
                        if (exchangeSkuModelList(jo.getJSONArray("skuModelList"))) {
                            // Todo: 更新活力值
                            TimeUtil.sleep(1000);
                        }
                    }
                }
            } while (hasMore);
            Status.flagToday(AntForestAlphaFlag.VITALITY_BENEFIT.flagName(labelType));
        } catch (Throwable th) {
            Log.i(TAG, "queryVitalityItemList err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static Boolean exchangeSkuModelList(JSONArray skuModelList) {
        try {
            String spuId = null, skuId = null, skuName = null;
            for (int i = 0; i < skuModelList.length(); i++) {
                JSONObject jo = skuModelList.getJSONObject(i);
                if (jo.optBoolean("secKill")) {
                    // 秒杀商品不搜刮
                    return false;
                }
                String rightsConfigId = jo.getString("rightsConfigId");
                if (rightsConfigId.contains("NO_EXPIRE")) {
                    // 只搜刮不限时
                    spuId = jo.getString("spuId");
                    skuId = jo.getString("skuId");
                    skuName = jo.getString("skuName");
                }
            }
            if (!StringUtil.isEmpty(skuId)) {
                Log.record("权益搜刮🔍[" + skuName + "]未拥有,即将尝试兑换");
                return AntForestV2.exchangeBenefit(spuId, skuId, skuName);
            }
        } catch (Throwable th) {
            Log.i(TAG, "exchangeSkuModelList err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    public enum AntForestAlphaFlag implements Status.StatusFlag {
        SEC_KILL,
        VITALITY_BENEFIT;
    }

    public enum AntForestVitalityOptions implements CustomOption {
        SEC_KILL("限量秒杀"),
        SKIN("权益搜刮(皮肤)"),
        JEWELRY("权益搜刮(挂件)");

        private final String nickName;

        AntForestVitalityOptions(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }
}
