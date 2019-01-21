package com.rch.entity;

/**
 * Created by Administrator on 2018/5/10.
 */

public class ModelEntity {
    private String id;//":"5a741a7e75a308e6cdfa14a3",  车型主键ID
    private String modelName;//":" MODEL S "   车型名称

    public void setId(String id) {
        this.id = id;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getId() {
        return id;
    }

    public String getModelName() {
        return modelName;
    }
}
