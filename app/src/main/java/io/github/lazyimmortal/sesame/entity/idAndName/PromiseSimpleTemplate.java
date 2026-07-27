package io.github.lazyimmortal.sesame.entity.idAndName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.idMap.PromiseSimpleTemplateIdMap;

public class PromiseSimpleTemplate extends IdAndName {

    public PromiseSimpleTemplate(String id, String name) {
        super(id, name);
    }

    public static List<PromiseSimpleTemplate> getList() {
        List<PromiseSimpleTemplate> list = new ArrayList<>();
        Set<Map.Entry<String, String>> idSet = PromiseSimpleTemplateIdMap.getInstance().getMap().entrySet();
        for (Map.Entry<String, String> entry : idSet) {
            list.add(new PromiseSimpleTemplate(entry.getKey(), entry.getValue()));
        }
        return list;
    }
}
