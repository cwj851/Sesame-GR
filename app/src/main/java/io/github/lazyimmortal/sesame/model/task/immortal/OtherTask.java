package io.github.lazyimmortal.sesame.model.task.immortal;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.modelFieldExt.*;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.data.RuntimeInfo;
import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.model.base.TaskCommon;
import io.github.lazyimmortal.sesame.model.task.antForest.EcoLifeRpcCall;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.Status;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.RandomUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Constanline
 * @since 2023/08/22
 */
public class OtherTask {
    private static final String TAG = OtherTask.class.getSimpleName();

    private static Integer executeIntervalInt=2000;


    private static String[] vvContenList =
        {"20240418OB020010036181761252", "20240410OB020010038477398256", "20240218OB020010035137157446",
            "20240216OB020010035135584439", "20240212OB020010035533110310", "20240306OB020010033550474098",
            "20240318OB020010036859725218", "20240322OB020010030562755667", "20240328OB020010039267747993",
            "20240320OB020010033561088455", "20240328OB020010033767796483", "20240320OB020010034961221630",
            "20240326OB020010032165688207", "20240419OB020010035282645979", "20240323OB020010031863404314"};
    private static Map<String, String> mapContent = new HashMap<String, String>() {
        {
            put("20240530OB020010036507620025",
                "{\"chInfo\":\"ch_life__chsub_Ndiscovery.featured__chssub_tabbartips_youleshiyan1\",\"contentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240530OB020010036507620025\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS1@I_BR1@I_XC1@B_CI2@C_LL1@CNTTP4@C_FT2@C_FT4@C_PT1@A_WT286@A_WT30@A_QS2@M_LS1@C_MG5@C_ML2@A_ZQ1@A_ZQ4@A_ZQ9@A_ZQ8@S_AS1@A_CQ33@F_CV1@C_AS2@A_EH2@A_DY247@A_DY262@A_DY263@A_DY285@H_DR1@H_VA5@V_PP1@A_TQ1@A_MLA9@C_TN2@A_PCH2@A_PCH5@A_PCH8@A_IP2@T_NE1@R_CQ1@R_CQ11@R_CQ2@A_SE575@A_LG1@A_VQ82@A_VQ14@A_VQ17@V_SR2@A_LZ000086\",\"allCateIds\":\"A_WT30@A_WT286\",\"authorId\":\"2030093816402651\",\"categoryIdL1\":\"A_WT30\",\"contentId\":\"20240530OB020010036507620025\",\"contentType\":\"video\",\"cttUdf\":\"\",\"distributionType\":\"recommend\",\"ext\":{\"FirstTabType\":\"discovery\",\"SecondTabType\":\"discovery.featured\",\"_act_src\":\"zrtj\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"0\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured__chssub_tabbartips_youleshiyan1\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured__chssub_tabbartips_youleshiyan1\",\"env\":\"PROD\",\"infoSecResult\":\"null\",\"ip\":\"221.239.27.130\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"10\\\",\\\"city\\\":\\\"天津市\\\",\\\"cityCode\\\":\\\"120000\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"county\\\":\\\"滨海新区\\\",\\\"countyCode\\\":\\\"120116\\\",\\\"ip\\\":\\\"221.239.27.130\\\",\\\"province\\\":\\\"天津市\\\",\\\"provinceCode\\\":\\\"120000\\\"}\",\"isCache\":\"false\",\"liveTab\":\"false\",\"mcnPid\":\"2088641538452710\",\"needDoubleWriteOldContent\":\"true\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"247\",\"pageType\":\"index\",\"refer\":\"discovery\",\"spmExt\":\"{\\\"_act_src\\\":\\\"zrtj\\\",\\\"_ad_sys\\\":\\\"contentlib\\\",\\\"_item_id\\\":\\\"20240530OB020010036507620025\\\",\\\"_item_src\\\":\\\"contentId\\\",\\\"distributionType\\\":\\\"recommend\\\"}\",\"subTabType\":\"discovery.featured\",\"tabType\":\"discovery\",\"trafficSource\":\"Life\",\"viewToken\":\"a618435ad3bc92276c22fd0ac61b66a3\"},\"firstScreen\":\"true\",\"flowExpFlag\":\"base\",\"flowFlag\":\"20TOTALITEM000\",\"high_painting_style\":\"2\",\"isCollect\":\"false\",\"isLike\":\"false\",\"newRequestType\":\"first_screen\",\"oraScmRecmixer\":\"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives0_5_tt_20240624@tab3_mix_goods_add_358587_10@tab3_immersion_mix_lives_pa_7\",\"pageIndex\":\"1\",\"requestType\":\"refresh\",\"scm\":\"a1003.b133.video.20240530OB020010036507620025.2187ff8a17211891826741078e2488.34301.29400233.29400233.232319590+232319587.22701.-.01.immr\",\"subType\":\"3\",\"userTag\":\"high_activity_user\"},\"nextContentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240528OB020010036506284290\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS1@A_BZ11@A_CQ33@A_DY113@A_DY195@A_DY86@A_FD228@A_IP2@A_KW49427@A_MLA9@A_PCH10@A_PCH3@A_PCH5@A_QS2@A_TQ1@A_VQ14@A_VQ17@A_VQ82@A_WT268@A_WT37@A_ZQ1@A_ZQ4@B_CI2@CNTTP4@C_AS2@C_FT2@C_FT4@C_MG5@C_ML2@C_PT1@C_TN2@F_CV2@H_DR1@I_BR1@I_XC1@M_LS2@S_AS1@T_NE1@V_PP1@V_SR1@H_VA3@A_EH2@A_ZQ8@A_ZQ9@G_FT1@R_CQ1@R_CQ10@R_CQ11@R_CQ5@R_CQ9@A_CF148@A_CF84@A_CF86@A_CF81@A_CF14@A_CF79@A_CF140@A_CF47@A_CF43@A_CF151@A_CF67@A_CF29@A_CF9@A_CF77@A_CF21@A_CF23\",\"allCateIds\":\"A_WT37@A_WT268\",\"authorId\":\"2030093591398654\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"1\",\"categoryIdL1\":\"A_WT37\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured__chssub_tabbartips_youleshiyan1\",\"contentId\":\"20240528OB020010036506284290\",\"contentType\":\"video\",\"cttUdf\":\"\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured__chssub_tabbartips_youleshiyan1\",\"distributionType\":\"recommend\",\"env\":\"PROD\",\"flowExpFlag\":\"exp\",\"flowFlag\":\"20TOTALITEM000_highbase@Default_highbase\",\"high_painting_style\":\"2\",\"infoSecResult\":\"null\",\"ip\":\"240e:3bb:a9b:2fd0:7a60:5bff:fef6:b512\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"80\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"ip\\\":\\\"240e:3bb:a9b:2fd0:7a60:5bff:fef6:b512\\\",\\\"province\\\":\\\"广东省\\\",\\\"provinceCode\\\":\\\"440000\\\"}\",\"mcnPid\":\"2088641696225654\",\"needDoubleWriteOldContent\":\"true\",\"newRequestType\":\"next_screen\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"247\",\"oraScmRecmixer\":\"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives0_5_tt_20240624@tab3_mix_goods_add_358587_10@tab3_immersion_mix_lives_pa_7\",\"pageIndex\":\"2\",\"pageType\":\"index\",\"publicId\":\"2030093591398654\",\"publisher\":\"{\\\"scenesCode\\\":\\\"S20230725215847615\\\",\\\"did\\\":\\\"0b22835317168828204812680e167d\\\"}\",\"realPoi\":\"{\\\"areaCode\\\":\\\"440100\\\",\\\"latitude\\\":\\\"23.112491\\\",\\\"longitude\\\":\\\"113.402562\\\"}\",\"refer\":\"discovery\",\"requestType\":\"next_page\",\"scm\":\"a1003.b133.video.20240528OB020010036506284290.2187ff8a17211891835731237e2488.34301.29400213.29400213.232319590+232319587+237820205+237818915+236719111+237619929+237418365+236719842+233818769+237219998+237818449+237219202+237418070+237318016+237618987+236819154+237018567+236818115.5101.-.01.immr\",\"source_type\":\"public\",\"subTabType\":\"discovery.featured\",\"subType\":\"3\",\"tabType\":\"discovery\",\"userTag\":\"high_activity_user\",\"viewToken\":\"6b3cb3a6fbbe8bd30ecff33d718e625e\"}}");
            put("20240703OB020010037529994802",
                "{\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"contentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240703OB020010037529994802\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS1@A_BZ11@A_CQ35@A_DY414@A_DY426@A_DY508@A_FD572@A_IP2@A_LG1@A_LZ000086@A_PCH10@A_PCH4@A_PCH5@A_QS2@A_TQ1@A_VQ14@A_VQ82@A_WT1@A_WT94@A_ZQ1@A_ZQ4@A_ZQ5@A_ZQ8@A_ZQ9@B_CI2@C_AS2@C_FT2@C_FT4@C_MG5@C_ML2@C_PT12@C_TN1@F_CV2@G_FT1@H_DR1@I_BR1@I_XC1@M_LS1@S_AS1@T_NE1@V_PP1@V_SR1@A_EH2@H_PG4@H_CR1@R_CQ11@C_MH1@H_HC1@V_CL2@A_CF148@A_CF84@A_CF86@A_CF81@A_CF14@A_CF79@A_CF140@A_CF46@A_CF39@A_CF35@A_CF43@A_CF152@A_CF69@A_CF29@A_CF9@A_CF77@A_CF22@A_CF24\",\"allCateIds\":\"A_WT1@A_WT94\",\"authorId\":\"2030031660555759\",\"categoryIdL1\":\"A_WT1\",\"contentId\":\"20240703OB020010037529994802\",\"contentType\":\"video\",\"cttUdf\":\"\",\"distributionType\":\"recommend\",\"ext\":{\"FirstTabType\":\"discovery\",\"SecondTabType\":\"discovery.featured\",\"_act_src\":\"zrtj\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"0\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"env\":\"PROD\",\"infoSecResult\":\"null\",\"ip\":\"113.82.150.203\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"80\\\",\\\"city\\\":\\\"汕尾市\\\",\\\"cityCode\\\":\\\"441500\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"county\\\":\\\"海丰县\\\",\\\"countyCode\\\":\\\"441521\\\",\\\"ip\\\":\\\"113.82.150.203\\\",\\\"province\\\":\\\"广东省\\\",\\\"provinceCode\\\":\\\"440000\\\"}\",\"isCache\":\"false\",\"liveTab\":\"false\",\"mcnPid\":\"2088731705064412\",\"needDoubleWriteOldContent\":\"true\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"248\",\"pageType\":\"index\",\"refer\":\"discovery\",\"spmExt\":\"{\\\"_act_src\\\":\\\"zrtj\\\",\\\"_ad_sys\\\":\\\"contentlib\\\",\\\"_item_id\\\":\\\"20240703OB020010037529994802\\\",\\\"_item_src\\\":\\\"contentId\\\",\\\"distributionType\\\":\\\"recommend\\\"}\",\"subTabType\":\"discovery.featured\",\"tabType\":\"discovery\",\"trafficSource\":\"Life\",\"viewToken\":\"05856222fee835970337b856c1bb506b\"},\"flowExpFlag\":\"exp\",\"flowFlag\":\"20TOTALITEM000_highbase@V_highbase\",\"high_painting_style\":\"0\",\"isCollect\":\"false\",\"isLike\":\"false\",\"newRequestType\":\"next_screen\",\"oraScmRecmixer\":\"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives0_5_tt_20240624@tab3_mix_goods_add_358587_10@tab3_immersion_mix_lives_pa_7\",\"pageIndex\":\"2\",\"requestType\":\"next_page\",\"scm\":\"a1003.b133.video.20240703OB020010037529994802.218fd3a317212092012713900eb17b.34301.29400380.29400380.232319590+232319587+237820205+237818915+237619929+237418365+236719842+233818769+237219998+237818449+237418070+237318016+237618987+236819154+237018567+236818115.5004.-.01.immr\",\"subType\":\"3\",\"userTag\":\"high_activity_user\"},\"nextContentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"A202407128228833800001199\",\"_item_src\":\"live\",\"alitaxScm\":\"discovery#discovery*featured##live.A202407128228833800001199.2024071722000927756300.218fd3a317212092012713900eb17b.;pa_tab3_live_immersive_instance_v60|tab3_live|-|diyType_KA100_S+_AuthorId@2017122701256110#diyTypeKA100Tag@2017122701256110#diyTypeRatioKA100Tag@KA100|1*0.237618266+235220946+235220998+235221002+235819660+233120676+230222530+236918237+236718388+236819763+235819317+237819481+235718115+234821011.tradelive.N\",\"anchorId\":\"2438106136\",\"authorId\":\"2017122701256110\",\"btnImg\":\"https://mdn.alipayobjects.com/huamei_cc0gae/afts/img/A*8CXlT4Z-NuYAAAAAAAAAAAAADhPOAQ/original\",\"btnLottie\":\"https://mdn.alipayobjects.com/portal_xefchc/afts/file/A*IxwQT7LvLQwAAAAAAAAAAAAAAQAAAQ\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"cityCode\":\"\",\"contentId\":\"A202407128228833800001199\",\"contentType\":\"live\",\"cttUdf\":\"\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"distributionType\":\"recommend\",\"liveRoomType\":\"LIFE\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"248\",\"oraScm\":\"pa_tab3_live_immersive_instance_v60|tab3_live|-|diyType_KA100_S+_AuthorId@2017122701256110#diyTypeKA100Tag@2017122701256110#diyTypeRatioKA100Tag@KA100|1.0\",\"oraScmRecmixer\":\"tab3_immersion_mix_lives_pa_7|tab3_immersion_mix|-|contentType@live|1.0|tab3_immersion_mix_content_lives0_5_tt_20240624@tab3_mix_goods_add_358587_10@tab3_immersion_mix_lives_pa_7\",\"pageType\":\"index\",\"publicId\":\"2017122701256110\",\"refer\":\"discovery\",\"scm\":\"a1003.b133.live.A202407128228833800001199.218fd3a317212092012713900eb17b.33365.29300518.29300518.237618266+235220946+235220998+235221002+235819660+233120676+230222530+236918237+236718388+236819763+235819317+237819481+235718115+234821011.801.-.01_home\",\"source_type\":\"public\",\"subTabType\":\"discovery.featured\",\"tabType\":\"discovery\",\"viewToken\":\"7e5dfbc7f319a0ac12d5512ca283994e\"}}");
            put("20240620OB020010036121235926",
                "{\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"contentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240620OB020010036121235926\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS1@I_BR1@I_XC1@B_CI1@C_LL1@CNTTP4@C_FT2@C_FT4@C_PT13@A_WT268@A_WT37@A_QS2@M_LS1@C_MG5@C_ML2@A_ZQ1@A_ZQ4@A_ZQ5@A_ZQ8@S_AS1@A_CQ33@F_CV2@C_AS2@A_EH2@A_DY113@A_DY195@A_DY86@G_FT1@H_DR1@V_PP1@A_KW58220@A_TQ1@A_MLA9@C_TN2@A_LZ000086@A_PCH10@A_PCH3@A_PCH6@A_IP2@T_NE1@A_LG1@A_STY7@A_BZ11@A_VQ82@A_VQ14@A_VQ17@V_SR1@R_CQ11\",\"allCateIds\":\"A_WT37@A_WT268\",\"authorId\":\"2030094207619610\",\"categoryIdL1\":\"A_WT37\",\"contentId\":\"20240620OB020010036121235926\",\"contentType\":\"video\",\"cttUdf\":\"\",\"distributionType\":\"recommend\",\"ext\":{\"FirstTabType\":\"discovery\",\"SecondTabType\":\"discovery.featured\",\"_act_src\":\"zrtj\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"0\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"env\":\"PROD\",\"infoSecResult\":\"null\",\"ip\":\"1.192.63.182\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"40\\\",\\\"city\\\":\\\"郑州市\\\",\\\"cityCode\\\":\\\"410100\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"county\\\":\\\"金水区\\\",\\\"countyCode\\\":\\\"410105\\\",\\\"ip\\\":\\\"1.192.63.182\\\",\\\"province\\\":\\\"河南省\\\",\\\"provinceCode\\\":\\\"410000\\\"}\",\"isCache\":\"false\",\"liveTab\":\"false\",\"mcnPid\":\"2088641405153722\",\"needDoubleWriteOldContent\":\"true\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"248\",\"pageType\":\"index\",\"refer\":\"discovery\",\"spmExt\":\"{\\\"_act_src\\\":\\\"zrtj\\\",\\\"_ad_sys\\\":\\\"contentlib\\\",\\\"_item_id\\\":\\\"20240620OB020010036121235926\\\",\\\"_item_src\\\":\\\"contentId\\\",\\\"distributionType\\\":\\\"recommend\\\"}\",\"subTabType\":\"discovery.featured\",\"tabType\":\"discovery\",\"trafficSource\":\"Life\",\"viewToken\":\"eb08776afc73a7e83f966ef5f1dfe3d9\"},\"flowExpFlag\":\"exp\",\"flowFlag\":\"20TOTALITEM000_highbase@Default_highbase\",\"high_painting_style\":\"2\",\"isCollect\":\"false\",\"isLike\":\"false\",\"newRequestType\":\"next_screen\",\"oraScmRecmixer\":\"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives0_5_tt_20240624@tab3_mix_goods_add_358587_10@tab3_immersion_mix_lives_pa_7\",\"pageIndex\":\"3\",\"requestType\":\"next_page\",\"scm\":\"a1003.b133.video.20240620OB020010036121235926.218fd3a317212094313365451eb17b.34301.29400380.29400380.232319590+232319587+237820205+237818915+237619929+237418365+236719842+233818769+237219998+237818449+237418070+237318016+237618987+236819154+237018567+236818115.5104.-.01.immr\",\"subType\":\"3\",\"userTag\":\"high_activity_user\"},\"nextContentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240528OB020010032606183139\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS1@A_CQ35@A_DY113@A_DY389@A_DY86@A_IP2@A_KW59078@A_KW69747@A_KW79591@A_MR11@A_MR16@A_MR17@A_MR3@A_PCH10@A_PCH3@A_PCH6@A_QS2@A_TQ1@A_VQ14@A_VQ17@A_VQ82@A_WT26@A_WT312@A_ZQ1@A_ZQ4@B_CI2@CNTTP4@C_AS2@C_FT2@C_FT4@C_LL1@C_MG5@C_ML2@C_PT1@C_TN2@F_CV2@H_DR1@I_BR1@I_XC1@M_LS1@S_AS1@T_NE1@V_PP1@V_SR1@H_VA3@A_EH2@A_ZQ8@A_ZQ9@R_CQ1@R_CQ5@V_CL2@A_CF148@A_CF83@A_CF87@A_CF81@A_CF14@A_CF80@A_CF141@A_CF46@A_CF43@A_CF152@A_CF72@A_CF29@A_CF9@A_CF76@A_CF22@A_CF24\",\"allCateIds\":\"A_WT26@A_WT312\",\"authorId\":\"2030093789153266\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"0\",\"categoryIdL1\":\"A_WT26\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"contentId\":\"20240528OB020010032606183139\",\"contentType\":\"video\",\"cttUdf\":\"\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"distributionType\":\"recommend\",\"env\":\"PROD\",\"flowExpFlag\":\"exp\",\"flowFlag\":\"20TOTALITEM000_highbase@Default_highbase\",\"high_painting_style\":\"2\",\"infoSecResult\":\"null\",\"ip\":\"2408:8215:2141:7910:484a:7bdc:3aec:3427\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"30\\\",\\\"city\\\":\\\"枣庄市\\\",\\\"cityCode\\\":\\\"370400\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"county\\\":\\\"滕州市\\\",\\\"countyCode\\\":\\\"370481\\\",\\\"ip\\\":\\\"2408:8215:2141:7910:484a:7bdc:3aec:3427\\\",\\\"province\\\":\\\"山东省\\\",\\\"provinceCode\\\":\\\"370000\\\"}\",\"mcnPid\":\"2088541830427262\",\"needDoubleWriteOldContent\":\"true\",\"newRequestType\":\"next_screen\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"248\",\"oraScmRecmixer\":\"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives0_5_tt_20240624@tab3_mix_goods_add_358587_10@tab3_immersion_mix_lives_pa_7\",\"pageIndex\":\"3\",\"pageType\":\"index\",\"publicId\":\"2030093789153266\",\"refer\":\"discovery\",\"requestType\":\"next_page\",\"scm\":\"a1003.b133.video.20240528OB020010032606183139.218fd3a317212094313365451eb17b.34301.29400380.29400380.232319590+232319587+237820205+237818915+237619929+237418365+236719842+233818769+237219998+237818449+237418070+237318016+237618987+236819154+237018567+236818115.5105.-.01.immr\",\"source_type\":\"public\",\"subTabType\":\"discovery.featured\",\"subType\":\"3\",\"tabType\":\"discovery\",\"userTag\":\"high_activity_user\",\"viewToken\":\"b63c7f871c0e6d1c8e61ce4c55cecfa0\"}}");
            put("20240717OB020010034740808428",
                "{\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"contentInfo\":{\"_act_src\":\"zrtj_cold\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240717OB020010034740808428\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS1@A_CQ35@A_DY272@A_DY294@A_DY346@A_DY353@A_IP2@A_KW47543@A_KW83994@A_LG1@A_QS3@A_SE170@A_TIME15@A_TQ1@A_VQ15@A_VQ82@A_WT131@A_WT21@A_ZQ1@A_ZQ4@A_ZQ5@A_ZQ8@A_ZQ9@B_CI2@C_AS2@C_MG4@C_ML2@C_TN2@F_CV2@G_FT1@H_DR1@I_BR1@I_XC1@M_LS1@S_AS1@T_NE1@V_IM2@V_PP1@V_SM3@V_SR1@A_LZ000086\",\"alitaxScm\":\"discovery#discovery*featured##content.20240717OB020010034740808428.2024071722000227772503.218fd3a317212092012713900eb17b.;null\",\"authorId\":\"2030094576591472\",\"cityCode\":\"\",\"contentId\":\"20240717OB020010034740808428\",\"contentType\":\"video\",\"cttUdf\":\"\",\"distributionType\":\"recommend\",\"ext\":{\"FirstTabType\":\"discovery\",\"SecondTabType\":\"discovery.featured\",\"_act_src\":\"zrtj_cold\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"1\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"env\":\"PROD\",\"infoSecResult\":\"null\",\"ip\":\"223.240.146.108\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"30\\\",\\\"city\\\":\\\"合肥市\\\",\\\"cityCode\\\":\\\"340100\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"ip\\\":\\\"223.240.146.108\\\",\\\"province\\\":\\\"安徽省\\\",\\\"provinceCode\\\":\\\"340000\\\"}\",\"isCache\":\"false\",\"liveTab\":\"false\",\"needDoubleWriteOldContent\":\"true\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"248\",\"pageType\":\"index\",\"refer\":\"discovery\",\"spmExt\":\"{\\\"_act_src\\\":\\\"zrtj_cold\\\",\\\"_ad_sys\\\":\\\"contentlib\\\",\\\"_item_id\\\":\\\"20240717OB020010034740808428\\\",\\\"_item_src\\\":\\\"contentId\\\",\\\"distributionType\\\":\\\"recommend\\\"}\",\"subTabType\":\"discovery.featured\",\"tabType\":\"discovery\",\"trafficSource\":\"Life\",\"viewToken\":\"da1a2c63db27447162d75f28d9e8fb96\"},\"flowFlag\":\"N\",\"isCollect\":\"false\",\"isLike\":\"false\",\"newRequestType\":\"next_screen\",\"operCategoryIds\":\"A_WT131\",\"oraScmRecmixer\":\"tab3_mix_goods_add_358587_10|tab3_immersion_mix|-|contentType@goods|1.0|tab3_immersion_mix_content_lives0_5_tt_20240624@tab3_mix_goods_add_358587_10@tab3_immersion_mix_lives_pa_7\",\"rec_data\":\"{\\\"ecologyFlag\\\":\\\"N\\\"}\",\"scm\":\"a1003.b133.video.20240717OB020010034740808428.218fd3a317212092012713900eb17b.arec2_tab3_essence_goods.28500423.28500423.232319593+237019365+232319587+230020333+236418081+237619667+236818222.121203.-.01.immr\",\"subType\":\"3\"},\"nextContentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240716OB020010031039785812\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS1@A_CQ35@A_DY445@A_DY523@A_DY599@A_IP2@A_KW309@A_KW58306@A_KW77315@A_LG1@A_LZ000086@A_PCH10@A_PCH3@A_PCH5@A_QS2@A_TQ1@A_VQ14@A_VQ17@A_VQ82@A_WT35@A_WT39@A_ZQ1@A_ZQ10@A_ZQ4@A_ZQ5@A_ZQ8@B_CI1@CNTTP4@C_AS2@C_MG5@C_ML2@C_TN2@F_CV2@G_FT1@H_DR1@H_PG8@I_BR1@I_XC1@M_LS2@S_AS1@T_NE1@V_PP1@V_SR1@Z_EX2@C_PT16@A_CF82@A_CF17@A_CF79@A_CF140@A_CF46@A_CF35@A_CF43@A_CF152@A_CF69@A_CF29@A_CF10@A_CF77@A_CF19@A_CF24\",\"allCateIds\":\"A_WT35@A_WT39\",\"authorId\":\"2030094465419104\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"0\",\"categoryIdL1\":\"A_WT35\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"contentId\":\"20240716OB020010031039785812\",\"contentType\":\"video\",\"cttUdf\":\"\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"distributionType\":\"recommend\",\"env\":\"PROD\",\"flowExpFlag\":\"exp\",\"flowFlag\":\"20TOTALITEM000_highbase@M_highbase\",\"high_painting_style\":\"0\",\"infoSecResult\":\"null\",\"mcnPid\":\"2088841074974932\",\"needDoubleWriteOldContent\":\"true\",\"newRequestType\":\"next_screen\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"248\",\"oraScmRecmixer\":\"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives0_5_tt_20240624@tab3_mix_goods_add_358587_10@tab3_immersion_mix_lives_pa_7\",\"pageIndex\":\"3\",\"pageType\":\"index\",\"publicId\":\"2030094465419104\",\"refer\":\"discovery\",\"requestType\":\"next_page\",\"scm\":\"a1003.b133.video.20240716OB020010031039785812.218fd3a317212094313365451eb17b.34301.29400380.29400380.232319590+232319587+237820205+237818915+237619929+237418365+236719842+233818769+237219998+237818449+237418070+237318016+237618987+236819154+237018567+236818115.19002.-.01.immr\",\"source_type\":\"public\",\"subTabType\":\"discovery.featured\",\"subType\":\"3\",\"tabType\":\"discovery\",\"userTag\":\"high_activity_user\",\"viewToken\":\"52359b6175ea6884fc6cbaee8668e647\"}}");
            put("20240608OB020010037913278432",
                "{\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"contentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240608OB020010037913278432\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS1@A_CQ35@A_DY113@A_DY389@A_DY86@A_IP2@A_KW2266@A_KW7537@A_KW9950@A_LG1@A_LZ000086@A_MR11@A_MR16@A_MR17@A_MR3@A_PCH10@A_PCH2@A_PCH6@A_TQ1@A_US3@A_VQ14@A_VQ17@A_VQ82@A_WT26@A_WT312@A_ZQ1@A_ZQ4@A_ZQ5@B_CI2@CNTTP4@C_AS1@C_FT2@C_FT4@C_MG5@C_ML2@C_PT14@C_TN2@F_CV2@G_FT1@H_DR1@I_BR3@I_XC1@M_LS1@S_AS1@T_NE1@V_PP1@V_SR1@H_VA3@A_EH2@A_ZQ8@A_ZQ9@V_CL1\",\"allCateIds\":\"A_WT26@A_WT312\",\"authorId\":\"2030093693767798\",\"categoryIdL1\":\"A_WT26\",\"contentId\":\"20240608OB020010037913278432\",\"contentType\":\"video\",\"cttUdf\":\"\",\"distributionType\":\"recommend\",\"ext\":{\"FirstTabType\":\"discovery\",\"SecondTabType\":\"discovery.featured\",\"_act_src\":\"zrtj\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"0\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"env\":\"PROD\",\"infoSecResult\":\"null\",\"ip\":\"120.231.49.209\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"80\\\",\\\"city\\\":\\\"茂名市\\\",\\\"cityCode\\\":\\\"440900\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"county\\\":\\\"茂南区\\\",\\\"countyCode\\\":\\\"440902\\\",\\\"ip\\\":\\\"120.231.49.209\\\",\\\"province\\\":\\\"广东省\\\",\\\"provinceCode\\\":\\\"440000\\\"}\",\"isCache\":\"false\",\"liveTab\":\"false\",\"needDoubleWriteOldContent\":\"true\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"248\",\"pageType\":\"index\",\"refer\":\"discovery\",\"spmExt\":\"{\\\"_act_src\\\":\\\"zrtj\\\",\\\"_ad_sys\\\":\\\"contentlib\\\",\\\"_item_id\\\":\\\"20240608OB020010037913278432\\\",\\\"_item_src\\\":\\\"contentId\\\",\\\"distributionType\\\":\\\"recommend\\\"}\",\"subTabType\":\"discovery.featured\",\"tabType\":\"discovery\",\"trafficSource\":\"Life\",\"viewToken\":\"99b497f93e35b5d4f1a33d8c0ad00f87\"},\"flowExpFlag\":\"exp\",\"flowFlag\":\"20TOTALITEM000_highbase@Default_highbase\",\"high_painting_style\":\"2\",\"isCollect\":\"false\",\"isLike\":\"false\",\"newRequestType\":\"next_screen\",\"oraScmRecmixer\":\"-|tab3_immersion_mix|-|-|-|tab3_immersion_mix_content_lives0_5_tt_20240624@tab3_mix_goods_add_358587_10@tab3_immersion_mix_lives_pa_7\",\"pageIndex\":\"2\",\"requestType\":\"next_page\",\"scm\":\"a1003.b133.video.20240608OB020010037913278432.218fd3a317212092012713900eb17b.34301.29400380.29400380.232319590+232319587+237820205+237818915+237619929+237418365+236719842+233818769+237219998+237818449+237418070+237318016+237618987+236819154+237018567+236818115.22501.-.01.immr\",\"subType\":\"3\",\"userTag\":\"high_activity_user\"},\"nextContentInfo\":{\"_act_src\":\"zrtj_cold\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240717OB020010034740808428\",\"_item_src\":\"contentId\",\"algoTagIds\":\"A_AS1@A_CQ35@A_DY272@A_DY294@A_DY346@A_DY353@A_IP2@A_KW47543@A_KW83994@A_LG1@A_QS3@A_SE170@A_TIME15@A_TQ1@A_VQ15@A_VQ82@A_WT131@A_WT21@A_ZQ1@A_ZQ4@A_ZQ5@A_ZQ8@A_ZQ9@B_CI2@C_AS2@C_MG4@C_ML2@C_TN2@F_CV2@G_FT1@H_DR1@I_BR1@I_XC1@M_LS1@S_AS1@T_NE1@V_IM2@V_PP1@V_SM3@V_SR1@A_LZ000086\",\"alitaxScm\":\"discovery#discovery*featured##content.20240717OB020010034740808428.2024071722000227772503.218fd3a317212092012713900eb17b.;null\",\"authorId\":\"2030094576591472\",\"canDisplay\":\"0\",\"canDownload\":\"1\",\"canSelectReply\":\"0\",\"canSmartCover\":\"1\",\"chInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"cityCode\":\"\",\"contentId\":\"20240717OB020010034740808428\",\"contentType\":\"video\",\"cttUdf\":\"\",\"curChInfo\":\"ch_life__chsub_Ndiscovery.featured\",\"distributionType\":\"recommend\",\"env\":\"PROD\",\"flowFlag\":\"N\",\"infoSecResult\":\"null\",\"ip\":\"223.240.146.108\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"30\\\",\\\"city\\\":\\\"合肥市\\\",\\\"cityCode\\\":\\\"340100\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"ip\\\":\\\"223.240.146.108\\\",\\\"province\\\":\\\"安徽省\\\",\\\"provinceCode\\\":\\\"340000\\\"}\",\"needDoubleWriteOldContent\":\"true\",\"newRequestType\":\"next_screen\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"248\",\"operCategoryIds\":\"A_WT131\",\"oraScmRecmixer\":\"tab3_mix_goods_add_358587_10|tab3_immersion_mix|-|contentType@goods|1.0|tab3_immersion_mix_content_lives0_5_tt_20240624@tab3_mix_goods_add_358587_10@tab3_immersion_mix_lives_pa_7\",\"pageType\":\"index\",\"publicId\":\"2030094576591472\",\"rec_data\":\"{\\\"ecologyFlag\\\":\\\"N\\\"}\",\"refer\":\"discovery\",\"scm\":\"a1003.b133.video.20240717OB020010034740808428.218fd3a317212092012713900eb17b.arec2_tab3_essence_goods.28500423.28500423.232319593+237019365+232319587+230020333+236418081+237619667+236818222.121203.-.01.immr\",\"source_type\":\"public\",\"subTabType\":\"discovery.featured\",\"subType\":\"3\",\"tabType\":\"discovery\",\"viewToken\":\"da1a2c63db27447162d75f28d9e8fb96\"}}");
            put("20240323OB020010031863779029",
                "{\"chInfo\":\"chuncuduanju\",\"contentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240323OB020010031863779029\",\"_item_src\":\"contentId\",\"aggSceneId\":\"SE30148920\",\"aggSceneType\":\"scene\",\"algoTagIds\":\"A_AS1@A_BZ7@A_CQ35@A_MR11@A_MR16@A_MR17@A_MR3@A_QS4@A_SE852@A_TQ1@A_VQ15@A_VQ82@A_WT26@A_WT299@B_CI2@C_AS2@C_FT2@C_LL1@C_MG5@C_ML2@C_PT17@C_TN1@H_DR1@I_BR1@I_XC1@M_LS2@S_AS1@T_NE1@V_PP1@H_VA3@A_EH2@A_YH15@V_CL3@A_DY113@A_DY195@A_DY86@T_DJ16@T_DJ6@R_CQ10@R_CQ11@R_CQ2@R_CQ4@A_CF148@A_CF84@A_CF88@A_CF81@A_CF14@A_CF80@A_CF46@A_CF43@A_CF152@A_CF69@A_CF29@A_CF10@A_CF77@A_CF22@A_CF24\",\"authorId\":\"2030093976304180\",\"categoryIdL1\":\"A_WT26\",\"cityCode\":\"\",\"contentId\":\"20240323OB020010031863779029\",\"contentType\":\"video\",\"ext\":{\"_act_src\":\"zrtj\",\"canDisplay\":\"0\",\"canDownload\":\"0\",\"canSelectReply\":\"0\",\"canSmartCover\":\"0\",\"chInfo\":\"chuncuduanju\",\"curChInfo\":\"chuncuduanju__chsub_NDetail\",\"env\":\"PROD\",\"infoSecResult\":\"null\",\"ip\":\"112.10.202.124\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"30\\\",\\\"city\\\":\\\"杭州市\\\",\\\"cityCode\\\":\\\"330100\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"county\\\":\\\"西湖区\\\",\\\"countyCode\\\":\\\"330106\\\",\\\"ip\\\":\\\"112.10.202.124\\\",\\\"province\\\":\\\"浙江省\\\",\\\"provinceCode\\\":\\\"330000\\\"}\",\"needDoubleWriteOldContent\":\"true\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"248\",\"pageType\":\"detail\",\"refer\":\"scene\",\"reportProgress\":\"{\\\"actionList\\\":[\\\"stopPlay\\\"],\\\"contentId\\\":\\\"20240323OB020010031863779029\\\",\\\"groupId\\\":\\\"20240409CG051800002356\\\",\\\"type\\\":\\\"associationProgress\\\"}\",\"spmExt\":\"{\\\"_act_src\\\":\\\"zrtj\\\",\\\"_ad_sys\\\":\\\"contentlib\\\",\\\"_item_id\\\":\\\"20240323OB020010031863779029\\\",\\\"_item_src\\\":\\\"contentId\\\",\\\"aggSceneId\\\":\\\"SE30148920\\\",\\\"aggSceneType\\\":\\\"scene\\\"}\",\"trafficSource\":\"Life\",\"viewToken\":\"c7c765bf1c9677b99dce0d4198acaf9f\"},\"isCollect\":\"false\",\"isLike\":\"false\",\"newRequestType\":\"next_screen\",\"operCategoryIds\":\"A_WT299\",\"scm\":\"a1003.b133.video.20240323OB020010031863779029.2183002917213196435181475ed617.34301.26900313.26900313.-.9996.-.00.feed\",\"subType\":\"3\",\"topicId\":\"BC_TP_20230814000855040@BC_TP_20240306006288174@BC_TP_20240222006053535\"},\"nextContentInfo\":{\"_act_src\":\"zrtj\",\"_ad_sys\":\"contentlib\",\"_item_id\":\"20240329OB020010035168631214\",\"_item_src\":\"contentId\",\"aggSceneId\":\"SE30148920\",\"aggSceneType\":\"scene\",\"algoTagIds\":\"A_AS1@A_CQ35@A_IP2@A_KW14063@A_KW58308@A_KW69078@A_KW71919@A_KW84358@A_MR11@A_MR3@A_QS4@A_SE575@A_TQ1@A_VQ15@A_VQ17@A_VQ82@A_WT26@A_WT299@B_CI2@CNTTP4@C_AS2@C_FT2@C_MG5@C_ML2@C_PT17@C_TN2@H_DR1@I_BR1@I_XC1@M_LS1@S_AS1@T_NE1@V_PP1@H_VA3@A_EH2@A_YH16@V_CL3@A_DY113@A_DY195@A_DY86@A_ZQ8@G_FT1@P_DJ1@T_DJ1@T_DJ6@R_CQ10@R_CQ11@R_CQ4@A_CF148@A_CF83@A_CF87@A_CF81@A_CF17@A_CF80@A_CF46@A_CF43@A_CF152@A_CF65@A_CF29@A_CF9@A_CF77@A_CF21@A_CF24\",\"authorId\":\"2030093730730516\",\"canDisplay\":\"0\",\"canDownload\":\"0\",\"canSelectReply\":\"0\",\"canSmartCover\":\"0\",\"categoryIdL1\":\"A_WT26\",\"chInfo\":\"chuncuduanju\",\"cityCode\":\"\",\"contentId\":\"20240329OB020010035168631214\",\"contentType\":\"video\",\"curChInfo\":\"chuncuduanju__chsub_NDetail\",\"env\":\"PROD\",\"infoSecResult\":\"null\",\"ip\":\"222.175.71.218\",\"ipLocation\":\"{\\\"areaCode\\\":\\\"30\\\",\\\"city\\\":\\\"莱芜市\\\",\\\"cityCode\\\":\\\"371200\\\",\\\"country\\\":\\\"中国\\\",\\\"countryCode\\\":\\\"CN\\\",\\\"countryCode3\\\":\\\"CHN\\\",\\\"countryCodeNo\\\":\\\"156\\\",\\\"ip\\\":\\\"222.175.71.218\\\",\\\"province\\\":\\\"山东省\\\",\\\"provinceCode\\\":\\\"370000\\\"}\",\"mcnPid\":\"2088641917193511\",\"needDoubleWriteOldContent\":\"true\",\"newRequestType\":\"next_screen\",\"offerCardAtomicTMPLId\":\"LIFETAB_detail_content_offer_card_atomic\",\"offerCardAtomicTMPLVer\":\"248\",\"operCategoryIds\":\"A_WT299\",\"pageType\":\"detail\",\"publicId\":\"2030093730730516\",\"refer\":\"scene\",\"reportProgress\":\"{\\\"groupId\\\":\\\"20240409CG055106175215\\\",\\\"contentId\\\":\\\"20240329OB020010035168631214\\\",\\\"actionList\\\":[\\\"stopPlay\\\"],\\\"type\\\":\\\"associationProgress\\\"}\",\"scm\":\"a1003.b133.video.20240329OB020010035168631214.2183002917213192424457998ed617.34301.26900313.26900313.-.9991.-.00.feed\",\"source_type\":\"public\",\"subType\":\"3\",\"viewToken\":\"acd8e6e1aebb72b2298b8ea3cd7a1421\"}}");
        }
    };
    private static List<String> keys = new ArrayList<>(mapContent.keySet());


