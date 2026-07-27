package io.github.lazyimmortal.sesame.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONObject;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.model.extensions.logModel.LogType;

public class FileUtil {
    private static final String TAG = FileUtil.class.getSimpleName();

    public static final String CONFIG_DIRECTORY_NAME = "Sesame";
    public static final File MAIN_DIRECTORY_FILE = getMainDirectoryFile();
    public static final File CONFIG_DIRECTORY_FILE = getConfigDirectoryFile();
    public static final File LOG_DIRECTORY_FILE = getLogDirectoryFile();
    public static final File BAK_DIRECTORY_FILE = getBakDirectoryFile();
    private static File cityCodeFile;
    private static File wuaFile;
    private static File exchangeUrlFile;
    private static File certCountDirectory;
    private static File certCountFile;

    private static File getMainDirectoryFile() {
        String storageDirStr = Environment.getExternalStorageDirectory() + File.separator + "Android" +
                File.separator + "media" + File.separator + ClassUtil.PACKAGE_NAME;
        File storageDir = new File(storageDirStr);
        File mainDir = new File(storageDir, CONFIG_DIRECTORY_NAME);
        if (mainDir.exists()) {
            if (mainDir.isFile()) {
                mainDir.delete();
                mainDir.mkdirs();
            }
        } else {
            mainDir.mkdirs();
            /*File oldDirectory = new File(Environment.getExternalStorageDirectory(), CONFIG_DIRECTORY_NAME);
            if (oldDirectory.exists()) {
                File deprecatedFile = new File(oldDirectory, "deprecated");
                if (!deprecatedFile.exists()) {
                    copyFile(oldDirectory, mainDirectory, "config.json");
                    copyFile(oldDirectory, mainDirectory, "friendId.list");
                    copyFile(oldDirectory, mainDirectory, "cooperationId.list");
                    copyFile(oldDirectory, mainDirectory, "reserveId.list");
                    copyFile(oldDirectory, mainDirectory, "statistics.json");
                    copyFile(oldDirectory, mainDirectory, "cityCode.json");
                    try {
                        deprecatedFile.createNewFile();
                    } catch (Throwable ignored) {
                    }
                }
            }*/
        }
        return mainDir;
    }

    private static File getLogDirectoryFile() {
        File logDir = new File(MAIN_DIRECTORY_FILE, "log");
        if (logDir.exists()) {
            if (logDir.isFile()) {
                logDir.delete();
                logDir.mkdirs();
            }
        } else {
            logDir.mkdirs();
        }
        return logDir;
    }

    private static File getBakDirectoryFile() {
        File bakDir = new File(MAIN_DIRECTORY_FILE, "bak");
        if (bakDir.exists()) {
            if (bakDir.isFile()) {
                bakDir.delete();
                bakDir.mkdirs();
            }
        } else {
            bakDir.mkdirs();
        }
        return bakDir;
    }

    private static File getConfigDirectoryFile() {
        File configDir = new File(MAIN_DIRECTORY_FILE, "config");
        if (configDir.exists()) {
            if (configDir.isFile()) {
                configDir.delete();
                configDir.mkdirs();
            }
        } else {
            configDir.mkdirs();
        }
        return configDir;
    }

    public static File getConfigDirectoryFile(String fileName) {
        File configDirectoryFile = new File(CONFIG_DIRECTORY_FILE, fileName);
        if (configDirectoryFile.exists() && configDirectoryFile.isDirectory()) {
            configDirectoryFile.delete();
        }
        return configDirectoryFile;
    }

    public static File getUserConfigDirectoryFile(String userId) {
        if (StringUtil.isEmpty(userId)) {
            return CONFIG_DIRECTORY_FILE;
        }
        File configDir = new File(CONFIG_DIRECTORY_FILE, userId);
        if (configDir.exists()) {
            if (configDir.isFile()) {
                configDir.delete();
                configDir.mkdirs();
            }
        } else {
            configDir.mkdirs();
        }
        return configDir;
    }

    public static File getCertCountDirectoryFile() {
        if (certCountDirectory == null) {
            certCountDirectory = new File(MAIN_DIRECTORY_FILE, "certCount");
            if (certCountDirectory.exists()) {
                if (certCountDirectory.isFile()) {
                    certCountDirectory.delete();
                    certCountDirectory.mkdirs();
                }
            } else {
                certCountDirectory.mkdirs();
            }
        }
        return certCountDirectory;
    }

