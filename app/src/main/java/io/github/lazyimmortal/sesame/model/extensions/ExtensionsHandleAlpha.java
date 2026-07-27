package io.github.lazyimmortal.sesame.model.extensions;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.RuntimeInfo;
import io.github.lazyimmortal.sesame.data.TokenConfig;
import io.github.lazyimmortal.sesame.data.modelFieldExt.EmptyModelField;
import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.model.extensions.logModel.LogType;
import io.github.lazyimmortal.sesame.model.normal.answerAI.AnswerAI;
import io.github.lazyimmortal.sesame.model.task.antDodo.AntDodoRpcCall;
import io.github.lazyimmortal.sesame.model.task.antFarm.AntFarmRpcCall;
import io.github.lazyimmortal.sesame.model.task.antMember.MerchantServiceRpcCall;
import io.github.lazyimmortal.sesame.model.task.antOrchard.AntOrchardRpcCall;
import io.github.lazyimmortal.sesame.model.task.antSports.AntSports;
import io.github.lazyimmortal.sesame.model.task.immortal.AntFarmAlpha;
import io.github.lazyimmortal.sesame.model.task.immortal.AntForestAlpha;
import io.github.lazyimmortal.sesame.model.task.immortal.AntMemberAlpha;
import io.github.lazyimmortal.sesame.model.task.immortal.Immortal;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestHandler;
import io.github.lazyimmortal.sesame.rpc.request.RequestMethod;
import io.github.lazyimmortal.sesame.rpc.request.RequestRpcCall;
import io.github.lazyimmortal.sesame.util.IntentUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;
import io.noties.markwon.Markwon;

public class ExtensionsHandleAlpha {
    private static final String TAG = ExtensionsHandleAlpha.class.getSimpleName();

