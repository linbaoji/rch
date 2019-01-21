package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/10/24.
 */

public class PicListBean implements Serializable{
    private String id;// "id"：" sdfsdfsf"
    private String vehicleId;//"vehicleId"：" sdfsdfsf"
    private String vehicleImage;//"vehicleImage"：" http//:www.rch.cn/2018-03-23/fhh7f76.jpg "
    private String imageOri;//"imageOri":" 11  方位
    private String sort;//"sort":" 1    排序
    private String vehicleImagePath;//"vehicleImagePath":" http//:www.rch.cn/2018-03-23/fhh7f76.jpg "
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PicListBean{" +
                "id='" + id + '\'' +
                ", vehicleImage='" + vehicleImage + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
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

    public String getVehicleImagePath() {
        return vehicleImagePath;
    }

    public void setVehicleImagePath(String vehicleImagePath) {
        this.vehicleImagePath = vehicleImagePath;
    }
}
