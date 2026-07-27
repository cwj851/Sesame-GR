package io.github.lazyimmortal.sesame.model.extensions;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

import io.github.lazyimmortal.sesame.data.RuntimeInfo;
import io.github.lazyimmortal.sesame.data.TokenConfig;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.model.task.antFarm.AntFarm;
import io.github.lazyimmortal.sesame.model.task.antFarm.AntFarmRpcCall;
import io.github.lazyimmortal.sesame.model.task.antForest.EcoLife;
import io.github.lazyimmortal.sesame.model.task.antSports.AntSports;
import io.github.lazyimmortal.sesame.model.task.antSports.AntSportsRpcCall;
import io.github.lazyimmortal.sesame.model.task.protectEcology.ProtectEcology;
import io.github.lazyimmortal.sesame.model.task.protectEcology.ProtectTreeRpcCall;
import io.github.lazyimmortal.sesame.model.task.antForest.AntForestRpcCall;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.util.LibraryUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;
import io.github.lazyimmortal.sesame.util.idMap.WalkPathIdMap;

public class ExtensionsHandle {
    private static final String TAG = ExtensionsHandle.class.getSimpleName();

    public static Object handleRequest(Request request) {
        Object result = handleAlphaRequest(request);
        if (result != null) {
            return result;
        }
        return handleRequest(request.type, request.method, request.data);
    }

    public static Object handleRequest(String type, String method, Object data) {
        RequestType requestType = RequestType.getRequestType(type);
        switch (requestType) {
            case UNKNOWN_TYPE:
                Log.record("[" + type + ":" + method + "]不是一个合法的请求类型");
                break;
            case ENABLE_DEVELOPER_MODE:
            case ADD_EXTENSION_VIEW:
                break;
            case RECORD_RUNTIME_INFO:
                recordRuntimeInfo((Object[]) data);
                break;
            case GET_TREE_ITEMS:
                getTreeItems();
                break;
            case GET_NEW_TREE_ITEMS:
                getNewTreeItems();
                break;
            case GET_UNLOCK_TREE_ITEMS:
                getUnlockTreeItems();
                break;
            case QUERY_AREA_TREES:
                queryAreaTrees();
                break;
            case QUERY_PROP_LIST:
                queryPropList();
                break;
            case QUERY_FARM_FOOD:
                queryFarmFood();
                break;
            case DISH_IMAGE_NOW:
                EcoLife.queryDish();
                break;
            case QUERY_USER_COOPERATE_PLANT_LIST:
                ProtectEcology.queryUserCooperatePlantList();
                break;
            case DO_FARM_TASK:
                return LibraryUtil.doFarmTask((JSONObject) data);
            case DO_FARM_DRAW_TIMES_TASK:
                return LibraryUtil.doFarmDrawTimesTask((JSONObject) data);
            case DO_FARM_IP_DRAW_TASK:
                AntFarm.listFarmIpDrawTask();
                break;
            case ADD_CUSTOM_WALK_PATH_ID:
                addCustomWalkPathIdList((String) data);
                break;
            case SYNC_STEP_COUNT_NOW:
                ModelTask.getModel(AntSports.class).syncStepCount();
                break;
            case SESAME_REQUEST:
                sesameRequest(method, data);
                break;
            case QUERY_FRIEND_ENERGY:
                queryFriendHomePage((String) data);
                break;
            case FETCH_RANKING:
                fetchRanking();
                break;
        }
        return null;
    }

    private static void sesameRequest(String method, Object data) {
        if (Objects.equals("setCurrentUserId", method)) {
            UserIdMap.setCurrentUserId((String) data);
        }
    }

    public static Object handleAlphaRequest(Request request) {
        try {
            return Class.forName("io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandleAlpha")
                    .getMethod("handleAlphaRequest", Request.class)
                    .invoke(null, request);
        } catch (Exception e) {
            return null;
        }
    }

