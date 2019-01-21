package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/22.
 */

public class SearchEntity implements Serializable{
    private String id;//":"1111e0a003f58ea75f9f0350",      车辆主键ID
    private String vehicleFullName;//":"奥迪 A6L 2018款 TFSL 豪华型" 车辆全称
    private String brandId;
    private String seriesId;
    private String showName;
    private String orderStr;
    private String searchId;
    private String brandName;
    private String seriesName;


    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getOrderStr() {
        return orderStr;
    }

    public void setOrderStr(String orderStr) {
        this.orderStr = orderStr;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public void setVehicleFullName(String vehicleFullName) {
        this.vehicleFullName = vehicleFullName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleFullName() {
        return vehicleFullName;
    }

    public String getId() {
        return id;
    }
}
