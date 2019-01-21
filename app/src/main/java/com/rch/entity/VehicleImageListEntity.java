package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/10.
 */

public class VehicleImageListEntity implements Serializable{
    private String id;//5a741a7e75a308e6cdfa14a3",  图片主键ID
    private String vehicleId;//5a741a7e75a308e6cdfa14a3",  商品主键ID
    private String vehicleImage;///2018-03-23/fhh7f76.jpg ", 	商品图片
    private String imageOri;//01",  图片方位
    private String sort;//":1,  排序
    private String remark;//备注",  备注
    private String ifDel;//1",  是否删除0-否（默认），1-是
    private String createTime;//2018-02-02 13:58:31",创建时间
    private String vehicleImagePath;//":" http:www.cs.cn/2018-03-23/fhh7f76.jpg "


    @Override
    public String toString() {
        return "VehicleImageListEntity{" +
                "id='" + id + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", vehicleImage='" + vehicleImage + '\'' +
                ", imageOri='" + imageOri + '\'' +
                ", sort='" + sort + '\'' +
                ", remark='" + remark + '\'' +
                ", ifDel='" + ifDel + '\'' +
                ", createTime='" + createTime + '\'' +
                ", vehicleImagePath='" + vehicleImagePath + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setIfDel(String ifDel) {
        this.ifDel = ifDel;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public void setVehicleImagePath(String vehicleImagePath) {
        this.vehicleImagePath = vehicleImagePath;
    }

    public void setImageOri(String imageOri) {
        this.imageOri = imageOri;
    }

    public String getId() {
        return id;
    }

    public String getRemark() {
        return remark;
    }

    public String getIfDel() {
        return ifDel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getSort() {
        return sort;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public String getVehicleImagePath() {
        return vehicleImagePath;
    }

    public String getImageOri() {
        return imageOri;
    }

}
