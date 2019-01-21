package com.rch.entity;

/**
 * Created by Administrator on 2018/8/8.
 */

public class OderListEntity {
    private String id;//5b398acd76bcb0e4e91bb17e",  订单主键ID
    private String userId;//5b398a9f76bcb0e4e71bb17e",  预约用户ID
    private String orderUserName;//张三",  预约用户名称
    private String orderUserMobile;//15180114174",  预约用户手机号码
    private String conventionTime;//2018-07-27 00:00:00"  预约看车时间
    private String orderState;//6",   预约单状态：1待回访  2客户到店看车3门店已确认看车  4客户已买车5门店确认失败6已取消
    private String distributorId;//",  分享人ID
    private String orginUserName;//",  分享人姓名
    private String vehicleId;//5b360de276bcb0e4591bb17e",  车辆主键ID
    private String vehicleFullName;//保时捷 Macan 2016款 Macan",  车辆名称全拼
    private String shelvesStatus;//1",  车辆上架状态：0待上架（默认）1已上架2已下架3已售罄
    private String auditState;//2",  车辆审核状态 0-待提交，1-待审核，2-审核通过，3-审核失败
    private String salesPrice;//":508000.00,  门店价
    private String priceshowtype;//":2,    价格显示类型   0-显示门店价，1-显示（门店价，批发价），2-显示（门店价，分销价）（暂时弃用）
    private String ratePrice;//":490000.00,  折扣价
    private String entUserId;//5b36f21876bcb0e4ba1bb17e",  供应商ID
    private String vehicleImage;//http://files.com/45538.jpg"  车辆主图
    private String orderStateName;//":"客户已看车"  订单审核状态-枚举名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getConventionTime() {
        return conventionTime;
    }

    public void setConventionTime(String conventionTime) {
        this.conventionTime = conventionTime;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(String distributorId) {
        this.distributorId = distributorId;
    }

    public String getOrginUserName() {
        return orginUserName;
    }

    public void setOrginUserName(String orginUserName) {
        this.orginUserName = orginUserName;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleFullName() {
        return vehicleFullName;
    }

    public void setVehicleFullName(String vehicleFullName) {
        this.vehicleFullName = vehicleFullName;
    }

    public String getShelvesStatus() {
        return shelvesStatus;
    }

    public void setShelvesStatus(String shelvesStatus) {
        this.shelvesStatus = shelvesStatus;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getPriceshowtype() {
        return priceshowtype;
    }

    public void setPriceshowtype(String priceshowtype) {
        this.priceshowtype = priceshowtype;
    }

    public String getRatePrice() {
        return ratePrice;
    }

    public void setRatePrice(String ratePrice) {
        this.ratePrice = ratePrice;
    }

    public String getEntUserId() {
        return entUserId;
    }

    public void setEntUserId(String entUserId) {
        this.entUserId = entUserId;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getOrderStateName() {
        return orderStateName;
    }

    public void setOrderStateName(String orderStateName) {
        this.orderStateName = orderStateName;
    }



}
