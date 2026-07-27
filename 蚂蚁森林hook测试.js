

let ApplicationHook = {
    requestString: function (method, data) {
        app.sendBroadcast({
            action: "com.eg.android.AlipayGphone.cactus.rpctest",
            extras: {
                method: method,
                data: data,
                type: "Rpc"
            }
        })
    }
}

main()

function 获取庄园游戏列表() {
    return ApplicationHook.requestString("com.alipay.antfarm.queryGameList", "[{\"commonDegradeResult\":{\"deviceLevel\":\"high\",\"resultReason\":0,\"resultType\":0},\"platform\":\"Android\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"10.3.36.7200\"}]")
}

/* function 庄园游戏初始(gameType) {
    return ApplicationHook.requestString("com.alipay.antfarm.initFarmGame","[{\"gameType\":\"" + gameType
                + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"toolTypes\":\"STEALTOOL,ACCELERATETOOL,SHARETOOL\"}]")
} */

function 庄园游戏初始(gameType) {
    return ApplicationHook.requestString("com.alipay.antfarm.initFarmGame", "{\"gameType\":\"flyGame\",\"requestType\":\"RPC\",\"sceneCode\":\"FLYGAME\",\"source\":\"FARM_game_yundongfly\",\"toolTypes\":\"ACCELERATETOOL,SHARETOOL,NONE\",\"version\":\"\"}")
}

function 庄园游戏同步分数(gameType, score) {
    return ApplicationHook.requestString("com.alipay.antfarm.recordFarmGame", "[{\"gameType\":\"" + gameType + "\",\"md5\":\"" + '0346c2edb8ee00c05d3c99297bae874f'
        + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"score\":" + score
        + ",\"source\":\"H5\",\"toolTypes\":\"STEALTOOL,ACCELERATETOOL,SHARETOOL\",\"uuid\":\"" + '93eb422b-f90f-4d7f-8600-6e89075ca83a'
        + "\"}]")
}


function 获取蚂蚁新村任务() {
    return ApplicationHook.requestString("com.alipay.antstall.task.list", "[{\"source\":\"ch_appcenter__chsub_9patch\",\"systemType\":\"android\",\"version\":\"0.1.2312271038.27\"}]")
}

function 蚂蚁新村完成任务(taskType) {
    var outBizNo = taskType + "_" + new Date().getTime()
    return ApplicationHook.requestString("com.alipay.antiep.finishTask", "[{\"outBizNo\":\"" + outBizNo
        + "\",\"requestType\":\"RPC\",\"sceneCode\":\"ANTSTALL_TASK\",\"source\":\"AST\",\"systemType\":\"android\",\"taskType\":\""
        + taskType + "\",\"version\":\"0.1.2312271038.27\"}]")
}

function 获取秒杀信息() {
    return ApplicationHook.requestString("com.alipay.antiep.seckill", "[{\"requestType\":\"RPC\",\"sceneCode\":\"ANTFOREST_VITALITY\",\"secKillId\":\"ANTFOREST_VITALITY_MALL_SEC_KILL\",\"source\":\"afEntry\"}]")
}

function 读书同步时长() {
    return ApplicationHook.requestString("com.alipay.antbookread.biz.mgw.syncUserReadInfo", '[{"bookId":"2023082200500009857576","chInfo":"sy_mysl_wzhyd_dnl_2023082200500009857576","chapterId":"2023082200700011670576","extString":"ARqZgmYAACv6UV99bmdnZyd1UDFnaAYJEwUICAwUSgoOCQ45OeQPI3pkaH5r5to7UrXS3nEkiqebM0auRHG9JIPAeLw5nEZP9h8eCtQoeECCLBW4yo7SkGEMEUwdTOXV7Dg7zK0=","miniClientVersion":"1.0.7","readCount":1,"readTime":100000,"timeStamp":1719835908217,"volumeId":"","yuyanVersion":"1.0.2093"}]')
}

function 读书任务状态() {
    return ApplicationHook.requestString("com.alipay.antbookpromo.taskcenter.queryTaskCenterPage", "[{\"bannerId\":\"\",\"chInfo\":\"ch_appcenter__chsub_9patch\",\"hasAddHome\":false,\"miniClientVersion\":\"1.0.0\",\"supportFeatures\":[\"prize_task_20230831\",\"auto_sign_in_20231211\"],\"yuyanVersion\":\"1.0.1829\"}]")
}

function 获取芭芭农场状态() {
    return ApplicationHook.requestString("com.alipay.antfarm.orchardIndex", "[{\"inHomepage\":\"true\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ORCHARD\",\"source\":\"ch_appcenter__chsub_9patch\",\"version\":\"0.1.2401111000.31\"}]")
}

function 获取支付宝会员任务() {
    return ApplicationHook.requestString("alipay.antmember.biz.rpc.membertask.h5.signPageTaskList", "[{\"sourceBusiness\":\"antmember\",\"spaceCode\":\"ant_member_xlight_task\"}]")
}

function 申请支付宝会员任务(darwinName, taskConfigId) {
    return ApplicationHook.requestString("alipay.antmember.biz.rpc.membertask.h5.applyTask", "[{\"darwinExpParams\":{\"darwinName\":\"" + darwinName
        + "\"},\"sourcePassMap\":{\"innerSource\":\"\",\"source\":\"myTab\",\"unid\":\"\"},\"taskConfigId\":"
        + taskConfigId + "}]")
}

