package io.github.lazyimmortal.sesame.model.task.immortal;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.StringUtil;

import android.os.Build;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.UUID;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Constanline
 * @since 2023/08/22
 */
public class OtherTaskRpcCall {

        /* 消费金 */
        private static String getRequestId() {
                StringBuilder sb = new StringBuilder();
                for (String str : UUID.randomUUID().toString().split("-")) {
                        sb.append(str.substring(str.length() / 2));
                }
                return sb.toString().toUpperCase();
        }

        public static String taskV2Index(String taskSceneCode) {
                return ApplicationHook.requestString("alipay.mobile.ipsponsorprod.consume.gold.taskV2.index",
                                "[{\"alipayAppVersion\":\"10.5.36.8100\",\"appClient\":\"Android\",\"appSource\":\"consumeGold\",\"cacheMap\":{},\"clientTraceId\":\""
                                                + UUID.randomUUID().toString()
                                                + "\",\"clientVersion\":\"6.3.0\",\"favoriteStatus\":\"LowVersion\",\"taskSceneCode\":\""
                                                + taskSceneCode + "\",\"userType\":\"new\"}]");
        }

        public static String consumeGoldIndex() {
                return ApplicationHook.requestString("alipay.mobile.ipsponsorprod.consume.gold.index",
                                "[{\"appSource\":\"consumeGold\",\"cacheMap\":{},\"clientTraceId\":\""
                                                + UUID.randomUUID().toString()
                                                + "\",\"clientVersion\":\"6.3.0\",\"favoriteStatus\":\"LowVersion\"}]");
        }

        public static String signinCalendar() {
                return ApplicationHook.requestString("alipay.mobile.ipsponsorprod.consume.gold.task.signin.calendar",
                                "[{}]");
        }

        public static String openBoxAward() {
                return ApplicationHook.requestString("alipay.mobile.ipsponsorprod.consume.gold.task.openBoxAward",
                                "[{\"actionAwardDetails\":[{\"actionType\":\"date_sign_start\"}],\"bizType\":\"CONSUME_GOLD\",\"boxType\":\"CONSUME_GOLD_SIGN_DATE\",\"clientVersion\":\"6.3.0\",\"timeScaleType\":0,\"userType\":\"new\"}]");
        }

        public static String taskV2TriggerSignUp(String taskId) {
                return ApplicationHook.requestString("alipay.mobile.ipsponsorprod.consume.gold.taskV2.trigger",
                                "[{\"taskId\":\"" + taskId + "\",\"triggerAction\":\"SIGN_UP\"}]");
        }

        public static String taskV2TriggerSend(String taskId) {
                return ApplicationHook.requestString("alipay.mobile.ipsponsorprod.consume.gold.taskV2.trigger",
                                "[{\"taskId\":\"" + taskId + "\",\"triggerAction\":\"SEND\"}]");
        }

        public static String taskV2TriggerReceive(String taskId) {
                return ApplicationHook.requestString("alipay.mobile.ipsponsorprod.consume.gold.taskV2.trigger",
                                "[{\"taskId\":\"" + taskId + "\",\"triggerAction\":\"RECEIVE\"}]");
        }

        public static String promoTrigger() {
                return ApplicationHook.requestString("alipay.mobile.ipsponsorprod.consume.gold.index.promo.trigger",
                                "[{\"appSource\":\"consumeGold\",\"cacheMap\":{},\"clientTraceId\":\""
                                                + UUID.randomUUID().toString()
                                                + "\",\"clientVersion\":\"6.3.0\",\"favoriteStatus\":\"UnFavorite\",\"requestId\":\""
                                                + getRequestId() + "\"}]");
        }

        public static String advertisement(String outBizNo) {
                return ApplicationHook.requestString(
                                "alipay.mobile.ipsponsorprod.consume.gold.send.promo.advertisement",
                                "[{\"appSource\":\"consumeGold\",\"cacheMap\":{},\"clientTraceId\":\""
                                                + UUID.randomUUID().toString()
                                                + "\",\"clientVersion\":\"6.3.0\",\"favoriteStatus\":\"UnFavorite\",\"outBizNo\":\""
                                                + outBizNo
                                                + "\",\"type\":\"HOME_PROMO_ADVERTISEMENT\",\"userType\":\"new\"}]");
        }