    public static File getConfigV2File(String userId) {
        File parent = StringUtil.isEmpty(userId)
                ? CONFIG_DIRECTORY_FILE : getUserConfigDirectoryFile(userId);
        File file = new File(parent, "config_v2.json");
        if (!file.exists()) {
            File oldFile = new File(CONFIG_DIRECTORY_FILE, "config_v2-" + userId + ".json");
            if (oldFile.exists()) {
                if (write2File(readFromFile(oldFile), file)) {
                    oldFile.delete();
                } else {
                    file = oldFile;
                }
            }
        }
        return file;
    }

    public static boolean setConfigV2File(String userId, String json) {
        File parent = StringUtil.isEmpty(userId)
                ? CONFIG_DIRECTORY_FILE : getUserConfigDirectoryFile(userId);
        return write2File(json, new File(parent, "config_v2.json"));
    }

    public static File getExtensionsConfigFile() {
        return new File(CONFIG_DIRECTORY_FILE, "extensions_config.json");
    }

    public static boolean setExtensionsConfigFile(String json) {
        return write2File(json, getExtensionsConfigFile());
    }

    public static boolean setTokenConfigFile(String json) {
        return write2File(json, new File(MAIN_DIRECTORY_FILE, "token_config.json"));
    }

    public static File getSelfIdFile(String userId) {
        File file = new File(getUserConfigDirectoryFile(userId), "self.json");
        if (file.exists() && file.isDirectory()) {
            file.delete();
        }
        return file;
    }

    public static File getFriendIdMapFile(String userId) {
        File file = new File(getUserConfigDirectoryFile(userId), "friend.json");
        if (file.exists() && file.isDirectory()) {
            file.delete();
        }
        return file;
    }

    public static File getRuntimeInfoFile(String userId) {
        File parent = StringUtil.isEmpty(userId)
                ? CONFIG_DIRECTORY_FILE : getUserConfigDirectoryFile(userId);
        File file = new File(parent, "runtimeInfo.json");
        if (file.exists() && file.isDirectory()) {
            file.delete();
        }
        return file;
    }

    public static File getStatusFile(String userId) {
        File file = new File(getUserConfigDirectoryFile(userId), "status.json");
        if (file.exists() && file.isDirectory()) {
            file.delete();
        }
        return file;
    }

    public static File getStatisticsFile() {
        File statisticsFile = new File(MAIN_DIRECTORY_FILE, "statistics.json");
        if (statisticsFile.exists() && statisticsFile.isDirectory()) {
            statisticsFile.delete();
        }
        if (statisticsFile.exists()) {
            Log.i(TAG, "[statistics]读:" + statisticsFile.canRead() + ";写:" + statisticsFile.canWrite());
        } else {
            Log.i(TAG, "statisticsFile.json文件不存在");
        }
        return statisticsFile;
    }

    public static File getExportedStatisticsFile() {
        String storageDirStr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + CONFIG_DIRECTORY_NAME;
        File storageDir = new File(storageDirStr);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File exportedStatisticsFile = new File(storageDir, "statistics.json");
        if (exportedStatisticsFile.exists() && exportedStatisticsFile.isDirectory()) {
            exportedStatisticsFile.delete();
        }
        return exportedStatisticsFile;
    }

    public static File getFriendWatchFile() {
        File friendWatchFile = new File(MAIN_DIRECTORY_FILE, "friendWatch.json");
        if (friendWatchFile.exists() && friendWatchFile.isDirectory()) {
            friendWatchFile.delete();
        }
        return friendWatchFile;
    }

    public static File getWuaFile() {
        if (wuaFile == null) {
            wuaFile = new File(MAIN_DIRECTORY_FILE, "wua.list");
        }
        return wuaFile;
    }

    public static File getCertCountFile(String userId) {
        File certCountFile = new File(getCertCountDirectoryFile(), "certCount-" + userId + ".json");
        if (!certCountFile.exists()) {
            JSONObject jo_certCount = new JSONObject();
            write2File(jo_certCount.toString(), certCountFile);
        }
        return certCountFile;
    }

    public static void setCertCount(String userId, String dateString, int certCount) {
        try {
            File certCountFile = getCertCountFile(userId);
            JSONObject jo_certCount = new JSONObject(readFromFile(certCountFile));
            jo_certCount.put(dateString, Integer.toString(certCount));
            write2File(formatJson(jo_certCount,false), certCountFile);
        } catch (Throwable ignored) {
        }
    }

    public static File exchangeUrlFile() {
        if (exchangeUrlFile == null) {
            exchangeUrlFile = new File(MAIN_DIRECTORY_FILE, "exchangeUrl.txt");
            if (!exchangeUrlFile.exists()) {
                try {
                    exchangeUrlFile.createNewFile();
                } catch (Throwable ignored) {
                }
            }
        }
        return exchangeUrlFile;
    }

