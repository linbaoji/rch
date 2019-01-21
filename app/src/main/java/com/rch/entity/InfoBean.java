package com.rch.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/5/22.
 */

public class InfoBean implements Parcelable {

    private String userAgentType;  //用户类型 1-企业；2-个人
    private String userName;
    private String userSex;
    private String birthday;
    private String licenseNo;
    private String address;
    private String enterpriseName;
    private String enterpriseNo;
    private String mobile;
    private String email;
    private String telphone;
    private String enterpriseAddress;
    private String userType;

    private String toDetail;
    private String contacts;
    private String contactsPhone;
    private String realName;

    public String getToDetail() {
        return toDetail;
    }

    public void setToDetail(String toDetail) {
        this.toDetail = toDetail;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserAgentType() {
        return userAgentType;
    }

    public void setUserAgentType(String userAgentType) {
        this.userAgentType = userAgentType;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseNo() {
        return enterpriseNo;
    }

    public void setEnterpriseNo(String enterpriseNo) {
        this.enterpriseNo = enterpriseNo;
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

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }


    public InfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userAgentType);
        dest.writeString(this.userName);
        dest.writeString(this.userSex);
        dest.writeString(this.birthday);
        dest.writeString(this.licenseNo);
        dest.writeString(this.address);
        dest.writeString(this.enterpriseName);
        dest.writeString(this.enterpriseNo);
        dest.writeString(this.mobile);
        dest.writeString(this.email);
        dest.writeString(this.telphone);
        dest.writeString(this.enterpriseAddress);
    }

    protected InfoBean(Parcel in) {
        this.userAgentType = in.readString();
        this.userName = in.readString();
        this.userSex = in.readString();
        this.birthday = in.readString();
        this.licenseNo = in.readString();
        this.address = in.readString();
        this.enterpriseName = in.readString();
        this.enterpriseNo = in.readString();
        this.mobile = in.readString();
        this.email = in.readString();
        this.telphone = in.readString();
        this.enterpriseAddress = in.readString();
    }

    public static final Creator<InfoBean> CREATOR = new Creator<InfoBean>() {
        @Override
        public InfoBean createFromParcel(Parcel source) {
            return new InfoBean(source);
        }

        @Override
        public InfoBean[] newArray(int size) {
            return new InfoBean[size];
        }
    };
}
