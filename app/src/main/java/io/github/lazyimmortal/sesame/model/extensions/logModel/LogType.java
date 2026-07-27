package io.github.lazyimmortal.sesame.model.extensions.logModel;

import io.github.lazyimmortal.sesame.util.Log;

public enum LogType {
    RUNTIME_LOG("runtime"),
    RECORD_LOG("record"),
    SYSTEM_LOG("system"),
    DEBUG_LOG("debug"),
    FOREST_LOG("forest"),
    FARM_LOG("farm"),
    OTHER_LOG("other"),
    CHAT_LOG("chat"),
    ERROR_LOG("error"),
    ;

    private final String logName;

    LogType(String logName) {
        this.logName = logName;
    }

    public String getLogFileName() {
        return Log.getLogFileName(logName);
    }
}