        /**
         * 查询任务
         *
         * @param appletId appletId
         * @return 结果
         */
        public static String taskQuery(String appletId) {
                return ApplicationHook.requestString("com.alipay.loanpromoweb.promo.task.taskQuery",
                                "[{\"appletId\":\"" + appletId + "\",\"completedBottom\":true}]");
        }

        /**
         * 触发任务
         *
         * @param appletId  appletId
         * @param stageCode stageCode
         * @param taskCenId 任务ID
         * @return 结果
         */
        public static String taskTrigger(String appletId, String stageCode, String taskCenId) {
                return ApplicationHook.requestString("com.alipay.loanpromoweb.promo.task.taskTrigger",
                                "[{\"appletId\":\"" + appletId + "\",\"stageCode\":\"" + stageCode
                                                + "\",\"taskCenId\":\"" + taskCenId
                                                + "\"}]");
        }

        public static String signInTrigger(String sceneId) {
                return ApplicationHook.requestString("com.alipay.loanpromoweb.promo.signin.trigger",
                                "[{\"extInfo\":{},\"sceneId\":\"" + sceneId + "\"}]");
        }

        /**
         * 黄金票首页
         *
         * @return 结果
         */
        public static String goldBillIndex() {
                return ApplicationHook.requestString("com.alipay.wealthgoldtwa.needle.goldbill.index",
                                "[{\"pageTemplateCode\":\"H5_GOLDBILL\",\"params\":{\"client_pkg_version\":\"0.0.5\"},"
                                                +
                                                "\"url\":\"https://68687437.h5app.alipay.com/www/index.html\"}]");
        }

        /**
         * 黄金票收取
         *
         * @param str signInfo
         * @return 结果
         */
        public static String goldBillCollect(String str) {
                return ApplicationHook.requestString("com.alipay.wealthgoldtwa.goldbill.v2.index.collect",
                                "[{" + str + "\"trigger\":\"Y\"}]");
        }

        /**
         * 黄金票任务
         *
         * @param taskId taskId
         * @return 结果
         */
        public static String goldBillTrigger(String taskId) {
                return ApplicationHook.requestString("com.alipay.wealthgoldtwa.goldbill.v4.task.trigger",
                                "[{\"goldBillTaskTransferVersion\":\"v2\",\"taskId\":\"" + taskId + "\"}]");
        }

        /**
         * 查询车神卡奖励
         *
         * @return 结果
         */
        public static String v1benefitQuery() {
                return ApplicationHook.requestString("com.alipay.pcreditbfweb.drpc.cargodcard.v1benefitQuery",
                                "[{\"args\":{\"productCode\":\"CAR_MASTER_CARD\"}}]");
        }

        /**
         * 领取车神卡奖励
         *
         * @param jsonObject jsonObject
         * @return 结果
         */
        public static String v1benefitTrigger(JSONObject jsonObject) {
                return ApplicationHook.requestString("com.alipay.pcreditbfweb.drpc.cargodcard.v1benefitTrigger",
                                "[" + jsonObject + "]");
        }

        /**
         * 查询车神卡奖励
         *
         * @return 结果
         */
        public static String queryTaskList() {
                return ApplicationHook.requestString("alipay.promoprod.task.query.queryTaskList",
                                "[{\"consultAccessFlag\": true,\"planId\": \"AP17187348\"}]");
        }

        /**
         * 领取车神卡奖励
         *
         * @param gplusItem gplusItem
         * @return 结果
         */
        public static String signup(String gplusItem, String taskId) {
                String str = "\"taskCenId\":\"AP17187348\",\"taskId\":\"" + taskId + "\"";
                if (!gplusItem.isEmpty()) {
                        str += ",\"extInfo\":{\"gplusItem\":\"" + gplusItem + "\"}";
                }
                return ApplicationHook.requestString("alipay.promoprod.task.query.signup",
                                "[{" + str + "}]");
        }

        /**
         * 领取车神卡奖励
         *
         * @param appletId appletId
         * @return 结果
         */
        public static String complete(String appletId) {
                return ApplicationHook.requestString("alipay.promoprod.applet.complete",
                                "[{\"appletId\":\"" + appletId + "\"}]");
        }

