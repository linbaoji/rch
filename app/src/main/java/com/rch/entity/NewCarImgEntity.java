package com.rch.entity;

/**
 * Created by Administrator on 2018/11/7.
 */

public class NewCarImgEntity {
    private String type;
    private String vehicleImage;

    @Override
    public String toString() {
        return "NewCarImgEntity{" +
                "type='" + type + '\'' +
                ", vehicleImage='" + vehicleImage + '\'' +
                '}';
    }

    public NewCarImgEntity(String type, String vehicleImage) {
        this.type = type;
        this.vehicleImage = vehicleImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }
}
