package io.github.lazyimmortal.sesame.model.normal.answerAI;

import java.util.List;

import io.github.lazyimmortal.sesame.util.Log;
import io.github.lazyimmortal.sesame.util.StringUtil;

public interface AnswerAIInterface {

    /**
     * 获取AI回答结果
     *
     * @param text 问题内容
     * @return AI回答结果
     */
    String getAnswerStr(String text);

    /**
     * 获取答案
     *
     * @param title      问题
     * @param answerList 答案集合
     * @return 空没有获取到
     */
    default int getAnswer(String title, List<String> answerList) {
        StringBuilder answerStr = new StringBuilder();
        for (String answer : answerList) {
            answerStr.append("[").append(answer).append("]");
        }
        String answerResult = getAnswerStr(title + "\n" + answerStr + "\n请只返回答案");
        if (!StringUtil.isEmpty(answerResult)) {
            Log.record(this.getClass().getSimpleName() + "🧠返回数据:" + answerResult);
            for (int i = 0; i < answerList.size(); i++) {
                if (answerResult.contains(answerList.get(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    static AnswerAIInterface getInstance() {
        return new AnswerAIInterface() {
            @Override
            public String getAnswerStr(String text) {
                return text; // 复读机
            }

            @Override
            public int getAnswer(String title, List<String> answerList) {
                return -1;
            }
        };
    }
}
