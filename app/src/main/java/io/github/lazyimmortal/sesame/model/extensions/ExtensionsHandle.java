package io.github.lazyimmortal.sesame.model.extensions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.Objects;

import io.github.lazyimmortal.sesame.data.TokenConfig;
import io.github.lazyimmortal.sesame.hook.Toast;
import io.github.lazyimmortal.sesame.model.task.antForest.AntForestRpcCall;
import io.github.lazyimmortal.sesame.model.task.antSports.AntSportsRpcCall;
import io.github.lazyimmortal.sesame.model.task.protectEcology.ProtectTreeRpcCall;
import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.PathThemeMapListMap;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class ExtensionsHandle {
    private static final String TAG = ExtensionsHandle.class.getSimpleName();

    public static void handleRequest(String type, String fun, Object data) {
        if (handleAlphaRequest(type, fun, data) != null) {
            return;
        }
        switch (type) {
            case "antForest":
                if (Objects.equals("getWateredItems", fun)) {
                    getWateredItems();
                }else if (Objects.equals("getWateringItems", fun)) {
                    getWateringItems();
                }else if (Objects.equals("getTreeItems", fun)) {
                    getTreeItems();
                } else if (Objects.equals("getNewTreeItems", fun)) {
                    getNewTreeItems();
                } else if (Objects.equals("queryAreaTrees", fun)) {
                    queryAreaTrees();
                } else if (Objects.equals("getUnlockTreeItems", fun)) {
                    getUnlockTreeItems();
                } else if (Objects.equals("fillWateredFriendList", fun)) {
                    fillWateredFriendList();
                } else if (Objects.equals("fetchRanking", fun)) {
                    fetchRanking();
                } else if (Objects.equals("queryFriendEnergy", fun)) {
                    queryFriendHomePage((String) data);
                }
                break;
            case "setCustomWalkPathIdList":
                addCustomWalkPathIdList((String) data);
                break;
            case "setCustomWalkPathIdQueue":
                if (Objects.equals("addCustomWalkPathIdQueue", fun)) {
                    addCustomWalkPathIdQueue((String) data);
                } else if (Objects.equals("clearCustomWalkPathIdQueue", fun)) {
                    clearCustomWalkPathIdQueue();
                }
                break;
        }
    }

    public static Object handleAlphaRequest(String type, String fun, Object data) {
        try {
            return Class.forName("io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandleAlpha")
                    .getMethod("handleAlphaRequest", String.class, String.class, Object.class)
                    .invoke(null, type, fun, data);
        } catch (Exception e) {
            return null;
        }
    }
    private static void getWateredItems() {
        Status.getWateredFriendToday();
    }
    
    private static void getWateringItems() {
        Status.getWateringFriendToday();
    }

    private static void fillWateredFriendList() {
        Status.fillWateredFriendList();
    }
    
    private static void getNewTreeItems() {
        try {
            JSONObject jo = new JSONObject(ProtectTreeRpcCall.queryTreeItemsForExchange("COMING", "project"));
            if (!MessageUtil.checkResultCode(TAG, jo)) {
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
            if (!MessageUtil.checkResultCode(TAG, jo)) {
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
            Log.forest("新树上苗🌱[" + region + "-" + treeName + "]#" + currentBudget + "株-" + tips);
        } catch (Throwable t) {
            Log.i(TAG, "queryTreeForExchange err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void getTreeItems() {
        try {
            JSONObject jo = new JSONObject(ProtectTreeRpcCall.queryTreeItemsForExchange("AVAILABLE,ENERGY_LACK", "project"));
            if (!MessageUtil.checkResultCode(TAG, jo)) {
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
            if (MessageUtil.checkResultCode(TAG, jo)) {
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

    private static void queryAreaTrees() {
        try {
            JSONObject jo = new JSONObject(ProtectTreeRpcCall.queryAreaTrees());
            if (!MessageUtil.checkResultCode(TAG, jo)) {
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

    private static void getUnlockTreeItems() {
        try {
            JSONObject jo = new JSONObject(ProtectTreeRpcCall.queryTreeItemsForExchange("", "project"));
            if (!MessageUtil.checkResultCode(TAG, jo)) {
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

    private static void addCustomWalkPathIdList(String pathId) {
        if (!StringUtil.isEmpty(pathId)) {
            String pathName = AntSportsRpcCall.queryPathName(pathId);
            if (pathName == null) {
                Toast.show("添加自定义路线列表失败:找不到路线信息");
                return;
            }
            PathThemeMapListMap.load();
            PathThemeMapListMap.add(pathId, pathName);
            PathThemeMapListMap.save();
            Toast.show("添加自定义路线列表成功:" + pathName);
        }
    }

    private static void addCustomWalkPathIdQueue(String pathId) {
        if (!StringUtil.isEmpty(pathId)) {
            String pathName = AntSportsRpcCall.queryPathName(pathId);
            if (pathName == null) {
                Toast.show("添加待行走路线队列失败:找不到路线信息");
                return;
            }
            if (TokenConfig.addCustomWalkPathIdQueue(pathId)) {
                Toast.show("添加待行走路线队列成功:" + pathName);
            }
        }
    }

    private static void clearCustomWalkPathIdQueue() {
        if (TokenConfig.clearCustomWalkPathIdQueue()) {
            Toast.show("清除待行走路线队列成功");
        }
    }

    private static void fetchRanking() {
        try {
            File userListFile = new File(FileUtil.MAIN_DIRECTORY_FILE, "userList.json");
            String userListJson;
            if (userListFile.exists()) {
                userListJson = FileUtil.readFromFile(userListFile);
            } else {
                String defaultList = "{\"userList\": []}";
                FileUtil.write2File(defaultList, userListFile);
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
            File outputFile = new File(FileUtil.MAIN_DIRECTORY_FILE, "森林数据.json");
            JSONObject outputRoot = new JSONObject();
            outputRoot.put("userBaseInfoList", results);
            outputRoot.put("保存时间", java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));
            FileUtil.write2File(outputRoot.toString(2), outputFile);
            Log.record("拉取总榜: 完成！成功 " + successCount + " 失败 " + failCount);
        } catch (Exception e) {
            Log.record("拉取总榜: 异常 " + e.getMessage());
            Log.printStackTrace(TAG, e);
        }
    }

    private static void queryFriendHomePage(String uid) {
        try {
            Log.forest("森林查询[" + UserIdMap.getMaskName(uid) + "]");
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
                        String bigIconDisplayName = jo.optJSONObject("business")
                                .optString("bigIconDisplayName");
                        Log.forest("森林查询[" + bigIconDisplayName + "|"
                                + TimeUtil.getCommonDate(produceTime) + " " + TimeUtil.getTimeStr(produceTime) + "]#"
                                + remainEnergy + "g");
                    }
                } else {
                    Log.forest("森林查询[未查询到森林能量球]");
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
                        Log.forest("森林查询[浇水来源:"
                                + UserIdMap.getMaskName(userId) + "|"
                                + TimeUtil.getCommonDate(produceTime) + " " + TimeUtil.getTimeStr(produceTime) + "]#"
                                + fullEnergy + "g");
                    }
                } else {
                    Log.forest("森林查询[未查询到浇水能量球]");
                }
            } else {
                Log.forest("森林查询[查询失败]");
                Log.record(jo.getString("resultDesc"));
                Log.i(s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryFriendHomePage err:");
            Log.printStackTrace(TAG, t);
        }
    }
}
