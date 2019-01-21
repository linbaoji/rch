package com.rch.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/1.
 */

public class HomeDateBean implements Serializable{
    private String remindType;
    private String remindDesc;
    private String hxUrl;
    private List<String>orderList;
    private List<BannerEntity>bannerAList;//轮播图
    private List<BannerEntity>bannerBList;//轮播图 广告位01
    private List<BannerEntity>bannerCList;//轮播图 广告位02
    private List<CarEntity>newVehicleList;
    private List<CarEntity>oldVehicleList;



    private List<CarEntity>starNewVehicleList;
    private List<CarEntity>starOldVehicleList;


    public HomeDateBean() {

    }



    public List<BannerEntity> getBannerBList() {
        return bannerBList;
    }

    public void setBannerBList(List<BannerEntity> bannerBList) {
        this.bannerBList = bannerBList;
    }

    public List<BannerEntity> getBannerCList() {
        return bannerCList;
    }

    public void setBannerCList(List<BannerEntity> bannerCList) {
        this.bannerCList = bannerCList;
    }

    public List<CarEntity> getStarNewVehicleList() {
        return starNewVehicleList;
    }

    public void setStarNewVehicleList(List<CarEntity> starNewVehicleList) {
        this.starNewVehicleList = starNewVehicleList;
    }

    public List<CarEntity> getStarOldVehicleList() {
        return starOldVehicleList;
    }

    public void setStarOldVehicleList(List<CarEntity> starOldVehicleList) {
        this.starOldVehicleList = starOldVehicleList;
    }

    public String getRemindType() {
        return remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }

    public String getRemindDesc() {
        return remindDesc;
    }

    public void setRemindDesc(String remindDesc) {
        this.remindDesc = remindDesc;
    }

    public String getHxUrl() {
        return hxUrl;
    }

    public void setHxUrl(String hxUrl) {
        this.hxUrl = hxUrl;
    }

    public List<String> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<String> orderList) {
        this.orderList = orderList;
    }

    public List<BannerEntity> getBannerAList() {
        return bannerAList;
    }

    public void setBannerAList(List<BannerEntity> bannerList) {
        this.bannerAList = bannerList;
    }

    public List<CarEntity> getNewVehicleList() {
        return newVehicleList;
    }

    public void setNewVehicleList(List<CarEntity> newVehicleList) {
        this.newVehicleList = newVehicleList;
    }

    public List<CarEntity> getOldVehicleList() {
        return oldVehicleList;
    }

    public void setOldVehicleList(List<CarEntity> oldVehicleList) {
        this.oldVehicleList = oldVehicleList;
    }

    public class BannerEntity{

        /**
         * id : 5be3fbc6d1b50e2dd75eA001
         * position : A
         * porder : 00
         * imageurl : http://files.ubuyche.com/banner01.jpg
         * noteurl :
         */

        private String id;
        private String position;
        private String porder;
        private String imageurl;
        private String noteurl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getPorder() {
            return porder;
        }

        public void setPorder(String porder) {
            this.porder = porder;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public String getNoteurl() {
            return noteurl;
        }

        public void setNoteurl(String noteurl) {
            this.noteurl = noteurl;
        }
    }


}
