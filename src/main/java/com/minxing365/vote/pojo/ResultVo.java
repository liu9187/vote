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
    /**
     * 信息收集数据结果
     */
    private Integer value;
    //获取部门
    private  String department;
    //获取审核标准
    private  String auditStandard;
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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAuditStandard() {
        return auditStandard;
    }

    public void setAuditStandard(String auditStandard) {
        this.auditStandard = auditStandard;
    }
}
