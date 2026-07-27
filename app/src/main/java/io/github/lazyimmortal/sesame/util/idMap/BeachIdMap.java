package io.github.lazyimmortal.sesame.util.idMap;

public class BeachIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "beach.json";
    }

    public static BeachIdMap getInstance() {
        return BeachIdMap.getInstance(BeachIdMap.class);
    }
}
