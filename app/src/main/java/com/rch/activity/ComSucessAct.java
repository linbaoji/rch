package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.common.SpUtils;

/**
 * Created by Administrator on 2018/9/3.
 */

public class ComSucessAct extends BaseActivity implements View.OnClickListener{
    private ImageView iv_finsh;
    private TextView tv_gogl,tv_continu;
    private String type;
    private String from;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_comsucess);
        initToolBar();
        initView();
        type=getIntent().getExtras().getString("type","");//5是新车发布后的成功。1是二手车
        from=getIntent().getExtras().getString("from","1");//默认是新增,2是修改

        if(type.equals("5")){//新车
            tv_gogl.setText("去管理新车");
            if(from.equals("1")){
                tv_continu.setText("继续发布新车");
                tv_continu.setVisibility(View.VISIBLE);
            }else {
                tv_continu.setVisibility(View.GONE);
            }
        }else {
            tv_gogl.setText("去管理二手车");
            if(from.equals("1")){
                tv_continu.setText("继续发布二手车");
                tv_continu.setVisibility(View.VISIBLE);
            }else {
                tv_continu.setVisibility(View.GONE);
            }
        }


    }

    private void initView() {
        iv_finsh= (ImageView) findViewById(R.id.iv_finsh);
        tv_gogl=(TextView) findViewById(R.id.tv_gogl);
        tv_continu=(TextView) findViewById(R.id.tv_continu);
        iv_finsh.setOnClickListener(this);
        tv_gogl.setOnClickListener(this);
        tv_continu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_finsh:
                setBack();
                break;

            case R.id.tv_gogl://去车辆管理
                if(type.equals("1")) {
                    SpUtils.setCaceOldCar(ComSucessAct.this, SpUtils.getToken(ComSucessAct.this), "");//清空保存
                    AppManager.getAppManager().finishActivity(ManageInfoAct.class);
                    AppManager.getAppManager().finishActivity(ReleaseAct.class);
                    sendBroadcast(new Intent(VehicleMnageAct.UPDATEUI));
                    finish();
                }else {
                    AppManager.getAppManager().finishActivity(CarManagerNewActivity.class);
                    AppManager.getAppManager().finishActivity(TabNewCarInfoAct.class);
                    AppManager.getAppManager().finishActivity(ReleaseNewCarActivity.class);
                    startActivity(new Intent(ComSucessAct.this, CarManagerNewActivity.class));
                    finish();
                }
                break;

            case R.id.tv_continu://继续发布其实跟后退是一样的
                setBack();
                break;
        }
    }

    private void setBack() {
        if(type.equals("1")) {
            SpUtils.setCaceOldCar(ComSucessAct.this, SpUtils.getToken(ComSucessAct.this), "");//清空保存
            Intent intent = new Intent(ComSucessAct.this, ReleaseAct.class);
            intent.putExtra("fromtype", "1");
            startActivity(intent);
            finish();
        }else {
            AppManager.getAppManager().finishActivity(ReleaseNewCarActivity.class);
            startActivity(new Intent(ComSucessAct.this, ReleaseNewCarActivity.class));
            finish();
        }
    }


    //写这个方法是防止点击系统后退键


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(type.equals("1")&&from.equals("1")) {
            SpUtils.setCaceOldCar(ComSucessAct.this, SpUtils.getToken(ComSucessAct.this), "");//清空保存
        }
        finish();
    }
}
