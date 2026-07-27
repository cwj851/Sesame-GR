package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.entity.DishImageEntity;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.idMap.AbstractIdMap;
import io.github.lazyimmortal.sesame.util.idMap.DishImageIdMap;

public class EcoLifeDishImage extends IdAndName {

    public EcoLifeDishImage(String name, String id) {
        super(name, id);
    }

    @Override
    public AbstractIdMap<?> getIdMapInstance() {
        return DishImageIdMap.getInstance();
    }

    public static List<EcoLifeDishImage> getList() {
        List<EcoLifeDishImage> list = new ArrayList<>();
        Set<Map.Entry<String, DishImageEntity>> idSet = DishImageIdMap.getInstance().getMap().entrySet();
        for (Map.Entry<String, DishImageEntity> entry : idSet) {
            try {
                list.add(new EcoLifeDishImage(entry.getKey(), entry.getValue().name()));
            } catch (Exception e) {
                Log.printStackTrace(e);
            }
        }
        return list;
    }
}