        /* 摇一摇 */
        public static String moduleRecommend() {
                return ApplicationHook.requestString("alipay.fundapplication.op.module.recommend",
                                "[{\"bizCode\":\"RED_ENVELOPE\",\"factors\":{\"chInfo\":\"bc_sydoudi\"},\"moduleCodes\":[\"INTERACT_PROMO\"],\"system\":\"fundapplication\"}]");
        }

        public static String certificateNum(String certTemplateId) {
                return ApplicationHook.requestString("alipay.giftinocenter.camp.campActivity.certificateNum",
                                "[{\"certTemplateId\":\"" + certTemplateId + "\"}]");
        }

        public static String promokernelTrigger(String campInfo) {
                return ApplicationHook.requestString("alipay.promoprod.camp.promokernel.trigger",
                                "[{\"campInfo\":\"" + campInfo + "\"}]");
        }

        public static String activityMatch(String activityId) {
                return ApplicationHook.requestString("alipay.giftinocenter.gift.activity.match",
                                "[{\"activityId\":\"" + activityId
                                                + "\",\"extInfoMap\":{\"checkMode\":\"N\",\"groupInstanceId\":\"\",\"merchantAppId\":\"\",\"merchantPageUrl\":\"\",\"taskToken\":\"\"},\"solutionCode\":\"\",\"specialCode\":\"\"}]");
        }

        public static String activityMatch2(String activityId, String merchantAppId, String merchantPageUrl,
                        String taskToken) {
                return ApplicationHook.requestString("alipay.giftinocenter.gift.activity.match",
                                "[{\"activityId\":\"" + activityId
                                                + "\",\"extInfoMap\":{\"checkMode\":\"N\",\"groupInstanceId\":\"\",\"merchantAppId\":\""
                                                + merchantAppId + "\",\"merchantPageUrl\":\"" + merchantPageUrl
                                                + "\",\"taskToken\":\"" + taskToken
                                                + "\"},\"solutionCode\":\"MERCHANT_MINI_APP\",\"specialCode\":\"\"}]");
        }

        public static String activityComplete(String activityId, String taskToken) {
                return ApplicationHook.requestString("alipay.giftinocenter.gift.activity.complete",
                                "[{\"activityId\":\"" + activityId
                                                + "\",\"extInfoMap\":{\"checkMode\":\"N\",\"groupInstanceId\":\"\",\"taskToken\":\""
                                                + taskToken + "\"}}]");
        }

        public static String taskListQuery2(String taskIds) {
                return ApplicationHook.requestString("alipay.promoprod.task.listQuery",
                                "[{\"consultAccessFlag\":true,\"extInfo\":{\"ALIPAY_APP_VERSION\":\"10.5.63.9000\",\"MOBILE_OS\":\"Android\",\"MOBILE_OS_VERSION\":\"14\"},\"taskIds\":[\""
                                                + taskIds + "\"]}]");
        }

        public static String taskListQuery(String taskCenInfo) {
                return ApplicationHook.requestString("alipay.promoprod.task.listQuery",
                                "[{\"consultAccessFlag\":true,\"extInfo\":{\"ALIPAY_APP_VERSION\":\"10.5.63.9000\",\"MOBILE_OS\":\"Android\",\"MOBILE_OS_VERSION\":\"14\"},\"taskCenInfo\":\""
                                                + taskCenInfo + "\"}]");
        }

        public static String appletTrigger(String appletId) {
                return ApplicationHook.requestString("alipay.promoprod.applet.trigger",
                                "[{\"appletId\":\"" + appletId
                                                + "\",\"source\":\"giftinocenter\",\"stageCode\":\"send\"}]");
        }

        /* 天天来财 */
        public static String ttlc_homepage_query() {
                return ApplicationHook.requestString("alipay.ofpgrowth.ttlc.homepage.query",
                                "[{\"init\":true}]");
        }

        public static String task_recall(String activityId) {
                return ApplicationHook.requestString("alipay.ofpgrowth.ttlc.props.task.recall",
                                "[{\"activityId\":\"" + activityId + "\"}]");
        }

        public static String pixiu_food_use(String activityId, String piXiuFoodIds) {
                return ApplicationHook.requestString("alipay.ofpgrowth.ttlc.pixiu.food.use",
                                "[{\"activityId\":\"" + activityId + "\",\"piXiuFoodIds\":[\"" + piXiuFoodIds
                                                + "\"]}]");
        }

