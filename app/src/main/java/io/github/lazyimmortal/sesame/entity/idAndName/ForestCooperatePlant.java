package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.idMap.CooperatePlantIdMap;

public class ForestCooperatePlant extends IdAndName {

    public ForestCooperatePlant(String id, String name) {
        super(id, name);
    }

    public static List<ForestCooperatePlant> getList() {
        List<ForestCooperatePlant> list = new ArrayList<>();
        Set<Map.Entry<String, String>> idSet = CooperatePlantIdMap.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> entry : idSet) {
            list.add(new ForestCooperatePlant(entry.getKey(), entry.getValue()));
        }
        return list;
    }

}