function 完成支付宝会员任务(bizParam, bizSubType) {
    return ApplicationHook.requestString("alipay.antmember.biz.rpc.membertask.h5.executeTask", "[{\"bizOutNo\":\"" + new Date().getTime() - 16000 + "\",\"bizParam\":\""
        + bizParam + "\",\"bizSubType\":\"" + bizSubType + "\",\"bizType\":\"OTHERS\"}]")
}

function 获取商家服务任务() {
    return ApplicationHook.requestString("alipay.mrchservbase.zcj.taskList.query", "[{\"compId\":\"ZCJ_TASK_LIST\",\"params\":{\"activityCode\":\"ZCJ\",\"clientVersion\":\"10.3.36\",\"extInfo\":{},\"platform\":\"Android\",\"underTakeTaskCode\":\"\"}}]")
}

function 完成商家服务任务(bizId) {
    return ApplicationHook.requestString("com.alipay.adtask.biz.mobilegw.service.task.finish", "[{\"bizId\":\"" + bizId + "\"}]")
}

function 领取商家服务任务(taskCode) {
    return ApplicationHook.requestString("alipay.mrchservbase.sqyj.task.receive", "[{\"compId\":\"ZTS_TASK_RECEIVE\",\"extInfo\":{\"taskCode\":\"" + taskCode + "\"}}]")
}

function 开始商家服务任务(actionCode) {
    return ApplicationHook.requestString("alipay.mrchservbase.task.query.by.actioncode", "[{\"actionCode\":\"" + actionCode + "\"}]")
}

function 结束商家领取任务(actionCode) {
    return ApplicationHook.requestString("alipay.mrchservbase.biz.task.action.produce", "[{\"actionCode\":\"" + actionCode + "\"}]")
}

function 获取森林集市能量状态() {
    return ApplicationHook.requestString("alipay.bizfmcg.greenlife.consultForSendEnergyByAction", "[{\"sourceType\":\"GREEN_LIFE\"}]")
}

function 领取森林集市能量() {
    return ApplicationHook.requestString("alipay.bizfmcg.greenlife.sendEnergyByAction", "[{\"actionType\":\"GOODS_BROWSE\",\"requestId\":\"q01wychf\",\"sourceType\":\"GREEN_LIFE\"}]")
}

function 森林集市打卡(taskTemplateId) {
    return ApplicationHook.requestString("alipay.bizfmcg.greenlife.finishCurrentTask", "[{\"taskTemplateId\":\"" + taskTemplateId + "\"}]")
}

function 森林开始打地鼠() {
    return ApplicationHook.requestString("alipay.antforest.forest.h5.startWhackMole", "[{\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]")
}

function 森林打地鼠(token) {
    return ApplicationHook.requestString("alipay.antforest.forest.h5.settlementWhackMole", "[{\"moleIdList\":[1,2,3,4,5,6,7],\"settlementScene\":\"NORMAL\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"token\":\"" + token + "\",\"version\":\"20231208\"}]")
}

function 获取森林页面信息() {
    return ApplicationHook.requestString("alipay.antforest.forest.h5.queryHomePage", "[{\"configVersionMap\":{\"redPacketConfig\":0,\"wateringBubbleConfig\":\"10\"},\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\"20231208\"}]")
}

