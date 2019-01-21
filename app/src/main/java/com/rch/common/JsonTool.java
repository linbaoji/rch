package com.rch.common;


import com.rch.custom.SellProblemLayout;
import com.rch.entity.BrandEntity;
import com.rch.entity.CarDetailEntity;
import com.rch.entity.CarEntity;
import com.rch.entity.CarSeriesEntity;
import com.rch.entity.CertifiedEntity;
import com.rch.entity.CityInfoEntity;
import com.rch.entity.DetailBrandEntity;
import com.rch.entity.IDCardPhotoEntity;
import com.rch.entity.ModelEntity;
import com.rch.entity.ProblemEntity;
import com.rch.entity.ReserveOrderEntity;
import com.rch.entity.SearchEntity;
import com.rch.entity.SellCatEntity;
import com.rch.entity.SupplierEntity;
import com.rch.entity.UserInfoEntity;
import com.rch.entity.UserOrderEntity;
import com.rch.entity.VehicleImageListEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by Administrator on 2018/3/27.
 */

public class JsonTool {

    static String resultMsg = "";

    public static String getResult(String result,String key) {
        try {
            JSONObject jsonObject = getJsonObject(result);
            return isNullKey(jsonObject,key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getResultStr(String result, String key) {
        try {
            JSONObject jsonObject = getJsonObject(result);
            JSONObject object = jsonObject.getJSONObject("result");
            return isNullKey(object,key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getResultMsg(String result) {
        try {
            JSONObject jsonObject = getJsonObject(result);
            return jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static JSONObject getJsonObject(String result) throws JSONException {
        return new JSONObject(result);
    }


    public static int getHttpCode(String jsonObject) throws JSONException {
        JSONObject object = getJsonObject(jsonObject);
        return Integer.parseInt(object.getString("code"));
    }

//    /*登录用户信息*/
//    public static List<UserInfoEntity> getLoginResult(String result) {
//        List<UserInfoEntity> salesmanInfoList = new ArrayList<>();
//        if(result.isEmpty())
//            return salesmanInfoList;
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = getJsonObject(result);
//            JSONObject resultObj = (JSONObject) jsonObject.getJSONObject("result");
//            JSONObject object = (JSONObject) resultObj.getJSONObject("loginResultUser");
//            UserInfoEntity userInfo = new UserInfoEntity();
//            userInfo.setId(isNullKey(object, "id"));
//            userInfo.setEmail(isNullKey(object, "email"));
//            userInfo.setInviteCode(isNullKey(object, "inviteCode"));
//            userInfo.setMobile(isNullKey(object, "mobile"));
//            userInfo.setUserName(isNullKey(object, "userName"));
//            userInfo.setUserSex(isNullKey(object, "userSex"));
//            userInfo.setProvince(isNullKey(object, "province"));
//            userInfo.setCity(isNullKey(object, "city"));
//            userInfo.setAddress(isNullKey(object, "address"));
//            userInfo.setLoginTime(isNullKey(object, "loginTime"));
//            userInfo.setLoginIp(isNullKey(object, "loginIp"));
//            userInfo.setAgent(isNullKey(object, "agent"));
//            userInfo.setToken(isNullKey(object, "token"));
//            userInfo.setUserResultState(isNullKey(object, "userResultState"));
//            userInfo.setBirthday(isNullKey(object, "birthday"));
//            userInfo.setInviteId(isNullKey(object, "inviteId"));
//            userInfo.setAgentType(isNullKey(object, "agentType"));
//            userInfo.setUserType(isNullKey(object, "userType"));
//            userInfo.setIsEntAdmin(isNullKey(object,"isEntAdmin"));
//            userInfo.setUserAuthorityType(isNullKey(object,"userAuthorityType"));
//            userInfo.setJsonData(true);
//            salesmanInfoList.add(userInfo);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return salesmanInfoList;
//    }


    /*我要卖车问题查询*/
    public static List<SellCatEntity> getSellCatReuslt(String result) {
        List<SellCatEntity> sellCatEntityList = new ArrayList<>();
        List<ProblemEntity> problemEntitieList=new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resultObj = (JSONObject) jsonObject.getJSONObject("result");
            SellCatEntity userInfo = new SellCatEntity();
            String totalNum =  resultObj.getString("totalNum");
            userInfo.setTotalNum(totalNum);
            JSONArray array =  resultObj.getJSONArray("list");
            for (int i=0;i<array.length();i++)
            {
                JSONObject object= (JSONObject) array.get(i);
                ProblemEntity problemEntitie=new ProblemEntity();
                problemEntitie.setParamCode(isNullKey(object,"paramCode"));
                problemEntitie.setParamName(isNullKey(object,"porder"));
                problemEntitie.setParamValue(isNullKey(object,"akey"));
                problemEntitie.setParamDesc(isNullKey(object,"avalue"));

                problemEntitieList.add(problemEntitie);
                userInfo.setList(problemEntitieList);
            }
            sellCatEntityList.add(userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sellCatEntityList;
    }


    /*获取车辆商品列表*/
    /*public static List<CarEntity> getCarListEntity(String result) {
        List<CarEntity> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = jsonObject.getJSONObject("result");
            JSONArray array = resObject.getJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                CarEntity entity = new CarEntity();
                entity.setId(isNullKey(object, "id"));
                entity.setBrandId(isNullKey(object, "brandId"));
                entity.setVehicleName(isNullKey(object, "vehicleName"));
                entity.setDiscountPrice(isNullKey(object, "discountPrice"));
                entity.setDiscountPriceStr(isNullKey(object, "discountPriceStr"));
                entity.setSalesPrice(isNullKey(object, "salesPrice"));
                entity.setRegistrationTime(isNullKey(object, "registrationTime"));
                entity.setRegistrationYear(isNullKey(object, "registrationYear"));
                entity.setShowMileage(isNullKey(object, "showMileage"));
                entity.setRegistrationArea(isNullKey(object, "registrationArea"));
                entity.setStandardDelivery(isNullKey(object, "standardDelivery"));
                entity.setGearboxType(isNullKey(object, "gearboxType"));
                entity.setVehicleDetails(isNullKey(object, "vehicleDetails"));
                // entity.setShelvesState(isNullKey(object,"shelvesState"));
                entity.setShelvesTime(isNullKey(object, "shelvesTime"));
                entity.setRemark(isNullKey(object, "remark"));
                entity.setIfDel(isNullKey(object, "ifDel"));
                entity.setCreateTime(isNullKey(object, "createTime"));
                entity.setBrandName(isNullKey(object, "brandName"));
                entity.setBrandImage(isNullKey(object, "brandImage"));
                entity.setBrandImagePath(isNullKey(object, "brandImagePath"));
                entity.setVehicleImage(isNullKey(object, "vehicleImage"));
                entity.setVehicleImagePath(isNullKey(object, "vehicleImagePath"));
                list.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }*/


    static String[] indexs = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /*字母索引*/
    public static String[] getChatSize(String result) {
        String[] strChat = new String[0];
        int i = 0;
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = jsonObject.getJSONObject("result");
            strChat = new String[resObject.length() + 1];
            strChat[i] = "*";
            //i++;
            for (int j = 0; j < indexs.length; j++) {
                if (!resObject.isNull(indexs[j])) {
                    i++;
                    strChat[i] = indexs[j];
                    //i++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return strChat;
    }


    /*搜索页-模糊匹配车辆名称*/
    public static List<SearchEntity> getSearchData(String result) {
        List<SearchEntity> list=new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = jsonObject.getJSONObject("result");
            JSONArray array=resObject.getJSONArray("list");
            for (int j = 0; j < array.length(); j++) {
                SearchEntity searchEntity=new SearchEntity();
                JSONObject object= (JSONObject) array.get(j);
                searchEntity.setId(isNullKey(object,"id"));
                searchEntity.setVehicleFullName(isNullKey(object,"vehicleFullName"));

                searchEntity.setBrandId(isNullKey(object,"brandId"));
                searchEntity.setSeriesId(isNullKey(object,"seriesId"));
                searchEntity.setShowName(isNullKey(object,"showName"));
                searchEntity.setOrderStr(isNullKey(object,"orderStr"));
                searchEntity.setSearchId(isNullKey(object,"searchId"));
                searchEntity.setBrandName(isNullKey(object,"brandName"));
                searchEntity.setSeriesName(isNullKey(object,"seriesName"));

                list.add(searchEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    /*获取车系列表*/
    public static List<CarSeriesEntity> getCarSeriesListData(String result) {
        List<CarSeriesEntity> list = new ArrayList<>();
        JSONObject jsonObject = null;
        CarSeriesEntity entity = null;
        try {
            jsonObject = getJsonObject(result);
            JSONArray array = jsonObject.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                entity = new CarSeriesEntity();
                entity.setId(isNullKey(object, "id"));
                entity.setBrandId(isNullKey(object, "brandId"));
                entity.setModelImage(isNullKey(object, "imageUrl"));
                entity.setModelName(isNullKey(object, "seriesName"));
                entity.setBrandName(isNullKey(object, "brandName"));
                entity.setRemark(isNullKey(object, "remark"));
                if ((i + 1) == array.length())
                    entity.setIdentityCount(true);
                else
                    entity.setIdentityCount(false);
                if (i == 0)
                    entity.setIdentity(true);
                else
                    entity.setIdentity(false);
                list.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*获取品牌列表*/
    public static List<BrandEntity> getBrandListData(String result) {
        List<BrandEntity> list = new ArrayList<>();
        JSONObject jsonObject = null;
        BrandEntity entity = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = jsonObject.getJSONObject("result");
            entity = new BrandEntity();
            entity.setId("");
            entity.setBrandName("不限品牌");
            entity.setBrandImage("");
            entity.setBrandLogo("");
            entity.setBrandImagePath("");
            entity.setFirstLetter("*");
            entity.setSpellInit("*");
            entity.setIdentity(true);
            entity.setIdentityCount(true);
            entity.setBrandPy("");
            entity.setRemark("");
            list.add(entity);
            for (int j = 0; j < indexs.length; j++) {
                if (!resObject.isNull(indexs[j])) {
                    JSONArray array = resObject.getJSONArray(indexs[j]);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = (JSONObject) array.get(i);
                        entity = new BrandEntity();
                        entity.setId(isNullKey(object, "id"));
                        entity.setBrandName(isNullKey(object, "brandName"));
                        entity.setBrandPy(isNullKey(object, "brandPy"));
                        entity.setBrandImage(isNullKey(object, "brandImage"));
                        entity.setBrandImagePath(isNullKey(object, "brandLogo"));
                        entity.setRemark(isNullKey(object, "remark"));
                        entity.setFirstLetter(isNullKey(object, "spellInit"));
                        entity.setSpellInit(isNullKey(object, "spellInit"));
                        entity.setBrandLogo(isNullKey(object, "brandLogo"));

                        if ((i + 1) == array.length())
                            entity.setIdentityCount(true);
                        else
                            entity.setIdentityCount(false);
                        if (i == 0)
                            entity.setIdentity(true);
                        else
                            entity.setIdentity(false);
                        list.add(entity);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    /*获取城市列表*/
    public static List<CityInfoEntity> getCityListData(String result) {
        List<CityInfoEntity> list =new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = jsonObject.getJSONObject("result");
            for (int j = 0; j < indexs.length; j++) {
                if (!resObject.isNull(indexs[j])) {
                    JSONArray array = resObject.getJSONArray(indexs[j]);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = (JSONObject) array.get(i);
                        CityInfoEntity entity = new CityInfoEntity();
                        entity.setCityLetter(isNullKey(object, "spellInit"));
                        entity.setCityName(isNullKey(object, "areaName"));
                        entity.setTotal(isNullKey(object, "total"));
                        entity.setCity(isNullKey(object,"city"));
                        if(i==0)
                            entity.setIdentity(true);
                        else
                            entity.setIdentity(false);
                        if ((i + 1) == array.length())
                            entity.setIdentityCount(true);
                        else
                            entity.setIdentityCount(false);
                        list.add(entity);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*获取首页banner数据*/
    public static List<String> getBannerData(String result) {
        List<String> list =new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = jsonObject.getJSONObject("result");
            JSONArray array=resObject.getJSONArray("bannerList");
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*获取首页--预约单列表*/
    public static List<String> getReserveData(String result) {
        List<String> list =new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = jsonObject.getJSONObject("result");
            JSONArray array=resObject.getJSONArray("orderList");
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*获取首页--预约单列表*/
  /*  public static List<ReserveOrderEntity> getReserveOrderData(String result) {
        List<ReserveOrderEntity> list =new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = jsonObject.getJSONObject("result");
            JSONArray array=resObject.getJSONArray("orderList");
            for (int i = 0; i < array.length(); i++) {
                ReserveOrderEntity entity=new ReserveOrderEntity();
                JSONObject object= (JSONObject) array.get(i);
                entity.setId(isNullKey(object,"id"));
                entity.setId(isNullKey(object,"conventionTime"));
                entity.setId(isNullKey(object,"userId"));
                entity.setId(isNullKey(object,"vehicleId"));
                entity.setId(isNullKey(object,"orderState"));
                entity.setId(isNullKey(object,"vehicleImage"));
                entity.setId(isNullKey(object,"mobile"));
                entity.setId(isNullKey(object,"vehicleFullName"));
                entity.setId(isNullKey(object,"stype"));
                entity.setId(isNullKey(object,"enterpriseName"));
                entity.setId(isNullKey(object,"enterpriseProvince"));
                entity.setId(isNullKey(object,"enterpriseCity"));
                entity.setId(isNullKey(object,"enterpriseAddress"));
                list.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }*/


    /*刷新/获取用户基本信息----订单数据 */
    public static UserOrderEntity getUserOrderData(String result)
    {
        UserOrderEntity entity=null;
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resultObj = (JSONObject) jsonObject.getJSONObject("result");
            if(!resultObj.isNull("userOrder")) {
                JSONObject object = (JSONObject) resultObj.getJSONObject("userOrder");
                entity = new UserOrderEntity();
                entity.setId(isNullKey(object, "id"));
                entity.setConventionTime(isNullKey(object, "conventionTime"));
                entity.setUserId(isNullKey(object, "userId"));
                entity.setVehicleId(isNullKey(object, "vehicleId"));
                entity.setOrderState(isNullKey(object, "orderState"));
                entity.setVehicleImage(isNullKey(object, "vehicleImage"));
                entity.setMobile(isNullKey(object, "mobile"));
                entity.setVehicleFullName(isNullKey(object, "vehicleFullName"));
                entity.setVehicleColor(isNullKey(object, "vehicleColor"));
                entity.setStype(isNullKey(object, "stype"));
                entity.setEnterpriseName(isNullKey(object, "enterpriseName"));
                entity.setEnterpriseProvince(isNullKey(object, "enterpriseProvince"));
                entity.setEnterpriseCity(isNullKey(object, "enterpriseCity"));
                entity.setEnterpriseAddress(isNullKey(object, "enterpriseAddress"));
                entity.setVehicleImagePath(isNullKey(object, "vehicleImagePath"));
                entity.setOrderStateName(isNullKey(object, "orderStateName"));
                entity.setTelphone(isNullKey(object,"telphone"));
                entity.setVersion(isNullKey(object, "version"));
                entity.setShelvesStatus(isNullKey(object, "shelvesStatus"));
                entity.setIsGoup(isNullKey(object,"isGoup"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }


    /*获取车列表*/
    public static List<CarEntity> getCarListData(String result) {
        List<CarEntity> list =new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = jsonObject.getJSONObject("result");
            JSONArray array=null;
            if(!resObject.isNull("list"))
                array=resObject.getJSONArray("list");
            else if(!resObject.isNull("vehicleList"))
                array=resObject.getJSONArray("vehicleList");
            else
                return list;
            for (int i = 0; i < array.length(); i++) {
                JSONObject object= (JSONObject) array.get(i);
                CarEntity entity=new CarEntity();
                entity.setId(isNullKey(object,"id"));
                entity.setVehicleName(isNullKey(object,"vehicleName"));
                entity.setVehicleColor(isNullKey(object,"vehicleColor"));
                entity.setVehicleYear(isNullKey(object,"vehicleYear"));
                entity.setProvince(isNullKey(object,"province"));
                entity.setCity(isNullKey(object,"city"));
                entity.setRegistrationTime(isNullKey(object,"registrationTime"));
                entity.setRegistrationArea(isNullKey(object,"registrationArea"));
                entity.setStandardDelivery(isNullKey(object,"standardDelivery"));
                entity.setGearboxType(isNullKey(object,"gearboxType"));
                entity.setShowMileage(isNullKey(object,"showMileage"));
                entity.setOriginalPrice(convertScientificCalc(isNullKey(object,"originalPrice")));
                // entity.setSalesPrice(isNullKey(object,"salesPrice"));
                entity.setEmissionStandard(isNullKey(object,"emissionStandard"));
                entity.setOilSupply(isNullKey(object,"oilSupply"));
                entity.setUsingNature(isNullKey(object,"usingNature"));
                entity.setVehicleBody(isNullKey(object,"vehicleBody"));
                entity.setCarrierNumber(isNullKey(object,"carrierNumber"));
                //entity.setOriginalPrice(isNullKey(object,"originalPrice"));
                entity.setSalesPrice(convertScientificCalc(isNullKey(object,"salesPrice")));
                entity.setRatePrice(convertScientificCalc(isNullKey(object,"ratePrice")));
                entity.setFirstType(isNullKey(object,"firstType"));
                entity.setFirstPayments(isNullKey(object,"firstPayments"));
                entity.setNper(isNullKey(object,"nper"));
                entity.setMonthlyPayments(isNullKey(object,"monthlyPayments"));
                entity.setAccidentNumber(isNullKey(object,"accidentNumber"));
                entity.setVinCode(isNullKey(object,"vinCode"));
                entity.setVehicleConfig(isNullKey(object,"vehicleConfig"));
                entity.setInvoicePrice(isNullKey(object,"invoicePrice"));
                entity.setInsideColor(isNullKey(object,"insideColor"));
                entity.setMaintain(isNullKey(object,"maintain"));
                entity.setChangeCount(isNullKey(object,"changeCount"));
                entity.setModified(isNullKey(object,"modified"));
                entity.setInsuranceDate(isNullKey(object,"insuranceDate"));
                entity.setInspectionDate(isNullKey(object,"inspectionDate"));
                entity.setBusinessDate(isNullKey(object,"businessDate"));
                entity.setViolationsPrice(isNullKey(object,"violationsPrice"));
                entity.setOwnership(isNullKey(object,"ownership"));
                entity.setTaxes(isNullKey(object,"taxes"));
                entity.setVehicleDetails(isNullKey(object,"vehicleDetails"));
                entity.setRemark(isNullKey(object,"remark"));
                entity.setRegistrationYear(isNullKey(object,"registrationYear"));
                entity.setVehicleImage(isNullKey(object,"vehicleImage"));
                entity.setVehicleImagePath(isNullKey(object,"vehicleImagePath"));
                entity.setBrandName(isNullKey(object,"brandName"));
                entity.setBrandImage(isNullKey(object,"brandImage"));
                entity.setModelName(isNullKey(object,"modelName"));
                entity.setGearboxTypeName(isNullKey(object,"gearboxTypeName"));
                entity.setEmissionStandardName(isNullKey(object,"emissionStandardName"));
                entity.setOilSupplyName(isNullKey(object,"oilSupplyName"));
                entity.setUsingNatureName(isNullKey(object,"usingNatureName"));
                entity.setVehicleBodyName(isNullKey(object,"vehicleBodyName"));
                entity.setFirstTypeName(isNullKey(object,"firstTypeName"));
                entity.setMaintainName(isNullKey(object,"maintainName"));
                entity.setOwnershipName(isNullKey(object,"ownershipName"));
                entity.setTaxesName(isNullKey(object,"taxesName"));
                entity.setProductionTime(isNullKey(object,"productionTime"));
                entity.setVehicleFullName(isNullKey(object,"vehicleFullName"));
                entity.setBrandId(isNullKey(object,"brandId"));
                entity.setVersion(isNullKey(object,"version"));
                entity.setModelId(isNullKey(object,"modelId"));
                entity.setVtypeId(isNullKey(object,"vtypeId"));
                entity.setVehicleBaseId(isNullKey(object,"vehicleBaseId"));
                entity.setIsRegistration(isNullKey(object,"isRegistration"));
                entity.setIsChangeowner(isNullKey(object,"isChangeowner"));
                entity.setVehicleSaleId(isNullKey(object,"vehicleSaleId"));
                entity.setEnterpriseId(isNullKey(object,"enterpriseId"));
                entity.setPriceType(isNullKey(object,"priceType"));
                entity.setPriceTypeName(isNullKey(object,"priceTypeName"));
                entity.setSaleRemark(isNullKey(object,"saleRemark"));
                entity.setTypeName(isNullKey(object,"typeName"));
                list.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    /*初始化用户认证信息 */
    public static CertifiedEntity getCertifiedData(String result)
    {
        //List<CertifiedEntity> certifiedEntityList = new ArrayList<>();
        CertifiedEntity entity=null;
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            // JSONObject resultObj = (JSONObject) jsonObject.getJSONObject("result");
            //JSONObject object = (JSONObject) resultObj.getJSONObject("loginResultUser");
            JSONObject object = (JSONObject) jsonObject.getJSONObject("result");
            entity = new CertifiedEntity();
            entity.setAuthenticateType(isNullKey(object, "authenticateType"));
            entity.setEnterpriseName(isNullKey(object, "enterpriseName"));
            entity.setEnterpriseUrl(isNullKey(object, "enterpriseUrl"));
            entity.setEnterpriseProvince(isNullKey(object, "enterpriseProvince"));
            entity.setEnterpriseCity(isNullKey(object, "enterpriseCity"));
            entity.setEnterpriseAddress(isNullKey(object, "enterpriseAddress"));
            entity.setUserName(isNullKey(object, "userName"));
            entity.setUserSex(isNullKey(object, "userSex"));
            entity.setLicenseNo(isNullKey(object, "licenseNo"));
            entity.setProvince(isNullKey(object, "province"));
            entity.setCity(isNullKey(object, "city"));
            entity.setAddress(isNullKey(object, "address"));
            entity.setLicenseUrl(isNullKey(object, "licenseUrl"));
            entity.setLicenseBackUrl(isNullKey(object, "licenseBackUrl"));
            entity.setEnterpriseUrlPath(isNullKey(object, "enterpriseUrlPath"));
            entity.setLicenseUrlPath(isNullKey(object, "licenseUrlPath"));
            entity.setLicenseBackUrlPath(isNullKey(object, "licenseBackUrlPath"));
            entity.setEnterpriseNo(isNullKey(object,"enterpriseNo"));
            entity.setEnterpriseBusiRole(isNullKey(object,"enterpriseBusiRole"));
            entity.setLegalName(isNullKey(object,"legalName"));
            entity.setLegalSex(isNullKey(object,"legalSex"));
            // certifiedEntityList.add(entity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /*获取商品详情--- （车辆信息）*/
    public static CarDetailEntity getDetailCarData(String result) {
        JSONObject jsonObject = null;
        CarDetailEntity entity=null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = jsonObject.getJSONObject("result");
            JSONObject object=resObject.getJSONObject("vehicle");
            entity=new CarDetailEntity();
            entity.setId(isNullKey(object,"id"));
            entity.setVehicleName(isNullKey(object,"vehicleName"));
            entity.setVehicleColor(isNullKey(object,"vehicleColor"));
            entity.setVehicleYear(isNullKey(object,"vehicleYear"));
            entity.setProvince(isNullKey(object,"province"));
            entity.setCity(isNullKey(object,"city"));
            entity.setRegistrationTime(isNullKey(object,"registrationTime"));
            entity.setRegistrationArea(isNullKey(object,"registrationArea"));
            entity.setStandardDelivery(isNullKey(object,"standardDelivery"));
            entity.setGearboxType(isNullKey(object,"gearboxType"));
            entity.setShowMileage(isNullKey(object,"showMileage"));
            entity.setOriginalPrice(isNullKey(object,"originalPrice"));
            entity.setSalesPrice(convertScientificCalc(isNullKey(object,"salesPrice")));
            entity.setEmissionStandard(isNullKey(object,"emissionStandard"));
            entity.setRegistrationCity(isNullKey(object,"registrationCity"));
            entity.setRegistrationProv(isNullKey(object,"registrationProv"));
            entity.setOilSupply(isNullKey(object,"oilSupply"));
            entity.setUsingNature(isNullKey(object,"usingNature"));
            entity.setVehicleBody(isNullKey(object,"vehicleBody"));
            entity.setCarrierNumber(isNullKey(object,"carrierNumber"));
            // entity.setOriginalPrice(isNullKey(object,"originalPrice"));
            //entity.setSalesPrice(convertScientificCalc(isNullKey(object,"salesPrice")));
            entity.setRatePrice(convertScientificCalc(isNullKey(object,"ratePrice")));
            entity.setFirstType(isNullKey(object,"firstType"));
            entity.setFirstPayments(isNullKey(object,"firstPayments"));
            entity.setNper(isNullKey(object,"nper"));
            entity.setMonthlyPayments(isNullKey(object,"monthlyPayments"));
            entity.setAccidentNumber(isNullKey(object,"accidentNumber"));
            entity.setVinCode(isNullKey(object,"vinCode"));
            entity.setVehicleConfig(isNullKey(object,"vehicleConfig"));
            entity.setInvoicePrice(isNullKey(object,"invoicePrice"));
            entity.setInsideColor(isNullKey(object,"insideColor"));
            entity.setMaintain(isNullKey(object,"maintain"));
            entity.setChangeCount(isNullKey(object,"changeCount"));
            entity.setModified(isNullKey(object,"modified"));
            entity.setInsuranceDate(isNullKey(object,"insuranceDate"));
            entity.setInspectionDate(isNullKey(object,"inspectionDate"));
            entity.setBusinessDate(isNullKey(object,"businessDate"));
            entity.setViolationsPrice(isNullKey(object,"violationsPrice"));
            entity.setOwnership(isNullKey(object,"ownership"));
            entity.setTaxes(isNullKey(object,"taxes"));
            entity.setVehicleDetails(isNullKey(object,"vehicleDetails"));
            entity.setRemark(isNullKey(object,"remark"));
            entity.setGearboxTypeName(isNullKey(object,"gearboxTypeName"));
            entity.setEmissionStandardName(isNullKey(object,"emissionStandardName"));
            entity.setOilSupplyName(isNullKey(object,"oilSupplyName"));
            entity.setUsingNatureName(isNullKey(object,"usingNatureName"));
            entity.setVehicleBodyName(isNullKey(object,"vehicleBodyName"));
            entity.setFirstTypeName(isNullKey(object,"firstTypeName"));
            entity.setMaintainName(isNullKey(object,"maintainName"));
            entity.setOwnershipName(isNullKey(object,"ownershipName"));
            entity.setTaxesName(isNullKey(object,"taxesName"));
            entity.setProductionTime(isNullKey(object,"productionTime"));
            entity.setSupplierId(isNullKey(object,"supplierId"));
            entity.setBrandId(isNullKey(object,"brandId"));
            entity.setModelId(isNullKey(object,"modelId"));
            entity.setAuditState(isNullKey(object,"auditState"));
            entity.setShelvesTime(isNullKey(object,"shelvesTime"));
            entity.setShelvesStatus(isNullKey(object,"shelvesStatus"));
            entity.setIfDel(isNullKey(object,"ifDel"));
            entity.setCreateUser(isNullKey(object,"createUser"));
            entity.setCreateTime(isNullKey(object,"createTime"));
            entity.setUpdateUser(isNullKey(object,"updateUser"));
            entity.setUpdateTime(isNullKey(object,"updateTime"));
            entity.setVehicleFullName(isNullKey(object,"vehicleFullName"));
            entity.setVersion(isNullKey(object,"version"));
            entity.setVtypeId(isNullKey(object,"vtypeId"));
            entity.setEnterpriseId(isNullKey(object,"enterpriseId"));
            entity.setVehicleType(isNullKey(object,"vehicleType"));
            entity.setVehicleColorName(isNullKey(object,"vehicleColorName"));
            entity.setDeliveryUnit(isNullKey(object,"deliveryUnit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /*获取商品详情--- （品牌信息）*/
    public static DetailBrandEntity getDetailBrandData(String result){
        DetailBrandEntity entity=null;
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = (JSONObject) jsonObject.getJSONObject("result");
            JSONObject object=resObject.getJSONObject("brand");
            entity=new DetailBrandEntity();
            entity.setId(isNullKey(object,"id"));
            entity.setBrandName(isNullKey(object,"brandName"));
            entity.setBrandPy(isNullKey(object,"brandPy"));
            entity.setBrandImage(isNullKey(object,"brandImage"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /*获取商品详情--- （车型信息）*/
    public static ModelEntity getModelData(String result){
        ModelEntity entity=null;
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = (JSONObject) jsonObject.getJSONObject("result");
            JSONObject object=resObject.getJSONObject("model");
            entity=new ModelEntity();
            entity.setId(isNullKey(object,"id"));
            entity.setModelName(isNullKey(object,"modelName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /*获取商品详情--- （供应商信息）*/
    public static SupplierEntity getSupplierData(String result){
        SupplierEntity entity=null;
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = (JSONObject) jsonObject.getJSONObject("result");
            JSONObject object=resObject.getJSONObject("supplier");
            entity=new SupplierEntity();
            entity.setStype(isNullKey(object,"stype"));
            entity.setEnterpriseName(isNullKey(object,"enterpriseName"));
            entity.setEnterpriseProvince(isNullKey(object,"enterpriseProvince"));
            entity.setEnterpriseCity(isNullKey(object,"enterpriseCity"));
            entity.setEnterpriseAddress(isNullKey(object,"enterpriseAddress"));
            entity.setTelphone(isNullKey(object,"telphone"));
            entity.setIsGoup(isNullKey(object,"isGoup"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /*获取商品详情--- （商品图片）*/
    public static List<VehicleImageListEntity> getVehicleImageListData(String result){
        List<VehicleImageListEntity> list= new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONObject resObject = (JSONObject) jsonObject.getJSONObject("result");
            JSONArray array=resObject.getJSONArray("vehicleImageList");
            for (int i=0;i<array.length();i++) {
                JSONObject object= (JSONObject) array.get(i);
                VehicleImageListEntity entity = new VehicleImageListEntity();
                entity.setId(isNullKey(object, "id"));
                entity.setVehicleId(isNullKey(object, "vehicleId"));
                entity.setVehicleImage(isNullKey(object, "vehicleImage"));
                entity.setImageOri(isNullKey(object, "imageOri"));
                entity.setSort(isNullKey(object, "sort"));
                entity.setRemark(isNullKey(object, "remark"));
                entity.setIfDel(isNullKey(object, "ifDel"));
                entity.setCreateTime(isNullKey(object, "createTime"));
                entity.setVehicleImagePath(isNullKey(object, "vehicleImagePath"));
                list.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*照片返回*/
    public static List<IDCardPhotoEntity> getIDCardPhoto(String result) {
        List<IDCardPhotoEntity> idCardPhotoEntities = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = getJsonObject(result);
            JSONArray array = (JSONArray) jsonObject.getJSONArray("result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                IDCardPhotoEntity userInfo = new IDCardPhotoEntity();
                userInfo.setImageUrl(isNullKey(object, "imageUrl"));
                userInfo.setImageUrlPath(isNullKey(object, "imageUrlPath"));
                idCardPhotoEntities.add(userInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return idCardPhotoEntities;
    }

    /*处理科学计算法*/
    private static String convertScientificCalc(String number)
    {
        if(number.isEmpty())
            return "";
        return new BigDecimal(number).toPlainString();
    }



    private static String isNullKey(JSONObject object, String keyName) throws JSONException {
        if (!object.isNull(keyName))
            return object.getString(keyName);
        else
            return "";
    }
}

