package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.lazyimmortal.sesame.util.idMap.AbstractIdMap;
import io.github.lazyimmortal.sesame.util.idMap.BeachIdMap;

public class OceanBeach extends IdAndName {
    private static List<OceanBeach> list;

    public OceanBeach(String id, String name) {
        super(id, name);
    }

    public static List<OceanBeach> getList() {
        if (list == null) {
            list = new ArrayList<>();
            for (Map.Entry<String, String> entry : BeachIdMap.getInstance().getMap().entrySet()) {
                list.add(new OceanBeach(entry.getKey(), entry.getValue()));
            }
        }
        return list;
    }

    @Override
    public AbstractIdMap<String> getIdMapInstance() {
        return BeachIdMap.getInstance();
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
