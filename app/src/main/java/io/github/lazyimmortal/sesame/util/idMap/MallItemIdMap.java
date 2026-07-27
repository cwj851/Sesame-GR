package io.github.lazyimmortal.sesame.util.idMap;

public class MallItemIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "mallItem.json";
    }

    public static MallItemIdMap getInstance() {
        return MallItemIdMap.getInstance(MallItemIdMap.class);
    }
}
