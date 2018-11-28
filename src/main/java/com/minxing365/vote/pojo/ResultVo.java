package com.minxing365.vote.pojo;

public class ResultVo {
    /**
     * id
     */
    private Integer id;
    /**
     * apps表id
     */
    private Integer appId;
    /**
     * sur_in  userId
     */
    private  String userId;
    private String label;
    private String tip;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
