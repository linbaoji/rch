package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/3.
 */

public class ImageBean implements Serializable{
    private String id;
    private String vehicleImage;
    private String imageOri;
    private String sort;
    private String remark;

    public String parentName;
    public long size;
    public String displayName;
    public String path;
    public boolean isChecked;
    public String imageUrl;
    public ImageBean() {
        super();
    }
    public ImageBean(String id, String vehicleImage, String imageOri, String sort, String remark) {
        this.id = id;
        this.vehicleImage = vehicleImage;
        this.imageOri = imageOri;
        this.sort = sort;
        this.remark = remark;
    }
    public ImageBean(String parentName, long size, String displayName, String path, boolean isChecked) {
        super();
        this.parentName = parentName;
        this.size = size;
        this.displayName = displayName;
        this.path = path;
        this.isChecked = isChecked;
    }
    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getImageOri() {
        return imageOri;
    }

    public void setImageOri(String imageOri) {
        this.imageOri = imageOri;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
