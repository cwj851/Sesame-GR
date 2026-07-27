package io.github.lazyimmortal.sesame.model.extensions.backupRestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.Log;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebDavHelper {
    private static final String TAG = WebDavHelper.class.getSimpleName();

    private final OkHttpClient client;
    private final String baseUrl;
    private final String authHeader;
    private final String subDirectory = FileUtil.CONFIG_DIRECTORY_NAME;

    public WebDavHelper(String baseUrl, String account, String password) {
        this.client = new OkHttpClient.Builder().build();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        this.authHeader = Credentials.basic(account, password);
    }

    // 创建文件夹
    public boolean createFolder(String subDirectory) {
        Request request = new Request.Builder()
                .url(baseUrl + subDirectory)
                .method("MKCOL", RequestBody.create(new byte[0], null)) // MKCOL 方法创建文件夹
                .header("Authorization", authHeader)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (Exception e) {
            Log.printStackTrace(TAG, e);
        }
        return false;
    }

    public boolean isFolderExists(String subDirectory) {
        Request request = new Request.Builder()
                .url(baseUrl + subDirectory)
                .method("PROPFIND", null) // 发送 PROPFIND 请求查询目录是否存在
                .header("Authorization", authHeader)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful(); // 200 OK 说明目录存在
        } catch (Exception e) {
            Log.printStackTrace(TAG, e);
        }
        return false;
    }

    // 上传文件
    public boolean uploadFile(File file) {
        if (!isFolderExists(subDirectory) && !createFolder(subDirectory)) {
            // 文件夹不存在且创建失败
            return false;
        }

        String url = baseUrl + subDirectory + "/" + file.getName();
        RequestBody body = RequestBody.create(file, MediaType.parse("application/octet-stream"));
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .header("Authorization", authHeader)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return true;
            } else {
                if (response.body() != null) {
                    String json = response.body().string();
                    Log.record("同步失败:" + json);
                }
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    // 下载文件
    public boolean downloadFile(String fileName, File destination) {
        String url = baseUrl + subDirectory + "/" + fileName;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .header("Authorization", authHeader)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return false;
            }

            // 获取流
            try (InputStream inputStream = response.body().byteStream();
                 FileOutputStream outputStream = new FileOutputStream(destination)) {

                byte[] buffer = new byte[8192]; // 8KB 缓冲区
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return true;
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
            return false;
        }
    }

    public List<String> getFileList() {
        Request request = new Request.Builder()
                .url(baseUrl + subDirectory)
                .method("PROPFIND", RequestBody.create("", null))
                .header("Authorization", authHeader)
                .build();
        List<String> fileList = new ArrayList<>();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                fileList = parseWebDavResponse(response.body().string());
            }
        } catch (Exception e) {
            Log.printStackTrace(TAG, e);
        }
        return fileList;
    }

    private List<String> parseWebDavResponse(String xmlData) {
        List<String> fileNames = new ArrayList<>();
        try {
            Pattern displayNamePattern = Pattern.compile("<d:displayname>(.+?)</d:displayname>");
            Pattern getContentTypePattern = Pattern.compile("<d:getcontenttype>(.+?)</d:getcontenttype>");
            Matcher displayNameMatcher = displayNamePattern.matcher(xmlData);
            Matcher getContentTypeMatcher = getContentTypePattern.matcher(xmlData);

            while (displayNameMatcher.find() && getContentTypeMatcher.find()) {
                if (Objects.equals("httpd/unix-directory", getContentTypeMatcher.group(1))) {
                    continue;
                }
                String displayName = displayNameMatcher.group(1);
                fileNames.add(displayName);
            }
            Collections.reverse(fileNames);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        return fileNames;
    }
}