    /*     private void salaryday() {
        JSONObject jSONObject;
        int i;
        try {
            jSONObject = new JSONObject(OtherTaskRpcCall.homePageQuery());
        } finally {
            try {
            } finally {
            }
        }
        if (!jSONObject.getBoolean("success")) {
            Log.i(TAG, "playTimes.homePageQuery" + jSONObject.optString("resultDesc"));
        } else {
            JSONObject jSONObject2 = jSONObject.getJSONObject("result");
            if ("ACTIVE".equals(jSONObject2.getString("activityStatus"))
                && (i = jSONObject2.getInt("gameChanceCount")) != 0) {
                int i2 = jSONObject2.getInt("joinGameTimes");
                while (i > 0) {
                    int i3 = i2 + 1;
                    JSONObject jSONObject3 = new JSONObject(OtherTaskRpcCall.prizeTrigger(i3));
                    if (!jSONObject3.getBoolean("success")) {
                        Log.i(TAG, "playTimes.prizeTrigger" + jSONObject3.optString("resultDesc"));
                    } else {
                        Log.other(
                            "红包雨🎮获得体验金[" + JsonUtil.getValueByPath(jSONObject3, "result.lotteryPrize.amount") + "]");
                        i += -1;
                        TimeUtil.sleep(executeIntervalInt);
                        i2 = i3;
                    }
                }
            }
        }
    }
    
    private void salarydayTask() {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(OtherTaskRpcCall.homePageQuery());
        } finally {
            try {
            } finally {
            }
        }
        if (jSONObject.getBoolean("success")) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("result");
            if ("ACTIVE".equals(jSONObject2.getString("activityStatus"))) {
                JSONArray jSONArray = jSONObject2.getJSONArray("taskDetailList");
                for (int i = 0; i < jSONArray.length(); i++) {
                    JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                    String string = jSONObject3.getString("taskProcessStatus");
                    if (!"NONE_SIGNUP".equals(string) && !"SIGNUP_EXPIRED".equals(string)) {
                        String string2 = jSONObject3.getString("taskId");
                        String string3 = jSONObject3.getString("appletId");
                        JSONObject jSONObject4 =
                            new JSONObject(OtherTaskRpcCall.salarydayTaskComplete(string3, string2));
                        if (jSONObject4.getBoolean("success")) {
                            JSONObject jSONObject5 =
                                new JSONObject(OtherTaskRpcCall.queryTaskByTaskId(string3, string2));
                            if (jSONObject5.getBoolean("success")) {
                                Log.other("红包雨🎮完成任务["
                                    + JsonUtil.getValueByPath(jSONObject5, "result.taskDetailList.[0].taskName") + "]");
                                TimeUtil.sleep(executeIntervalInt);
                            } else {
                                Log.i(TAG, "salarydayTask.queryTaskByTaskId" + jSONObject5.optString("resultDesc"));
                            }
                        } else {
                            Log.i(TAG, "salarydayTask.salarydayTaskComplete" + jSONObject4.optString("resultDesc"));
                        }
                    }
                }
            }
        } else {
            Log.i(TAG, "salarydayTask.homePageQuery" + jSONObject.optString("resultDesc"));
        }
    } */

