package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rch.R;
import com.rch.base.SecondBaseActivity;
import com.rch.common.Config;
import com.rch.common.EncryptionTools;
import com.rch.common.ToastTool;
import com.rch.http.OkHttpCallBack;
import com.rch.http.OkHttpUtils;
import com.rch.http.RequestParam;

/**
 * Created by Administrator on 2018/11/7.
 */

public class OrderResultActivity extends SecondBaseActivity {
    @ViewInject(R.id.et_content)
    private EditText et_content;

    private String id;
    @Override
    public void setLayout() {
        setContentView(R.layout.activity_order_result);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setBackVisiable(false);//不显示返回按钮
        setTopTitle("处理结果");
        if (getIntent()!=null)
        id = getIntent().getStringExtra("id");
    }

    @OnClick({R.id.tv_cancle,R.id.tv_save})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancle:
                finish();
                break;
            case R.id.tv_save:
               String content = et_content.getText().toString().trim()+"";
//               if (!TextUtils.isEmpty(content)){
//                    saveData(id,content);
//               }else {
//                   ToastTool.show(mContext,"请输入处理结果");
//                   et_content.requestFocus();
//               }

                saveData(id,content);//可以为空的
                break;
        }
    }

    private void saveData(String id,String remark){
        RequestParam param = new RequestParam();
        param.add("id",id);
//        if (!TextUtils.isEmpty(remark))
//            param.add("remark", remark);
        param.add("remark",remark);
        OkHttpUtils.post(Config.INQUIRY_OPER_URL, param, new OkHttpCallBack(this, "加载中...") {

            @Override
            public void onSuccess(String data) {
                //更新客户详情数据
                sendBroadcast(new Intent(CustomerDetailActivity.CUSTOMER_DETAIL));
                finish();
            }

            @Override
            public void onError(int code, String error) {
                ToastTool.show(mContext, error);
            }
        });
    }
}
