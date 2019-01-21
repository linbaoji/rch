package com.rch.activity;

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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.CodeUtils;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.GsonUtils;
import com.rch.common.JsonTool;
import com.rch.common.ReceiverTool;
import com.rch.common.RegTool;
import com.rch.common.SpUtils;
import com.rch.common.TimePareUtil;
import com.rch.common.ToastTool;
import com.rch.common.ZhuGeIOTool;
import com.rch.custom.CommonView;
import com.rch.entity.BuyMonitorEntity;
import com.rch.entity.CarDetailEntity;
import com.rch.entity.SupplierEntity;
import com.rch.entity.UserInfoEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;
import com.zhuge.analysis.stat.ZhugeSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/5/11.
 */

public class PreOrderActivity extends BaseActivity implements View.OnClickListener{

    TextView tvOk,tvCode,tvStoreType,tvStoreTitle,tvStoreAddress;
    ImageView ivBack;
    EditText etName,etPhone,etCode,et_tjr;

    String id="",sCarName="";
    LinearLayout pre_order_code_layout;

    SupplierEntity supplierEntity;
    private String version;

    private CommonView cv_date;//时间悬着器
    private TimePickerView pvTime;//
    private Date startDate;


    String time="";
    private String strName;
    private String strPhone;
    private String strCode;
    private String strtjr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order);
        initToolBar();
        id=getIntent().getStringExtra("carId");
        sCarName=getIntent().getExtras().getString("name");
        initControls();
        initStoresData();

    }

    private void initControls()
    {
        tvOk = (TextView) findViewById(R.id.pre_order_ok);
        tvCode = (TextView) findViewById(R.id.pre_order_code_btn);
        ivBack = (ImageView) findViewById(R.id.pre_order_back);
        etName = (EditText) findViewById(R.id.pre_order_name);
        etPhone = (EditText) findViewById(R.id.pre_order_phone);
        etCode = (EditText) findViewById(R.id.pre_order_code_text);
        tvStoreType = (TextView) findViewById(R.id.pre_order_store_type);
        tvStoreTitle = (TextView) findViewById(R.id.pre_order_store_title);
        tvStoreAddress = (TextView) findViewById(R.id.pre_order_store_address);
        pre_order_code_layout= (LinearLayout) findViewById(R.id.pre_order_code_layout);
        cv_date= (CommonView) findViewById(R.id.cv_date);
        et_tjr= (EditText) findViewById(R.id.et_tjr);
        pre_order_code_layout.setVisibility(View.GONE);
        etPhone.setEnabled(true);

        tvOk.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvCode.setOnClickListener(this);
        cv_date.setOnClickListener(this);

    }

    private void initStoresData()
    {
        if(isLogin())
        {//登录
            etPhone.setEnabled(false);
//            etPhone.setText(getUserInfo().getMobile());
            etPhone.setText(getUser());
            pre_order_code_layout.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(getUserInfo().getInviteMobile())){//推荐人不为空
               et_tjr.setText(getUserInfo().getInviteMobile());
            }
            if(!TextUtils.isEmpty(getUserInfo().getIfRealnameCertify())&&getUserInfo().getIfRealnameCertify().equals("1")){//实名
                etName.setText(getUserInfo().getUserName());
                etName.setEnabled(false);
            }else {
                etName.setEnabled(true);
            }


        }else {
//            etPhone.setEnabled(true);
//            etPhone.setHint("请输入您的手机号");
            pre_order_code_layout.setVisibility(View.VISIBLE);
            etName.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.pre_order_ok:
                if(!ButtonUtils.isFastDoubleClick(v.getId()))
                if(isVisible()){
                preOrderSubmit();
                }
                break;
            case R.id.pre_order_code_btn:
                if(!ButtonUtils.isFastDoubleClick(v.getId())) {
                    isExceedSendCodeAmount();
                }
                break;
            case R.id.pre_order_back:
                finish();
                break;
            case R.id.code_ok:
                if(!ButtonUtils.isFastDoubleClick(v.getId()))
                verifyImageCode();
                break;
            case R.id.code_image:
                ivCodeImage.setImageBitmap(codeUtils.createBitmap());
                break;

            case R.id.cv_date:
                showDateDialog();//显示时间悬着
                break;
        }
    }

    private boolean isVisible() {
        strName=etName.getText().toString().trim();
        strPhone=etPhone.getText().toString().trim();
        strCode=etCode.getText().toString().trim();
        strtjr=et_tjr.getText().toString().trim();
        if(strName.isEmpty())
        {
            ToastTool.show(this,"请输入联系人");
            return false;
        }
        if(strPhone.isEmpty())
        {
            ToastTool.show(this,"请输入联系人手机号");
            return false;
        }
        if(!isLogin()&&!RegTool.isMobile(strPhone))
        {
            ToastTool.show(this,"请输入正确联系人手机号");
            return false;
        }
        if(strCode.isEmpty()&&!isLogin())
        {
            ToastTool.show(this,"请输入验证码");
            return false;
        }

        if(TextUtils.isEmpty(time)){
            ToastTool.show(this,"请选择预计到店看车的日期");
            return false;
        }

        return true;
    }

    private void showDateDialog() {
        Calendar clstartDate = Calendar.getInstance();
        Calendar clendDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        clstartDate.set(TimePareUtil.getCurrentYear(),TimePareUtil.getCurrentMonth(),TimePareUtil.getCurrentDay()+1);
        clendDate.set(TimePareUtil.getCurrentYear(),TimePareUtil.getCurrentMonth(),TimePareUtil.getCurrentDay()+7);
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                int days= (int) TimePareUtil.getDayDiff(TimePareUtil.getCurrentDate("yyyy-MM-dd"),date);
                if(date.before(TimePareUtil.getCurrentDate("yyyy-MM-dd HH:mm"))||days>7){
                    ToastTool.show(PreOrderActivity.this, "请重新选择");
//                    showDateDialog();//再默认
                     time="";
                }else {
                    time = TimePareUtil.getTimeForDate("yyyy-MM-dd HH:mm", date);
                    cv_date.setDesText(time);
                    cv_date.setDesTextColor(getResources().getColor(R.color.black_1));
//                Toast.makeText(PreOrderActivity.this, time, Toast.LENGTH_SHORT).show();
                    startDate = TimePareUtil.getDateTimeForTime("yyyy-MM-dd HH:mm", time);
                }
            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .isDialog(true)
                .isCenterLabel(false)//年月日不显示在中间
                .isDialog(false)
                .setTitleColor(getResources().getColor(R.color.white))
                .setLineSpacingMultiplier(2.0f)
                .gravity(Gravity.CENTER)
                .setRangDate(clstartDate,clendDate)
                .setCancelColor(getResources().getColor(R.color.gray_3))
                .setSubmitColor(getResources().getColor(R.color.gray_13))
//                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {
//                    @Override
//                    public void customLayout(View v) {
//
//                    }
//                })
                .build();

        pvTime.setKeyBackCancelable(false);
        if(startDate!=null) {
            pvTime.setDate(TimePareUtil.getCalendarForDate(startDate));
        }
        pvTime.show();
    }

    /*预购提交*/
    private void preOrderSubmit()
    {


        upLoadingShow();
        RequestParam param = new RequestParam();
        param.add("userName",strName);
        param.add("conventionDate",time);
        param.add("mobile",strPhone);
        param.add("captcha",strCode);
        param.add("inviteMobile",strtjr);
        param.add("id",id);


        OkHttpUtils.post(Config.PRELOOKCAR, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                upLoadingClose();

                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    String orderState = result.getString("orderState");
                    if (orderState.equals("405"))//今天已预约
                    {
//                        startActivity(new Intent(PreOrderActivity.this, PreOrderSuccessActivity.class).putExtra("desc", "2"));
//                        finish();
                        ToastTool.show(PreOrderActivity.this,"您已提交过了，不可重复提交哦！");
                    } else if(orderState.equals("404")){
                        ToastTool.show(PreOrderActivity.this,"预约处理失败");
                    }else {//200.201.202
                        startActivity(new Intent(PreOrderActivity.this,FinancialSubmitSuccessActivity.class).putExtra("from_type","1"));
                        finish();
                    }

                    if(!orderState.equals("405")&&!orderState.equals("404")){
                    if(!result.isNull("loginResultUser")) {
                        saveUser(strPhone, data);//更新一下用户
                        JSONObject loginresultuser = result.getJSONObject("loginResultUser");
                        UserInfoEntity entity = GsonUtils.gsonToBean(loginresultuser.toString(), UserInfoEntity.class);
                        SpUtils.setToken(PreOrderActivity.this, entity.getToken());//保存登录token
                        SpUtils.setIsLogin(PreOrderActivity.this, true);//保存是否登录状态
                    }
                    }




                    Map<String, String> map = new HashMap<>();
                    map.put("用户手机", strPhone);
                    map.put("用户名称", strName);
                    map.put("商品名称", sCarName);
                    ZhuGeIOTool.buyMonitor(PreOrderActivity.this, "预约看车", map);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(int code, String error) {
                /*if(code==405)
                    startActivity(new Intent(PreOrderActivity.this,PreOrderSuccessActivity.class).putExtra("desc","您今日已预约成功，客服将跟您联系，请留意接听哦"));
                else*/
                ToastTool.show(PreOrderActivity.this,error);
                upLoadingClose();
            }
        });
    }

    int isNewUser=0;
    int isBlack=0;
    int ifOverrun=0;
    String inviteMobile;//推荐人手机号

    /*短信验证码是否超过3次*/
    private void isExceedSendCodeAmount()
    {
        strPhone=etPhone.getText().toString().trim();
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
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    int count=result.getInt("smsNumber");//手机号码发送短信次数
                    isNewUser=result.getInt("isNewUser");//是否新用户 0-否，1-是
                    isBlack=result.getInt("isBlack");//手机号码是否被冻结  0-否，1-是
                    ifOverrun=result.getInt("ifOverrun");//该用户获取验证码是否超限  0-否，1-是
                    if(!result.isNull("inviteMobile")) {
                        inviteMobile = result.getString("inviteMobile");//推荐人手机号
                        if (!TextUtils.isEmpty(inviteMobile)) {
                            et_tjr.setText(interval);
                            et_tjr.setEnabled(false);
                        }
                    }
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
                ToastTool.show(PreOrderActivity.this,error);
            }
        });
    }

    /*请求验证码*/
    private void requestCodeData()
    {


        RequestParam param=new RequestParam();
        param.add("mobile",strPhone);
        param.add("type","5");
        OkHttpUtils.post(Config.CODE, param, new OkHttpCallBack(this,"加载中...") {

            @Override
            public void onSuccess(String data) {
                startTime();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(PreOrderActivity.this,error);
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


    int interval=1000;
    int countdown=60;
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(countdown>0) {
                countdown--;
                tvCode.setEnabled(false);
                //tvCode.setText("重新获取验证码("+countdown+")");
                tvCode.setText(String.valueOf(countdown)+"S"+"重新获取");
            }
            else {
                tvCode.setText("重新获取");
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
}