    private static void recordRuntimeInfo(Object[] recordArray) {
        try {
            if (Objects.equals("alipay.antforest.forest.h5.giveProp", recordArray[1])) {
                JSONObject jo = new JSONObject(recordArray[2].toString());
                jo = jo.getJSONArray("requestData").getJSONObject(0);
                RuntimeInfo.getInstance().put(RuntimeInfo.RuntimeInfoKey.ForestGivePropRdsBizNo, jo.getString("rdsBizNo"));
                RuntimeInfo.getInstance().put(RuntimeInfo.RuntimeInfoKey.ForestGivePropRdsToken, jo.getString("rdsToken").replace("\"", "\\\""));
            }
        } catch (Throwable t) {
            Log.i(TAG, "recordRuntimeInfo err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void getNewTreeItems() {
        try {
            JSONObject jo = new JSONObject(ProtectTreeRpcCall.queryTreeItemsForExchange("COMING", "project"));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray ja = jo.getJSONArray("treeItems");
            if (ja.length() == 0) {
                Log.forest("新树上苗🌱[当前没有新树上苗信息!]");
                return;
            }
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                if (!jo.has("projectType"))
                    continue;
                if (!"TREE".equals(jo.getString("projectType")))
                    continue;
                if (!"COMING".equals(jo.getString("applyAction")))
                    continue;
                String projectId = jo.getString("itemId");
                queryTreeForExchange(projectId);
            }
        } catch (Throwable t) {
            Log.i(TAG, "getTreeItems err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void queryTreeForExchange(String projectId) {
        try {
            JSONObject jo = new JSONObject(ProtectTreeRpcCall.queryTreeForExchange(projectId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject exchangeableTree = jo.getJSONObject("exchangeableTree");
            int currentBudget = exchangeableTree.getInt("currentBudget");
            String region = exchangeableTree.getString("region");
            String treeName = exchangeableTree.getString("treeName");
            String tips = "不可合种";
            if (exchangeableTree.optBoolean("canCoexchange", false)) {
                tips = "可以合种-合种类型："
                        + exchangeableTree.getJSONObject("extendInfo").getString("cooperate_template_id_list");
            }
            long onlineDatetime = exchangeableTree.getJSONObject("extendInfo").getLong("onlineDatetime");
            Log.forest("新树上苗🌱[" + region + "-" + treeName + "]#" + currentBudget + "株-"
                    + tips + "(上线时间:" + TimeUtil.getCommonDateTime(onlineDatetime) + ")");
        } catch (Throwable t) {
            Log.i(TAG, "queryTreeForExchange err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void getTreeItems() {
        try {
            JSONObject jo = new JSONObject(ProtectTreeRpcCall.queryTreeItemsForExchange("AVAILABLE,ENERGY_LACK", "project"));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray ja = jo.getJSONArray("treeItems");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                if (!jo.has("projectType"))
                    continue;
                String projectId = jo.getString("itemId");
                String itemName = jo.getString("itemName");
                getTreeCurrentBudget(projectId, itemName);
                TimeUtil.sleep(100);
            }
        } catch (Throwable t) {
            Log.i(TAG, "getTreeItems err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void getTreeCurrentBudget(String projectId, String treeName) {
        try {
            JSONObject jo = new JSONObject(ProtectTreeRpcCall.queryTreeForExchange(projectId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                JSONObject exchangeableTree = jo.getJSONObject("exchangeableTree");
                int currentBudget = exchangeableTree.getInt("currentBudget");
                String region = exchangeableTree.getString("region");
                Log.forest("树苗查询🌱[" + region + "-" + treeName + "]#剩余:" + currentBudget);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryTreeForExchange err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void getUnlockTreeItems() {
        try {
            JSONObject jo = new JSONObject(ProtectTreeRpcCall.queryTreeItemsForExchange("", "project"));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONArray ja = jo.getJSONArray("treeItems");
            for (int i = 0; i < ja.length(); i++) {
                jo = ja.getJSONObject(i);
                if (!jo.has("projectType"))
                    continue;
                int certCountForAlias = jo.optInt("certCountForAlias", -1);
                if (certCountForAlias == 0) {
                    String itemName = jo.optString("itemName");
                    String region = jo.optString("region");
                    String organization = jo.optString("organization");
                    Log.forest("未解锁项目🐘[" + region + "-" + itemName + "]#" + organization);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "getUnlockTreeItems err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void queryAreaTrees() {
        try {
            JSONObject jo = new JSONObject(ProtectTreeRpcCall.queryAreaTrees());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            JSONObject areaTrees = jo.getJSONObject("areaTrees");
            JSONObject regionConfig = jo.getJSONObject("regionConfig");
            Iterator<String> regionKeys = regionConfig.keys();
            while (regionKeys.hasNext()) {
                String regionKey = regionKeys.next();
                if (!areaTrees.has(regionKey)) {
                    JSONObject region = regionConfig.getJSONObject(regionKey);
                    String regionName = region.optString("regionName");
                    Log.forest("未解锁地区🗺️[" + regionName + "]");
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryAreaTrees err:");
            Log.printStackTrace(TAG, t);
        }
    }

        /* 查询道具 */
        private static void queryPropList() {
            try {
                JSONObject jo = new JSONObject(AntForestRpcCall.queryPropList(false));
                if ("SUCCESS".equals(jo.getString("resultCode"))) {
                    JSONArray forestPropVOList = jo.optJSONArray("forestPropVOList");
                    if (forestPropVOList != null && forestPropVOList.length() > 0) {
                        for (int i = 0; i < forestPropVOList.length(); i++) {
                            jo = forestPropVOList.getJSONObject(i);
                            int holdsNum = jo.optInt("holdsNum", 0);
                            String propName = jo.optJSONObject("propConfigVO").optString("propName");
                            Log.forest("查询道具🎭[" + propName + "]#" + holdsNum + "个");
                        }
                    }
                } else {
                    Log.record(jo.getString("resultDesc"));
                    Log.i(jo.toString());
                }
            } catch (Throwable th) {
                Log.i(TAG, "queryPropList err:");
                Log.printStackTrace(TAG, th);
            }
        }

    private static void queryFarmFood() {
        try {
            JSONObject jo = new JSONObject(AntFarmRpcCall.enterFarm("", UserIdMap.getCurrentUid()));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            int countSum=0;
                JSONArray cuisineList = jo.optJSONArray("cuisineList");
                if (cuisineList != null && cuisineList.length() > 0) {
                    for (int i = 0; i < cuisineList.length(); i++) {
                        jo = cuisineList.getJSONObject(i);
                        int count = jo.optInt("count", 0);
                        String name = jo.optString("name");
                        if (count>0) {
                            countSum=countSum+count;
                            Log.farm("查询美食🍱[" + name + "]#" + count + "个");
                        }
                    }
                    Log.farm("查询美食🍱合计"+countSum+"个。");
                }

        } catch (Throwable th) {
            Log.i(TAG, "queryFarmFood err:");
            Log.printStackTrace(TAG, th);
        }
    }
    private static void addCustomWalkPathIdList(String pathId) {
        RuntimeInfo.getInstance().putAll(RuntimeInfo.RuntimeInfoKey.RequestSuccess, true);
        if (!StringUtil.isEmpty(pathId)) {
            String pathName = AntSportsRpcCall.queryPathName(pathId);
            if (pathName == null) {
                ToastUtil.show(ApplicationHook.getContext(), "添加自定义路线列表失败:找不到路线信息");
                return;
            }
            WalkPathIdMap.getInstance().load();
            WalkPathIdMap.getInstance().add(pathId, pathName);
            WalkPathIdMap.getInstance().save();
            ToastUtil.show(ApplicationHook.getContext(), "添加自定义路线列表成功:" + pathName);
        }
    }

    @Deprecated
    private static void addCustomWalkPathIdQueue(String pathId) {
        if (!StringUtil.isEmpty(pathId)) {
            String pathName = AntSportsRpcCall.queryPathName(pathId);
            if (pathName == null) {
                ToastUtil.show(ApplicationHook.getContext(), "添加待行走路线队列失败:找不到路线信息");
                return;
            }
            if (TokenConfig.addCustomWalkPathIdQueue(pathId)) {
                ToastUtil.show(ApplicationHook.getContext(), "添加待行走路线队列成功:" + pathName);
            }
        }
    }

    @Deprecated
    private static void clearCustomWalkPathIdQueue() {
        if (TokenConfig.clearCustomWalkPathIdQueue()) {
            ToastUtil.show(ApplicationHook.getContext(), "清除待行走路线队列成功");
        }
    }

    /* 查询好友能量来源 */
    private static void queryFriendHomePage(String uid) {
        try {
            Log.forest("森林查询🌿[" + UserIdMap.getMaskName(uid) + "]");
            String s = AntForestRpcCall.queryFriendHomePage(uid);
            TimeUtil.sleep(100);
            JSONObject jo = new JSONObject(s);
            if ("SUCCESS".equals(jo.getString("resultCode"))) {
                JSONArray bubbles = jo.optJSONArray("bubbles");
                JSONArray wateringBubbles = jo.optJSONArray("wateringBubbles");
                if (bubbles != null && bubbles.length() > 0) {
                    for (int i = 0; i < bubbles.length(); i++) {
                        jo = bubbles.optJSONObject(i);
                        int remainEnergy = jo.optInt("remainEnergy");
                        Long produceTime = jo.getLong("produceTime");
                        String bigIconDisplayName = jo.optJSONObject("business").optString("bigIconDisplayName");
                        Log.forest("森林查询🌿[" + bigIconDisplayName + "|" + TimeUtil.getCommonDateTime(produceTime) + "]#"
                            + remainEnergy + "g");
                    }
                } else {
                    Log.forest("森林查询🌿[未查询到森林能量球]");
                }
                if (wateringBubbles != null && wateringBubbles.length() > 0) {
                    for (int j = 0; j < wateringBubbles.length(); j++) {
                        jo = wateringBubbles.optJSONObject(j);
                        int fullEnergy = jo.optInt("fullEnergy");
                        String userId = jo.optString("userId");
                        String bizType = jo.optString("bizType");
                        if (!"jiaoshui".equals(bizType))
                            continue;
                        Long produceTime = jo.getLong("produceTime");
                        Log.forest("森林查询🌿[浇水来源:" + UserIdMap.getMaskName(userId) + "|"
                            + TimeUtil.getCommonDateTime(produceTime) + "]#" + fullEnergy + "g");
                    }
                } else {
                    Log.forest("森林查询🌿[未查询到浇水能量球]");
                }
            } else {
                Log.forest("森林查询🌿[查询失败]");
                Log.record(jo.getString("resultDesc"));
                Log.i(s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryFriendHomePage err:");
            Log.printStackTrace(t);
        }
    }

    private static void fetchRanking() {
        try {
            File userListFile = new File(io.github.lazyimmortal.sesame.util.FileUtil.MAIN_DIRECTORY_FILE, "userList.json");
            String userListJson;
            if (userListFile.exists()) {
                userListJson = io.github.lazyimmortal.sesame.util.FileUtil.readFromFile(userListFile);
            } else {
                String defaultList = "{\"userList\": []}";
                io.github.lazyimmortal.sesame.util.FileUtil.write2File(defaultList, userListFile);
                userListJson = defaultList;
            }
            JSONObject userListRoot = new JSONObject(userListJson);
            JSONArray userList = userListRoot.optJSONArray("userList");
            if (userList == null || userList.length() == 0) {
                Log.record("拉取总榜: userList.json 中没有用户数据");
                return;
            }
            Log.record("拉取总榜: 开始拉取 " + userList.length() + " 个用户数据");
            JSONArray results = new JSONArray();
            int successCount = 0;
            int failCount = 0;
            for (int i = 0; i < userList.length(); i++) {
                JSONObject record = userList.optJSONObject(i);
                if (record == null) {
                    continue;
                }
                String userId = record.optString("userId", "").trim();
                if (userId.isEmpty()) {
                    continue;
                }
                try {
                    String response = AntForestRpcCall.queryFriendHomePage(userId);
                    JSONObject responseJson = new JSONObject(response);
                    if (!"SUCCESS".equals(responseJson.optString("resultCode"))) {
                        Log.record("拉取总榜: 查询用户 " + userId + " 失败 " + responseJson.optString("resultDesc"));
                        failCount++;
                        continue;
                    }
                    JSONObject userBaseInfo = responseJson.optJSONObject("userBaseInfo");
                    if (userBaseInfo == null) {
                        Log.record("拉取总榜: 查询用户 " + userId + " 返回数据中没有 userBaseInfo");
                        failCount++;
                        continue;
                    }
                    JSONObject output = new JSONObject();
                    output.put("昵称", record.optString("nickName", ""));
                    output.put("userId", userBaseInfo.optString("userId", ""));
                    output.put("昵称-支", userBaseInfo.optString("displayName", ""));
                    output.put("总能量", userBaseInfo.optLong("totalEnergy", 0));
                    output.put("当前能量", userBaseInfo.optLong("currentEnergy", 0));
                    output.put("总证书", userBaseInfo.optInt("totalCertCount", 0));
                    output.put("古树", userBaseInfo.optInt("ancientTreeCount", 0));
                    output.put("动物", userBaseInfo.optInt("animalCertCount", 0));
                    output.put("保护地", userBaseInfo.optInt("reserveCount", 0));
                    output.put("海洋", userBaseInfo.optInt("seaPlantCount", 0));
                    output.put("森林", userBaseInfo.optInt("treeCount", 0));
                    output.put("登录账号", userBaseInfo.optString("loginId", ""));
                    output.put("头像", userBaseInfo.optString("headPortrait", ""));
                    results.put(output);
                    successCount++;
                    Log.record("拉取总榜: [" + (i + 1) + "/" + userList.length() + "] " + userId + " 查询成功");
                    Thread.sleep(200);
                } catch (Exception e) {
                    Log.record("拉取总榜: 查询用户 " + userId + " 异常 " + e.getMessage());
                    Log.printStackTrace(TAG, e);
                    failCount++;
                }
            }
            File userBaseInfoFile = new File(io.github.lazyimmortal.sesame.util.FileUtil.MAIN_DIRECTORY_FILE, "森林数据.json");
            JSONObject outputRoot = new JSONObject();
            outputRoot.put("userBaseInfoList", results);
            outputRoot.put("保存时间", TimeUtil.getCommonDateTime(System.currentTimeMillis()).substring(0, 16));
            io.github.lazyimmortal.sesame.util.FileUtil.write2File(outputRoot.toString(2), userBaseInfoFile);
            Log.record("拉取总榜: 完成！成功 " + successCount + " 失败 " + failCount);
        } catch (Exception e) {
            Log.record("拉取总榜: 异常 " + e.getMessage());
            Log.printStackTrace(TAG, e);
        }
    }
}
