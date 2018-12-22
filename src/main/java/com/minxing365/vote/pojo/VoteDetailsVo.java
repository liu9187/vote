package com.minxing365.vote.pojo;

import java.util.List;

/**
 * 投票详情对象
 */
public class VoteDetailsVo {
    /**
     * survey_forms id
     */
    private Integer id;
    /**
     * 待解析数据
     */
    private String body;
    /**
     * apps表id
     */
    private Integer appId;
    /**
     * sur_in  userId
     */
    private  String userId;
    /**
     * survey_name
     */
    private  String name;
    /**
     * survey_apps forms
     */
    private  String forms;
    private  String modify;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForms() {
        return forms;
    }

    public void setForms(String forms) {
        this.forms = forms;

    }
    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }
}
