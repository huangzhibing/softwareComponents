package com.hqu.modules.purchasemanage.purplan.entity;

import java.io.Serializable;

public class Checker implements Serializable{

    private String name;
    private  String deptId;


    public Checker(String name, String deptId) {
        this.name = name;
        this.deptId=deptId;

    }

    public Checker() {

    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
