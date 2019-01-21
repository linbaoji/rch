package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ToastTool;
import com.rch.entity.UserOrderEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 意见反馈
 * Created by Administrator on 2018/5/31.
 */

public class FeedbackActivity extends BaseActivity{
    @ViewInject(R.id.rg_check)
    private RadioGroup rg_check;
    @ViewInject(R.id.et_ms)
    private EditText et_ms;
    @ViewInject(R.id.rb_xc)
    private RadioButton rb_xc;
    @ViewInject(R.id.rb_jc)
    private RadioButton rb_jc;

    private String stms;
    private String check="1";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initToolBar();
        ViewUtils.inject(this);

        rb_xc.setChecked(true);
        rg_check.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_xc://新车
                        check="1";
                        break;

                    case R.id.rb_jc://旧车
                        check="2";
                        break;

                    case R.id.rb_qt://其他
                        check="9";
                        break;
                }

            }
        });
    }


    @OnClick({R.id.tv_tj,R.id.iv_back,R.id.tv_myfk})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_tj:
                if(isValidate()){
                   tiJiao();
                }

                break;
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_myfk:
                startActivity(new Intent(FeedbackActivity.this,FeedbackListActivity.class));
                break;
        }

    }

    /**
     * 提交
     */
    private void tiJiao() {
        upLoadingShow();
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("source", "3");
        param.add("fbtype", check);
        param.add("suggestion", stms);
        OkHttpUtils.post(Config.SAVEFEEDBACK, param, new OkHttpCallBack(FeedbackActivity.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
//                try {
//                    JSONObject object=new JSONObject(data.toString());
//                    String msg=object.getString("msg");
//                    ToastTool.show(FeedbackActivity.this,msg);
////                    finish();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                ToastTool.show(FeedbackActivity.this,"提交成功");
                et_ms.setText("");
                check="1";
                rb_xc.setChecked(true);
                upLoadingClose();
            }

            @Override
            public void onError(int code, String error) {
            ToastTool.show(FeedbackActivity.this,error);
                upLoadingClose();
            }
        });

    }

    private boolean isValidate() {
        stms=et_ms.getText().toString();
        if(TextUtils.isEmpty(stms)){
            ToastTool.show(FeedbackActivity.this,"请输入问题描述");
            et_ms.requestFocus();
            return false;
        }
        if(stms.length()<10){
            ToastTool.show(FeedbackActivity.this,"问题描述不少于10个字");
            et_ms.requestFocus();
            return false;
        }

//        if(TextUtils.isEmpty(check)){
//            ToastTool.show(FeedbackActivity.this,"请选择您想反馈的问题");
//            et_ms.requestFocus();
//            return false;
//        }

        return true;
    }


}
