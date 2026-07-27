package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.idMap.NeverLandBenefitIdMap;

public class NeverLandBenefit extends IdAndName {

    public NeverLandBenefit(String id, String name) {
        super(id, name);
    }

    public static List<NeverLandBenefit> getList() {
        List<NeverLandBenefit> list = new ArrayList<>();
        Set<Map.Entry<String, String>> idSet = NeverLandBenefitIdMap.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> entry: idSet) {
            list.add(new NeverLandBenefit(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}
