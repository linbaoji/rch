package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.BaseActivity;
import com.rch.common.RegTool;
import com.rch.common.ToastTool;

/**
 * Created by Administrator on 2018/7/27.
 */

public class VinCodeAct extends BaseActivity {
    @ViewInject(R.id.et_vin)
    private EditText et_vin;
    @ViewInject(R.id.tv_bc)
    private TextView tv_bc;

    private String vincode;
    private String type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_vincode);
        ViewUtils.inject(this);
        vincode=getIntent().getExtras().getString("vincode","");
        type=getIntent().getExtras().getString("type","1");
        if(type.equals("3")){
            et_vin.setEnabled(false);
        }else {
            et_vin.setEnabled(true);
        }
        if(!TextUtils.isEmpty(vincode)){
            et_vin.setText(vincode);
        }
    }

    @OnClick({R.id.iv_back,R.id.tv_bc})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
//                setResult(100,new Intent().putExtra("vincode",et_vin.getText().toString().trim()));
                finish();
                break;
            case R.id.tv_bc:
                String vincode=et_vin.getText().toString();
                if(TextUtils.isEmpty(vincode)){
                    ToastTool.show(VinCodeAct.this,"请输入vin码");
                    et_vin.requestFocus();
                    return;
                }
                setResult(100,new Intent().putExtra("vincode",et_vin.getText().toString().trim()));
                finish();
                break;
        }
    }
}
