package io.github.lazyimmortal.sesame.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.AppConfig;
import io.github.lazyimmortal.sesame.data.ConfigV2;
import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModuleInfo;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.entity.idAndName.AlipayUser;
import io.github.lazyimmortal.sesame.ui.dialog.AlertDialogBuilder;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;
import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.IntentUtil;
import io.github.lazyimmortal.sesame.util.LibraryUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.idMap.AchievementOrnamentIdMap;
import io.github.lazyimmortal.sesame.util.idMap.AnimalIdMap;
import io.github.lazyimmortal.sesame.util.idMap.BeachIdMap;
import io.github.lazyimmortal.sesame.util.idMap.CooperatePlantIdMap;
import io.github.lazyimmortal.sesame.util.idMap.FlashSaleIdMap;
import io.github.lazyimmortal.sesame.util.idMap.MallItemIdMap;
import io.github.lazyimmortal.sesame.util.idMap.MarathonIdMap;
import io.github.lazyimmortal.sesame.util.idMap.MemberBenefitIdMap;
import io.github.lazyimmortal.sesame.util.idMap.MerchantSeckillIdMap;
import io.github.lazyimmortal.sesame.util.idMap.NeverLandBenefitIdMap;
import io.github.lazyimmortal.sesame.util.idMap.NewAncientTreeIdMap;
import io.github.lazyimmortal.sesame.util.idMap.PromiseSimpleTemplateIdMap;
import io.github.lazyimmortal.sesame.util.idMap.ReserveIdMap;
import io.github.lazyimmortal.sesame.util.idMap.TreeIdMap;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;
import io.github.lazyimmortal.sesame.util.idMap.VitalityBenefitIdMap;
import io.github.lazyimmortal.sesame.util.idMap.WalkPathIdMap;

public class SettingsActivity extends BaseActivity {

    private String userId;
    private String userName;
    private Boolean debug = false;

