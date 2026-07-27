package io.github.lazyimmortal.sesame.model.task.immortal;

import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.modelFieldExt.BooleanModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.entity.CustomOption;

import java.util.LinkedHashSet;

public class Immortal extends ModelTask {
    private static final String TAG = Immortal.class.getSimpleName();

    private BooleanModelField antForestVitalityTask;
    private SelectModelField antForestVitalityOptions;

    @Override
    public String getName() {
        return "长生界";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.OTHER;
    }

    public void setAntForestVitalityTask(BooleanModelField antForestVitalityTask) {
        this.antForestVitalityTask = antForestVitalityTask;
    }

    public void setAntForestVitalityOptions(SelectModelField antForestVitalityOptions) {
        this.antForestVitalityOptions = antForestVitalityOptions;
    }

    public Boolean getAntForestVitalityTask() {
        return antForestVitalityTask != null && antForestVitalityTask.getValue();
    }

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(antForestVitalityTask = new BooleanModelField("antForestVitalityTask", "活力值秒杀", false));
        modelFields.addField(antForestVitalityOptions = new SelectModelField("antForestVitalityOptions", "活力值 | 选项", new LinkedHashSet<>(), CustomOption::getAntForestVitalityOptions));
        return modelFields;
    }

    @Override
    public Boolean check() {
        return antForestVitalityTask.getValue();
    }

    @Override
    public void run() {
        if (!check()) {
            return;
        }
        if (antForestVitalityOptions.getValue().contains("SEC_KILL")) {
            AntForestAlpha.secKill();
        }
    }
}
