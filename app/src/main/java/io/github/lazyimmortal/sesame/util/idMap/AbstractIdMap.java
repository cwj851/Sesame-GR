package io.github.lazyimmortal.sesame.util.idMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.TypeUtil;

public abstract class AbstractIdMap<T> {
    private static final Map<Class<? extends AbstractIdMap<?>>, AbstractIdMap<?>> INSTANCE_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <IdMap extends AbstractIdMap<?>> IdMap getInstance(Class<IdMap> clazz) {
        if (!INSTANCE_MAP.containsKey(clazz)) {
            try {
                INSTANCE_MAP.put(clazz, clazz.getConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException(clazz.getSimpleName() + ".getConstructor().newInstance() error", e);
            }
        }
        return (IdMap) INSTANCE_MAP.get(clazz);
    }

    private final Type valueType = TypeUtil.getTypeArgument(this.getClass().getGenericSuperclass(), 0);

    private final Map<String, T> idMap = new ConcurrentHashMap<>();

    private final Map<String, T> readOnlyIdMap = Collections.unmodifiableMap(idMap);

    public Map<String, T> getMap() {
        return readOnlyIdMap;
    }

    public T get(String key) {
        return idMap.get(key);
    }

    public synchronized void add(String key, T value) {
        idMap.put(key, value);
    }

    public synchronized void remove(String key) {
        idMap.remove(key);
    }

    protected abstract String getFileName();

    public synchronized void load() {
        idMap.clear();
        try {
            String body = FileUtil.readFromFile(FileUtil.getConfigDirectoryFile(getFileName()));
            if (!body.isEmpty()) {
                Map<String, T> newMap = JsonUtil.parseObject(body, getTypeReference());
                idMap.putAll(newMap);
            }
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
    }

    protected TypeReference<Map<String, T>> getTypeReference() {
        return new TypeReference<Map<String, T>>() {
            @Override
            public Type getType() {
                return new ParameterizedType() {
                    @NonNull
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[]{String.class, valueType};
                    }

                    @NonNull
                    @Override
                    public Type getRawType() {
                        return Map.class;
                    }

                    @Nullable
                    @Override
                    public Type getOwnerType() {
                        return null;
                    }
                };
            }
        };
    }

    public synchronized boolean save() {
        return FileUtil.write2File(JsonUtil.toFormatJsonString(idMap), FileUtil.getConfigDirectoryFile(getFileName()));
    }

    public synchronized void clear() {
        idMap.clear();
    }
}