        public static String prize_award(String activityId) {
                return ApplicationHook.requestString("alipay.ofpgrowth.ttlc.prize.award",
                                "[{\"activityId\":\"" + activityId + "\"}]");
        }

        public static String ttlc_appletTrigger(String appletId, String stageCode) {
                return ApplicationHook.requestString("alipay.promoprod.applet.trigger",
                                "[{\"appletId\":\"" + appletId + "\",\"stageCode\":\"" + stageCode + "\"}]");
        }

        /* 健康医疗任务 */
        public static String independent_component_task_reward_query() {
                return ApplicationHook.requestString("alipay.imasp.program.programInvoke",
                                "[{\"channel\":\"health_channel\",\"cityCode\":\"330300\",\"components\":{\"independent_component_task_reward_01190910_independent_component_task_reward_query\":{\"isFillTaskRewardOrder\":\"Y\"}},\"deviceInfo\":{},\"operationParamIdentify\":\"independent_component_program2024040901258676\",\"source\":\"health_channel\"}]");
        }

        public static String independent_component_task_reward_process(String code, String recordNo) {
                return ApplicationHook.requestString("alipay.imasp.program.programInvoke",
                                "[{\"cityCode\":\"330300\",\"components\":{\"independent_component_task_reward_01190910_independent_component_task_reward_process\":{\"code\":\""
                                                + code + "\",\"outBizNo\":" + System.currentTimeMillis()
                                                + ",\"recordNo\":\"" + recordNo
                                                + "\"}},\"operationParamIdentify\":\"independent_component_program2024040901258676\",\"source\":\"health_channel\"}]");
        }

        public static String independent_component_sign_in_recall() {
                return ApplicationHook.requestString("alipay.imasp.program.programInvoke",
                                "[{\"channel\":\"health_channel\",\"cityCode\":\"330300\",\"components\":{\"independent_component_sign_in_01190911_independent_component_sign_in_recall\":{\"signDate\":\"\"}},\"deviceInfo\":{},\"operationParamIdentify\":\"independent_component_program2024040901258676\",\"source\":\"health_channel\"}]");
        }

        public static String independent_component_sign_in(String code) {
                return ApplicationHook.requestString("alipay.imasp.program.programInvoke",
                                "[{\"channel\":\"health_channel\",\"cityCode\":\"330300\",\"components\":{\"independent_component_sign_in_01190911_independent_component_sign_in\":{\"code\":\""
                                                + code
                                                + "\",\"signDate\":\"\"}},\"deviceInfo\":{},\"operationParamIdentify\":\"independent_component_program2024040901258676\",\"source\":\"health_channel\"}]");
        }

        /* 看视频领红包 */
        public static String interact_task_reward(String taskType, String taskActivityId, String rewardParams) {
                String args1;
                if ("signIn".equals(taskType)) {
                        args1 = "[{\"taskType\":\"" + taskType + "\",\"taskActivityId\":\"" + taskActivityId
                                        + "\",\"rewardParams\":\"" + rewardParams.replaceAll("\"", "\\\\\"")
                                        + "\"}]";
                } else {
                        args1 = "[" + rewardParams + "]";
                }
                return ApplicationHook.requestString("alipay.content.interact.task.reward", args1);
        }

        public static String invite_friend_non_status_list() {
                return ApplicationHook.requestString("com.alipay.codeapp.module.invite.friend.non.status.list",
                                "[{\"desc\":\"\",\"extInfo\":\"{}\",\"groupChat\":true,\"invitedUsers\":false,\"needGroupFlock\":false,\"shareInviteSceneId\":\"invite_ztokenV0_ufVQxHrG\",\"title\":\"\"}]");
        }

        public static String extend_carry() {
                return ApplicationHook.requestString("alipay.content.interact.task.extend.carry",
                                "[{\"carry\":\"1\"}]");
        }