    public static Object handleAlphaRequest(Request request) {
        AlphaRequestType alphaRequestType = AlphaRequestType.getAlphaRequestType(request.type);
        switch (alphaRequestType) {
            case UNKNOWN_TYPE:
                return null;
            case ENABLE_DEVELOPER_MODE:
                return developerMode(request);
            case ADD_EXTENSION_VIEW:
                addExtensionView(request.method, (LinearLayout) request.data);
            case RECORD_RUNTIME_INFO:
                return recordRuntimeInfo((Object[]) request.data);
            case PRC:
                test(request.method, (String) request.data);
                break;
            case DO_FARM_TASK:
                return AntFarmAlpha.doFarmTask((JSONObject) request.data);
            case DO_FARM_DRAW_TIMES_TASK:
                return AntFarmAlpha.doFarmDrawTimesTask((JSONObject) request.data);
            case DO_FARM_IP_DRAW_TASK:
                AntFarmAlpha.listFarmIpDrawTask();
                break;
            case GET_WATERING_LEFT_TIMES:
                getWateringLeftTimes();
                break;
            case BATCH_HIRE_ANIMAL_RECOMMEND:
                batchHireAnimalRecommend();
                break;
            case SEND_ANT_DODO_ALL_CARD:
                sendAntDodoCard(request.method, (String) request.data, true);
                break;
            case SEND_ANT_DODO_ONE_SET_CARD:
                sendAntDodoCard(request.method, (String) request.data, false);
                break;
            case SEND_ANT_DODO_ONE_WHOLE_SET_CARD:
                sendAntDodoOneWholeSetCard(request.method, (String) request.data);
                break;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static void modelOrderAddClass(Object list) {
        modelOrderAddClass((List<Class<? extends Model>>) list);
    }

    public static void modelOrderAddClass(List<Class<? extends Model>> list) {
        list.add(Immortal.class);
    }

    private static void test(String fun, String data) {
        Log.debug("收到测试消息:\n方法:" + fun + "\n数据:" + data + "\n结果:" + ApplicationHook.requestString(fun, data));
    }

    private static Boolean recordRuntimeInfo(Object[] recordArray) {
        Object method = recordArray[1];
        Object args = recordArray[2];
        Object data = recordArray[3];
        try {
            switch (method.toString()) {
                case "com.alipay.antfishpond.fishpondAngle": {
                    JSONObject jo = new JSONObject(args.toString());
                    jo = jo.getJSONArray("requestData").getJSONObject(0);
                    RuntimeInfo.getInstance().put(RuntimeInfo.RuntimeInfoKey.FishPondAngleBizNo, jo.getString("bizNo"));
                    RuntimeInfo.getInstance().put(RuntimeInfo.RuntimeInfoKey.FishPondAngleRiskToken, jo.getString("riskToken").replace("\"", "\\\""));
                    RuntimeInfo.getInstance().put(RuntimeInfo.RuntimeInfoKey.FishPondAngleTokenUpdateTime, System.currentTimeMillis());
                    return true;
                }
                case "com.alipay.antfarm.visitAnimal": {
                    JSONArray ja = new JSONObject(data.toString()).optJSONArray("talkNodes");
                    if (ja == null) {
                        return false;
                    }
                    ja = ja.getJSONObject(0).getJSONArray("actionNodes");
                    String consistencyKey = ja.getJSONObject(0).getString("consistencyKey");
                    JSONObject jo = new JSONObject(AntFarmRpcCall.visitAnimalSendPrize(consistencyKey));
                    if (MessageUtil.checkResponse(TAG, jo)) {
                        String prizeName = jo.getString("prizeName");
                        String userMaskName = UserIdMap.getCurrentMaskName();
                        Log.farm("小鸡到访💞爱心投喂[" + userMaskName + "]#获得[" + prizeName + "]");
                        return true;
                    }
                    return false;
                }
                default:
                    return null;
            }
        } catch (Throwable t) {
            Log.i(TAG, "recordRuntimeInfo err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    public static String queryEnvironmentCertDetailList(String alias, int pageNum, String targetUserID) {
        return RequestRpcCall.queryEnvironmentCertDetailList(alias, pageNum, targetUserID);
    }

    public static String sendTree(String certificateId, String friendUserId) {
        return RequestRpcCall.sendTree(certificateId, friendUserId);
    }

    private static void getWateringLeftTimes() {
        try {
            JSONObject jo = new JSONObject(AntOrchardRpcCall.orchardIndex());
            if ("100".equals(jo.getString("resultCode"))) {
                String taobaoData = jo.getString("taobaoData");
                jo = new JSONObject(taobaoData);
                JSONObject plantInfo = jo.getJSONObject("gameInfo").getJSONObject("plantInfo");
                /*
                 * boolean canExchange = plantInfo.getBoolean("canExchange");
                 * if (canExchange) {
                 * Log.farm("农场果树似乎可以兑换了！");
                 * return;
                 * }
                 */
                JSONObject accountInfo = jo.getJSONObject("gameInfo").getJSONObject("accountInfo");
                int wateringLeftTimes = accountInfo.getInt("wateringLeftTimes");
                Log.farm("今日剩余施肥次数[" + wateringLeftTimes + "]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "getWateringLeftTimes err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void batchHireAnimalRecommend() {
        try {
            JSONObject jo = new JSONObject(AntOrchardRpcCall.batchHireAnimalRecommend(UserIdMap.getCurrentUid()));
            if ("100".equals(jo.getString("resultCode"))) {
                JSONArray recommendGroupList = jo.optJSONArray("recommendGroupList");
                if (recommendGroupList != null && recommendGroupList.length() > 0) {
                    List<String> GroupList = new ArrayList<>();
                    for (int i = 0; i < recommendGroupList.length(); i++) {
                        jo = recommendGroupList.getJSONObject(i);
                        String animalUserId = jo.getString("animalUserId");
                        int earnManureCount = jo.getInt("earnManureCount");
                        String groupId = jo.getString("groupId");
                        String orchardUserId = jo.getString("orchardUserId");
                        GroupList.add("{\"animalUserId\":\"" + animalUserId + "\",\"earnManureCount\":"
                                + earnManureCount + ",\"groupId\":\"" + groupId + "\",\"orchardUserId\":\""
                                + orchardUserId + "\"}");
                    }
                    if (!GroupList.isEmpty()) {
                        jo = new JSONObject(AntOrchardRpcCall.batchHireAnimal(GroupList));
                        if ("100".equals(jo.getString("resultCode"))) {
                            Log.farm("一键捉鸡🐣[除草]");
                        }
                    }
                }
            } else {
                Log.record(jo.getString("resultDesc") + jo.toString());
            }
        } catch (Throwable t) {
            Log.i(TAG, "batchHireAnimalRecommend err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void sendAntDodoCard(String bookIdInfo, String targetUser, boolean sendAll) {
        try {
            JSONObject jo = new JSONObject(bookIdInfo);
            JSONArray bookIdList = jo.getJSONArray("bookIdList");
            for (int i = 0; i < bookIdList.length(); i++) {
                JSONObject bookInfo = bookIdList.getJSONObject(i);
                if (sendAll) {
                    sendAntDodoAllCard(bookInfo.getString("bookId"), targetUser);
                } else {
                    sendAntDodoOneSetCard(bookInfo.getString("bookId"), targetUser);
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "sendAntDodoCard err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void sendAntDodoAllCard(String bookId, String targetUser) {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.queryBookInfo(bookId));
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray animalForUserList = jo.getJSONObject("data").optJSONArray("animalForUserList");
                for (int i = 0; i < animalForUserList.length(); i++) {
                    JSONObject animalForUser = animalForUserList.getJSONObject(i);
                    int count = animalForUser.getJSONObject("collectDetail").optInt("count");
                    if (count <= 0)
                        continue;
                    JSONObject animal = animalForUser.getJSONObject("animal");
                    String animalId = animal.getString("animalId");
                    String ecosystem = animal.getString("ecosystem");
                    String name = animal.getString("name");
                    for (int j = 0; j < count; j++) {
                        jo = new JSONObject(AntDodoRpcCall.social(animalId, targetUser));
                        if ("SUCCESS".equals(jo.getString("resultCode"))) {
                            Log.forest("赠送卡片🦕[" + UserIdMap.getMaskName(targetUser) + "]#" + ecosystem + "-" + name);
                        } else {
                            Log.i(TAG, jo.getString("resultDesc"));
                        }
                        TimeUtil.sleep(500L);
                    }
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "sendAntDodoAllCard err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void sendAntDodoOneSetCard(String bookId, String targetUser) {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.queryBookInfo(bookId));
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray animalForUserList = jo.getJSONObject("data").optJSONArray("animalForUserList");
                for (int i = 0; i < animalForUserList.length(); i++) {
                    JSONObject animalForUser = animalForUserList.getJSONObject(i);
                    int count = animalForUser.getJSONObject("collectDetail").optInt("count");
                    if (count <= 0)
                        continue;
                    JSONObject animal = animalForUser.getJSONObject("animal");
                    String animalId = animal.getString("animalId");
                    String ecosystem = animal.getString("ecosystem");
                    String name = animal.getString("name");
                    jo = new JSONObject(AntDodoRpcCall.social(animalId, targetUser));
                    if ("SUCCESS".equals(jo.getString("resultCode"))) {
                        Log.forest("赠送卡片🦕[" + UserIdMap.getMaskName(targetUser) + "]#" + ecosystem + "-" + name);
                    } else {
                        Log.i(TAG, jo.getString("resultDesc"));
                    }
                    TimeUtil.sleep(500L);
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "sendAntDodoOneSetCard err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void sendAntDodoOneWholeSetCard(String bookId, String targetUser) {
        try {
            JSONObject jo = new JSONObject(AntDodoRpcCall.queryBookInfo(bookId));
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray animalForUserList = jo.getJSONObject("data").optJSONArray("animalForUserList");
                for (int i = 0; i < animalForUserList.length(); i++) {
                    JSONObject animalForUser = animalForUserList.getJSONObject(i);
                    int count = animalForUser.getJSONObject("collectDetail").optInt("count");
                    if (count <= 0)
                        return;
                }
                for (int j = 0; j < animalForUserList.length(); j++) {
                    JSONObject animalForUser = animalForUserList.getJSONObject(j);
                    JSONObject animal = animalForUser.getJSONObject("animal");
                    String animalId = animal.getString("animalId");
                    String ecosystem = animal.getString("ecosystem");
                    String name = animal.getString("name");
                    jo = new JSONObject(AntDodoRpcCall.social(animalId, targetUser));
                    if ("SUCCESS".equals(jo.getString("resultCode"))) {
                        Log.forest("赠送卡片🦕[" + UserIdMap.getMaskName(targetUser) + "]#" + ecosystem + "-" + name);
                    } else {
                        Log.i(TAG, jo.getString("resultDesc"));
                    }
                    TimeUtil.sleep(500L);
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "sendAntDodoOneWholeSetCard err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static Object developerMode(Request request) {
        if (request.method == null) {
            return true;
        }
        RequestMethod requestMethod = RequestMethod.valueOf(request.method);
        switch (requestMethod) {
            case MODEL_ORDER_ADD_CLASS:
                modelOrderAddClass(request.data);
                break;
            case ANT_FOREST_UPDATE_USING_PROPS:
                AntForestAlpha.updateUsingProps();
                break;
            case ANT_FOREST_ENERGY_RAIN:
                AntForestAlpha.useEnergyRainChance();
                break;
            case ANT_FARM_FAMILY:
                AntFarmAlpha.listFamilyTask((JSONArray) request.data);
                break;
            case ANT_SPORTS_WALK_PATH:
                return antSportsWalkPath();
            case ANT_MEMBER_SIGN_IN_TASK:
                antMemberSignInTask(request.data);
                break;
            case MERCHANT_SERVICE_HIDE_TASK:
                merchantServiceHideTask();
                break;
        }
        return null;
    }

    private static void addExtensionView(String viewName, LinearLayout linearLayout) {
        Context context = linearLayout.getContext();
        if (Objects.equals(context.getString(R.string.developer_mode), viewName)) {
            addDeveloperModeView(context, linearLayout);
        } else if (Objects.equals("芝麻问答", viewName)) {
            addSesameAskView(context, linearLayout);
        }

    }

    private static void addDeveloperModeView(Context context, LinearLayout linearLayout) {
        EditText etMethod = new EditText(context);
        etMethod.setHint(context.getString(R.string.please_input_method));
        etMethod.setMaxLines(3);
        EditText etData = new EditText(linearLayout.getContext());
        etData.setHint(context.getString(R.string.please_input_data));
        etData.setMaxLines(10);
        EmptyModelField sendRpcRequest = new EmptyModelField("sendRpcRequest",
                context.getString(R.string.send_rpc_request), (c, m) -> {
            String rpc_method = etMethod.getText().toString();
            String rpc_data = etData.getText().toString();
            sendRpcRequest(c, rpc_method, rpc_data);
        });
        EmptyModelField viewDebugLog = new EmptyModelField("viewDebugLog",
                context.getString(R.string.view_debug_log), (c, m) -> IntentUtil.viewLog(c, LogType.DEBUG_LOG)
        );
        linearLayout.addView(etMethod);
        linearLayout.addView(etData);
        linearLayout.addView(sendRpcRequest.getView(context));
        linearLayout.addView(viewDebugLog.getView(context));
    }

    private static void sendRpcRequest(Context context, String rpc_method, String rpc_data) {
        if (rpc_method.isEmpty() || rpc_data.isEmpty()) {
            ToastUtil.show(context, "请求不能为空");
            return;
        }
        RequestHandler.sendRequestBroadcast(context,
                new Request(
                        ExtensionsHandleAlpha.AlphaRequestType.PRC.name(), rpc_method, rpc_data
                )
        );
        ToastUtil.show(context, "已发送Rpc请求，请在debug日志查看结果！");
    }

    private static void addSesameAskView(Context context, LinearLayout linearLayout) {
        EditText editText = new EditText(context);
        editText.setHint("请输入要询问的问题");
        editText.setMaxLines(5);

        TextView textView = new TextView(context);
        textView.setVerticalScrollBarEnabled(true);
        Markwon markwon = Markwon.create(context);

        AnswerAI answerAI = Model.getModel(AnswerAI.class);
        if (answerAI != null) {
            answerAI.boot(null);
        }
        EmptyModelField askAI = new EmptyModelField("", "问一问AI", (c, m) -> {
            String message = editText.getText().toString();
            if (StringUtil.isEmpty(message)) {
                ToastUtil.show(c, "内容不能为空");
                return;
            }
            textView.setText("大模型接收信息中:" + message);
            editText.getText().clear();
            new Thread(() -> {
                String text = "AI:\n\n" + AnswerAI.getAnswer(message);
                textView.post(() -> markwon.setMarkdown(textView, "user:\n\n" + message + "\n\n" + text));
            }).start();
        });
        EmptyModelField chatLog = new EmptyModelField("", "查看问答记录", (c, m) -> IntentUtil.viewLog(c, LogType.CHAT_LOG));
        linearLayout.addView(editText);
        linearLayout.addView(askAI.getView(context));
        linearLayout.addView(chatLog.getView(context));
        linearLayout.addView(textView);

    }

    private static String antSportsWalkPath() {
        TimeUtil.sleep(1000);
        if (Immortal.getWalkDayReward().getValue()) {
            String joinPathId = "p000202407261531001";
            if (AntSports.checkJoinPathId(joinPathId)) {
                if (AntSports.joinPath(joinPathId)) {
                    return joinPathId;
                }
            }
        }
        return null;
    }

    private static void antMemberSignInTask(Object task) {
        try {
            JSONObject jo = (JSONObject) task;
            JSONArray taskList = jo.getJSONArray("taskProcessVOList");
            String type = jo.getString("type");
            if (Objects.equals("OTHERS", type)) {
                AntMemberAlpha.doOtherTask(taskList);
            }
        } catch (Throwable th) {
            Log.i(TAG, "antMemberSignInTask err:");
            Log.printStackTrace(TAG, th);
        }
    }

    private static void merchantServiceHideTask() {
        try {
            if (!Status.hasFlagToday("merchantService::doHideTask")) {
                JSONArray taskList = new JSONArray();
                // 绝版任务:不可用
//            taskList.put(new JSONObject("{\"taskCode\":\"XCZBJLLRWCS_TASK\",\"actionCode\":\"XCZBJLL_VIEWED\"}"));
//            taskList.put(new JSONObject("{\"taskCode\":\"LLSQMDLB_TASK\",\"actionCode\":\"LL_SQMDLB_VIEWED\"}"));
//            taskList.put(new JSONObject("{\"taskCode\":\"SYH_CPC_FIXED_2\",\"actionCode\":\"MRCH_CPC_FIXED_VIEWED\"}"));
//            taskList.put(new JSONObject("{\"taskCode\":\"HHKLLRW_TASK\",\"actionCode\":\"HHKLLX_VIEWED\"}"));
                // 绝版任务:可用
                taskList.put(new JSONObject("{\"taskCode\":\"BBNCLLRWX_TASK\",\"actionCode\":\"GYG_BBNC_VIEWED\"}"));
                taskList.put(new JSONObject("{\"taskCode\":\"SYH_CPC_ALMM_1\",\"actionCode\":\"MRCH_CPC_ALMM_VIEWED\"}"));
                taskList.put(new JSONObject("{\"taskCode\":\"TJBLLRW_TASK\",\"actionCode\":\"TJBLLRW_TASK_VIEWED\"}"));
                taskList.put(new JSONObject("{\"taskCode\":\"ZCJ_VIEW_TRADE\",\"actionCode\":\"ZCJ_VIEW_TRADE_VIEWED\"}"));
                for (int i = 0; i < taskList.length(); i++) {
                    JSONObject jo = taskList.getJSONObject(i);
                    MerchantServiceRpcCall.taskReceive(jo.getString("taskCode"));
                    TimeUtil.sleep(1000);
                    MerchantServiceRpcCall.taskActionProduce(jo.getString("actionCode"));
                    TimeUtil.sleep(1000);
                }
                Status.flagToday("merchantService::doHideTask");
            }
        } catch (Throwable th) {
            Log.i(TAG, "merchantServiceHideTask err:");
            Log.printStackTrace(TAG, th);
        }
    }

    public enum AlphaRequestType {
        // 共同Type
        UNKNOWN_TYPE("unknownType"),
        ENABLE_DEVELOPER_MODE("enableDeveloperMode"),
        ADD_EXTENSION_VIEW("addExtensionView"),
        RECORD_RUNTIME_INFO("recordRuntimeInfo"),
        DO_FARM_TASK("doFarmTask"),
        DO_FARM_DRAW_TIMES_TASK("doFarmDrawTimesTask"),
        DO_FARM_IP_DRAW_TASK("doFarmIpDrawTask"),
        // AlphaType
        PRC("Rpc", "rpc"),
        GET_WATERING_LEFT_TIMES("getWateringLeftTimes"),
        BATCH_HIRE_ANIMAL_RECOMMEND("batchHireAnimalRecommend"),
        SEND_ANT_DODO_ALL_CARD("sendAntDodoAllCard"),
        SEND_ANT_DODO_ONE_SET_CARD("sendAntDodoOneSetCard"),
        SEND_ANT_DODO_ONE_WHOLE_SET_CARD("sendAntDodoOneWholeSetCard");

        private final String firstType, secondType;

        AlphaRequestType(String firstType) {
            this.firstType = firstType;
            this.secondType = null;
        }

        AlphaRequestType(String firstType, String secondType) {
            this.firstType = firstType;
            this.secondType = secondType;
        }

        public static AlphaRequestType getAlphaRequestType(String requestType) {
            if (StringUtil.isEmpty(requestType)) {
                return AlphaRequestType.UNKNOWN_TYPE;
            }
            for (AlphaRequestType alphaRequestType : AlphaRequestType.values()) {
                if (Objects.equals(alphaRequestType.name(), requestType)
                        || Objects.equals(alphaRequestType.firstType, requestType)
                        || Objects.equals(alphaRequestType.secondType, requestType)) {
                    return alphaRequestType;
                }
            }
            return AlphaRequestType.UNKNOWN_TYPE;
        }
    }
}
