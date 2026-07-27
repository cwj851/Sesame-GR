package io.github.lazyimmortal.sesame.util.idMap;

public class NewAncientTreeIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "newAncientTree.json";
    }

    public static NewAncientTreeIdMap getInstance() {
        return NewAncientTreeIdMap.getInstance(NewAncientTreeIdMap.class);
    }
}
