package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/9.
 */

public class CertifiedEntity implements Serializable{
    private String authenticateType;//1",  认证类型 1-企业，2-个人
    private String enterpriseName;//XX公司",  企业名称   企业认证信息
    private String enterpriseUrl;///2018-05-08/ahfj.jpg",     营业执照照片   企业认证信息
    private String enterpriseProvince;//上海",     企业所在省份   企业认证信息
    private String enterpriseCity;//上海",     企业所在城市   企业认证信息
    private String enterpriseAddress;//梅园路228号",     企业联系地址   企业认证信息
    private String userName;//",     客户姓名   个人认证信息
    private String userSex;//",     客户性别  个人认证信息
    private String licenseNo;//",     证件号码  个人认证信息
    private String province;//",     所在省份  个人认证信息
    private String city;//",     所在城市  个人认证信息
    private String address;//",     联系地址  个人认证信息
    private String licenseUrl;//",     证件正面照片  个人认证信息
    private String licenseBackUrl;//",     证件背面照片  个人认证信息
    private String enterpriseUrlPath;//http://www.rch.cn/ahfj.jpg",     营业执照照片 全路径
    private String licenseUrlPath;//",     证件正面照片 全路径
    private String licenseBackUrlPath;//":"",     证件背面照片 全路径
    private String enterpriseNo;//统一社会信用代码 企业认证信息
    private String enterpriseBusiRole;//认证企业角色  1-车商，2-代理商  企业认证信息
    private String legalName;
    private String legalSex;
    private String contacts;
    private String contactsPhone;
    private String userLicenseNo;
    private String enterpriseProvinceName;
    private String enterpriseCityName;

    public CertifiedEntity() {

    }

    public String getEnterpriseProvinceName() {
        return enterpriseProvinceName;
    }

    public void setEnterpriseProvinceName(String enterpriseProvinceName) {
        this.enterpriseProvinceName = enterpriseProvinceName;
    }

    public String getEnterpriseCityName() {
        return enterpriseCityName;
    }

    public void setEnterpriseCityName(String enterpriseCityName) {
        this.enterpriseCityName = enterpriseCityName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getUserLicenseNo() {
        return userLicenseNo;
    }

    public void setUserLicenseNo(String userLicenseNo) {
        this.userLicenseNo = userLicenseNo;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalSex() {
        return legalSex;
    }

    public void setLegalSex(String legalSex) {
        this.legalSex = legalSex;
    }

    public String getEnterpriseBusiRole() {
        return enterpriseBusiRole;
    }

    public void setEnterpriseBusiRole(String enterpriseBusiRole) {
        this.enterpriseBusiRole = enterpriseBusiRole;
    }

    public String getEnterpriseNo() {
        return enterpriseNo;
    }

    public void setEnterpriseNo(String enterpriseNo) {
        this.enterpriseNo = enterpriseNo;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    public void setLicenseUrlPath(String licenseUrlPath) {
        this.licenseUrlPath = licenseUrlPath;
    }

    public void setLicenseBackUrlPath(String licenseBackUrlPath) {
        this.licenseBackUrlPath = licenseBackUrlPath;
    }

    public void setLicenseBackUrl(String licenseBackUrl) {
        this.licenseBackUrl = licenseBackUrl;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setAuthenticateType(String authenticateType) {
        this.authenticateType = authenticateType;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }

    public void setEnterpriseCity(String enterpriseCity) {
        this.enterpriseCity = enterpriseCity;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public void setEnterpriseProvince(String enterpriseProvince) {
        this.enterpriseProvince = enterpriseProvince;
    }

    public void setEnterpriseUrl(String enterpriseUrl) {
        this.enterpriseUrl = enterpriseUrl;
    }

    public void setEnterpriseUrlPath(String enterpriseUrlPath) {
        this.enterpriseUrlPath = enterpriseUrlPath;
    }

    public String getProvince() {
        return province;
    }

    public String getLicenseUrlPath() {
        return licenseUrlPath;
    }

    public String getLicenseUrl() {
        return licenseUrl;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public String getLicenseBackUrlPath() {
        return licenseBackUrlPath;
    }

    public String getLicenseBackUrl() {
        return licenseBackUrl;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getUserSex() {
        return userSex;
    }

    public String getUserName() {
        return userName;
    }

    public String getAuthenticateType() {
        return authenticateType;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public String getEnterpriseCity() {
        return enterpriseCity;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getEnterpriseProvince() {
        return enterpriseProvince;
    }

    public String getEnterpriseUrl() {
        return enterpriseUrl;
    }

    public String getEnterpriseUrlPath() {
        return enterpriseUrlPath;
    }

}
