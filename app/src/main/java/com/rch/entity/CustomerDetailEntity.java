package com.rch.entity;

import java.util.List;

/**
 * 客户详情
 * Created by Administrator on 2018/10/30.
 */

public class CustomerDetailEntity {

    /**
     * userId : asdaw
     * userName : 用户
     * mobile : 13456897895
     * inviteId : adad
     * inviteName : 张三
     * followSize :  1
     * orderSize : 1
     * querySaleSize : 1
     * followList : [{"customerLevel":"H","customerLevelName":"H级 - 建议"," followResult ":"1"," followResultName ":"成功销售","notes":"跟进类容"," nextDate":"2018年9月10日 10:00"," followDate ":"2018-12-12  14:00:00 ","userId":"12121","userName":"张三","followWay":"1","followWayName":"电话"}]
     * orderList : [{"id":"asdadsasdad","vehicleId":" asdasdadsadf"," vehicleFullName ":"宝马"," enterpriseId":"5a741a7e75a308f2d23d144"," enterpriseName":"接力航"," conventionTime":"2018年9月10日 10:00"," orginid":"aa "," orginidName":"张三 ","ifNew":"1"," ifNewName":"新车","state":"1","stateName":"等待看车","factTime ":"2018 年9月10日","cacelReason ":"1","cacelReasonName":"1","rejectReason ":"1","rejectReasonName ":"1 ","createTime ":"2018-12-12  14:00:00"}]
     * querySaleList : [{"id ":"asdadsasdad"," vehicleFullName ":"宝马"," enterpriseId ":"5a741a7e75a308f2d23d144"," enterpriseName ":"接力航","createTime ":"2018-12-12  14:00:00 ","ifNew":"1"," ifNewName":"新车"}]
     */

