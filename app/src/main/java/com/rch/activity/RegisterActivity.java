package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.Config;
import com.rch.common.RegTool;
import com.rch.common.ToastTool;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/4/12.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    ImageView ivBack;
    TextView tvOk,tvCode;
    EditText etName,etPhone,etCode,etInvitationCode;

    String sPhone="",sCode="",sInvitationCode="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolBar();
        initControls();
    }

    private void initControls() {
        ivBack= (ImageView) findViewById(R.id.register_back);
        tvOk= (TextView) findViewById(R.id.register_ok);
        tvCode= (TextView) findViewById(R.id.register_code);
        etName= (EditText) findViewById(R.id.register_name);
        etPhone= (EditText) findViewById(R.id.register_phone);
        etCode= (EditText) findViewById(R.id.register_et_code);
        etInvitationCode= (EditText) findViewById(R.id.register_invitation_code);

        setActivityList(this);
        ivBack.setOnClickListener(this);
        tvCode.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    /*下一步*/
    private void next()
    {
        sPhone=etPhone.getText().toString().trim();
        sCode=etCode.getText().toString().trim();
        sInvitationCode=etInvitationCode.getText().toString().trim();

        if(sPhone.isEmpty())
        {
            ToastTool.show(this,"手机号码不能为空！");
            return;
        }
        else if(!RegTool.isMobile(sPhone))
        {
            ToastTool.show(this,"手机号码不正确！");
            return;
        }
        else if(sCode.isEmpty())
        {
            ToastTool.show(this,"验证码不能为空！");
            return;
        }
        else if(sInvitationCode.isEmpty())
        {
            ToastTool.show(this,"邀请码不能为空！");
            return;
        }
        httpRegisterData();
    }

    /*用户注册-点击下一步验证*/
    private void httpRegisterData()
    {
        RequestParam param = new RequestParam();
        param.add("mobile",sPhone);
        param.add("code",sCode);
        param.add("inviteCode",sInvitationCode);
        param.add("source","3");
        OkHttpUtils.post(Config.NEXTVERIFIC, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                startActivity(new Intent(RegisterActivity.this,DistributionActivity.class).putExtra("type",0).putExtra("phone",sPhone).putExtra("inviteCode",sInvitationCode));
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(RegisterActivity.this,error);
            }
        });
    }


    /*发送验证码*/
    private void  sendCode()
    {
        sPhone=etPhone.getText().toString().trim();
        if(sPhone.isEmpty())
        {
            ToastTool.show(this,"手机号码不能为空！");
            return;
        }
        else if(!RegTool.isMobile(sPhone))
        {
            ToastTool.show(this,"手机号码不正确！");
            return;
        }
        requestCodeData();
    }

    /*请求验证码*/
    private void requestCodeData()
    {
        RequestParam param = new RequestParam();
        param.add("mobile",sPhone);
        OkHttpUtils.post(Config.CODE, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                startTime();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(RegisterActivity.this,error);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.register_back:
                finish();
                break;
            case R.id.register_ok:
                if(!ButtonUtils.isFastDoubleClick(R.id.register_ok))
                next();
                break;
            case R.id.register_code:
                if(!ButtonUtils.isFastDoubleClick(R.id.register_code))
                sendCode();
                break;
        }
    }


    int interval=1000;
    int countdown=60;
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(countdown>0) {
                countdown--;
                tvCode.setEnabled(false);
                tvCode.setText("重新获取验证码("+countdown+")");
            }
            else {
                tvCode.setText("获取验证码");
                tvCode.setEnabled(true);
                countdown = 60;
                stopTime();
            }
        }
    };


    Timer timer=null;
    TimerTask timerTask=null;

    /**
     * 开启定时器
     */
    private void startTime()
    {
        if(timer==null)
        {
            timer=new Timer();
            if(timerTask==null)
            {
                timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                };
            }
            timer.schedule(timerTask,interval,interval);
        }
    }

    /**
     * 停止定时器
     */
    private void stopTime()
    {
        if(timer!=null)
        {
            timer.cancel();
            timer.purge();
            timer=null;
        }
        if(timerTask!=null)
        {
            timerTask.cancel();
            timerTask=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivityList(this);
    }
}
