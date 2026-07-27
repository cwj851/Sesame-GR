package io.github.lazyimmortal.sesame.util.idMap;

import java.io.File;

public class CooperatePlantIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return UserIdMap.getCurrentUid() + File.separator + "cooperatePlant.json";
    }

    public static CooperatePlantIdMap getInstance() {
        return CooperatePlantIdMap.getInstance(CooperatePlantIdMap.class);
    }
}
