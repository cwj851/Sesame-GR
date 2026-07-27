package io.github.lazyimmortal.sesame.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.highcapable.yukihookapi.YukiHookAPI;

import java.io.File;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.data.AppConfig;
import io.github.lazyimmortal.sesame.data.ModuleInfo;
import io.github.lazyimmortal.sesame.data.RunType;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.entity.UserEntity;
import io.github.lazyimmortal.sesame.entity.idAndName.FriendWatch;
import io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandle;
import io.github.lazyimmortal.sesame.model.extensions.logModel.LogType;
import io.github.lazyimmortal.sesame.rpc.request.Request;
import io.github.lazyimmortal.sesame.ui.dialog.AlertDialogBuilder;
import io.github.lazyimmortal.sesame.ui.dialog.ModelFieldDialog;
import io.github.lazyimmortal.sesame.util.ColorUtil;
import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.HandlerUtil;
import io.github.lazyimmortal.sesame.util.IntentUtil;
import io.github.lazyimmortal.sesame.util.LanguageUtil;
import io.github.lazyimmortal.sesame.util.LibraryUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.PermissionUtil;
import io.github.lazyimmortal.sesame.util.Statistics;
import io.github.lazyimmortal.sesame.util.ThreadUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.idMap.UserIdMap;

public class MainActivity extends BaseActivity {

    private boolean hasPermissions = false;

    private boolean isBackground = false;

    private boolean isClick = false;

    private TextView tvStatistics;

    private Runnable titleRunner;

    private String[] userNameArray = {"默认"};

    private UserEntity[] userEntityArray = {null};

    private final ActivityResultLauncher<Intent> importStatisticsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    FileUtil.importFile(this, result.getData().getData(), FileUtil.getStatisticsFile());
                }
            }
    );
    private final ActivityResultLauncher<Intent> exportStatisticsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    FileUtil.exportFile(this, FileUtil.getStatisticsFile(), result.getData().getData());
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatistics = findViewById(R.id.tv_statistics);
        /*ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setIcon(R.drawable.title_logo);
        }*/
        updateSubTitle(ModuleInfo.getRunType());
        titleRunner = () -> updateSubTitle(RunType.DISABLE);
        registerBroadcastReceiver();
