package com.minxing365.vote.pojo;

import com.minxing365.vote.bean.OptionSublistTable;

import java.util.List;

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
    //图片地址
    private String pictureUrl;
    //视频地址
  //  private String viewUrl;
    //备注
    private String remarks;
    //判断登陆人可以投票 0 可以 其他数字不可以
    private Integer isVote;
    private String department;
    //选择表子类
    List<OptionSublistTable> listSublist;

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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

//    public String getViewUrl() {
//        return viewUrl;
//    }
//
//    public void setViewUrl(String viewUrl) {
//        this.viewUrl = viewUrl;
//    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getIsVote() {
        return isVote;
    }

    public void setIsVote(Integer isVote) {
        this.isVote = isVote;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<OptionSublistTable> getListSublist() {
        return listSublist;
    }

    public void setListSublist(List<OptionSublistTable> listSublist) {
        this.listSublist = listSublist;
    }
}
