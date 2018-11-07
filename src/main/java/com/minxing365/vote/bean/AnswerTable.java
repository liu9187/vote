package com.minxing365.vote.bean;

import org.springframework.stereotype.Component;

/**
 *答案表
 */
@Component
public class AnswerTable {
    //答案表ID
    private Integer id;
    //选项表ID
    private Integer optionId;
    //行员号
    private String optionTitle;
    //答题人
    private String answerUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public String getAnswerUserName() {
        return answerUserName;
    }

    public void setAnswerUserName(String answerUserName) {
        this.answerUserName = answerUserName;
    }
}
