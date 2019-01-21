package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.common.ReceiverTool;


/**
 * 认证失败界面
 * Created by Administrator on 2018/4/12.
 */

public class VerifyFailureActivity extends BaseActivity implements View.OnClickListener{

    TextView tvReset,tvDesc,tv_zbrz;
    ImageView ivBack;
    String auditDesc="";
    String from="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failure);
        auditDesc=getIntent().getStringExtra("auditDesc");
        initControls();
    }

    private void initControls()
    {
        ivBack= (ImageView) findViewById(R.id.failure_back);
        tvReset= (TextView) findViewById(R.id.failure_reset);
        tvDesc=(TextView) findViewById(R.id.failure_desc);
        tv_zbrz= (TextView) findViewById(R.id.tv_zbrz);
        tv_zbrz.setOnClickListener(this);

        if(!TextUtils.isEmpty(auditDesc)){
            tvDesc.setText(auditDesc);
        }
        ivBack.setOnClickListener(this);
        tvReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.failure_back:
                finish();
                break;
            case R.id.failure_reset:
//                startActivity(new Intent(this, DistributionActivity.class).putExtra("type",2));
                startActivity(new Intent(this, AuthenticateActivity.class));
                finish();
                break;
            case R.id.tv_zbrz:
                AppManager.getAppManager().finishActivity(DistributorActivity.class);
                AppManager.getAppManager().finishActivity(AuthenticateActivity.class);
                sendBroadcast(new Intent(ReceiverTool.MYFRAGMENTMODULE));
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // sendBroadcast(new Intent(ReceiverTool.REFRESHMYFRAGMENTMODULE));
    }
}
