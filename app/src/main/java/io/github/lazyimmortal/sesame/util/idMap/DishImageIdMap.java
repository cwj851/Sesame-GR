package io.github.lazyimmortal.sesame.util.idMap;

import java.util.ArrayList;
import java.util.List;

import io.github.lazyimmortal.sesame.entity.DishImageEntity;
import io.github.lazyimmortal.sesame.util.RandomUtil;

public class DishImageIdMap extends AbstractIdMap<DishImageEntity> {
    @Override
    protected String getFileName() {
        return "dishImage.json";
    }

    public synchronized boolean add(DishImageEntity entity) {
        if (!getMap().containsKey(entity.id())) {
            add(entity.id(), entity);
            return save();
        }
        return true;
    }

    public DishImageEntity getRandomDishImage() {
        List<String> list = new ArrayList<>(getMap().keySet());
        if (list.isEmpty()) {
            return new DishImageEntity();
        }
        int pos = RandomUtil.nextInt(0, list.size() - 1);
        return get(list.get(pos));
    }

    public int getCount() {
        return getMap().size();
    }

    public static DishImageIdMap getInstance() {
        return DishImageIdMap.getInstance(DishImageIdMap.class);
    }
}
