package io.github.lazyimmortal.sesame.util.idMap;

public class MarathonIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "marathon.json";
    }

    public static MarathonIdMap getInstance() {
        return MarathonIdMap.getInstance(MarathonIdMap.class);
    }
}
