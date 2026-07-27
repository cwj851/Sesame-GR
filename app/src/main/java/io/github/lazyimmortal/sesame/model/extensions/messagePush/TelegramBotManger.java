package io.github.lazyimmortal.sesame.model.extensions.messagePush;

import io.github.lazyimmortal.sesame.util.Log;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TelegramBotManger {
    private static final String BASE_URL = "https://api.telegram.org/bot";
    private final String botToken;
    private final String chatId;

    TelegramBotManger(String botToken, String chatId) {
        this.botToken = botToken;
        this.chatId = chatId;
    }

    public Boolean sendMessage(String content) {
        return sendMessage(botToken, chatId, content);
    }

    public static Boolean sendMessage(String botToken, String chatId, String content) {
        try {
            String url = BASE_URL + botToken + "/sendMessage";
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("chat_id", chatId)
                    .add("text", content)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    return true;
                }
                Log.record("TelegramBot发送消息失败:状态码" + response.code());
            }
        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        return false;
    }
}
