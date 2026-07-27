package io.github.lazyimmortal.sesame.util.idMap;

public class AnimalIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "animal.json";
    }

    public static AnimalIdMap getInstance() {
        return AnimalIdMap.getInstance(AnimalIdMap.class);
    }
}
