package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/16.
 */

public class BrandEntity implements Serializable{

    private String id;//5a7d0316bf38f9dd89f8dee0",    品牌主键ID
    private String brandName;//奥迪",    品牌名称
    private String brandImage;///2018-03-24/2223.jpg",    品牌图标
    private String brandImagePath;//"http://www.r.cn/2018-03-24/2223.jpg",    品牌图标
    private String remark;//备注",    备注
    private String firstLetter;//A"  开头字母
    private String brandPy;//:"aodi";//    品牌拼音全拼
    private Boolean identity;//标识-是否第一次显示字母  true-第一次,false 第N次
    private Boolean identityCount;//标识-字母一项下面横线是否已到最后一个

    private String spellInit;
    private String spellFullinit;
    private String spellFull;
    private String brandLogo;

    public String getSpellInit() {
        return spellInit;
    }

    public void setSpellInit(String spellInit) {
        this.spellInit = spellInit;
    }

    public String getSpellFullinit() {
        return spellFullinit;
    }

    public void setSpellFullinit(String spellFullinit) {
        this.spellFullinit = spellFullinit;
    }

    public String getSpellFull() {
        return spellFull;
    }

    public void setSpellFull(String spellFull) {
        this.spellFull = spellFull;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    public void setBrandImagePath(String brandImagePath) {
        this.brandImagePath = brandImagePath;
    }

    public String getBrandImagePath() {
        return brandImagePath;
    }

    public void setBrandPy(String brandPy) {
        this.brandPy = brandPy;
    }

    public String getBrandPy() {
        return brandPy;
    }

    public void setIdentityCount(Boolean identityCount) {
        this.identityCount = identityCount;
    }

    public Boolean getIdentityCount() {
        return identityCount;
    }

    public void setIdentity(Boolean identity) {
        this.identity = identity;
    }

    public Boolean getIdentity() {
        return identity;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*public void setBrandType(String brandType) {
        this.brandType = brandType;
    }*/

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getRemark() {
        return remark;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public String getId() {
        return id;
    }

  /*  public String getBrandType() {
        return brandType;
    }*/

    public String getFirstLetter() {
        return firstLetter;
    }
}