    public static void saveExchangeUrl(String exchangeUrl) {
        try {
            File exchangeUrlFile = exchangeUrlFile();
            write2File(exchangeUrl, exchangeUrlFile);
        } catch (Throwable ignored) {
        }
    }

    public static File exportFile(File file) {
        String exportDirStr = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + CONFIG_DIRECTORY_NAME;
        File exportDir = new File(exportDirStr);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File exportFile = new File(exportDir, file.getName());
        if (exportFile.exists() && exportFile.isDirectory()) {
            exportFile.delete();
        }
        if (FileUtil.copyTo(file, exportFile)) {
            return exportFile;
        }
        return null;
    }

    /**
     * 将本地文件导出到指定的 URI 位置。
     *
     * @param context 上下文对象，用于获取 ContentResolver 和显示 Toast
     * @param file    要导出的本地文件
     * @param uri     目标文件的 URI（通常是 SAF 选择的存储位置）
     */
    public static boolean exportFile(Context context, File file, Uri uri) {
        if (file == null || uri == null) {
            return false;
        }
        try (FileInputStream inputStream = new FileInputStream(file);
             OutputStream outputStream = context.getContentResolver().openOutputStream(uri)) {
            if (streamTo(inputStream, outputStream)) {
                ToastUtil.show(context, file.getName() + "导出成功");
                return true;
            }
        } catch (IOException e) {
            Log.printStackTrace(e);
            ToastUtil.show(context, file.getName() + "导出失败");
        }
        return false;
    }

