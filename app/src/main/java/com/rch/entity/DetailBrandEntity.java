package com.rch.entity;

/**
 * Created by Administrator on 2018/5/10.
 */

public class DetailBrandEntity {
    private String id;//5a741a7e75a308e6cdfa14a3",  品牌主键ID
    private String brandName;//特斯拉",   品牌名称
    private String brandPy;//tesila ", 	品牌名称拼音全拼
    private String brandImage;//":" /2018-03-23/fhh7f76.jpg "  品牌图片

    public void setId(String id) {
        this.id = id;
    }

    public void setBrandPy(String brandPy) {
        this.brandPy = brandPy;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public String getId() {
        return id;
    }

    public String getBrandPy() {
        return brandPy;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getBrandImage() {
        return brandImage;
    }
}
