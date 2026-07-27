package io.github.lazyimmortal.sesame.model.task.antMember;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;

public class GameCenter {
    private static final String TAG = GameCenter.class.getSimpleName();

    public static void run() {
        SelectModelField gameCenterOptions = ModelTask.getModel(AntMember.class).gameCenterOptions;
        if (gameCenterOptions.contains(GameCenterOption.SIGN_IN.name())) {
            querySignInBall();
        }
        if (gameCenterOptions.contains(GameCenterOption.LUCKY_DRAW.name())) {
            queryModularTaskList();
        }
        if (gameCenterOptions.contains(GameCenterOption.RECEIVE_POINT_BALL.name())) {
            queryPointBallList();
        }
    }

    private static void querySignInBall() {
        try {
            JSONObject jo = new JSONObject(GameCenterRpcCall.querySignInBall());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data").getJSONObject("signInBallModule");
            if (!jo.getBoolean("signInStatus")) {
                continueSignIn();
            }
        } catch (Throwable t) {
            Log.i(TAG, "querySignInBall err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void continueSignIn() {
        try {
            JSONObject jo = new JSONObject(GameCenterRpcCall.continueSignIn());
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data").getJSONObject("autoSignInToastModule");
                String pointAmount = StringUtil.getSubString(jo.getString("desc"), "玩乐豆+", null);
                Log.other("游戏中心🎮每日签到#获得[" + pointAmount + "玩乐豆]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "continueSignIn err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void queryModularTaskList() {
        try {
            JSONObject jo = new JSONObject(GameCenterRpcCall.queryModularTaskList());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            JSONArray taskModuleList = jo.getJSONArray("taskModuleList");
            for (int i = 0; i < taskModuleList.length(); i++) {
                jo = taskModuleList.getJSONObject(i);
                JSONArray taskList = jo.getJSONArray("taskList");
                for (int j = 0; j < taskList.length(); j++) {
                    doTask(taskList.getJSONObject(j));
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryModularTaskList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void doTask(JSONObject task) {
        try {
            if (!Objects.equals("VIEW", task.getString("actionType"))) {
                return;
            }
            String taskId = task.getString("taskId");
            String subTitle = task.getString("subTitle");
            String taskStatus = task.getString("taskStatus");
            int prizeAmount = task.getInt("prizeAmount");
            if (Objects.equals("NOT_DONE", taskStatus) && task.getBoolean("needSignUp")) {
                // 注册任务
                if (!MessageUtil.checkResponse(TAG, new JSONObject(GameCenterRpcCall.doTaskSignup(taskId)))) {
                    return;
                }
                // Log.other("游戏中心🎮注册任务[" + subTitle + "]");
            }
            // 完成任务
            if (MessageUtil.checkResponse(TAG, new JSONObject(GameCenterRpcCall.doTaskSend(taskId)))) {
                Log.other("游戏中心🎮完成任务[" + subTitle + "]#待领[" + prizeAmount + "玩乐豆]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "doTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void queryPointBallList() {
        try {
            JSONObject jo = new JSONObject(GameCenterRpcCall.queryPointBallList());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            JSONArray pointBallList = jo.getJSONArray("pointBallList");
            if (pointBallList.length() > 0) {
                batchReceivePointBall();
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryPointBallList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void batchReceivePointBall() {
        try {
            JSONObject jo = new JSONObject(GameCenterRpcCall.batchReceivePointBall());
            if (MessageUtil.checkResponse(TAG, jo)) {
                jo = jo.getJSONObject("data");
                String totalAmount = jo.getString("totalAmount");
                Log.other("游戏中心🎮批量领取#获得[" + totalAmount + "玩乐豆]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "batchReceivePointBall err:");
            Log.printStackTrace(TAG, t);
        }
    }

    @Deprecated
    public static Boolean queryGameAggCard(String source) {
        try {
            JSONObject jo = new JSONObject(GameCenterRpcCall.queryGameAggCard(source));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            jo = jo.getJSONObject("data");
            JSONArray gameAggCardList = jo.getJSONArray("gameAggCardList");
            for (int i = 0; i < gameAggCardList.length(); i++) {
                jo = gameAggCardList.getJSONObject(i);
                String cardId = jo.getString("cardId");
                if (!Objects.equals(source, cardId)) {
                    continue;
                }
                jo = jo.getJSONObject("channelUndertakeModule");
                String taskStatus = jo.optString("taskStatus");
                if (Objects.equals("DONE", taskStatus)) {
                    return false;
                }
                jo = jo.getJSONArray("gameInfoList").getJSONObject(0);
                return consultFloatingBall(jo, source);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryGameAggCard err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    private static Boolean consultFloatingBall(JSONObject gameInfo, String gameModuleId) {
        try {
            String name = gameInfo.getString("name");
            String gameId = gameInfo.getString("gameId");
            JSONObject jo = new JSONObject(GameCenterRpcCall.consultFloatingBall(gameId, gameModuleId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            jo = jo.getJSONObject("data").getJSONObject("floatingBallVO");
            long timeSeconds = jo.getLong("timeSeconds");
            Log.other("游戏中心🎮秒完游戏[" + name + "]#等待[" + timeSeconds + "秒]");
            TimeUtil.sleep(TimeUnit.SECONDS.toMillis(timeSeconds));
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "consultFloatingBall err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    public enum GameCenterOption implements CustomOption {
        SIGN_IN("每日签到"),
        LUCKY_DRAW("赚玩乐豆"),
        RECEIVE_POINT_BALL("领玩乐豆");

        private final String nickName;

        GameCenterOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }
}
