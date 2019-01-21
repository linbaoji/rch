package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.SecondBaseActivity;
import com.rch.common.ButtonUtils;
import com.rch.common.Config;
import com.rch.common.RegTool;
import com.rch.common.ToastTool;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/12/17.
 */

public class PublishCircleAct extends SecondBaseActivity{
    @ViewInject(R.id.et_phone)
    private EditText et_phone;
    @ViewInject(R.id.et_content)
    private EditText et_content;
    private String phone,content;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_publishcircle);
    }

    @Override
    public void init(Bundle savedInstanceState) {
      setTopTitle("发布生意");
      getMobile();//获取联系方式
    }

    private void getMobile() {
        RequestParam param=new RequestParam();
        OkHttpUtils.post(Config.INITCIRCLE_URL, param, new OkHttpCallBack(mContext,"") {
            @Override
            public void onSuccess(String data) {
                try {
                    JSONObject object=new JSONObject(data.toString());
                    String mobile=object.getString("result");
                    if(!TextUtils.isEmpty(mobile)){
                        et_phone.setText(mobile);
                        et_phone.setSelection(mobile.length());
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

    @OnClick({R.id.tv_sub})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_sub:
                if(!ButtonUtils.isFastDoubleClick(R.id.tv_sub)) {
                    if (isVisble()) {
                        goSubmit();
                    }
                }
                break;
        }
    }

    private void goSubmit() {
        RequestParam param=new RequestParam();
        param.add("mobile",phone);
        param.add("content",content);
        OkHttpUtils.post(Config.SAVECIRCLE_URL, param, new OkHttpCallBack(mContext,"") {
            @Override
            public void onSuccess(String data) {
                startActivity(new Intent(mContext, FinancialSubmitSuccessActivity.class).putExtra("from_type", "3"));
                finish();
            }

            @Override
            public void onError(int code, String error) {
             ToastTool.show(mContext,error);
            }
        });
    }

    private boolean isVisble() {
        phone=et_phone.getText().toString().trim();
        content=et_content.getText().toString();
        if(TextUtils.isEmpty(phone)){
            et_phone.requestFocus();
            ToastTool.show(mContext,"请输入联系方式");
            return false;
        }

//        if(!((phone.length() == 11 && RegTool.isMobile(phone))||(phone.length()<20&&RegTool.isZjPhone(phone)))) {
//            return false;
//
//        }
        if(!RegTool.isLince(phone)){
            et_phone.requestFocus();
            et_phone.setSelection(phone.length());
            ToastTool.show(mContext,"联系方式格式错误");
            return false;
        }
        if(TextUtils.isEmpty(content)||content.length()<10){
            et_content.requestFocus();
            et_content.setSelection(content.length());
            ToastTool.show(mContext,"内容描述不能少于10个字");
            return false;
        }
        return true;
    }
}
