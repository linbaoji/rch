package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/2.
 */

public class CarSeriesEntity implements Serializable{
    private String id;//5a7d0316bf38f9dd89f8dee0",    车系ID
    private String brandId;//asda",    品牌Id
    private String modelName;//aodi",    车系名称
    private String modelImage;///2018-03-24/2223.jpg",    车系图标
    private String remark;//备注",    备注
    private Boolean identity;//标识-是否第一次显示字母  true-第一次,false 第N次
    private Boolean identityCount;//标识-字母一项下面横线是否已到最后一个
    private String brandName;//品牌名称



    public void setIdentityCount(Boolean identityCount) {
        this.identityCount = identityCount;
    }

    public void setIdentity(Boolean identity) {
        this.identity = identity;
    }

    public Boolean getIdentityCount() {
        return identityCount;
    }

    public Boolean getIdentity() {
        return identity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }


    @Override
    public String toString() {
        return id+","+brandId+","+modelName+","+modelImage+","+remark+","+identity+","+identityCount+","+brandName;
    }


}
