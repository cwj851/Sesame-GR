package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.idMap.NewAncientTreeIdMap;

public class ForestNewAncientTree extends IdAndName {
    private static List<ForestNewAncientTree> list;

    public ForestNewAncientTree(String id, String name) {
        super(id, name);
    }

    public static List<ForestNewAncientTree> getList() {
        if (list == null) {
            list = new ArrayList<>();
            Set<Map.Entry<String, String>> idSet = NewAncientTreeIdMap.getInstance().getMap().entrySet();
            for (Map.Entry<String, String> entry : idSet) {
                list.add(new ForestNewAncientTree(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    public static void remove(String id) {
        getList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id.equals(id)) {
                list.remove(i);
                break;
            }
        }
    }
}
