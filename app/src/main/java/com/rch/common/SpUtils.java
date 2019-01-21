package com.rch.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;

import com.rch.activity.CorporateInfoActivity;
import com.rch.activity.LoginActivity;
import com.rch.activity.ReleaseAct;
import com.rch.entity.CertifiedEntity;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author sunshaochi
 */
public class SpUtils {
    private final static String UMC_SP="umc_sp";
    private final static String ISFIRST = "is_first";
    private final static String SCREEN_WITH="screen_with";
    private final static String URL = "hx_url";
    private final static String HISTORY = "history_list";
    private final static String CITY = "city";
    private final static String CITY_LIST = "city_list";
    private final static String TOKEN = "token";
    private final static String ISLOGIN = "islogin";
    private final static String CACEPIC="CACEPIC";
    private final static String CARTIFIED="CARTIFIED";
    private final static String CTTYID="CITYID";
    private final static String CACESERCH="CACESERCH";
    private final static String ISSHOWUPDATE="ISSHOWUPDATE";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static SharedPreferences getSharedPreferences(Context context)
    {
        return context.getSharedPreferences("rch",MODE_PRIVATE);
    }
    public static void clearAll(Context context) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences=getSharedPreferences(context);
        editor= sharedPreferences.edit();
        editor.clear();
        editor.commit();
        SpUtils.setIsLogin(context,false);
        SpUtils.setToken(context,"");
        setOutStatus(context);
    }

    public static void setOutStatus(Context context) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences=getSharedPreferences(context);
        editor= sharedPreferences.edit();
        editor.putBoolean("isOut",true);
        editor.commit();
    }

    //保存
    public static void saveUser(Context context,String phone,String json) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences=getSharedPreferences(context);
        editor= sharedPreferences.edit();
        editor.putString("phone",phone);
        editor.putString("jsonStr",json);
        editor.commit();
    }

    public static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(UMC_SP, MODE_PRIVATE);
    }


    /**
     * 保存用户是否同意过协议
     * @param context
     * @param phoneNumber 保存的key
     * @param isAgreement 是否同意
     */
    public static void saveUserStatus(Context context,String phoneNumber,boolean isAgreement){
        getSp(context).edit().putBoolean(phoneNumber,isAgreement).commit();
    }

    /**
     * 获取用户是否同意过协议
     * @param context
     * @param phoneNumber
     * @return
     */
    public static boolean getUserStatus(Context context,String phoneNumber){
        return getSp(context).getBoolean(phoneNumber,false);
    }
    /**
     * 是否第一次进入app
     *
     * @param context
     * @param isFirst
     */
    public static void setIsFirst(Context context, boolean isFirst) {
        getSp(context).edit().putBoolean(ISFIRST, isFirst).commit();
    }

    public static boolean getIsFirst(Context context) {
        return getSp(context).getBoolean(ISFIRST, true);
    }





    public static void setScreenWith(Context context,int screenWith){
        getSp(context).edit().putInt(SCREEN_WITH, screenWith).commit();
    }

    public static int getScreenWith(Context context){
        return getSp(context).getInt(SCREEN_WITH, 0);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context 上下文
     * @param pxValue 像素值
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取手机屏幕宽
     *
     * @param activity
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // float density = metrics.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        // int densityDpi = metrics.densityDpi; // 屏幕密度DPI（120 / 160 / 240）
        return metrics.widthPixels;
    }


    /**
     * 根据图片的宽高比获取控件宽高比
     *
     * @param
     */
    public static int get_View_heigth(int screen_with,int heigt,int width) {
        int screen_height = screen_with *heigt / width;
        return screen_height;
    }


    /**
     * url
     *
     * @param context
     * @param url
     */
    public static void setHxurl(Context context, String url) {
        getSp(context).edit().putString(URL, url).commit();
    }

    public static String getHxurl(Context context) {
        return getSp(context).getString(URL, "");
    }


    /**
     *
     *
     *
     * 清偏好设置
     */
    public static void clearSp(Context context){
//        getSp(context).edit().clear().commit();
        setToken(context,"");
        setIsLogin(context,false);
    }


    public static void sethistoty(Context context, String json) {
        getSp(context).edit().putString(HISTORY, json).commit();
    }

    public static String gethistory(Context context) {
        return getSp(context).getString(HISTORY, "");
    }

    public static void setLoacationCity(Context context,String gpsCity) {
        getSp(context).edit().putString(CITY, gpsCity).commit();
    }

    public static String getLoacationCity(Context context) {
        return getSp(context).getString(CITY, "");
    }

    //保存城市列表
    public static void setLoacationCityList(Context context,String cityJson) {
        getSp(context).edit().putString(CITY_LIST, cityJson).commit();
    }

    public static String getLoacationCityList(Context context) {
        return getSp(context).getString(CITY_LIST, "");
    }

    public static void setToken(Context context, String token) {
        getSp(context).edit().putString(TOKEN, token).commit();

    }
    public static String getToken(Context context) {
        return getSp(context).getString(TOKEN, "");

    }

    public static void setIsLogin(Context context, boolean b) {
        getSp(context).edit().putBoolean(ISLOGIN, b).commit();

    }

    public static boolean getIsLogin(Context context) {
        return getSp(context).getBoolean(ISLOGIN, false);
    }


    /**
     * 保存二手车发布时候缓存通过token
     * @param context
     * @param token
     * @param json
     */
    public static void setCaceOldCar(Context context,String token,String json) {
        getSp(context).edit().putString(token, json).commit();

    }

    /**
     * 获取二手车发布时候缓存通过token唯一人
     * @param context
     * @param token
     * @return
     */
    public static String getCaceOldCar(Context context,String token) {
        return getSp(context).getString(token, "");
    }

    /**
     * 保存二手车发布时候缓存通过token
     * @param context
     * @param
     * @param json
     */
    public static void setCaceoldCarPic(Context context,String json) {
        getSp(context).edit().putString(CACEPIC, json).commit();

    }

    public static CharSequence getCaceoldCarPic(Context context) {
        return getSp(context).getString(CACEPIC, "");
    }

    /**
     * 设置
     * @param context
     * @param json
     */
    public static void setCartifi(Context context,String tokentype,String json) {
        getSp(context).edit().putString(tokentype, json).commit();

    }

    public static String getCartifi(Context context,String tokentype) {
        return getSp(context).getString(tokentype,"");
    }

    public static void setCityid(Context context,String cityid) {
        getSp(context).edit().putString(CTTYID, cityid).commit();
    }

    public static String getCityid(Context context) {
        return getSp(context).getString(CTTYID,"");
    }

    public static void setCaceSearchList(Context context,String search) {
        getSp(context).edit().putString(CACESERCH, search).commit();
    }
    public static String getCaceSearchList(Context context) {
        return getSp(context).getString(CACESERCH,"");
    }

    /**
     * 设置是否每次提醒更新
     * @param context
     * @param b
     */
    public static void setIsShowUpdate(Context context,String versionName,boolean b) {
        getSp(context).edit().putBoolean(versionName,b).commit();
    }

    /**
     * 默认给true每次提醒
     * @param context
     * @return
     */
    public static boolean getIsShowUpdate(Context context,String versionName) {
        return getSp(context).getBoolean(versionName,true);
    }


//    SharedPreferences sharedPreferences=getSharedPreferences("UMY", Activity.MODE_PRIVATE);
//                        sharedPreferences.edit().clear().commit();




}
