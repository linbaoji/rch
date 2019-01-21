package com.rch.entity;

/**
 * Created by Administrator on 2018/7/19.
 */

public class VersionBean {


    /**
     * id : 5966ffbdf1278ea7a6eadb2d
     * version : 1.0.5
     * versionCode : 1
     * versionType : 1
     * versionDesc : 1
     * downloadUrl :
     * isMandatory : 1
     */

    private String id;//主键，ID
    private String version;//版本号
    private int versionCode;//版本
    private String versionType;//类型：1-android,2-ios
    private String versionDesc;//1.0.5版本更新",  版本内容描述
    private String downloadUrl;//安装包下载地址
    private String isMandatory;//是否强制更新：0-否，1-是

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }
}
