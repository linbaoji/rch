package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.StrSplitTool;
import com.rch.common.ToastTool;
import com.rch.custom.VerificationCodeView;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/4/12.
 */

public class SendCodeActivity extends BaseActivity implements View.OnClickListener{

    String phone="",code="";
    TextView tvPhone,ok,time;
    VerificationCodeView verificationCodeView;
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_code);
        initToolBar();
        phone=getIntent().getStringExtra("phone");
        initControls();
        setData();
    }

    private void initControls() {

        back= (ImageView) findViewById(R.id.code_back);
        tvPhone= (TextView) findViewById(R.id.code_phone);
        ok= (TextView) findViewById(R.id.code_ok);
        time= (TextView) findViewById(R.id.code_time);

        verificationCodeView= (VerificationCodeView) findViewById(R.id.code_verification);

        setActivityList(this);
        tvPhone.setText("+"+ StrSplitTool.intervalFour(phone));
        ok.setEnabled(false);
        ok.setOnClickListener(this);
        back.setOnClickListener(this);
        time.setOnClickListener(this);
    }

    private void setData()
    {
        requestCodeData();
        verificationCodeView.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {
            @Override
            public void onComplete(String content) {
                Log.e("code",content);
                code=content;
                ok.setSelected(true);
                ok.setEnabled(true);
                //requestLoginData();
            }

            @Override
            public void onUndone() {
                ok.setSelected(false);
                ok.setEnabled(false);
            }
        });
    }


    /*验证*/
    private void requestVerificData()
    {
        RequestParam param = new RequestParam();
        param.add("emailOrMobile",phone);
        param.add("code",code);
        param.add("registType","3");
        param.add("source","3");
        OkHttpUtils.post(Config.CHECKCODE, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                startActivity(new Intent(SendCodeActivity.this, ResetPwdActivity.class).putExtra("phone",phone).putExtra("code",code));
                finish();
               // saveUser(phone,data);
              //  removeALLActivity();
               // finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(SendCodeActivity.this,error);
            }
        });
    }

    /*请求验证码*/
    private void requestCodeData()
    {
        RequestParam param = new RequestParam();
        param.add("emailOrMobile",phone);
        OkHttpUtils.post(Config.SENDCODE, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                startTime();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(SendCodeActivity.this,error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.code_ok:
                requestVerificData();
                break;
            case R.id.code_time:
                requestCodeData();
                break;
            case R.id.code_back:
                finish();
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
                time.setVisibility(View.VISIBLE);
                time.setEnabled(false);
                time.setText("重新获取验证码("+countdown+")");
            }
            else {
                time.setText("获取验证码");
                time.setEnabled(true);
                time.setVisibility(View.INVISIBLE);
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
