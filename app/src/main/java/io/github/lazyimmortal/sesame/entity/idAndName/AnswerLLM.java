package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.entity.LLMEntity;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.idMap.AbstractIdMap;
import io.github.lazyimmortal.sesame.util.idMap.LLMIdMap;

public class AnswerLLM extends IdAndName {

    public AnswerLLM(String id, String name) {
        super(id, name);
    }

    @Override
    public AbstractIdMap<?> getIdMapInstance() {
        return LLMIdMap.getInstance();
    }

    public static List<AnswerLLM> getList() {
        List<AnswerLLM> list = new ArrayList<>();
        Set<Map.Entry<String, LLMEntity>> idSet = LLMIdMap.getInstance().getMap().entrySet();
        for (Map.Entry<String, LLMEntity> entry : idSet) {
            try {
                list.add(new AnswerLLM(entry.getKey(), entry.getValue().name()));
            } catch (Exception e) {
                Log.printStackTrace(e);
            }
        }
        return list;
    }
}
