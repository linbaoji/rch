package com.rch.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by acer on 2018/8/9.
 */

public class OwnerBean implements Serializable{

    /**
     * id : 5b5ed64dd2ef8ea7e697e581
     * purchaseId : 20180730171141247
     * userId : 5b3b4b40f50fb0e4c669a310
     * userName :
     * userMobile : 13816002214
     * mobile : 13816002214
     * ptype : 1
     * ptypeName : 帮人卖车
     * title :
     * sex : 3
     * brandId : 7
     * brandName : 本田
     * modelId : 318
     * modelName : 里程
     * registrationProv : 北京市
     * registrationCity : 北京市
     * registrationTime : 2018-07-30 00:00:00
     * showMileage : 180000
     * showMileageView : 18.0
     * hopingPrice : 470000.0
     * hopingPriceView : 47.0
     * imageUrl : http://files.ubuyche.com/ubuyche-store/files/2018-07-30/1532940706204_157618.jpg
     * status : 1
     * statusName : 客服审核中
     * ifDel : 0
     * createUser : 000000000000000000000000
     * createTime : 2018-07-30 17:11:41
     * updateUser : 1
     * updateTime : 2018-08-01 16:15:31
     * validCode :
     * type :
     * enterpriseId :
     * auitList :
     * addIdentity :
     * addSource :
     * openId :
     * vehicleFullName : 本田 里程
     */

    private String id;
    private String purchaseId;
    private String userId;
    private String userName;
    private String userMobile;
    private String mobile;
    private String ptype;
    private String ptypeName;
    private String title;
    private String sex;
    private String brandId;
    private String brandName;
    private String modelId;
    private String modelName;
    private String registrationProv;
    private String registrationCity;
    private String registrationTime;
    private String showMileage;
    private double showMileageView;
    private double hopingPrice;
    private double hopingPriceView;
    private String imageUrl;
    private String status;
    private String statusName;
    private String ifDel;
    private String createUser;
    private String createTime;
    private String updateUser;
    private String updateTime;
    private String validCode;
    private String type;
    private String enterpriseId;
    private String auitList;
    private String addIdentity;
    private String addSource;
    private String openId;
    private String vehicleFullName;
    private List<AuitListBean>logList;

    public List<AuitListBean> getLogList() {
        return logList;
    }

    public void setLogList(List<AuitListBean> logList) {
        this.logList = logList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getPtypeName() {
        return ptypeName;
    }

    public void setPtypeName(String ptypeName) {
        this.ptypeName = ptypeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getRegistrationProv() {
        return registrationProv;
    }

    public void setRegistrationProv(String registrationProv) {
        this.registrationProv = registrationProv;
    }

    public String getRegistrationCity() {
        return registrationCity;
    }

    public void setRegistrationCity(String registrationCity) {
        this.registrationCity = registrationCity;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getShowMileage() {
        return showMileage;
    }

    public void setShowMileage(String showMileage) {
        this.showMileage = showMileage;
    }

    public double getShowMileageView() {
        return showMileageView;
    }

    public void setShowMileageView(double showMileageView) {
        this.showMileageView = showMileageView;
    }

    public double getHopingPrice() {
        return hopingPrice;
    }

    public void setHopingPrice(double hopingPrice) {
        this.hopingPrice = hopingPrice;
    }

    public double getHopingPriceView() {
        return hopingPriceView;
    }

    public void setHopingPriceView(double hopingPriceView) {
        this.hopingPriceView = hopingPriceView;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getIfDel() {
        return ifDel;
    }

    public void setIfDel(String ifDel) {
        this.ifDel = ifDel;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getAuitList() {
        return auitList;
    }

    public void setAuitList(String auitList) {
        this.auitList = auitList;
    }

    public String getAddIdentity() {
        return addIdentity;
    }

    public void setAddIdentity(String addIdentity) {
        this.addIdentity = addIdentity;
    }

    public String getAddSource() {
        return addSource;
    }

    public void setAddSource(String addSource) {
        this.addSource = addSource;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getVehicleFullName() {
        return vehicleFullName;
    }

    public void setVehicleFullName(String vehicleFullName) {
        this.vehicleFullName = vehicleFullName;
    }
}
