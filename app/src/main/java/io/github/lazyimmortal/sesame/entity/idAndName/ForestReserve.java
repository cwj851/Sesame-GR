package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.idMap.AbstractIdMap;
import io.github.lazyimmortal.sesame.util.idMap.ReserveIdMap;

public class ForestReserve extends IdAndName {
    private static List<ForestReserve> list;

    public ForestReserve(String id, String name) {
        super(id, name);
    }

    public static List<ForestReserve> getList() {
        if (list == null) {
            list = new ArrayList<>();
            Set<Map.Entry<String, String>> idSet = ReserveIdMap.getInstance().getMap().entrySet();
            for (Map.Entry<String, String> entry : idSet) {
                list.add(new ForestReserve(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    @Override
    public AbstractIdMap<String> getIdMapInstance() {
        return ReserveIdMap.getInstance();
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