        /*
         * public static String invite_friend_non_status_invite(JSONObject json, String
         * str) {
         * return ApplicationHook.requestString(
         * "com.alipay.codeapp.module.invite.start.non.status.invite",
         * "[{\"desc\":\"\",\"extInfo\":\"\",\"inviteUrl\":\"" + URLEncoder.encode(
         * "alipays://platformapi/startapp?appId=20002065&type=detail&refer=alipayHome&voiceOpen=true&chInfo=dailyJoy&shareSource=dailyJoy&clearlist=true&enableTask=1&shareScene=shareTask&carry="
         * + str
         * +
         * "&tab3Redirect=alipays://platformapi/startapp?appId=20000001&actionType=20002065&forceUpdate=true&chInfo=dailyJoy&forceTop=true&selectTab=discovery&selectSubTab=discovery.featured&shareSource=dailyJoy&clearlist=true&enableTask=1&shareScene=shareTask&carry="
         * + URLEncoder.encode(str,
         * StandardCharsets.UTF_8.toString()),
         * StandardCharsets.UTF_8.toString())
         * + "\",\"inviteUserInfo\":"
         * + json.toString().replace("\\/", "/")
         * + ",\"shareInviteSceneId\":\"invite_ztokenV0_ufVQxHrG\",\"title\":\"\"}]");
         * }
         */

        public static String interact_task_reserve() {
                return ApplicationHook.requestString("alipay.content.interact.task.reserve",
                                "[{\"sourcePage\":\"\",\"taskType\":\"reserve\"}]");
        }

        public static String interact_task_query() {
                return ApplicationHook.requestString("alipay.content.interact.task.query",
                                "[{\"pageType\":\"index\",\"taskExt\":\"{}\"}]");
        }

        public static String interact_task_center() {
                return ApplicationHook.requestString("alipay.content.interact.task.center",
                                "[null]");
        }

        public static String interact_task_activity_reward(String taskType) {
                String args1;
                if ("reserve".equals(taskType)) {
                        args1 = "[{\"taskType\":\"reserve\"}]";
                } else if ("cooperation".equals(taskType)) {
                        args1 = "[{\"subTaskType\": \"antFarm\",\"taskType\":\"cooperation\"}]";
                } else {
                        args1 = "[{null}]";
                }
                return ApplicationHook.requestString("alipay.content.interact.task.activity.reward", args1);
        }
        /* 天天领购物金 */
        public static String queryAllTaskInfo() {
                return ApplicationHook.requestString("com.alipay.ugshopping.biz.rpc.promo.queryAllTaskInfo",
                        "[{\"needPollingSignTask\":false}]");
        }

        public static String finishTaskToReward(String taskCode,String subTaskCode) {
                String args ="[{\"taskCode\":\""+taskCode+"\"}]";
                if(!subTaskCode.isEmpty()){
                        args ="[{\"subTaskCode\":\""+subTaskCode+"\",\"taskCode\":\""+taskCode+"\"}]";
                }
                return ApplicationHook.requestString("com.alipay.ugshopping.biz.rpc.promo.finishTaskToReward",args);
        }
        public static String queryGwjInfo() {
                return ApplicationHook.requestString("com.alipay.ugshopping.biz.rpc.promo.queryGwjInfo",
                        "[{}]");
        }
        public static String exchangeGwj(String code) {
                return ApplicationHook.requestString("com.alipay.ugshopping.biz.rpc.promo.exchangeGwj",
                        "[{\"code\":\""+code+"\"}]");
        }


        /* 免费领礼物 */
        public static String chooseItem(String itemId) {
                return ApplicationHook.requestString("com.alipay.ugshopping.biz.rpc.starcoin.chooseItem",
                        "[{\"itemId\":\""+itemId+"\"}]");
        }
        /*签到*/
        public static String getRewardInfo() {
                return ApplicationHook.requestString("com.alipay.ugshopping.biz.rpc.starcoin.getRewardInfo",
                        "[{\"taskTypes\":[]}]");
        }

        public static String queryTask() {
                return ApplicationHook.requestString("com.alipay.ugshopping.biz.rpc.starcoin.queryTask",
                        "[{}]");
        }
        public static String reportTaskCompletion(String taskCode) {
                return ApplicationHook.requestString("com.alipay.ugshopping.biz.rpc.starcoin.reportTaskCompletion",
                        "[{\"taskCode\":\""+taskCode+"\"}]");
        }
        public static String getActivityInfo(String taskCode) {
                return ApplicationHook.requestString("com.alipay.ugshopping.biz.rpc.starcoin.getActivityInfo",
                        "[{\"taskCode\":\""+taskCode+"\"}]");
        }
}
