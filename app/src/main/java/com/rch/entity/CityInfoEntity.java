package com.rch.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Administrator on 2018/4/17.
 */

public class CityInfoEntity implements Parcelable{

    private String cityName;
    private String cityLetter;
    private String total;//":3,    拥有车辆数量
    private Boolean identity;//标识-是否第一次显示字母  true-第一次,false 第N次*/
    private Boolean identityCount;//标识-字母一项下面横线是否已到最后一个
    private Boolean isSelected=false;//是否选中

    private String city;
    private String areaName;
    private String spellInit;

    protected CityInfoEntity(Parcel in) {
        cityName = in.readString();
        cityLetter = in.readString();
        total = in.readString();
        byte tmpIdentity = in.readByte();
        identity = tmpIdentity == 0 ? null : tmpIdentity == 1;
        byte tmpIdentityCount = in.readByte();
        identityCount = tmpIdentityCount == 0 ? null : tmpIdentityCount == 1;
        byte tmpIsSelected = in.readByte();
        isSelected = tmpIsSelected == 0 ? null : tmpIsSelected == 1;
        city = in.readString();
        areaName = in.readString();
        spellInit = in.readString();
    }

    public static final Creator<CityInfoEntity> CREATOR = new Creator<CityInfoEntity>() {
        @Override
        public CityInfoEntity createFromParcel(Parcel in) {
            return new CityInfoEntity(in);
        }

        @Override
        public CityInfoEntity[] newArray(int size) {
            return new CityInfoEntity[size];
        }
    };

    @Override
    public String toString() {
        return "CityInfoEntity{" +
                "cityName='" + cityName + '\'' +
                ", cityLetter='" + cityLetter + '\'' +
                ", total='" + total + '\'' +
                ", identity=" + identity +
                ", identityCount=" + identityCount +
                ", isSelected=" + isSelected +
                ", city='" + city + '\'' +
                ", areaName='" + areaName + '\'' +
                ", spellInit='" + spellInit + '\'' +
                '}';
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getSpellInit() {
        return spellInit;
    }

    public void setSpellInit(String spellInit) {
        this.spellInit = spellInit;
    }

    public CityInfoEntity(){}


    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public void setIdentityCount(Boolean identityCount) {
        this.identityCount = identityCount;
    }

    public Boolean getIdentityCount() {
        return identityCount;
    }

    public void setIdentity(Boolean identity) {
        this.identity = identity;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal() {
        return total;
    }

    public void setCityLetter(String cityLetter) {
        this.cityLetter = cityLetter;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Boolean getIdentity() {
        return identity;
    }

    public String getCityLetter() {
        return cityLetter;
    }

    public String getCityName() {
        return cityName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(cityName);
        parcel.writeString(cityLetter);
        parcel.writeString(total);
        parcel.writeByte((byte) (identity == null ? 0 : identity ? 1 : 2));
        parcel.writeByte((byte) (identityCount == null ? 0 : identityCount ? 1 : 2));
        parcel.writeByte((byte) (isSelected == null ? 0 : isSelected ? 1 : 2));
        parcel.writeString(city);
        parcel.writeString(areaName);
        parcel.writeString(spellInit);
    }
}
