package com.minxing365.vote.pojo;

import java.util.List;

/**
 * 查询页面统计表格
 */
public class VoteCount {
    //主标题目
  private   String vote;
    //描述
    private  String describes;
    // 备注
    private String remarks;
   //选择列表
  private List<AnswerCount> list;

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getDescribes() {
        return describes;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<AnswerCount> getList() {
        return list;
    }

    public void setList(List<AnswerCount> list) {
        this.list = list;
    }
}
