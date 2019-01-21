package com.rch.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.base.SecondBaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.CodeUtils;
import com.rch.common.Config;
import com.rch.common.GsonUtils;
import com.rch.common.JsonTool;
import com.rch.common.ReceiverTool;
import com.rch.common.RegTool;
import com.rch.common.SpUtils;
import com.rch.common.ToastTool;
import com.rch.entity.UserInfoEntity;
import com.rch.fragment.BusinessCircleFragment;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;


/**
 * Created by Administrator on 2018/3/28.
 */

public class LoginActivity extends SecondBaseActivity implements View.OnClickListener {

    TextView login, regis, lost, code;
    EditText phone, pwd, etYqm;
    View login_yqm_line;
    CheckBox cb_check;
    boolean ischeck = true;//是否选中用户协议

    private TextView login_pact;

    CodeUtils codeUtils;

    int isNewUser = 0;
    int isBlack = 0;
    int ifOverrun = 0;
    private String auditDesc;
    private String ifNewUser;

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("登录");
        initControls();
    }


    private void initControls() {
        login = (TextView) findViewById(R.id.login_ok);
        regis = (TextView) findViewById(R.id.login_regis);
        lost = (TextView) findViewById(R.id.login_lost);
        code = (TextView) findViewById(R.id.login_code);
        phone = (EditText) findViewById(R.id.login_phone);
        pwd = (EditText) findViewById(R.id.login_pwd);
        etYqm = (EditText) findViewById(R.id.login_yqm);
        login_pact = (TextView) findViewById(R.id.login_pact);

        login_yqm_line = findViewById(R.id.login_yqm_line);
        cb_check = (CheckBox) findViewById(R.id.cb_check);


       /* if(isLogin()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }*/
//        phone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                String sPhont=phone.getText().toString().trim();
//                if(sPhont.isEmpty())
//                {
//                    login.setEnabled(false);
//                    login.setBackgroundColor(getResources().getColor(R.color.gray));
//                }else
//                {
//                    login.setEnabled(true);
//                    login.setBackground(getResources().getDrawable(R.drawable.button));
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        login.setOnClickListener(this);
        regis.setOnClickListener(this);
        lost.setOnClickListener(this);
        code.setOnClickListener(this);
        login_pact.setOnClickListener(this);
        cb_check.setChecked(true);
        cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ischeck = true;
                } else {
                    ischeck = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_ok:
                if (!ButtonUtils.isFastDoubleClick(R.id.login_ok))
                    if (ischeck) {
                        login();
                    } else {
                        ToastTool.show(this, "您未同意协议");
                    }
                break;
            case R.id.login_regis:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.login_lost:
                startActivity(new Intent(this, LostPwdActivity.class));
                break;
            case R.id.login_code://获取验证码
                if (!ButtonUtils.isFastDoubleClick(v.getId()))
                    isExceedSendCodeAmount();
                break;
            case R.id.code_ok:
                verifyImageCode();
                break;
            case R.id.code_image:
                ivCodeImage.setImageBitmap(codeUtils.createBitmap());
                break;

            case R.id.login_pact:
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;

            case R.id.reg_rz:
                startActivity(new Intent(LoginActivity.this, AuthenticateActivity.class));
                finish();
                break;
            case R.id.reg_esc:
                sendBroadcast(new Intent(ReceiverTool.REFRESHHOMEFRAGMENTMODULE));
                finish();
                break;
        }
    }

    /*短信验证码是否超过3次*/
    private void isExceedSendCodeAmount() {
        strPhone = phone.getText().toString().trim();
        if (strPhone.isEmpty()) {
            ToastTool.show(this, "手机号码不能为空！");
            return;
        } else if (!RegTool.isMobile(strPhone)) {
            ToastTool.show(this, "手机号格式错误！");
            return;
        }
        RequestParam param = new RequestParam();
        param.add("mobile", strPhone);
        OkHttpUtils.post(Config.SENDNUMBER, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
//                int count = JsonTool.getResultStr(data,"smsNumber").isEmpty()?0:Integer.parseInt(JsonTool.getResultStr(data,"smsNumber"));
//                isNewUser=JsonTool.getResultStr(data,"isNewUser").isEmpty()?0:Integer.parseInt(JsonTool.getResultStr(data,"isNewUser"));
                try {
                    JSONObject object = new JSONObject(data.toString());
                    JSONObject result = object.getJSONObject("result");
                    int count = result.getInt("smsNumber");//手机号码发送短信次数
                    isNewUser = result.getInt("isNewUser");//是否新用户 0-否，1-是
                    isBlack = result.getInt("isBlack");//手机号码是否被冻结  0-否，1-是
                    ifOverrun = result.getInt("ifOverrun");//该用户获取验证码是否超限  0-否，1-是

//                    if(isNewUser==1) {
//                        etYqm.setVisibility(View.VISIBLE);
//                        login_yqm_line.setVisibility(View.VISIBLE);
//                    }else
//                    {
//                        etYqm.setVisibility(View.GONE);
//                        login_yqm_line.setVisibility(View.GONE);
//                    }
                    //isNewUser= JsonTool.getResult(data,"result").isEmpty()?0:Integer.parseInt(JsonTool.getResult(data,"result"));
//                    Log.e("e",JsonTool.getResult(data,"result"));
                    if (count >= 3) {
                        showCodeImage();
                    } else {
                        requestCodeData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(LoginActivity.this, error);
            }
        });
    }
    /*是否需要显示验证码弹框*/
  /*  private void isShowCodeImage()
    {
        strPhone=phone.getText().toString().trim();
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
        int sCount=1;
        String sDay="";
        String loginCount=getLoginCount();
        if(!loginCount.isEmpty()) {
            sDay = loginCount.split("-")[0];
            sCount= Integer.parseInt(loginCount.split("-")[1]);
        }
        SimpleDateFormat sdf=new SimpleDateFormat("dd");
        String systemDay=sdf.format(new Date());
        if(sDay.equals(systemDay))
        {
            if(sCount>3)
                showCodeImage();
            else
                saveLoginCount(systemDay+"-"+(sCount+1));
        }else {
            saveLoginCount(systemDay + "-1");
            requestCodeData();
        }
    }*/

    Dialog dialog;
    TextView tvCodeOk;
    ImageView ivCodeImage;
    EditText etCodeEt;

    /*显示验证码图片*/
    private void showCodeImage() {
        codeUtils = CodeUtils.getInstance();
        Bitmap bitmap = codeUtils.createBitmap();

        View view = LayoutInflater.from(this).inflate(R.layout.image_code_layout, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog = new Dialog(this);
        dialog.addContentView(view, params);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setGravity(Gravity.CENTER);
        tvCodeOk = (TextView) view.findViewById(R.id.code_ok);
        etCodeEt = (EditText) view.findViewById(R.id.code_et);
        ivCodeImage = (ImageView) view.findViewById(R.id.code_image);


        ivCodeImage.setImageBitmap(bitmap);

        tvCodeOk.setOnClickListener(this);
        ivCodeImage.setOnClickListener(this);
        dialog.show();
    }


    /*验证图形验证码*/
    private void verifyImageCode() {
        String sCodeText = etCodeEt.getText().toString().trim();
        //获取图形验证码
        String codeStr = codeUtils.getCode();
        if (sCodeText.equalsIgnoreCase(codeStr)) {
            if (dialog != null)
                dialog.cancel();
            requestCodeData();
        } else
            ToastTool.show(this, "验证码不正确！");
    }

    String strPhone = "", strPwd = "", strYqm = "";

    /*登录*/
    private void login() {
        strPhone = phone.getText().toString().trim();
        strPwd = pwd.getText().toString().trim();
        strYqm = etYqm.getText().toString().trim();
        if (strPhone.isEmpty()) {
            ToastTool.show(this, "手机号码不能为空！");
            phone.requestFocus();
            return;
        } else if (!RegTool.isMobile(strPhone)) {
            ToastTool.show(this, "手机号码不正确！");
            phone.requestFocus(strPhone.length());
            return;
        } else if (strPwd.isEmpty()) {
            ToastTool.show(this, "短信验证码不能为空！");
            pwd.requestFocus();
            return;
        }
        httpLoginReg();
    }


    private void httpLoginReg() {

        upLoadingShow();
        RequestParam param = new RequestParam();
        param.add("mobile", strPhone);
        param.add("captcha", strPwd);
//        param.add("source","3");
//        if(isNewUser==1 && !TextUtils.isEmpty(strYqm)) {
//            param.add("inviteMobile", strYqm);//邀请手机号
//        }
        OkHttpUtils.post(Config.LOGIN, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
                upLoadingClose();
                saveUser(strPhone, data);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONObject json = jsonObject.getJSONObject("result");
                    ifNewUser = json.getString("ifNewUser");//是否新用户 0-否，1-是
                    if (!json.isNull("auditDesc")) {//认证失败原因
                        auditDesc = json.getString("auditDesc");
                    }

                    sendBroadcast(new Intent(BusinessCircleFragment.TEXT_RECEIVER));
                    JSONObject loginresultuser = json.getJSONObject("loginResultUser");
                    UserInfoEntity entity = GsonUtils.gsonToBean(loginresultuser.toString(), UserInfoEntity.class);
                    SpUtils.setToken(LoginActivity.this, entity.getToken());//保存登录token
                    SpUtils.setIsLogin(LoginActivity.this, true);//保存是否登录状态

//                    if(ifNewUser.equals("1")) {
//                        showDialog();
//                    }
//                     else {
//                        //startActivity(new Intent(LoginActivity.this,AuthenticateActivity.class));
//                        finish();
//                    }
                    switch (entity.getUserAuditState()) {
                        case "1"://未认证
                            if (ifNewUser.equals("1")) {
                                startActivity(new Intent(LoginActivity.this, AuthenticateActivity.class).putExtra("type", 1));
                            }
                            finish();
                            break;

                        case "2"://审核中
                            ToastTool.show(LoginActivity.this, "登录成功！");
                            //startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                            break;

                        case "3"://认证成功
                        case "4"://
                            ToastTool.show(LoginActivity.this, "登录成功！");
                            //startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                            break;
                        case "5"://认证失败
//                            ToastTool.show(LoginActivity.this,"登录成功！");

                            startActivity(new Intent(LoginActivity.this, VerifyFailureActivity.class).putExtra("auditDesc", auditDesc));
                            finish();
                            break;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //200-认证通过  201-新注册用户，202-账号已被冻结，203-未认证，204-认证审核中，205-认证审核未通过
//                int userState=Integer.parseInt(JsonTool.getResultStr(data,"userState"));
//                switch (userState)
//                {
//
//                  /*  case UserState.FREEZE://202-账号已被冻结
//                        ToastTool.show(LoginActivity.this,"该手机号已被冻结！");
//                        break;*/
//                      case UserState.REGISTER://201-新注册用户 - 认证页
//                     case UserState.NOTCERTIFIED://203-未认证- 认证页
//                         saveUser(strPhone,data);
//                         startActivity(new Intent(LoginActivity.this,DistributionActivity.class).putExtra("type",1));
//                         finish();
//                         break;
//                    case UserState.NOTPASS://205-认证审核未通过- 跳我的页面
//                         saveUser(strPhone,data);
//                         String auditDesc = JsonTool.getResultStr(data,"auditDesc");
//                         startActivity(new Intent(LoginActivity.this,VerifyFailureActivity.class).putExtra("auditDesc",auditDesc));
//                         finish();
//                         break;
//                    case UserState.NOTVERIFY://204-认证审核中- 跳我的页面
//                    case UserState.SUCCESS://200认证通过
//                        saveUser(strPhone,data);
//                        //sendBroadcast(new Intent(ReceiverTool.REFRESHMYFRAGMENTMODULE));
//                        ToastTool.show(LoginActivity.this,"登录成功！");
//                        //startActivity(new Intent(LoginActivity.this,MainActivity.class));
//                        finish();
//                        break;
//                }

            }

            @Override
            public void onError(int code, String error) {
                upLoadingClose();
                ToastTool.show(LoginActivity.this, error);
            }
        });
    }

    Dialog regDialog;
    ImageView reg_rz, reg_esc;

    private void showDialog() {
        regDialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        View view = LayoutInflater.from(this).inflate(R.layout.res_dialog_layout, null);
        regDialog.setContentView(view);

        regDialog.setCanceledOnTouchOutside(false);
        regDialog.setCancelable(false);
        Window dialogWindow = regDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager manager = dialogWindow.getWindowManager();
        Display d = manager.getDefaultDisplay();
        lp.width = d.getWidth();
//        lp.x = 0;
//        lp.y = 0;
        dialogWindow.setAttributes(lp);
        reg_rz = (ImageView) view.findViewById(R.id.reg_rz);
        reg_esc = (ImageView) view.findViewById(R.id.reg_esc);
        reg_rz.setOnClickListener(this);
        reg_esc.setOnClickListener(this);
        regDialog.setCancelable(false);
        regDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return false;
                }
                return false;
            }
        });
        //regDialog.getWindow().setDimAmount(0f);//核心代码 解决了无法去除遮罩问
        regDialog.show();
    }


    /*发送验证码*/
 /*   private void  sendCode()
    {

        requestCodeData();
    }*/

    /*请求验证码*/
    private void requestCodeData() {
        RequestParam param = new RequestParam();
        param.add("mobile", strPhone);
        param.add("type", "4");
        OkHttpUtils.post(Config.CODE, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
                startTime();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(LoginActivity.this, error);
            }
        });
    }


    int interval = 1000;
    int countdown = 60;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (countdown > 0) {
                countdown--;
                code.setEnabled(false);
                //code.setText("重新获取验证码("+countdown+")");
                code.setText(String.valueOf(countdown) + "s" + "重新获取");
            } else {
                code.setText("重新获取");
                code.setEnabled(true);
                countdown = 60;
                stopTime();
            }
        }
    };


    Timer timer = null;
    TimerTask timerTask = null;

    /**
     * 开启定时器
     */
    private void startTime() {
        if (timer == null) {
            timer = new Timer();
            if (timerTask == null) {
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                };
            }
            timer.schedule(timerTask, interval, interval);
        }
    }

    /**
     * 停止定时器
     */
    private void stopTime() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTime();
        if (regDialog != null) {
            regDialog.dismiss();
            regDialog = null;
        }
    }


//    interface UserState{
//        int SUCCESS= 200;//-认证通过
//        int REGISTER=201;//-新注册用户，
//        int FREEZE=202;//-账号已被冻结，
//        int NOTCERTIFIED= 203;//-未认证，
//        int NOTVERIFY=204;//-认证未审核，
//        int NOTPASS= 205;//-认证审核未通过
//    }
}
