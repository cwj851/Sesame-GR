package io.github.lazyimmortal.sesame.model.extensions.messagePush;

import com.zjiecode.wxpusher.client.WxPusher;
import com.zjiecode.wxpusher.client.bean.Message;
import com.zjiecode.wxpusher.client.bean.MessageResult;
import com.zjiecode.wxpusher.client.bean.Result;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.StringUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WxPusherManager {
    /**
     * 发送极简推送
     *
     * @param simplePushToken 极简推送Token
     * @param content         推送内容
     */
    public static Boolean sendSimplePush(String simplePushToken, String content) {
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "https://wxpusher.zjiecode.com/api/send/message/" + simplePushToken + "/" + content;
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    return true;
                }
                Log.record("WxPusher发送极简通知失败:状态码" + response.code());
            }
        } catch (IOException e) {
            Log.printStackTrace(e);
        }
        return false;
    }

    /**
     * 发送标准推送
     *
     * @param appToken 标准推送AppToken
     * @param uid      用户UID
     * @param content  推送内容
     */
    public static Boolean sendPush(String appToken, String uid, String content) {
        Message message = new Message();
        message.setAppToken(appToken);
        message.setUid(uid);
        message.setContentType(Message.CONTENT_TYPE_TEXT);
        message.setContent(content);
        Result<List<MessageResult>> result = WxPusher.send(message);
        return result.isSuccess();
    }

    /**
     * 发送标准推送
     *
     * @param appToken 标准推送AppToken
     * @param uid      用户UID
     * @param summary  内容摘要
     * @param content  推送内容
     */
    public static Boolean sendPush(String appToken, String uid, String summary, String content) {
        if (StringUtil.isEmpty(summary)) {
            return sendPush(appToken, uid, content);
        }
        return CompletableFuture.supplyAsync(() -> {
            Message message = new Message();
            message.setAppToken(appToken);
            message.setUid(uid);
            message.setContentType(Message.CONTENT_TYPE_TEXT);
            message.setSummary(summary);
            message.setContent(content);
            Result<List<MessageResult>> result = WxPusher.send(message);
            return result.isSuccess();
        }).join();
    }
}