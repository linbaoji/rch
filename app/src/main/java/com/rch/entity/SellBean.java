package com.rch.entity;

import java.util.List;

/**
 * Created by acer on 2018/8/19.
 */

public class SellBean {
    private String akey;
    private String ifDel;
    private List<String>aValue;

    public String getAkey() {
        return akey;
    }

    public void setAkey(String akey) {
        this.akey = akey;
    }

    public String getIfDel() {
        return ifDel;
    }

    public void setIfDel(String ifDel) {
        this.ifDel = ifDel;
    }

    public List<String> getaValue() {
        return aValue;
    }

    public void setaValue(List<String> aValue) {
        this.aValue = aValue;
    }
}
