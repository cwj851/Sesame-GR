package io.github.lazyimmortal.sesame.model.extensions.backupRestore;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;

import io.github.lazyimmortal.sesame.data.Model;
import io.github.lazyimmortal.sesame.data.ModelFields;
import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.data.extensions.ExtensionsConfig;
import io.github.lazyimmortal.sesame.data.modelFieldExt.EmptyModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.IntegerModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.ListModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.SelectModelField;
import io.github.lazyimmortal.sesame.data.modelFieldExt.StringModelField;
import io.github.lazyimmortal.sesame.entity.idAndName.CustomOption;
import io.github.lazyimmortal.sesame.ui.ExtensionsActivity;
import io.github.lazyimmortal.sesame.ui.dialog.AlertDialogBuilder;
import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.HandlerUtil;
import io.github.lazyimmortal.sesame.util.ListUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.NotificationUtil;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.ThreadUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import io.github.lazyimmortal.sesame.util.ToastUtil;
import io.github.lazyimmortal.sesame.util.ZipUtil;

public class BackupRestore extends Model {
    private static final String TAG = BackupRestore.class.getSimpleName();

    @Override
    public String getName() {
        return "备份与恢复";
    }

    @Override
    public ModelGroup getGroup() {
        return ModelGroup.OTHER;
    }

    private static StringModelField webDavUrl;
    private static StringModelField webDavAccount;
    private static StringModelField webDavPassword;
    private static StringModelField webDavSyncFileName;
    private static SelectModelField backupOptions;
    private static IntegerModelField backupFileRetentionPeriod;
    private static ListModelField.ListJoinCommaToStringModelField backupExcludeList;

    @Override
    public ModelFields getFields() {
        ModelFields modelFields = new ModelFields();
        modelFields.addField(webDavUrl = new StringModelField("webDavUrl", "WebDAV | 地址", "https://dav.jianguoyun.com/dav/"));
        modelFields.addField(webDavAccount = new StringModelField("webDavAccount", "WebDAV | 账号", ""));
        modelFields.addField(webDavPassword = new StringModelField("webDavPassword", "WebDAV | 密码", ""));
        modelFields.addField(webDavSyncFileName = new StringModelField("webDavSyncFileName", "WebDAV | 同步文件名", "sesame", "同步后的文件名，没有特殊需求默认即可"));
        modelFields.addField(backupOptions = new SelectModelField("backupOptions", "备份 | 选项", new LinkedHashSet<>(), BackupOption.class));
        modelFields.addField(backupExcludeList = new ListModelField.ListJoinCommaToStringModelField("backupExcludeList", "备份 | 排除文件(夹)", ListUtil.newArrayList("log", "bak"), "填写备份时要排除的文件(夹)，多个用英文“,”隔开"));
        modelFields.addField(backupFileRetentionPeriod = new IntegerModelField("backupFileRetentionPeriod", "备份 | 本地文件保留期限(天)", 7, 0, 36500));
        modelFields.addField(new EmptyModelField("backupToLocal", "备份 | 备份到本地", (c, m) -> backupToLocal(c)));
        modelFields.addField(new EmptyModelField("backupToCloud", "备份 | 备份到云端", (c, m) -> backupToCloud(c), "确认备份数据到云端？"));
        modelFields.addField(new EmptyModelField("restoreByLocal", "恢复 | 从本地恢复", (c, m) -> restoreByLocal(c)));
        modelFields.addField(new EmptyModelField("restoreByCloud", "恢复 | 从云端恢复", (c, m) -> restoreByCloud(c)));
        return modelFields;
    }

    private static String getBackupFileName() {
        return webDavSyncFileName.getValue() + "_" + TimeUtil.getCommonDate(new Date()) + ".zip";
    }

