package io.github.lazyimmortal.sesame.model.task.antForest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.lazyimmortal.sesame.hook.Toast;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.TimeUtil;

/**
 * 6秒拼手速打地鼠
 * 执行逻辑对齐 xrz 分支 ForestSquare.whackMole()：
 * 单局 start -> 校验 canPlayToday -> 收集全部地鼠 id -> 等6秒 -> 一次性结算
 */
public class WhackMole {
    private static final String TAG = "WhackMole";
    private static final String SOURCE = "senlinguangchangdadishu";
    private static final String EXEC_FLAG = "forest::whackMole::executed";

    public static void start() {
        if (Status.hasFlagToday(EXEC_FLAG)) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(AntForestRpcCall.startWhackMole());
            if (!MessageUtil.checkSuccess(TAG, jo)) {
                return;
            }
            if (!jo.optBoolean("canPlayToday")) {
                Status.flagToday(EXEC_FLAG);
                return;
            }
            String token = jo.optString("token");
            JSONArray moleInfo = jo.optJSONArray("moleInfo");
            if (token.isEmpty() || moleInfo == null) {
                return;
            }
            List<String> moleIdList = new ArrayList<>();
            for (int i = 0; i < moleInfo.length(); i++) {
                moleIdList.add(String.valueOf(moleInfo.getJSONObject(i).getInt("id")));
            }
            if (moleIdList.isEmpty()) {
                return;
            }
            TimeUtil.sleep(6000);
            jo = new JSONObject(AntForestRpcCall.settlementWhackMole(token, moleIdList, SOURCE));
            if (MessageUtil.checkSuccess(TAG, jo)) {
                int totalEnergy = jo.optInt("totalEnergy", 0);
                Toast.show("打地鼠获得[" + totalEnergy + "g能量]");
                Log.forest("森林能量⚡️[打地鼠]#获得[" + totalEnergy + "g能量]");
                Status.flagToday(EXEC_FLAG);
            }
        } catch (Throwable t) {
            Log.i(TAG, "whackMole err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public static Boolean closeWhackMole() {
        try {
            JSONObject jo = new JSONObject(AntForestRpcCall.closeWhackMole());
            return MessageUtil.checkSuccess(TAG, jo);
        }
        catch (Throwable t) {
            Log.printStackTrace(t);
        }
        return false;
    }
}