/*         AlertDialogBuilder.getAlertDialogBuilder(this)
                .setTitle(R.string.tips)
                .setMessage(R.string.start_message)
                .setPositiveButton(R.string.btn_understood, (dialog, which) -> dialog.dismiss())
                .create()
                .show(); */
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasPermissions) {
            if (!hasFocus) {
                isBackground = true;
                return;
            }
            isBackground = false;
            HandlerUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (isBackground) {
                        return;
                    }
                    hasPermissions = PermissionUtil.checkOrRequestFilePermissions(MainActivity.this);
                    if (hasPermissions) {
                        onResume();
                        return;
                    }
                    ToastUtil.show(MainActivity.this, "未获取文件读写权限");
                    HandlerUtil.postDelayed(this, 2000);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermissions) {
            if (RunType.DISABLE == ModuleInfo.getRunType()) {
                HandlerUtil.postDelayed(titleRunner, 3000);
                try {
                    sendBroadcast(new Intent(IntentUtil.ACTION_ALIPAY_STATUS));
                } catch (Throwable th) {
                    Log.i("view sendBroadcast status err:");
                    Log.printStackTrace(th);
                }
            }
            try {
                LinkedList<String> userNameList = new LinkedList<>();
                LinkedList<UserEntity> userEntityList = new LinkedList<>();
                File[] configFiles = FileUtil.CONFIG_DIRECTORY_FILE.listFiles();
                if (configFiles != null) {
                    for (File configDir : configFiles) {
                        if (configDir.isDirectory()) {
                            String userId = configDir.getName();
                            UserIdMap.loadSelf(userId);
                            UserEntity userEntity = UserIdMap.get(userId);
                            String userName;
                            if (userEntity == null) {
                                userName = userId;
                            } else {
                                userName = userEntity.getShowName() + ": " + userEntity.getAccount();
                            }
                            userNameList.add(userName);
                            userEntityList.add(userEntity);
                        }
                    }
                }
                userNameList.addFirst("默认");
                userEntityList.addFirst(null);
                userNameArray = userNameList.toArray(new String[0]);
                userEntityArray = userEntityList.toArray(new UserEntity[0]);
            } catch (Exception e) {
                userNameArray = new String[]{"默认"};
                userEntityArray = new UserEntity[]{null};
                Log.printStackTrace(e);
            }
            updateStatistics();
        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_test) {
            try {
                HandlerUtil.postDelayed(titleRunner, 3000);
                sendBroadcast(new Intent(IntentUtil.ACTION_ALIPAY_STATUS));
                isClick = true;
            } catch (Throwable th) {
                Log.i("view sendBroadcast status err:");
                Log.printStackTrace(th);
            }
        } else if (v.getId() == R.id.btn_forest_log) {
            IntentUtil.viewLog(this, LogType.FOREST_LOG);
        } else if (v.getId() == R.id.btn_farm_log) {
            IntentUtil.viewLog(this, LogType.FARM_LOG);
        } else if (v.getId() == R.id.btn_other_log) {
            IntentUtil.viewLog(this, LogType.OTHER_LOG);
        } else if (v.getId() == R.id.btn_friend_watch) {
            ModelFieldDialog.show(this, new SelectModelField("", getString(R.string.friend_watch), null, FriendWatch::getList));
        } else if (v.getId() == R.id.btn_github) {
            //   欢迎自己打包 欢迎大佬pr
            //   项目开源且公益  维护都是自愿
            //   但是如果打包改个名拿去卖钱忽悠小白
            //   那我只能说你妈死了 就当开源项目给你妈烧纸钱了
            IntentUtil.viewWebsite(this, "https://github.com/LazyImmortal/Sesame");
        } else if (v.getId() == R.id.btn_settings) {
            selectSettingUid();
        }
    }

    private final int HIDE_THE_APPLICATION_ICON = 1;
    private final int LANGUAGE_SIMPLIFIED_CHINESE = 2;
    private final int HIDE_THE_STATISTIC_FILE = 3;
    private final int EXPORT_THE_STATISTIC_FILE = 4;
    private final int IMPORT_THE_STATISTIC_FILE = 5;
    private final int EXTENSIONS = 6;
    private final int SETTINGS = 7;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int state = getPackageManager()
                .getComponentEnabledSetting(new ComponentName(this, getClass().getCanonicalName() + "Alias"));
        menu.add(0, HIDE_THE_APPLICATION_ICON, HIDE_THE_APPLICATION_ICON, R.string.hide_the_application_icon)
                .setCheckable(true)
                .setChecked(state > PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        menu.add(0, LANGUAGE_SIMPLIFIED_CHINESE, LANGUAGE_SIMPLIFIED_CHINESE, R.string.language_simplified_chinese)
                .setCheckable(true)
                .setChecked(AppConfig.INSTANCE.getLanguageSimplifiedChinese());
        menu.add(0, HIDE_THE_STATISTIC_FILE, HIDE_THE_STATISTIC_FILE, R.string.hide_the_statistic_file)
                .setCheckable(true)
                .setChecked(AppConfig.INSTANCE.getHideStatisticFile());
        menu.add(0, EXPORT_THE_STATISTIC_FILE, EXPORT_THE_STATISTIC_FILE, R.string.export_the_statistic_file);
        menu.add(0, IMPORT_THE_STATISTIC_FILE, IMPORT_THE_STATISTIC_FILE, R.string.import_the_statistic_file);
        menu.add(0, EXTENSIONS, EXTENSIONS, R.string.extensions);
        menu.add(0, SETTINGS, SETTINGS, R.string.settings);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case HIDE_THE_APPLICATION_ICON:
                int state = item.isChecked() ? PackageManager.COMPONENT_ENABLED_STATE_DEFAULT : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                getPackageManager()
                        .setComponentEnabledSetting(new ComponentName(this, getClass().getCanonicalName() + "Alias"), state, PackageManager.DONT_KILL_APP);
                item.setChecked(!item.isChecked());
                break;

            case LANGUAGE_SIMPLIFIED_CHINESE: {
                AppConfig appConfig = AppConfig.INSTANCE;
                appConfig.setLanguageSimplifiedChinese(!appConfig.getLanguageSimplifiedChinese());
                if (AppConfig.save()) {
                    item.setChecked(!item.isChecked());
                    LanguageUtil.setLocal(this);
                    recreate();
                }
                break;
            }

            case HIDE_THE_STATISTIC_FILE: {
                AppConfig appConfig = AppConfig.INSTANCE;
                appConfig.setHideStatisticFile(!appConfig.getHideStatisticFile());
                if (AppConfig.save()) {
                    item.setChecked(!item.isChecked());
                    updateStatistics();
                }
                break;
            }
            case EXPORT_THE_STATISTIC_FILE: {
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/json");
                intent.putExtra(Intent.EXTRA_TITLE, FileUtil.getStatisticsFile().getName());
                exportStatisticsLauncher.launch(intent);
                break;
            }
            case IMPORT_THE_STATISTIC_FILE: {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/json");
                importStatisticsLauncher.launch(intent);
                break;
            }

            case EXTENSIONS:
                IntentUtil.startActivity(this, ExtensionsActivity.class);
                break;

            case SETTINGS:
                selectSettingUid();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectSettingUid() {
        AtomicBoolean selected = new AtomicBoolean(false);
        AlertDialog alertDialog = AlertDialogBuilder.getAlertDialogBuilder(this, "请选择配置", null)
                .setItems(userNameArray, (dialog, which) -> {
                    selected.set(true);
                    dialog.dismiss();
                    goSettingActivity(which);
                })
                .setOnDismissListener(dialog -> selected.set(true))
                .setPositiveButton(R.string.back, (dialog, which) -> dialog.dismiss())
                .create();
        alertDialog.show();
        int length = userNameArray.length;
        if (length > 0 && length < 3) {
            ThreadUtil.start(() -> {
                TimeUtil.sleep(800);
                if (!selected.get()) {
                    alertDialog.dismiss();
                    goSettingActivity(length - 1);
                }
            });
        }
    }

    private void goSettingActivity(int index) {
        UserEntity userEntity = userEntityArray[index];
        if (!LibraryUtil.loadLibrary("sesame")) {
            AppConfig.INSTANCE.setNewUI(false);
        }
        Intent intent = new Intent(this, AppConfig.INSTANCE.getNewUI() ? NewSettingsActivity.class : MaterialSettingsActivity.class);
        if (userEntity != null) {
            intent.putExtra("userId", userEntity.getUserId());
            intent.putExtra("userName", userEntity.getShowName());
        } else {
            intent.putExtra("userName", userNameArray[index]);
        }
        startActivity(intent);
    }

    private void updateSubTitle(RunType runType) {
        switch (runType) {
            case DISABLE:
                setBaseSubtitle(getString(R.string.disable));
                setBaseSubtitleTextColor(ColorUtil.getColor(this, com.google.android.material.R.attr.colorOnError));
                break;
            case MODEL:
                String text = getString(R.string.activated);
                text += " " + YukiHookAPI.Status.Executor.INSTANCE.getName();
                text += " API " + YukiHookAPI.Status.Executor.INSTANCE.getApiLevel();
                setBaseSubtitle(text);
                break;
            case PACKAGE:
                setBaseSubtitle(getString(R.string.running));
                break;
        }
    }

    private void updateStatistics() {
        try {
            Statistics.load();
            Statistics.updateDay(Calendar.getInstance());
            tvStatistics.setText(Statistics.getText());
            tvStatistics.setVisibility(AppConfig.INSTANCE.getHideStatisticFile() ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentUtil.ACTION_SESAME_STATUS);
        intentFilter.addAction(IntentUtil.ACTION_SESAME_UPDATE);
        intentFilter.addAction(IntentUtil.ACTION_SESAME_REQUEST);
        ContextCompat.registerReceiver(this, new SesameBroadcastReceiver(), intentFilter, ContextCompat.RECEIVER_EXPORTED);
    }

    private class SesameBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("view broadcast action:" + action + " intent:" + intent);
            if (action == null) {
                return;
            }
            switch (action) {
                case IntentUtil.ACTION_SESAME_STATUS:
                    //if (RunType.DISABLE == ModuleInfo.getRunType()) {
                        updateSubTitle(RunType.PACKAGE);
                    //}
                    HandlerUtil.removeCallbacks(titleRunner);
                    if (isClick) {
                        ToastUtil.show(context, "仙人掌加载状态正常");
                        isClick = false;
                    }
                    break;
                case IntentUtil.ACTION_SESAME_UPDATE:
                    updateStatistics();
                    break;
                case IntentUtil.ACTION_SESAME_REQUEST:
                    String type = intent.getStringExtra("type");
                    String method = intent.getStringExtra("method");
                    String data = intent.getStringExtra("data");
                    ExtensionsHandle.handleRequest(new Request(type, method, data));
                    break;
            }
        }
    }
}
