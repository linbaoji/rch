package com.rch.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.CodeUtils;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.JsonTool;
import com.rch.common.RegTool;
import com.rch.common.ToastTool;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/7/27.
 */

public class FinancialSubmitActivity  extends BaseActivity implements View.OnClickListener{
    ImageView financial_submit_back;
    EditText financial_submit_enterprise_name,financial_submit_enterprise_code,financial_submit_name,
            financial_submit_phone,financial_submit_code_text;
    TextView financial_submit_code,financial_submit_ok;

    String sEnterpriseName="", sEnterpriseCode="",sName="",sPhone="",sCode="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_submit);
        initToolBar();
        initControsl();
    }

    private void initControsl() {
        financial_submit_back= (ImageView) findViewById(R.id.financial_submit_back);

        financial_submit_enterprise_name= (EditText) findViewById(R.id.financial_submit_enterprise_name);
        financial_submit_enterprise_code= (EditText) findViewById(R.id.financial_submit_enterprise_code);
        financial_submit_name= (EditText) findViewById(R.id.financial_submit_name);
        financial_submit_phone= (EditText) findViewById(R.id.financial_submit_phone);
        financial_submit_code_text= (EditText) findViewById(R.id.financial_submit_code_text);

        financial_submit_code= (TextView) findViewById(R.id.financial_submit_code);
        financial_submit_ok= (TextView) findViewById(R.id.financial_submit_ok);

        financial_submit_code.setOnClickListener(this);
        financial_submit_back.setOnClickListener(this);
        financial_submit_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.financial_submit_back:
                finish();
                break;
            case R.id.financial_submit_code:
                if(!ButtonUtils.isFastDoubleClick(v.getId()))
                    isExceedSendCodeAmount();
                 break;
            case R.id.financial_submit_ok:
                submit();
                break;
            case R.id.code_ok:
                verifyImageCode();
                break;
            case R.id.code_image:
                ivCodeImage.setImageBitmap(codeUtils.createBitmap());
                break;

        }
    }

    public void submit()
    {
        sEnterpriseName=financial_submit_enterprise_name.getText().toString().trim();//企业名称
        sEnterpriseCode=financial_submit_enterprise_code.getText().toString().trim();//社会统一码
        sName=financial_submit_name.getText().toString().trim();//姓名
        sPhone=financial_submit_phone.getText().toString().trim();//手机号
        sCode=financial_submit_code_text.getText().toString().trim();//验证码
        if(sEnterpriseName.isEmpty())
        {
            ToastTool.show(this,"企业名称不能为空！");
            financial_submit_enterprise_name.requestFocus();
            return;
        }
        else if(sEnterpriseCode.isEmpty())
        {
            ToastTool.show(this,"信用代码不能为空！");
            financial_submit_enterprise_code.requestFocus();
            return;
        }
        else if(sName.isEmpty())
        {
            ToastTool.show(this,"姓名不能为空！");
            financial_submit_name.requestFocus();
            return;
        }else if(sPhone.isEmpty())
        {
            ToastTool.show(this,"手机号不能为空！");
            financial_submit_phone.requestFocus();
            return;
        }else if(!RegTool.isMobile(sPhone)){
            ToastTool.show(this,"请输入正确手机号！");
            financial_submit_phone.requestFocus();
        }
        else if(sCode.isEmpty())
        {
            ToastTool.show(this,"验证码不能为空！");
            return;
        }
        httpDataSubmit();
    }

    public void httpDataSubmit()
    {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source","3");
        param.add("mobile",sPhone);
        param.add("username",sName);
        param.add("enterpriseName", sEnterpriseName);
        param.add("enterpriseNo", sEnterpriseCode);
        param.add("productId","2");
        param.add("validCode",sCode);
        param.add("type","3");
        OkHttpUtils.post(Config.LOAN, param, new OkHttpCallBack(this,"") {

            @Override
            public void onSuccess(String data) {
                startActivity(new Intent(FinancialSubmitActivity.this,FinancialSubmitSuccessActivity.class));
                finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(FinancialSubmitActivity.this,error);
            }
        });
    }

    String strPhone="";
    /*短信验证码是否超过3次*/
    private void isExceedSendCodeAmount()
    {
        strPhone=financial_submit_phone.getText().toString().trim();
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
        RequestParam param = new RequestParam();
        param.add("mobile",strPhone);
        OkHttpUtils.post(Config.SENDNUMBER, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                int count = JsonTool.getResult(data,"result").isEmpty()?0:Integer.parseInt(JsonTool.getResultStr(data,"smsNumber"));
                if(count>=3)
                    showCodeImage();
                else
                    requestCodeData();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(FinancialSubmitActivity.this,error);
            }
        });
    }

    CodeUtils codeUtils;
    Dialog dialog;
    TextView tvCodeOk;
    ImageView ivCodeImage;
    EditText etCodeEt;
    /*显示验证码图片*/
    private void showCodeImage()
    {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();

        View view= LayoutInflater.from(this).inflate(R.layout.image_code_layout,null);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog=new Dialog(this);
        dialog.addContentView(view,params);
        Window window=dialog.getWindow();
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setGravity(Gravity.CENTER);
        tvCodeOk= (TextView) view.findViewById(R.id.code_ok);
        etCodeEt= (EditText) view.findViewById(R.id.code_et);
        ivCodeImage= (ImageView) view.findViewById(R.id.code_image);


        ivCodeImage.setImageBitmap(bitmap);

        tvCodeOk.setOnClickListener(this);
        ivCodeImage.setOnClickListener(this);
        dialog.show();
    }


    /*验证图形验证码*/
    private void verifyImageCode()
    {
        String sCodeText=etCodeEt.getText().toString().trim();
        //获取图形验证码
        String codeStr = codeUtils.getCode();
        if(sCodeText.equalsIgnoreCase(codeStr)) {
            if (dialog != null)
                dialog.cancel();
            requestCodeData();
        }else
            ToastTool.show(this,"验证码不正确！");
    }

    /*请求验证码*/
    private void requestCodeData()
    {
        RequestParam param = new RequestParam();
        param.add("mobile",strPhone);
        param.add("type","3");
        OkHttpUtils.post(Config.YZM, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                startTime();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(FinancialSubmitActivity.this,error);
            }
        });
    }

    int interval=1000;
    int countdown=60;
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(countdown>0) {
                countdown--;
                financial_submit_code.setEnabled(false);
                //code.setText("重新获取验证码("+countdown+")");
                financial_submit_code.setText(String.valueOf(countdown)+"S"+"重新获取");
            }
            else {
                financial_submit_code.setText("重新获取");
                financial_submit_code.setEnabled(true);
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
        stopTime();
    }
}
