package com.rch.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/11.
 */

public class CarDetailEntity implements Serializable {
    private String id;//1111e0a003f58ea75f9f0350",	商品主键ID
    private String supplierId;//5a741a7e75a308f2d23d144",  	供应商ID
    private String brandId;//5a741a7e75a308e6cdfa14a3",	品牌ID
    private String modelId;//5a741a7e75a308f2d236533",  	车型ID
    private String vehicleName;// 2016款 MODEL S 70D",	车款名称
    private String vehicleColor;//象牙白",  	车身颜色
    private String vehicleYear;//3,  	车龄
    private String province;//上海",  	所在省份
    private String city;//上海",  	所在城市
    private String productionTime;//2015-07-02 00:00:00",	上牌时间
    private String registrationTime;//2015-02-02 00:00:00",	上牌时间
    private String registrationArea;//上海",	上牌地区
    private String standardDelivery;//1.6L",	标准排量
    private String gearboxType;//1",  	变速箱类型1-自动，2-手动
    private String showMileage;//67444,	表显里程
    private String emissionStandard;//4",  排放标准1-国二，2-国三，3-国四，4-国五
    private String oilSupply;//1",  供油系统1-汽油，2-柴油，3-电动，4-油电混合，5-其它
    private String usingNature;//2",  使用性质1-非营运，2-租赁，3-其它
    private String vehicleBody;//1",  车体形式1-两厢轿车，2-三厢轿车，3-跑车，4-SUV，5-MPV，6-面包车，7-皮卡
    private String carrierNumber;//5,    核载人数
    private String registrationCity;//":"上海",	上牌地区-市
    private String originalPrice;//1800000.00,  	原价格
    private String salesPrice;//1600000.00,  	销售价格
    private String ratePrice;//160000.00,   最终折扣价（为销售价*用户折扣）
    private String firstType;//1",  首期类型  1-百分比，2-金额
    private String firstPayments;//10,  首期款
    private String nper;//1,  期数
    private String monthlyPayments;//7400,  月供
    private String accidentNumber;//0,  	事故次数
    private String vinCode;//H9FL8V6D8639KF7653H4F248",  	VIN码
    private String vehicleConfig;//电动天窗",  	车辆配置
    private String invoicePrice;//1980000,  	购车发票价格
    private String insideColor;//黑",  	内部颜色
    private String maintain;//1",  	保养情况 1- 4S店保养，2-自行保养，3-无保养
    private String changeCount;//0,  	过户记录
    private String modified;//无",  	改装情况
    private String insuranceDate;//2018-02-02 13:58:31",  	交强险到期日期
    private String inspectionDate;//2018-02-02 13:58:31",  	年检到期日期
    private String businessDate;//2018-02-02 13:58:31",  	商业险到期日期
    private String violationsPrice;//0,  	违章罚款
    private String ownership;//1",  	所有权性质1-私家车，2-公车，3其它
    private String taxes;//1,3",  	已交税费1-购置税，2-车船使用税，3-附加税
    private String vehicleDetails;//巴拉巴拉一大堆",  	车辆详情
    private String auditState;//",    审核状态0-待提交，1-待审核，2-审核通过，3-审核不通过
    private String shelvesTime;//2018-04-02 13:58:31",  	上架时间
    private String shelvesStatus;//1",  	上架状态 0-待上架，1-上架，2-下架
    private String remark;//备什么注",  	备注
    private String ifDel;//0",  	是否删除0-否，1-是
    private String createUser;//a23de8er2a65d75f86c5855a",  	创建人
    private String createTime;//2018-04-02 13:58:31",  	创建时间
    private String updateUser;// a23de8er2a65d75f86c5855a ",  	更新人
    private String updateTime;//2018-04-02 13:58:31",  	更新时间
    private String gearboxTypeName;//手动",	变速箱类型-枚举名称
    private String emissionStandardName;//国四",  排放标准-枚举名称
    private String oilSupplyName;//汽油",  供油系统-枚举名称
    private String usingNatureName;//非营运",  使用性质-枚举名称
    private String vehicleBodyName;//三箱轿车",  车体形式-枚举名称
    private String firstTypeName;//百分比",  首期类型 -枚举名称
    private String maintainName;//4S店保养",	  保养情况-枚举名称
    private String ownershipName;//私家车",	  所有权性质-枚举名称
    private String taxesName;//":"购置税 附加税"	已交税费-枚举名称
    private String vehicleFullName;//
    private String registrationProv;
    private String vehicleColorName;//车身颜色
    private String deliveryUnit;//排量单位

    public String getDeliveryUnit() {
        return deliveryUnit;
    }

    public void setDeliveryUnit(String deliveryUnit) {
        this.deliveryUnit = deliveryUnit;
    }

    public String getRegistrationProv() {
        return registrationProv;
    }

    public void setRegistrationProv(String registrationProv) {
        this.registrationProv = registrationProv;
    }

    private String version;
    private String vtypeId;
    private String enterpriseId;

    private String vehicleType;//车辆类型,1最新2历史

    public void setRegistrationCity(String registrationCity) {
        this.registrationCity = registrationCity;
    }

