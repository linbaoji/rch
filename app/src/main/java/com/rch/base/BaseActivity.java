package com.rch.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rch.R;
import com.rch.common.JsonTool;
import com.rch.common.SpUtils;
import com.rch.custom.LoadingDialog;
import com.rch.entity.UserInfoEntity;
import com.rch.view.statusbar.BarUtils;
import com.rch.view.statusbar.StatusBarCompat;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by Administrator on 2017/8/1.
 */

public class BaseActivity extends AppCompatActivity {

   /* protected ImageLoader imageLoader;

    protected DisplayImageOptions options;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
*/
   protected ImageLoader imageLoader;

    protected DisplayImageOptions options;


    String token="";

    private MyApplication application;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    protected static int width=0,height=0,keyHeight,versionCode;

    UserInfoEntity userInfoEntity;

    LoadingDialog upLoadingDialog;

    Gson gson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题栏
        // 系统 6.0 以上 状态栏白底黑字的实现方法
//        this.getWindow()
//                .getDecorView()
//                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        AppManager.getAppManager().addActivity(this);
        initScreenOrientation();
        screen();
        PushAgent.getInstance(this).onAppStart();
        if (application == null) {
            // 得到Application对象
            application = (MyApplication) getApplication();
        }
        gson=new Gson();
        //默认不调取输入盘
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    protected void initToolBar() {
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.transparent), true);
        StatusBarCompat.setTranslucent(this.getWindow(), true);
        if (Build.VERSION.SDK_INT >= 19 && this.findViewById(R.id.toolbar) != null) {
            Toolbar toolBar = (Toolbar) this.findViewById(R.id.toolbar);
            int statusBarHeight = BarUtils.getStatusBarHeight();
            toolBar.setPadding(0, statusBarHeight, 0, 0);
            toolBar.getLayoutParams().height = statusBarHeight + (int) TypedValue.applyDimension(1, 45.0F, this.getResources().getDisplayMetrics());
        }else if (Build.VERSION.SDK_INT < 19 && this.findViewById(R.id.view_top)!=null){
            this.findViewById(R.id.view_top).setVisibility(View.GONE);
        }
    }
    protected void upLoadingShow()
    {
        upLoadingDialog=new LoadingDialog(this);
        upLoadingDialog.setText("加载中...");
        upLoadingDialog.getWindow().setBackgroundDrawable(new BitmapDrawable());
        upLoadingDialog.getWindow().setDimAmount(0f);//核心代码 解决了无法去除遮罩问
        upLoadingDialog.showAnim();
        upLoadingDialog.show();
    }

    protected void upLoadingShow(String strPrompt)
    {
        upLoadingDialog=new LoadingDialog(this);
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


    protected void initScreenOrientation()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    protected void screen()
    {
        DisplayMetrics ds=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ds);
        Log.e("width",String.valueOf(ds.density));
        Log.e("height",String.valueOf(ds.densityDpi));

        width=getWindowManager().getDefaultDisplay().getWidth();
        height=getWindowManager().getDefaultDisplay().getHeight();
        keyHeight=height/3;
        Log.e("width1",String.valueOf(width));
        Log.e("height1",String.valueOf(height));

        try {
            PackageInfo info= getPackageManager().getPackageInfo(getPackageName(),0);
            versionCode=info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    protected int getOutOfMemoryActivityIndex()
    {
        sharedPreferences=getSharedPreferences();
        int outOfMemory= sharedPreferences.getInt("outOfMemory",0);
        Log.e("localJsonStr",String.valueOf(outOfMemory));
        return outOfMemory;
    }


    protected void setOutOfMemoryActivityIndex(int index)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putInt("outOfMemory",index);
        editor.commit();
    }

    /*个人认证抛框*/
    protected void setPersonalDialog(boolean flag)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putBoolean("personalDialog",flag);
        editor.commit();
    }

    protected void removePersonalDialog()
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.remove("personalDialog");
        editor.commit();
    }




    //获取当前页面索引
   /* protected int getOutOfMemoryActivityIndex()
    {
        sharedPreferences=getSharedPreferences();
        int outOfMemory= sharedPreferences.getInt("outOfMemory",0);
        Log.e("localJsonStr",String.valueOf(outOfMemory));
        return outOfMemory;
    }*/

    protected UserInfoEntity getUserInfo()
    {
//        if(userInfoEntity==null||!userInfoEntity.getJsonData())
            getUserObject();
            return userInfoEntity;
    }

    /*认证数据保存*/
    protected void setCorporateData(String corporateData)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putString("corporateData",corporateData);
        editor.commit();
    }


    protected String getCorporateData()
    {
        return getSharedPreferences().getString("corporateData","");
    }

    /*清除认证数据保存*/
    protected void clearCorporateData()
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.remove("corporateData");
        editor.commit();
    }


    /*卖车数据保存*/
    protected void setSellData(String sellData)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putString("sellData",sellData);
        editor.commit();
    }


    protected String getSellData()
    {
        return getSharedPreferences().getString("sellData","");
    }

    /*清除卖车数据保存*/
    protected void clearSellData()
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.remove("sellData");
        editor.commit();
    }


    protected void setActivityList(Activity activity)
    {
        application.addActivity(activity);
    }

    protected void removeActivityList(Activity activity)
    {
        application.removeActivity(activity);
    }

    protected void removeALLActivity()
    {
        application.removeALLActivity();
    }


    /*品牌浏览记录数据保存*/
    protected void setBrowseHistory(String browseHistory)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putString("browseHistory",browseHistory);
        editor.commit();
    }


    protected String getBrowseHistory()
    {
        return getSharedPreferences().getString("browseHistory","");
    }

    protected void clearBrowseHistory()
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.remove("browseHistory");
        editor.commit();
    }


    //登录对象
    protected void getUserObject()
    {
        sharedPreferences=getSharedPreferences();
        String jsonStr= sharedPreferences.getString("jsonStr","");
//        Log.e("baseFragment",jsonStr);
//        List<UserInfoEntity> list= JsonTool.getLoginResult(jsonStr);
//        if(list!=null&&list.size()>0)
//            this.userInfoEntity=list.get(0);
//        else
//            this.userInfoEntity=new UserInfoEntity();
        if(!TextUtils.isEmpty(jsonStr)){
            try {
                JSONObject object=new JSONObject(jsonStr.toString());
                JSONObject result=object.getJSONObject("result");
                JSONObject loginResultUser=result.getJSONObject("loginResultUser");
                userInfoEntity=gson.fromJson(loginResultUser.toString(),UserInfoEntity.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            userInfoEntity=new UserInfoEntity();
        }
    }

    protected boolean isLogin()
    {
        return getUser().isEmpty()?false:true;
    }


    protected String getUser()
    {
        sharedPreferences=getSharedPreferences();
        return sharedPreferences.getString("phone","");
    }

    //保存
    protected void saveUser(String phone,String json)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putString("phone",phone);
        editor.putString("jsonStr",json);
        editor.commit();
    }

   /* protected void saveLoginCount(String loginCount)
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putString("loginCount",loginCount);
        editor.commit();
    }

    protected String getLoginCount()
    {
        return getSharedPreferences().getString("loginCount","");
    }*/


    //清除
    protected void clearUser()
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.remove("phone");
        editor.remove("jsonStr");
        editor.remove("outOfMemory");
       // editor.putBoolean("isOut",true);
        userInfoEntity=null;
       /* editor.remove("isCertified");*/
        editor.commit();
    }



    protected void clearAll()
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.clear();
        editor.commit();
        SpUtils.setIsLogin(application,false);
        SpUtils.setToken(application,"");
//        SpUtils.setIsShowUpdate(application,true);//原来逻辑是推出后赋值能在弹出来
        setOutStatus();
    }


    protected void setOutStatus()
    {
        sharedPreferences=getSharedPreferences();
        editor= sharedPreferences.edit();
        editor.putBoolean("isOut",true);
        editor.commit();
    }



    private SharedPreferences getSharedPreferences()
    {
        return getSharedPreferences("rch",MODE_PRIVATE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(this);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
                view.clearFocus();
            }
        }

        return super.dispatchTouchEvent(ev);

    }

    /**
     * 判定是否需要隐藏
     */
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
