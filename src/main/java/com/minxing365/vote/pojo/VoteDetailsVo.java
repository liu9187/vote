package com.minxing365.vote.pojo;

import java.util.List;

/**
 * 投票详情对象
 */
public class VoteDetailsVo {
//    private boolean condition;
//    private String type;
//    private String name;
//    private String label;
//    private String tip;
//    private String bind;
//    private String componentId;
//    private String valueClassName;
//    private boolean valid;
//    private Restrict restrict;
//    private List<Data> data;
//    private String value;
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
}