    public String getRegistrationCity() {
        return registrationCity;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVtypeId() {
        return vtypeId;
    }

    public void setVtypeId(String vtypeId) {
        this.vtypeId = vtypeId;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getVehicleFullName() {
        return vehicleFullName;
    }

    public void setVehicleFullName(String vehicleFullName) {
        this.vehicleFullName = vehicleFullName;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getModelId() {
        return modelId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getIfDel() {
        return ifDel;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getShelvesTime() {
        return shelvesTime;
    }

    public String getAuditState() {
        return auditState;
    }

    public String getCreateUser() {
        return createUser;
    }

    public String getShelvesStatus() {
        return shelvesStatus;
    }

    public void setIfDel(String ifDel) {
        this.ifDel = ifDel;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setShelvesTime(String shelvesTime) {
        this.shelvesTime = shelvesTime;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public void setShelvesStatus(String shelvesStatus) {
        this.shelvesStatus = shelvesStatus;
    }

    public void setRatePrice(String ratePrice) {
        this.ratePrice = ratePrice;
    }

    public String getRatePrice() {
        return ratePrice;
    }

    public void setProductionTime(String productionTime) {
        this.productionTime = productionTime;
    }

    public String getProductionTime() {
        return productionTime;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getRegistrationArea() {
        return registrationArea;
    }

    public void setRegistrationArea(String registrationArea) {
        this.registrationArea = registrationArea;
    }

    public String getStandardDelivery() {
        return standardDelivery;
    }

    public void setStandardDelivery(String standardDelivery) {
        this.standardDelivery = standardDelivery;
    }

    public String getGearboxType() {
        return gearboxType;
    }

    public void setGearboxType(String gearboxType) {
        this.gearboxType = gearboxType;
    }

    public String getShowMileage() {
        return showMileage;
    }

    public void setShowMileage(String showMileage) {
        this.showMileage = showMileage;
    }

    public String getEmissionStandard() {
        return emissionStandard;
    }

    public void setEmissionStandard(String emissionStandard) {
        this.emissionStandard = emissionStandard;
    }

    public String getOilSupply() {
        return oilSupply;
    }

    public void setOilSupply(String oilSupply) {
        this.oilSupply = oilSupply;
    }

    public String getUsingNature() {
        return usingNature;
    }

    public void setUsingNature(String usingNature) {
        this.usingNature = usingNature;
    }

    public String getVehicleBody() {
        return vehicleBody;
    }

    public void setVehicleBody(String vehicleBody) {
        this.vehicleBody = vehicleBody;
    }

    public String getCarrierNumber() {
        return carrierNumber;
    }

    public void setCarrierNumber(String carrierNumber) {
        this.carrierNumber = carrierNumber;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(String salesPrice) {
        this.salesPrice = salesPrice;
    }


    public String getFirstType() {
        return firstType;
    }

    public void setFirstType(String firstType) {
        this.firstType = firstType;
    }

    public String getFirstPayments() {
        return firstPayments;
    }

    public void setFirstPayments(String firstPayments) {
        this.firstPayments = firstPayments;
    }

    public String getNper() {
        return nper;
    }

    public void setNper(String nper) {
        this.nper = nper;
    }

    public String getMonthlyPayments() {
        return monthlyPayments;
    }

    public void setMonthlyPayments(String monthlyPayments) {
        this.monthlyPayments = monthlyPayments;
    }

    public String getAccidentNumber() {
        return accidentNumber;
    }

    public void setAccidentNumber(String accidentNumber) {
        this.accidentNumber = accidentNumber;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getVehicleConfig() {
        return vehicleConfig;
    }

    public void setVehicleConfig(String vehicleConfig) {
        this.vehicleConfig = vehicleConfig;
    }

    public String getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(String invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public String getInsideColor() {
        return insideColor;
    }

    public void setInsideColor(String insideColor) {
        this.insideColor = insideColor;
    }

    public String getMaintain() {
        return maintain;
    }

    public void setMaintain(String maintain) {
        this.maintain = maintain;
    }

    public String getChangeCount() {
        return changeCount;
    }

    public void setChangeCount(String changeCount) {
        this.changeCount = changeCount;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }


    public String getViolationsPrice() {
        return violationsPrice;
    }

    public void setViolationsPrice(String violationsPrice) {
        this.violationsPrice = violationsPrice;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getTaxes() {
        return taxes;
    }


    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public String getInsuranceDate() {
        return insuranceDate;
    }



    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public void setInsuranceDate(String insuranceDate) {
        this.insuranceDate = insuranceDate;
    }



    public String getGearboxTypeName() {
        return gearboxTypeName;
    }

    public void setGearboxTypeName(String gearboxTypeName) {
        this.gearboxTypeName = gearboxTypeName;
    }

    public String getEmissionStandardName() {
        return emissionStandardName;
    }

    public void setEmissionStandardName(String emissionStandardName) {
        this.emissionStandardName = emissionStandardName;
    }

    public String getOilSupplyName() {
        return oilSupplyName;
    }

    public void setOilSupplyName(String oilSupplyName) {
        this.oilSupplyName = oilSupplyName;
    }

    public String getUsingNatureName() {
        return usingNatureName;
    }

    public void setUsingNatureName(String usingNatureName) {
        this.usingNatureName = usingNatureName;
    }

    public String getVehicleBodyName() {
        return vehicleBodyName;
    }

    public void setVehicleBodyName(String vehicleBodyName) {
        this.vehicleBodyName = vehicleBodyName;
    }

    public String getFirstTypeName() {
        return firstTypeName;
    }

    public void setFirstTypeName(String firstTypeName) {
        this.firstTypeName = firstTypeName;
    }

    public String getMaintainName() {
        return maintainName;
    }

    public void setMaintainName(String maintainName) {
        this.maintainName = maintainName;
    }

    public String getOwnershipName() {
        return ownershipName;
    }

    public void setOwnershipName(String ownershipName) {
        this.ownershipName = ownershipName;
    }

    public String getTaxesName() {
        return taxesName;
    }

    public void setTaxesName(String taxesName) {
        this.taxesName = taxesName;
    }

    public String getVehicleColorName() {
        return vehicleColorName;
    }

    public void setVehicleColorName(String vehicleColorName) {
        this.vehicleColorName = vehicleColorName;
    }
}
