package io.github.lazyimmortal.sesame.util;

import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import java.util.Objects;

import io.github.lazyimmortal.sesame.hook.ApplicationHook;
import io.github.lazyimmortal.sesame.model.normal.base.BaseModel;

public class MessageUtil {
    private static final String TAG = MessageUtil.class.getSimpleName();
    private static final String UNKNOWN_TAG = "Unknown TAG";

    public static JSONObject newJSONObject(String str) {
        try {
            return new JSONObject(str);
        } catch (Throwable t) {
            Log.i(TAG, "newJSONObject err:");
            Log.printStackTrace(TAG, t);
        }
        return new JSONObject();
    }

    public static void printErrorMessage(String tag, JSONObject jo) {
        try {
            String errMsg = tag + " error:";
            if (jo.has("resultDesc")) {
                errMsg += jo.getString("resultDesc");
            } else if (jo.has("resultView")) {
                errMsg += jo.getString("resultView");
            } else if (jo.has("resultMsg")) {
                errMsg += jo.getString("resultMsg");
            } else if (jo.has("memo")) {
                errMsg += jo.getString("memo");
            } else if (jo.has("desc")) {
                errMsg += jo.getString("desc");
            } else if (jo.has("message")) {
                errMsg += jo.getString("message");
            } else if (jo.has("errorDesc")) {
                errMsg += jo.getString("errorDesc");
            } else if (jo.has("errorMessage")) {
                errMsg += jo.getString("errorMessage");
            } else if (jo.has("errorMsg")) {
                errMsg += jo.getString("errorMsg");
            } else if (jo.has("errMsg")) {
                errMsg += jo.getString("errMsg");
            } else {
                errMsg += jo.toString();
            }
            Log.record(errMsg);
            Log.i(errMsg, jo.toString());
            if (jo.has("error") && BaseModel.getSendNotificationOptions().contains(
                    BaseModel.SendNotificationOption.SEND_ERROR_NOTIFICATION.name())) {
                if (Objects.equals(jo.opt("error"), 1009)) {
                    NotificationUtil.sendNotification(ApplicationHook.getContext(),
                            NotificationUtil.getNotificationId(tag),
                            "请求异常",
                            errMsg,
                            true
                    );
/*                    Context context = ApplicationHook.getContext();
                    context.sendBroadcast(new Intent("com.eg.android.AlipayGphone.captcha"));
                    Log.record("系统繁忙，可能需要滑动验证");*/
                }
            }
        } catch (Throwable t) {
            Log.i(TAG, "printErrorMessage err:");
            Log.printStackTrace(TAG, t);
        }
    }

    public static Boolean checkResponse(JSONObject jo) {
        return checkResponse(UNKNOWN_TAG, jo);
    }

    public static Boolean checkResponse(String tag, JSONObject jo) {
        try {
            if (jo.optBoolean("success") || jo.optBoolean("isSuccess")) {
                return true;
            }
            printErrorMessage(tag, jo);
        } catch (Throwable t) {
            Log.i(TAG, "checkResponse err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    /**
     * @deprecated 请使用 {@link #checkResponse(String, JSONObject)} 替代
     */
    @Deprecated
    public static Boolean checkResultCode(String tag, JSONObject jo) {
        try {
            Object resultCode = jo.opt("resultCode");
            if (resultCode instanceof Integer) {
                return checkResultCodeInteger(tag, jo);
            } else if (resultCode instanceof String) {
                return checkResultCodeString(tag, jo);
            }
            Log.i(tag, jo.toString());
        } catch (Throwable t) {
            Log.i(TAG, "checkResultCode err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    public static Boolean checkResultCodeString(String tag, JSONObject jo) {
        try {
            String resultCode = jo.optString("resultCode");
            if (!resultCode.equalsIgnoreCase("SUCCESS")
                    && !resultCode.equals("100")
                    && !resultCode.equals("200")) {
                printErrorMessage(tag, jo);
                return false;
            }
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "checkResultCodeString err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }

    public static Boolean checkResultCodeInteger(String tag, JSONObject jo) {
        try {
            int resultCode = jo.optInt("resultCode");
            if (resultCode != 200) {
                printErrorMessage(tag, jo);
                return false;
            }
            return true;
        } catch (Throwable t) {
            Log.i(TAG, "checkResultCodeInteger err:");
            Log.printStackTrace(TAG, t);
        }
        return false;
    }
}