    /* 消费金 */

    public static void taskV2Index(String taskSceneCode) {
        boolean doubleCheck = false;
        try {
            String s = OtherTaskRpcCall.taskV2Index(taskSceneCode);
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                JSONArray taskList = jo.getJSONArray("taskList");
                for (int i = 0; i < taskList.length(); i++) {
                    jo = taskList.getJSONObject(i);
                    JSONObject extInfo = jo.getJSONObject("extInfo");
                    String taskStatus = extInfo.getString("taskStatus");
                    String title = extInfo.getString("title");
                    String taskId = extInfo.getString("actionBizId");
                    if ("TO_RECEIVE".equals(taskStatus)) {
                        taskV2TriggerReceive(taskId, title);
                    } else if ("NONE_SIGNUP".equals(taskStatus)) {
                        taskV2TriggerSignUp(taskId);
                        Thread.sleep(1000L);
                        taskV2TriggerSend(taskId);
                        doubleCheck = true;
                    } else if ("SIGNUP_COMPLETE".equals(taskStatus)) {
                        taskV2TriggerSend(taskId);
                        doubleCheck = true;
                    }
                }
                if (doubleCheck)
                    taskV2Index(taskSceneCode);
            } else {
                Log.record(jo.getString("resultDesc"));
                Log.i(s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "taskV2Index err:");
            Log.printStackTrace(t);
        }
    }

