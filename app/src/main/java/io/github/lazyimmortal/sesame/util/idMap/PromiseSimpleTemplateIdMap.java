package io.github.lazyimmortal.sesame.util.idMap;

public class PromiseSimpleTemplateIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "promiseSimpleTemplate.json";
    }

    public static PromiseSimpleTemplateIdMap getInstance() {
        return PromiseSimpleTemplateIdMap.getInstance(PromiseSimpleTemplateIdMap.class);
    }
}
