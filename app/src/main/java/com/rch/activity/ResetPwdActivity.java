package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.ToastTool;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ResetPwdActivity extends BaseActivity implements View.OnClickListener{

    ImageView ivBack;
    TextView tvAccount,tvOk;
    EditText etPwd,etConfirmPwd;

    String sPhone="",sCode="",sPwd="",sConfirmPwd="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        sPhone=getIntent().getExtras().getString("phone");
        sCode=getIntent().getExtras().getString("code");
        initControls();
    }

    private void initControls() {
        ivBack= (ImageView) findViewById(R.id.reset_back);
        tvAccount= (TextView) findViewById(R.id.reset_account);
        tvOk= (TextView) findViewById(R.id.reset_ok);
        etPwd= (EditText) findViewById(R.id.reset_pwd);
        etConfirmPwd= (EditText) findViewById(R.id.reset_confirm_pwd);


        tvAccount.setText(sPhone);

        ivBack.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // removeActivityList(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.reset_back:
                finish();
                break;
            case R.id.reset_ok:
                resetVerify();
                break;
        }
    }

    /*提交验证*/
    private void resetVerify()
    {
        sPwd=etPwd.getText().toString().trim();
        sConfirmPwd=etConfirmPwd.getText().toString().trim();
        if(sPwd.isEmpty())
        {
            ToastTool.show(this,"密码不能为空！");
            return;
        }
        if(sPwd.length()<6)
        {
            ToastTool.show(this,"密码长度不能少于6位！");
            return;
        }
        else if(sConfirmPwd.isEmpty())
        {
            ToastTool.show(this,"确认密码不能为空！");
            return;
        }else if(!sPwd.equals(sConfirmPwd))
        {
            ToastTool.show(this,"2次密码不匹配，请重新输入！");
            return;
        }
        httpResetPwd();
    }

    /*提交*/
    private void httpResetPwd()
    {
        RequestParam param=new RequestParam();
        param.add("emailOrMobile",sPhone);
        param.add("code",sCode);
        param.add("password",sPwd);
        param.add("confirmPassword",sConfirmPwd);
        param.add("source","3");
        OkHttpUtils.post(Config.RESETPWD, param, new OkHttpCallBack(this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                removeALLActivity();
                finish();
                ToastTool.show(ResetPwdActivity.this,"重置成功！");
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(ResetPwdActivity.this,error);
            }
        });
    }
}