    public static void backupToLocal(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/zip");
            intent.putExtra(Intent.EXTRA_TITLE, getBackupFileName());
            File zipFile = new File(context.getExternalCacheDir(), getBackupFileName());
            backupZip(zipFile);
            intent.putExtra("file", zipFile);
            ExtensionsActivity.getInstance(context).exportBackupLauncher.launch(intent);
        } catch (Exception e) {
            Log.printStackTrace(e);
            ToastUtil.show(context, "文档选择器跳转失败");
        }
    }

    public static void backupToLocal(Context context, Intent intent) {
        if (intent == null || intent.getData() == null) {
            return;
        }
        File zipFile = new File(context.getExternalCacheDir(), getBackupFileName());
        backupZip(zipFile);
        if (FileUtil.exportFile(context, zipFile, intent.getData())) {
            FileUtil.deleteFile(zipFile);
        }
    }

    public static void backupToCloud(Context context) {
        if (!canSyncToCloud(context)) {
            return;
        }
        ThreadUtil.start(() -> {
            File zipFile = new File(context.getExternalCacheDir(), getBackupFileName());
            backupZip(zipFile);
            if (backupToCloud(context, zipFile)) {
                ToastUtil.show(context, "云端同步成功");
            }
            FileUtil.deleteFile(zipFile);
        });
    }

    public static Boolean backupToCloud(Context context, File zipFile) {
        boolean result = new WebDavHelper(webDavUrl.getValue(), webDavAccount.getValue(), webDavPassword.getValue())
                .uploadFile(zipFile);
        if (!result) {
            ToastUtil.show(context, "云端同步失败");
        }
        Log.system(TAG, "上传备份文件" + zipFile.getName() + "到" + webDavUrl.getValue() + (result ? "成功" : "失败"));
        return result;
    }

    public static void backupZip(File zipFile) {
        ZipUtil.zipFolder(FileUtil.MAIN_DIRECTORY_FILE, zipFile, backupExcludeList.getValue());
    }

    public static boolean canSyncToCloud(Context context) {
        if (StringUtil.hasEmpty(webDavUrl.getValue(), webDavAccount.getValue(), webDavPassword.getValue(), getBackupFileName())) {
            ToastUtil.show(context, "WebDAV同步停止:请正确填写必要参数(地址、账号、密码、同步文件名)");
            return false;
        }
        return true;
    }

    public static synchronized void autoBackup(Context context) {
        int notificationId = NotificationUtil.getNotificationId(BackupOption.class);
        try {
            ExtensionsConfig.load();
            if (!backupOptions.contains(BackupOption.AUTO_BACKUP_TO_LOCAL.name())
                    && !backupOptions.contains(BackupOption.AUTO_BACKUP_TO_CLOUD.name())) {
                return;
            }
            NotificationUtil.sendNotification(context, notificationId, "自动备份", "正在生成备份文件");
            File zipFile = new File(context.getExternalCacheDir(), getBackupFileName());
            backupZip(zipFile);
            if (backupOptions.contains(BackupOption.AUTO_BACKUP_TO_CLOUD.name()) && canSyncToCloud(context)) {
                NotificationUtil.sendNotification(context, notificationId, "自动备份", "正在同步备份文件到云端");
                backupToCloud(context, zipFile);
            }
            if (backupOptions.contains(BackupOption.AUTO_BACKUP_TO_LOCAL.name())) {
                NotificationUtil.sendNotification(context, notificationId, "自动备份", "正在复制备份文件到本地");
                FileUtil.deleteFile(FileUtil.BAK_DIRECTORY_FILE, System.currentTimeMillis() - TimeUnit.DAYS.toMillis(backupFileRetentionPeriod.getValue()));
                File file = new File(FileUtil.BAK_DIRECTORY_FILE, getBackupFileName());
                FileUtil.moveTo(zipFile, file);
            }
            FileUtil.deleteFile(zipFile);
        } catch (Exception e) {
            Log.printStackTrace(e);
        } finally {
            NotificationUtil.removeNotification(context, notificationId);
        }
    }

    public static void restore(Context context, File zipFile) {
        if (zipFile == null || !zipFile.exists()) {
            ToastUtil.show(context, "备份文件不存在");
            return;
        }
        ToastUtil.show(context, "正在恢复备份中:请耐心等待!");
        File sourceFile = FileUtil.MAIN_DIRECTORY_FILE;
        if (ZipUtil.unzipFile(zipFile, sourceFile)) {
            ToastUtil.show(context, "备份已成功恢复");
            ExtensionsConfig.load();
        }
    }

    public static void restoreByLocal(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/zip");
            ExtensionsActivity.getInstance(context).importBackupLauncher.launch(intent);
        } catch (Exception e) {
            Log.printStackTrace(e);
            ToastUtil.show(context, "文档选择器跳转失败");
        }
    }

    public static void restoreByLocal(Context context, Intent intent) {
        if (intent == null || intent.getData() == null) {
            return;
        }
        File zipFile = new File(context.getExternalCacheDir(), getBackupFileName());
        if (FileUtil.importFile(context, intent.getData(), zipFile)) {
            restore(context, zipFile);
        }
    }

    public static void restoreByCloud(Context context) {
        if (!canSyncToCloud(context)) {
            return;
        }
        AlertDialog alertDialog = AlertDialogBuilder.createLoadingAlertDialog(context);
        alertDialog.show();
        File zipFile = new File(context.getExternalCacheDir(), getBackupFileName());
        WebDavHelper webDavHelper = new WebDavHelper(webDavUrl.getValue(), webDavAccount.getValue(), webDavPassword.getValue());
        ThreadUtil.start(() -> {
            String[] fileNames = webDavHelper.getFileList().toArray(new String[0]);
            HandlerUtil.post(() -> {
                alertDialog.dismiss();
                AlertDialogBuilder.getAlertDialogBuilder(context)
                        .setTitle("请选择恢复文件")
                        .setItems(fileNames, (dialogInterface, i) -> ThreadUtil.start(() -> {
                            boolean result = webDavHelper.downloadFile(fileNames[i], zipFile);
                            if (result) restore(context, zipFile);
                            FileUtil.deleteFile(zipFile);
                        }))
                        .create()
                        .show();
            });
        });
    }

    public enum BackupOption implements CustomOption {
        AUTO_BACKUP_TO_LOCAL("自动备份到本地\nAndroid/media/Alipay/Sesame/bak"),
        AUTO_BACKUP_TO_CLOUD("自动同步到云端\nWebDAV/Sesame");

        private final String nickName;

        BackupOption(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String nickName() {
            return nickName;
        }
    }
}
