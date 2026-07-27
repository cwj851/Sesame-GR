package io.github.lazyimmortal.sesame.util.idMap;

import java.io.File;

public class AchievementOrnamentIdMap extends AbstractIdMap<String> {
    @Override
    protected String getFileName() {
        return UserIdMap.getCurrentUid() + File.separator + "achievementOrnament.json";
    }

    public static AchievementOrnamentIdMap getInstance() {
        return AchievementOrnamentIdMap.getInstance(AchievementOrnamentIdMap.class);
    }
}
