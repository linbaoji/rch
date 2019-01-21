package com.rch.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.rch.activity.ReleaseAct;
import com.rch.entity.DatailBean;
import com.rch.entity.VehicleImageListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/4.
 */

public class PhotoFaUtils {

    private final static String UMC_SP="umc_photosp";
    private final static String FMURL="umc_photofm";
    private final static String ZCURL="umc_photozc";
    private final static String ZHURL="umc_photozh";
    private final static String JSCZYURL="umc_photojsczy";
    private final static String YBPURL="umc_photoybp";
    private final static String FDJCURL="umc_photofdjc";
    private final static String CTURL="umc_photoct";
    private final static String CWURL="umc_photocw";
    private final static String YCURL="umc_photoyc";
    private final static String YHCURL="umc_photoyhc";
    private final static String YQCURL="umc_photoyqc";
    private final static String RYRTURL="umc_photoryrt";
    private final static String HPZYURL="umc_photohpzy";
    private final static String HBXURL="umc_photohbx";
    private final static String JSONBEAN="json_bean";
    private final static String JSONARRAY="json_array";
    private final static String CARNEWDETAIL="car_new_detail";

    public static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(UMC_SP, Context.MODE_PRIVATE);
    }


    public static void setfmur(Context context,String imageUrlPath){
        getSp(context).edit().putString(FMURL, imageUrlPath).commit();
    }

    public static String getfmur(Context context){
        return getSp(context).getString(FMURL, "");
    }



    public static void setzcur(Context context,String imageUrlPath){
        getSp(context).edit().putString(ZCURL, imageUrlPath).commit();
    }

    public static String getzcur(Context context){
        return getSp(context).getString(ZCURL, "");
    }

    public static void setzhur(Context context,String imageUrlPath){
        getSp(context).edit().putString(ZHURL, imageUrlPath).commit();
    }

    public static String getzhur(Context context){
        return getSp(context).getString(ZHURL, "");
    }

    public static void setjsczyur(Context context,String imageUrlPath){
        getSp(context).edit().putString(JSCZYURL, imageUrlPath).commit();
    }

    public static String getjsczyur(Context context){
        return getSp(context).getString(JSCZYURL, "");
    }

    public static void setybpur(Context context,String imageUrlPath){
        getSp(context).edit().putString(YBPURL, imageUrlPath).commit();
    }

    public static String getybpur(Context context){
        return getSp(context).getString(YBPURL, "");
    }

    public static void setfdjcur(Context context,String imageUrlPath){
        getSp(context).edit().putString(FDJCURL, imageUrlPath).commit();
    }

    public static String getfdjcur(Context context){
        return getSp(context).getString(FDJCURL, "");
    }

    public static void setctur(Context context,String imageUrlPath){
        getSp(context).edit().putString(CTURL, imageUrlPath).commit();
    }

    public static String getctur(Context context){
        return getSp(context).getString(CTURL, "");
    }

    public static void setcwur(Context context,String imageUrlPath){
        getSp(context).edit().putString(CWURL, imageUrlPath).commit();
    }

    public static String getcwur(Context context){
        return getSp(context).getString(CWURL, "");
    }

    public static void setycur(Context context,String imageUrlPath){
        getSp(context).edit().putString(YCURL, imageUrlPath).commit();
    }

    public static String getycur(Context context){
        return getSp(context).getString(YCURL, "");
    }

    public static void setyhcur(Context context,String imageUrlPath){
        getSp(context).edit().putString(YHCURL, imageUrlPath).commit();
    }

    public static String getyhcur(Context context){
        return getSp(context).getString(YHCURL, "");
    }

    public static void setyqcur(Context context,String imageUrlPath){
        getSp(context).edit().putString(YQCURL, imageUrlPath).commit();
    }

    public static String getyqcur(Context context){
        return getSp(context).getString(YQCURL, "");
    }

    public static void setryrtur(Context context,String imageUrlPath){
        getSp(context).edit().putString(RYRTURL, imageUrlPath).commit();
    }

    public static String getryrtur(Context context){
        return getSp(context).getString(RYRTURL, "");
    }

    public static void sethbzyur(Context context,String imageUrlPath){
        getSp(context).edit().putString(HPZYURL, imageUrlPath).commit();
    }

    public static String gethbzyur(Context context){
        return getSp(context).getString(HPZYURL, "");
    }

    public static void sethbxur(Context context,String imageUrlPath){
        getSp(context).edit().putString(HBXURL, imageUrlPath).commit();
    }

    public static String gethbxur(Context context){
        return getSp(context).getString(HBXURL, "");
    }

    public static String getImageNum(Context context){
        List<String>list=new ArrayList<>();
        list.clear();
        if(!TextUtils.isEmpty(getfmur(context))){
            list.add(getfmur(context));
        }
        if(!TextUtils.isEmpty(getzcur(context))){
            list.add(getzcur(context));
        }
        if(!TextUtils.isEmpty(getzhur(context))){
            list.add(getzhur(context));
        }
        if(!TextUtils.isEmpty(getjsczyur(context))){
            list.add(getjsczyur(context));
        }
        if(!TextUtils.isEmpty(getybpur(context))){
            list.add(getybpur(context));
        }
        if(!TextUtils.isEmpty(getfdjcur(context))){
            list.add(getfdjcur(context));
        }
        if(!TextUtils.isEmpty(getctur(context))){
            list.add(getfmur(context));
        }
        if(!TextUtils.isEmpty(getcwur(context))){
            list.add(getcwur(context));
        }
        if(!TextUtils.isEmpty(getycur(context))){
            list.add(getycur(context));
        }
        if(!TextUtils.isEmpty(getyhcur(context))){
            list.add(getyhcur(context));
        }
        if(!TextUtils.isEmpty(getyqcur(context))){
            list.add(getyqcur(context));
        }
        if(!TextUtils.isEmpty(getryrtur(context))){
            list.add(getryrtur(context));
        }
        if(!TextUtils.isEmpty(gethbzyur(context))){
            list.add(gethbzyur(context));
        }
        if(!TextUtils.isEmpty(gethbxur(context))){
            list.add(gethbzyur(context));
        }

        return list.size()+"";
    }



    /**
     *
     *
     *
     * 清偏好设置
     */
    public static void clearSp(Context context){
        getSp(context).edit().clear().commit();
    }


    public static void setJsonBean(Context context,String json) {
        getSp(context).edit().putString(JSONBEAN, json).commit();
    }

    public static String getJsonBean(Context context) {
        return getSp(context).getString(JSONBEAN, "");
    }
    //  发布新车详情保存
    public static void setCarnewdetail(Context context,String json) {
        getSp(context).edit().putString(CARNEWDETAIL, json).commit();
    }

    public static String getCarnewdetail(Context context) {
        return getSp(context).getString(CARNEWDETAIL, "");
    }
    public static void setJsonArray(Context context,String json) {
        getSp(context).edit().putString(JSONARRAY, json).commit();
    }

    public static String getJsonArray(Context context) {

        return getSp(context).getString(JSONARRAY, "");
    }
}
