package com.s349967.oblig3s349967.model;


public class SortingStrategyModel {
    private Integer fieldNmb;
    private Integer isDesc;

    public  SortingStrategyModel(Integer fieldNmb, Integer isDesc){
        this.fieldNmb = fieldNmb;
        this.isDesc   = isDesc;
    }

    public Integer getFieldNmb(){
        return this.fieldNmb;
    }
    public Integer getIsDesc(){
        return this.isDesc;
    }

    public void setFieldNmb(Integer fieldNmb){
        this.fieldNmb = fieldNmb;
    }
    public  void setIsDesc(Integer isDesc){
        this.isDesc = isDesc;
    }
}