    private String userId;
    private String userName;
    private String mobile;
    private String inviteId;
    private String inviteName;
    private String followSize;
    private String orderSize;
    private String querySaleSize;
    private java.util.List<FollowListBean> followList;
    private java.util.List<OrderListBean> orderList;
    private java.util.List<QuerySaleListBean> querySaleList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }

    public String getFollowSize() {
        return followSize;
    }

    public void setFollowSize(String followSize) {
        this.followSize = followSize;
    }

    public String getOrderSize() {
        return orderSize;
    }

    public void setOrderSize(String orderSize) {
        this.orderSize = orderSize;
    }

    public String getQuerySaleSize() {
        return querySaleSize;
    }

    public void setQuerySaleSize(String querySaleSize) {
        this.querySaleSize = querySaleSize;
    }

    public List<FollowListBean> getFollowList() {
        return followList;
    }

    public void setFollowList(List<FollowListBean> followList) {
        this.followList = followList;
    }

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public List<QuerySaleListBean> getQuerySaleList() {
        return querySaleList;
    }

    public void setQuerySaleList(List<QuerySaleListBean> querySaleList) {
        this.querySaleList = querySaleList;
    }

    public static class FollowListBean {
        /**
         * customerLevel : H
         * customerLevelName : H级 - 建议
         *  followResult  : 1
         *  followResultName  : 成功销售
         * notes : 跟进类容
         *  nextDate : 2018年9月10日 10:00
         *  followDate  : 2018-12-12  14:00:00
         * userId : 12121
         * userName : 张三
         * followWay : 1
         * followWayName : 电话
         */

        private String customerLevel;
        private String customerLevelName;
        private String followResult;
        private String followResultName;
        private String notes;
        private String nextDate;
        private String followDate;
        private String userId;
        private String userName;
        private String followWay;
        private String followWayName;

        public String getCustomerLevel() {
            return customerLevel;
        }

        public void setCustomerLevel(String customerLevel) {
            this.customerLevel = customerLevel;
        }

        public String getCustomerLevelName() {
            return customerLevelName;
        }

        public void setCustomerLevelName(String customerLevelName) {
            this.customerLevelName = customerLevelName;
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

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getNextDate() {
            return nextDate;
        }

        public void setNextDate(String nextDate) {
            this.nextDate = nextDate;
        }

        public String getFollowDate() {
            return followDate;
        }

        public void setFollowDate(String followDate) {
            this.followDate = followDate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getFollowWay() {
            return followWay;
        }

        public void setFollowWay(String followWay) {
            this.followWay = followWay;
        }

        public String getFollowWayName() {
            return followWayName;
        }

        public void setFollowWayName(String followWayName) {
            this.followWayName = followWayName;
        }
    }

    public static class OrderListBean{


        /**
         * id : ar11e1111a111
         * vehicleId : 5bd7e8719ade4a17f95ed9b4
         * vehicleFullName : 奥迪 奥迪A8L(进口) 2013款 奥迪A4L 30 TFSI 自动 舒适型
         * enterpriseId : 5b32f0e49d2fb0e4cabab82f
         * enterpriseName : 上海嵘辉商务咨询有限公司
         * conventionTime : 2018-10-31 00:00
         * conventionBeginDate :
         * conventionEndDate :
         * orginid : 5b36f218736eb0e4ba1bb17e
         * orginidName : 庞继峰
         * ifNew : 0
         * ifNewName : 二手车
         * state : 4
         * stateName : 客服确认失败
         * factTime :
         * cacelReason :
         * cacelReasonName :
         * rejectReason :
         * createTime : 2018-10-31 10:57:23
         * contanctName :
         * userName : 庞继峰
         */

        private String id;
        private String vehicleId;
        private String vehicleFullName;
        private String enterpriseId;
        private String enterpriseName;
        private String conventionTime;
        private String conventionBeginDate;
        private String conventionEndDate;
        private String orginid;
        private String orginidName;
        private String ifNew;
        private String ifNewName;
        private String state;
        private String stateName;
        private String factTime;
        private String cacelReason;
        private String cacelReasonName;
        private String rejectReason;
        private String rejectReasonName;
        private String createTime;
        private String contanctName;
        private String userName;
        private String brandName;//品牌
        private String seriesName;//车系名称
        private String modelName;//车型名称

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getSeriesName() {
            return seriesName;
        }

        public void setSeriesName(String seriesName) {
            this.seriesName = seriesName;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getRejectReasonName() {
            return rejectReasonName;
        }

        public void setRejectReasonName(String rejectReasonName) {
            this.rejectReasonName = rejectReasonName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
        }

        public String getVehicleFullName() {
            return vehicleFullName;
        }

        public void setVehicleFullName(String vehicleFullName) {
            this.vehicleFullName = vehicleFullName;
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

        public String getConventionTime() {
            return conventionTime;
        }

        public void setConventionTime(String conventionTime) {
            this.conventionTime = conventionTime;
        }

        public String getConventionBeginDate() {
            return conventionBeginDate;
        }

        public void setConventionBeginDate(String conventionBeginDate) {
            this.conventionBeginDate = conventionBeginDate;
        }

        public String getConventionEndDate() {
            return conventionEndDate;
        }

        public void setConventionEndDate(String conventionEndDate) {
            this.conventionEndDate = conventionEndDate;
        }

        public String getOrginid() {
            return orginid;
        }

        public void setOrginid(String orginid) {
            this.orginid = orginid;
        }

        public String getOrginidName() {
            return orginidName;
        }

        public void setOrginidName(String orginidName) {
            this.orginidName = orginidName;
        }

        public String getIfNew() {
            return ifNew;
        }

        public void setIfNew(String ifNew) {
            this.ifNew = ifNew;
        }

        public String getIfNewName() {
            return ifNewName;
        }

        public void setIfNewName(String ifNewName) {
            this.ifNewName = ifNewName;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getFactTime() {
            return factTime;
        }

        public void setFactTime(String factTime) {
            this.factTime = factTime;
        }

        public String getCacelReason() {
            return cacelReason;
        }

        public void setCacelReason(String cacelReason) {
            this.cacelReason = cacelReason;
        }

        public String getCacelReasonName() {
            return cacelReasonName;
        }

        public void setCacelReasonName(String cacelReasonName) {
            this.cacelReasonName = cacelReasonName;
        }

        public String getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(String rejectReason) {
            this.rejectReason = rejectReason;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getContanctName() {
            return contanctName;
        }

        public void setContanctName(String contanctName) {
            this.contanctName = contanctName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public static class QuerySaleListBean{


        /**
         * id : 1
         * enterpriseId : 5b32f0e49d2fb0e4cabab82f
         * enterpriseName : 上海嵘辉商务咨询有限公司
         * vehicleFullName : 奥迪 奥迪A8L(进口) 2013款 奥迪A4L 30 TFSI 自动 舒适型
         * createTime : 2018-11-02 11:31:53
         * ifNew : 0
         * ifNewName : 二手车
         * salesPriceMin : 34.00
         * salesPriceMax : 56.00
         * tradePriceMin : 21.00
         * tradePriceMax : 78.00
         * status : 0
         * statusName : 未处理
         * notes : ada
         * operator : 5b36f218736eb0e4ba1bb17e
         * operatorName : 庞继峰
         * contanctName : asd
         */

        private String id;
        private String enterpriseId;
        private String enterpriseName;
        private String vehicleFullName;
        private String createTime;
        private String ifNew;
        private String ifNewName;
        private String salesPriceMin;
        private String salesPriceMax;
        private String tradePriceMin;
        private String tradePriceMax;
        private String status;
        private String statusName;
        private String notes;
        private String operator;
        private String operatorName;
        private String contanctName;
        private String brandName;//品牌
        private String seriesName;//车系名称
        private String modelName;//车型名称

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getSeriesName() {
            return seriesName;
        }

        public void setSeriesName(String seriesName) {
            this.seriesName = seriesName;
        }

        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getVehicleFullName() {
            return vehicleFullName;
        }

        public void setVehicleFullName(String vehicleFullName) {
            this.vehicleFullName = vehicleFullName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getIfNew() {
            return ifNew;
        }

        public void setIfNew(String ifNew) {
            this.ifNew = ifNew;
        }

        public String getIfNewName() {
            return ifNewName;
        }

        public void setIfNewName(String ifNewName) {
            this.ifNewName = ifNewName;
        }

        public String getSalesPriceMin() {
            return salesPriceMin;
        }

        public void setSalesPriceMin(String salesPriceMin) {
            this.salesPriceMin = salesPriceMin;
        }

        public String getSalesPriceMax() {
            return salesPriceMax;
        }

        public void setSalesPriceMax(String salesPriceMax) {
            this.salesPriceMax = salesPriceMax;
        }

        public String getTradePriceMin() {
            return tradePriceMin;
        }

        public void setTradePriceMin(String tradePriceMin) {
            this.tradePriceMin = tradePriceMin;
        }

        public String getTradePriceMax() {
            return tradePriceMax;
        }

        public void setTradePriceMax(String tradePriceMax) {
            this.tradePriceMax = tradePriceMax;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public String getContanctName() {
            return contanctName;
        }

        public void setContanctName(String contanctName) {
            this.contanctName = contanctName;
        }
    }
}
