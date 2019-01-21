package com.rch;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.rch.base.SecondBaseActivity;
import com.rch.base.SoftKeyBoardListener;
import com.rch.common.Config;
import com.rch.common.GeneralUtils;
import com.rch.common.GsonUtils;
import com.rch.common.ReceiverTool;
import com.rch.common.RoncheUtil;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.entity.SearchEntity;
import com.rch.entity.VersionBean;
import com.rch.fragment.BusinessCircleFragment;
import com.rch.fragment.HomeFragment;
import com.rch.fragment.MyFragment;
import com.rch.fragment.NewCarFragment;
import com.rch.fragment.WorkbenchFragment;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import com.zhuge.analysis.stat.ZhugeSDK;

import java.util.ArrayList;
import java.util.List;

import com.networkbench.agent.impl.NBSAppAgent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/10/16.
 */

public class NewMainActivity extends SecondBaseActivity implements SoftKeyBoardListener.OnSoftKeyBoardChangeListener{
    @ViewInject(R.id.fram_main)
    private FrameLayout fram_main;
    @ViewInject(R.id.rd_grpup)
    private RadioGroup rd_grpup;
    @ViewInject(R.id.rb_home)
    private RadioButton rb_home;
    @ViewInject(R.id.rb_chosecar)
    private RadioButton rb_chosecar;
    @ViewInject(R.id.rb_worktab)
    private RadioButton rb_worktab;
    @ViewInject(R.id.rb_my)
    private RadioButton rb_my;
    @ViewInject(R.id.rb_bcircle)
    private RadioButton rb_bcircle;
    @ViewInject(R.id.iv_customer)
    private ImageView iv_customer;

    private FragmentManager fragmentManager;

    private HomeFragment homeFragment;
    private NewCarFragment carFragment;
    private WorkbenchFragment workbeanchFragment;
    private MyFragment myfragment;
    private BusinessCircleFragment circlefragment;
    private GeneralUtils generalUtils;
    private SoftKeyBoardListener listener;

    String[] permissions = new String[]{
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};

    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();





