package com.minxing365.vote.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页工具类
 */
public class PageUtils<T> {
    //起始页
    private Integer pageNum;
    //每页显示条数
    private Integer pageSize;
    //需要分页的list
    private List<T> list;
    //总条数
    private Integer total;
    //总页数
    private Integer pages;

    public PageUtils(Integer pageNum, Integer pageSize, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.list = list;
        this.total = list.size();
        this.pages = (total + pageSize - 1) / pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    //获取分页后的list
    public List<T> getList() {
        List<T> newList = this.list.subList(pageSize * (pageNum - 1), (pageNum * pageSize) > total ? total : (pageNum * pageSize));
        return newList;
    }

    //获取总条数
    public Integer getTotal() {
        return total;
    }

    //获取总页数
    public Integer getPages() {
        return pages;
    }


}
