package io.github.lazyimmortal.sesame.rpc.request;

import android.content.Context;
import android.content.Intent;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.model.extensions.ExtensionsHandle;
import io.github.lazyimmortal.sesame.util.IntentUtil;

public class RequestHandler {
    private static final String TAG = RequestHandler.class.getSimpleName();

    public static void start(String broadcastFun, String broadcastData, String testType) {
        new Thread() {
            String broadcastFun;
            String broadcastData;
            String testType;

            public Thread setData(String fun, String data, String type) {
                broadcastFun = fun;
                broadcastData = data;
                testType = type;
                return this;
            }

            @Override
            public void run() {
                ExtensionsHandle.handleRequest(
                        new Request(testType, broadcastFun, broadcastData)
                );
            }
        }.setData(broadcastFun, broadcastData, testType).start();
    }

    public static void sendRequestBroadcast(Context context, Request request) {
        Intent intent = new Intent(IntentUtil.ACTION_ALIPAY_REQUEST);
        intent.putExtra("type", request.type);
        intent.putExtra("method", request.method);
        intent.putExtra("data", (String) request.data);
        context.sendBroadcast(intent);
    }

    public static void sendSesameRequestBroadcast(String method, String data) {
        Intent intent = new Intent(IntentUtil.ACTION_SESAME_REQUEST);
        intent.putExtra("type", RequestType.SESAME_REQUEST.name());
        intent.putExtra("method", method);
        intent.putExtra("data", data);
        ApplicationHook.getContext().sendBroadcast(intent);
    }
}
