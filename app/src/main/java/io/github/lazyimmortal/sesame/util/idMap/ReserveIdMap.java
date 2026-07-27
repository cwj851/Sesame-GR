package io.github.lazyimmortal.sesame.util.idMap;

public class ReserveIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "reserve.json";
    }

    public static ReserveIdMap getInstance() {
        return ReserveIdMap.getInstance(ReserveIdMap.class);
    }
}
