package com.minxing365.vote.pojo.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

public class CountExcel implements Serializable {
    @Excel(name = "公司部门")
    private String department;
    @Excel(name = "职能发挥")
    private Integer v1;
    @Excel(name = "工作效率")
    private Integer v2;
    @Excel(name = "服务质量")
    private Integer v3;
    @Excel(name = "服务态度")
    private Integer v4;
    @Excel(name = "人数")
    private Integer number;

    public CountExcel(String department, Integer v1, Integer v2, Integer v3, Integer v4, Integer number) {
        this.department = department;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
        this.number = number;
    }

    public CountExcel() {
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getV1() {
        return v1;
    }

    public void setV1(Integer v1) {
        this.v1 = v1;
    }

    public Integer getV2() {
        return v2;
    }

    public void setV2(Integer v2) {
        this.v2 = v2;
    }

    public Integer getV3() {
        return v3;
    }

    public void setV3(Integer v3) {
        this.v3 = v3;
    }

    public Integer getV4() {
        return v4;
    }

    public void setV4(Integer v4) {
        this.v4 = v4;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
