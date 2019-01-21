package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.ToastTool;
import com.rch.custom.PromptDialog;
import com.rch.http.OKDownLoadCallback;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

/**
 * Created by Administrator on 2018/4/19.
 */

public class UpdatePwdActivity extends BaseActivity implements View.OnClickListener {

    TextView tvAccount,tvOldPwd,tvPwd,tvConfirmPwd,tvEcs,tvOk,tvLostPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        intiControls();
    }

    private void intiControls()
    {
        tvAccount= (TextView) findViewById(R.id.update_account);
        tvOldPwd= (TextView) findViewById(R.id.update_old_pwd);
        tvPwd= (TextView) findViewById(R.id.update_pwd);
        tvConfirmPwd= (TextView) findViewById(R.id.update_confirm_pwd);
        tvEcs= (TextView) findViewById(R.id.update_esc);
        tvOk= (TextView) findViewById(R.id.update_ok);

        tvLostPwd= (TextView) findViewById(R.id.update_lost_pwd);


        tvEcs.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        tvLostPwd.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.update_esc:
                finish();
                break;
            case R.id.update_ok:
                upPassWordVerify();
                break;
            case R.id.update_lost_pwd:
                break;
        }
    }

    String sAccount="",sOldPwd="",sPwd="",sConfirmPwd="";
    public void upPassWordVerify()
    {
        sOldPwd=tvOldPwd.getText().toString().trim();
        sPwd=tvPwd.getText().toString().trim();
        sConfirmPwd =tvConfirmPwd.getText().toString().trim();
        if(sOldPwd.isEmpty())
        {
            ToastTool.show(this,"旧密码不能为空！");
            return;
        }
        else if(sPwd.isEmpty())
        {
            ToastTool.show(this,"密码不能为空！");
            return;
        }
        else if(sPwd.length()<6)
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
        upHttpPassWord();
    }

    private void upHttpPassWord()
    {
        RequestParam param=new RequestParam();


        OkHttpUtils.post("", param, new OkHttpCallBack(this,"加载中...") {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onError(int code, String error) {

            }
        });
    }

}

