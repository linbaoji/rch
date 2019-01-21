package com.rch.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ToastTool;
import com.rch.common.ZhuGeIOTool;
import com.rch.entity.BuyMonitorEntity;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/31.
 */

public class LuxuryActivity extends BaseActivity{

    @ViewInject(R.id.rg_check)
    private RadioGroup rg_check;
    @ViewInject(R.id.et_ms)
    private EditText et_ms;
    @ViewInject(R.id.rb_xc)
    private RadioButton rb_xc;

    private String stms;
    private String check="2";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luxury);
        initToolBar();
        ViewUtils.inject(this);
        rb_xc.setEnabled(false);

        rg_check.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_xc://新车
                        check="1";
                        break;

                    case R.id.rb_jc://高端车
                        check="2";
                        break;

                }

            }
        });
    }

    @OnClick({R.id.tv_tj,R.id.iv_back})
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
        }
    }


    private boolean isValidate() {
        stms=et_ms.getText().toString();
        if(TextUtils.isEmpty(stms)){
            ToastTool.show(LuxuryActivity.this,"请对您想要购买的豪车进行简单的描述");
            et_ms.requestFocus();
            return false;
        }
//        if(stms.length()<10){
//            ToastTool.show(LuxuryActivity.this,"请输入不小于10字的描述");
//            et_ms.requestFocus();
//            return false;
//        }



        return true;
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
        param.add("cqtype", check);
        param.add("requestDesc", stms);
        OkHttpUtils.post(Config.SAVEVEHICLEQUEST, param, new OkHttpCallBack(LuxuryActivity.this,"加载中...") {
            @Override
            public void onSuccess(String data) {
                BuyMonitorEntity entity=new BuyMonitorEntity();
              /*  entity.setUserPhone(getUserInfo().getMobile());
                entity.setUserName(getUserInfo().getUserName());
                entity.setCarShopName("高端二手车");
                entity.setDesc(stms);
                entity.setEventName("豪车定制");
                ZhuGeIOTool.buyMonitor(LuxuryActivity.this,entity);*/
                Map<String,String> map=new HashMap<>();
                map.put("用户手机",getUserInfo().getMobile());
                map.put("用户名称",getUserInfo().getUserName());
                map.put("定制类型","高端二手车");
                map.put("定制描述",stms);
                ZhuGeIOTool.buyMonitor(LuxuryActivity.this,"豪车定制",map);
                ToastTool.show(LuxuryActivity.this,"提交成功");
                et_ms.setText("");
                upLoadingClose();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(LuxuryActivity.this,error);
                upLoadingClose();
            }
        });

    }
}
