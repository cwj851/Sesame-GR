package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.idMap.MemberBenefitIdMap;

public class MemberBenefit extends IdAndName {

    public MemberBenefit(String id, String name) {
        super(id, name);
    }

    public static List<MemberBenefit> getList() {
        List<MemberBenefit> list = new ArrayList<>();
        Set<Map.Entry<String, String>> idSet = MemberBenefitIdMap.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> entry : idSet) {
            list.add(new MemberBenefit(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}
