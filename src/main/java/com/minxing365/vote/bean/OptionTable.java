package com.minxing365.vote.bean;

import org.springframework.stereotype.Component;

/**
 * 选项表
 */
@Component
public class OptionTable {
    //选项表ID
    private Integer  id;
    //主表ID
    private String voteId;
    //主题
    private String optionTitle;
    //图片地址
    private String pictureUrl;
    //视频地址
    private String viewUrl;
    //选项类型(文本，图片，视频)
    private Integer optionFlag;
    //备注
    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
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

    public Integer getOptionFlag() {
        return optionFlag;
    }

    public void setOptionFlag(Integer optionFlag) {
        this.optionFlag = optionFlag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
