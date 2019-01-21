package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/27.
 */

public class UserInfoEntity implements Serializable{


    /**
     * id : 5bdc0793c0390e2df4af295a
     * userName : 139****2726
     * userSex : 3
     * birthday :
     * mobile : 139****2726
     * inviteId :
     * inviteMobile :
     * inviteCode : 618473
     * token : d30b242e097331cff207ebc7b64bfe2e
     * userType : 2
     * enterpriseId :
     * enterpriseName :
     * showName : 139****2726
     * userRoleType : 501
     * ifAdmin : 0
     * ifDailyadmin : 0
     * userAuditState : 1
     * toDetail : 1
     * ifRealnameCertify : 0
     * loginTime : 2018-11-05 16:43:14
     * loginIp : 192.168.15.190
     */

    private String id;
    private String userName;
    private String userSex;
    private String birthday;
    private String mobile;
    private String inviteId;
    private String inviteMobile;
    private String inviteCode;
    private String token;
    private String userType;
    private String enterpriseId;
    private String enterpriseName;
    private String showName;
    private String userRoleType;
    private String ifAdmin;
    private String ifDailyadmin;
    private String userAuditState;
    private String toDetail;
    private String ifRealnameCertify;
    private String loginTime;
    private String loginIp;

    public UserInfoEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getInviteMobile() {
        return inviteMobile;
    }

    public void setInviteMobile(String inviteMobile) {
        this.inviteMobile = inviteMobile;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getUserRoleType() {
        return userRoleType;
    }

    public void setUserRoleType(String userRoleType) {
        this.userRoleType = userRoleType;
    }

    public String getIfAdmin() {
        return ifAdmin;
    }

    public void setIfAdmin(String ifAdmin) {
        this.ifAdmin = ifAdmin;
    }

    public String getIfDailyadmin() {
        return ifDailyadmin;
    }

    public void setIfDailyadmin(String ifDailyadmin) {
        this.ifDailyadmin = ifDailyadmin;
    }

    public String getUserAuditState() {
        return userAuditState;
    }

    public void setUserAuditState(String userAuditState) {
        this.userAuditState = userAuditState;
    }

    public String getToDetail() {
        return toDetail;
    }

    public void setToDetail(String toDetail) {
        this.toDetail = toDetail;
    }

    public String getIfRealnameCertify() {
        return ifRealnameCertify;
    }

    public void setIfRealnameCertify(String ifRealnameCertify) {
        this.ifRealnameCertify = ifRealnameCertify;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
}
