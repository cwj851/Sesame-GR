package io.github.lazyimmortal.sesame.model.task.antForest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.DishImageEntity;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.model.base.TaskCommon;
import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.DishImageIdMap;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class EcoLife {
    private static final String TAG = EcoLife.class.getSimpleName();

    public static void run() {
        try {
            JSONObject jo = new JSONObject(EcoLifeRpcCall.queryHomePage());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject data = jo.getJSONObject("data");
            if (!data.getBoolean("openStatus")) {
                Log.forest("绿色任务☘未开通");
                jo = new JSONObject(EcoLifeRpcCall.openEcolife());
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return;
                }
                if (!String.valueOf(true).equals(JsonUtil.getValueByPath(jo, "data.opResult"))) {
                    return;
                }
                Log.forest("绿色任务🍀报告大人，开通成功(～￣▽￣)～可以愉快的玩耍了");
                jo = new JSONObject(EcoLifeRpcCall.queryHomePage());
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    return;
                }
                data = jo.getJSONObject("data");
            }
            String dayPoint = data.getString("dayPoint");
            JSONArray actionListVO = data.getJSONArray("actionListVO");
            SelectModelField ecoLifeOptions = ModelTask.getModel(AntForestV2.class).ecoLifeOptions;
            if (ecoLifeOptions.contains(EcoLifeOption.TICK.name())) {
                ecoLifeTick(actionListVO, dayPoint);
            }
            if (ecoLifeOptions.contains(EcoLifeOption.DISH.name())) {
                queryDish();
            }
            if (ecoLifeOptions.contains(EcoLifeOption.EASE_CONSUME.name())) {
                easeConsumeGenerateEnergy();
            }
        } catch (Throwable th) {
            Log.i(TAG, "ecoLife err:");
            Log.printStackTrace(TAG, th);
        }
    }

    /* 绿色行动打卡 */
    private static void ecoLifeTick(JSONArray actionListVO, String dayPoint) {
        try {
            String source = "source";
            for (int i = 0; i < actionListVO.length(); i++) {
                JSONObject actionVO = actionListVO.getJSONObject(i);
                JSONArray actionItemList = actionVO.getJSONArray("actionItemList");
                for (int j = 0; j < actionItemList.length(); j++) {
                    JSONObject actionItem = actionItemList.getJSONObject(j);
                    if (!actionItem.has("actionId")) {
                        continue;
                    }
                    if (actionItem.getBoolean("actionStatus")) {
                        continue;
                    }
                    String actionId = actionItem.getString("actionId");
                    String actionName = actionItem.getString("actionName");
                    if ("photoguangpan".equals(actionId)) {
                        continue;
                    }
                    JSONObject jo = new JSONObject(EcoLifeRpcCall.tick(actionId, dayPoint, source));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        Log.forest("绿色打卡🍀[" + actionName + "]");
                    }
                    TimeUtil.sleep(500);
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "ecoLifeTick err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void easeConsumeGenerateEnergy() {
        try {
            if (Status.canEaseConsumeGenerateEnergy()) {
                JSONObject jo = new JSONObject(EcoLifeRpcCall.easeConsumeGenerateEnergy(UserIdMap.getCurrentUid()));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    Status.easeConsumeGenerateEnergy();
                    return;
                }
                if(jo.has("errorCode")){
                    if("EASE_CONSUME_GENERATE_ENERGY_LIMIT".equals(jo.getString("errorCode"))){
                        Status.easeConsumeGenerateEnergy();
                        return;
                    }
                }
                Log.forest("放心消费🍀[打卡]");
                Status.easeConsumeGenerateEnergy();
            }
        } catch (Throwable th) {
            Log.i(TAG, "easeConsumeGenerateEnergy err:");
            Log.printStackTrace(TAG, th);
        }
    }

    /* 光盘行动 */
    public static void queryDish() {
        try {
            //检查今日任务状态
            JSONObject jo = new JSONObject(EcoLifeRpcCall.queryDish());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("data");
            String dayPoint = jo.getString("dayPoint");
            int totalDays = jo.optInt("totalDays") + 1;
            DishImageIdMap.getInstance().load();
            if (Objects.equals("SUCCESS", jo.getString("status"))) {
                // 打卡完成，更新光盘行动照片
                String beforeMeals = StringUtil.getSubString(jo.getString("beforeMealsImageUrl"),
                        "https://mdn.alipayobjects.com/afts/img/", "/original");
                String afterMeals = StringUtil.getSubString(jo.getString("afterMealsImageUrl"),
                        "https://mdn.alipayobjects.com/afts/img/", "/original");
                DishImageIdMap.getInstance().add(new DishImageEntity(beforeMeals, afterMeals));
//                Log.forest("光盘行动💿今日打卡已完成");
                return;
            }

            DishImageEntity entity = DishImageIdMap.getInstance().getRandomDishImage();
            if (!entity.checkDishImage()) {
                Log.forest("光盘行动💿请先完成一次光盘打卡");
                return;
            }
            //上传餐前照片
            jo = new JSONObject(EcoLifeRpcCall.uploadBeforeMealsDishImage(entity.getBeforeMeals(), dayPoint));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            //上传餐后照片
            jo = new JSONObject(EcoLifeRpcCall.uploadAfterMealsDishImage(entity.getAfterMeals(), dayPoint));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            //提交
            jo = new JSONObject(EcoLifeRpcCall.tick("photoguangpan", dayPoint, "renwuGD"));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            String toastMsg = jo.getJSONObject("data").getString("toastMsg");
            Log.forest("光盘行动💿坚持打卡[第" + totalDays + "天]#" + toastMsg);
        } catch (Throwable t) {
            Log.i(TAG, "photoGuangPan err:");
            Log.printStackTrace(TAG, t);
        }
    }


    public enum EcoLifeOption implements CustomOption {
        TICK("绿色行动打卡"),
        DISH("光盘行动打卡"),
        EASE_CONSUME("放心消费能量");

        private final String nickName;

        EcoLifeOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }

}
