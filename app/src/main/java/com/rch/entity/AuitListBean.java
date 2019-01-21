package com.rch.entity;

import java.io.Serializable;

/**
 * Created by acer on 2018/8/9.
 */

public class AuitListBean implements Serializable{

    //        	" day":"今天",	//不一定有值
//                    " date":"02：02：12",	//不一定有值
//                    " title":"主题",
//                    " content":"内容",
//                    " order":"顺序",
//                    " isColor":"1",	1-主题突出 2-主题不突出
    private String day;
    private String date;
    private String title;
    private String content;
    private String order;
    private String isColor;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getIsColor() {
        return isColor;
    }

    public void setIsColor(String isColor) {
        this.isColor = isColor;
    }
}