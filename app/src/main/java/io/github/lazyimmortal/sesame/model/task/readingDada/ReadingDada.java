package io.github.lazyimmortal.sesame.model.task.readingDada;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.lazyimmortal.sesame.data.ModelGroup;
import io.github.lazyimmortal.sesame.model.normal.answerAI.AnswerAI;
import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.MessageUtil;
import io.github.lazyimmortal.sesame.util.StringUtil;

/**
 * @author Constanline
 * @since 2023/08/22
 */
public class ReadingDada {
    private static final String TAG = ReadingDada.class.getSimpleName();

    public ModelGroup getGroup() {
        return ModelGroup.STALL;
    }

    public static boolean answerQuestion(JSONObject bizInfo) {
        try {
            String taskJumpUrl = bizInfo.optString("taskJumpUrl");
            if (StringUtil.isEmpty(taskJumpUrl)) {
                taskJumpUrl = bizInfo.getString("targetUrl");
            }
            String activityId = taskJumpUrl.split("activityId%3D")[1].split("%26")[0];
            String outBizId;
            if (taskJumpUrl.contains("outBizId%3D")) {
                outBizId = taskJumpUrl.split("outBizId%3D")[1].split("%26")[0];
            } else {
                outBizId = "";
            }
            JSONObject jo = new JSONObject(ReadingDadaRpcCall.getQuestion(activityId));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            JSONArray options = jo.getJSONArray("options");
            String answer = AnswerAI.getAnswer(jo.getString("title"), JsonUtil.jsonArrayToList(options));
            if (StringUtil.isEmpty(answer)) {
                answer = options.getString(0);
            }
            jo = new JSONObject(ReadingDadaRpcCall.submitAnswer(activityId, outBizId, jo.getString("questionId"), answer));
            if (!MessageUtil.checkResponse(TAG, jo)) {
                return false;
            }
            Log.record("答答星球🪐答题完成[" + answer + "]");
            return true;
        } catch (Throwable e) {
            Log.i(TAG, "answerQuestion err:");
            Log.printStackTrace(TAG, e);
        }
        return false;
    }
}