    private static void taskV2TriggerReceive(String taskId, String name) {
        try {
            String s = OtherTaskRpcCall.taskV2TriggerReceive(taskId);
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                int receiveAmount = jo.getInt("receiveAmount");
                Log.other("赚消费金💰[" + name + "]#" + receiveAmount);
            }
        } catch (Throwable t) {
            Log.i(TAG, "taskV2TriggerReceive err:");
            Log.printStackTrace(t);
        }
    }

    private static void taskV2TriggerSignUp(String taskId) {
        try {
            String s = OtherTaskRpcCall.taskV2TriggerSignUp(taskId);
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {

            }
        } catch (Throwable t) {
            Log.i(TAG, "taskV2TriggerSignUp err:");
            Log.printStackTrace(t);
        }
    }

    private static void taskV2TriggerSend(String taskId) {
        try {
            String s = OtherTaskRpcCall.taskV2TriggerSend(taskId);
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {

            }
        } catch (Throwable t) {
            Log.i(TAG, "taskV2TriggerSend err:");
            Log.printStackTrace(t);
        }
    }

    public static void consumeGoldIndex() {
        try {
            String s = OtherTaskRpcCall.consumeGoldIndex();
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                JSONObject homePromoInfoDTO = jo.getJSONObject("homePromoInfoDTO");
                JSONArray homePromoTokenDTOList = homePromoInfoDTO.getJSONArray("homePromoTokenDTOList");
                int tokenLeftAmount = 0;
                int tokenTotalAmount = 0;
                for (int i = 0; i < homePromoTokenDTOList.length(); i++) {
                    jo = homePromoTokenDTOList.getJSONObject(i);
                    String tokenType = jo.getString("tokenType");
                    if ("CONSUME_GOLD".equals(tokenType)) {
                        tokenLeftAmount = jo.getInt("tokenLeftAmount");
                    }
                }
                if (tokenLeftAmount > 0) {
                    for (int j = 0; j < tokenLeftAmount; j++) {
                        jo = new JSONObject(OtherTaskRpcCall.promoTrigger());
                        if (jo.optBoolean("success")) {
                            JSONObject homePromoPrizeInfoDTO = jo.getJSONObject("homePromoPrizeInfoDTO");
                            int quantity = homePromoPrizeInfoDTO.getInt("quantity");
                            Log.other("赚消费金💰[投5币抽]#" + quantity);
                            if (homePromoPrizeInfoDTO.has("promoAdvertisementInfo")) {
                                JSONObject promoAdvertisementInfo =
                                    homePromoPrizeInfoDTO.getJSONObject("promoAdvertisementInfo");
                                String outBizNo = promoAdvertisementInfo.getString("outBizNo");
                                jo = new JSONObject(OtherTaskRpcCall.advertisement(outBizNo));
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "queryTreasureBox err:");
            Log.printStackTrace(t);
        }
    }

    public static void signinCalendar() {
        try {
            String s = OtherTaskRpcCall.signinCalendar();
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                boolean signed = jo.getBoolean("isSignInToday");
                if (!signed) {
                    jo = new JSONObject(OtherTaskRpcCall.openBoxAward());
                    if (jo.optBoolean("success")) {
                        int amount = jo.getInt("amount");
                        Log.other("消费金签到💰[" + amount + "金币]");
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "signinCalendar err:");
            Log.printStackTrace(t);
        }
    }

    /**
     * 黄金票任务
     */
    private static void goldTicket() {
        try {
            String str = OtherTaskRpcCall.goldBillIndex();
            JSONObject jsonObject = new JSONObject(str);
            if (!jsonObject.getBoolean("success")) {
                Log.i(TAG + ".goldTicket.goldBillIndex", jsonObject.optString("resultDesc"));
                return;
            }
            jsonObject = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonObject.getJSONArray("cardModel");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String cardTypeId = object.getString("cardTypeId");
                if ("H5_GOLDBILL_ASSERT".equals(cardTypeId)) {
                    // 我的黄金票
                } else if ("H5_GOLDBILL_TASK".equals(cardTypeId)) {
                    // 任务列表，待完成的
                    // JSONArray jsonArray2 = (JSONArray)
                    // JsonUtil.getValueByPathObject(object,"dataModel.jsonResult.tasks.todo");
                    JSONArray jsonArray2 = object.getJSONObject("dataModel").getJSONObject("jsonResult")
                        .getJSONObject("tasks").optJSONArray("todo");
                    if (jsonArray2 == null) {
                        continue;
                    }
                    for (int j = 0; j < jsonArray2.length(); j++) {
                        JSONObject object2 = jsonArray2.getJSONObject(j);
                        String title = object2.getString("title");
                        if (title.contains("1元起") || title.contains("体验终身")) {
                            // 跳过这种傻逼玩意
                            continue;
                        }
                        str = OtherTaskRpcCall.goldBillTrigger(object2.getString("taskId"));
                        jsonObject = new JSONObject(str);
                        if (!jsonObject.getBoolean("success")) {
                            Log.i(TAG + ".goldTicket.goldBillTrigger", jsonObject.optString("resultDesc"));
                            continue;
                        }
                        Log.other("黄金票🏦[" + title + "]" + object2.getString("subTitle"));
                    }
                } else if ("H5_GOIDBILL_EQUITY".equals(cardTypeId)) {
                    // 兑换列表
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "goldTicket err:");
            Log.printStackTrace(th);
        } finally {
            try {
                Thread.sleep(executeIntervalInt);
            } catch (InterruptedException e) {
                Log.printStackTrace(e);
            }
        }
    }

    /**
     * 收取黄金票
     */
    private static void goldBillCollect(String signInfo) {
        try {
            String str = OtherTaskRpcCall.goldBillCollect(signInfo);
            JSONObject jsonObject = new JSONObject(str);
            if (!jsonObject.getBoolean("success")) {
                Log.i(TAG + ".goldBillCollect.goldBillCollect", jsonObject.optString("resultDesc"));
                return;
            }
            JSONObject object = jsonObject.getJSONObject("result");
            JSONArray jsonArray = object.getJSONArray("collectedList");
            int length = jsonArray.length();
            if (length == 0) {
                return;
            }
            for (int i = 0; i < length; i++) {
                Log.other("黄金票🙈[" + jsonArray.getString(i) + "]");
            }
            // Log.other("黄金票🏦本次总共获得[" + JsonUtil.getValueByPath(object,
            // "collectedCamp.amount") + "]");
            Log.other("黄金票🏦本次总共获得[" + object.getJSONObject("collectedCamp").optInt("amount", 0) + "]");
        } catch (Throwable th) {
            Log.i(TAG, "signIn err:");
            Log.printStackTrace(th);
        } finally {
            try {
                Thread.sleep(executeIntervalInt);
            } catch (InterruptedException e) {
                Log.printStackTrace(e);
            }
        }
    }

    /**
     * 车神卡领奖
     */
    public static void carGodCardbenefit() {
        try {
            while (true) {
                String str = OtherTaskRpcCall.v1benefitQuery();
                JSONObject jsonObject = new JSONObject(str);
                if (!jsonObject.getBoolean("success")) {
                    Log.i(TAG + ".carGodCardbenefit.v1benefitQuery", jsonObject.optString("resultDesc"));
                    return;
                }
                JSONObject object = jsonObject.getJSONObject("data");
                // JSONArray jsonArray = (JSONArray) JsonUtil.getValueByPathObject(object,
                // "result.aggregationOfferInfos");
                JSONArray jsonArray = object.getJSONObject("result").optJSONArray("aggregationOfferInfos");
                if (jsonArray == null || jsonArray.length() == 0) {
                    return;
                }
                jsonObject = new JSONObject();
                jsonObject.put("args", new JSONObject().put("offerRequest", jsonArray));
                str = OtherTaskRpcCall.v1benefitTrigger(jsonObject);
                jsonObject = new JSONObject(str);
                if (!jsonObject.getBoolean("success")) {
                    Log.i(TAG + ".carGodCardbenefit.v1benefitTrigger", jsonObject.optString("resultDesc"));
                    continue;
                }
                // jsonArray = (JSONArray) JsonUtil.getValueByPathObject(jsonObject,
                // "data.result");
                jsonArray = object.getJSONObject("data").optJSONArray("result");
                if (jsonArray == null) {
                    continue;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    str = "车神卡🏎获得[" + jsonObject.getString("name");
                    if (jsonObject.has("memo")) {
                        str += "-" + jsonObject.getString("memo");
                    }
                    str += "]" + jsonObject.getString("price") + jsonObject.getString("unit");
                    Log.other(str);
                }
                TimeUtil.sleep(executeIntervalInt);
            }
        } catch (Throwable th) {
            Log.i(TAG, "carGodCardbenefit err:");
            Log.printStackTrace(th);
        } finally {
            try {
                Thread.sleep(executeIntervalInt);
            } catch (InterruptedException e) {
                Log.printStackTrace(e);
            }
        }
    }

    /**
     * 实体红包
     */
    public static void promoprodTaskList() {
        try {
            String str = OtherTaskRpcCall.queryTaskList();
            JSONObject jsonObject = new JSONObject(str);
            if (!jsonObject.getBoolean("success")) {
                Log.i(TAG + ".queryTaskList", jsonObject.optString("resultDesc"));
                return;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("taskDetailList");
            int length = jsonArray.length();
            if (length == 0) {
                return;
            }
            for (int i = 0; i < length; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String status = object.getString("taskProcessStatus");
                String taskType = object.getString("taskType");
                if ("RECEIVE_SUCCESS".equals(status) || "TRANSFORMER".equals(taskType) || "GUIDE".equals(taskType)) {
                    continue;
                }
                if (!"SIGNUP_COMPLETE".equals(status)) {
                    /*
                     * str = OtherTaskRpcCall.signup(JsonUtil.getValueByPath(object,
                     * "taskParticipateExtInfo.gplusItem"), object.getString("taskId"));
                     */
                    JSONObject taskParticipateExtInfo = object.getJSONObject("taskParticipateExtInfo");
                    str = OtherTaskRpcCall.signup(taskParticipateExtInfo.optString("gplusItem"),
                        object.getString("taskId"));
                    jsonObject = new JSONObject(str);
                    if (!jsonObject.getBoolean("success")) {
                        Log.i(TAG + ".queryTaskList.signup", jsonObject.optString("errorMsg"));
                    }
                    TimeUtil.sleep(executeIntervalInt);
                }
                str = OtherTaskRpcCall.complete(object.getString("taskId"));
                jsonObject = new JSONObject(str);
                if (!jsonObject.getBoolean("success")) {
                    Log.i(TAG + ".queryTaskList.complete", jsonObject.optString("errorMsg"));
                    continue;
                }
                Log.other("实体红包🍷获取["
                    + jsonObject.getJSONObject("appletBaseConfigDTO").getString("appletName") + "]" + jsonObject
                        .getJSONArray("prizeSendInfo").getJSONObject(0).getJSONObject("price").optDouble("amount")
                    + "元");

                /*
                 * Log.other("实体红包🍷获取[" + JsonUtil.getValueByPath(jsonObject,
                 * "appletBaseConfigDTO.appletName") + "]" + JsonUtil.getValueByPath(jsonObject,
                 * "prizeSendInfo.price.amount") + "元");
                 */
                TimeUtil.sleep(executeIntervalInt);
            }
        } catch (Throwable th) {
            Log.i(TAG, "queryTaskList err:");
            Log.printStackTrace(th);
        } finally {
            try {
                Thread.sleep(executeIntervalInt);
            } catch (InterruptedException e) {
                Log.printStackTrace(e);
            }
        }
    }

    /* 摇一摇 */

    public static void moduleRecommend() {
        try {
            String s = OtherTaskRpcCall.moduleRecommend();
            JSONObject jo = new JSONObject(s);
            int countj = 0;
            if (jo.getBoolean("success")) {
                JSONArray modules = jo.getJSONObject("model").optJSONArray("modules");
                if (modules != null && modules.length() > 0) {
                    JSONObject content = modules.getJSONObject(0).getJSONObject("content");
                    JSONObject mainArea = content.getJSONObject("mainArea");
                    String initCampId = mainArea.optString("initCampId");
                    promokernelinit(initCampId);
                    String taskCenterIdKey = content.getJSONObject("taskArea").optString("taskCenterIdKey");
                    String lightTaskId = content.getJSONObject("lightFireArea").optString("lightTaskId");
                    taskListQuery(taskCenterIdKey);
                    taskListQuery2(lightTaskId);
                    String certificateTmplId = mainArea.optString("certificateTmplId");
                    String mainActiveId = mainArea.optString("mainActiveId");
                    jo = new JSONObject(OtherTaskRpcCall.certificateNum(certificateTmplId));
                    if (jo.getBoolean("success")) {
                        int availableNum = jo.optInt("availableNum", 0);
                        if (availableNum > 0) {
                            for (int i = 0; i < availableNum; i++) {
                                jo = new JSONObject(OtherTaskRpcCall.promokernelTrigger(mainActiveId));
                                if (jo.getBoolean("success")) {
                                    JSONArray prizeSendInfo = jo.optJSONArray("prizeSendInfo");
                                    if (prizeSendInfo != null && prizeSendInfo.length() > 0) {
                                        String activityId = prizeSendInfo.getJSONObject(0)
                                            .getJSONObject("prizeProperty").optString("activityId");
                                        if (!activityId.isEmpty()) {
                                            jo = new JSONObject(OtherTaskRpcCall.activityMatch(activityId));
                                            if (jo.getBoolean("success")) {
                                                JSONObject taskVO =
                                                    jo.getJSONObject("bcActivityVO").getJSONObject("taskVO");
                                                String taskState = taskVO.optString("taskState");
                                                JSONObject taskParams = taskVO.getJSONObject("taskParams");
                                                String taskToken = taskParams.optString("taskToken");
                                                if ("inComplete".equals(taskState)) {
                                                    String merchantPageUrl = taskParams.optString("url");
                                                    String merchantAppId =
                                                        StringUtil.getSubString(merchantPageUrl, "appId=", "&");
                                                    jo = new JSONObject(OtherTaskRpcCall.activityMatch2(activityId,
                                                        merchantAppId, merchantPageUrl, taskToken));
                                                    if (jo.getBoolean("success")) {
                                                        taskVO =
                                                            jo.getJSONObject("bcActivityVO").getJSONObject("taskVO");
                                                        taskState = taskVO.optString("taskState");
                                                        taskParams = taskVO.getJSONObject("taskParams");
                                                        taskToken = taskParams.optString("taskToken");
                                                    }
                                                }
                                                jo = new JSONObject(
                                                    OtherTaskRpcCall.activityComplete(activityId, taskToken));
                                                if (jo.getBoolean("success")) {
                                                    JSONArray assetVOs = jo.optJSONArray("assetVOs");
                                                    if (assetVOs != null && assetVOs.length() > 0) {
                                                        String showAmount =
                                                            assetVOs.getJSONObject(0).optString("showAmount");
                                                        Log.other("摇一摇红包🧧获取[" + showAmount + "元]");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Thread.sleep(1500);
                            }
                        }
                    }
                }
            } else {
                Log.record("moduleRecommend" + s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "moduleRecommend err:");
            Log.printStackTrace(t);
        }
    }

    private static void promokernelinit(String initCampId) {
        try {
            String s = OtherTaskRpcCall.promokernelTrigger(initCampId);
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                Log.other("摇一摇🧧[签到成功]");
            } else {
                Log.record("摇一摇🧧今日已签到！");
            }
        } catch (Throwable t) {
            Log.i(TAG, "promokernelinit err:");
            Log.printStackTrace(t);
        }
    }

    private static void taskListQuery(String taskCenInfo) {
        try {
            String s = OtherTaskRpcCall.taskListQuery(taskCenInfo);
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONArray taskDetailList = jo.optJSONArray("taskDetailList");
                if (taskDetailList != null && taskDetailList.length() > 0) {
                    for (int i = 0; i < taskDetailList.length(); i++) {
                        jo = taskDetailList.optJSONObject(i);
                        if (jo == null)
                            continue;
                        String sendCampTriggerType = jo.optString("sendCampTriggerType");
                        if (!"EVENT_TRIGGER".equals(sendCampTriggerType)) {
                            String taskId = jo.optString("taskId");
                            String taskProcessStatus = jo.optString("taskProcessStatus");
                            if ("NOT_DONE".equals(taskProcessStatus)) {
                                jo = new JSONObject(OtherTaskRpcCall.appletTrigger(taskId));
                                if (jo.getBoolean("success")) {
                                    Log.other("摇一摇🧧[次数+1]");
                                }
                            }
                        }
                        Thread.sleep(500);
                    }
                }
            } else {
                Log.record("taskListQuery" + s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "taskListQuery err:");
            Log.printStackTrace(t);
        }
    }

    private static void taskListQuery2(String taskIds) {
        try {
            String s = OtherTaskRpcCall.taskListQuery2(taskIds);
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                JSONArray taskDetailList = jo.optJSONArray("taskDetailList");
                if (taskDetailList != null && taskDetailList.length() > 0) {
                    for (int i = 0; i < taskDetailList.length(); i++) {
                        jo = taskDetailList.optJSONObject(i);
                        if (jo == null)
                            continue;
                        String sendCampTriggerType = jo.optString("sendCampTriggerType");
                        if (!"EVENT_TRIGGER".equals(sendCampTriggerType)) {
                            String taskId = jo.optString("taskId");
                            String taskProcessStatus = jo.optString("taskProcessStatus");
                            if ("NOT_DONE".equals(taskProcessStatus)) {
                                jo = new JSONObject(OtherTaskRpcCall.appletTrigger(taskId));
                                if (jo.getBoolean("success")) {
                                    Log.other("摇一摇🧧[次数+1]");
                                }
                            }
                        }
                        Thread.sleep(500);
                    }
                }
            } else {
                Log.record("taskListQuery2" + s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "taskListQuery2 err:");
            Log.printStackTrace(t);
        }
    }

    public static void healthChannelTask() {
        try {
            String s = OtherTaskRpcCall.independent_component_task_reward_query();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("isSuccess")) {
                JSONArray playTaskOrderInfoList = (JSONArray)JsonUtil.getValueByPathObject(jo,
                    "components.independent_component_task_reward_01190910_independent_component_task_reward_query.content.playTaskOrderInfoList");// jo.optJSONArray("taskDetailList");
                if (playTaskOrderInfoList != null && playTaskOrderInfoList.length() > 0) {
                    for (int i = 0; i < playTaskOrderInfoList.length(); i++) {
                        jo = playTaskOrderInfoList.optJSONObject(i);
                        if (jo == null)
                            continue;
                        String advanceType = jo.optString("advanceType");
                        if (!"userPush".equals(advanceType))
                            continue;
                        String taskStatus = jo.optString("taskStatus");
                        if ("claim".equals(taskStatus)) {
                            String code = jo.optString("code");
                            String recordNo = jo.optString("recordNo");
                            JSONObject displayInfo = jo.getJSONObject("displayInfo");
                            String activityName = displayInfo.optString("activityName");
                            String activitySubTitle = displayInfo.optString("activitySubTitle");
                            if (!code.isEmpty() && !recordNo.isEmpty()) {
                                jo = new JSONObject(
                                    OtherTaskRpcCall.independent_component_task_reward_process(code, recordNo));
                                if (jo.getBoolean("isSuccess")) {
                                    Log.other("健康医疗🚑[" + activityName + "]#" + activitySubTitle);
                                }
                            }
                        }
                        Thread.sleep(500);
                    }
                }
            } else {
                Log.record("independent_component_task_reward_query" + s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "healthChannelTask err:");
            Log.printStackTrace(t);
        }
    }

    public static void healthChannelSignInRecall() {
        try {
            String s = OtherTaskRpcCall.independent_component_sign_in_recall();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("isSuccess")) {
                JSONArray playSignInOrderInfoList = (JSONArray)JsonUtil.getValueByPathObject(jo,
                    "components.independent_component_sign_in_01190911_independent_component_sign_in_recall.content.playSignInOrderInfoList");
                if(playSignInOrderInfoList.length()<=0){
                    return;
                }
                jo = playSignInOrderInfoList.getJSONObject(0);
                JSONObject playSignInTemplateInfo = jo.getJSONObject("playSignInTemplateInfo");
                String code = playSignInTemplateInfo.getString("code");
                if (jo.has("playSignInCycleInstanceInfoList")) {
                    JSONArray playSignInCycleInstanceInfoList = jo.getJSONArray("playSignInCycleInstanceInfoList");
                    jo = playSignInCycleInstanceInfoList.getJSONObject(0);
                    String latestSignInDate = jo.optString("latestSignInDate");
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String today = sdf.format(date);
                    if (!today.equals(latestSignInDate)) {
                        healthChannelSignIn(code);
                    }
                } else {
                    healthChannelSignIn(code);
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "healthChannelSignInRecall err:");
            Log.printStackTrace(t);
        }
    }

    private static void healthChannelSignIn(String code) {
        try {
            String s = OtherTaskRpcCall.independent_component_sign_in(code);
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("isSuccess")) {
                Log.other("健康医疗🚑[签到成功]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "healthChannelSignIn err:");
            Log.printStackTrace(t);
        }
    }

    /* 天天来财 */
    public static void ttlcHomepageQuery() {
        try {
            String s = OtherTaskRpcCall.ttlc_homepage_query();
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                JSONObject data = jo.getJSONObject("data");
                if (data.optBoolean("hasPendingActivity") && data.has("pendingActivityInfo")) {
                    JSONObject pendingActivityInfo = data.getJSONObject("pendingActivityInfo");
                    if (pendingActivityInfo.optBoolean("hasWinning")) {
                        String activityName = pendingActivityInfo.optString("activityName");
                        double amount = pendingActivityInfo.getJSONObject("prizeAmountTotal").optDouble("amount");
                        Log.other("天天来财💰[中奖啦-" + activityName + "]#" + amount + "元");
                        JSONObject jo_recall = new JSONObject(OtherTaskRpcCall.task_recall(""));
                        if (jo_recall.optBoolean("success")) {
                            String award_activityId = pendingActivityInfo.optString("activityId");
                            JSONObject jo_award = new JSONObject(OtherTaskRpcCall.prize_award(award_activityId));
                            if (jo_award.optBoolean("success")) {
                                Log.other("天天来财💰[领奖成功，红包三天有效，请尽快使用哟~]");
                            }
                        }
                    }
                }
                String activityId = data.optString("activityId");
                taskRecall(activityId);
                JSONArray boxedLuckyNumbers = data.optJSONArray("boxedLuckyNumbers");
                int boxedLuckyNumbersLength = boxedLuckyNumbers.length();
                int maxLuckyNumbersLength = data.getInt("maxLuckyNumbersLength");
                if (boxedLuckyNumbersLength < maxLuckyNumbersLength) {
                    JSONArray piXiuFoods = data.optJSONArray("piXiuFoods");
                    if (piXiuFoods != null && piXiuFoods.length() > 0) {
                        int min = Math.min(piXiuFoods.length(), maxLuckyNumbersLength - boxedLuckyNumbersLength);
                        for (int i = 0; i < min; i++) {
                            jo = piXiuFoods.optJSONObject(i);
                            if (jo == null)
                                continue;
                            String status = jo.optString("status");
                            if ("UNUSED".equals(status)) {
                                String id = jo.optString("id");
                                jo = new JSONObject(OtherTaskRpcCall.pixiu_food_use(activityId, id));
                                if (jo.optBoolean("success")) {
                                    JSONArray boxedLuckyNumbersAfter =
                                        (JSONArray)JsonUtil.getValueByPathObject(jo, "data.boxedLuckyNumbersAfter");
                                    if (boxedLuckyNumbersAfter != null && boxedLuckyNumbersAfter.length() > 0) {
                                        StringBuilder sb = new StringBuilder();
                                        for (int j = 0; j < boxedLuckyNumbersAfter.length(); j++) {
                                            sb.append(boxedLuckyNumbersAfter.getJSONObject(j).getString("luckyNumber"));
                                        }
                                        Log.other("天天来财💰[集球成功]#当前幸运数字:" + sb);
                                    }
                                }
                            }
                        }

                    }
                }
            } else {
                Log.record("ttlcHomepageQuery:" + s);
            }
        } catch (Throwable t) {
            Log.i(TAG, "ttlcHomepageQuery err:");
            Log.printStackTrace(t);
        }
    }

    private static void taskRecall(String activityId) {
        try {
            String s = OtherTaskRpcCall.task_recall(activityId);
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                JSONArray tasks = (JSONArray)JsonUtil.getValueByPathObject(jo, "data.tasks");
                if (tasks != null && tasks.length() > 0) {
                    for (int i = 0; i < tasks.length(); i++) {
                        jo = tasks.optJSONObject(i);
                        if (jo == null)
                            continue;
                        String status = jo.optString("status");
                        if (!"DONE".equals(status)) {
                            String taskId = jo.optString("taskId");
                            String title = jo.getString("title");
                            jo = new JSONObject(OtherTaskRpcCall.ttlc_appletTrigger(taskId, "signup"));
                            if (jo.optBoolean("success")) {
                                TimeUtil.sleep(300);
                                jo = new JSONObject(OtherTaskRpcCall.ttlc_appletTrigger(taskId, "send"));
                                if (jo.optBoolean("success")) {
                                    Log.other("天天来财💰[完成任务-" + title + "]");
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "taskRecall err:");
            Log.printStackTrace(t);
        }
    }

    /* 看视频 */
    public static void interactTaskQuery() {
        try {
            String s = OtherTaskRpcCall.interact_task_query();
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                JSONArray taskList = jo.optJSONArray("taskList");
                if (taskList != null && taskList.length() > 0) {
                    for (int i = 0; i < taskList.length(); i++) {
                        jo = taskList.optJSONObject(i);
                        if (jo == null)
                            continue;
                        if (jo.optBoolean("completed"))
                            continue;
                        String taskType = jo.optString("taskType");
                        if ("signIn".equals(taskType)) {
                            String rewardParams = jo.optString("rewardParams");
                            String taskActivityId = jo.getString("taskActivityId");
                            interactTaskSignIn(taskType, taskActivityId, rewardParams);
                        } else if ("duration".equals(taskType)) {
                            // addChildTask(new ChildModelTask("Interact", "Other", () ->
                            // contentInteract(jo.toString())));
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "interactTaskQuery err:");
            Log.printStackTrace(t);
        }
    }

    private static void interactTaskSignIn(String taskType, String taskActivityId, String rewardParams) {
        try {
            String s = OtherTaskRpcCall.interact_task_reward(taskType, taskActivityId, rewardParams);
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                double amount = jo.optDouble("amount");
                Log.other("视频签到🎞️[签到成功]#" + amount + "元");
            }
        } catch (Throwable t) {
            Log.i(TAG, "interactTaskSignIn err:");
            Log.printStackTrace(t);
        }
    }

    private static void rewardReserve() {
        try {
            String s = OtherTaskRpcCall.interact_task_activity_reward("reserve");
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                double amount = jo.optDouble("amount");
                Log.other("视频任务🎞️[领取预约奖励]#" + amount + "元");
            }
        } catch (Throwable t) {
            Log.i(TAG, "interactTaskReserve err:");
            Log.printStackTrace(t);
        }
    }

    private static void interactTaskReserve() {
        try {
            String s = OtherTaskRpcCall.interact_task_reserve();
            JSONObject jo = new JSONObject(s);
            if (jo.getBoolean("success")) {
                Log.other("视频任务🎞️[预约成功]");
            }
        } catch (Throwable t) {
            Log.i(TAG, "interactTaskReserve err:");
            Log.printStackTrace(t);
        }
    }

    public static void interactTaskCenter() {
        try {
            String s = OtherTaskRpcCall.interact_task_center();
            JSONObject jo = new JSONObject(s);
            if (jo.optBoolean("success")) {
                JSONArray taskList = jo.optJSONArray("taskList");
                if (taskList != null && taskList.length() > 0) {
                    for (int i = 0; i < taskList.length(); i++) {
                        jo = taskList.optJSONObject(i);
                        if (jo == null)
                            continue;
                        String taskType = jo.optString("taskType");
                        if ("signIn".equals(taskType)) {
                            boolean signedToday = jo.optBoolean("signedToday");
                            if (!signedToday) {
                                interactTaskQuery();
                            }
                        } else if ("reserve".equals(taskType)) {
                            int status = jo.optInt("status");
                            if (status == 1) {
                                interactTaskReserve();
                            } else if (status == 3) {
                                rewardReserve();
                            }
                        }
                    }
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "interactTaskCenter err:");
            Log.printStackTrace(t);
        }
    }

    private static void contentInteract(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            do {
                jSONObject = reward(jSONObject);
            } while (jSONObject != null);
        } catch (Throwable t) {
            Log.i(TAG, "contentInteract err:");
            Log.printStackTrace(t);
        }
    }

    private static JSONObject reward(JSONObject jSONObject) {
        try {
            List<String> list = keys;
            String contentId = list.get(RandomUtil.nextInt(list.size()));
            jSONObject.put("taskExt", "{}");
            jSONObject.put("hasTask", true);
            jSONObject.put("loading", false);
            jSONObject.put("contentId", contentId);
            jSONObject.put("ext", mapContent.get(contentId));
            JSONObject jSONObject2 = jSONObject.getJSONObject("taskData");
            long j = (jSONObject2.getInt("duration") * 1000) + 2000;
            Log.other("看视频等待" + (j / 1000) + "s");
            TimeUtil.sleep(j);
            String replace = jSONObject.toString().replace("\\/", "/");
            JSONObject jo = new JSONObject(OtherTaskRpcCall.interact_task_reward("duration", "", replace));
            if (jo.getBoolean("success")) {
                String optString = jo.optString("availableAmount");
                String optString2 = jo.optString("amount");
                JSONObject optJSONObject = jo.optJSONObject("nextStageTask");
                if (optJSONObject == null) {
                    Log.other("报告大人，今日刷视频任务已完成");
                    return null;
                }
                Log.other("视频获得[" + optString2 + "]可用金额[" + optString + "]");
                if (optJSONObject.getBoolean("completed")) {
                    return null;
                }
                return optJSONObject;
            }
            return null;
        } catch (Throwable th) {
            Log.i(TAG, "reward err:");
            Log.printStackTrace(TAG, th);
            return null;
        }
    }

    public static void ugShopping() {
        try {
            if (Status.canUgShoppingPollingSign()) {
                JSONObject jo = new JSONObject(OtherTaskRpcCall.finishTaskToReward("POLLING_SIGN",""));
                if (!MessageUtil.checkResponse(TAG, jo)) {
                    Status.ugShoppingPollingSign();
                    return;
                }
                if(jo.has("resultCode")){
                    if("TASK_FINISH_NUM_LIMIT".equals(jo.getString("resultCode"))){
                        Status.ugShoppingPollingSign();
                        return;
                    }
                }
                String rewardAmount=jo.getString("rewardAmount");
                Log.other("领购物金🍀[签到]#"+rewardAmount);
                Status.ugShoppingPollingSign();
            }
            JSONObject taskInfo=new JSONObject(OtherTaskRpcCall.queryAllTaskInfo());
            if (!MessageUtil.checkResponse(TAG, taskInfo)) {
                return;
            }
            if(taskInfo.has("gwjTaskDTO")){
                JSONObject gwjTaskDTO=taskInfo.getJSONObject("gwjTaskDTO");
                JSONArray promoTaskList=gwjTaskDTO.optJSONArray("promoTaskList");
                if (promoTaskList != null && promoTaskList.length() > 0) {
                    for (int i = 0; i < promoTaskList.length(); i++) {
                        JSONObject task = promoTaskList.getJSONObject(i);
                        String taskStatus=task.getString("taskStatus");
                        if("FINISHED".equals(taskStatus)){
                            continue;
                        }
                        String taskType=task.getString("taskType");
                        if("GWJ_GUIDE_TASK".equals(taskType)){
                            String taskCode=task.getString("taskCode");
                            String subTaskCode=task.optString("subTaskCode");
                            JSONObject jo = new JSONObject(OtherTaskRpcCall.finishTaskToReward(taskCode,subTaskCode));
                            if (!MessageUtil.checkResponse(TAG, jo)) {
                                continue;
                            }
                            String taskTitle=task.getString("taskTitle");
                            Log.other("领购物金🍀[完成任务]#"+taskTitle);
                            TimeUtil.sleep(500);
                        }else if("BROWSE_TASK".equals(taskType)){
                            String taskCode=task.getString("taskCode");
                            JSONObject jo = new JSONObject(OtherTaskRpcCall.finishTaskToReward(taskCode,""));
                            if (!MessageUtil.checkResponse(TAG, jo)) {
                                continue;
                            }
                            String taskTitle=task.getString("taskTitle");
                            Log.other("领购物金🍀[完成任务]#"+taskTitle);
                            TimeUtil.sleep(500);
                        }else if("LOTTERY_TASK".equals(taskType)){
                            String taskCode=task.getString("taskCode");
                            JSONObject jo = new JSONObject(OtherTaskRpcCall.finishTaskToReward(taskCode,""));
                            if (!MessageUtil.checkResponse(TAG, jo)) {
                                continue;
                            }
                            String taskTitle=task.getString("taskTitle");
                            Log.other("领购物金🍀[完成任务]#"+taskTitle);
                            TimeUtil.sleep(500);
                        }
                    }
                }
            }
        } catch (Throwable th) {
            Log.i(TAG, "ugShopping err:");
            Log.printStackTrace(TAG, th);
        }
    }
}