    @Override
    public void setLayout() {
        setContentView(R.layout.act_newmain);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        //打开诸葛IO统计
        ZhugeSDK.getInstance().openDebug();
        ZhugeSDK.getInstance().init(getApplicationContext());
        listener=new SoftKeyBoardListener(this);
        listener.setListener(NewMainActivity.this,this);
        showFrag(0);
        rd_grpup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_home:
                        showFrag(0);//显示不同fragmenet
                        break;
                    case R.id.rb_chosecar:
                        showFrag(1);//显示不同fragmenet
                        break;
                    case R.id.rb_worktab:
                        showFrag(2);//显示不同fragmenet
                        break;
                    case R.id.rb_my:
                        showFrag(3);//显示不同fragmenet
                        break;
                    case R.id.rb_bcircle:
                        showFrag(4);//显示不同fragmenet
                        break;
                }
            }
        });

    }


    private void showFrag(int i) {
        fragmentManager=getSupportFragmentManager();//申明
        FragmentTransaction transaction= fragmentManager.beginTransaction();
        hideFragment(transaction);//影长fragment
        switch (i){
            case 0:
                if(homeFragment==null){
                    homeFragment=new HomeFragment();
                    transaction.add(R.id.fram_main,homeFragment);
                    //预先加载选车界面并隐藏不显示，防止用户第一次打开app时直接选择二手车跳转选车界面不切换
                    carFragment=new NewCarFragment();
                    transaction.add(R.id.fram_main,carFragment);
                    transaction.hide(carFragment);
                }else {
                    transaction.show(homeFragment);
                    homeFragment.upDateUi();//更新ui
                }
                rb_home.setChecked(true);
                break;
            case 1:
                if(carFragment==null){
                    carFragment=new NewCarFragment();
                    transaction.add(R.id.fram_main,carFragment);
                }else {
                    transaction.show(carFragment);
//                    carFragment.upDateUi();
                }
                rb_chosecar.setChecked(true);
                break;


            case 2:
                if(workbeanchFragment==null){
                    workbeanchFragment=new WorkbenchFragment();
                    transaction.add(R.id.fram_main,workbeanchFragment);
                }else {
                    transaction.show(workbeanchFragment);
                }
                rb_worktab.setChecked(true);
                break;
            case 3:
                if(myfragment==null){
                    myfragment=new MyFragment();
                    transaction.add(R.id.fram_main,myfragment);
                }else {
                    transaction.show(myfragment);
                }
                rb_my.setChecked(true);
                break;
            case 4:
                if(circlefragment==null){
                    circlefragment=new BusinessCircleFragment();
                    transaction.add(R.id.fram_main,circlefragment);
                }else {
                    transaction.show(circlefragment);
                    circlefragment.upDate();
                }
                rb_bcircle.setChecked(true);
                break;


        }
        transaction.commitAllowingStateLoss();



            registPersim();//动态注册权限并且获取版本更新

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if(carFragment!=null){
            transaction.hide(carFragment);
        }
        if (workbeanchFragment != null) {
            transaction.hide(workbeanchFragment);
        }
        if (myfragment != null) {
            transaction.hide(myfragment);
        }
        if(circlefragment!=null){
            transaction.hide(circlefragment);
        }
    }




    final int REQUESTWRITEPERIMISSINCODE=102;//读写权限
    private void registPersim() {
        RequestParam param = new RequestParam();
        param.add("versionType","1");//android
        OkHttpUtils.post(Config.APPVERSION, param, new OkHttpCallBack(mContext,"") {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject jsonObject=new JSONObject(data);
                    JSONObject result=jsonObject.getJSONObject("result");
                    VersionBean bean=GsonUtils.gsonToBean(result.toString(),VersionBean.class);
                    if(Integer.parseInt(bean.getVersionCode()+"")>Integer.parseInt(RoncheUtil.getVersionCode(mContext))){

                        mPermissionList.clear();
                        for (int j = 0; j < permissions.length; j++) {
                            if (ContextCompat.checkSelfPermission(NewMainActivity.this, permissions[j]) != PackageManager.PERMISSION_GRANTED) {
                                mPermissionList.add(permissions[j]);
                            }
                        }

                        if (!mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(permissions, REQUESTWRITEPERIMISSINCODE);
                            }
                        }else {
                            if(SpUtils.getIsShowUpdate(mContext,bean.getVersionCode()+"")) {
                                generalUtils = GeneralUtils.getInstance();
                                generalUtils.toVersion(NewMainActivity.this, "1");
                            }
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void onError(int code, String error) {

            }
        });


    }



    /**
     *点击首页里面的新车，高端二手车
     * @param index activity对应的index
     * @param pos 选车fragment对应的index
     */
    public void setNavigationBackground(int index,int pos){

        showFrag(index);
//        if (index == 1){
//            if (carFragment!=null)
        carFragment.showCurrentFrg(pos);
//        }
    }

    /**
     *点击首页里面的明星车款
     *
     *
     */
    public void setNavigationBackgroundforStart(){
        showFrag(1);
        carFragment.fromStartClick();//方法通知选车fragment全部推荐
    }

    private Receive receiver;
    @Override
    protected void onResume() {
        super.onResume();
        if (receiver==null){
            receiver=new Receive();
            IntentFilter intentFilter=new IntentFilter();
            intentFilter.addAction(ReceiverTool.REFRESHCARFRAGMENTMODULE);
            intentFilter.addAction(ReceiverTool.REFRESHHOMEFRAGMENTMODULE);
            intentFilter.addAction(ReceiverTool.MYFRAGMENTMODULE);
            registerReceiver(receiver, intentFilter);
        }
//        registPersim();//动态注册权限并且获取版本更新
        setWorkbenchIsVisiable();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("from_collection",false)){
              showFrag(1);
        }else if (intent.getBooleanExtra("from_pre_order",false)) {
              showFrag(0);
        }else if (intent.getBooleanExtra("from_SearchActivity",false)) {
              showFrag(1);
              SearchEntity searchentity= (SearchEntity) intent.getExtras().getSerializable("serchentity");
              carFragment.fromSerchBrand(searchentity);
        }else if(intent.getBooleanExtra("from_verifysucess",false)){
               showFrag(0);
        }else if (intent.getBooleanExtra("from_authen",false)){
            showFrag(0);
        }
    }



    //设置工作台是否可见
    public void setWorkbenchIsVisiable(){
        //判断是否登录，未登录，工作台不显示，登录判断登陆者身份(未认证,普通用户，员工，工作台不显示)
//        if (isLogin()){
//            if(getUserInfo().getUserRoleType().equals("102")||getUserInfo().getUserRoleType().equals("202")||
//                    getUserInfo().getUserRoleType().equals("302")||
//                    getUserInfo().getUserRoleType().equals("401")||getUserInfo().getUserRoleType().equals("501")) {
//                rb_worktab.setVisibility(View.GONE);
//                if(getUserInfo().getIfDailyadmin().equals("1")){
//                    rb_worktab.setVisibility(View.VISIBLE);
//                }
//            }else {
//                   rb_worktab.setVisibility(View.VISIBLE);
//            }
//        }else {
//            rb_worktab.setVisibility(View.GONE);
//        }
    }

    //设置悬浮球是否显示(1.2.1版本客服悬浮球不显示,故直接这里全部隐藏)
    public void setIvCustomerVisiable(boolean isVisiable){
        iv_customer.setVisibility(View.GONE);
//        if (isVisiable){
//            iv_customer.setVisibility(View.VISIBLE);
//        }else {
//            if (iv_customer!=null)
//                iv_customer.setVisibility(View.GONE);
//            else
//                Log.e("======","iv_customer==null00");
//        }
    }

    @Override
    public void keyBoardShow(int height) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rd_grpup.setVisibility(View.GONE);
                    }
                });
            }
        }.start();
    }

    @Override
    public void keyBoardHide(int height) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rd_grpup.setVisibility(View.VISIBLE);
                    }
                });
            }
        }.start();
    }

    private class Receive extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ReceiverTool.REFRESHCARFRAGMENTMODULE)) {
//                navigationBackground(MainActivity.NavigationCode.BLOCKTWO);
                showFrag(1);
            }
            else if(intent.getAction().equals(ReceiverTool.REFRESHHOMEFRAGMENTMODULE)) {//切换到首页
//                navigationBackground(MainActivity.NavigationCode.BLOCKONE);
                showFrag(0);
            }
            else if(intent.getAction().equals(ReceiverTool.MYFRAGMENTMODULE)) {//切到我的页面
//                navigationBackground(MainActivity.NavigationCode.BLOCKONE);
                showFrag(3);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUESTWRITEPERIMISSINCODE:
//                if(!permissions[0].equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)||grantResults[0]!=PackageManager.PERMISSION_GRANTED)
//                {
//                    ToastTool.show(NewMainActivity.this,"请手动开启文件写入权限！");
//                    return;
//                }
//                else if(!permissions[1].equals(android.Manifest.permission.READ_EXTERNAL_STORAGE)||grantResults[1]!=PackageManager.PERMISSION_GRANTED)
//                {
//                    ToastTool.show(NewMainActivity.this,"请手动开启文件读取权限！");
//                    return;
//                }

                for(int i=0;i<grantResults.length;i++){
                  if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){//拒绝
                      //判断是否勾选禁止后不再询问
                      boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(NewMainActivity.this, permissions[i]);
                      if (showRequestPermission) {//是否被勾选默认
//                          ToastTool.show(NewMainActivity.this,"请手动开启文件读取权限！");
                          return;
                      }else {//勾选上了
                          return;
                      }
                  }
                    registPersim();//动态注册权限并且获取版本更新
                }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZhugeSDK.getInstance().flush(getApplicationContext());
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
    }

    /**
     * 按两次退出键时间小于2秒退出
     */
    private final static long WAITTIME = 2000;
    private long touchTime = 0;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= WAITTIME) {
            ToastTool.show(this, "再按一次退出");
            touchTime = currentTime;
        } else {
            moveTaskToBack(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
