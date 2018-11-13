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
           Integer size;
           if (list.size()>=pageSize){
               size=pageSize;
           }else {
               size=list.size();
           }
        List<T> newList=new ArrayList<>();
        for (int i=0;i<size;i++){
            //索引id
            Integer index=pageNum-1;
               newList.add(list.get(index));
        }
        return newList;
    }
    //获取总条数
    public Integer getTotal() {
           if (null !=list && list.size()>0){
               total=list.size();
           }
        return total;
    }
    //获取总页数
    public Integer getPages() {
           if (null !=list && list.size()>0){
               pages=list.size()/pageSize;
           }
        return pages;
    }

}
