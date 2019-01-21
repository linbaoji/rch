package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.GsonUtils;
import com.rch.common.ToastTool;
import com.rch.common.ValidateUtil;
import com.rch.entity.CertifiedEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 认证分销商
 * Created by Administrator on 2018/9/29.
 */

public class DistributorActivity extends SecondBaseActivity implements View.OnClickListener{

    EditText et_real_name;
    EditText et_card_no;
    private String name;//姓名
    private String cardNo;//身份证号码
    TextView tv_submit;//提交
    private CertifiedEntity bean;

    @Override
    public void setLayout() {
        setContentView(R.layout.act_authen_distributor);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("分销商认证");
        init();
        getInfo();//获取认证信息
    }

    private void getInfo() {
        RequestParam param=new RequestParam();
        OkHttpUtils.post(Config.REFRESHCERTIFIEDINFO, param, new OkHttpCallBack(mContext,"加载中...") {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object=new JSONObject(data.toString());
                    JSONObject result=object.getJSONObject("result");
                    bean= GsonUtils.gsonToBean(result.toString(),CertifiedEntity.class);
                    setUi();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code, String error) {

            }
        });
    }

    private void setUi() {
        if(bean!=null){
            if(!TextUtils.isEmpty(bean.getUserName())){
                et_real_name.setText(bean.getUserName());
            }
            if(!TextUtils.isEmpty(bean.getUserLicenseNo())){
                et_card_no.setText(bean.getLicenseNo());
            }
        }
    }

    private void init(){
        et_real_name = (EditText) findViewById(R.id.et_real_name);
        et_card_no = (EditText) findViewById(R.id.et_card_no);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
    }

    private boolean isAvailable(){
        name = et_real_name.getText().toString().trim();
        cardNo = et_card_no.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            ToastTool.show(mContext,"请输入真实姓名");
            et_real_name.requestFocus();
            return false;
        }
        if (!ValidateUtil.isLegalName(name)){
            ToastTool.show(mContext,"请输入正确的姓名");
            et_real_name.requestFocus(name.length());
            return false;
        }
        if(TextUtils.isEmpty(cardNo)){
            ToastTool.show(mContext,"请输入身份证号码");
            et_card_no.requestFocus();
            return false;
        }
        if (!ValidateUtil.checkIdCard(cardNo)){
            ToastTool.show(mContext,"请输入正确的身份证号码");
            et_card_no.requestFocus(cardNo.length());
            return false;
        }
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_submit:
                if (isAvailable()){
                    submit();
                    tv_submit.setEnabled(false);
                }
//                    startActivity(new Intent(this, SeachCarServerSubmitActivity.class).putExtra("type", 4));
                break;
        }
    }

    private void submit() {
        RequestParam param=new RequestParam();
        param.add("authenticateType","2");
        param.add("userName",name);
        param.add("userLicenseNo",cardNo);
        OkHttpUtils.post(Config.SUBMITAUTHENTICATION_URL, param, new OkHttpCallBack(mContext,"加载中...") {
            @Override
            public void onSuccess(String data) {
                tv_submit.setEnabled(true);
                startActivity(new Intent(mContext, VerifySucessActivity.class).putExtra("from","2"));
            }

            @Override
            public void onError(int code, String error) {
//             ToastTool.show(mContext,error);
                tv_submit.setEnabled(true);
                startActivity(new Intent(mContext, VerifyFailureActivity.class).putExtra("auditDesc",error));

            }
        });

    }
}
