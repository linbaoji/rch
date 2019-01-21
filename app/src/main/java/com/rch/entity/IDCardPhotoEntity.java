package com.rch.entity;

/**
 * Created by Administrator on 2018/4/13.
 */

public class IDCardPhotoEntity {
    private String imageUrl;//2018-04-13/11111.jpg",   图片路径
    private String imageUrlPath;//www.rch.com/2018-04-13/11111.jpg"  图片全路径

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageUrlPath(String imageUrlPath) {
        this.imageUrlPath = imageUrlPath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUrlPath() {
        return imageUrlPath;
    }
}
