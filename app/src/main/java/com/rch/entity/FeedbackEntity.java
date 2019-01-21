package com.rch.entity;

/**
 * 意见反馈实体
 * Created by Administrator on 2018/12/19.
 */

public class FeedbackEntity {

    /**
     * id : 5be9669c41530e2dbbf738a0
     * fbtype : 1
     * suggestion : 哦哦哦哦哦哦哦哦想我了llllll
     * fbuserid : 5be3f408758c0e2d9e79d949
     * status : 0
     * result :
     * createUser : 000000000000000000000000
     * createTime : 2018-12-19 00:00:00
     * fbtypeName : 新车问题
     * statusName : 未处理
     */

    private String id;
    private String fbtype;
    private String suggestion;
    private String fbuserid;
    private String status;
    private String result;
    private String createUser;
    private String createTime;
    private String fbtypeName;
    private String statusName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFbtype() {
        return fbtype;
    }

    public void setFbtype(String fbtype) {
        this.fbtype = fbtype;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getFbuserid() {
        return fbuserid;
    }

    public void setFbuserid(String fbuserid) {
        this.fbuserid = fbuserid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public String getFbtypeName() {
        return fbtypeName;
    }

    public void setFbtypeName(String fbtypeName) {
        this.fbtypeName = fbtypeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
