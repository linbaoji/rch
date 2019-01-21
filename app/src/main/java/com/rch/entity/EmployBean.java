package com.rch.entity;

import java.io.Serializable;

/**
 * Created by acer on 2018/8/8.
 */

public class EmployBean implements Serializable{

//        "id":"1111e0a003f58ea75f9f0350", 主键，ID
//        " enterpriseId":"5a741a7e75a308f2d23d144",	所属企业ID
//        " isEntAdmin":"1",	是否为管理员
//        "ptype":”1”,	车型类型
//        " enterpriseName":"自己卖车", 企业名称
//        " userName":"象牙白",	用户名称
//        " userSex":"1",	性别
//        " licenseType":" ",  证件类型
//        " licenseNo":"2",	 证件号码
//        " birthday":"5",	出生日期
//        " province":"1",	省份
//        " city":"2",	  城市
//        " address",2,	地址
//        " licenseUrl":" 证件正面照
//        " licenseBackUrl":" 证件背面照
//
//        " mobile":" 手机号
//        " email":"电子邮箱
//        " sourceName":" 注册方式名称
//        " legalSexName":" 性别名称
//        " licenseTypeName":" 证件类型名称
//        " auditState":" 审核状态
//        " auditStateName":" 审核状态名称

    private String id;
    private String enterpriseId;
    private String isAdmin;
    private String ptype;
    private String enterpriseName;
    private String userName;
    private String userSex;
    private String licenseType;
    private String licenseNo;
    private String birthday;
    private String province;
    private String city;
    private String address;
    private String licenseUrl;
    private String licenseBackUrl;
    private String mobile;
    private String email;
    private String sourceName;
    private String legalSexName;
    private String licenseTypeName;
    private String auditState;
    private String auditStateName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public String getLicenseBackUrl() {
        return licenseBackUrl;
    }

    public void setLicenseBackUrl(String licenseBackUrl) {
        this.licenseBackUrl = licenseBackUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getLegalSexName() {
        return legalSexName;
    }

    public void setLegalSexName(String legalSexName) {
        this.legalSexName = legalSexName;
    }

    public String getLicenseTypeName() {
        return licenseTypeName;
    }

    public void setLicenseTypeName(String licenseTypeName) {
        this.licenseTypeName = licenseTypeName;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getAuditStateName() {
        return auditStateName;
    }

    public void setAuditStateName(String auditStateName) {
        this.auditStateName = auditStateName;
    }

    @Override
    public String toString() {
        return "EmployBean{" +
                "userName='" + userName + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}