function 查询签到() {
    return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.querySignInBall", "[{\"source\":\"ch_appcenter__chsub_9patch\"}]")
}

function 游戏中心签到() {
    return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.continueSignIn", "[{\"sceneId\":\"GAME_CENTER\",\"signType\":\"NORMAL_SIGN\",\"source\":\"ch_appcenter__chsub_9patch\"}]")
}

function 游戏中心查询任务() {
    return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.queryModularTaskList", "[{\"source\":\"ch_appcenter__chsub_9patch\"}]")
}

function 游戏中心做任务(taskId) {
    return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.doTaskSend", "[{\"taskId\":\"" + taskId + "\"}]")
}

function 车神卡查询任务() {
    return ApplicationHook.requestString("com.alipay.gamecenteruprod.biz.rpc.v3.queryModularTaskList", "[{\n" +
        "\"consultAccessFlag\": true,\n" +
        "\"planId\": \"AP17187348\"\n" +
        "}]")
}

function 黄金票收集(str) {
    return ApplicationHook.requestString("com.alipay.wealthgoldtwa.goldbill.v2.index.collect", "[{\"campId\":\"CP1417744\",\"directModeDisableCollect\":true,\"from\":\"antfarm\",\"trigger\":\"Y\"}]")
}

function 黄金票首页() {
    return ApplicationHook.requestString("com.alipay.wealthgoldtwa.needle.goldbill.index", "[{\"pageTemplateCode\":\"H5_GOLDBILL\",\"params\":{\"client_pkg_version\":\"0.0.5\"},"
        +
        "\"url\":\"https://68687437.h5app.alipay.com/www/index.html\"}]")
}

function 健康医疗能量(uniqueId) {
    return ApplicationHook.requestString("alipay.iblib.channel.data", "[{\"activityCode\":\"produce_forest_energy\",\"activityId\":\"2024052300762674\",\"appId\":\"2021003141652419\",\"body\":{\"scene\":\"FEEDS\",\"uniqueId\":\""
        + uniqueId + "\"},\"version\":\"2.0\"}]")
}

function 健康医疗查询能量() {
    return ApplicationHook.requestString("alipay.iblib.channel.data", "[{\"activityCode\":\"query_forest_energy\",\"activityId\":\"2024052300762675\",\"appId\":\"2021003141652419\",\"body\":{\"scene\":\"FEEDS\"},\"version\":\"2.0\"}]")
}
function 健康医疗收能量(energy, id) {
    return ApplicationHook.requestString("alipay.iblib.channel.data", "[{\"activityCode\":\"harvest_forest_energy\",\"activityId\":\"2024052300762676\",\"appId\":\"2021003141652419\",\"body\":{\"bubbles\":[{\"energy\":"
        + energy + ",\"id\":\"" + id + "\"}],\"scene\":\"FEEDS\"},\"version\":\"2.0\"}]")
}

function 额外收能量(propId, propType) {
    return ApplicationHook.requestString("alipay.antforest.forest.h5.collectRobExpandEnergy", "[{\"propId\":\"" + propId + "\",\"propType\":\"" + propType
        + "\",\"source\":\"chInfo_ch_appcenter__chsub_9patch\"}]")
}

function 完成庄园任务(bizKey) {
    return ApplicationHook.requestString("com.alipay.antfarm.doFarmTask", "[{\"bizKey\":\"" + bizKey
        + "\",\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"source\":\"H5\",\"version\":\"1.8.2302070202.46\"}]")
}

function 多多有礼获取任务(bizKey) {
    return ApplicationHook.requestString("alipay.giftinocenter.gift.activity.match", '[{"activityId":"202401050633300563960000000008199170","extInfoMap":{"checkMode":"N","groupInstanceId":"","merchantAppId":"2021001140664847","merchantPageUrl":"https://2021001140664847.hybrid.alipay-eco.com/index.html#pages/index/index","taskToken":""},"solutionCode":"","specialCode":""}]')
}

function 多多有礼完成任务(bizKey) {
    return ApplicationHook.requestString("alipay.giftinocenter.gift.activity.complete", '[{"activityId":"202401050633300563960000000008199170","extInfoMap":{"checkMode":"N","groupInstanceId":"","merchantAppId":"2021001140664847","merchantPageUrl":"https://2021001140664847.hybrid.alipay-eco.com/index.html#pages/index/index","taskToken":"202406060633300763960000000010927011_fd16d9cc-bc18-4caf-a9c7-cdb1fa85a1dd"}}]')
}

function 青春特快领取() {
    return ApplicationHook.requestString("com.alipay.antiep.receiveTaskAward", '[{"ignoreLimit":true,"requestType":"H5","sceneCode":"ANTFOREST_VITALITY_TASK","source":"ANTFOREST","taskType":"DAXUESHENG_SJK"}]')
}

function 青春特快() {
    return ApplicationHook.requestString("alipay.antforest.forest.h5.popupTask", '[{"fromAct":"pop_task","needInitSign":false,"source":"DNHZ_SL_college","statusList":["TODO","FINISHED"],"version":"20240105"}]')
}

function 出行优惠抽奖() {
    return ApplicationHook.requestString("alipay.imasp.program.programInvoke", '[{"channel":"magicJob_gw_2407","cityCode":"330100","components":{"magicJob_gw_2407_query_point":{"queryUnReceivePoint":"false"},"magicJob_gw_2407_recall_template":{}},"operationParamIdentify":"magicJob_gw_2407","source":"imasp"}]')
}

function 出行优惠获取任务列表() {
    return ApplicationHook.requestString("alipay.imasp.program.programInvoke", '[{"channel":"magicJob_gw_2407","cityCode":"330100","components":{"magicJob_gw_2407_query_point":{},"magicJob_gw_2407_query_task":{}},"operationParamIdentify":"magicJob_gw_2407","source":"imasp"}]')
}

function 摇一摇获取任务(taskCenInfo) {
    return ApplicationHook.requestString("alipay.promoprod.task.listQuery", '[{\"consultAccessFlag\":true,\"extInfo\":{\"ALIPAY_APP_VERSION\":\"10.5.63.9000\",\"MOBILE_OS\":\"Android\",\"MOBILE_OS_VERSION\":\"14\"},\"taskCenInfo\":\"' + taskCenInfo + '\"}]')
}

function 摇一摇加次数(appletId) {
    return ApplicationHook.requestString("alipay.promoprod.applet.trigger", '[{"appletId":"' + appletId + '","source":"giftinocenter","stageCode":"send"}]')
}

function 摇一摇加次数2() {
    return ApplicationHook.requestString("alipay.promoprod.task.complete.notice", '[{"chInfo":"giftinocenter","taskCenInfos":["MZVPQ0DScvD6NjaPJzk8iHi%2BeBruXpIX","MZVPQ0DScvD6NjaPJzk8iOzoUfLFOJw2"]}]')
}

function 摇一摇() {
    return ApplicationHook.requestString("alipay.fundapplication.op.module.recommend", '[{\"bizCode\":\"RED_ENVELOPE\",\"factors\":{\"chInfo\":\"bc_sydoudi\"},\"moduleCodes\":[\"INTERACT_PROMO\"],\"system\":\"fundapplication\"}]')
}

function 摇一摇获取剩余次数(certTemplateId) {
    return ApplicationHook.requestString("alipay.giftinocenter.camp.campActivity.certificateNum", "[{\"certTemplateId\":\"" + certTemplateId + "\"}]")
}

function 摇一摇1(campInfo) {
    return ApplicationHook.requestString("alipay.promoprod.camp.promokernel.trigger", '[{"campInfo":"' + campInfo + '"}]')
}

function 摇一摇2(activityId) {
    return ApplicationHook.requestString("alipay.giftinocenter.gift.activity.match", '[{"activityId":"' + activityId + '","extInfoMap":{"checkMode":"N","groupInstanceId":"","merchantAppId":"","merchantPageUrl":"","taskToken":""},"solutionCode":"","specialCode":""}]')
}

function 摇一摇3(activityId, merchantAppId, merchantPageUrl, taskToken) {
    return ApplicationHook.requestString("alipay.giftinocenter.gift.activity.match", '[{"activityId":"' + activityId + '","extInfoMap":{"checkMode":"N","groupInstanceId":"","merchantAppId":"' + merchantAppId + '","merchantPageUrl":"' + merchantPageUrl + '","taskToken":"' + taskToken + '"},"solutionCode":"MERCHANT_MINI_APP","specialCode":""}]')
}

/* function 摇一摇3(activityId, taskToken) {
    return ApplicationHook.requestString("alipay.giftinocenter.gift.activity.match",'[{"activityId":"' + activityId + '","extInfoMap":{"checkMode":"N","groupInstanceId":"","merchantAppId":"","merchantPageUrl":"","taskToken":"' + taskToken + '"},"solutionCode":"MERCHANT_MINI_APP","specialCode":""}]')
} */

function 摇一摇4(activityId, merchantAppId, merchantPageUrl, taskToken) {
    return ApplicationHook.requestString("alipay.giftinocenter.gift.activity.complete", '[{"activityId":"' + activityId + '","extInfoMap":{"checkMode":"N","groupInstanceId":"","merchantAppId":"' + merchantAppId + '","merchantPageUrl":"' + merchantPageUrl + '","taobaoLiveUserId":"","taskToken":"' + taskToken + '"}}]')
}
function 摇一摇5(activityId, taskToken) {
    return ApplicationHook.requestString("alipay.giftinocenter.gift.activity.complete", '[{"activityId":"' + activityId + '","extInfoMap":{"checkMode":"N","groupInstanceId":"","taskToken":"' + taskToken + '"}}]')
}

function 运动中心完成任务(taskAction, taskId) {
    return ApplicationHook.requestString("com.alipay.sportshealth.biz.rpc.SportsHealthCoinTaskRpc.completeTask", "[{\"chInfo\":\"ch_appcenter__chsub_9patch\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_AI\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"SUPPORT_NEW_MATCH\",\"EXTERNAL_ADVERTISEMENT_TASK\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"taskAction\":\""
        + taskAction + "\",\"taskId\":\"" + taskId + "\"}]")
}

function 运动中心领取奖励(assetId, coinAmount) {
    return ApplicationHook.requestString("com.alipay.sportshealth.biz.rpc.SportsHealthCoinCenterRpc.receiveCoinAsset", '[{"assetId":"' + assetId + '","chInfo":"ch_appcenter__chsub_9patch","clientOS":"android","coinAmount":' + coinAmount + ',"features":["DAILY_STEPS_RANK_V2","STEP_BATTLE","CLUB_HOME_CARD","NEW_HOME_PAGE_STATIC","CLOUD_SDK_AUTH","STAY_ON_COMPLETE","EXTRA_TREASURE_BOX","NEW_HOME_PAGE_STATIC","SUPPORT_AI","SUPPORT_TAB3","SUPPORT_FLYRABBIT","SUPPORT_NEW_MATCH","EXTERNAL_ADVERTISEMENT_TASK","PROP","PROPV2","ASIAN_GAMES"],"tracertPos":"任务面板"}]')
}

function 运动中心获取任务() {
    return ApplicationHook.requestString("com.alipay.sportshealth.biz.rpc.SportsHealthCoinTaskRpc.queryCoinTaskPanel", "[{\"canAddHome\":false,\"chInfo\":\"ch_appcenter__chsub_9patch\",\"clientAuthStatus\":\"not_support\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_AI\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"SUPPORT_NEW_MATCH\",\"EXTERNAL_ADVERTISEMENT_TASK\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"topTaskId\":\"\"}]")
}

function 查询生活记录(recordId) {
    return ApplicationHook.requestString(
        "com.antgroup.zmxy.zmmemberop.biz.rpc.promise.PromiseRpcManager.queryDetail",
        "[{\"recordId\":\"" + recordId + "\"}]");
}
function 神奇物种道具列表() {
    return ApplicationHook.requestString("alipay.antdodo.rpc.h5.propList",
        "[{}]");
}
function 万能兑换神奇卡片(animalId, propId) {
    return ApplicationHook.requestString(
        "alipay.antdodo.rpc.h5.consumeProp",
        '[{"extendInfo":{"animalId":"' + animalId + '"},"propId":"' + propId + '","propType":"UNIVERSAL_CARD_7_DAYS"}]');
}

function 视频签到(rewardParams) {
    return ApplicationHook.requestString("alipay.content.interact.task.reward", '[{"rewardParams":' + JSON.stringify(rewardParams) + ',"taskActivityId":"PLAY102553396","taskType":"signIn"}]');
}
//2088702225705653
function 八周年分享(inviteUserIds) {
    return ApplicationHook.requestString("alipay.antforest.forest.h5.activity.doRubickActivity", '[{"actionCode":"sharePlantTrace","activityId":"anniversary8th","extentMap":{"inviteUserIds":' + JSON.stringify(inviteUserIds) + ',"sendChat":false},"source":"ant_forest_home"}]');
}

function 健康医疗账单能量() {
    return ApplicationHook.requestString("alipay.iblib.channel.data", '[{"activityCode":"produce_forest_energy","activityId":"2024052300762674","body":{"scene":"BILL","uniqueId":"' + new Date().getTime() + '"},"version":"2.0"}]');
}

function 查询健康医疗账单能量() {
    return ApplicationHook.requestString("alipay.iblib.channel.data", '[{"activityCode":"query_drug_notice","activityId":"2022081600001208","body":{"source":"main_channel"},"version":"2.0"}]');
}

function main() {
    //获取安卓版本
    let Android_version = device.release
    //文件路径
    let dateOfToday = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date())
    var path = "/storage/emulated/0/Android/media/com.eg.android.AlipayGphone/cactus/log/debug." + dateOfToday + ".log";

    if (files.exists(path)) {
        //清空文件
        files.write(path, "")
    }
    let method = 'com.alipay.antbooks.biz.mgw.queryUserScore'
    let args1 = '[{"albumId":2018700000,"chInfo":"ch_appid-20002103__chsub_pageid-com.alipay.android.phone.wallet.taskmanager.ui.TaskManagerActivity","miniClientVersion":"1.0.7","supportFeatures":["podcast_version_20240801"],"yuyanVersion":"1.3.48"}]';
    ApplicationHook.requestString(method, args1);
    sleep(1000)
    method = 'com.alipay.antbookpromo.ad.queryAdStrategy'
    args1 = '[{"albumId":2018700000,"bizType":"AlbumPlayer","chInfo":"ch_appid-20002103__chsub_pageid-com.alipay.android.phone.wallet.taskmanager.ui.TaskManagerActivity","miniClientVersion":"1.0.7","supportFeatures":["podcast_version_20240801"],"yuyanVersion":"1.3.48"}]';
    ApplicationHook.requestString(method, args1);
    sleep(1000)
    method = 'com.alipay.antbookpromo.minitaskcenter.queryMiniTaskCenterInfo'
    args1 = '[{"chInfo":"ch_appid-20002103__chsub_pageid-com.alipay.android.phone.wallet.taskmanager.ui.TaskManagerActivity","hasAddHome":false,"isFromSync":false,"miniClientVersion":"1.0.7","needInfos":"","supportFeatures":["podcast_version_20240801"],"yuyanVersion":"1.3.48"}]';
    ApplicationHook.requestString(method, args1);
    sleep(1000)
    method = 'com.alipay.antbooks.biz.mgw.queryPlayPage'
    args1 = '[{"albumId":2018700000,"chInfo":"ch_appid-20002103__chsub_pageid-com.alipay.android.phone.wallet.taskmanager.ui.TaskManagerActivity","miniClientVersion":"1.0.7","sceneId":"","soundId":4067386178,"supportFeatures":["podcast_version_20240801"],"yuyanVersion":"1.3.48"}]';
    ApplicationHook.requestString(method, args1);
    sleep(1000)
    method = 'com.alipay.antbooks.biz.mgw.queryWufuCardActivity'
    args1 = '[{"albumId":2018700000,"chInfo":"ch_appid-20002103__chsub_pageid-com.alipay.android.phone.wallet.taskmanager.ui.TaskManagerActivity","miniClientVersion":"1.0.7","sceneCode":"listenBooks","soundId":4067386178,"supportFeatures":["podcast_version_20240801"],"yuyanVersion":"1.3.48"}]';
    ApplicationHook.requestString(method, args1);

    sleep(1000)
    method = 'com.alipay.antbooks.biz.mgw.syncUserPlayData'
    args1 = '[{"chInfo":"ch_appcollect__chsub_my-recentlyUsed","miniClientVersion":"1.0.7","supportFeatures":["podcast_version_20240801"],"syncingPlayRecordRequestList":[{"albumId":2018700000,"position":1893,"soundId":4067386178,"timestamp":' + new Date().getTime() + '}],"yuyanVersion":"1.3.48"}]';
    ApplicationHook.requestString(method, args1);
    //查询健康医疗账单能量()
    //健康医疗账单能量()

    /*     ApplicationHook.requestString("alipay.ofpgrowth.ttlc.props.task.recall",
            "[{\"activityId\":\"" + 'AC2024081700000104794' + "\"}]"); */

    /*         ApplicationHook.requestString("alipay.ofpgrowth.ttlc.pixiu.food.use",
                "[{\"activityId\":\"AC2024081700000104794\",\"piXiuFoodIds\":[\"FO2024081965655024051487\"]}]") */
    /*         ApplicationHook.requestString("alipay.promoprod.applet.trigger",
                "[{\"appletId\":\"" + 'AP13240569' + "\",\"stageCode\":\"send\"}]"); */
    /*     ApplicationHook.requestString("alipay.ofpgrowth.ttlc.homepage.query",
            "[{\"init\":true}]") */

    /*     app.sendBroadcast({
            action: "com.eg.android.AlipayGphone.cactus.rpctest",
            extras: {
                method: "com.alipay.sportshealth.biz.rpc.SportsHealthCoinTaskRpc.signInCoinTask",
                data: '[{\"chInfo\":\"ch_appcenter__chsub_9patch\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_AI\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"SUPPORT_NEW_MATCH\",\"EXTERNAL_ADVERTISEMENT_TASK\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"operatorType\":\"signIn\"}]',
                type: "Rpc"
            }
        }) */

    /*     app.sendBroadcast({
            action: "com.eg.android.AlipayGphone.cactus.rpctest",
            extras: {
                method: "",
                data: "",
                type: "getTreeItems"
            }
        }) */

    /*                 app.sendBroadcast({
                        action: "com.eg.android.AlipayGphone.cactus.rpctest",
                        extras: {
                            method: "",
                            data: "",
                            type: "interactTaskQuery"
                        }
                    }) */
    //ApplicationHook.requestString("com.alipay.antfarm.DeliverMsgSend", '[{"content":"清早的露珠，映着日头的光辉，愿这份温柔的美好，伴你度过明媚的一天。早安，愿君心如初见，笑如春风。","deliverId":"17254880518972088702225705653","friendUserIds":["2088442024894434","2088122198284683"],"groupId":"0057400009220240904085714654","mode":"AI","requestType":"NORMAL","sceneCode":"ANTFARM","source":"H5","spaceType":"ChickFamily"}]');
    //ApplicationHook.requestString("com.alipay.antfarm.doFarmTask", '[{"bizKey":"INVITE_ELDER","requestType":"RPC","sceneCode":"ANTFARM","source":"H5","taskSceneCode":"ANTFARM_FAMILY_TASK"}]');
    let VERSION = "20240806";
    let args = "[{\"requestType\":\"NORMAL\",\"sceneCode\":\"ANTFARM\",\"signSceneCode\":\"\",\"source\":\"H5\",\"taskSceneCode\":\"ANTFARM_FAMILY_TASK\"}]";
    // ApplicationHook.requestString("com.alipay.antfarm.listFarmTask", args);
    /*    ApplicationHook.requestString("alipay.antforest.forest.h5.queryHomePage",
        "[{\"configVersionMap\":{\"wateringBubbleConfig\":\"10\"},\"skipWhackMole\":true,\"source\":\"chInfo_ch_appcenter__chsub_9patch\",\"version\":\""
                + VERSION + "\"}]") */
    //ApplicationHook.requestString("com.antgroup.zmxy.zmmemberop.biz.rpc.promise.PromiseRpcManager.queryHome", "null");

    //ApplicationHook.requestString("alipay.content.interact.task.query", '[{\"pageType\":\"index\",\"taskExt\":\"{}\"}]');
    let inviteUserIds = [
        "2088702225705653"
    ]
    //八周年分享(inviteUserIds)

    //let args1 = "[{\"page\":" + "1" + ",\"pageSize\":" + "8" + "}]";
    // ApplicationHook.requestString("alipay.antmember.biz.rpc.member.h5.queryPointCert", args1);

    let rewardParams = { "completed": false, "origTaskType": "duration_2", "rewardParams": "{\"cp\":\"CP142266784\",\"er\":\"f271235cb63a05f3869b96d1ac0135fd\",\"lti\":\"0b44243817241459534537338eb089\",\"osc\":0,\"ot\":\"duration_2\",\"s\":12,\"sc\":30,\"t\":3300144,\"ts\":1724145953,\"tt\":\"duration\"}", "taskActivityId": "CP142266784", "taskData": { "amount": "0.1", "availableAmount": "0.23", "duration": 30, "ext": { "noReceivedRedPacketImg": "https://mdn.alipayobjects.com/huamei_uddmhv/afts/img/A*lpK-T5GbqfMAAAAAAAAAAAAADq-rAQ/fmt.avif", "receivedRedPacketImg": "https://mdn.alipayobjects.com/huamei_uddmhv/afts/img/A*EBmZQrUT58YAAAAAAAAAAAAADq-rAQ/fmt.avif" }, "rewardType": "redEnvelope", "stage": 12, "unit": "元" }, "taskType": "duration", "todayLimited": false }
    rewardParams.taskExt = "{}";
    rewardParams.hasTask = true;
    rewardParams.loading = false;
    rewardParams.contentId = '20240723OB020010039445483728'
    rewardParams.ext = '{"chInfo":"ch_life__chsub_Ndiscovery.featured","contentInfo":{"_act_src":"zrtj","_ad_sys":"contentlib","_item_id":"20240723OB020010039445483728","_item_src":"contentId","algoTagIds":"A_BRA845@A_BZ11@A_BZ6@A_CQ35@A_DY255@A_DY431@A_DY531@A_IP2@A_LG1@A_LZ000086@A_MR11@A_MR3@A_PCH10@A_PCH3@A_PCH5@A_QS3@A_SE176@A_TQ1@A_VQ14@A_VQ82@A_WT201@A_WT25@A_ZQ1@A_ZQ4@A_ZQ5@A_ZQ8@A_ZQ9@B_CI2@B_FT1@C_FT2@C_FT4@C_LL1@C_MG5@C_ML2@C_PT12@C_TN2@F_CV2@G_FT1@H_DR1@I_BR1@I_XC1@M_LS1@S_AS1@T_NE1@V_PP1@V_SR1@Z_EX4@A_EH2@H_VA2@S_EE14@C_MH1@H_HC7@V_CL2@C_OS2@R_CQ10@R_CQ11@R_CQ9@U_SV1","allCateIds":"A_WT25@A_WT201","authorId":"2030092659327944","categoryIdL1":"A_WT25","contentId":"20240723OB020010039445483728","contentType":"video","distributionType":"recommend","flowExpFlag":"exp","flowFlag":"20TOTALITEM000_highbase@Default_highbase","high_painting_style":"2","isCollect":"false","isLike":"false","newRequestType":"next_screen","oraScmRecmixer":"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives0_5_qianyi_v2@tab3_mix_goods_add_358587_2@tab3_immersion_mix_lives_mj_v325_rt0819b","pageIndex":"2","red_banner":"false","requestType":"next_page","sceneAbExp":"adScene","scm":"a1003.b133.video.20240723OB020010039445483728.2189b97d17241253343636789ec6eb.34301.33200419.33200419.232319590+232319587+242118019+241418266+243518216+243519551.48606.-.01.immr","subType":"3","userTag":"low_activity_user"},"nextContentInfo":{"_act_src":"zrtj","_ad_sys":"contentlib","_item_id":"20240801OB020010031453080121","_item_src":"contentId","algoTagIds":"A_AS1@A_CQ33@A_DY236@A_DY247@A_DY264@A_IP2@A_KW30072@A_LG1@A_LZ000086@A_MR11@A_MR16@A_MR3@A_QS2@A_SZ3@A_TQ1@A_VQ15@A_VQ82@A_WT286@A_WT30@A_ZQ1@A_ZQ10@A_ZQ4@A_ZQ5@A_ZQ8@B_CI2@C_AS2@C_FT2@C_FT4@C_MG5@C_ML2@C_PT12@C_TN2@F_CV2@G_FT2@H_DR1@I_BR1@I_XC1@M_LS1@S_AS1@T_NE1@V_PP1@V_SR1@A_EH2@H_VA5@V_CL2@B_FT1@R_CQ11@U_SV1@S_EE10","allCateIds":"A_WT30@A_WT286","authorId":"2030093510780149","canDisplay":"0","canDownload":"0","canSelectReply":"0","canSmartCover":"1","categoryIdL1":"A_WT30","chInfo":"ch_life__chsub_Ndiscovery.featured","contentId":"20240801OB020010031453080121","contentType":"video","curChInfo":"ch_life__chsub_Ndiscovery.featured","distributionType":"recommend","env":"PROD","flowExpFlag":"exp","flowFlag":"20TOTALITEM000_highbase@Default_highbase","high_painting_style":"2","infoSecResult":"null","ip":"113.251.84.11","ipLocation":"{\"areaCode\":\"50\",\"city\":\"重庆市\",\"cityCode\":\"500000\",\"country\":\"中国\",\"countryCode\":\"CN\",\"countryCode3\":\"CHN\",\"countryCodeNo\":\"156\",\"county\":\"渝北区\",\"countyCode\":\"500112\",\"ip\":\"113.251.84.11\",\"province\":\"重庆市\",\"provinceCode\":\"500000\"}","mcnPid":"2088341731558142","needDoubleWriteOldContent":"true","newRequestType":"next_screen","offerCardAtomicTMPLId":"LIFETAB_detail_content_offer_card_atomic","offerCardAtomicTMPLVer":"256","oraScmRecmixer":"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives0_5_qianyi_v2@tab3_mix_goods_add_358587_2@tab3_immersion_mix_lives_mj_v325_rt0819b","pageIndex":"2","pageType":"index","publicId":"2030093510780149","red_banner":"false","refer":"discovery","requestType":"next_page","sceneAbExp":"adScene","scm":"a1003.b133.video.20240801OB020010031453080121.2189b97d17241253343636789ec6eb.34301.33200419.33200419.232319590+232319587+242118019+241418266+243518216+243519551.5001.-.01.immr","source_type":"public","subTabType":"discovery.featured","subType":"3","tabType":"discovery","userTag":"low_activity_user","viewToken":"d56152371522cae09cbdafdeb82fad7b"}}';
    //console.log(JSON.stringify(rewardParams).replace("\\/", "/"))
    //ApplicationHook.requestString("alipay.content.interact.task.reward", "["+JSON.stringify(rewardParams)+"]");
    //ApplicationHook.requestString("alipay.content.interact.task.query", '[{\"pageType\":\"index\",\"taskExt\":\"{}\"}]');

    //ApplicationHook.requestString("alipay.content.interact.task.center","[null]");

    //ApplicationHook.requestString("alipay.content.interact.task.activity.reward","[{\"subTaskType\": \"antFarm\",\"taskType\":\"cooperation\"}]");
    /* ApplicationHook.requestString("alipay.content.interact.task.reserve",
        "[{\"sourcePage\":\"\",\"taskType\":\"reserve\"}]"); */
    //视频签到() 
    /*     
    let taskType = 'ONE_CLICK_WATERING_V1'
        let sceneCode = 'ANTFOREST_VITALITY_TASK'
        let outBizNo = taskType + "_123456433765"
        ApplicationHook.requestString("com.alipay.antiep.finishTask",
            "[{\"outBizNo\":\"" + outBizNo + "\",\"requestType\":\"H5\",\"sceneCode\":\"" +
            sceneCode + "\",\"source\":\"ANTFOREST\",\"taskType\":\"" + taskType + "\"}]"); */
    //运动中心获取任务()
    //运动中心完成任务("JUMP", "AP19222143")

    /*     ApplicationHook.requestString(
            "com.alipay.sportshealth.biz.rpc.SportsHealthCoinTaskRpc.signUp",
            "[{\"chInfo\":\"ch_appcenter__chsub_9patch\",\"clientOS\":\"android\",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_AI\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"SUPPORT_NEW_MATCH\",\"EXTERNAL_ADVERTISEMENT_TASK\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"taskAction\":\""
                            + "JUMP" + "\",\"taskId\":\"" + "AP19222143" + "\"}]"); */
    //运动中心领取奖励("B00000000101BD1CCFEFEBF50E79A727CB121F2AAF7A10012088702225705653", 35)
    /* ApplicationHook.requestString(
        "com.alipay.sportshealth.biz.rpc.SportsHealthCoinCenterRpc.receiveCoinAsset",
        "[{\"assetId\":\"B00000000101B668E1C5FB86086FFA93183505FC122D10012088702225705653\",\"chInfo\":\"ch_appcenter__chsub_9patch\",\"clientOS\":\"android\",\"coinAmount\":"
                        + 35
                        + ",\"features\":[\"DAILY_STEPS_RANK_V2\",\"STEP_BATTLE\",\"CLUB_HOME_CARD\",\"NEW_HOME_PAGE_STATIC\",\"CLOUD_SDK_AUTH\",\"STAY_ON_COMPLETE\",\"EXTRA_TREASURE_BOX\",\"NEW_HOME_PAGE_STATIC\",\"SUPPORT_TAB3\",\"SUPPORT_FLYRABBIT\",\"PROP\",\"PROPV2\",\"ASIAN_GAMES\"],\"tracertPos\":\"任务面板\"}]"); */
    //sleep(3000)
    //运动中心完成任务("SHOW_AD", "AP11229676")
    //摇一摇获取任务('MZVPQ0DScvD6NjaPJzk8iHi%2BeBruXpIX')
    //青春特快()
    //摇一摇加次数("AP17193231")

    //摇一摇加次数2()
    //摇一摇获取剩余次数('CT8240747')
    //摇一摇()
    //摇一摇1("ybjsS1zkl75GQ6G7pIZ3627BKZC0vt5b")
    //摇一摇2("202405270633300263960000000005177022")
    //摇一摇3("202406250633300563960000000011066007", "2021001178689171", "alipays://platformapi/startapp?appId=2021001178689171&page=pages%2Findex%2Findex%3Ffeature%3Dautosave%26container%3DSINGLE%26forceOpen%3Dtrue&chInfo=bchongbao_f00", "202406250633300763960000000011227765_6a73e9ea-f0c3-47bb-b1e8-b6988d8a79f7")

    /*      摇一摇5("202406250633300563960000000011066007",  "202406250633300763960000000011227765_55b75a72-36a2-434f-9003-780ad6371f12") */
    //
    //青春特快领取()
    //完成庄园任务('GREETING')
    //额外收能量("1eqe66z1uje2p11gr5gc01sml7fx4680","ROB_EXPAND_CARD_20")
    //健康医疗能量("20240611OB020010036515121805")
    //健康医疗获取视频()
    //健康医疗查询能量()
    //健康医疗收能量(16,'6201328609')
    //蚂蚁新村完成任务("SHANGYEHUA_ceshi")

    //查询签到()
    //游戏中心签到()
    //游戏中心查询任务()

    //游戏中心做任务("AP16159058")
    //黄金票首页()
    //黄金票收集("\"campId\":\"CP1417744\",\"directModeDisableCollect\":true,\"from\":\"antfarm\",")
    //车神卡查询任务()
    //获取森林页面信息()
    //森林开始打地鼠()
    //森林打地鼠("fa98f275-d482-4009-8282-cab8778776f0")
    //森林集市打卡(2024050700032809)
    //领取森林集市能量() 

    //获取森林集市能量状态()
    /*     let gameType = 'flyGame'
        庄园游戏初始(gameType)
        sleep(1000)
        庄园游戏同步分数(gameType, 4218)
     */

    //获取商家服务任务()
    /*       领取商家服务任务('TYWQWWJLHB_TASK')
          sleep(500)
          开始商家服务任务('TYWQWWJLHB_TASK_VIEWED')
          结束商家领取任务('TYWQWWJLHB_TASK_VIEWED') */
    //获取支付宝会员任务()

    /*     申请支付宝会员任务('订阅油价涨跌提醒', 600202300003715)
        sleep(1000)
        完成支付宝会员任务('Y', 'ngfe_tag__r9ilgwiwpd') */
    //蚂蚁新村完成任务("ANTSTALL_TASK_nongchangleyuan")
    //获取蚂蚁新村任务()
    //获取芭芭农场状态()
    //读书同步时长()
    //读书任务状态()
    sleep(3000)
    if (files.exists(path)) {
        var file = open(path);
        //读取文件的所有内容
        var text = file.read();
        console.log(text)
    }
}