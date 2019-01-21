package com.rch.entity;

/**
 * Created by Administrator on 2018/5/21.
 */

public class ReserveOrderEntity {
    private String id;//1111e0a003f58ea75f9f0350",     预约订单主键ID
    private String conventionTime;//2015-02-09 16:30:00",     预约时间
    private String userId;//113a6d6f003f58ea70f7a6c6c7",     用户ID
    private String vehicleId;//743a6d6f003f58ea70f34c57",车辆ID
    private String orderState;//1",     1待回访,2有看车意向,3无效订单,4已看车,5已买车,6看车后无意向,7客户取消
    private String vehicleImage;///2018-03-23/fhh7f76.jpg",     车辆主图
    private String mobile;//9768",     用户手机号码后4位
    private String vehicleFullName;//奥迪 A6L 2018款 TFSL 豪华型",     车辆名称全拼
    private String stype;//1",     供应商性质：1直营9其它
    private String enterpriseName;//上海捷利行",     企业名称
    private String enterpriseProvince;//上海",     所在省份
    private String enterpriseCity;//上海",     所在城市
    private String enterpriseAddress;//闵行区XXX路666号",     联系地址

    public void setStype(String stype) {
        this.stype = stype;
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

    public void setId(String id) {
        this.id = id;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setConventionTime(String conventionTime) {
        this.conventionTime = conventionTime;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVehicleFullName(String vehicleFullName) {
        this.vehicleFullName = vehicleFullName;
    }

    public String getStype() {
        return stype;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public String getEnterpriseCity() {
        return enterpriseCity;
    }

    public String getId() {
        return id;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getEnterpriseProvince() {
        return enterpriseProvince;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getConventionTime() {
        return conventionTime;
    }

    public String getOrderState() {
        return orderState;
    }

    public String getUserId() {
        return userId;
    }

    public String getVehicleFullName() {
        return vehicleFullName;
    }
}
