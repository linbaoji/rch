package com.rch;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rch.activity.CollectionAct;
import com.rch.activity.CustomerAct;
import com.rch.adatper.PagerAdapter;
import com.rch.base.BaseActivity;
import com.rch.base.SoftKeyBoardListener;
import com.rch.common.GeneralUtils;
import com.rch.common.ReceiverTool;
import com.rch.common.ToastTool;
import com.rch.custom.NoScrollViewPager;
import com.rch.fragment.CarFragment;
import com.rch.fragment.ClientFragment;
import com.rch.fragment.HomeFragment;
import com.rch.fragment.MyFragment;
import com.rch.fragment.FinancialFragment;

import com.rch.fragment.NewCarFragment;


import com.rch.fragment.WorkbenchFragment;

import com.zhuge.analysis.stat.ZhugeSDK;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends BaseActivity implements View.OnClickListener,SoftKeyBoardListener.OnSoftKeyBoardChangeListener{

    NoScrollViewPager main_vp;
    LinearLayout block_one,block_two,block_three,block_four,block_five,block_workbench;
    ImageView block_one_img,block_two_img,block_three_img,block_four_img,block_five_img,block_workbench_img;
    TextView block_one_text,block_two_text,block_three_text,block_four_text,block_five_text,block_workbench_text;

    private ImageView iv_customer;

    private GeneralUtils generalUtils;


    List<Fragment> fragmentList;

   LinearLayout navigation_contdainer;

    /**
     * 按两次退出键时间小于2秒退出
     */
    private final static long WAITTIME = 2000;
    private long touchTime = 0;

    private LinearLayout ll_nocontent;
    private SoftKeyBoardListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null)
            Log.e("1122334455","程序被系统回收,需要重新登录!");
        setContentView(R.layout.activity_main);
        initControls(savedInstanceState);

        registPersim();//动态注册权限并且获取版本更新

        listener=new SoftKeyBoardListener(this);
        listener.setListener(MainActivity.this,this);

