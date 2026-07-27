package io.github.lazyimmortal.sesame.data;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;

import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication;

import io.github.lazyimmortal.sesame.BuildConfig;
import io.github.lazyimmortal.sesame.R;
import io.github.lazyimmortal.sesame.util.IntentUtil;
import io.github.lazyimmortal.sesame.util.Log;

public class ModuleInfo {
    private final String name;
    private final RunType runType;

    private static ModuleInfo INSTANCE;

    private ModuleInfo() {
        Context context = ModuleApplication.Companion.getAppContext();
        name = context.getString(R.string.app_name);
        if (isModuleActive()) {
            runType = RunType.MODEL;
        } else {
            runType = RunType.DISABLE;
        }
    }

    private static ModuleInfo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModuleInfo();
        }
        return INSTANCE;
    }

    public static String getAppTitle() {
        return getInstance().name + " " + getVersionName();
    }

    public static String getModuleInfo() {
        String appInfo = getFlavor();
        appInfo += "\n模块版本：" + getVersionName() + "(" + getVersionCode() + ")";
        appInfo += "\n构建信息：" + getBuildTime() + "(" + getGitCommitHash() + ")";
        return appInfo;
    }

    public static String getPackageName() {
        return BuildConfig.APPLICATION_ID;
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static String getVersionNameAndCode() {
        return getVersionName() + "(" + getVersionCode() + ")";
    }

    public static String getFlavor() {
        return BuildConfig.FLAVOR;
    }

    public static String getBuildTime() {
        return BuildConfig.BUILD_TIME;
    }

    public static String getGitCommitHash() {
        return BuildConfig.GIT_COMMIT_HASH;
    }

    public static String getBuildInfo() {
        return getPackageName() + ":" + getVersionName();
    }

    public static RunType getRunType() {
        return getInstance().runType;
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug() {
        try {
            ApplicationInfo applicationInfo = ModuleApplication.Companion.getAppContext().getApplicationInfo();
            return (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        return false;
    }

    /**
     * 判断模块是否激活
     */
    private Boolean isModuleActive() {
        Log.chat("思想掠过我的心头，仿佛群群野鸭飞过天空，我听到了它们振翅高飞的声音。我看着蓝天，白云，毛榉，也就得到了答案。");
        return isTaiChiModuleActive();
    }

    /**
     * 判断模块是否在太极、无极中激活
     */
    private Boolean isTaiChiModuleActive() {
        Bundle result;
        Context context = ModuleApplication.Companion.getAppContext();
        Uri uri = Uri.parse("content://me.weishu.exposed.CP/");
        try {
            result = context.getContentResolver().call(uri, "active", null, null);
        } catch (RuntimeException e) {
            // TaiChi is killed, try invoke
            try {
                Intent intent = new Intent(IntentUtil.ACTION_TAI_CHI_ACTIVE);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                result = context.getContentResolver().call(uri, "active", null, null);
            } catch (Throwable t) {
                return false;
            }
        }
        return result != null && result.getBoolean("active", false);
    }
}
