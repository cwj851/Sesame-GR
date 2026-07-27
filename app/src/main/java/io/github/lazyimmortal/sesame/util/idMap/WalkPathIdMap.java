package io.github.lazyimmortal.sesame.util.idMap;

public class WalkPathIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "walkPath.json";
    }

    public static WalkPathIdMap getInstance() {
        return WalkPathIdMap.getInstance(WalkPathIdMap.class);
    }
}
