package io.github.lazyimmortal.sesame.model.task.antSports;

import org.json.JSONObject;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;

public class NeverLandRpcCall {
    /**
     * 查询健康岛签到
     */
    public static String querySign() {
        String args = "[{\"source\":\"jkdprizesign\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.querySign", args);
    }

    /**
     * 健康岛签到
     */
    public static String takeSign() {
        String args = "[{\"source\":\"jkdprizesign\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.takeSign", args);
    }

    /**
     * 查询任务中心
     */
    public static String queryTaskCenter() {
        String args = "[{\"source\":\"jkdprizesign\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.queryTaskCenter", args);
    }

    /**
     * 完成任务
     *
     * @param task 要完成的任务
     */
    public static String taskSend(JSONObject task) {
        String args = "[" + task + "]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.taskSend", args);
    }

    /**
     * 领取任务奖励
     *
     * @param task 已完成的任务
     */
    public static String taskReceive(JSONObject task) {
        String args = "[" + task + "]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.taskReceive", args);
    }

    /**
     * 查询任务信息(浏览商品)
     */
    public static String queryTaskInfo() {
        String args = "[{\"source\":\"health-island\",\"type\":\"LIGHT_FEEDS_TASK\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.queryTaskInfo", args);
    }

    /**
     * 领取健康能量(浏览商品)
     *
     * @param taskInfo 要领取的任务信息
     */
    public static String energyReceive(JSONObject taskInfo) {
        String args = "[" + taskInfo + "]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.energyReceive", args);
    }

    /**
     * 查询用户账号
     */
    public static String queryUserAccount() {
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.queryUserAccount", "[{}]");
    }

    /**
     * 查询健康能量任务
     */
    public static String queryBubbleTask() {
        String args = "[{\"sportsAuthed\":true}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.queryBubbleTask", args);
    }

    /**
     * 领取健康能量
     *
     * @param medEnergyBallInfoRecordId 健康能量球信息记录ID
     */
    public static String pickBubbleTaskEnergy(String medEnergyBallInfoRecordId) {
        String args = "[{\"medEnergyBallInfoRecordIds\":[\"" + medEnergyBallInfoRecordId + "\"],\"pickAllEnergyBall\":false}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.pickBubbleTaskEnergy", args);
    }

    public static String pickAllBubbleTaskEnergy() {
        String args = "[{\"medEnergyBallInfoRecordIds\":[],\"pickAllEnergyBall\":true,\"source\":\"SPORT\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.pickBubbleTaskEnergy", args);
    }

    /**
     * 领取离线奖励
     */
    public static String offlineAward() {
        // isAdvertisement 是否双倍领取
        String args = "[{\"isAdvertisement\":true}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.offlineAward", args);
    }

    /**
     * 查询基础信息
     */
    public static String queryBaseInfo() {
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.queryBaseinfo", "[{}]");
    }

    /**
     * 查询地图信息
     */
    public static String queryMapInfo(String branchId, String mapId) {
        String args = "[{\"branchId\":\"" + branchId + "\",\"drilling\":false,\"mapId\":\"" + mapId + "\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.queryMapInfo", args);
    }

    /**
     * @param branchId 分支ID
     * @param mapId    地图ID
     */
    public static String walkGrid(String branchId, String mapId) {
        String args = "[{\"branchId\":\"" + branchId + "\",\"drilling\":false,\"mapId\":\"" + mapId + "\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.walkGrid", args);
    }

    /**
     * 查询权益商品列表
     *
     * @param categoryType 权益类别
     *                     FEEDS_VIRTUAL_EQUITY 虚拟权益
     * @param pageNum      页码(1开始)
     */
    public static String queryItemList(String categoryType, int pageNum) {
        String args = "[{\"categoryType\":\"" + categoryType + "\",\"pageNum\":" + pageNum + ",\"pageSize\":15}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.queryItemList", args);
    }

    /**
     * 查询权益商品详情
     *
     * @param benefitId    权益ID
     * @param itemId       商品ID
     * @param materialType 材质类型
     * @noinspection unused
     */
    public static String queryItemDetail(String benefitId, String itemId, String materialType) {
        String args = "[{\"benefitId\":\"" + benefitId + "\",\"itemId\":\"" + itemId + "\",\"materialType\":\"" + materialType + "\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.queryItemDetail", args);
    }

    public static String mapStageReward(String mapId,int level) {
        String args = "[{\"branchId\":\"MASTER\",\"level\":"+level+",\"mapId\":\""+mapId+"\",\"source\":\"ch_appid-ActivityApplicationStub\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.mapStageReward", args);
    }

    /**
     * 创建订单兑换权益
     *
     * @param benefitId 权益ID
     * @param itemId    商品ID
     */
    public static String createOrder(String benefitId, String itemId) {
        String args = "[{\"benefitId\":\"" + benefitId + "\",\"itemId\":\"" + itemId + "\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.createOrder", args);
    }

    public static String serviceTaskFinish(String bizId) {
        String args = "[{\"bizId\":\"" + bizId + "\"}]";
        return ApplicationHook.requestString("com.alipay.adtask.biz.mobilegw.service.task.finish", args);
    }

/**
* 运动健康能量任务
 */
    public static String queryCoinTaskPanel() {
        String args = "[{\"apiVersion\":\"energy\",\"canAddHome\":false,\"chInfo\":\"medical_health\",\"clientAuthStatus\":\"not_support\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_AI\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"SUPPORT_NEW_MATCH\",\"EXTERNAL_ADVERTISEMENT_TASK\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"topTaskId\":\"\"}]";
        return ApplicationHook.requestString("com.alipay.sportshealth.biz.rpc.SportsHealthCoinTaskRpc.queryCoinTaskPanel", args);
    }

    public static String completeTask(String taskAction,String taskId) {
        String args = "[{\"apiVersion\":\"energy\",\"chInfo\":\"medical_health\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_AI\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"SUPPORT_NEW_MATCH\",\"EXTERNAL_ADVERTISEMENT_TASK\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"taskAction\":\""+taskAction+"\",\"taskId\":\""+taskId+"\"}]";
        return ApplicationHook.requestString("com.alipay.sportshealth.biz.rpc.SportsHealthCoinTaskRpc.completeTask", args);
    }

    public static String queryEnergyBubbleModule() {
        String args = "[{\"apiVersion\":\"energy\",\"bubbleId\":\"\",\"canAddHome\":false,\"chInfo\":\"ch_shouquan_shouye\",\"clientAuthStatus\":\"not_support\",\"clientOS\":\"android\",\"distributionChannel\":\"\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_AI\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"SUPPORT_NEW_MATCH\",\"EXTERNAL_ADVERTISEMENT_TASK\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"outBizNo\":\"\"}]";
        return ApplicationHook.requestString("com.alipay.sportshealth.biz.rpc.sportsHealthHomeRpc.queryEnergyBubbleModule", args);
    }

    /**
     * 运动健康能量秒杀
     */
    public static String queryFlashSaleItemList() {
        String args = "[{\"cityCode\":\"330100\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.queryFlashSaleItemList", args);
    }

    public static String viewDailyAds() {
        String args = "[{\"source\":\"ch_appid-20002100__chsub_pageid-com.alipay.android.phone.mytinyapp.activity.MyAppActivityV2\"}]";
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.viewDailyAds", args);
    }

}