    /**
     * 从指定的 URI 读取文件内容，并将其导入到本地存储。
     *
     * @param context 上下文对象，用于获取 ContentResolver 和显示 Toast
     * @param uri     需要导入的文件的 URI（通常是用户选择的文件）
     * @param file    目标存储的本地文件
     */
    public static boolean importFile(Context context, Uri uri, File file) {
        if (uri == null || file == null) {
            return false;
        }
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            if (FileUtil.streamTo(inputStream, outputStream)) {
                ToastUtil.show(context, file.getName() + "导入成功");
                return true;
            }
        } catch (IOException e) {
            Log.printStackTrace(e);
            ToastUtil.show(context, file.getName() + "导入失败");
        }
        return false;
    }

    public static File getCityCodeFile() {
        if (cityCodeFile == null) {
            cityCodeFile = new File(MAIN_DIRECTORY_FILE, "cityCode.json");
            if (cityCodeFile.exists() && cityCodeFile.isDirectory()) {
                cityCodeFile.delete();
            }
        }
        return cityCodeFile;
    }

    public static File getLogFile(LogType logType) {
        File logFile = new File(LOG_DIRECTORY_FILE, logType.getLogFileName());
        if (logFile.exists() && logFile.isDirectory()) {
            logFile.delete();
        }
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (Throwable ignored) {
            }
        }
        return logFile;
    }

    public static void clearLog() {
        File[] files = LOG_DIRECTORY_FILE.listFiles();
        if (files == null) {
            return;
        }
        SimpleDateFormat sdf = Log.DATE_FORMAT_THREAD_LOCAL.get();
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
        String today = sdf.format(new Date());
        for (File file : files) {
            String name = file.getName();
            if (name.endsWith(today + ".log")) {
                if (file.length() < 104_857_600) {
                    continue;
                }
            }
            try {
                file.delete();
            } catch (Exception e) {
                Log.printStackTrace(e);
            }
        }
    }

    public static String readFromFile(File f) {
        if (!f.exists()) {
            return "";
        }
        if (!f.canRead()) {
            ToastUtil.show(ApplicationHook.getContext(), f.getName() + "没有读取权限！", true);
            return "";
        }
        StringBuilder result = new StringBuilder();
        FileReader fr = null;
        try {
            fr = new FileReader(f);
            char[] chs = new char[1024];
            int len;
            while ((len = fr.read(chs)) >= 0) {
                result.append(chs, 0, len);
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        } finally {
            close(fr);
        }
        return result.toString();
    }

    public static boolean write2File(String s, File f) {
        if (f.exists()) {
            if (!f.canWrite()) {
                ToastUtil.show(ApplicationHook.getContext(), f.getAbsoluteFile() + "没有写入权限！", true);
                return false;
            }
            if (f.isDirectory()) {
                f.delete();
                f.getParentFile().mkdirs();
            }
        } else {
            f.getParentFile().mkdirs();
        }
        boolean success = false;
        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            fw.write(s);
            fw.flush();
            success = true;
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        }
        close(fw);
        return success;
    }

    public static boolean append2File(String s, File f) {
        if (f.exists() && !f.canWrite()) {
            ToastUtil.show(ApplicationHook.getContext(), f.getAbsoluteFile() + "没有写入权限！", true);
            return false;
        }
        boolean success = false;
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, true);
            fw.append(s);
            fw.flush();
            success = true;
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        }
        close(fw);
        return success;
    }

    public static boolean moveTo(File source, File dest) {
        if (dest.exists()) {
            dest.delete();
        }
        return source.renameTo(dest);
    }

    public static boolean copyTo(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(createFile(dest)).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            return true;
        } catch (IOException e) {
            Log.printStackTrace(e);
        } finally {
            try {
                if (inputChannel != null) {
                    inputChannel.close();
                }
            } catch (IOException e) {
                Log.printStackTrace(e);
            }
            try {
                if (outputChannel != null) {
                    outputChannel.close();
                }
            } catch (IOException e) {
                Log.printStackTrace(e);
            }
        }
        return false;
    }

    public static boolean streamTo(InputStream source, OutputStream dest) {
        if (source == null || dest == null) {
            return false;
        }
        try {
            byte[] b = new byte[1024];
            int length;
            while ((length = source.read(b)) > 0) {
                dest.write(b, 0, length);
                dest.flush();
            }
            return true;
        } catch (IOException e) {
            Log.printStackTrace(e);
        } finally {
            try {
                source.close();
            } catch (IOException e) {
                Log.printStackTrace(e);
            }
            try {
                dest.close();
            } catch (IOException e) {
                Log.printStackTrace(e);
            }
        }
        return false;
    }

    public static void close(Closeable c) {
        try {
            if (c != null)
                c.close();
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        }
    }

    public static File createFile(File file) {
        if (file.exists() && file.isDirectory()) {
            if (!file.delete()) {
                return null;
            }
        }
        if (!file.exists()) {
            try {
                File parentFile = file.getParentFile();
                if (parentFile != null) {
                    boolean ignore = parentFile.mkdirs();
                }
                if (!file.createNewFile()) {
                    return null;
                }
            } catch (Exception e) {
                Log.printStackTrace(e);
                return null;
            }
        }
        return file;
    }

    public static File createDirectory(File file) {
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                return null;
            }
        }
        if (!file.exists()) {
            try {
                if (!file.mkdirs()) {
                    return null;
                }
            } catch (Exception e) {
                Log.printStackTrace(e);
                return null;
            }
        }
        return file;
    }

    public static Boolean clearFile(File file) {
        if (file.exists()) {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(file);
                fileWriter.write("");
                fileWriter.flush();
                return true;
            } catch (IOException e) {
                Log.printStackTrace(e);
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    Log.printStackTrace(e);
                }
            }
        }
        return false;
    }

    public static Boolean deleteFile(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        File[] files = file.listFiles();
        if (files == null) {
            return file.delete();
        }
        for (File innerFile : files) {
            deleteFile(innerFile);
        }
        return file.delete();
    }

    /**
     * 删除过期文件
     *
     * @param file      要检查的文件
     * @param timestamp 最早保留时间(删除比它早的文件)
     * @return 删除结果
     */
    public static Boolean deleteFile(File file, long timestamp) {
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            if (file.lastModified() < timestamp) {
                return file.delete();
            }
            return false;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return file.delete();
        }
        for (File innerFile : files) {
            deleteFile(innerFile, timestamp);
        }
        return file.listFiles() == null && file.delete();
    }

    public static String formatJson(JSONObject jo, boolean removeQuote) {
        String formatted;
        try {
            formatted = jo.toString(4);
        } catch (Throwable t) {
            return jo.toString();
        }
        if (!removeQuote)
            return formatted;
        StringBuilder sb = new StringBuilder(formatted);
        char currentChar, lastNonSpaceChar = 0;
        for (int i = 0; i < sb.length(); i++) {
            currentChar = sb.charAt(i);
            switch (currentChar) {
                case '"':
                    switch (lastNonSpaceChar) {
                        case ':':
                        case '[':
                            sb.deleteCharAt(i);
                            i = sb.indexOf("\"", i);
                            sb.deleteCharAt(i);
                            if (lastNonSpaceChar != '[')
                                lastNonSpaceChar = sb.charAt(--i);
                    }
                    break;

                case ' ':
                    break;

                default:
                    if (lastNonSpaceChar == '[' && currentChar != ']')
                        break;
                    lastNonSpaceChar = currentChar;
            }
        }
        formatted = sb.toString();
        return formatted;
    }
}
