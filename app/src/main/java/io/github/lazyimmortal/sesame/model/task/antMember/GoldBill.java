package io.github.lazyimmortal.sesame.model.task.antMember;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.StringUtil;

public class GoldBill {
    private static final String TAG = GoldBill.class.getSimpleName();

    public static void run() {
        index();
        collect("CP1417744");
    }

    private static void index() {
        if (Status.hasFlagToday(GoldBillFlag.SIGN.flagName())) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(GoldBillRpcCall.index());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("result").getJSONObject("upsertData");
            JSONArray timeline = jo.getJSONObject("sign").getJSONArray("timeline");
            for (int i = 0; i < timeline.length(); i++) {
                jo = timeline.getJSONObject(i);
                if (jo.getBoolean("isToday")) {
                    boolean signed = jo.getBoolean("signed");
                    if (!signed) {
                        int base = jo.getInt("base");
                        jo = new JSONObject(GoldBillRpcCall.trigger("SIGN"));
                        if (MessageUtil.checkResponse(TAG, jo)) {
                            Log.other("我的黄金💰周周领福利#获得[" + base + "份黄金票]");
                            signed = true;
                        }
                    }
                    if (signed) {
                        Status.flagToday(GoldBillFlag.SIGN.flagName());
                    }
                    return;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "index err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void collect(String campId) {
        if (Status.hasFlagToday(GoldBillFlag.COLLECT.flagName())) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(GoldBillRpcCall.collect(campId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            Status.flagToday(GoldBillFlag.COLLECT.flagName());
            jo = jo.getJSONObject("result");
            if (jo.has("collectedCamp")) {
                jo = jo.getJSONObject("collectedCamp");
                int amount = jo.getInt("amount");
                if (!StringUtil.isEmpty(jo.optString("prizeScene"))) {
                    Log.other("我的黄金💰" + jo.getString("prizeScene") + "#获得[" + amount + "份黄金票]");
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "collect err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private enum GoldBillFlag implements Status.StatusFlag {
        SIGN, COLLECT
    }
}
