package io.github.lazyimmortal.sesame.model.task.immortal;

import java.util.HashSet;
import java.util.LinkedHashSet;

import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.BooleanModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.ListModelField.ListJoinCommaToStringModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.data.RuntimeInfo;
import io.github.lazyimmortal.sesame.model.base.TaskCommon;
import io.github.lazyimmortal.sesame.model.task.antForest.AntForestV2;
import io.github.lazyimmortal.sesame.util.ListUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.NotificationUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import lombok.Getter;

public class Immortal extends ModelTask {
    private static final String TAG = Immortal.class.getSimpleName();

    @Override
    public String getName() {
        return "长生界";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.OTHER;
    }

    protected final BooleanModelField stealthCard = new BooleanModelField("stealthCard", "隐身卡 | 接力使用", false);
    protected final  SelectModelField stealthCardOptions = new SelectModelField("stealthCardOptions", "隐身卡 | 使用选项", new HashSet<>(), AntForestV2.AntForestPropOption.class);
    protected final  BooleanModelField bubbleBoost = new BooleanModelField("bubbleBoost", "加速器 | 定时使用", false);
    protected final  SelectModelField bubbleBoostOptions = new SelectModelField("bubbleBoostOptions", "加速器 | 使用选项", new HashSet<>(), AntForestV2.AntForestPropOption.class);
    protected final  ListJoinCommaToStringModelField bubbleBoostTime = new ListJoinCommaToStringModelField("bubbleBoostTime", "加速器 | 定时使用时间", ListUtil.newArrayList("0630"));
    protected final  BooleanModelField energyShield = new BooleanModelField("energyShield", "保护罩 | 接力使用", false);
    protected final  SelectModelField energyShieldOptions = new SelectModelField("energyShieldOptions", "保护罩 | 使用选项", new HashSet<>(), AntForestV2.AntForestPropOption.class);
    private final BooleanModelField antForestVitality = new BooleanModelField("antForestVitality", "活力值 | 扩展", false);
    protected final  SelectModelField antForestVitalityOptions = new SelectModelField("antForestVitalityOptions", "活力值 | 选项", new LinkedHashSet<>(), AntForestAlpha.AntForestVitalityOptions.class);
    @Getter
    private static final BooleanModelField walkDayReward = new BooleanModelField("walkDayReward", "走路线 | 每日夺宝", false);
    private final BooleanModelField incomePlus = new BooleanModelField("incomePlus", "余额宝 | 收益加", false);
    protected final SelectModelField incomePlusOptions = new SelectModelField("incomePlusOptions", "余额宝 | 收益加选项", new LinkedHashSet<>(), YEB.IncomePlusOption.class);
    private final BooleanModelField antBank = new BooleanModelField("antBank", "网商银行 | 开启", false);
    protected final SelectModelField antBankOptions = new SelectModelField("antBankOptions", "网商银行 | 选项", new LinkedHashSet<>(), AntBank.AntBankOption.class);
    private final BooleanModelField antFishPond = new BooleanModelField("antFishPond", "福气鱼塘 | 开启", false);
    protected final SelectModelField antFishPondOptions = new SelectModelField("antFishPondOptions", "福气鱼塘 | 选项", new LinkedHashSet<>(), AntFishPond.AntFishPondOption.class);
    private final BooleanModelField antMemberReSignIn = new BooleanModelField("antMemberReSignIn", "会员签到 | 补签", false);
    private final BooleanModelField goldBill = new BooleanModelField("goldBill", "我的黄金 | 黄金票", false);

    private BooleanModelField carGodCard;
    private BooleanModelField promoprodRedEnvelope;
    private BooleanModelField fundapplication;
    private BooleanModelField salaryday;
    private BooleanModelField jiaoFeiCard;
    private BooleanModelField yebExpGold;
    private BooleanModelField tripBenefit;
    private BooleanModelField healthChannel;
    private BooleanModelField piXiuFood;
    private BooleanModelField contentInteract;

