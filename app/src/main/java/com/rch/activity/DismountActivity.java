package com.rch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ToastTool;
import com.rch.custom.MyAlertDialog;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

/**
 * 下架
 * Created by Administrator on 2018/10/11.
 */

public class DismountActivity extends SecondBaseActivity {
    @ViewInject(R.id.rg_status)
    RadioGroup rg_status;
    @ViewInject(R.id.rb_xj)
    RadioButton rb_xj;
    @ViewInject(R.id.rb_ys)
    RadioButton rb_ys;
    private String reson;
    private String id;
    private String from;
    private String httpurl;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_dismount);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("下架");
        id=getIntent().getExtras().getString("id");
        from=getIntent().getExtras().getString("from");
        if(from.equals("0")){//新车
            httpurl=Config.DELETE_NEW_CAR;
        }else {//二手车
            httpurl=Config.DELETE_OLD_CAR;
        }
        rg_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.rb_xj:
                        reson = "1";
                        break;
                    case R.id.rb_ys:
                        reson = "2";
                        break;
                }
            }
        });
    }
    private boolean isAvailable(){
        if (!rb_xj.isChecked() && !rb_ys.isChecked()){
            ToastTool.show(mContext,"请至少选择一种车辆状态");
            return false;
        }
        return true;
    }
    @OnClick({R.id.tv_submit})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                if (isAvailable()) {
                    showXjDialog();
                }
                break;
        }
    }

    private void showXjDialog() {
        MyAlertDialog dialog = new MyAlertDialog(mContext);
        dialog.builder().setTitle("提示").setMsg("确认下架该车辆").setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                         DismountCar(id,reson);
            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }

    //下架车辆
    private void DismountCar(String id,String reason){
        EncryptionTools.initEncrypMD5(getUserInfo().getToken()==null?"":getUserInfo().getToken());
        RequestParam param = new RequestParam();
        param.add("token", getUserInfo().getToken()==null?"":getUserInfo().getToken());
        param.add("timestamp", EncryptionTools.TIMESTAMP);
        param.add("nonce", EncryptionTools.NONCE);
        param.add("signature", EncryptionTools.SIGNATURE);
        param.add("id", id);
        param.add("type", "1");//1-下架 2-删除
        param.add("reason",reason);
        OkHttpUtils.post(httpurl, param, new OkHttpCallBack(mContext,"加载中...") {
            @Override
            public void onSuccess(String data) {
                ToastTool.show(mContext,"提交成功");
//                startActivity(new Intent(mContext, SeachCarServerSubmitActivity.class).putExtra("type", 5));
                finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext,error);
            }
        });
    }
}
