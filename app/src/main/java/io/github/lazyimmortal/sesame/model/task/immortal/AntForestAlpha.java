package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.data.task.ModelTask.ChildModelTask;
import io.github.lazyimmortal.sesame.model.task.antForest.AntForestV2;
import io.github.lazyimmortal.sesame.model.task.antForest.AntForestV2.ItemStatus;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.TimeUtil;

public class AntForestAlpha {
    private static final String TAG = AntForestAlpha.class.getSimpleName();

    private static Boolean addChildTask(ChildModelTask childTask) {
        AntForestV2 task = ModelTask.getModel(AntForestV2.class);
        return task != null && task.addChildTask(childTask);
    }

    // 活力值秒杀
    public static void secKill() {
        querySecKillItems();
    }

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
                        if (secKillEndTime < System.currentTimeMillis()) {
                            break;
                        }
                    }
                    boolean isKilled = Status.hasFlagToday("AntForestAlpha::SEC_KILL::" + skuId);
                    Log.forest("蹲点秒杀⚡[" + skuName + "]" + (isKilled ? "成功🎉" : "失败💔"));
                }, secKillTime))) {
                    Log.record("添加蹲点秒杀⏰[" + skuName + "]在[" + TimeUtil.getCommonDate(secKillTime) + " " + TimeUtil.getTimeStr(secKillTime) + "]执行");
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
                Status.flagToday("AntForestAlpha::SEC_KILL::" + skuId);
                return true;
            }
            if (Objects.equals("QUOTA_NOT_ENOUGH", jo.optString("resultCode"))) {
                return true;
            }
        } catch (Throwable th) {
            Log.i(TAG, "exchangeSkillBenefit err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }
}
