package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.idMap.AchievementOrnamentIdMap;

public class FarmAchievementOrnament extends IdAndName {

    public FarmAchievementOrnament(String id, String name) {
        super(id, name);
    }

    public static List<FarmAchievementOrnament> getList() {
        List<FarmAchievementOrnament> list = new ArrayList<>();
        Set<Map.Entry<String, String>> idSet = AchievementOrnamentIdMap.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> entry : idSet) {
            list.add(new FarmAchievementOrnament(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}
