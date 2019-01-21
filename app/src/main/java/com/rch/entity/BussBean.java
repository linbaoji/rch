package com.rch.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/12/19.
 */

public class BussBean implements Parcelable {


    /**
     * id : 5be40dae6ba50e2d586d0001
     * userId : 5bea5d59c5040e2d35189d4c
     * mobile : 131****0000
     * enterpriseId : 5bebe8ee481b0e2d29942068
     * content : 内容呼吁日UR额我hi人护惹我我人
     * viewcount : 0
     * auditDate : 2018-12-19 13:03:22
     * auditDateStr : 1小时前
     */

    private String id;
    private String userId;
    private String mobile;
    private String enterpriseId;
    private String content;
    private int viewcount;
    private String auditDate;
    private String auditDateStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getViewcount() {
        return viewcount;
    }

    public void setViewcount(int viewcount) {
        this.viewcount = viewcount;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditDateStr() {
        return auditDateStr;
    }

    public void setAuditDateStr(String auditDateStr) {
        this.auditDateStr = auditDateStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.mobile);
        dest.writeString(this.enterpriseId);
        dest.writeString(this.content);
        dest.writeInt(this.viewcount);
        dest.writeString(this.auditDate);
        dest.writeString(this.auditDateStr);
    }

    public BussBean() {
    }

    protected BussBean(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.mobile = in.readString();
        this.enterpriseId = in.readString();
        this.content = in.readString();
        this.viewcount = in.readInt();
        this.auditDate = in.readString();
        this.auditDateStr = in.readString();
    }

    public static final Parcelable.Creator<BussBean> CREATOR = new Parcelable.Creator<BussBean>() {
        @Override
        public BussBean createFromParcel(Parcel source) {
            return new BussBean(source);
        }

        @Override
        public BussBean[] newArray(int size) {
            return new BussBean[size];
        }
    };
}
