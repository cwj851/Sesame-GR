/**
 * 蚂蚁庄园 - 亲密家庭「美食请客」Auto.js 脚本（xrz 分支方式）
 *
 * 原理：调用 Sesame(cwj) 模块内置 HTTP 调试接口 /debugHandler 远程执行支付宝 RPC，
 *       复刻 AntFarm.java#familyEatTogether() 的 xrz 分支实现。
 *
 * 与 cwj 方式的差异：
 *   1. 请客成员列表通过 com.alipay.antfarm.queryFamilyInfo 获取（getFamilyMemberList(true,true)，
 *      含非好友与自己），而非 enterFamily 的 animals
 *   2. syncAnimalStatus 使用 xrz 参数：operTag=SYNC_RESUME / operType=QUERY_ALL，不带 userId/version
 *   3. 触发不再要求 eatTogetherConfig.periodItemList
 *   4. 请客成功后调用 syncFamilyStatus 同步 FAMILY_INTERACT_ACTION 与 INTIMACY_VALUE，
 *      服务端据此清除 EatTogether 标记，从而支持当日多次（每餐段）请客
 *
 * debugHandler 用法（POST http://127.0.0.1:8080/debugHandler）：
 *   Content-Type: application/json
 *   Authorization: Bearer <TOKEN>   （Token 见 ApplicationHook.java，模块启动时硬编码）
 *   Body: {"methodName":"<RPC方法名>","requestData":"<数组形式JSON字符串>"}
 *   返回：RPC 原始 JSON 字符串（若 RPC 无结果则返回 {"status":"empty"}）
 *
 * 前置条件：
 *   1. 支付宝已安装并加载 Sesame(cwj) 模块，HTTP 服务运行于 127.0.0.1:8080
 *   2. Auto.js 已开启悬浮窗/无障碍权限（http 与 toast 需要）
 *
 * 流程：
 *   enterFarm 取自己 farmId -> enterFamily 取家庭信息 -> queryFamilyInfo 取成员 ->
 *   syncAnimalStatus 取厨房美食 -> 按 count 降序凑齐 N 份 ->
 *   familyEatTogether 请客 -> syncFamilyStatus 同步交互动作与亲密度
 */

const HOST = "http://127.0.0.1:8080";
const DEBUG_HANDLER = HOST + "/debugHandler";
const TOKEN = "ET3vB^#td87sQqKaY*eMUJXP";
const VERSION = "20250818";

// 可选：手动指定当前支付宝 userId（2088 开头）。
// 留空时脚本会自动调用蚂蚁森林首页接口获取；自动获取失败则必须手动填写。
const MANUAL_UID = "";

// ================= RPC 封装 =================
function rpc(methodName, requestData) {
    const payload = {
        methodName: methodName,
        requestData: JSON.stringify(requestData)
    };
    // http.postJson(url, data, options)：headers 需放在 options.headers 中
    const res = http.postJson(DEBUG_HANDLER, payload, {
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + TOKEN
        }
    });
    const body = res.body.string ? res.body.string() : String(res.body);
    console.log("RPC >>> " + methodName);
    console.log("RPC <<< " + body);
    if (!body || body.trim() === "") {
        throw new Error(methodName + " 返回空响应");
    }
    const parsed = JSON.parse(body);
    if (parsed && parsed.status === "empty") {
        throw new Error(methodName + " 返回空数据");
    }
    return parsed;
}

// ================= 获取当前 userId =================
// 通过蚂蚁森林首页接口拿到当前登录用户的 userId（请求不含 userId 参数）
function getCurrentUid() {
    if (MANUAL_UID) return MANUAL_UID;
    const home = rpc("alipay.antforest.forest.h5.queryHomePage", [{
        configVersionMap: { wateringBubbleConfig: "10" },
        skipWhackMole: true,
        source: "chInfo_ch_appcenter__chsub_9patch",
        version: VERSION
    }]);
    const candidates = [
        home && home.userBaseInfo && home.userBaseInfo.userId,
        home && home.userId,
        home && home.userBaseInfo && home.userBaseInfo.userInfoList && home.userBaseInfo.userInfoList[0] && home.userBaseInfo.userInfoList[0].userId
    ];
    for (let i = 0; i < candidates.length; i++) {
        if (candidates[i]) return candidates[i];
    }
    throw new Error("无法自动获取 userId，请在脚本顶部 MANUAL_UID 手动填写当前支付宝 userId");
}

// ================= 家庭成员列表（xrz 方式：queryFamilyInfo）=================
// 对齐 AntFarm.java#getFamilyMemberList(containsNotFriend, containsOwner)
function getFamilyMemberList(containsNotFriend, containsOwner) {
    const info = rpc("com.alipay.antfarm.queryFamilyInfo", [{
        requestType: "NORMAL",
        sceneCode: "ANTFARM",
        source: "H5"
    }]);
    if (!info || (info.success !== true && info.memo !== "SUCCESS")) {
        throw new Error("queryFamilyInfo 调用失败: " + JSON.stringify(info));
    }
    const list = info.familyMemberInfoList || [];
    const result = [];
    for (let i = 0; i < list.length; i++) {
        const m = list[i];
        const uid = m.userId;
        if (!uid) continue;
        const isCurrent = m.currentUser === true;
        const isFriend = m.friend === true;
        if (isCurrent ? containsOwner : (isFriend || containsNotFriend)) {
            result.push(uid);
        }
    }
    return result;
}

