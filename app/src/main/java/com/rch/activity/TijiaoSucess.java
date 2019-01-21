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
import com.rch.common.ReceiverTool;

/**
 * Created by Administrator on 2018/9/4.
 */

public class TijiaoSucess extends BaseActivity implements View.OnClickListener{

    ImageView ivBack;
    TextView tv_tj;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tijiaosucess);
        initControls();
    }

    private void initControls() {
        ivBack= (ImageView) findViewById(R.id.material_back);
        ivBack.setOnClickListener(this);
        tv_tj= (TextView) findViewById(R.id.serach_car_submit_shop);
        tv_tj.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.material_back:
                finish();
                break;

            case R.id.tv_tj:
                sendBroadcast(new Intent(ReceiverTool.REFRESHHOMEFRAGMENTMODULE));
                finish();
                AppManager.getAppManager().finishActivity(AuthenticateActivity.class);
                break;
        }
    }
}
