package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.RegTool;
import com.rch.common.ToastTool;

/**
 * Created by Administrator on 2018/4/12.
 */

public class LostPwdActivity extends BaseActivity implements View.OnClickListener {

    ImageView back;
    TextView login;
    EditText phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pwd);
        initToolBar();
        initControls();
    }

    private void initControls() {
        back= (ImageView) findViewById(R.id.lost_pwd_back);
        login= (TextView) findViewById(R.id.lost_pwd_ok);
        phone= (EditText) findViewById(R.id.lost_pwd_phone);

        setActivityList(this);
        login.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.lost_pwd_back:
                finish();
                break;
            case R.id.lost_pwd_ok:
                if(!ButtonUtils.isFastDoubleClick(R.id.lost_pwd_ok))
                login();
                break;
        }
    }

    private void login()
    {
        String strPhone=phone.getText().toString().trim();
        if(strPhone.isEmpty())
        {
            ToastTool.show(this,"手机号码不能为空！");
            return;
        }
        else if(!RegTool.isMobile(strPhone))
        {
            ToastTool.show(this,"手机号码不正确！");
            return;
        }
        startActivity(new Intent(LostPwdActivity.this,SendCodeActivity.class).putExtra("phone",strPhone));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivityList(this);
    }

}