// ================= 时段判断 =================
function getEatPeriod() {
    const d = new Date();
    const t = d.getHours() * 100 + d.getMinutes();
    if (t >= 600 && t < 1100) return "早餐";
    if (t >= 1100 && t < 1600) return "午餐";
    if (t >= 1600 && t < 2000) return "晚餐";
    return null;
}

// ================= 凑美食（count 降序，凑齐 needCount 份）=================
function pickCuisines(cuisineList, needCount) {
    const sorted = cuisineList.slice().sort(function (a, b) {
        return (b.count || 0) - (a.count || 0);
    });
    const result = [];
    let total = 0;
    for (let i = 0; i < sorted.length && total < needCount; i++) {
        const c = sorted[i];
        const cnt = (c.count || 0);
        if (cnt <= 0) continue;
        const take = (total + cnt >= needCount) ? (needCount - total) : cnt;
        result.push(Object.assign({}, c, { count: take }));
        total += take;
    }
    if (total !== needCount) return null;
    return result;
}

// ================= 主流程 =================
function main() {
    toast("开始执行家庭美食请客");

    // 0. 先获取当前登录用户 userId（enterFarm 必须传真实 uid，否则服务端返回 104）
    const currentUid = getCurrentUid();

    // 1. 自己的庄园信息（取 ownerFarmId）
    //    enterFarm 响应结构：{farmVO:{subFarmVO:{farmId},masterUserInfoVO:{userId}},...}
    const farm = rpc("com.alipay.antfarm.enterFarm", [{
        queryLastRecordNum: true,
        recall: false,
        requestType: "NORMAL",
        sceneCode: "ANTFARM",
        source: "H5",
        userId: currentUid
    }]);
    if (!farm || farm.memo !== "SUCCESS") {
        throw new Error("enterFarm 调用失败: " + JSON.stringify(farm));
    }
    const farmVO = farm.farmVO;
    if (!farmVO || !farmVO.subFarmVO || !farmVO.subFarmVO.farmId) {
        throw new Error("enterFarm 未返回 farmVO.subFarmVO");
    }
    const ownerFarmId = farmVO.subFarmVO.farmId;

    // 2. 亲密家庭信息
    const fam = rpc("com.alipay.antfarm.enterFamily", [{
        fromAnn: false,
        requestType: "NORMAL",
        sceneCode: "ANTFARM",
        source: "H5"
    }]);
    if (!fam.groupId) {
        throw new Error("enterFamily 未返回 groupId");
    }
    const groupId = fam.groupId;

    // 2.1 当天该餐段是否已请过客
    const interactActions = fam.familyInteractActions || [];
    for (let i = 0; i < interactActions.length; i++) {
        if (interactActions[i].familyInteractType === "EatTogether") {
            toast("今天已请过客，跳过");
            return;
        }
    }
    const period = getEatPeriod();
    if (!period) {
        toast("当前不在请客时段（早/午/晚餐 06:00-20:00），跳过");
        return;
    }

    // 2.2 家庭成员列表（xrz 方式：getFamilyMemberList(true,true) = 含非好友、含自己）
    const friendUserIds = getFamilyMemberList(true, true);
    if (friendUserIds.length === 0) {
        toast("家庭成员为空，跳过");
        return;
    }

    // 3. 查询自己厨房美食（xrz 参数：operTag=SYNC_RESUME / operType=QUERY_ALL，无 userId/version）
    const sync = rpc("com.alipay.antfarm.syncAnimalStatus", [{
        farmId: ownerFarmId,
        operTag: "SYNC_RESUME",
        operType: "QUERY_ALL",
        requestType: "NORMAL",
        sceneCode: "ANTFARM",
        source: "H5"
    }]);
    const cuisineList = sync.cuisineList || [];
    if (cuisineList.length === 0) {
        toast("厨房暂无美食，跳过");
        return;
    }

    // 4. 排序凑齐 N 份（N = 家庭成员数，含自己）
    const needCount = friendUserIds.length;
    const cuisines = pickCuisines(cuisineList, needCount);
    if (!cuisines) {
        toast("美食不足 " + needCount + " 份，跳过");
        return;
    }

    // 5. 请客
    const res = rpc("com.alipay.antfarm.familyEatTogether", [{
        cuisines: cuisines,
        friendUserIds: friendUserIds,
        groupId: groupId,
        requestType: "NORMAL",
        sceneCode: "ANTFARM",
        source: "H5",
        spaceType: "ChickFamily"
    }]);
    if (res.success === true || res.memo === "SUCCESS") {
        console.log("请客成功: " + period + "，消耗 " + needCount + " 份美食，group=" + groupId);
        // 6. 同步家庭交互动作与亲密度（xrz 关键步骤，清除 EatTogether 标记以支持再次请客）
        rpc("com.alipay.antfarm.syncFamilyStatus", [{
            groupId: groupId,
            operType: "FAMILY_INTERACT_ACTION",
            requestType: "NORMAL",
            sceneCode: "ANTFARM",
            source: "H5",
            syncUserIds: [currentUid]
        }]);
        rpc("com.alipay.antfarm.syncFamilyStatus", [{
            groupId: groupId,
            operType: "INTIMACY_VALUE",
            requestType: "NORMAL",
            sceneCode: "ANTFARM",
            source: "H5",
            syncUserIds: [currentUid]
        }]);
        toast(period + "请客成功，消耗 " + needCount + " 份美食");
    } else {
        toast("请客失败");
        console.log("请客失败: " + JSON.stringify(res));
    }
}

try {
    main();
} catch (e) {
    console.log("执行出错: " + e.message);
    toast("执行出错: " + e.message);
}
