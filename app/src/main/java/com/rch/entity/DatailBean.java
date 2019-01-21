package com.rch.entity;

import com.luck.picture.lib.entity.LocalMedia;
import com.rch.base.SecondBaseActivity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by acer on 2018/8/13.
 */

public class DatailBean implements Serializable{
    private String standardDelivery;
    private String deliveryUnit;
    private String gearboxType;
    private String emissionStandard;
    private String oilSupply;
    private String vehicleBody;
    private String carrierNumber;
    private String productionTime;
    private String vinCode;
    private String remark;
    private String vehicleBaseId;
    private String province;
    private String city;
    private String vehicleYear;
    private String showMileage;
    private String isRegistration;
    private String registrationProv;
    private String registrationCity;
    private String registrationTime;
    private String isChangeowner;
    private String usingNature;
    private String invoicePrice;
    private String accidentNumber;
    private String vehicleConfig;
    private String maintain;
    private String changeCount;
    private String modified;
    private String insurance;
    private String inspection;
    private String business;
    private String violationsPrice;

    private String ownership;
    private String taxes;
    private String vehicleDetails;
    private String enterpriseId;
    private String originalPrice;
    private String salesPrice;
    private String definePricePer;
    private String definePriceEnt;
    private String priceType;
    private String firstType;
    private String isRecommend;
    private String saleRemark;
    private String gearboxTypeName;
    private String emissionStandardName;
    private String oilSupplyName;
    private String vehicleBodyName;
    private String isRegistrationName;
    private String isChangeownerName;
    private String usingNatureName;
    private String maintainName;
    private String ownershipName;
    private String ownerShipName;//大写 名字

    private String taxesName;
    private String firstTypeName;
    private String brandName;
    private String brandImage;
    private String brandImagePath;
    private String modelName;
    private String typeName;
    private String ratePrice;
    private String vehicleFullName;
    private String vehicleImage;
    private String vehicleImagePath;


    private String id;
    private String auditState;
    private String shelvesStatus;
    private String shelvesTime;
    private String vehicleSaleId;
    private String brandId;
    private String version;
    private String modelId;
    private String vtypeId;
    private String vehicleColor;
    private String insideColor;
    private String vehicleColorName;
    private String vehicleType;
    private String vehicleStandard;
    private String vehicleStandardName;

    private String salesPriceMin;
    private String salesPriceMax;
    private String tradePriceMin;
    private String tradePriceMax;
    private String driverType;
    private String modelYear;
    private String makerType;
    private String sourcecode;
    private String isSave;//0-未收藏 1-已收藏 是否收藏
    private String provinceName;
    private String cityName;
    private String isSelected;
    private String salesPriceMinView;
    private String salesPriceMaxView;
    private String registrationProvName;
    private String registrationCityName;
    private String seriesName;
    private String seriesId;
    private List<PicListBean>picList;
    private List<LocalMedia> localMediaList;
    private List<LocalMedia> localMediaMainList;

    private String ownerTypeName;//车辆性质
    private String rejectReason;//驳回原因
    private String carBaseConfig;//参数配置
//    private String vehicleColor;//	车身颜色
//    private String insideColor;//内饰颜色
//    private String modified;//改装信息
    private String ifStar;

    public List<LocalMedia> getLocalMediaMainList() {
        return localMediaMainList;
    }

    public void setLocalMediaMainList(List<LocalMedia> localMediaMainList) {
        this.localMediaMainList = localMediaMainList;
    }

    public String getIfStar() {
        return ifStar;
    }

    public void setIfStar(String ifStar) {
        this.ifStar = ifStar;
    }

    public String getCarBaseConfig() {
        return carBaseConfig;
    }

