package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.idMap.VitalityBenefitIdMap;

public class ForestVitalityBenefit extends IdAndName {

    public ForestVitalityBenefit(String id, String name) {
        super(id, name);
    }

    public static List<ForestVitalityBenefit> getList() {
        List<ForestVitalityBenefit> list = new ArrayList<>();
        Set<Map.Entry<String, String>> idSet = VitalityBenefitIdMap.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> entry: idSet) {
            list.add(new ForestVitalityBenefit(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}
