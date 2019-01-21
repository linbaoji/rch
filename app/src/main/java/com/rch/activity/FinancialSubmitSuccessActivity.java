package com.rch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rch.R;
import com.rch.base.AppManager;
import com.rch.base.BaseActivity;
import com.rch.common.ReceiverTool;

/**
 * 询底价预约看车成功界面
 * Created by Administrator on 2018/8/3.
 */

public class FinancialSubmitSuccessActivity extends BaseActivity implements View.OnClickListener{

    ImageView financial_submit_success_back;
    TextView financial_submit_success_home;
    TextView tv_success,tv_title,tv_content,tv_content_two;

    private String from_type="0";//默认为0，询底价  1预约看车成功

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_submit_success);
        initToolBar();
        initControsl();

        if (getIntent()!=null){
            from_type = getIntent().getStringExtra("from_type");
            if (TextUtils.isEmpty(from_type)){
                from_type = "0";
            }
        }

        if (from_type.equals("1")){
            tv_success.setText("提交成功");
            tv_title.setText("预约申请已提交");
//            tv_content.setText("客服将于2个工作日内与您联系，请保持电话畅通");
            tv_content_two.setText("温馨提示：到店看车时，别忘了携带您的驾驶证哟～");
            financial_submit_success_home.setText("返回首页");
        }else if(from_type.equals("0")){
            tv_title.setText("提交成功");
//            tv_content.setText("客服将于2个工作日内与您联系，请保持电话畅通");
            financial_submit_success_home.setText("返回首页");
        }else if(from_type.equals("3")){
            tv_title.setText("提交成功，审核中");
            financial_submit_success_home.setText("返回生意圈");
        }
    }

    private void initControsl() {
        financial_submit_success_back= (ImageView) findViewById(R.id.financial_submit_success_back);
        financial_submit_success_home= (TextView) findViewById(R.id.financial_submit_success_home);
        tv_success = (TextView) findViewById(R.id.tv_success);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content_two = (TextView) findViewById(R.id.tv_content_two);
        financial_submit_success_back.setOnClickListener(this);
        financial_submit_success_home.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.financial_submit_success_back:
                finish();
                break;
            case R.id.financial_submit_success_home:
                if(from_type.equals("3")){
                    finish();
                }else {
                    AppManager.getAppManager().finishActivity(CarDetailActivity.class);
                    AppManager.getAppManager().finishActivity(NewCarInfoAct.class);
                    AppManager.getAppManager().finishActivity(FindloweAct.class);
                    sendBroadcast(new Intent(ReceiverTool.REFRESHHOMEFRAGMENTMODULE));//返回首页
                    finish();
                }
                break;
        }
    }
}
