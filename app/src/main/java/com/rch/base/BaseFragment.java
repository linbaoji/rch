package com.rch.base;


import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rch.R;
import com.rch.common.JsonTool;
import com.rch.custom.LoadingDialog;
import com.rch.entity.UserInfoEntity;
import com.rch.view.statusbar.BarUtils;
import com.rch.view.statusbar.StatusBarCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by Administrator on 2017/8/2.
 */

public abstract class BaseFragment extends Fragment {

    protected ImageLoader imageLoader;

    protected DisplayImageOptions options;

    protected  int width,height,versionCode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    protected UserInfoEntity userInfoEntity;

    LoadingDialog upLoadingDialog;
    Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen();
        gson=new Gson();
    }


    @Override
    public void onResume() {
        super.onResume();
        boolean isOut=getSharedPreferences().getBoolean("isOut",false);
        if(isOut)
        {
            userInfoEntity=null;
            setOutStatus();
        }
    }

    protected void screen()
    {
        DisplayMetrics ds=new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(ds);
        Log.e("width",String.valueOf(ds.density));
        Log.e("height",String.valueOf(ds.densityDpi));

        width= getActivity().getWindowManager().getDefaultDisplay().getWidth();
        height=getActivity().getWindowManager().getDefaultDisplay().getHeight();
        //keyHeight=height/3;
        Log.e("width1",String.valueOf(width));
        Log.e("height1",String.valueOf(height));

        try {
            PackageInfo info= getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0);
            versionCode=info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void upLoadingShow()
    {
        upLoadingDialog=new LoadingDialog(getActivity());
        upLoadingDialog.setText("加载中...");
        upLoadingDialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        upLoadingDialog.getWindow().setDimAmount(0f);//核心代码 解决了无法去除遮罩问
        upLoadingDialog.showAnim();
        upLoadingDialog.show();
    }

    protected void upLoadingShow(String strPrompt)
    {
        upLoadingDialog=new LoadingDialog(getActivity());
        upLoadingDialog.setText(strPrompt);
        upLoadingDialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        upLoadingDialog.getWindow().setDimAmount(0f);//核心代码 解决了无法去除遮罩问
        upLoadingDialog.showAnim();
        upLoadingDialog.show();
    }

    protected void upLoadingClose()
    {
        if(upLoadingDialog!=null) {
            upLoadingDialog.closeAnim();
            upLoadingDialog.cancel();
        }
    }


    protected void setOutStatus()
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putBoolean("isOut",true);
        editor.commit();
    }

    /*是否认证*/
 /*   protected void setCertified(boolean isCertified)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putBoolean("isCertified",isCertified);
        editor.commit();
    }

    protected boolean getCertified()
    {
        return getSharedPreferences().getBoolean("isCertified",false);
    }*/



    //清除
    protected void clearUser()
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    protected String getLocationCity()
    {
        return getSharedPreferences().getString("city","");
    }

    protected void setLocationCity(String cityName)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putString("city",cityName);
        editor.commit();
    }

    //boolean isOut;
    protected UserInfoEntity getUserInfo()
    {
//        if(userInfoEntity==null||!userInfoEntity.getJsonData())
            getUserObject();
        return userInfoEntity;
    }


    /*用户退出，设置刷新一次*/
    protected void setIsOut()
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putBoolean("userOut",false);
        editor.commit();
    }



/*
    *//*设置本地是否存在JSON*//*
    protected void setJsonStrStatus(boolean flag)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putBoolean("isExistJsonStr",flag);
        editor.commit();
    }


    protected boolean getJsonStrStatus()
    {
        return   getSharedPreferences().getBoolean("isExistJsonStr",false);
    }*/



    /*个人认证抛框*/
    protected void setPersonalDialog(boolean flag)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putBoolean("personalDialog",flag);
        editor.commit();
    }

    /*个人认证抛框*/
    protected boolean getPersonalDialog()
    {
        return getSharedPreferences().getBoolean("personalDialog",false);
    }

    //登录对象
    protected void getUserObject()
    {
        sharedPreferences=getSharedPreferences();
        String jsonStr= sharedPreferences.getString("jsonStr","");
        Log.e("baseFragment",jsonStr);
//        List<UserInfoEntity> list=null;
//        list= JsonTool.getLoginResult(jsonStr);
//        if(list!=null&&list.size()>0)
//            this.userInfoEntity=list.get(0);
//        else
//            this.userInfoEntity=new UserInfoEntity();
        if(!TextUtils.isEmpty(jsonStr)){
            try {
                JSONObject object=new JSONObject(jsonStr.toString());
                JSONObject json=object.getJSONObject("result");
                JSONObject loginResultUser=json.getJSONObject("loginResultUser");
                userInfoEntity=gson.fromJson(loginResultUser.toString(),UserInfoEntity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            userInfoEntity=new UserInfoEntity();
        }
    }

    protected void setLoginJsonStr(String jsonStr)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putString("jsonStr",jsonStr);
        editor.commit();
    }


    /*Ture---已登录，flase----未登录*/
    protected boolean isLogin()
    {
        return getUser().isEmpty()?false:true;
    }

    protected String getUser()
    {
        try{
            sharedPreferences=getSharedPreferences();
            return sharedPreferences.getString("phone","");
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    private SharedPreferences getSharedPreferences()
    {
        return getActivity().getSharedPreferences("rch",getActivity().MODE_PRIVATE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint())
            onVisible();
        else
            onInvisible();
    }

    protected abstract void onVisible(); //是否可见
    protected abstract void onInvisible(); //是否不可见
}
