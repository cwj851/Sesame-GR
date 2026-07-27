package io.github.lazyimmortal.sesame.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    public static void zipFolder(File sourceFolder, File zipFile, List<String> excludeFolderNames) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
            zipFile(sourceFolder, sourceFolder, zipOutputStream, excludeFolderNames);
        } catch (IOException e) {
            Log.printStackTrace(e);
        }
    }

    public static void zipFile(File rootFolder, File currentFile, ZipOutputStream zipOutputStream, List<String> excludeFolderNames) throws IOException {
        if (currentFile.isHidden()) {
            // 忽略隐藏文件
            return;
        }
        if (excludeFolderNames.contains(currentFile.getName())) {
            // 排除指定文件夹
            return;
        }
        if (currentFile.isDirectory()) {
            // 是目录，遍历所有文件
            File[] files = currentFile.listFiles();
            if (files == null) {
                return;
            }
            for (File file : files) {
                zipFile(rootFolder, file, zipOutputStream, excludeFolderNames);
            }
        } else {
            String entryName = currentFile.getAbsolutePath().substring(rootFolder.getAbsolutePath().length() + 1);
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipEntry.setTime(currentFile.lastModified());
            zipOutputStream.putNextEntry(zipEntry);

            try (FileInputStream fis = new FileInputStream(currentFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, length);
                }
            }
            zipOutputStream.closeEntry();
        }
    }

    public static boolean unzipFile(File zipFile, File targetDir) {
        if (!zipFile.exists()) {
            Log.record("备份文件不存在:" + zipFile.getAbsolutePath());
            return false;
        }

        try (FileInputStream fileInputStream = new FileInputStream(zipFile);
             ZipInputStream zipInputStream = new ZipInputStream(fileInputStream)) {
            ZipEntry entry;
            byte[] buffer = new byte[1024];

            while ((entry = zipInputStream.getNextEntry()) != null) {
                File outputFile = new File(targetDir, entry.getName());

                // 如果是目录，就创建目录
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    // 确保父目录存在
                    File parentDir = outputFile.getParentFile();
                    if (parentDir != null && !parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    // 写入文件
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    outputFile.setLastModified(entry.getTime());
                }
                zipInputStream.closeEntry();
            }
            return true;
        } catch (IOException e) {
            Log.printStackTrace(e);
            return false;
        }
    }
}
