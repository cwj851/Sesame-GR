package io.github.lazyimmortal.sesame.data;

import android.os.Build;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import io.github.lazyimmortal.sesame.util.FileUtil;
import io.github.lazyimmortal.sesame.util.JsonUtil;
import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.StringUtil;
import io.github.lazyimmortal.sesame.util.TimeUtil;
import lombok.Data;

@Data
@Deprecated
public class TokenConfig {

    private static final String TAG = TokenConfig.class.getSimpleName();

    public static final TokenConfig INSTANCE = new TokenConfig();

    @JsonIgnore
    private boolean init;

    // sports
    private final Queue<String> customWalkPathIdQueue = new LinkedList<>();

    // farm
    private final Set<Question> questionSet = new HashSet<>();

    // ecoLife
    private final Set<DishImage> dishImageList = new HashSet<>();

    public static String getCustomWalkPathId(String walkCustomPathId) {
        String pathId = INSTANCE.customWalkPathIdQueue.poll();
        if (pathId != null) {
            save();
            return pathId;
        }
        return walkCustomPathId;
    }

    public static Boolean addCustomWalkPathIdQueue(String pathId) {
        INSTANCE.customWalkPathIdQueue.add(pathId);
        return save();
    }

    public static Boolean clearCustomWalkPathIdQueue() {
        TokenConfig tokenConfig = INSTANCE;
        if (!tokenConfig.customWalkPathIdQueue.isEmpty()) {
            tokenConfig.customWalkPathIdQueue.clear();
            return save();
        }
        return true;
    }

    public static String getQuestionAnswer(String question) {
        for (Question qa : INSTANCE.questionSet) {
            if (qa.question.equals(question)) {
                return qa.answer;
            }
        }
        return new Question().answer;
    }

    public static void saveQuestion(String question, String answer) {
        String todayDate = TimeUtil.getDateStr();
        String tomorrowDate = TimeUtil.getDateStr(1);
        Question newQuestion = new Question(tomorrowDate, question, answer);

        Set<Question> set = INSTANCE.questionSet;
        if (!set.contains(newQuestion)) {
            // 更新问题集合
            set.add(newQuestion);

            // 移除过期问题
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                set.removeIf(q -> !q.date.equals(todayDate) && !q.date.equals(tomorrowDate));
            } else {
                Iterator<Question> iterator = set.iterator();
                while (iterator.hasNext()) {
                    Question q = iterator.next();
                    if (!q.date.equals(todayDate) && !q.date.equals(tomorrowDate)) {
                        iterator.remove();
                    }
                }
            }
            save();
        }
    }

    public static Boolean save() {
        Log.record("保存Token配置");
        return FileUtil.setTokenConfigFile(toSaveStr());
    }

    public static synchronized TokenConfig load() {
        File tokenConfigFile = new File(FileUtil.MAIN_DIRECTORY_FILE, "token_config.json");
        try {
            if (tokenConfigFile.exists()) {
                String json = FileUtil.readFromFile(tokenConfigFile);
                JsonUtil.copyMapper().readerForUpdating(INSTANCE).readValue(json);
                String formatted = toSaveStr();
                if (formatted != null && !formatted.equals(json)) {
                    Log.i(TAG, "格式化Token配置");
                    Log.system(TAG, "格式化Token配置");
                    FileUtil.write2File(formatted, tokenConfigFile);
                }
            } else {
                unload();
                Log.i(TAG, "初始Token配置");
                Log.system(TAG, "初始Token配置");
                FileUtil.write2File(toSaveStr(), tokenConfigFile);
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
            Log.i(TAG, "重置Token配置");
            Log.system(TAG, "重置Token配置");
            try {
                unload();
                FileUtil.write2File(toSaveStr(), tokenConfigFile);
            } catch (Exception e) {
                Log.printStackTrace(TAG, t);
            }
        }
        INSTANCE.setInit(true);
        return INSTANCE;
    }

    public static synchronized void unload() {
        try {
            JsonUtil.copyMapper().updateValue(INSTANCE, new TokenConfig());
        } catch (JsonMappingException e) {
            Log.printStackTrace(TAG, e);
        }
    }

    public static String toSaveStr() {
        return JsonUtil.toFormatJsonString(INSTANCE);
    }

    @Data
    public static class DishImage {
        private final String beforeMeals;
        private final String afterMeals;

        public DishImage() {
            beforeMeals = afterMeals = null;
        }

        public DishImage(String beforeMeals, String afterMeals) {
            this.beforeMeals = beforeMeals;
            this.afterMeals = afterMeals;
        }

        public Boolean checkDishImage() {
            return !StringUtil.isEmpty(beforeMeals)
                    && !StringUtil.isEmpty(afterMeals)
                    && !Objects.equals(beforeMeals, afterMeals);
        }
    }

    @Data
    public static class Question {
        private final String date;
        private final String question;
        private final String answer;

        public Question() {
            date = question = answer = null;
        }

        public Question(String date, String question, String answer) {
            this.date = date;
            this.question = question;
            this.answer = answer;
        }

        @Override
        public int hashCode() {
            return Objects.hash(date, question, answer);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Question) {
                Question questionObj = (Question) obj;
                return Objects.equals(questionObj.date, date)
                        && Objects.equals(questionObj.question, question)
                        && Objects.equals(questionObj.answer, answer);
            }
            return false;
        }
    }
}
