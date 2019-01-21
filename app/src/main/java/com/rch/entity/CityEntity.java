package com.rch.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class CityEntity implements Serializable{
    private String chatLable;
    private List<CityInfoEntity> list;

    public void setList(List<CityInfoEntity> list) {
        this.list = list;
    }

    public void setChatLable(String chatLable) {
        this.chatLable = chatLable;
    }

    public List<CityInfoEntity> getList() {
        return list;
    }

    public String getChatLable() {
        return chatLable;
    }
}
