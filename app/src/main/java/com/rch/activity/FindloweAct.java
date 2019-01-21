package com.rch.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.base.SecondBaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.CodeUtils;
import com.rch.common.Config;
import com.rch.common.GsonUtils;
import com.rch.common.JsonTool;
import com.rch.common.RegTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.common.ZhuGeIOTool;
import com.rch.entity.UserInfoEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/10/15.
 */

public class FindloweAct extends SecondBaseActivity{
    @ViewInject(R.id.et_name)
    private EditText et_name;
    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    @ViewInject(R.id.et_code)
    private EditText et_code;
    @ViewInject(R.id.tv_code)
    private TextView tv_code;
    @ViewInject(R.id.ll_sms)
    private LinearLayout ll_sms;

    private String name;
    private String phone;
    private String code;
    private String id;

    int isNewUser=0;
    int isBlack=0;
    int ifOverrun=0;

    CodeUtils codeUtils;


    @Override
    public void setLayout() {
        setContentView(R.layout.act_findlowe);
    }

    @Override
    public void init(Bundle savedInstanceState) {
      setTopTitle("询底价");
      codeUtils=new CodeUtils();
      id=getIntent().getExtras().getString("id","");
      if(SpUtils.getIsLogin(mContext)){//登录状态
          ll_sms.setVisibility(View.GONE);
          if(getUserInfo().getIfRealnameCertify().equals("1")){//已经实名
              et_name.setEnabled(false);
              et_name.setText(getUserInfo().getUserName());
          }else {
              et_name.setEnabled(true);
          }
          et_phone.setText(getUser()+"");
          et_phone.setEnabled(false);
      }else {
          ll_sms.setVisibility(View.VISIBLE);
      }
    }

    @OnClick({R.id.tv_code,R.id.login_ok})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_code:
                if(!ButtonUtils.isFastDoubleClick(R.id.tv_code))
                getCaptchaNum();//获取验证码发送次数
                break;

            case R.id.login_ok:
                if(!ButtonUtils.isFastDoubleClick(R.id.login_ok)) {
                    if (isVisible()) {
                        goSub();//提交
                    }
                }
                break;
        }
    }

    private void goSub() {
        RequestParam param = new RequestParam();
        param.add("userName",name);
        if(!SpUtils.getIsLogin(mContext)) {//未登录是比传
            param.add("mobile", phone);
            param.add("captcha",code);
        }
        param.add("id",id);
        OkHttpUtils.post(Config.SUBMITVEHICLEINQUIRY_URL, param, new OkHttpCallBack(mContext,"加载中") {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    if(!result.isNull("loginResultUser")){
                        saveUser(phone,data);//更新一下用户
                        JSONObject loginresultuser=result.getJSONObject("loginResultUser");
                        UserInfoEntity entity= GsonUtils.gsonToBean(loginresultuser.toString(),UserInfoEntity.class);
                        SpUtils.setToken(FindloweAct.this,entity.getToken());//保存登录token
                        SpUtils.setIsLogin(FindloweAct.this,true);//保存是否登录状态
                    }

                    startActivity(new Intent(mContext,FinancialSubmitSuccessActivity.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {
             ToastTool.show(mContext,error);
            }
        });
    }

    private boolean isVisible() {
        name=et_name.getText().toString().trim();
        phone=et_phone.getText().toString().trim();
        code=et_code.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            et_name.requestFocus();
            ToastTool.show(mContext,"请输入您的姓名");
            return false;
        }

        if(!SpUtils.getIsLogin(mContext)) {//未登录时候
            if (TextUtils.isEmpty(phone)) {
                et_phone.requestFocus();
                ToastTool.show(mContext, "请输入手机号");
                return false;
            }

            if(!RegTool.isMobile(phone)){
                et_phone.requestFocus();
                et_phone.setSelection(phone.length());
                ToastTool.show(mContext,"手机号格式不正确");
                return false;
            }

            if(TextUtils.isEmpty(code)){
                et_code.requestFocus();
                ToastTool.show(mContext,"请输入验证码");
                return false;
            }
        }

        return true;

    }

    private void getCaptchaNum() {
        phone=et_phone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            et_phone.requestFocus();
            ToastTool.show(mContext,"请输入手机号");
            return;
        }
        if(!RegTool.isMobile(phone)){
            et_phone.requestFocus();
            et_phone.setSelection(phone.length());
            ToastTool.show(mContext,"手机号格式不正确");
            return;
        }else {
            RequestParam param = new RequestParam();
            param.add("mobile",phone);
            OkHttpUtils.post(Config.SENDNUMBER, param, new OkHttpCallBack(this,"加载中...") {

                @Override
                public void onSuccess(String data) {
//                int count = JsonTool.getResultStr(data,"smsNumber").isEmpty()?0:Integer.parseInt(JsonTool.getResultStr(data,"smsNumber"));
//                isNewUser=JsonTool.getResultStr(data,"isNewUser").isEmpty()?0:Integer.parseInt(JsonTool.getResultStr(data,"isNewUser"));
                    try {
                        JSONObject object=new JSONObject(data.toString());
                        JSONObject result=object.getJSONObject("result");
                        int count=result.getInt("smsNumber");//手机号码发送短信次数
                        isNewUser=result.getInt("isNewUser");//是否新用户 0-否，1-是
                        isBlack=result.getInt("isBlack");//手机号码是否被冻结  0-否，1-是
                        ifOverrun=result.getInt("ifOverrun");//该用户获取验证码是否超限  0-否，1-是
                        if(count>=3) {
                            showCodeImage();
                        }
                        else {
                            requestCodeData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(int code, String error) {
                    ToastTool.show(mContext,error);
                }
            });
        }
    }

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

        tvCodeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sCodeText=etCodeEt.getText().toString().trim();
                //获取图形验证码
                String codeStr = codeUtils.getCode();
                if(sCodeText.equalsIgnoreCase(codeStr)) {
                    if (dialog != null)
                        dialog.cancel();
                    requestCodeData();
                }else {
                    ToastTool.show(mContext, "验证码不正确！");
                }
            }
        });
        ivCodeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivCodeImage.setImageBitmap(codeUtils.createBitmap());
            }
        });
        dialog.show();
    }

    /*请求验证码*/
    private void requestCodeData()
    {
        RequestParam param = new RequestParam();
        param.add("mobile",phone);
        param.add("type","9");
        OkHttpUtils.post(Config.CODE, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                i = 60;
                tv_code.setEnabled(false);
                timer = new Timer();
                myTask = new MyTimerTask();
                timer.schedule(myTask, 0, 1000);
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext,error);
            }
        });
    }

    private int i = 60;
    private Timer timer;
    private MyTimerTask myTask;
    /**
     * 倒计时
     *
     * @author wangbin
     *
     */
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(i--);
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                tv_code.setEnabled(true);
                tv_code.setText("重新发送");
                timer.cancel();
                myTask.cancel();
            } else {
                tv_code.setText("重新获取"+"("+msg.what + "秒"+")");
            }
        }

    };

}
