package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/21.
 */

public class SupplierEntity implements Serializable{
    private String stype;//1",     供应商性质：1直营9其它
    private String enterpriseName;//上海捷利行",     企业名称
    private String enterpriseProvince;//上海",     所在省份
    private String enterpriseCity;//上海",     所在城市
    private String enterpriseAddress;//闵行区XXX路666号",     联系地址
    private String telphone;//400 666 8888" 座机号码
    private String isGoup;//供应商性质：1直营9其它（数据库表字段删除，用现字段isGoup代替）

    public String getIsGoup() {
        return isGoup;
    }

    public void setIsGoup(String isGoup) {
        this.isGoup = isGoup;
    }

    public void setEnterpriseProvince(String enterpriseProvince) {
        this.enterpriseProvince = enterpriseProvince;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public void setEnterpriseCity(String enterpriseCity) {
        this.enterpriseCity = enterpriseCity;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getEnterpriseProvince() {
        return enterpriseProvince;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getEnterpriseCity() {
        return enterpriseCity;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public String getStype() {
        return stype;
    }

    public String getTelphone() {
        return telphone;
    }
}
