package io.github.lazyimmortal.sesame.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class ChecksumUtil {

    /**
     * @param file         要获取校验和的文件
     * @param checksumType 校验和类型
     * @return 校验和
     */
    public static String getFileChecksum(File file, ChecksumType checksumType) {
        return getFileChecksum(file, checksumType.algorithm);
    }

    /**
     * @param file      要获取校验和的文件
     * @param algorithm 算法: MD5 SHA-1 SHA-256
     * @return 校验和
     */
    public static String getFileChecksum(File file, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            fis.close();

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest.digest()) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        return null;
    }

    public enum ChecksumType {
        MD5("MD5"),
        SHA1("SHA-1"),
        SHA256("SHA-256");

        public final String algorithm;

        ChecksumType(String algorithm) {
            this.algorithm = algorithm;
        }
    }
}
