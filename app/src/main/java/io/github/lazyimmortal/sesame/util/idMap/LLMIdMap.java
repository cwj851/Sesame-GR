package io.github.lazyimmortal.sesame.util.idMap;

import io.github.lazyimmortal.sesame.entity.LLMEntity;

public class LLMIdMap extends AbstractIdMap<LLMEntity> {
    @Override
    protected String getFileName() {
        return "largeLanguageModel.json";
    }

    public synchronized boolean add(LLMEntity entity) {
        if (!getMap().containsKey(entity.id())) {
            add(entity.id(), entity);
            return save();
        }
        return true;
    }

    public static LLMIdMap getInstance() {
        return LLMIdMap.getInstance(LLMIdMap.class);
    }
}
