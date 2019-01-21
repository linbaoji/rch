package com.rch.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/23.
 */

public class HistoryBean implements Parcelable {

    /**
     * id : 1111e0a003f58ea75f9f0350
     * conventionTime : 2015-02-09 16:30:00
     * userId : 113a6d6f003f58ea70f7a6c6c7
     * vehicleId : 743a6d6f003f58ea70f34c57
     * orderState : 1
     * vehicleImage : /2018-03-23/fhh7f76.jpg
     * mobile : 9768
     * vehicleFullName : 奥迪 A6L 2018款 TFSL 豪华型
     * stype : 1
     * enterpriseName : 上海捷利行
     * enterpriseProvince : 上海
     * enterpriseCity : 上海
     * enterpriseAddress : 闵行区XXX路666号
     * vehicleImagePath : www.ymc.cn/2018-03-23/fhh7f76.jpg
     */

    private String id;
    private String conventionTime;
    private String userId;
    private String vehicleId;
    private String orderState;
    private String vehicleImage;
    private String mobile;
    private String vehicleFullName;
    private String vehicleColor;
    private String stype;
    private String enterpriseName;
    private String enterpriseProvince;
    private String enterpriseCity;
    private String enterpriseAddress;
    private String vehicleImagePath;
    private String telphone;
    private String orderStateName;

    public String getOrderStateName() {
        return orderStateName;
    }

    public void setOrderStateName(String orderStateName) {
        this.orderStateName = orderStateName;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConventionTime() {
        return conventionTime;
    }

    public void setConventionTime(String conventionTime) {
        this.conventionTime = conventionTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVehicleFullName() {
        return vehicleFullName;
    }

    public void setVehicleFullName(String vehicleFullName) {
        this.vehicleFullName = vehicleFullName;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseProvince() {
        return enterpriseProvince;
    }

    public void setEnterpriseProvince(String enterpriseProvince) {
        this.enterpriseProvince = enterpriseProvince;
    }

    public String getEnterpriseCity() {
        return enterpriseCity;
    }

    public void setEnterpriseCity(String enterpriseCity) {
        this.enterpriseCity = enterpriseCity;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }

    public String getVehicleImagePath() {
        return vehicleImagePath;
    }

    public void setVehicleImagePath(String vehicleImagePath) {
        this.vehicleImagePath = vehicleImagePath;
    }


    public HistoryBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.conventionTime);
        dest.writeString(this.userId);
        dest.writeString(this.vehicleId);
        dest.writeString(this.orderState);
        dest.writeString(this.vehicleImage);
        dest.writeString(this.mobile);
        dest.writeString(this.vehicleFullName);
        dest.writeString(this.vehicleColor);
        dest.writeString(this.stype);
        dest.writeString(this.enterpriseName);
        dest.writeString(this.enterpriseProvince);
        dest.writeString(this.enterpriseCity);
        dest.writeString(this.enterpriseAddress);
        dest.writeString(this.vehicleImagePath);
        dest.writeString(this.telphone);
        dest.writeString(this.orderStateName);
    }

    protected HistoryBean(Parcel in) {
        this.id = in.readString();
        this.conventionTime = in.readString();
        this.userId = in.readString();
        this.vehicleId = in.readString();
        this.orderState = in.readString();
        this.vehicleImage = in.readString();
        this.mobile = in.readString();
        this.vehicleFullName = in.readString();
        this.vehicleColor = in.readString();
        this.stype = in.readString();
        this.enterpriseName = in.readString();
        this.enterpriseProvince = in.readString();
        this.enterpriseCity = in.readString();
        this.enterpriseAddress = in.readString();
        this.vehicleImagePath = in.readString();
        this.telphone = in.readString();
        this.orderStateName = in.readString();
    }

    public static final Parcelable.Creator<HistoryBean> CREATOR = new Parcelable.Creator<HistoryBean>() {
        @Override
        public HistoryBean createFromParcel(Parcel source) {
            return new HistoryBean(source);
        }

        @Override
        public HistoryBean[] newArray(int size) {
            return new HistoryBean[size];
        }
    };
}
