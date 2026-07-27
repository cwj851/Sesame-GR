package io.github.lazyimmortal.sesame.ui;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;

import com.fasterxml.jackson.core.type.TypeReference;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.ModelConfig;
import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.ModuleInfo;
import io.github.lazyimmortal.sesame.data.task.ModelTask;
import io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandle;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.rpc.request.RequestType;
import io.github.lazyimmortal.sesame.ui.dto.ModelDto;
import io.github.lazyimmortal.sesame.ui.dto.ModelFieldInfoDto;
import io.github.lazyimmortal.sesame.ui.dto.ModelFieldShowDto;
import io.github.lazyimmortal.sesame.ui.dto.ModelGroupDto;
import io.github.lazyimmortal.sesame.util.AESUtil;
import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.ToastUtil;

public class NewSettingsActivity extends SettingsActivity {
    private WebView webView;

    private final List<ModelDto> tabList = new ArrayList<>();

    private final List<ModelGroupDto> groupList = new ArrayList<>();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_settings);

        load();
        setBaseBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        setBaseTitleTextColor(ContextCompat.getColor(this, R.color.textColorPrimary));
        setBaseSubtitleTextColor(ContextCompat.getColor(this, R.color.textColorPrimary));

        webView = findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        //settings.setPluginsEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDefaultTextEncodingName(StandardCharsets.UTF_8.name());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // 强制在当前 WebView 中加载 url
                Uri requestUrl = request.getUrl();
                String scheme = requestUrl.getScheme();
                assert scheme != null;
                if (
                        scheme.equalsIgnoreCase("http")
                                || scheme.equalsIgnoreCase("https")
                                || scheme.equalsIgnoreCase("ws")
                                || scheme.equalsIgnoreCase("wss")
                ) {
                    view.loadUrl(requestUrl.toString());
                    return true;
                }
                view.stopLoading();
                ToastUtil.show(NewSettingsActivity.this, "Forbidden Scheme:\"" + scheme + "\"");
                return false;
            }

        });
        webView.addJavascriptInterface(new WebViewCallback(), "HOOK");
        if (!Objects.equals(
                ExtensionsHandle.handleRequest(new Request(RequestType.ENABLE_DEVELOPER_MODE)),
                Boolean.TRUE)) {
            String htmlData = AESUtil.loadDecryptHtmlData(this);
            webView.loadDataWithBaseURL("file:///android_asset/web/", htmlData, "text/html", "UTF-8", null);
        } else {
            webView.loadUrl("file:///android_asset/web/index.html");
//        webView.loadUrl("http://192.168.31.32:5500/app/src/main/assets/web/index.html");
        }
        webView.requestFocus();

        Map<String, ModelConfig> modelConfigMap = ModelTask.getModelConfigMap();
        for (Map.Entry<String, ModelConfig> configEntry : modelConfigMap.entrySet()) {
            ModelConfig modelConfig = configEntry.getValue();
            tabList.add(new ModelDto(configEntry.getKey(), modelConfig.getName(), modelConfig.getIcon(), null));
        }

        for (ModelGroup modelGroup : ModelGroup.values()) {
            groupList.add(new ModelGroupDto(modelGroup.getCode(), modelGroup.getName(), modelGroup.getIcon()));
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    alertBeforeSave();
                }
            }
        });
    }

    public class WebAppInterface {
        @JavascriptInterface
        public void onBackPressed() {
            runOnUiThread(() -> {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    NewSettingsActivity.this.finish();
                }
            });
        }

        @JavascriptInterface
        public void onExit() {
            runOnUiThread(NewSettingsActivity.this::finish);
        }
    }

    private class WebViewCallback {

        @JavascriptInterface
        public String getTabs() {
            return JsonUtil.toJsonString(tabList);
        }

        /*@JavascriptInterface
        public String getAllConfig() {
            return JsonUtil.toJsonString(ModelTask.getModelConfigMap());
        }*/

        @JavascriptInterface
        public String getBuildInfo() {
            return ModuleInfo.getBuildInfo();
        }

        @JavascriptInterface
        public String getUserId() {
            return getSettingsUserId();
        }

        @JavascriptInterface
        public String getGroup() {
            return JsonUtil.toJsonString(groupList);
        }

        @JavascriptInterface
        public String getModelByGroup(String groupCode) {
            Collection<ModelConfig> modelConfigCollection = ModelTask.getGroupModelConfig(ModelGroup.getByCode(groupCode)).values();
            List<ModelDto> modelDtoList = new ArrayList<>();
            for (ModelConfig modelConfig : modelConfigCollection) {
                List<ModelFieldShowDto> modelFields = new ArrayList<>();
                for (ModelField<?> modelField : modelConfig.getFields().values()) {
                    modelFields.add(ModelFieldShowDto.toShowDto(modelField));
                }
                modelDtoList.add(new ModelDto(modelConfig.getCode(), modelConfig.getName(), groupCode, modelFields));
            }
            return JsonUtil.toJsonString(modelDtoList);
        }

        @JavascriptInterface
        public String setModelByGroup(String groupCode, String modelsValue) {
            List<ModelDto> modelDtoList = JsonUtil.parseObject(modelsValue, new TypeReference<List<ModelDto>>() {
            });
            Map<String, ModelConfig> modelConfigSet = ModelTask.getGroupModelConfig(ModelGroup.getByCode(groupCode));
            for (ModelDto modelDto : modelDtoList) {
                ModelConfig modelConfig = modelConfigSet.get(modelDto.getModelCode());
                if (modelConfig != null) {
                    List<ModelFieldShowDto> modelFields = modelDto.getModelFields();
                    if (modelFields != null) {
                        for (ModelFieldShowDto newModelField : modelFields) {
                            if (newModelField != null) {
                                ModelField<?> modelField = modelConfig.getModelField(newModelField.getCode());
                                if (modelField != null) {
                                    modelField.setConfigValue(newModelField.getConfigValue());
                                }
                            }
                        }
                    }
                }
            }
            return "SUCCESS";
        }

        @JavascriptInterface
        public String getModel(String modelCode) {
            ModelConfig modelConfig = ModelTask.getModelConfigMap().get(modelCode);
            if (modelConfig != null) {
                ModelFields modelFields = modelConfig.getFields();
                List<ModelFieldShowDto> list = new ArrayList<>();
                for (ModelField<?> modelField : modelFields.values()) {
                    list.add(ModelFieldShowDto.toShowDto(modelField));
                }
                return JsonUtil.toJsonString(list);
            }
            return null;
        }

        @JavascriptInterface
        public String setModel(String modelCode, String fieldsValue) {
            ModelConfig modelConfig = ModelTask.getModelConfigMap().get(modelCode);
            if (modelConfig != null) {
                try {
                    ModelFields modelFields = modelConfig.getFields();
                    Map<String, ModelFieldShowDto> map = JsonUtil.parseObject(fieldsValue, new TypeReference<Map<String, ModelFieldShowDto>>() {
                    });
                    for (Map.Entry<String, ModelFieldShowDto> entry : map.entrySet()) {
                        ModelFieldShowDto newModelField = entry.getValue();
                        if (newModelField != null) {
                            ModelField<?> modelField = modelFields.get(entry.getKey());
                            if (modelField != null) {
                                modelField.setConfigValue(newModelField.getConfigValue());
                            }
                        }
                    }
                    return "SUCCESS";
                } catch (Exception e) {
                    Log.printStackTrace(e);
                }
            }
            return "FAILED";
        }

        @JavascriptInterface
        public String getField(String modelCode, String fieldCode) {
            ModelConfig modelConfig = ModelTask.getModelConfigMap().get(modelCode);
            if (modelConfig != null) {
                ModelField<?> modelField = modelConfig.getModelField(fieldCode);
                if (modelField != null) {
                    return JsonUtil.toJsonString(ModelFieldInfoDto.toInfoDto(modelField));
                }
            }
            return null;
        }

        @JavascriptInterface
        public String setField(String modelCode, String fieldCode, String fieldValue) {
            ModelConfig modelConfig = ModelTask.getModelConfigMap().get(modelCode);
            if (modelConfig != null) {
                try {
                    ModelField<?> modelField = modelConfig.getModelField(fieldCode);
                    if (modelField != null) {
                        modelField.setConfigValue(fieldValue);
                        return "SUCCESS";
                    }
                } catch (Exception e) {
                    Log.printStackTrace(e);
                }
            }
            return "FAILED";
        }

        @JavascriptInterface
        public void Log(String log) {
            Log.record("设置：" + log);
        }

    }

}
