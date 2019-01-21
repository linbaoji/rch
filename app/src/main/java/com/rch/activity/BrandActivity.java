package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.adatper.BrandAdatper;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.JsonTool;
import com.rch.common.ToastTool;
import com.rch.custom.CustomContactViewControl;
import com.rch.custom.LoadingView;
import com.rch.entity.BrandEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class BrandActivity extends BaseActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    CustomContactViewControl customContactViewControl;
    BrandAdatper adatper;
    ImageView back;
    List<BrandEntity> list=new ArrayList<>();
    private LoadingView load_view;
    TextView title;

    int type=1;//1---选车品牌，2----找车服务

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        initToolBar();
        type=getIntent().getIntExtra("type",1);
        initControls();
        setData();
    }


    private void initControls() {
        recyclerView= (RecyclerView) findViewById(R.id.brand_recycler);
        customContactViewControl= (CustomContactViewControl) findViewById(R.id.brand_chat);
        back= (ImageView) findViewById(R.id.brand_back);
        title= (TextView) findViewById(R.id.more_shop_title);

        /*if(type==1)
            title.setText("选择品牌");
        else
            title.setText("品牌型号");*/
        back.setOnClickListener(this);
        load_view= (LoadingView) findViewById(R.id.load_view);
        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                requestBrandData();
            }
        });
    }
    LinearLayoutManager linearLayoutManager=null;
    private void setData() {
        //setTestData();
        requestBrandData();
        linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adatper=new BrandAdatper(this,list,type);
        recyclerView.setAdapter(adatper);

        customContactViewControl.setOnSingerNameIndexerClicked(new CustomContactViewControl.OnSingerNameIndexerClicked() {
            @Override
            public void singerNameItemClicked(String selectName) {
                //该字母首次出现的位置
                int position = adatper.getPositionForSection(selectName);
                if (position != -1) {
                    linearLayoutManager.scrollToPositionWithOffset(position, 0);
                    //recyclerView.scrollToPosition(position);
                }
            }
        });

        adatper.setOnSelBrandNameInterface(new BrandAdatper.onSelBrandNameInterface() {
            @Override
            public void onItem(BrandEntity entity) {
                setResult(RESULT_OK, new Intent().putExtra("BrandEntity", entity));
                finish();
                /*if(type==1) {
                    setResult(RESULT_OK, new Intent().putExtra("BrandEntity", entity));
                    finish();
                }else{
                    *//*选择产品*//*
                }*/
            }
        });
    }



    private void requestBrandData()
    {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        OkHttpUtils.post(Config.BRANDSLIST, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                load_view.loadComplete();
                String[] strChat= JsonTool.getChatSize(data);
                customContactViewControl.setIndexs(strChat);
                list=JsonTool.getBrandListData(data);
                if(list.size()==0){
                    load_view.noContent();
                    load_view.setNoContentTxt("暂无数据");
                }else {
                    adatper.updateBrandData(list);
                }
            }

            @Override
            public void onError(int code, String error) {
                load_view.loadError();
                ToastTool.show(BrandActivity.this,error);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.brand_back:
                finish();
                break;
        }
    }
}
