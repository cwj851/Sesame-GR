package io.github.lazyimmortal.sesame.model.task.antMember;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.RuntimeInfo;
import io.github.lazyimmortal.sesame.data.modelFieldExt.BooleanModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.entity.idAndName.MemberBenefit;
import io.github.lazyimmortal.sesame.entity.idAndName.MerchantSeckill;
import io.github.lazyimmortal.sesame.entity.idAndName.PromiseSimpleTemplate;
import io.github.lazyimmortal.sesame.model.base.TaskCommon;
import io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandle;
import io.github.lazyimmortal.sesame.model.normal.base.BaseModel;
import io.github.lazyimmortal.sesame.model.task.antMember.AntInsurance.AntInsuranceOption;
import io.github.lazyimmortal.sesame.model.task.immortal.AntForestAlpha;

import io.github.lazyimmortal.sesame.model.task.immortal.AntForestAlphaRpcCall;
import io.github.lazyimmortal.sesame.model.task.immortal.AntMemberAlphaRpcCall;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestMethod;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.NotificationUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.idMap.MemberBenefitIdMap;
import io.github.lazyimmortal.sesame.util.idMap.MerchantSeckillIdMap;

public class AntMember extends ModelTask {
    private static final String TAG = AntMember.class.getSimpleName();

    @Override
    public String getName() {
        return "会员";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.MEMBER;
    }

    private static List<String> otherTaskIdList;

