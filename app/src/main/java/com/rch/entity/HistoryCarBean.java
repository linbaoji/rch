package com.rch.entity;

import com.rch.custom.SortModel;

import java.io.Serializable;

/**
 * Created by acer on 2018/8/20.
 */

public class HistoryCarBean implements Serializable{
    private String name;
    private String brandId;
    private String brandImagePath;
    private String id;//5a7d0316bf38f9dd89f8dee0",    车系ID
    private String modelName;//aodi",    车系名称
    private String modelImage;///2018-03-24/2223.jpg",    车系图标


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandImagePath() {
        return brandImagePath;
    }

    public void setBrandImagePath(String brandImagePath) {
        this.brandImagePath = brandImagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelImage() {
        return modelImage;
    }

    public void setModelImage(String modelImage) {
        this.modelImage = modelImage;
    }
}
