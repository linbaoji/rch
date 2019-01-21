package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ToastTool;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

/**
 * Created by acer on 2018/8/13.
 */

public class DisMissAct extends BaseActivity{
    @ViewInject(R.id.et_mark)
    private EditText et_mark;

    private String orderId;
    private String auditType;

    private String mark;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_dismiss);
        ViewUtils.inject(this);
        orderId=getIntent().getExtras().getString("orderId","");
        auditType=getIntent().getExtras().getString("auditType","");
    }

    @OnClick({R.id.tv_upload,R.id.act_order_info_back})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.tv_upload:
                mark=et_mark.getText().toString();
                if(TextUtils.isEmpty(mark)){
                    ToastTool.show(DisMissAct.this,"请输入驳回描述");
                    et_mark.requestFocus();
                }else {
                    upload();
                }

                break;

            case R.id.act_order_info_back:
                finish();
                break;
        }
    }

    private void upload() {
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("orderId",orderId);
        param.add("remark",mark);
        param.add("auditType",auditType);
        OkHttpUtils.post(Config.AUDITVEHICLE, param, new OkHttpCallBack(DisMissAct.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                finish();
//                ToastTool.show(DisMissAct.this,"已驳回");

            }

            @Override
            public void onError(int code, String error) {

            }
        });
    }
}
