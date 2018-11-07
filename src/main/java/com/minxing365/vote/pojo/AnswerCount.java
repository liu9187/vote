package com.minxing365.vote.pojo;

/**
 * 选项答案统计
 */
public class AnswerCount {
    //选择表id
    private  Integer optionId;
    //选择表题目
    private String optiontile;
    //获取答案总条数
    private Integer count;

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getOptiontile() {
        return optiontile;
    }

    public void setOptiontile(String optiontile) {
        this.optiontile = optiontile;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
