package com.rch.entity;

import java.util.Collection;

/**
 * Created by Administrator on 2018/3/27.
 */

public class LableEntity {
    String id;
    String lable;
    String type;

    public void setLable(String lable) {
        this.lable = lable;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLable() {
        return lable;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        LableEntity lableEntity=(LableEntity)obj;
        return this.type.equals(lableEntity.type);
    }
}
