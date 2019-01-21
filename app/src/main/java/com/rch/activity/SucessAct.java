package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;

/**
 * Created by acer on 2018/8/18.
 */

public class SucessAct extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sucess);
        ViewUtils.inject(this);
    }

    @OnClick({R.id.serach_car_submit_back,R.id.serach_car_submit_shop})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.serach_car_submit_back:
                AppManager.getAppManager().finishActivity(OrderInfo.class);
                AppManager.getAppManager().finishActivity(FillInfoAct.class);
                finish();
                break;

            case R.id.serach_car_submit_shop:
                AppManager.getAppManager().finishActivity(OrderInfo.class);
                AppManager.getAppManager().finishActivity(FillInfoAct.class);
                finish();
                break;
        }
    }
}
