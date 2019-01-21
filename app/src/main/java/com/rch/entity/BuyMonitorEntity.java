package com.rch.entity;

/**
 * Created by Administrator on 2018/6/6.
 */

public class BuyMonitorEntity {
    String userName;//用户名
    String userPhone;//用户手机
    String carShopName;//商品名
    String eventName;//事件名
    String desc;//描述

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCarShopName() {
        return carShopName;
    }

    public void setCarShopName(String carShopName) {
        this.carShopName = carShopName;
    }

}
