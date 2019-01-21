package com.rch.entity;

import java.util.List;

/**
 * 客户管理
 * Created by Administrator on 2018/10/29.
 */

public class CustomerManagerEntity {


    /**
     * userAmin : 1
     * totalSize : 450
     * userList : [{"userId":"1111e0a003f58ea75f9f0350"," enterpriseId":"5a741a7e75a308f2d23d144"," enterpriseName":"\u201d企业名称\u201d"," userName":"象牙白"," userSex":"1"," licenseType":" "," licenseNo":"2"," birthday":"5"," licenseUrl":" 证件正面照 "," licenseBackUrl":" 证件背面照"," mobile":" 手机号"," legalSexName":" 性别名称 ","followResult ":"1"," followResultName ":"1"}]
     * enterList : [{"enterpriseId":"5a741a7e75a308f2d23d144"," enterpriseName":"\u201d企业名称\u201d"}]
     */

    private String userAmin;
    private String totalSize;
    private java.util.List<UserListBean> userList;
    private java.util.List<EnterListBean> enterList;


    public String getUserAmin() {
        return userAmin;
    }

    public void setUserAmin(String userAmin) {
        this.userAmin = userAmin;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public List<UserListBean> getUserList() {
        return userList;
    }

    public void setUserList(List<UserListBean> userList) {
        this.userList = userList;
    }

    public List<EnterListBean> getEnterList() {
        return enterList;
    }

    public void setEnterList(List<EnterListBean> enterList) {
        this.enterList = enterList;
    }

    public static class UserListBean {
        /**
         * userId : 1111e0a003f58ea75f9f0350
         *  enterpriseId : 5a741a7e75a308f2d23d144
         *  enterpriseName : ”企业名称”
         *  userName : 象牙白
         *  userSex : 1
         *  licenseType :
         *  licenseNo : 2
         *  birthday : 5
         *  licenseUrl :  证件正面照
         *  licenseBackUrl :  证件背面照
         *  mobile :  手机号
         *  legalSexName :  性别名称
         * followResult  : 1
         *  followResultName  : 1
         */

        private String id;
        private String enterpriseId;
        private String enterpriseName;
        private String userName;
        private String userSex;
        private String licenseType;
        private String licenseNo;
        private String birthday;
        private String licenseUrl;
        private String licenseBackUrl;
        private String mobile;
        private String legalSexName;
        private String licenseTypeName;
        private String followResult;
        private String followResultName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLicenseTypeName() {
            return licenseTypeName;
        }

        public void setLicenseTypeName(String licenseTypeName) {
            this.licenseTypeName = licenseTypeName;
        }

        public String getEnterpriseId() {
            return enterpriseId;
        }

        public void setEnterpriseId(String enterpriseId) {
            this.enterpriseId = enterpriseId;
        }

        public String getEnterpriseName() {
            return enterpriseName;
        }

        public void setEnterpriseName(String enterpriseName) {
            this.enterpriseName = enterpriseName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserSex() {
            return userSex;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }

        public String getLicenseType() {
            return licenseType;
        }

        public void setLicenseType(String licenseType) {
            this.licenseType = licenseType;
        }

        public String getLicenseNo() {
            return licenseNo;
        }

        public void setLicenseNo(String licenseNo) {
            this.licenseNo = licenseNo;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }

        public String getLicenseBackUrl() {
            return licenseBackUrl;
        }

        public void setLicenseBackUrl(String licenseBackUrl) {
            this.licenseBackUrl = licenseBackUrl;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getLegalSexName() {
            return legalSexName;
        }

        public void setLegalSexName(String legalSexName) {
            this.legalSexName = legalSexName;
        }

        public String getFollowResult() {
            return followResult;
        }

        public void setFollowResult(String followResult) {
            this.followResult = followResult;
        }

        public String getFollowResultName() {
            return followResultName;
        }

        public void setFollowResultName(String followResultName) {
            this.followResultName = followResultName;
        }
    }

    public static class EnterListBean{

        /**
         * enterpriseId : 5a741a7e75a308f2d23d144
         *  enterpriseName : ”企业名称”
         */

        private String enterpriseId;
        private String enterpriseName;

        public String getEnterpriseId() {
            return enterpriseId;
        }

        public void setEnterpriseId(String enterpriseId) {
            this.enterpriseId = enterpriseId;
        }

        public String getEnterpriseName() {
            return enterpriseName;
        }

        public void setEnterpriseName(String enterpriseName) {
            this.enterpriseName = enterpriseName;
        }
    }
}
