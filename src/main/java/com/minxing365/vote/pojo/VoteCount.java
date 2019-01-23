package com.minxing365.vote.pojo;

import com.minxing365.vote.bean.OptionSublistTable;

import java.util.List;

/**
 * 查询页面统计表格
 */
public class VoteCount {
    //主标题目
    private String vote;
    //描述
    private String describes;
    // 备注
    private String remarks;
    //总条数
    private Integer total;
    //总页数
    private Integer pages;
    //选择列表
    private List<AnswerCount> listOption;
    //选择表子表
    private List<OptionSublistTable> listSublist;

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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<AnswerCount> getListOption() {
        return listOption;
    }

    public void setListOption(List<AnswerCount> listOption) {
        this.listOption = listOption;
    }

    public List<OptionSublistTable> getListSublist() {
        return listSublist;
    }

    public void setListSublist(List<OptionSublistTable> listSublist) {
        this.listSublist = listSublist;
    }
}