    private BooleanModelField ugShopping;
   
    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(stealthCard);
        modelFields.addField(stealthCardOptions);
        modelFields.addField(bubbleBoost);
        modelFields.addField(bubbleBoostOptions);
        modelFields.addField(bubbleBoostTime);
        modelFields.addField(energyShield);
        modelFields.addField(energyShieldOptions);
        modelFields.addField(antForestVitality);
        modelFields.addField(antForestVitalityOptions);
        modelFields.addField(walkDayReward);
        modelFields.addField(incomePlus);
        modelFields.addField(incomePlusOptions);
        modelFields.addField(antBank);
        modelFields.addField(antBankOptions);
        modelFields.addField(antFishPond);
        modelFields.addField(antFishPondOptions);
        modelFields.addField(antMemberReSignIn);
        modelFields.addField(goldBill);

        modelFields.addField(carGodCard = new BooleanModelField("carGodCard", "开启 | 车神卡", false));
        modelFields.addField(promoprodRedEnvelope = new BooleanModelField("promoprodRedEnvelope", "开启 | 实体红包", false));
        modelFields.addField(fundapplication = new BooleanModelField("fundapplication", "开启 | 摇一摇红包", false));
        // modelFields.addField(salaryday = new BooleanModelField("salaryday", "开启 | 红包雨", false));
        // modelFields.addField(jiaoFeiCard = new BooleanModelField("jiaoFeiCard", "开启 | 无忧卡",false));
        // modelFields.addField(yebExpGold = new BooleanModelField("yebExpGold", "开启 | 体验金", false));
        // modelFields.addField(tripBenefit = new BooleanModelField("tripBenefit", "开启 | 出行优惠",false));
        modelFields.addField(healthChannel = new BooleanModelField("healthChannel", "开启 | 健康医疗任务", false));
        modelFields.addField(piXiuFood = new BooleanModelField("piXiuFood", "开启 | 天天来财", false));
        modelFields.addField(contentInteract = new BooleanModelField("contentInteract", "开启 | 看视频领红包", false));
        modelFields.addField(ugShopping = new BooleanModelField("ugShopping", "开启 | 天天领购物金", false));
        return modelFields;
    }

    @Override
    public Boolean check() {
        if (TaskCommon.IS_ENERGY_TIME) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        try {
            NotificationUtil.sendTaskNotification(this);
            if (antForestVitality.getValue()) {
                AntForestAlpha.vitality();
            }
            if (incomePlus.getValue()) {
                YEB.incomePlus();
            }
            if (antBank.getValue()) {
                AntBank.virtualProfit();
            }
            if (antFishPond.getValue()) {
                AntFishPond.run();
            }
            if (antMemberReSignIn.getValue()) {
                AntMemberAlpha.reSignIn();
            }
            if (goldBill.getValue()) {
                GoldBillAlpha.run();
            }
            long executeTime = RuntimeInfo.getInstance().getLong(RuntimeInfo.RuntimeInfoKey.otherTask);
            if(TimeUtil.isLessThanSecondOfDays(executeTime,System.currentTimeMillis())||System.currentTimeMillis() - executeTime >= 10600000) {
                if (ugShopping.getValue()) {
                    OtherTask.ugShopping();
                }
                if (promoprodRedEnvelope.getValue()) {
                    OtherTask.promoprodTaskList();
                }

                if (carGodCard.getValue()) {
                    OtherTask.carGodCardbenefit();
                }

            /* if (salaryday.getValue()) {
                salarydayTask();
                salaryday();
            } */

                if (fundapplication.getValue()) {
                    OtherTask.moduleRecommend();
                }

                if (healthChannel.getValue()) {
                    OtherTask.healthChannelTask();
                    OtherTask.healthChannelSignInRecall();
                }

                if (piXiuFood.getValue()) {
                    OtherTask.ttlcHomepageQuery();
                }

                if (contentInteract.getValue()) {
                    OtherTask.interactTaskCenter();
                }
                RuntimeInfo.getInstance().put(RuntimeInfo.RuntimeInfoKey.otherTask, System.currentTimeMillis());
            }
        } catch (Throwable t) {
            Log.i(TAG, "start.run err:");
            Log.printStackTrace(TAG, t);
        } finally {
            NotificationUtil.removeTaskNotification(this);
        }
    }
}
