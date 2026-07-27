package io.github.lazyimmortal.sesame.util;

import android.content.Context;
import android.os.Build;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication;

import java.io.File;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import io.github.lazyimmortal.sesame.R;
import lombok.Data;

@Data
public class Statistics {

    private static final String TAG = Statistics.class.getSimpleName();

    public static final Statistics INSTANCE = new Statistics();

    // farm
    private final Set<Question> questionSet = new HashSet<>();

    private TimeStatistics total = new TimeStatistics();
    private TimeStatistics year = new TimeStatistics();
    private TimeStatistics month = new TimeStatistics();
    private TimeStatistics day = new TimeStatistics();

    public static String getQuestionAnswer(String question) {
        for (Question qa : INSTANCE.questionSet) {
            if (Objects.equals(qa.question, question)) {
                return qa.answer;
            }
        }
        return null;
    }

    public static void saveQuestion(String question, String answer) {
        Question newQuestion = new Question(TimeUtil.getDateStr(1), question, answer);
        Set<Question> set = INSTANCE.questionSet;
        if (!set.contains(newQuestion)) {
            // 更新问题集合
            set.add(newQuestion);
            save();
        }
    }

    public static void removeQuestion() {
        // 移除过期问题
        Set<Question> set = INSTANCE.questionSet;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            set.removeIf(Question::needRemove);
        } else {
            Iterator<Question> iterator = set.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().needRemove()) {
                    iterator.remove();
                }
            }
        }
    }

    public static void addData(DataType dataType, int i) {
        Statistics stat = INSTANCE;
        stat.total.addData(dataType, i);
        stat.day.addData(dataType, i);
        stat.month.addData(dataType, i);
        stat.year.addData(dataType, i);
    }

    public static int getData(TimeType timeType, DataType dataType) {
        TimeStatistics timeStatistics = null;
        if (timeType == TimeType.TOTAL) {
            timeStatistics = INSTANCE.total;
        } else if (timeType == TimeType.YEAR) {
            timeStatistics = INSTANCE.year;
        } else if (timeType == TimeType.MONTH) {
            timeStatistics = INSTANCE.month;
        } else if (timeType == TimeType.DAY) {
            timeStatistics = INSTANCE.day;
        }
        if (timeStatistics == null) {
            return 0;
        }
        return timeStatistics.getData(dataType);
    }

    public static String getText() {
        // 添加表头
        Context context = ModuleApplication.Companion.getAppContext();
        return context.getText(R.string.total) + "  " + INSTANCE.total.makeText() + "\n" +
                context.getText(R.string.year) + "  " + INSTANCE.year.makeText() + "\n" +
                context.getText(R.string.month) + "  " + INSTANCE.month.makeText() + "\n" +
                context.getText(R.string.day) + "  " + INSTANCE.day.makeText();
    }

    public static synchronized Statistics load() {
        try {
            File statisticsFile = FileUtil.getStatisticsFile();
            if (statisticsFile.exists()) {
                String json = FileUtil.readFromFile(statisticsFile);
                JsonUtil.copyMapper().readerForUpdating(INSTANCE).readValue(json);
                String formatted = JsonUtil.toFormatJsonString(INSTANCE);
                if (formatted != null && !formatted.equals(json)) {
                    Log.i(TAG, "重新格式化 statistics.json");
                    Log.system(TAG, "重新格式化 statistics.json");
                    FileUtil.write2File(formatted, statisticsFile);
                }
            } else {
                JsonUtil.copyMapper().updateValue(INSTANCE, new Statistics());
                Log.i(TAG, "初始化 statistics.json");
                Log.system(TAG, "初始化 statistics.json");
                FileUtil.write2File(JsonUtil.toFormatJsonString(INSTANCE), statisticsFile);
            }
        } catch (Throwable t) {
            Log.printStackTrace(TAG, t);
            Log.i(TAG, "统计文件格式有误，已重置统计文件");
            Log.system(TAG, "统计文件格式有误，已重置统计文件");
            try {
                JsonUtil.copyMapper().updateValue(INSTANCE, new Statistics());
                FileUtil.write2File(JsonUtil.toFormatJsonString(INSTANCE), FileUtil.getStatisticsFile());
            } catch (JsonMappingException e) {
                Log.printStackTrace(TAG, e);
            }
        }
        return INSTANCE;
    }

    public static synchronized void unload() {
        try {
            JsonUtil.copyMapper().updateValue(INSTANCE, new Statistics());
        } catch (JsonMappingException e) {
            Log.printStackTrace(TAG, e);
        }
    }

    public static synchronized void save() {
        save(Calendar.getInstance());
    }

    public static synchronized void save(Calendar nowCalendar) {
        if (updateDay(nowCalendar)) {
            Log.system(TAG, "重置 statistics.json");
        } else {
            Log.system(TAG, "保存 statistics.json");
        }
        FileUtil.write2File(JsonUtil.toFormatJsonString(INSTANCE), FileUtil.getStatisticsFile());
    }

    public static Boolean updateDay(Calendar nowCalendar) {
        int year = nowCalendar.get(Calendar.YEAR);
        int month = nowCalendar.get(Calendar.MONTH) + 1;
        int day = nowCalendar.get(Calendar.DAY_OF_MONTH);
        if (INSTANCE.total.time == 0) {
            INSTANCE.total.reset(INSTANCE.year.time);
            INSTANCE.total.addData(DataType.COLLECTED, INSTANCE.year.collected);
            INSTANCE.total.addData(DataType.HELPED, INSTANCE.year.helped);
            INSTANCE.total.addData(DataType.WATERED, INSTANCE.year.watered);
        }
        if (year != INSTANCE.year.time) {
            INSTANCE.year.reset(year);
            INSTANCE.month.reset(month);
            INSTANCE.day.reset(day);
        } else if (month != INSTANCE.month.time) {
            INSTANCE.month.reset(month);
            INSTANCE.day.reset(day);
        } else if (day != INSTANCE.day.time) {
            INSTANCE.day.reset(day);
        } else {
            return false;
        }
        removeQuestion();
        return true;
    }

    public enum TimeType {
        TOTAL, YEAR, MONTH, DAY
    }

    public enum DataType {
        TIME, COLLECTED, HELPED, WATERED
    }

    @Data
    public static class TimeStatistics {
        int time;
        int collected, helped, watered;

        public TimeStatistics() {
        }

        TimeStatistics(int i) {
            reset(i);
        }

        public void reset(int i) {
            time = i;
            collected = 0;
            helped = 0;
            watered = 0;
        }

        public void addData(DataType dataType, int i) {
            switch (dataType) {
                case COLLECTED -> collected += i;
                case HELPED -> helped += i;
                case WATERED -> watered += i;
            }
        }

        public int getData(DataType dataType) {
            if (dataType == DataType.TIME) {
                return time;
            } else if (dataType == DataType.COLLECTED) {
                return collected;
            } else if (dataType == DataType.HELPED) {
                return helped;
            } else if (dataType == DataType.WATERED) {
                return watered;
            } else {
                return 0;
            }
        }

        public String makeText() {
            Context context = ModuleApplication.Companion.getAppContext();
            return context.getText(R.string.collected) + ": " + collected + " " +
                    context.getText(R.string.helped) + ": " + helped + " " +
                    context.getText(R.string.watered) + ": " + watered;
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
            if (obj instanceof Question questionObj) {
                return Objects.equals(questionObj.date, date)
                        && Objects.equals(questionObj.question, question)
                        && Objects.equals(questionObj.answer, answer);
            }
            return false;
        }

        private boolean needRemove() {
            if (date != null) {
                try {
                    Date parseDate = DateFormat.getDateInstance().parse(date);
                    if (parseDate != null) {
                        return TimeUtil.isLessThanNowOfDays(parseDate.getTime());
                    }
                } catch (Exception e) {
                    Log.printStackTrace(e);
                }
            }
            return true;
        }
    }
}