    public void setCarBaseConfig(String carBaseConfig) {
        this.carBaseConfig = carBaseConfig;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getOwnerShipName() {
        return ownerShipName;
    }

    public void setOwnerShipName(String ownerShipName) {
        this.ownerShipName = ownerShipName;
    }

    public String getOwnerTypeName() {
        return ownerTypeName;
    }

    public void setOwnerTypeName(String ownerTypeName) {
        this.ownerTypeName = ownerTypeName;
    }

    public List<LocalMedia> getLocalMediaList() {
        return localMediaList;
    }

    public void setLocalMediaList(List<LocalMedia> localMediaList) {
        this.localMediaList = localMediaList;
    }

    public String getVehicleStandard() {
        return vehicleStandard;
    }

    public void setVehicleStandard(String vehicleStandard) {
        this.vehicleStandard = vehicleStandard;
    }

    public String getSeriesId() {
        return seriesId;
    }


    public List<PicListBean> getPicList() {
        return picList;
    }



    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public DatailBean() {
    }



    public void setPicList(List<PicListBean> picList) {
        this.picList = picList;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getRegistrationProvName() {
        return registrationProvName;
    }

    public void setRegistrationProvName(String registrationProvName) {
        this.registrationProvName = registrationProvName;
    }

    public String getRegistrationCityName() {
        return registrationCityName;
    }

    public void setRegistrationCityName(String registrationCityName) {
        this.registrationCityName = registrationCityName;
    }

    public String getSalesPriceMinView() {
        return salesPriceMinView;
    }

    public void setSalesPriceMinView(String salesPriceMinView) {
        this.salesPriceMinView = salesPriceMinView;
    }

    public String getSalesPriceMaxView() {
        return salesPriceMaxView;
    }

    public void setSalesPriceMaxView(String salesPriceMaxView) {
        this.salesPriceMaxView = salesPriceMaxView;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getIsSave() {
        return isSave;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }

    public String getSourcecode() {
        return sourcecode;
    }

    public void setSourcecode(String sourcecode) {
        this.sourcecode = sourcecode;
    }

    public String getMakerType() {
        return makerType;
    }

    public void setMakerType(String makerType) {
        this.makerType = makerType;
    }

    public String getModelYear() {
        return modelYear;
    }

    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
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

    public String getVehicleStandardName() {
        return vehicleStandardName;
    }

    public void setVehicleStandardName(String vehicleStandardName) {
        this.vehicleStandardName = vehicleStandardName;
    }

    private List<VehicleImageListEntity>imgList;

    public List<VehicleImageListEntity> getImgList() {
        return imgList;
    }

    public void setImgList(List<VehicleImageListEntity> imgList) {
        this.imgList = imgList;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleColorName() {
        return vehicleColorName;
    }

    public void setVehicleColorName(String vehicleColorName) {
        this.vehicleColorName = vehicleColorName;
    }

    public String getStandardDelivery() {
        return standardDelivery;
    }

    public void setStandardDelivery(String standardDelivery) {
        this.standardDelivery = standardDelivery;
    }

    public String getDeliveryUnit() {
        return deliveryUnit;
    }

    public void setDeliveryUnit(String deliveryUnit) {
        this.deliveryUnit = deliveryUnit;
    }

    public String getGearboxType() {
        return gearboxType;
    }

    public void setGearboxType(String gearboxType) {
        this.gearboxType = gearboxType;
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

    public String getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(String productionTime) {
        this.productionTime = productionTime;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVehicleBaseId() {
        return vehicleBaseId;
    }

    public void setVehicleBaseId(String vehicleBaseId) {
        this.vehicleBaseId = vehicleBaseId;
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

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getShowMileage() {
        return showMileage;
    }

    public void setShowMileage(String showMileage) {
        this.showMileage = showMileage;
    }

    public String getIsRegistration() {
        return isRegistration;
    }

    public void setIsRegistration(String isRegistration) {
        this.isRegistration = isRegistration;
    }

    public String getRegistrationProv() {
        return registrationProv;
    }

    public void setRegistrationProv(String registrationProv) {
        this.registrationProv = registrationProv;
    }

    public String getRegistrationCity() {
        return registrationCity;
    }

    public void setRegistrationCity(String registrationCity) {
        this.registrationCity = registrationCity;
    }

    public String getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(String registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getIsChangeowner() {
        return isChangeowner;
    }

    public void setIsChangeowner(String isChangeowner) {
        this.isChangeowner = isChangeowner;
    }

    public String getUsingNature() {
        return usingNature;
    }

    public void setUsingNature(String usingNature) {
        this.usingNature = usingNature;
    }

    public String getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(String invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public String getAccidentNumber() {
        return accidentNumber;
    }

    public void setAccidentNumber(String accidentNumber) {
        this.accidentNumber = accidentNumber;
    }

    public String getVehicleConfig() {
        return vehicleConfig;
    }

    public void setVehicleConfig(String vehicleConfig) {
        this.vehicleConfig = vehicleConfig;
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

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getInspection() {
        return inspection;
    }

    public void setInspection(String inspection) {
        this.inspection = inspection;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
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

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(String vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
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

    public String getDefinePricePer() {
        return definePricePer;
    }

    public void setDefinePricePer(String definePricePer) {
        this.definePricePer = definePricePer;
    }

    public String getDefinePriceEnt() {
        return definePriceEnt;
    }

    public void setDefinePriceEnt(String definePriceEnt) {
        this.definePriceEnt = definePriceEnt;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getFirstType() {
        return firstType;
    }

    public void setFirstType(String firstType) {
        this.firstType = firstType;
    }

    public String getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(String isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getSaleRemark() {
        return saleRemark;
    }

    public void setSaleRemark(String saleRemark) {
        this.saleRemark = saleRemark;
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

    public String getVehicleBodyName() {
        return vehicleBodyName;
    }

    public void setVehicleBodyName(String vehicleBodyName) {
        this.vehicleBodyName = vehicleBodyName;
    }

    public String getIsRegistrationName() {
        return isRegistrationName;
    }

    public void setIsRegistrationName(String isRegistrationName) {
        this.isRegistrationName = isRegistrationName;
    }

    public String getIsChangeownerName() {
        return isChangeownerName;
    }

    public void setIsChangeownerName(String isChangeownerName) {
        this.isChangeownerName = isChangeownerName;
    }

    public String getUsingNatureName() {
        return usingNatureName;
    }

    public void setUsingNatureName(String usingNatureName) {
        this.usingNatureName = usingNatureName;
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

    public String getFirstTypeName() {
        return firstTypeName;
    }

    public void setFirstTypeName(String firstTypeName) {
        this.firstTypeName = firstTypeName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public String getBrandImagePath() {
        return brandImagePath;
    }

    public void setBrandImagePath(String brandImagePath) {
        this.brandImagePath = brandImagePath;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getRatePrice() {
        return ratePrice;
    }

    public void setRatePrice(String ratePrice) {
        this.ratePrice = ratePrice;
    }

    public String getVehicleFullName() {
        return vehicleFullName;
    }

    public void setVehicleFullName(String vehicleFullName) {
        this.vehicleFullName = vehicleFullName;
    }

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getVehicleImagePath() {
        return vehicleImagePath;
    }

    public void setVehicleImagePath(String vehicleImagePath) {
        this.vehicleImagePath = vehicleImagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getShelvesStatus() {
        return shelvesStatus;
    }

    public void setShelvesStatus(String shelvesStatus) {
        this.shelvesStatus = shelvesStatus;
    }

    public String getShelvesTime() {
        return shelvesTime;
    }

    public void setShelvesTime(String shelvesTime) {
        this.shelvesTime = shelvesTime;
    }

    public String getVehicleSaleId() {
        return vehicleSaleId;
    }

    public void setVehicleSaleId(String vehicleSaleId) {
        this.vehicleSaleId = vehicleSaleId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getVtypeId() {
        return vtypeId;
    }

    public void setVtypeId(String vtypeId) {
        this.vtypeId = vtypeId;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getInsideColor() {
        return insideColor;
    }

    public void setInsideColor(String insideColor) {
        this.insideColor = insideColor;
    }
}
