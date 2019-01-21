package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/24.
 */

public class UserOrderEntity implements Serializable{
    private String id;//1111e0a003f58ea75f9f0350",     预约订单主键ID
    private String conventionTime;//2015-02-09 16:30:00",     预约时间
    private String userId;//113a6d6f003f58ea70f7a6c6c7",     用户ID
    private String vehicleId;//743a6d6f003f58ea70f34c57",车辆ID
    private String orderState;//1",    预约单状态1待回访,2有看车意向,3无效订单,4已看车,5已买车,6看车后无意向,7客户取消
    private String vehicleImage;///2018-03-23/fhh7f76.jpg",     车辆主图
    private String mobile;//9768",     用户手机号码后4位
    private String vehicleFullName;//奥迪 A6L 2018款 TFSL 豪华型",     车辆名称全拼
    private String vehicleColor;//","白色",
    private String stype;//1",     供应商性质：1直营9其它
    private String enterpriseName;//上海捷利行",     企业名称
    private String enterpriseProvince;//上海",     所在省份
    private String enterpriseCity;//上海",     所在城市
    private String enterpriseAddress;//闵行区XXX路666号",     联系地址
    private String vehicleImagePath;//www.ymc.cn/2018-03-23/fhh7f76.jpg",  车辆主图全路径
    private String orderStateName;//","待回访", 预约单状态
    private String telphone;
    private String version;//车辆版本号
    private String shelvesStatus;//上架状态：0待上架（默认）1上架2下架
    private String isGoup;//是否集团所属机构：0-否，1-是

    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getVehicleColorName() {
        return vehicleColorName;
    }

    public void setVehicleColorName(String vehicleColorName) {
        this.vehicleColorName = vehicleColorName;
    }

    private String orderid;//订单id
    private String orderUserName;
    private String orderUserMobile;
    private String vehicleColorName;

    private String salesPrice;
    private String ratePrice;

    public String getRatePrice() {
        return ratePrice;
    }

    public void setRatePrice(String ratePrice) {
        this.ratePrice = ratePrice;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getOrderUserName() {
        return orderUserName;
    }

    public void setOrderUserName(String orderUserName) {
        this.orderUserName = orderUserName;
    }

    public String getOrderUserMobile() {
        return orderUserMobile;
    }

    public void setOrderUserMobile(String orderUserMobile) {
        this.orderUserMobile = orderUserMobile;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getShelvesStatus() {
        return shelvesStatus;
    }

    public void setShelvesStatus(String shelvesStatus) {
        this.shelvesStatus = shelvesStatus;
    }

    public String getIsGoup() {
        return isGoup;
    }

    public void setIsGoup(String isGoup) {
        this.isGoup = isGoup;
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

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
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

    public String getOrderStateName() {
        return orderStateName;
    }

    public void setOrderStateName(String orderStateName) {
        this.orderStateName = orderStateName;
    }

}
