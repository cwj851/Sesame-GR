package io.github.lazyimmortal.sesame.data.extensions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.lazyimmortal.sesame.data.ModelConfig;
import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.Log;
import lombok.Data;

@Data
public class ExtensionsConfig {

    private static final String TAG = ExtensionsConfig.class.getSimpleName();
    public static final ExtensionsConfig INSTANCE = new ExtensionsConfig();

    @JsonIgnore
    private boolean init;

    private final Map<String, ModelFields> modelFieldsMap = new ConcurrentHashMap<>();

    public void setModelFieldsMap(Map<String, ModelFields> newModels) {
        modelFieldsMap.clear();
        if (newModels == null) {
            newModels = new HashMap<>();
        }
        Map<String, ModelConfig> modelConfigMap = ExtensionsModel.getModelConfigMap();
        for (ModelConfig modelConfig : modelConfigMap.values()) {
            String modelCode = modelConfig.getCode();
            ModelFields newModelFields = new ModelFields();
            ModelFields configModelFields = modelConfig.getFields();
            ModelFields modelFields = newModels.get(modelCode);
            if (modelFields != null) {
                for (ModelField<?> configModelField : configModelFields.values()) {
                    ModelField<?> modelField = modelFields.get(configModelField.getCode());
                    try {
                        if (modelField != null) {
                            Object value = modelField.getValue();
                            if (value != null) {
                                configModelField.setObjectValue(value);
                            }
                        }
                    } catch (Exception e) {
                        Log.printStackTrace(e);
                    }
                    newModelFields.addField(configModelField);
                }
            } else {
                for (ModelField<?> configModelField : configModelFields.values()) {
                    newModelFields.addField(configModelField);
                }
            }
            modelFieldsMap.put(modelCode, newModelFields);
        }
    }

    public Boolean hasModelFields(String modelCode) {
        return modelFieldsMap.containsKey(modelCode);
    }

    public Boolean hasModelField(String modelCode, String fieldCode) {
        ModelFields modelFields = modelFieldsMap.get(modelCode);
        if (modelFields == null) {
            return false;
        }
        return modelFields.containsKey(fieldCode);
    }

    public static Boolean isModify() {
        String json = null;
        File file = FileUtil.getExtensionsConfigFile();
        if (file.exists()) {
            json = FileUtil.readFromFile(file);
        }
        if (json != null) {
            String formatted = toSaveStr();
            return formatted == null || !formatted.equals(json);
        }
        return true;
    }

    public static Boolean save(Boolean force) {
        if (!force) {
            if (!isModify()) {
                return true;
            }
        }
        boolean success = FileUtil.setExtensionsConfigFile(toSaveStr());
        Log.record("保存扩展配置");
        return success;
    }

    public static synchronized ExtensionsConfig load() {
        File file = FileUtil.getExtensionsConfigFile();
        try {
            if (file.exists()) {
                String json = FileUtil.readFromFile(file);
                JsonUtil.copyMapper().readerForUpdating(INSTANCE).readValue(json);
                String formatted = toSaveStr();
                if (formatted != null && !formatted.equals(json)) {
                    Log.i(TAG, "格式化扩展配置");
                    Log.system(TAG, "格式化扩展配置");
                    FileUtil.write2File(formatted, file);
                }
            } else {
                unload();
                Log.i(TAG, "初始扩展配置");
                Log.system(TAG, "初始扩展配置");
                FileUtil.write2File(toSaveStr(), file);
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
            Log.i(TAG, "重置扩展配置");
            Log.system(TAG, "重置扩展配置");
            try {
                unload();
                FileUtil.write2File(toSaveStr(), file);
            } catch (Exception e) {
                Log.printStackTrace(TAG, t);
            }
        }
        INSTANCE.setInit(true);
        return INSTANCE;
    }

    public static synchronized void unload() {
        for (ModelFields modelFields : INSTANCE.modelFieldsMap.values()) {
            for (ModelField<?> modelField : modelFields.values()) {
                if (modelField != null) {
                    modelField.reset();
                }
            }
        }
    }

    public static String toSaveStr() {
        return JsonUtil.toFormatJsonString(INSTANCE);
    }

}
