package io.github.lazyimmortal.sesame.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

/**
 * @author Constanline
 * @since 2023/08/18
 */
public class RuntimeInfo {
    private static final String TAG = RuntimeInfo.class.getSimpleName();

    private static RuntimeInfo instance;

    private JSONObject joAll;
    private JSONObject joCurrent;
    private final String userId;

    public enum RuntimeInfoKey {
        BackupTime,
        RequestSuccess,
        ForestPauseTime,
        ForestGivePropRdsBizNo,
        ForestGivePropRdsToken,
        FishPondAngleBizNo,
        FishPondAngleRiskToken,
        FishPondAngleTokenUpdateTime,
        FishPondAngleTokenExpiryTime,
        MemberSignPagePauseTime,
        otherTask
    }

    public static RuntimeInfo getInstance() {
        if (instance == null || !Objects.equals(instance.userId, UserIdMap.getCurrentUid())) {
            instance = new RuntimeInfo();
        }
        return instance;
    }

    public static RuntimeInfo getInstance(boolean force) {
        if (force) {
            return instance = new RuntimeInfo();
        } else {
            return getInstance();
        }
    }

    private RuntimeInfo() {
        try {
            joAll = new JSONObject(FileUtil.readFromFile(FileUtil.getRuntimeInfoFile(null)));
        } catch (JSONException e) {
            joAll = new JSONObject();
        }
        userId = UserIdMap.getCurrentUid();
        String content = FileUtil.readFromFile(FileUtil.getRuntimeInfoFile(userId));
        try {
            joCurrent = new JSONObject(content);
        } catch (Exception ignored) {
            joCurrent = new JSONObject();
        }
    }

    public void save() {
        FileUtil.write2File(joCurrent.toString(), FileUtil.getRuntimeInfoFile(userId));
    }

    public Object get(RuntimeInfoKey key) {
        return joCurrent.opt(key.name());
    }

    public String getString(String key) {
        return joCurrent.optString(key);
    }

    public Long getLong(String key, long def) {
        return joCurrent.optLong(key, def);
    }

    public Boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public Boolean getBoolean(String key, boolean def) {
        return joCurrent.optBoolean(key, def);
    }

    public String getString(RuntimeInfoKey key) {
        return joCurrent.optString(key.name());
    }

    public Long getLong(RuntimeInfoKey key) {
        return joCurrent.optLong(key.name(), 0L);
    }

    public void put(RuntimeInfoKey key, Object value) {
        put(key.name(), value);
    }

    public void put(String key, Object value) {
        try {
            joCurrent.put(key, value);
        } catch (JSONException e) {
            Log.i(TAG, "put err:");
            Log.printStackTrace(TAG, e);
        }
        save();
    }

    public Object getByAll(RuntimeInfoKey key) {
        return joAll.opt(key.name());
    }

    public Long getLongByAll(RuntimeInfoKey key, long def) {
        return joAll.optLong(key.name(), def);
    }

    public void putAll(RuntimeInfoKey key, Object value) {
        putAll(key.name(), value);
    }

    public void putAll(String key, Object value) {
        try {
            joAll.put(key, value);
        } catch (JSONException e) {
            Log.i(TAG, "putAll err:");
            Log.printStackTrace(TAG, e);
        }
        FileUtil.write2File(joAll.toString(), FileUtil.getRuntimeInfoFile(null));
    }
}
