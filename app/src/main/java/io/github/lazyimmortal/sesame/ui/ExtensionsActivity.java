package io.github.lazyimmortal.sesame.ui;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Map;
import java.util.Objects;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.ModelConfig;
import io.github.lazyimmortal.sesame.data.ModelField;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.extensions.ExtensionsConfig;
import io.github.lazyimmortal.sesame.data.extensions.ExtensionsModel;
import io.github.lazyimmortal.sesame.model.extensions.backupRestore.BackupRestore;
import io.github.lazyimmortal.sesame.model.extensions.logModel.LogType;
import io.github.lazyimmortal.sesame.ui.dialog.AlertDialogBuilder;
import io.github.lazyimmortal.sesame.util.FileUtil;

public class ExtensionsActivity extends BaseActivity {

    public final ActivityResultLauncher<Intent> importBackupLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    BackupRestore.restoreByLocal(ExtensionsActivity.this, result.getData());
                }
            }
    );
    public final ActivityResultLauncher<Intent> exportBackupLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    BackupRestore.backupToLocal(ExtensionsActivity.this, result.getData());
                }
            }
    );
    public final ActivityResultLauncher<Intent> exportRuntimeLogLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    FileUtil.exportFile(ExtensionsActivity.this, FileUtil.getLogFile(LogType.RUNTIME_LOG), result.getData().getData());
                }
            }
    );

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_material_settings);
        setBaseSubtitle(getString(R.string.extensions));


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        ExtensionsModel.initAllModel();
        ExtensionsConfig.load();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        Map<String, ModelConfig> modelConfigMap = ExtensionsModel.getModelConfigMap();
        for (Map.Entry<String, ModelConfig> configEntry : modelConfigMap.entrySet()) {
            ModelConfig modelConfig = configEntry.getValue();
            ModelFields modelFields = modelConfig.getFields();
            String modelCode = modelConfig.getCode();
            String modelName = modelConfig.getName();
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            for (ModelField<?> modelField : modelFields.values()) {
                if (Objects.equals("enable", modelField.getCode())) {
                    continue;
                }
                View view = modelField.getView(this);
                if (view != null) {
                    linearLayout.addView(view);
                }
            }
            ScrollView scrollView = new ScrollView(this);
            scrollView.addView(linearLayout);
            viewPagerAdapter.addView(modelCode, modelName, scrollView);
        }
        new TabLayoutMediator(tabLayout, viewPager, ((tab, position) -> {
            tab.setText(viewPagerAdapter.getModelName(position));
        })).attach();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                save();
            }
        });
    }

    public void save() {
        if (!ExtensionsConfig.isModify()) {
            finish();
            return;
        }
        AlertDialogBuilder.getAlertDialogBuilder(this, "修改配置", "配置文件发生改变，确认要保存吗？")
                .setPositiveButton(R.string.ok, ((dialogInterface, i) -> {
                    ExtensionsConfig.save(false);
                    finish();
                }))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> finish())
                .create()
                .show();
    }

    public static ExtensionsActivity getInstance(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof ExtensionsActivity) {
                return (ExtensionsActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
