package com.minxing365.vote.bean;

import org.springframework.stereotype.Component;

/**
 * 投票主表
 */
@Component
public class VoteMainTable {
    //id

    private String id;
    //主题
    private String voteTitle;
    //创建人工号
    private String createUserNum;
    //创建人名称
    private String createUserName;
    private Long endTime;
    //状态
    private Integer state;
    //描述
    private  String describes;
    // 备注
    private String remarks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public String getCreateUserNum() {
        return createUserNum;
    }

    public void setCreateUserNum(String createUserNum) {
        this.createUserNum = createUserNum;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
}