//        generalUtils=new GeneralUtils();
//        generalUtils.toVersion(MainActivity.this,"1");
        setWorkbenchIsVisiable();
    }

    //设置工作台是否可见
    private void setWorkbenchIsVisiable(){
        //判断是否登录，未登录，工作台不显示，登录判断登陆者身份
        if (isLogin()){
            block_workbench.setVisibility(View.VISIBLE);
        }else {
            block_workbench.setVisibility(View.GONE);
        }
    }

    BroadcastReceiver br=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
         /*   if(intent.getAction().equals(ReceiverTool.REFRESHMYFRAGMENTMODULE))
            {
             *//*   Log.e("asd",String.valueOf(main_vp.getCurrentItem()));
                //main_vp.getChildAt()
                if(main_vp.getCurrentItem()==NavigationCode.BLOCKFOUR)
                    ((MyFragment)fragmentList.get(NavigationCode.BLOCKFOUR)).init();
                else*//*
                    navigationBackground(NavigationCode.BLOCKFOUR);
            }else */if(intent.getAction().equals(ReceiverTool.REFRESHCARFRAGMENTMODULE))
                navigationBackground(NavigationCode.BLOCKTWO);
            else if(intent.getAction().equals(ReceiverTool.REFRESHHOMEFRAGMENTMODULE))
                navigationBackground(NavigationCode.BLOCKONE);

        }
    };


    final int REQUESTWRITEPERIMISSINCODE=102;//读写权限
    private void registPersim() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadContactsPermission = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED || hasReadContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUESTWRITEPERIMISSINCODE);
                return;
            }
        }
        generalUtils = new GeneralUtils();
        generalUtils.toVersion(MainActivity.this,"1");
    }

    private void initControls(Bundle savedInstanceState) {
        main_vp = (NoScrollViewPager) findViewById(R.id.main_vp);
        block_one= (LinearLayout) findViewById(R.id.block_one);
        block_two= (LinearLayout) findViewById(R.id.block_two);
        block_three= (LinearLayout) findViewById(R.id.block_three);
        block_four= (LinearLayout) findViewById(R.id.block_four);
        block_five= (LinearLayout) findViewById(R.id.block_five);
        block_workbench = (LinearLayout) findViewById(R.id.block_workbench);


        block_one_img= (ImageView) findViewById(R.id.block_one_img);
        block_two_img= (ImageView) findViewById(R.id.block_two_img);
        block_three_img= (ImageView) findViewById(R.id.block_three_img);
        block_four_img= (ImageView) findViewById(R.id.block_four_img);
        block_five_img= (ImageView) findViewById(R.id.block_five_img);
        block_workbench_img = (ImageView) findViewById(R.id.block_workbench_img);


        block_one_text= (TextView) findViewById(R.id.block_one_text);
        block_two_text= (TextView) findViewById(R.id.block_two_text);
        block_three_text= (TextView) findViewById(R.id.block_three_text);
        block_four_text= (TextView) findViewById(R.id.block_four_text);
        block_five_text= (TextView) findViewById(R.id.block_five_text);
        block_workbench_text = (TextView) findViewById(R.id.block_workbench_text);

        navigation_contdainer= (LinearLayout) findViewById(R.id.navigation_contdainer);
        iv_customer= (ImageView) findViewById(R.id.iv_customer);


        block_one.setOnClickListener(this);
        block_two.setOnClickListener(this);
        block_three.setOnClickListener(this);
        block_four.setOnClickListener(this);
        block_five.setOnClickListener(this);
        iv_customer.setOnClickListener(this);
        block_workbench.setOnClickListener(this);



        addFragmentList();
        main_vp.setAdapter(new PagerAdapter(getSupportFragmentManager(),fragmentList));
        if(savedInstanceState!=null)
            navigationBackground(getOutOfMemoryActivityIndex());
        else
            navigationBackground(NavigationCode.BLOCKONE);

        IntentFilter intentFilter=new IntentFilter();
        //intentFilter.addAction(ReceiverTool.REFRESHMYFRAGMENTMODULE);
        intentFilter.addAction(ReceiverTool.REFRESHCARFRAGMENTMODULE);
        intentFilter.addAction(ReceiverTool.REFRESHHOMEFRAGMENTMODULE);
        registerReceiver(br, intentFilter);

        //诸葛IO
        ZhugeSDK.getInstance().openDebug();
        ZhugeSDK.getInstance().init(getApplicationContext());
    }

    /**
     * 添加Fragment碎片
     */
    private void addFragmentList()
    {
        fragmentList=new ArrayList<>();
        fragmentList.add(new HomeFragment());
//        fragmentList.add(new CarFragment());
        fragmentList.add(new NewCarFragment());
        fragmentList.add(new FinancialFragment());
        fragmentList.add(new MyFragment());//我的
        fragmentList.add(new ClientFragment());//客户管理
        fragmentList.add(new WorkbenchFragment());//工作台
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.block_one:
                navigationBackground(NavigationCode.BLOCKONE);
                break;
            case R.id.block_two:
                navigationBackground(NavigationCode.BLOCKTWO);
                break;
            case R.id.block_three:
                navigationBackground(NavigationCode.BLOCKTHREE);
                break;
            case R.id.block_four://我的
                navigationBackground(NavigationCode.BLOCKFOUR);
                break;
            case R.id.block_five://客户管理
                navigationBackground(NavigationCode.BLOCKFIVE);
                break;
            case R.id.block_workbench://工作台
                navigationBackground(NavigationCode.BLOCKWORKBENCH);
                break;

            case R.id.iv_customer:
                startActivity(new Intent(MainActivity.this, CustomerAct.class));
                break;


        }
    }

    public void setNavigationBackground(int index){
        navigationBackground(index);
    }
    /**
     * 导航条背景
     * @param index
     */
    private void navigationBackground(int index)
    {
        switch (index)
        {
            case NavigationCode.BLOCKONE:
                block_one_img.setImageResource(R.mipmap.sel_home);
                block_two_img.setImageResource(R.mipmap.no_car);
                block_three_img.setImageResource(R.mipmap.no_financial);
                block_four_img.setImageResource(R.mipmap.no_my);
                block_five_img.setImageResource(R.mipmap.no_client);
                block_workbench_img.setImageResource(R.mipmap.no_workbench);
                block_one_text.setSelected(true);
                block_two_text.setSelected(false);
                block_three_text.setSelected(false);
                block_four_text.setSelected(false);
                block_five_text.setSelected(false);
                block_workbench_text.setSelected(false);
                break;

            case NavigationCode.BLOCKTWO:
                block_one_img.setImageResource(R.mipmap.no_home);
                block_two_img.setImageResource(R.mipmap.sel_car);
                block_three_img.setImageResource(R.mipmap.no_financial);
                block_four_img.setImageResource(R.mipmap.no_my);
                block_five_img.setImageResource(R.mipmap.no_client);
                block_workbench_img.setImageResource(R.mipmap.no_workbench);
                block_one_text.setSelected(false);
                block_two_text.setSelected(true);
                block_three_text.setSelected(false);
                block_four_text.setSelected(false);
                block_five_text.setSelected(false);
                block_workbench_text.setSelected(false);
                break;

            case NavigationCode.BLOCKTHREE:
                block_one_img.setImageResource(R.mipmap.no_home);
                block_two_img.setImageResource(R.mipmap.no_car);
                block_three_img.setImageResource(R.mipmap.sel_financial);
                block_four_img.setImageResource(R.mipmap.no_my);
                block_five_img.setImageResource(R.mipmap.no_client);
                block_workbench_img.setImageResource(R.mipmap.no_workbench);
                block_one_text.setSelected(false);
                block_two_text.setSelected(false);
                block_three_text.setSelected(true);
                block_four_text.setSelected(false);
                block_five_text.setSelected(false);
                block_workbench_text.setSelected(false);
                break;

            case NavigationCode.BLOCKFOUR:
                block_one_img.setImageResource(R.mipmap.no_home);
                block_two_img.setImageResource(R.mipmap.no_car);
                block_three_img.setImageResource(R.mipmap.no_financial);
                block_four_img.setImageResource(R.mipmap.sel_my);
                block_five_img.setImageResource(R.mipmap.no_client);
                block_workbench_img.setImageResource(R.mipmap.no_workbench);
                block_one_text.setSelected(false);
                block_two_text.setSelected(false);
                block_three_text.setSelected(false);
                block_four_text.setSelected(true);
                block_five_text.setSelected(false);
                block_workbench_text.setSelected(false);
                break;

            case NavigationCode.BLOCKFIVE:
                block_one_img.setImageResource(R.mipmap.no_home);
                block_two_img.setImageResource(R.mipmap.no_car);
                block_three_img.setImageResource(R.mipmap.no_financial);
                block_four_img.setImageResource(R.mipmap.no_my);
                block_five_img.setImageResource(R.mipmap.sel_client);
                block_workbench_img.setImageResource(R.mipmap.no_workbench);
                block_one_text.setSelected(false);
                block_two_text.setSelected(false);
                block_three_text.setSelected(false);
                block_four_text.setSelected(false);
                block_five_text.setSelected(true);
                block_workbench_text.setSelected(false);
                break;
            case NavigationCode.BLOCKWORKBENCH:
                block_one_img.setImageResource(R.mipmap.no_home);
                block_two_img.setImageResource(R.mipmap.no_car);
                block_three_img.setImageResource(R.mipmap.no_financial);
                block_four_img.setImageResource(R.mipmap.no_my);
                block_five_img.setImageResource(R.mipmap.no_client);
                block_workbench_img.setImageResource(R.mipmap.sel_workbench);
                block_one_text.setSelected(false);
                block_two_text.setSelected(false);
                block_three_text.setSelected(false);
                block_four_text.setSelected(false);
                block_five_text.setSelected(false);
                block_workbench_text.setSelected(true);
                break;
        }
        main_vp.setCurrentItem(index,false);
        setOutOfMemoryActivityIndex(index);
    }

    @Override
    public void keyBoardShow(int height) {//让第二个隐藏
        new Thread(){
            @Override
            public void run() {
                super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        navigation_contdainer.setVisibility(View.GONE);
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
                        navigation_contdainer.setVisibility(View.VISIBLE);
                    }
                });
            }
        }.start();

    }


    interface NavigationCode
    {
        int BLOCKONE=0;
        int BLOCKTWO=1;
        int BLOCKTHREE=2;
        int BLOCKFOUR=3;
        int BLOCKFIVE=4;
        int BLOCKWORKBENCH=5;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZhugeSDK.getInstance().flush(getApplicationContext());

    }



    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= WAITTIME) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        } else {
            moveTaskToBack(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("from_collection",false)){
            //show carFragment
            navigationBackground(NavigationCode.BLOCKTWO);
        }else if (intent.getBooleanExtra("from_pre_order",false)) {
            navigationBackground(NavigationCode.BLOCKONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setWorkbenchIsVisiable();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUESTWRITEPERIMISSINCODE:
                if(!permissions[0].equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)||grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    ToastTool.show(MainActivity.this,"请手动开启文件写入权限！");
                    return;
                }
                else if(!permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)||grantResults[1]!=PackageManager.PERMISSION_GRANTED)
                {
                    ToastTool.show(MainActivity.this,"请手动开启文件读取权限！");
                    return;
                }

                registPersim();
                break;

        }
    }
}