    private final ActivityResultLauncher<Intent> importLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    importConfig(result.getData());
                }
            }
    );
    private final ActivityResultLauncher<Intent> exportLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    exportConfig(result.getData());
                }
            }
    );

    @Override
    public String getBaseSubtitle() {
        return getString(R.string.settings);
    }

    private final int EXPORT_CONFIG = 1;
    private final int IMPORT_CONFIG = 2;
    private final int DELETE_CONFIG = 3;
    private final int ONE_WAY_FRIEND = 4;
    private final int EXTENSIONS = 5;
    private final int SWITCH_UI = 6;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, EXPORT_CONFIG, EXPORT_CONFIG, "导出配置");
        menu.add(0, IMPORT_CONFIG, IMPORT_CONFIG, "导入配置");
        menu.add(0, DELETE_CONFIG, DELETE_CONFIG, "删除配置");
        menu.add(0, ONE_WAY_FRIEND, ONE_WAY_FRIEND, "单向好友");
        menu.add(0, EXTENSIONS, EXTENSIONS, "扩展功能");
        if (LibraryUtil.loadLibrary("sesame")) {
            menu.add(0, SWITCH_UI, SWITCH_UI, "切换UI");
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EXPORT_CONFIG:
                Intent exportIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                exportIntent.addCategory(Intent.CATEGORY_OPENABLE);
                exportIntent.setType("application/json");
                exportIntent.putExtra(Intent.EXTRA_TITLE, "[" + userName + "]-config_v2.json");
                exportLauncher.launch(exportIntent);
                break;
            case IMPORT_CONFIG:
                Intent importIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                importIntent.addCategory(Intent.CATEGORY_OPENABLE);
                importIntent.setType("application/json");
                importIntent.putExtra(Intent.EXTRA_TITLE, "config_v2.json");
                importLauncher.launch(importIntent);
                break;
            case DELETE_CONFIG:
                AlertDialogBuilder.getAlertDialogBuilder(this, "删除配置", "确认删除该配置？")
                        .setPositiveButton(R.string.ok, (dialog, id) -> {
                            File userConfigDirectoryFile = FileUtil.getConfigV2File(userId);
                            if (!StringUtil.isEmpty(userId)) {
                                userConfigDirectoryFile = FileUtil.getUserConfigDirectoryFile(userId);
                            }
                            if (FileUtil.deleteFile(userConfigDirectoryFile)) {
                                ToastUtil.show(this, "配置删除成功");
                            } else {
                                ToastUtil.show(this, "配置删除失败");
                            }
                            finish();
                        }).create()
                        .show();
                break;
            case ONE_WAY_FRIEND:
                ModelFieldDialog.show(this, new SelectModelField("", "单向好友列表", null, AlipayUser.getList(user -> user.getFriendStatus() != 1)));
                break;
            case EXTENSIONS:
                Intent extensionIntent = new Intent(this, ExtensionsActivity.class);
                startActivity(extensionIntent);
                break;
            case SWITCH_UI:
                Class<?> clazz = this instanceof MaterialSettingsActivity
                        ? NewSettingsActivity.class : MaterialSettingsActivity.class;
                AppConfig.INSTANCE.setNewUI(Objects.equals(clazz, NewSettingsActivity.class));
                if (AppConfig.save()) {
                    Intent intent = new Intent(this, clazz);
                    intent.putExtra("userId", userId);
                    intent.putExtra("userName", userName);
                    finish();
                    startActivity(intent);
                } else {
                    ToastUtil.show(this, "切换UI失败");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void load() {
        userId = null;
        userName = null;
        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
            userName = intent.getStringExtra("userName");
            debug = intent.getBooleanExtra("debug", debug);
        }
        Model.initAllModel();
        UserIdMap.setCurrentUserId(userId);
        UserIdMap.load(userId);
        TreeIdMap.getInstance().load();
        ReserveIdMap.getInstance().load();
        AnimalIdMap.getInstance().load();
        MarathonIdMap.getInstance().load();
        NewAncientTreeIdMap.getInstance().load();
        BeachIdMap.getInstance().load();
        WalkPathIdMap.getInstance().load();
        CooperatePlantIdMap.getInstance().load();
        AchievementOrnamentIdMap.getInstance().load();
        VitalityBenefitIdMap.getInstance().load();
        MallItemIdMap.getInstance().load();
        MemberBenefitIdMap.getInstance().load();
        MerchantSeckillIdMap.getInstance().load();
        NeverLandBenefitIdMap.getInstance().load();
        FlashSaleIdMap.getInstance().load();
        PromiseSimpleTemplateIdMap.getInstance().load();
        ConfigV2.load(userId);
        if (userName != null) {
            setBaseSubtitle(getString(R.string.settings) + ": " + userName);
        }
        if (debug) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    protected void alertBeforeSave() {
        if (!ConfigV2.isModify(userId)) {
            finish();
            return;
        }
        AlertDialogBuilder.getAlertDialogBuilder(this, "修改配置", "配置文件发生改变，确认要保存吗？")
                .setPositiveButton(R.string.ok, ((dialogInterface, i) -> {
                    save();
                    finish();
                }))
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> finish())
                .create()
                .show();
    }

    protected void save() {
        if (ConfigV2.save(userId, false)) {
            ToastUtil.show(this, "保存成功！");
            if (!StringUtil.isEmpty(userId)) {
                try {
                    Intent intent = new Intent(IntentUtil.ACTION_ALIPAY_RESTART);
                    intent.putExtra("userId", userId);
                    sendBroadcast(intent);
                } catch (Throwable th) {
                    Log.printStackTrace(th);
                }
            }
        }
        if (!StringUtil.isEmpty(userId)) {
            UserIdMap.save(userId);
        }
    }

    protected String getSettingsUserId() {
        return userId;
    }

    private void importConfig(Intent importIntent) {
        if (importIntent.getData() == null) {
            return;
        }
        try (InputStream inputStream = getContentResolver().openInputStream(importIntent.getData());
             FileOutputStream outputStream = new FileOutputStream(FileUtil.getConfigV2File(userId))) {
            if (FileUtil.streamTo(inputStream, outputStream)) {
                ToastUtil.show(this, "导入配置成功！");
                if (!StringUtil.isEmpty(userId)) {
                    try {
                        Intent intent = new Intent(IntentUtil.ACTION_ALIPAY_RESTART);
                        intent.putExtra("userId", userId);
                        sendBroadcast(intent);
                    } catch (Throwable th) {
                        Log.printStackTrace(th);
                    }
                }
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return;
            }
        } catch (IOException e) {
            Log.printStackTrace(e);
        }
        ToastUtil.show(this, "导入配置失败！");
    }

    private void exportConfig(Intent exportIntent) {
        if (exportIntent.getData() == null) {
            return;
        }
        try (FileInputStream inputStream = new FileInputStream(FileUtil.getConfigV2File(userId));
             OutputStream outputStream = getContentResolver().openOutputStream(exportIntent.getData())) {
            if (FileUtil.streamTo(inputStream, outputStream)) {
                ToastUtil.show(this, "导出配置成功！");
                return;
            }
        } catch (IOException e) {
            Log.printStackTrace(e);
        }
        ToastUtil.show(this, "导出配置失败！");
    }
}
