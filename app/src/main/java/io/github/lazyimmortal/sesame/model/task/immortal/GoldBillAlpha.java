package io.github.lazyimmortal.sesame.model.task.immortal;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;

public class GoldBillAlpha {
    private static final String TAG = GoldBillAlpha.class.getSimpleName();

    public static void run() {
        index();
    }

    private static void index() {
        try {
            JSONObject jo = new JSONObject(GoldBillAlphaRpcCall.index());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("result").getJSONObject("upsertData")
                    .getJSONObject("assetInfo").getJSONObject("config")
                    .getJSONObject("collectConfig");
            JSONArray campSceneList = jo.getJSONArray("campSceneList");
            for (int i = 0; i < campSceneList.length(); i++) {
                jo = campSceneList.getJSONObject(i);
                String campId = jo.getString("campId");
                collect(campId);
            }
        } catch (Throwable t) {
            Log.i(TAG, "index err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void collect(String campId) {
        if (Status.hasFlagToday(GoldBillAlphaFlag.COLLECT.flagName(campId))) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(GoldBillAlphaRpcCall.collect(campId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            Status.flagToday(GoldBillAlphaFlag.COLLECT.flagName(campId));
            jo = jo.getJSONObject("result");
            if (jo.has("collectedCamp")) {
                jo = jo.getJSONObject("collectedCamp");
                int amount = jo.getInt("amount");
                String prizeScene = jo.getString("prizeScene");
                Log.other("我的黄金💰" + prizeScene + "#获得[" + amount + "份黄金票]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "collect err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private enum GoldBillAlphaFlag implements Status.StatusFlag {
        COLLECT
    }
}
