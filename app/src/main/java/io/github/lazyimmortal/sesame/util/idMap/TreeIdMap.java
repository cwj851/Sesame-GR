package io.github.lazyimmortal.sesame.util.idMap;

public class TreeIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return "tree.json";
    }

    public static TreeIdMap getInstance() {
        return TreeIdMap.getInstance(TreeIdMap.class);
    }
}