    private BooleanModelField memberSignIn;
    private SelectModelField memberSignInOptions;
    private static BooleanModelField inviteSign;
    private BooleanModelField memberPointExchangeBenefit;
    private SelectModelField memberPointExchangeBenefitList;
    private BooleanModelField zhiMaCredit;
    protected SelectModelField zhiMaCreditOptions;
    protected SelectModelField lifeRecordList;
    private BooleanModelField welfarePlus;
    protected SelectModelField welfarePlusOptions;
    private BooleanModelField antInsurance;
    protected SelectModelField antInsuranceOptions;
    private BooleanModelField signinCalendar;
    private BooleanModelField gameCenter;
    protected SelectModelField gameCenterOptions;
    private BooleanModelField merchantService;
    private BooleanModelField merchantSeckill;
    private SelectModelField merchantSeckillList;
    protected SelectModelField merchantServiceOptions;
    private BooleanModelField goldBill;

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(memberSignIn = new BooleanModelField("memberSignIn", "会员签到", false));
        modelFields.addField(memberSignInOptions = new SelectModelField("memberSignInOptions", "会员签到 | 选项", new LinkedHashSet<>(), MemberSignInOption.class));
        modelFields.addField(inviteSign = new BooleanModelField("inviteSign", "会员任务 |邀请好友签到", false));
        modelFields.addField(memberPointExchangeBenefit = new BooleanModelField("memberPointExchangeBenefit", "会员积分 | 兑换权益", false));
        modelFields.addField(memberPointExchangeBenefitList = new SelectModelField("memberPointExchangeBenefitList", "会员积分 | 权益列表", new LinkedHashSet<>(), MemberBenefit::getList));
        modelFields.addField(zhiMaCredit = new BooleanModelField("zhiMaCredit", "芝麻信用 | 开启", false));
        modelFields.addField(zhiMaCreditOptions = new SelectModelField("zhiMaCreditOptions", "芝麻信用 | 选项", new LinkedHashSet<>(), AntGroup.ZhiMaCreditOption.class));
        modelFields.addField(lifeRecordList = new SelectModelField("lifeRecordList", "芝麻信用 | 生活记录列表", new LinkedHashSet<>(), PromiseSimpleTemplate::getList));
        modelFields.addField(welfarePlus = new BooleanModelField("welfarePlus", "我的快递 | 福利加", false));
        modelFields.addField(welfarePlusOptions = new SelectModelField("welfarePlusOptions", "我的快递 | 福利加选项", new LinkedHashSet<>(), WelfarePlus.WelfarePlusOption.class));
        modelFields.addField(gameCenter = new BooleanModelField("gameCenter", "游戏中心 | 开启", false));
        modelFields.addField(gameCenterOptions = new SelectModelField("gameCenterOptions", "游戏中心 | 选项", new LinkedHashSet<>(), GameCenter.GameCenterOption.class));
        modelFields.addField(merchantService = new BooleanModelField("merchantService", "商家服务 | 开启", false));
        modelFields.addField(merchantServiceOptions = new SelectModelField("merchantServiceOptions", "商家服务 | 选项", new LinkedHashSet<>(), MerchantService.MerchantServiceOption.class));
        modelFields.addField(merchantSeckill = new BooleanModelField("merchantSeckill", "收钱有奖秒杀 | 开启", false));
        modelFields.addField(merchantSeckillList = new SelectModelField("merchantSeckillList", "收钱有奖秒杀 | 列表", new LinkedHashSet<>(), MerchantSeckill::getList));
        modelFields.addField(goldBill = new BooleanModelField("goldBill", "我的黄金 | 黄金票", false));
        modelFields.addField(antInsurance = new BooleanModelField("antInsurance", "蚂蚁保 | 开启", false));
        modelFields.addField(antInsuranceOptions = new SelectModelField("antInsuranceOptions", "蚂蚁保 | 选项", new LinkedHashSet<>(), AntInsuranceOption.class));
        modelFields.addField(signinCalendar = new BooleanModelField("signinCalendar", "消费金 | 签到", false));
        return modelFields;
    }

    @Override
    public Boolean check() {
        if (TaskCommon.IS_ENERGY_TIME) {
            Log.other("任务暂停⏸️蚂蚁会员:当前为只收能量时间");
            return false;
        }
        long executeTime = RuntimeInfo.getInstance().getLong("AntMember", 0);
        long currentTime = System.currentTimeMillis();
        return TimeUtil.isLessThanSecondOfDays(executeTime, currentTime) || currentTime - executeTime >= 10600000;
        //return true;
    }

    @Override
    public void run() {
        try {
            RuntimeInfo.getInstance().put("AntMember", System.currentTimeMillis());
            NotificationUtil.sendTaskNotification(this);
            if (merchantSeckill.getValue()) {
                mrchpoint_ttms_page();
            }
            if (memberSignIn.getValue()) {
                memberSignIn();
            }
            if (memberPointExchangeBenefit.getValue()) {
                memberPointExchangeBenefit();
            }
            if (zhiMaCredit.getValue()) {
                AntGroup.run();
            }
            // 我的快递任务
            if (welfarePlus.getValue()) {
                WelfarePlus.run();
            }
            if (goldBill.getValue()) {
                GoldBill.run();
            }
            if (gameCenter.getValue()) {
                GameCenter.run();
            }
            if (merchantService.getValue()) {
                MerchantService.run();
            }
            if (antInsurance.getValue()) {
                AntInsurance.run();
            }
            // 消费金签到
            if (signinCalendar.getValue()) {
                signinCalendar();
            }
        } catch (Throwable t) {
            Log.i(TAG, "start.run err:");
            Log.printStackTrace(TAG, t);
        } finally {
            NotificationUtil.removeTaskNotification(this);
        }
    }

    private void memberSignIn() {
        try {
            if (!Status.hasFlagToday(AntMemberFlag.SIGN_IN.flagName())) {
                JSONObject jo = new JSONObject(AntMemberRpcCall.queryMemberSigninCalendar());
                TimeUtil.sleep(500);
                if (MessageUtil.checkResponse(TAG, jo)) {
                    if (jo.getBoolean("autoSignInSuccess")) {
                        Log.other("会员任务📅连续签到[第"
                                + jo.getString("signinSumDay") + "天]#获得["
                                + jo.getString("signinPoint") + "积分]");
                    }
                    Status.flagToday(AntMemberFlag.SIGN_IN.flagName());
                }
            }
            queryPointCert(1, 8);
            if (memberSignInOptions.getValue()
                    .contains(MemberSignInOption.SIGN_PAGE_TASK_LIST.name())) {
                signPageTaskList();
            }
            if (memberSignInOptions.getValue()
                    .contains(MemberSignInOption.QUERY_ALL_STATUS_TASK_LIST.name())) {
                queryAllStatusTaskList();
            }
        } catch (Throwable t) {
            Log.i(TAG, "memberSign err:");
            Log.printStackTrace(TAG, t);
        }
    }


    // 商家秒杀

    private void mrchpoint_ttms_page() {
        try {
            JSONObject jo = new JSONObject(MerchantServiceRpcCall.mrchpoint_ttms_query());
            if (!MessageUtil.checkResponse(TAG, jo) || !jo.has("data")) {
                return;
            }
            JSONObject data = jo.getJSONObject("data");
            if (!data.has("productInfo")) {
                return;
            }
            String productCode = data.getJSONObject("productInfo").optString("productCode");
            jo = new JSONObject(MerchantServiceRpcCall.mrchpoint_ttms_page(productCode));
            if (!MessageUtil.checkResponse(TAG, jo) || !jo.has("data")) {
                return;
            }
            JSONObject seckill = jo.getJSONObject("data").getJSONObject("seckill");
            JSONArray itemInfo = seckill.getJSONArray("itemInfo");
            for (int i = 0; i < itemInfo.length(); i++) {
                jo = itemInfo.getJSONObject(i);
                String channelItemCode = jo.getString("channelItemCode");
                String itemName = jo.getString("itemName");
                MerchantSeckillIdMap.getInstance().add(channelItemCode, itemName);
            }
            MerchantSeckillIdMap.getInstance().save();
            JSONArray seckillingRoundInfo = seckill.getJSONArray("seckillingRoundInfo");
            for (int j = 0; j < seckillingRoundInfo.length(); j++) {
                jo = seckillingRoundInfo.getJSONObject(j);
                String roundStatus = jo.getString("roundStatus");
                if ("WAIT_START".equals(roundStatus)) {
                    JSONArray roundItemInfo = jo.getJSONArray("roundItemInfo");
                    long roundStartTime = jo.getLong("roundStartTime");
                    for (int k = 0; k < roundItemInfo.length(); k++) {
                        jo = roundItemInfo.getJSONObject(k);
                        String itemCode = jo.getString("channelItemCode");
                        if (!merchantSeckillList.contains(itemCode)) {
                            continue;
                        }
                        String roundInstanceId = jo.getString("roundInstanceId");
                        String itemName = MerchantSeckillIdMap.getInstance().get(itemCode);
                        String taskId = "SK|" + roundInstanceId;
                        if (addChildTask(new ChildModelTask(taskId, "SK", () -> {
                            long secKillEndTime = TimeUnit.MINUTES.toMillis(1) + System.currentTimeMillis();
                            while (!mrchpoint_item_seckill(itemCode, 10, roundInstanceId)) {
                                // 尝试抢购到秒杀时间后1分钟
                                if (secKillEndTime < System.currentTimeMillis()) {
                                    break;
                                }
                            }
                            boolean isKilled = Status.hasFlagToday(AntForestAlpha.AntForestAlphaFlag.SEC_KILL.flagName(roundInstanceId));
                            Log.other("蹲点秒杀⚡[" + itemName + "]" + (isKilled ? "成功🎉" : "失败💔"));
                        }, roundStartTime))) {
                            Log.record("添加蹲点秒杀⏰[" + itemName + "]在[" + TimeUtil.getCommonDateTime(roundStartTime) + "]执行");
                        }
                    }
                }
            }

        } catch (Throwable t) {
            Log.i(TAG, "mrchpoint_ttms_page err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static Boolean mrchpoint_item_seckill(String itemCode, int pointAmount, String roundInstanceId) {
        try {
            JSONObject jo = new JSONObject(MerchantServiceRpcCall.mrchpoint_item_seckill(itemCode, pointAmount, roundInstanceId));
            if (Objects.equals("FAILED", jo.getJSONObject("data").getJSONObject("seckillingResult").optString("result"))) {
                // 库存不足
                return true;
            }
            if (MessageUtil.checkResponse(TAG, jo)) {
                Status.flagToday(AntForestAlpha.AntForestAlphaFlag.SEC_KILL.flagName(roundInstanceId));
                return true;
            }
        } catch (Throwable th) {
            Log.i(TAG, "mrchpointItemSeckill err:");
            Log.printStackTrace(TAG, th);
        }
        return false;
    }

    private static void queryPointCert(int page, int pageSize) {
        try {
            JSONObject jo = new JSONObject(AntMemberRpcCall.queryPointCert(page, pageSize));
            TimeUtil.sleep(500);
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            boolean hasNextPage = jo.getBoolean("hasNextPage");
            JSONArray jaCertList = jo.getJSONArray("certList");
            for (int i = 0; i < jaCertList.length(); i++) {
                jo = jaCertList.getJSONObject(i);
                String bizTitle = jo.getString("bizTitle");
                String id = jo.getString("id");
                int pointAmount = jo.getInt("pointAmount");
                jo = new JSONObject(AntMemberRpcCall.receivePointByUser(id));
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.other("会员任务🎖️领取奖励[" + bizTitle + "]#获得[" + pointAmount + "积分]");
                }
            }
            if (hasNextPage) {
                queryPointCert(page + 1, pageSize);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryPointCert err:");
            Log.printStackTrace(TAG, t);
        }
    }

    /**
     * 做任务赚积分
     */
    private void signPageTaskList() {
        if (RuntimeInfo.getInstance().getLong(RuntimeInfo.RuntimeInfoKey.MemberSignPagePauseTime) >= System.currentTimeMillis()) {
            return;
        }
        try {
            otherTaskIdList = new ArrayList<>();
            do {
                JSONObject jo = new JSONObject(AntMemberRpcCall.signPageTaskList());
                if (!MessageUtil.checkResponse(TAG + " signPageTaskList", jo)) {
                    return;
                }
                jo = jo.getJSONObject("resultData");
                if (jo.has("adTaskList")) {
                    JSONArray adTaskList = jo.getJSONArray("adTaskList");
                    if (adTaskList.length() > 0) {
                        for (int i = 0; i < adTaskList.length(); i++) {
                            JSONObject adTask = adTaskList.getJSONObject(i);
                            doADTask(adTask);
                        }
                    }
                }
                boolean doubleCheck = false;
                if (!jo.has("categoryTaskList")) {
                    return;
                }
                JSONArray categoryTaskList = jo.getJSONArray("categoryTaskList");
                for (int j = 0; j < categoryTaskList.length(); j++) {
                    jo = categoryTaskList.getJSONObject(j);
                    JSONArray taskList = jo.getJSONArray("taskProcessVOList");
                    String type = jo.getString("type");
                    if (Objects.equals("BROWSE", type)) {
                        doBrowseTask(taskList);
                    } else {
                        doubleCheck = doOtherTask(taskList);
/*                        ExtensionsHandle.handleRequest(
                                new Request(
                                        RequestType.ENABLE_DEVELOPER_MODE,
                                        RequestMethod.ANT_MEMBER_SIGN_IN_TASK,
                                        jo
                                )
                        );*/
                    }
                }
                if (jo.has("topTask")) {
                    JSONObject topTask = jo.getJSONObject("topTask");
                    doOtherTask(topTask, 0, 1);
                }
                if (doubleCheck) {
                    continue;
                }
                break;
            } while (true);
        } catch (Throwable t) {
            Log.i(TAG, "signPageTaskList err:");
            Log.printStackTrace(TAG, t);
        } finally {
            Long nextRunTime = BaseModel.getNextRunTime();
            if (!TimeUtil.isLessThanSecondOfDays(System.currentTimeMillis(), nextRunTime)) {
                RuntimeInfo.getInstance().put(RuntimeInfo.RuntimeInfoKey.MemberSignPagePauseTime, nextRunTime);
            }
        }
    }

    /**
     * 查询所有状态任务列表
     */
    private void queryAllStatusTaskList() {
        try {
            JSONObject jo = new JSONObject(AntMemberRpcCall.queryAllStatusTaskList());
            TimeUtil.sleep(500);
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            jo = jo.getJSONObject("resultData");
            if (jo.has("taskProcessVOList")) {
                JSONArray taskProcessVOList = jo.getJSONArray("taskProcessVOList");
                doBrowseTask(taskProcessVOList);
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryAllStatusTaskList err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 蚂蚁积分-做浏览任务
    private static void doBrowseTask(JSONArray taskList) {
        //boolean doubleCheck = false;
        Log.i(TAG, taskList.toString());
        try {
            for (int i = 0; i < taskList.length(); i++) {
                JSONObject task = taskList.getJSONObject(i);
                if (task.has("extInfo")) {
                    int periodCurrentCount = Integer.parseInt(task.getJSONObject("extInfo").optString("PERIOD_CURRENT_COUNT", "0"));
                    int periodTargetCount = Integer.parseInt(task.getJSONObject("extInfo").optString("PERIOD_TARGET_COUNT", "1"));
                    int count = periodTargetCount > periodCurrentCount ? periodTargetCount - periodCurrentCount : 0;
                    //Log.i(TAG, "periodCurrentCount:"+periodCurrentCount+"periodTargetCount:"+periodTargetCount+"count:"+count);
                    if (count > 0) {
                        doBrowseTask(task, periodCurrentCount, periodTargetCount);
                    }
                } else {
                    doBrowseTask(task, 0, 1);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "doBrowseTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private static void doBrowseTask(JSONObject task, int left, int right) {
        try {
            JSONObject simpleTaskConfig = task.getJSONObject("simpleTaskConfig");
            String title = simpleTaskConfig.getString("title");
            Long configId = simpleTaskConfig.getLong("configId");
            String awardParamPoint = simpleTaskConfig.getJSONArray("stageVOList").getJSONObject(0).getJSONObject("awardParam")
                    .optString("awardParamPoint");
            String targetBusiness = task.getJSONArray("targetBusiness")
                    .getString(0);
            for (int i = left; i < right; i++) {
                if (!applyTask(title, configId)) {
                    continue;
                }
                TimeUtil.sleep(500);
                String[] targetBusinessArray = targetBusiness.split("#");
                String bizParam;
                String bizSubType;
                if (targetBusinessArray.length > 2) {
                    bizParam = targetBusinessArray[2];
                    bizSubType = targetBusinessArray[1];
                } else {
                    bizParam = targetBusinessArray[1];
                    bizSubType = targetBusinessArray[0];
                }
                JSONObject jo = new JSONObject(AntMemberRpcCall.executeTask(bizParam, bizSubType));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    continue;
                }
                TimeUtil.sleep(2000L);
                String ex = right == 1 && left == 0 ? "" : "(" + (i + 1) + "/" + right + ")";
                Log.other("会员任务🎖️完成任务[" + title + ex + "]#获得[" + awardParamPoint + "积分]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "doBrowseTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 蚂蚁积分-做其他任务
    public static boolean doOtherTask(JSONArray taskList) {
        boolean doubleCheck = false;
        try {
            for (int i = 0; i < taskList.length(); i++) {
                JSONObject task = taskList.getJSONObject(i);
                if (task.has("extInfo")) {
                    int periodCurrentCount = Integer.parseInt(task.getJSONObject("extInfo").optString("PERIOD_CURRENT_COUNT", "0"));
                    int periodTargetCount = Integer.parseInt(task.getJSONObject("extInfo").optString("PERIOD_TARGET_COUNT", "1"));
                    int count = periodTargetCount > periodCurrentCount ? periodTargetCount - periodCurrentCount : 0;
                    if (count > 0) {
                        doubleCheck = doOtherTask(task, periodCurrentCount, periodTargetCount) || doubleCheck;
                    }
                } else {
                    doubleCheck = doOtherTask(task, 0, 1) || doubleCheck;
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "doOtherTask err:");
            Log.printStackTrace(TAG, t);
        }
        return doubleCheck;
    }

    private static boolean doOtherTask(JSONObject task, int left, int right) {
        boolean doubleCheck = false;
        try {
            // String status = task.optString("status");
            JSONObject simpleTaskConfig = task.getJSONObject("simpleTaskConfig");
            String title = simpleTaskConfig.getString("title");
            Long configId = simpleTaskConfig.getLong("configId");
            String awardParamPoint = simpleTaskConfig.getJSONArray("stageVOList").getJSONObject(0).getJSONObject("awardParam")
                    .optString("awardParamPoint");
            String targetBusiness = task.getJSONArray("targetBusiness")
                    .getString(0);
            if ("去健康岛一键领取能量".equals(title)) {
                if (!applyTask(title, configId)) {
                    return doubleCheck;
                }
                JSONObject jojkd = new JSONObject(AntMemberRpcCall.finishOutTask());
                if (MessageUtil.checkResponse(TAG + " finishOutTask", jojkd)) {
                    Log.other("会员任务🎖️完成任务[" + title + "]#获得[" + awardParamPoint + "积分]");

                }
                return doubleCheck;
            } else if ("从首页点击进健康档案".equals(title)) {
                if (!applyTask(title, configId)) {
                    return doubleCheck;
                }
                JSONObject jojkda = new JSONObject(AntMemberRpcCall.clickSendAppBenefit());
                if (MessageUtil.checkResponse(TAG + " clickSendAppBenefit", jojkda)) {
                    Log.other("会员任务🎖️完成任务[" + title + "]#获得[" + awardParamPoint + "积分]");
                }
                return doubleCheck;
            }
            String businessType = simpleTaskConfig.getString("businessType");
            if ("uvChangeBusinessType".equals(businessType)) {
                if (!targetBusiness.startsWith("ngfe")) {
                    return doubleCheck;
                }
                String[] targetBusinessArray = targetBusiness.split("#");
                String tagCode = targetBusinessArray[0];
                if (otherTaskIdList.contains(tagCode)) {
                    return doubleCheck;
                } else {
                    otherTaskIdList.add(tagCode);
                    doubleCheck = true;
                }
                for (int i = left; i < right; i++) {
                    if (applyTask(title, configId)) {
                        JSONObject jo = new JSONObject(AntMemberRpcCall.ngfeUpdate(tagCode));
                        TimeUtil.sleep(2000L);
                        if (!MessageUtil.checkResponse(TAG + " doOtherTask.ngfeUpdate", jo)) {
                            continue;
                        }
                        String ex = right == 1 && left == 0 ? "" : "(" + (i + 1) + "/" + right + ")";
                        Log.other("会员任务🎖️完成任务[" + title + ex + "]#获得[" + awardParamPoint + "积分]");
                    }
                }
            } else if ("countBusinessType".equals(businessType)) {
                String[] targetBusinessArray = targetBusiness.split("#");
                String bizType = targetBusinessArray[0];
                String bizSubType = targetBusinessArray[1];
                if ("邀请好友签到领积分".equals(title)) {
                    if (inviteSign.getValue()) {
                        left = task.optInt("currentCount", 0);
                        right = 10;
                    } else {
                        left = 0;
                        right = 0;
                    }
                }
                for (int i = left; i < right; i++) {
                    if (applyTask(title, configId)) {
                        JSONObject jo = new JSONObject(AntMemberRpcCall.award(bizSubType, bizType));
                        TimeUtil.sleep(2000L);
                        if (!MessageUtil.checkResponse(TAG + " countBusinessType", jo)) {
                            continue;
                        }
                        String ex = right == 1 && left == 0 ? "" : "(" + (i + 1) + "/" + right + ")";
                        Log.other("会员任务🎖️完成任务[" + title + ex + "]#获得[" + awardParamPoint + "积分]");
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "doOtherTask err:");
            Log.printStackTrace(TAG, t);
        }
        return doubleCheck;
    }

    // 蚂蚁积分-做广告任务
    private static boolean applyTask(String title, Long configId) {
        try {
            JSONObject jo = new JSONObject(AntMemberRpcCall.applyTask(title, configId));
            if (MessageUtil.checkResponse(TAG + "applyTask", jo)) {
                TimeUtil.sleep(300);
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "applyTask err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    // 蚂蚁积分-做广告任务
    private static void doADTask(JSONObject task) {
        try {
            JSONObject simpleTaskConfig = task.getJSONObject("simpleTaskConfig");
            String title = simpleTaskConfig.getString("title");
            String awardParamPoint = simpleTaskConfig.getJSONArray("stageVOList").getJSONObject(0).getJSONObject("awardParam")
                    .optString("awardParamPoint");
            JSONObject lightsAdExtMap = task.getJSONObject("lightsAdExtMap");
            String actionType=lightsAdExtMap.getString("actionType");
            if("26".equals(actionType)){
                return;
            }
            String bizId = lightsAdExtMap.optString("bizId");
            if (!bizId.isEmpty()) {
                JSONObject jo = new JSONObject(AntMemberRpcCall.adtaskFinish(bizId));
                TimeUtil.sleep(2000L);
                if (MessageUtil.checkResponse(TAG, jo)) {
                    Log.other("会员任务🎖️完成任务[" + title + "]#获得[" + awardParamPoint + "积分]");
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "doADTask err:");
            Log.printStackTrace(TAG, t);
        }
    }

    // 会员积分兑换
    private void memberPointExchangeBenefit() {
        try {
            JSONObject jo = new JSONObject(AntMemberRpcCall.queryShandieEntityList(new JSONArray()
                    .put("94000SR2025010611442003")
                    .put("94000SR2025010611458003"))
            );
            if (!MessageUtil.checkResponse(TAG, jo) || !jo.has("benefits")) {
                return;
            }
            JSONArray benefits = jo.getJSONArray("benefits");
            for (int i = 0; i < benefits.length(); i++) {
                jo = benefits.getJSONObject(i);
                String benefitId = jo.getString("benefitId");
                String name = jo.getString("name");
                MemberBenefitIdMap.getInstance().add(benefitId, name);
                if (!Status.canMemberPointExchangeBenefitToday(benefitId)
                        || !memberPointExchangeBenefitList.contains(benefitId)) {
                    continue;
                }
                String itemId = jo.getString("itemId");
                if (exchangeBenefit(benefitId, itemId)) {
                    String point = jo.getJSONObject("pricePresentation").getString("point");
                    Log.other("会员积分🎐兑换权益[" + name + "]#消耗[" + point + "积分]");
                }
            }
            MemberBenefitIdMap.getInstance().save();
        } catch (Throwable t) {
            Log.i(TAG, "memberPointExchangeBenefit err:");
            Log.printStackTrace(TAG, t);
        }
    }

    private Boolean exchangeBenefit(String benefitId, String itemId) {
        try {
            JSONObject jo = new JSONObject(AntMemberRpcCall.exchangeBenefit(benefitId, itemId));
            if (MessageUtil.checkResponse(TAG, jo)) {
                Status.memberPointExchangeBenefitToday(benefitId);
                return true;
            }
        } catch (Throwable t) {
            Log.i(TAG, "exchangeBenefit err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    // 消费金签到
    private void signinCalendar() {
        try {
            JSONObject jo = new JSONObject(AntMemberRpcCall.signinCalendar());
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return;
            }
            boolean signed = jo.optBoolean("isSignInToday");
            if (!signed) {
                jo = new JSONObject(AntMemberRpcCall.openBoxAward());
                if (MessageUtil.checkResponse(TAG, jo)) {
                    int amount = jo.getInt("amount");
                    int consecutiveSignInDays = jo.getInt("consecutiveSignInDays");
                    Log.other("攒消费金💰连续签到[第" + consecutiveSignInDays + "天]#获得[" + amount + "消费金]");
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "signinCalendar err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public enum AntMemberFlag implements Status.StatusFlag {
        SIGN_IN
    }

    public enum MemberSignInOption implements CustomOption {
        SIGN_PAGE_TASK_LIST("做任务赚积分"),
        QUERY_ALL_STATUS_TASK_LIST("逛一逛赚积分");

        private final String nickName;

        MemberSignInOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }
}
