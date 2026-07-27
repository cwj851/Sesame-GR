package io.github.lazyimmortal.sesame.model.task.antForest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandle;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestMethod;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Statistics;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class ForestSquare {
    private static final String TAG = ForestSquare.class.getSimpleName();

    private static SelectModelField grantEnergyRainChanceList;

    public static void run() {
        SelectModelField forestSquareOptions = ModelTask.getModel(AntForestV2.class).forestSquareOptions;
        grantEnergyRainChanceList = ModelTask.getModel(AntForestV2.class).grantEnergyRainChanceList;
        if (forestSquareOptions.contains(ForestSquareOption.ENERGY_RAIN.name())) {
            energyRain();
        }
        if (forestSquareOptions.contains(ForestSquareOption.WHACK_MOLE.name())) {
            whackMole();
        }
        if (forestSquareOptions.contains(ForestSquareOption.MARKET.name())) {
            greenLife();
        }
    }

    // 能量雨
    private static void energyRain() {
        ExtensionsHandle.handleRequest(
                new Request(
                        RequestType.ENABLE_DEVELOPER_MODE,
                        RequestMethod.ANT_FOREST_ENERGY_RAIN
                )
        );
        try {
            JSONObject jo;
            do {
                jo = new JSONObject(ForestSquareRpcCall.queryEnergyRainHome());
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return;
                }
                if (jo.getBoolean("canGrantStatus")) {
                    Log.record("有送能量雨的机会");
                    queryEnergyRainCanGrantList();
                    TimeUtil.sleep(500);
                }
                if (jo.getBoolean("canPlayToday")) {
                    if (!startEnergyRain()) {
                        return;
                    }
                }
            } while (jo.getBoolean("canPlayToday"));
        } catch (Throwable th) {
            Log.i(TAG, "energyRain err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static Boolean startEnergyRain() {
        try {
            JSONObject jo = new JSONObject(ForestSquareRpcCall.startEnergyRain());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            String token = jo.getString("token");
            JSONArray bubbleEnergyList = jo.getJSONObject("difficultyInfo")
                    .getJSONArray("bubbleEnergyList");
            int sum = 0;
            for (int i = 0; i < bubbleEnergyList.length(); i++) {
                sum += bubbleEnergyList.getInt(i);
            }
            TimeUtil.sleep(TimeUnit.SECONDS.toMillis(jo.optInt("playtime", 10)));
            jo = new JSONObject(ForestSquareRpcCall.energyRainSettlement(sum, token));
            if (MessageUtil.checkResponse(TAG, jo)) {
                ToastUtil.show(ApplicationHook.getContext(), "获得了[" + sum + "g]能量[能量雨]");
                Log.forest("森林广场🌳收能量雨#获得[" + sum + "g能量]");
                Statistics.addData(Statistics.DataType.COLLECTED, sum);
                return true;
            }
        } catch (Throwable th) {
            Log.i(TAG, "startEnergyRain err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    private static void queryEnergyRainCanGrantList() {
        try {
            JSONObject jo = new JSONObject(ForestSquareRpcCall.queryEnergyRainCanGrantList());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray grantInfos = jo.getJSONArray("grantInfos");
            for (int j = 0; j < grantInfos.length(); j++) {
                JSONObject grantInfo = grantInfos.getJSONObject(j);
                if (grantInfo.getBoolean("canGrantedStatus")) {
                    String userId = grantInfo.getString("userId");
                    if (grantEnergyRainChanceList.contains(userId)) {
                        grantEnergyRainChance(userId);
                        return;
                    }
                }
            }
            Log.record("没有可以送的用户");
        } catch (Throwable th) {
            Log.i(TAG, "queryEnergyRainCanGrantList err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void grantEnergyRainChance(String userId) {
        try {
            JSONObject jo = new JSONObject(ForestSquareRpcCall.grantEnergyRainChance(userId));
            String userMaskName = UserIdMap.getMaskName(userId);
            if (MessageUtil.checkResponse("赠送好友[" + userMaskName + "]能量雨机会", jo)) {
                Log.forest("森林广场🌳赠送好友[" + userMaskName + "]能量雨机会");
            }
        } catch (Throwable th) {
            Log.i(TAG, "grantEnergyRainChance err:");
            Log.printStackTrace(TAG, th);
        }
    }

    /* 6秒拼手速 打地鼠 */
    private static void whackMole() {
        if (Status.hasFlagToday(ForestSquareFlag.WHACK_MOLE.flagName())) {
            return;
        }
        try {
            JSONObject jo = new JSONObject(ForestSquareRpcCall.startWhackMole());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            if (!jo.getBoolean("canPlayToday")) {
                Status.flagToday(ForestSquareFlag.WHACK_MOLE.flagName());
                return;
            }
            String token = jo.getString("token");
            JSONArray moleInfo = jo.getJSONArray("moleInfo");
            JSONArray moleIdList = new JSONArray();
            for (int i = 0; i < moleInfo.length(); i++) {
                jo = moleInfo.getJSONObject(i);
                moleIdList.put(jo.getInt("id"));
            }
            if (moleIdList.length() == 0) {
                return;
            }
            TimeUtil.sleep(6000);
            jo = new JSONObject(ForestSquareRpcCall.settlementWhackMole(moleIdList, token));
            if (MessageUtil.checkResponse(TAG, jo)) {
                int totalEnergy = jo.getInt("totalEnergy");
                Log.forest("森林广场🌳赚能量[打地鼠]#获得[" + totalEnergy + "g能量]");
                Status.flagToday(ForestSquareFlag.WHACK_MOLE.flagName());
            }
        } catch (Throwable t) {
            Log.i(TAG, "whackMole err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static Boolean closeWhackMole() {
        try {
            JSONObject jo = new JSONObject(ForestSquareRpcCall.closeWhackMole());
            return MessageUtil.checkResponse(TAG, jo);
        } catch (Throwable t) {
            Log.printStackTrace(t);
        }
        return false;
    }

    /* 森林集市 */
    private static void greenLife() {
        sendEnergyByAction("GREEN_LIFE");
        sendEnergyByAction("ANTFOREST");
        forestMarketLayoutQuery();
    }

    private static void forestMarketLayoutQuery() {
        try {
            JSONObject jo = new JSONObject(GreenLifeRpcCall.forestMarketLayoutQuery());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("asePageInfo");
            if (!jo.has("layoutGroups")) {
                return;
            }
            JSONArray layoutGroups=jo.getJSONArray("layoutGroups");
            String playId="PLAY1024113283";
            for (int i = 0; i < layoutGroups.length(); i++) {
                jo = layoutGroups.getJSONObject(i);
                String aseBlockCode=jo.getString("aseBlockCode");
                if("L2025032828727".equals(aseBlockCode)){
                    JSONObject styleConfig=jo.getJSONArray("subLayouts").getJSONObject(0).getJSONObject("bizParam").getJSONObject("styleConfig");
                    playId=styleConfig.getString("playId");
                    JSONArray activityPrizeLists=styleConfig.getJSONArray("activityPrizeLists");
                    break;
                }
            }
            jo = new JSONObject(GreenLifeRpcCall.signInStatusQuery(playId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            boolean signInToday=jo.optBoolean("signInToday",true);
            if(signInToday){
                return;
            }
            int continuousNum= jo.optInt("continuousNum")+1;
            jo = new JSONObject(GreenLifeRpcCall.signInTrigger(playId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            boolean hasPrizeSend=jo.optBoolean("hasPrizeSend",false);
            Log.forest("森林集市🛍️打卡[连学" + continuousNum + "天]");
        } catch (Throwable t) {
            Log.i(TAG, "retrieveCurrentActivity err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void sendEnergyByAction(String sourceType) {
        try {
            JSONObject jo = new JSONObject(GreenLifeRpcCall.consultForSendEnergyByAction(sourceType));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject data = jo.getJSONObject("data");
            if (data.optBoolean("canSendEnergy", false)) {
                jo = new JSONObject(GreenLifeRpcCall.sendEnergyByAction(sourceType));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    data = jo.getJSONObject("data");
                    if (data.optBoolean("canSendEnergy", false)) {
                        int receivedEnergyAmount = data.getInt("receivedEnergyAmount");
                        Log.forest("森林集市🛍️完成[线上逛街]#产生[" + receivedEnergyAmount + "g能量]");
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "sendEnergyByAction err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public enum ForestSquareOption implements CustomOption {
        ENERGY_RAIN("能量雨"),
        WHACK_MOLE("赚能量(打地鼠)"),
        MARKET("森林集市");

        private final String nickName;

        ForestSquareOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

    private enum ForestSquareFlag implements Status.StatusFlag {
        WHACK_MOLE
    }
}
