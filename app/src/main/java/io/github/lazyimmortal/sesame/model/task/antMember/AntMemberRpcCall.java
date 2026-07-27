package io.github.lazyimmortal.sesame.model.task.antMember;

import org.json.JSONArray;

import io.github.lazyimmortal.sesame.data.AlipayInfo;
import io.github.lazyimmortal.sesame.entity.RpcEntity;
import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class AntMemberRpcCall {

    public static Boolean check() {
        RpcEntity rpcEntity = ApplicationHook.requestObject("alipay.antmember.biz.rpc.member.h5.queryPointCert",
                "[{\"page\":" + 1 + ",\"pageSize\":" + 8 + "}]", 1, 0);
        return rpcEntity != null && !rpcEntity.getHasError();
    }

    /* ant member point */
    public static String queryPointCert(int page, int pageSize) {
        String args1 = "[{\"page\":" + page + ",\"pageSize\":" + pageSize + "}]";
        return ApplicationHook.requestString("alipay.antmember.biz.rpc.member.h5.queryPointCert", args1);
    }

    public static String receivePointByUser(String certId) {
        String args1 = "[{\"certId\":" + certId + "}]";
        return ApplicationHook.requestString("alipay.antmember.biz.rpc.member.h5.receivePointByUser", args1);
    }

    public static String queryMemberSigninCalendar() {
        return ApplicationHook.requestString("com.alipay.amic.biz.rpc.signin.h5.queryMemberSigninCalendar",
                "[{\"autoSignIn\":true,\"invitorUserId\":\"\",\"sceneCode\":\"QUERY\"}]");
    }

    /* 会员任务 */
    public static String signPageTaskList() {
        return ApplicationHook.requestString("com.alipay.amic.memtask.h5.MemTaskListQueryFacade.signPageTaskList",
                "[{\"source\":\"antmember\",\"sourcePassMap\":{\"innerSource\":\"\",\"source\":\"myTab\",\"unid\":\"\"},\"spaceCode\":\"ant_member_xlight_task\",\"taskTopConfigId\":\"\"}]");
    }

    public static String applyTask(String darwinName, Long taskConfigId) {
        return ApplicationHook.requestString("alipay.antmember.biz.rpc.membertask.h5.applyTask",
                "[{\"darwinExpParams\":{\"darwinName\":\"" + darwinName
                        + "\"},\"sourcePassMap\":{\"innerSource\":\"\",\"source\":\"myTab\",\"unid\":\"\"},\"taskConfigId\":"
                        + taskConfigId + "}]");
    }

    public static String executeTask(String bizParam, String bizSubType) {
        return ApplicationHook.requestString("alipay.antmember.biz.rpc.membertask.h5.executeTask",
                "[{\"bizOutNo\":\"" + (System.currentTimeMillis() - 16000L) + "\",\"bizParam\":\""
                        + bizParam + "\",\"bizSubType\":\"" + bizSubType + "\",\"bizType\":\"BROWSE\"}]");
    }

    public static String ngfeUpdate(String tagCode) {
        return ApplicationHook.requestString("com.alipay.csprod.prom.camp.ngfe.update",
                "[{\"tagCode\":\"" + tagCode + "\"}]");
    }

    public static String adtaskFinish(String bizId) {
        return ApplicationHook.requestString("com.alipay.adtask.biz.mobilegw.service.task.finish",
                "[{\"bizId\":\""+bizId+"\"}]");
    }
    /*去健康岛一键领取能量*/
    public static String finishOutTask() {
        return ApplicationHook.requestString("com.alipay.neverland.biz.rpc.finishOutTask",
                "[{\"apDid\":\"GUXGKorDqqX/IH4RszM3mRkwV5V8ReUFy08v8M0yKROOn9y2x+vUoP3m+lvcXK0XQpqH/wlbFr/wdI/l/qyd/6wVj9gaPaQZDTTz5dPh/4Q=\",\"cityCode\":\"330100\",\"extInfo\":{\"chInfoSource\":\"ZHI_MEMBER\"},\"source\":\"jkdhylqnl\"}]");
    }
    /*去健康岛一键领取能量*/
    public static String clickSendAppBenefit() {
        return ApplicationHook.requestString("alipay.mobileappconfig.biz.app.clickSendAppBenefit",
                "[{\"appId\":\"2021003141652419\",\"behavior\":\"CLICK\",\"ext\":{\"caprMode\":\"sync\"},\"postion\":\"SQUARE\",\"scene\":\"H5\"}]");
    }

    public static String award(String bizSubType,String bizType) {
        return ApplicationHook.requestString("alipay.iblib.channel.award",
                "[{\"body\":{\"extInfo\":{\"bizSubType\":\""+bizSubType+"\",\"bizType\":\""+bizType+"\"},\"source\":\"antmember\"}}]");
    }

    public static String queryAllStatusTaskList() {
        String args = "[{\"source\":\"signInAd\"}]";
        return ApplicationHook.requestString("com.alipay.amic.memtask.h5.MemTaskListQueryFacade.queryAllStatusTaskList", args);
    }

    /**
     * 查询会员积分兑换福利列表方法1
     *
     * @param userId     userId
     * @param deliveryId 分类码
     *                   94000SR2023102305988003: 0元起
     *                   94000SR2024011106752003: 0元起/公益道具
     *                   94000SR2024071108523003: 0元起/皮肤
     *                   94000SR2024071808609003: 皮肤
     * @return 分类下商品列表
     * @ param naviCode 导航分类码
     * 皮肤："bb82b"、0元起："全积分"、影音："13"
     */
    @Deprecated
    public static String queryDeliveryZoneDetail(String userId, String deliveryId) {
        String uniqueId = System.currentTimeMillis() + "全积分0and99999999INTELLIGENT_SORT" + userId;
        String args = "[{\"cityCode\":\"\",\"deliveryId\":\"" + deliveryId + "\",\"pageNum\":1,\"pageSize\":18,\"sourcePassMap\":{\"innerSource\":\"\",\"source\":\"myTab\",\"unid\":\"\"},\"topIdList\":[],\"uniqueId\":\"" + uniqueId + "\"}]";
        return ApplicationHook.requestString("com.alipay.alipaymember.biz.rpc.config.h5.queryDeliveryZoneDetail", args);
    }

    /**
     * 查询会员积分兑换福利列表方法2
     *
     * @param userId   userId
     * @param naviCode 导航分类码
     *                 特色："14"、出行："1"、美食："11"、日用："12"、上新：""
     * @return 分类下商品列表
     */
    @Deprecated
    public static String queryIndexNaviBenefitFlowV2(String userId, String naviCode) {
        String sortStrategy = "INTELLIGENT_SORT";
        String upperPoint = "99999999";
        String uniqueId = System.currentTimeMillis() + naviCode + "0and" + upperPoint + sortStrategy + userId;
        String args = "[\n" +
                "        {\n" +
                "            \"adCopyId\": \"\",\n" +
                "            \"benefitFlowSource\": \"REC\",\n" +
                "            \"cityCode\": \"\",\n" +
                "            \"excludeIds\": \"\",\n" +
                "            \"exposeChannel\": \"antmember\",\n" +
                "            \"fastTag\": \"\",\n" +
                "            \"lowerPoint\": 0,\n" +
                "            \"naviCode\": \"" + naviCode + "\",\n" +
                "            \"pageNum\": 1,\n" +
                "            \"pageSize\": 50,\n" +
                "            \"requestSourceInfo\": \"-|feeds\",\n" +
                "            \"sortStrategy\": \"" + sortStrategy + "\",\n" +
                "            \"sourcePassMap\": {\n" +
                "                \"innerSource\": \"\",\n" +
                "                \"source\": \"myTab\",\n" +
                "                \"unid\": \"\"\n" +
                "            },\n" +
                "            \"stickyIdList\": [],\n" +
                "            \"tagCodeIdx\": -1,\n" +
                "            \"uniqueId\": \"" + uniqueId + "\",\n" +
                "            \"upperPoint\": " + upperPoint + ",\n" +
                "            \"withPointRange\": false\n" +
                "        }\n" +
                "    ]";
        return ApplicationHook.requestString("com.alipay.alipaymember.biz.rpc.config.h5.queryIndexNaviBenefitFlowV2", args);
    }

    /**
     * 查询会员积分兑换权益列表方法
     *
     * @param deliveryIdList deliveryIdList
     * @return String
     */
    public static String queryShandieEntityList(JSONArray deliveryIdList) {
        StringBuilder uniqueId = new StringBuilder(System.currentTimeMillis() + UserIdMap.getCurrentUid());
        for (int i = 0; i < deliveryIdList.length(); i++) {
            uniqueId.append(deliveryIdList.optString(i));
        }
        String args = "[{\"deliveryIdList\":" + deliveryIdList + ",\"pageNum\":1,\"pageSize\":18,\"queryType\":\"DELIVERY_ID_LIST\",\"uniqueId\":\"" + uniqueId + "\"}]";
        return ApplicationHook.requestString("com.alipay.alipaymember.biz.rpc.config.h5.queryShandieEntityList", args);
    }

    /**
     * 会员积分兑换福利
     *
     * @param benefitId benefitId
     * @param itemId    itemId
     * @return 结果
     */
    public static String exchangeBenefit(String benefitId, String itemId) {
        String requestId = "requestId" + System.currentTimeMillis();
        String alipayClientVersion = AlipayInfo.getVersionName();
        String args = "[{\"benefitId\":\"" + benefitId + "\",\"cityCode\":\"\",\"exchangeType\":\"POINT_PAY\",\"itemId\":\"" + itemId + "\",\"miniAppId\":\"\",\"orderSource\":\"\",\"requestId\":\"" + requestId + "\",\"requestSourceInfo\":\"\",\"sourcePassMap\":{\"alipayClientVersion\":\"" + alipayClientVersion + "\",\"innerSource\":\"\",\"mobileOsType\":\"Android\",\"source\":\"\",\"unid\":\"\"},\"userOutAccount\":\"\"}]";
        return ApplicationHook.requestString("com.alipay.alipaymember.biz.rpc.exchange.h5.exchangeBenefit", args);
    }

    // 消费金签到
    public static String signinCalendar() {
        return ApplicationHook.requestString("alipay.mobile.ipsponsorprod.consume.gold.task.signin.calendar",
                "[{}]");
    }

    public static String openBoxAward() {
        return ApplicationHook.requestString("alipay.mobile.ipsponsorprod.consume.gold.task.openBoxAward",
                "[{\"actionAwardDetails\":[{\"actionType\":\"date_sign_start\"}],\"bizType\":\"CONSUME_GOLD\",\"boxType\":\"CONSUME_GOLD_SIGN_DATE\",\"clientVersion\":\"6.3.0\",\"timeScaleType\":0,\"userType\":\"new\"}]");
    }
}
