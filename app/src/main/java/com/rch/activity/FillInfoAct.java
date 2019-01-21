package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.PickTimeUtil;
import com.rch.common.ToastTool;
import com.rch.custom.CommonView;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

/**
 * Created by acer on 2018/8/13.
 */

public class FillInfoAct extends BaseActivity{
    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    @ViewInject(R.id.et_mark)
    private EditText et_mark;
    @ViewInject(R.id.tv_upload)
    private TextView tv_upload;

    private String time="";
    private String mark="";//输入框
    private String orderId;
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fillinfo);
        ViewUtils.inject(this);
        AppManager.getAppManager().addActivity(this);
        orderId=getIntent().getExtras().getString("orderId","");
        setColor();

        et_mark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.length()>0){
                mark=charSequence.toString();
                setColor();
            }else {
                mark="";
                setColor();
            }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setColor() {
        if(tv_time.getText().toString().equals("实际看车日期")){
            time="";
        }else {
            time=tv_time.getText().toString();
        }

        if(!TextUtils.isEmpty(time)&&!TextUtils.isEmpty(mark)){
            tv_upload.setBackground(getResources().getDrawable(R.drawable.button));
            tv_upload.setEnabled(true);
        }else {
            tv_upload.setBackgroundColor(getResources().getColor(R.color.gray_3));
            tv_upload.setEnabled(false);
        }

    }

    @OnClick({R.id.act_order_info_back,R.id.tv_upload,R.id.rl_select})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.act_order_info_back:
                finish();
                break;
            case R.id.tv_upload:
                upLoad();
                break;

            case R.id.rl_select:
                PickTimeUtil.pickShowYMD(FillInfoAct.this,tv_time, new PickTimeUtil.CheckListion() {
                    @Override
                    public void over() {//从写了
                        time=tv_time.getText().toString();
                        setColor();
                    }
                });
                break;
        }
    }

    private void upLoad() {
        mark=et_mark.getText().toString();
        if(TextUtils.isEmpty(mark)){
            ToastTool.show(FillInfoAct.this,"请输入备注");
            et_mark.requestFocus();
            return;
        }

        if(mark.length()<10){
            ToastTool.show(FillInfoAct.this,"备注不能少于10个字");
            et_mark.requestFocus();
            et_mark.setSelection(mark.length());
            return;
        }
        if(time.equals("请选择")){
            ToastTool.show(FillInfoAct.this,"请选择时间");
            return;
        }

        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("orderId",orderId);
        param.add("remark",mark);
        param.add("factTime",time);

        OkHttpUtils.post(Config.CONFIRMSEEVEHICLE, param, new OkHttpCallBack(FillInfoAct.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
//                ToastTool.show(FillInfoAct.this,"提交成功");
                startActivity(new Intent(FillInfoAct.this,SucessAct.class));
                finish();
            }

            @Override
            public void onError(int code, String error) {
            ToastTool.show(FillInfoAct.this,error);
            }
        });

    }
}
