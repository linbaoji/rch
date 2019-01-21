package com.rch.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/7/30.
 */

public class SellCatEntity {
    private String totalNum;//":"1222",卖出车辆个数
    List<ProblemEntity> list;

    public void setList(List<ProblemEntity> list) {
        this.list = list;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public List<ProblemEntity> getList() {
        return list;
    }

    public String getTotalNum() {
        return totalNum;
    }
}
