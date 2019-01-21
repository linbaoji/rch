package com.rch.entity;

/**
 * Created by Administrator on 2018/7/30.
 */

public class ProblemEntity {
    private String paramCode;//":"sys_buy_answer",	code
    private String paramName;//":”1”,	排序
    private String paramValue;//":"我多久可以把车卖出去",   问题
    private String paramDesc;//":"根据你卖车的时间决定"        答案
    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }


}
