package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.adatper.DistributionAdatper;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.JsonTool;
import com.rch.common.ReceiverTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.custom.NoScrollViewPager;
import com.rch.entity.CertifiedEntity;
import com.rch.fragment.FirmFragment;
import com.rch.fragment.PersonalFragment;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/12.
 */

public class DistributionActivity extends BaseActivity implements View.OnClickListener{

    ImageView ivBack;
    TextView tvTabOne,tvTabTwo;
    NoScrollViewPager viewPager;

    List<Fragment> fragmentList;
    DistributionAdatper adatper;

    //String phone="",inviteCode="";
    int type=1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution);
        initToolBar();
     /*   phone=getIntent().getExtras().getString("phone","");
        inviteCode=getIntent().getExtras().getString("json","");*/
        type=getIntent().getExtras().getInt("type",1);
        initControls();
        setData();
        setPersonalDialog(true);
    }

    private void initControls() {
        ivBack= (ImageView) findViewById(R.id.distribution_back);
        tvTabOne= (TextView) findViewById(R.id.distribution_tab_one);
        tvTabTwo= (TextView) findViewById(R.id.distribution_tab_two);
        viewPager= (NoScrollViewPager) findViewById(R.id.distribution_vp);


        tvTabOne.setSelected(true);
        ivBack.setOnClickListener(this);
        tvTabOne.setOnClickListener(this);
        tvTabTwo.setOnClickListener(this);

    }

    private void setData() {
        addFragmentList();
        adatper=new DistributionAdatper(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adatper);
        navigationBackground(TabCode.TABONE);
        if(type==2)
            httpUserFillInInfo();
    }

    //初始化用户认证信息
    CertifiedEntity entity;
    private void httpUserFillInInfo()
    {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param=new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        OkHttpUtils.post(Config.REFRESHCERTIFIEDINFO, param, new OkHttpCallBack(this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                entity= JsonTool.getCertifiedData(data);
                if(entity!=null)
                {
                    if(entity.getAuthenticateType().equalsIgnoreCase("1"))
                        navigationBackground(TabCode.TABONE);
                    else
                        navigationBackground(TabCode.TABTWO);
                }
            }

            @Override
            public void onError(int code,String error) {
                if(code==1||code==2||code==3){
                    clearAll();
                    SpUtils.clearSp(DistributionActivity.this);
                    startActivity(new Intent(DistributionActivity.this,LoginActivity.class));
                }
                ToastTool.show(DistributionActivity.this,error);
            }
        });
    }

    /**
     * 添加Fragment碎片
     */
    FirmFragment firmFragment;
    PersonalFragment personalFragment;
    private void addFragmentList()
    {
        fragmentList=new ArrayList<>();
        firmFragment=new FirmFragment();//企业认证
//        personalFragment=new PersonalFragment();
        fragmentList.add(firmFragment);
//        fragmentList.add(personalFragment);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.distribution_back:
                finish();
                break;
            case R.id.distribution_tab_one:
                navigationBackground(TabCode.TABONE);
                break;
            case R.id.distribution_tab_two:
                navigationBackground(TabCode.TABTWO);
                break;
        }
    }

    private void initFirmFragment()
    {
        firmFragment= (FirmFragment) fragmentList.get(0);
        firmFragment.transferData(entity);
    }

    private void initPersonalFragment()
    {
        personalFragment= (PersonalFragment) fragmentList.get(1);
        personalFragment.transferData(entity);
    }




    /**
     * 导航条背景
     * @param index
     */
    private void navigationBackground(int index)
    {
        switch (index) {
            case TabCode.TABONE:
                tvTabOne.setSelected(true);
                tvTabTwo.setSelected(false);
                initFirmFragment();
                break;

            case TabCode.TABTWO:
                tvTabOne.setSelected(false);
                tvTabTwo.setSelected(true);
                initPersonalFragment();
                break;

        }
        viewPager.setCurrentItem(index,false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //sendBroadcast(new Intent(ReceiverTool.REFRESHMYFRAGMENTMODULE));
        removePersonalDialog();
    }

    interface TabCode
    {
        int TABONE=0;
        int TABTWO=1;
    }
}
