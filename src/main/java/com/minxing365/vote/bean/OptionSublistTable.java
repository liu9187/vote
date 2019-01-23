package com.minxing365.vote.bean;

/**
 * 选择表字表
 */
public class OptionSublistTable {
    //字表id
    private Integer id;
    //选择表id
    private Integer optionId;
    //图片url
    private String pictureUrl;
    //视频url
    private  String viewUrl;
    //说明
    private String sublistTitle;
    //状态 1未删除 0已删除
    private Integer stateSublist;
    //备注
    private String remarks;

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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String getSublistTitle() {
        return sublistTitle;
    }

    public void setSublistTitle(String sublistTitle) {
        this.sublistTitle = sublistTitle;
    }

    public Integer getStateSublist() {
        return stateSublist;
    }

    public void setStateSublist(Integer stateSublist) {
        this.stateSublist = stateSublist;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
