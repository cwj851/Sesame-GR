package io.github.lazyimmortal.sesame.data;

import android.content.Context;
import android.content.pm.PackageInfo;

import androidx.core.content.pm.PackageInfoCompat;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.util.ClassUtil;
import io.github.lazyimmortal.sesame.util.Log;

public class AlipayInfo {
    private final String versionName;
    private final long versionCode;

    private static AlipayInfo INSTANCE;

    private AlipayInfo(String versionName, long versionCode) {
        this.versionName = versionName;
        this.versionCode = versionCode;
    }

    private static AlipayInfo getInstance() {
        if (INSTANCE == null) {
            String versionName = "0";
            long versionCode = 0;
            try {
                Context context = ApplicationHook.getContext();
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(ClassUtil.PACKAGE_NAME, 0);
                versionName = packageInfo.versionName;
                versionCode = PackageInfoCompat.getLongVersionCode(packageInfo);
            } catch (Exception e) {
                Log.printStackTrace(e);
            }
            INSTANCE = new AlipayInfo(versionName, versionCode);
        }
        return INSTANCE;
    }

    public static String getVersionName() {
        return getInstance().versionName;
    }

    public static long getVersionCode() {
        return getInstance().versionCode;
    }

    public static String getVersionNameAndCode() {
        return getVersionName() + "(" + getVersionCode() + ")";
    }

    /**
     * 通过 versionName 来比较版本大小
     *
     * @param versionName 要比较的版本名
     * @return 当前版本更高 > 0;当前版本更低 < 0;版本相同 = 0;
     */
    public static int compareVersionName(String versionName) {
        return compareVersions(getVersionName(), versionName);
    }

    private static int compareVersions(String v1, String v2) {
        String[] version1 = v1.split("\\.");
        String[] version2 = v2.split("\\.");

        int length = Math.max(version1.length, version2.length);
        for (int i = 0; i < length; i++) {
            int num1 = i < version1.length ? Integer.parseInt(version1[i]) : 0;
            int num2 = i < version2.length ? Integer.parseInt(version2[i]) : 0;

            if (num1 < num2) return -1; // v1比v2旧
            if (num1 > num2) return 1;  // v1比v2新
        }
        return 0; // 两个版本相等
    }
}
