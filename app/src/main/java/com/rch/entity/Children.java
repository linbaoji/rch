package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/6.
 */

public class Children implements Serializable{
    private String key;
    private String value;

    public Children(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Children{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}